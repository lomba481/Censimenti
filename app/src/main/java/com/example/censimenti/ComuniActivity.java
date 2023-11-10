package com.example.censimenti;

import static com.example.censimenti.AdapterComuni.refComuni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.censimenti.Utenti.UtentiActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class ComuniActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Toolbar toolbar;
    private AdapterComuni adapterComuni;
    FloatingActionButton addButton, gestioneUtenti;
    String key;

    String username;

//    static DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ref = FirebaseDatabase.getInstance().getReference("comune");

        username = getIntent().getStringExtra("nomeCognome");

        refComuni = FirebaseDatabase.getInstance().getReference("Comuni");
        recyclerView = findViewById(R.id.comuneRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);

        // qui ci va messa la query
//        Query query = refComuni;



        FirebaseRecyclerOptions<Comune> options
                = new FirebaseRecyclerOptions.Builder<Comune>()
                .setQuery(refComuni, Comune.class)
                .build();
        adapterComuni = new AdapterComuni(options);
        recyclerView.setAdapter(adapterComuni);


        addButton = findViewById(R.id.addingBtn);
        gestioneUtenti = findViewById(R.id.gestisciUtenti);

//        Quando clicco sul bottone per aggiungere un comune
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Salvo la key del nuovo comune appena clicco sul "+"
                String key = refComuni.push().getKey();

//                Passo all'activity in cui posso personalizzare il comune che voglio aggiungere
                Intent intent = new Intent(ComuniActivity.this, AggiungiComune.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });

        gestioneUtenti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComuniActivity.this, UtentiActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterComuni.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterComuni.startListening();
    }
}