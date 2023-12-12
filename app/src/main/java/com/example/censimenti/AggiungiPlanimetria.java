package com.example.censimenti;

import static com.example.censimenti.AdapterPlanimetrie.refPlanimetrie;
import static com.example.censimenti.AggiungiComune.storageC;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
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
    String keyPlanimetria, keyEdificio;

    RelativeLayout scegliImmagine;
    ImageView imageView;

    Uri uri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_planimetria);
        nomePlanimetria = findViewById(R.id.nomePlanimetria);

        keyPlanimetria = getIntent().getStringExtra("keyPlanimetria");

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
                    String url = snapshot.child("imageUrl").getValue(String.class);
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
                        .start();
            }
        });


        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomePlanimetria.getText().toString();
                Planimetria planimetria = new Planimetria();
                planimetria.setNome(nome);
                planimetria.setConteggio(0L);
                try {
                    addDataFirebase(planimetria);
                    finish();
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
                });
            }
        });
        refPlanimetrie.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(AggiungiPlanimetria.this, "Planimetria aggiunta con successo!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AggiungiPlanimetria.this, "Non riesco ad inserire i dati"+ error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
