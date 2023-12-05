package com.example.censimenti;

import static com.example.censimenti.AdapterEdifici.refEdifici;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AggiungiEdificio extends AppCompatActivity {
    TextInputEditText nomeEdificio, indirizzoEdificio;

    Button salva, indietro;
    String  keyEdificio;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_edificio);

        nomeEdificio = findViewById(R.id.nomeEdificio);
        indirizzoEdificio = findViewById(R.id.indirizzo);

        keyEdificio = getIntent().getStringExtra("keyEdificio");

        salva = findViewById(R.id.saveBtn);
        indietro = findViewById(R.id.backBtn);

        DatabaseReference eRef = refEdifici.child(keyEdificio);
        eRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nome = snapshot.child("nome").getValue(String.class);
                    String indirizzo = snapshot.child("indirizzo").getValue(String.class);
                    nomeEdificio.setText(nome);
                    indirizzoEdificio.setText(indirizzo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeEdificio.getText().toString();
                String indirizzo = indirizzoEdificio.getText().toString();
                Edificio edificio = new Edificio();
                edificio.setNome(nome);
                edificio.setIndirizzo(indirizzo);
                refEdifici.child(keyEdificio).setValue(edificio);


                refEdifici.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Toast.makeText(AggiungiEdificio.this, "Edificio aggiunto con successo!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AggiungiEdificio.this, "Non riesco ad inserire i dati"+ error, Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
            }
        });

        indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
