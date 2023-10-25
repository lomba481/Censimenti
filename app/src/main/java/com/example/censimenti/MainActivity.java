package com.example.censimenti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Toolbar toolbar;
    private AdapterComuni adapterComuni;
    FloatingActionButton addButton;
    String key;

    static DatabaseReference ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ref = FirebaseDatabase.getInstance().getReference("comune");

        recyclerView = findViewById(R.id.comuneRecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);

        FirebaseRecyclerOptions<Comune> options
                = new FirebaseRecyclerOptions.Builder<Comune>()
                .setQuery(ref, Comune.class)
                .build();
        adapterComuni = new AdapterComuni(options);
        recyclerView.setAdapter(adapterComuni);

        addButton = findViewById(R.id.addingBtn);

//        Quando clicco sul bottone per aggiungere un comune
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Salvo la key del nuovo comune appena clicco sul "+"
                String key = ref.push().getKey();

//                Creo nuovo oggetto Comune
                Comune nuovoComune = new Comune("", "");

//                Lo inserisco nel database
                ref.child(key).setValue(nuovoComune);

//                Passo all'activity in cui posso personalizzare il comune che voglio aggiungere
                Intent intent = new Intent(MainActivity.this, AggiungiComune.class);
                intent.putExtra("key", key);
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