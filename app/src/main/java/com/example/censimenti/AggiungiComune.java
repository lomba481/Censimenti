package com.example.censimenti;

import static com.example.censimenti.ComuniActivity.ref;

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
import com.google.firebase.database.ValueEventListener;

public class AggiungiComune extends AppCompatActivity {

    TextInputEditText nomeComune, nCommessa;

    Button salva, indietro;
    String key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_comune);
        nomeComune = findViewById(R.id.nomeComune);
        nCommessa = findViewById(R.id.numCommessa);

        key = getIntent().getStringExtra("key");

        salva = findViewById(R.id.saveBtn);
        indietro = findViewById(R.id.backBtn);

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeComune.getText().toString();
                String commessa = nCommessa.getText().toString();
                Comune comune = new Comune();
                comune.setNome(nome);
                comune.setnCommessa(commessa);
                ref.child(key).setValue(comune);

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Toast.makeText(AggiungiComune.this, "dati aggiunti", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AggiungiComune.this, "Non riesco ad inserire i dati"+ error, Toast.LENGTH_SHORT).show();
                    }
                });
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
