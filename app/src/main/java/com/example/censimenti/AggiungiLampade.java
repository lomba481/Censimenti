package com.example.censimenti;

import static com.example.censimenti.CensimentiInterni.lRef;
import static com.example.censimenti.PlanimetrieActivity.pRef;

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AggiungiLampade extends AppCompatActivity {

    TextInputEditText nomeLampada, descrLampada;
    Button salva, indietro;
    String keyPlanimetria, keyLampada;
    RelativeLayout scegliImmagine;
    ImageView imageView;
    float x, y;

    Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_lampada);

        nomeLampada = findViewById(R.id.nomeLampada);
        descrLampada = findViewById(R.id.descrLampada);
        keyPlanimetria = getIntent().getStringExtra("keyPlanimetria");
        keyLampada = getIntent().getStringExtra("keyLampada");
        x = getIntent().getFloatExtra("x", 0);
        y = getIntent().getFloatExtra("y", 0);


        salva = findViewById(R.id.saveBtn);
        indietro = findViewById(R.id.backBtn);
        scegliImmagine = findViewById(R.id.ScegliImmagine);
        imageView = findViewById(R.id.ImageView);

        scegliImmagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AggiungiLampade.this)
                        .cameraOnly()
                        .start();
            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeLampada.getText().toString();
                String descr = descrLampada.getText().toString();
                Lampada lampada = new Lampada();
                lampada.setNome(nome);
                lampada.setDescrizione(descr);
                lampada.setKey(keyPlanimetria);
                lampada.setY(y);
                lampada.setX(x);
                try {
                    addDataFirebase(lampada);
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

    private void addDataFirebase(Lampada lampada) throws FileNotFoundException {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("Lampade");
        String timestamp = Long.toString(System.currentTimeMillis());
        StorageReference imageRef = storageReference.child(timestamp);

        InputStream stream = getContentResolver().openInputStream(uri);

        UploadTask uploadTask = imageRef.putStream(stream);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    String imageUrl = downloadUri.toString();
                    lampada.setFoto(imageUrl);
                    lRef.child(keyPlanimetria).child(keyLampada).setValue(lampada);
                });
            }
        });
        pRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(AggiungiLampade.this, "dati aggiunti", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AggiungiLampade.this, "Non riesco ad inserire i dati"+ error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
