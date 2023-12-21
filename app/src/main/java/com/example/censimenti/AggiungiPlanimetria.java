package com.example.censimenti;

import static com.example.censimenti.AdapterPlanimetrie.refPlanimetrie;
import static com.example.censimenti.AggiungiComune.storageC;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;

public class AggiungiPlanimetria extends AppCompatActivity {
    TextInputEditText nomePlanimetria;
    Button salva, indietro;
    String keyPlanimetria, modifica;

    RelativeLayout scegliImmagine;
    CircularProgressIndicator progressIndicator;
    ImageView imageView;
    TextView textView;
    String url = null;

    Uri uri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_planimetria);
        nomePlanimetria = findViewById(R.id.nomePlanimetria);
        textView = findViewById(R.id.textPlanimetria);
        progressIndicator = findViewById(R.id.progressIndicator);

        keyPlanimetria = getIntent().getStringExtra("keyPlanimetria");
        Log.d("dede", keyPlanimetria);

        modifica = getIntent().getStringExtra("modifica");
        if (modifica == null) {
            textView.setText("Aggiungi Planimetria");
        }
        else {
            textView.setText("Modifica Planimetria");
        }

        salva = findViewById(R.id.saveBtn);
        indietro = findViewById(R.id.backBtn);
        scegliImmagine = findViewById(R.id.ScegliImmagine);
        imageView = findViewById(R.id.ImageView);

        DatabaseReference pRef = refPlanimetrie.child(keyPlanimetria);
        pRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nome = snapshot.child("nome").getValue(String.class);
                    url = snapshot.child("imageUrl").getValue(String.class);

                    if(url != null){
                        loadUrlAsDrawable(url, getApplicationContext(), new Icon.OnDrawableLoadedListener() {
                            @Override
                            public void onDrawableLoaded(Drawable d) {
                                imageView.setImageDrawable(d);
                            }
                        });

                    }

                    nomePlanimetria.setText(nome);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        scegliImmagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AggiungiPlanimetria.this)
                        .maxResultSize(750, 750)
                        .start();
            }
        });


        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressIndicator.setVisibility(View.VISIBLE);

                try {
                    Planimetria planimetria = new Planimetria(nomePlanimetria.getText().toString(), url, 0L);
                    if (uri == null) {

                        refPlanimetrie.child(keyPlanimetria).setValue(planimetria);
                        refPlanimetrie.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Toast.makeText(AggiungiPlanimetria.this, "Planimetria aggiunta con successo!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(AggiungiPlanimetria.this, "Non riesco ad inserire i dati"+ error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        addDataFirebase(planimetria);
                    }


                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        imageView.setImageURI(uri);
    }
    private void addDataFirebase(Planimetria planimetria) throws FileNotFoundException {

        UploadTask uploadTask = storageC.child(keyPlanimetria).putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    String imageUrl = downloadUri.toString();
                    planimetria.setImageUrl(imageUrl);
                    refPlanimetrie.child(keyPlanimetria).setValue(planimetria);
                    refPlanimetrie.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Toast.makeText(AggiungiPlanimetria.this, "Planimetria aggiunta con successo!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AggiungiPlanimetria.this, "Non riesco ad inserire i dati"+ error, Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });

    }
    public void loadUrlAsDrawable(String url, Context context, final Icon.OnDrawableLoadedListener listener) {
        Glide.with(context)
                .load(url)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        listener.onDrawableLoaded(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}
