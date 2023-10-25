package com.example.censimenti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Toolbar toolbar;
    private RCAdapter rcAdapter;
    FloatingActionButton addButton;
    String key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("comune");

        recyclerView = findViewById(R.id.comuneRecycler);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);

        FirebaseRecyclerOptions<Comune> options
                = new FirebaseRecyclerOptions.Builder<Comune>()
                .setQuery(ref, Comune.class)
                .build();
        rcAdapter = new RCAdapter(options);
        recyclerView.setAdapter(rcAdapter);

        addButton = findViewById(R.id.addingBtn);



    }

    @Override
    protected void onStart() {
        super.onStart();
        rcAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        rcAdapter.startListening();
    }
}