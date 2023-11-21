package com.example.censimenti;

import static com.example.censimenti.AdapterComuni.refComuni;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AggiungiComune extends AppCompatActivity {
    TextInputEditText nomeComune, nCommessa;
    TextView elencoUtenti;

    Button salva, indietro;
    String key;
    boolean[] selectUtente;
    ArrayList<Integer> utentiList = new ArrayList<>();

    int k = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_comune);
        nomeComune = findViewById(R.id.nomeComune);
        nCommessa = findViewById(R.id.numCommessa);
        elencoUtenti = findViewById(R.id.selezionaUtenti);
        key = getIntent().getStringExtra("key");
        salva = findViewById(R.id.saveBtn);
        indietro = findViewById(R.id.backBtn);

        DatabaseReference cRef = refComuni.child(key);
        cRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nome = snapshot.child("nome").getValue(String.class);
                    String num = snapshot.child("nCommessa").getValue(String.class);
                    nomeComune.setText(nome);
                    nCommessa.setText(num);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //String[] utentiArray = {"utente1", "utente2", "utente3", "utente4", "utente5"};
        elencoUte(new FirebaseCallback() {
            @Override
            public void onCallback(String[] utentiArray) {
                selectUtente = new boolean[utentiArray.length];
                elencoUtenti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                AggiungiComune.this);
                        //setta titolo
                        builder.setTitle("Seleziona utenti");
                        builder.setCancelable(false);
                        builder.setMultiChoiceItems(utentiArray, selectUtente, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                if (b) {
                                    utentiList.add(i);
                                    Collections.sort(utentiList);
                                } else {
                                    utentiList.remove(i);
                                }
                            }
                        });

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int j = 0; j < utentiList.size(); j++) {
                                    stringBuilder.append(utentiArray[utentiList.get(j)]);

                                    if (j != utentiList.size() - 1) {
                                        stringBuilder.append(", ");
                                    }
                                }
                                elencoUtenti.setText(stringBuilder.toString());
                            }
                        });

                        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });

                        builder.setNeutralButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (int j = 0; j < selectUtente.length; j++) {
                                    selectUtente[j] = false;
                                    utentiList.clear();
                                    elencoUtenti.setText("");
                                }

                            }
                        });

                        builder.show();
                    }

                });

            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeComune.getText().toString();
                String commessa = nCommessa.getText().toString();
                Comune comune = new Comune();
                comune.setNome(nome);
                comune.setnCommessa(commessa);

                String testo = elencoUtenti.getText().toString();
                String [] strings = testo.split(", ");
                List<String>stringList = new ArrayList<>(Arrays.asList(strings));

                refComuni.child(key).setValue(comune);
                for (int i=0;i<stringList.size();i++){
                    refComuni.child(key).child("utenti").push().setValue(stringList.get(i));
                }



                refComuni.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Toast.makeText(AggiungiComune.this, "dati aggiunti", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AggiungiComune.this, "Non riesco ad inserire i dati" + error, Toast.LENGTH_SHORT).show();
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

    public void elencoUte(final FirebaseCallback callback) {
        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("utenti");

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> stringList = new ArrayList<String>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    stringList.add(ds.child("nome").getValue(String.class) + " " +
                            ds.child("cognome").getValue(String.class));
                }
                String[] dataArray = stringList.toArray(new String[0]);
                callback.onCallback(dataArray);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci eventuali errori
            }
        });
    }

    public interface FirebaseCallback {
        void onCallback(String[] data);
    }


}
