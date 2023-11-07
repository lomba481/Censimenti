package com.example.censimenti;

import static com.example.censimenti.CensimentiInterni.lRef;
import static com.example.censimenti.CensimentiInterni.lref1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AggiungiLampade extends AppCompatActivity {

    TextInputEditText potenzaLampada, attaccoLampada;
    AutoCompleteTextView sorgenteLampada, modelloLampada;
    Button salva, indietro;
    String keyPlanimetria, keyLampada;
    RelativeLayout scegliImmagine;
    ImageView imageView;
    float x, y;

    Uri uri = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_lampada);

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int larghezzaSchermo = size.x;
        int altezzaSchermo = size.y;


        sorgenteLampada = findViewById(R.id.sorgenteLampada);
        modelloLampada = findViewById(R.id.modelloLampada);
        potenzaLampada = findViewById(R.id.potenzaLampada);
        attaccoLampada = findViewById(R.id.attaccoLampada);
        keyPlanimetria = getIntent().getStringExtra("keyPlanimetria");
        keyLampada = getIntent().getStringExtra("keyLampada");

        Log.d("dada", keyPlanimetria + " -- " + keyLampada);
        x = getIntent().getFloatExtra("x", 0);
        y = getIntent().getFloatExtra("y", 0);

        float Kx = larghezzaSchermo / x;
        float Ky = altezzaSchermo / y;

        salva = findViewById(R.id.saveBtn);
        indietro = findViewById(R.id.backBtn);
        scegliImmagine = findViewById(R.id.ScegliImmagine);
        imageView = findViewById(R.id.ImageView);

        String[] opzioniTipoLampada = {"Led", "Fluorescente", "Scarica"};
        String[] opzioniTipoApparecchio = {"Stagna", "Proiettore", "Incasso"};
        autoCompleteMenu(opzioniTipoLampada, sorgenteLampada);
        autoCompleteMenu(opzioniTipoApparecchio, modelloLampada);

        DatabaseReference lref2 = lref1.child(keyLampada);
        lref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String tipo = snapshot.child("tipo").getValue(String.class);
                    String sorgente = snapshot.child("sorgente").getValue(String.class);
                    String attacco = snapshot.child("attacco").getValue(String.class);
                    String potenza = snapshot.child("potenza").getValue(String.class);
                    sorgenteLampada.setText(sorgente);
                    modelloLampada.setText(tipo);
                    potenzaLampada.setText(potenza);
                    attaccoLampada.setText(attacco);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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


                // recupero i dati per poi eseguire salvataggio in firebase

                Lampada lampada = new Lampada(modelloLampada.getText().toString(),
                        sorgenteLampada.getText().toString(),
                        potenzaLampada.getText().toString(),
                        attaccoLampada.getText().toString(),
                        null,
                        keyPlanimetria,
                        x, y, Kx, Ky);
                try {
                    if (uri == null) {
                        lampada.setFoto("");
                        lRef.child(keyPlanimetria).child(keyLampada).setValue(lampada);
                        lRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Toast.makeText(AggiungiLampade.this, "dati aggiunti", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(AggiungiLampade.this, "Non riesco ad inserire i dati" + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                        //finish();



                    }
                    else {
                        addDataFirebase(lampada);
                        //finish();

                    }
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

        lRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(AggiungiLampade.this, "dati aggiunti", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AggiungiLampade.this, "Non riesco ad inserire i dati" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void autoCompleteMenu(String[] elenco, AutoCompleteTextView autoCompleteTextView) {

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.drop_down_layout, elenco);
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

}
