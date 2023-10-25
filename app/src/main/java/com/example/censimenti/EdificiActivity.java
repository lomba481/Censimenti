package com.example.censimenti;

import static com.example.censimenti.MainActivity.ref;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EdificiActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    Toolbar toolbar;
    private AdapterEdifici adapterEdifici;
    FloatingActionButton addButton;
    String key;
    static DatabaseReference dref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edifici);
        dref = FirebaseDatabase.getInstance().getReference("edificio");
        recyclerView = findViewById(R.id.edificiRecycler);
        key = getIntent().getStringExtra("key");
        Log.d("key","BVBCBVCBCVBCVBC    "+ key);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);

        FirebaseRecyclerOptions<Edificio> options
                = new FirebaseRecyclerOptions.Builder<Edificio>()
                .setQuery(dref, Edificio.class)
                .build();

        adapterEdifici = new AdapterEdifici(options);
        recyclerView.setAdapter(adapterEdifici);

        addButton = findViewById(R.id.addingBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EdificiActivity.this, AggiungiEdificio.class);
//                String key = dref.push().getKey();

                Edificio nuovoEdifico = new Edificio("", "");
                dref.child(key).setValue(nuovoEdifico);
                intent.putExtra("key", key);
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
