package com.example.censimenti;

import android.content.Intent;
import android.os.Bundle;
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

public class PlanimetrieActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterPlanimetrie adapterPlanimetrie;
    FloatingActionButton addButton;
    String keyPlanimetria, keyEdificio ;

    static DatabaseReference pRef;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planimetrie);
        pRef = FirebaseDatabase.getInstance().getReference("planimetrie");
        recyclerView = findViewById(R.id.planimetrieRecycler);
        keyEdificio = getIntent().getStringExtra("key");


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        Query query = pRef.child(keyEdificio).orderByChild("key").equalTo(keyEdificio);

        FirebaseRecyclerOptions<Planimetria> options
                = new FirebaseRecyclerOptions.Builder<Planimetria>()
                .setQuery(query, Planimetria.class)
                .build();

        adapterPlanimetrie = new AdapterPlanimetrie(options);
        recyclerView.setAdapter(adapterPlanimetrie);

        addButton = findViewById(R.id.addingBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanimetrieActivity.this, AggiungiPlanimetria.class);
//                String key = dref.push().getKey();

                Planimetria nuovaPlanimetria = new Planimetria("", "", "");
                keyPlanimetria= pRef.push().getKey();
                pRef.child(keyEdificio).child(keyPlanimetria).setValue(nuovaPlanimetria);
                intent.putExtra("keyEdificio", keyEdificio);
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
