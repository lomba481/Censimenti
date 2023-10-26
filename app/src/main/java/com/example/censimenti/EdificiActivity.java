package com.example.censimenti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class EdificiActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterEdifici adapterEdifici;
    FloatingActionButton addButton;
    String keyCommessa, keyEdificio ;
    static DatabaseReference eRef;
    static DatabaseReference myeRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edifici);

        recyclerView = findViewById(R.id.edificiRecycler);
        keyCommessa = getIntent().getStringExtra("key");
        eRef = FirebaseDatabase.getInstance().getReference("edificio");
//        myeRef = FirebaseDatabase.getInstance().getReference("edificio");


         Query query = eRef.child(keyCommessa).orderByChild("key").equalTo(keyCommessa);

//

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);

        FirebaseRecyclerOptions<Edificio> options
                = new FirebaseRecyclerOptions.Builder<Edificio>()
                .setQuery(query, Edificio.class)
                .build();



        adapterEdifici = new AdapterEdifici(options);
        recyclerView.setAdapter(adapterEdifici);

        addButton = findViewById(R.id.addingBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EdificiActivity.this, AggiungiEdificio.class);
//                String key = eRef.push().getKey();

                Edificio nuovoEdifico = new Edificio("", "", "");
                keyEdificio = eRef.push().getKey();
                Log.d("zeta", keyEdificio);
                eRef.child(keyCommessa).child(keyEdificio).setValue(nuovoEdifico);
                intent.putExtra("keyCommessa", keyCommessa);
                intent.putExtra("keyEdificio", keyEdificio);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterEdifici.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterEdifici.startListening();
    }
}
