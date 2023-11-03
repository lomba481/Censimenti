package com.example.censimenti.Utenti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.censimenti.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UtentiActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUtenti;
    Toolbar toolbar;
    private AdapterUtente adapterUtenti;

    static DatabaseReference ref;

    FloatingActionButton addUtente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ref = FirebaseDatabase.getInstance().getReference("utenti");
        Log.d("seba", "" + ref);

        recyclerViewUtenti = findViewById(R.id.utenteRecycler);

        recyclerViewUtenti.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUtenti.setItemAnimator(null);

        FirebaseRecyclerOptions<Utente> options
                = new FirebaseRecyclerOptions.Builder<Utente>()
                .setQuery(ref, Utente.class)
                .build();
        adapterUtenti = new AdapterUtente(options);
        recyclerViewUtenti.setAdapter(adapterUtenti);

        addUtente = findViewById(R.id.addUtente);

        addUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UtentiActivity.this, AggiungiUtente.class);
                startActivity(intent);
            }
        });
//        gestioneUtenti = findViewById(R.id.gestisciUtenti);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterUtenti.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterUtenti.startListening();
    }
}