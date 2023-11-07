package com.example.censimenti;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdapterComuni extends FirebaseRecyclerAdapter<Comune, AdapterComuni.comuniviewHolder> {
    private static DatabaseReference myRef;
    CardView cardView;
    ImageView puntini;
    Context context;


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

        myRef = FirebaseDatabase.getInstance().getReference("comune");
        DatabaseReference itemRef = getRef(holder.getAbsoluteAdapterPosition());
        String chiave = itemRef.getKey();

        puntini.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Cosa vuoi fare?");
                builder.setPositiveButton("Modifica", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String chiave1 = dataSnapshot.getKey();
                                    if (chiave == chiave1) {
                                        String nome = dataSnapshot.child("nome").getValue(String.class);
                                        String numCommessa = dataSnapshot.child("numCommessa").getValue(String.class);
                                        Intent intent = new Intent(context.getApplicationContext(), AggiungiComune.class);
                                        intent.putExtra("nome", nome);
                                        intent.putExtra("numCommessa", numCommessa);
                                        context.startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {


                            }
                        });

                    }
                });
                builder.setNegativeButton("Elimina", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemRef.removeValue();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Copiato da Seba

                Intent intent = new Intent(context.getApplicationContext(), EdificiActivity.class);
                intent.putExtra("key", chiave);
                context.startActivity(intent);

            }
        });

    }

    public class comuniviewHolder extends RecyclerView.ViewHolder {
        TextView nome, nCommessa;
        public comuniviewHolder(View itemView) {
            super(itemView);
            puntini = itemView.findViewById(R.id.puntini_comune);
            context = itemView.getContext();
            nome = itemView.findViewById(R.id.comune);
            nCommessa = itemView.findViewById(R.id.nCommessa);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }
}
