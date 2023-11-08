package com.example.censimenti;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;

public class AdapterPlanimetrie extends FirebaseRecyclerAdapter<Planimetria, AdapterPlanimetrie.planimetrieViewHolder> {
     static DatabaseReference refPlanimetrie;
    CardView cardView;
    Context context;
    String imageUrl;
    ImageView puntini;

    public AdapterPlanimetrie(@NonNull FirebaseRecyclerOptions<Planimetria> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull AdapterPlanimetrie.planimetrieViewHolder holder, int position, @NonNull Planimetria model) {
        holder.nome.setText(model.getNome());
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


                        refPlanimetrie.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String chiave1 = dataSnapshot.getKey();
                                    if (chiave == chiave1) {
                                        Intent intent = new Intent(context.getApplicationContext(), AggiungiPlanimetria.class);
                                        intent.putExtra("keyPlanimetria", chiave);
                                        Log.d("dove", "modifica");
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
                DatabaseReference itemRefImage = itemRef.child("imageUrl");

                itemRefImage.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        imageUrl = snapshot.getValue(String.class);

                        Intent intent = new Intent(context.getApplicationContext(), CensimentiInterni.class);
                        intent.putExtra("imageUrl", imageUrl);
                        intent.putExtra("key", chiave);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    @NonNull
    @Override
    public AdapterPlanimetrie.planimetrieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planimetria_recyclerview, parent, false);
        return new AdapterPlanimetrie.planimetrieViewHolder(view);
    }

    public class planimetrieViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        public planimetrieViewHolder(View itemView) {
            super(itemView);
            puntini = itemView.findViewById(R.id.puntini_planimetria);
            context = itemView.getContext();
            nome = itemView.findViewById(R.id.piano);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
