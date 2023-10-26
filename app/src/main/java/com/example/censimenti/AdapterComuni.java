package com.example.censimenti;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdapterComuni extends FirebaseRecyclerAdapter<Comune, AdapterComuni.comuniviewHolder> {
    private static DatabaseReference myRef;
    CardView cardView;
    Context context;

    private List<String> lista = new ArrayList<String>();
    private String key = null;
    private int posizione = 0;
    private Comune comune;
    public AdapterComuni(FirebaseRecyclerOptions<Comune> options) {
        super(options);
    }


    @NonNull
    @Override
    public comuniviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new AdapterComuni.comuniviewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull comuniviewHolder holder, int position, @NonNull Comune model) {
        holder.nome.setText(model.getNome());
        holder.nCommessa.setText(model.getnCommessa());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Copiato da Seba
                myRef = FirebaseDatabase.getInstance().getReference("comune");
                DatabaseReference itemRef = getRef(holder.getAbsoluteAdapterPosition());
                String chiave = itemRef.getKey();
                Log.d("chiave", chiave);
//                posizione = holder.getAbsoluteAdapterPosition();


                Intent intent = new Intent(context.getApplicationContext(), EdificiActivity.class);
                intent.putExtra("key", chiave);
                context.startActivity(intent);



//                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot data : snapshot.getChildren()) {
//                            key = data.getKey();
//                            Log.d("zeta", key);
//                            lista.add(key);
//                        }
////                        for (int i =0; i<lista.size();i++) {
////                            Log.d("zeta" + lista.);
////                        }
//                        //String chiave = lista.get(posizione);
////                        Log.d("zeta", lista.get(posizione));
////                        Intent intent = new Intent(context.getApplicationContext(), EdificiActivity.class);
////                        intent.putExtra("key", chiave);
////                        context.startActivity(intent);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

//                Questo codice funziona, ma ti fa passare all'activity degli edifici senza prendere la chiave (cosa che invece serve)
//                Intent intent = new Intent(context.getApplicationContext(), EdificiActivity.class);
//                intent.putExtra("key", key);
//                context.startActivity(intent);
            }
        });

    }

    public class comuniviewHolder extends RecyclerView.ViewHolder {
        TextView nome, nCommessa;
        public comuniviewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            nome = itemView.findViewById(R.id.comune);
            nCommessa = itemView.findViewById(R.id.nCommessa);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }
}
