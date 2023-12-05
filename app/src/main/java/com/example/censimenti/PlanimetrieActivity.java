package com.example.censimenti;

import static com.example.censimenti.AdapterEdifici.refEdifici;
import static com.example.censimenti.AdapterPlanimetrie.refPlanimetrie;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;

public class PlanimetrieActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterPlanimetrie adapterPlanimetrie;
    FloatingActionButton addButton;
    String keyPlanimetria, keyEdificio ;

    static StorageReference storagePlanimetrie;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planimetrie);
        recyclerView = findViewById(R.id.planimetrieRecycler);
        keyEdificio = getIntent().getStringExtra("key");

        refPlanimetrie = refEdifici.child(keyEdificio).child("Planimetrie");


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);

        FirebaseRecyclerOptions<Planimetria> options
                = new FirebaseRecyclerOptions.Builder<Planimetria>()
                .setQuery(refPlanimetrie, Planimetria.class)
                .build();

        adapterPlanimetrie = new AdapterPlanimetrie(options);
        recyclerView.setAdapter(adapterPlanimetrie);

        addButton = findViewById(R.id.addingBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanimetrieActivity.this, AggiungiPlanimetria.class);

                keyPlanimetria = refPlanimetrie.push().getKey();
                intent.putExtra("keyPlanimetria", keyPlanimetria);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterPlanimetrie.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterPlanimetrie.startListening();
    }
}
