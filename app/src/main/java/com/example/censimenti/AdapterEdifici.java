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
import com.google.firebase.database.ValueEventListener;

public class AdapterEdifici extends FirebaseRecyclerAdapter<Edificio, AdapterEdifici.edificiViewHolder> {
    static DatabaseReference refEdifici;
    CardView cardView;
    Context context;
    ImageView puntini;
    String keyCommessa;


    public AdapterEdifici(@NonNull FirebaseRecyclerOptions<Edificio> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull AdapterEdifici.edificiViewHolder holder, int position, @NonNull Edificio model) {
        holder.nome.setText(model.getNome());
        holder.indirizzo.setText(model.getIndirizzo());
//        refEdifici.child("Edifici");
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

                        refEdifici.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String chiave1 = dataSnapshot.getKey();
                                    if (chiave == chiave1) {
                                        Intent intent = new Intent(context.getApplicationContext(), AggiungiEdificio.class);
                                        intent.putExtra("keyEdificio", chiave);
                                        intent.putExtra("modifica", "si");
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

                Intent intent = new Intent(context.getApplicationContext(), PlanimetrieActivity.class);
                intent.putExtra("key", chiave);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public AdapterEdifici.edificiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edificio_recyclerview, parent, false);
        return new AdapterEdifici.edificiViewHolder(view);
    }

    public class edificiViewHolder extends RecyclerView.ViewHolder {
        TextView nome, indirizzo;
        public edificiViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            puntini = itemView.findViewById(R.id.puntini_edificio);
            nome = itemView.findViewById(R.id.edificio);
            indirizzo = itemView.findViewById(R.id.indirizzo);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }
}
