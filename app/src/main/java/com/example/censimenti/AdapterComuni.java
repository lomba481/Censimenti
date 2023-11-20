package com.example.censimenti;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AdapterComuni extends FirebaseRecyclerAdapter<AggiungiComune.Comune, AdapterComuni.comuniviewHolder> {
    static DatabaseReference refComuni;
    CardView cardView;
    ImageView puntini;
    Context context;


    public AdapterComuni(FirebaseRecyclerOptions<AggiungiComune.Comune> options) {
        super(options);
    }


    @NonNull
    @Override
    public comuniviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new AdapterComuni.comuniviewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull comuniviewHolder holder, int position, @NonNull AggiungiComune.Comune model) {
        holder.nome.setText(model.getNome());
        holder.nCommessa.setText(model.getnCommessa());

//        refComuni = FirebaseDatabase.getInstance().getReference("comune");
        DatabaseReference itemRef = getRef(holder.getAbsoluteAdapterPosition());
        String chiave = itemRef.getKey();

        puntini.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Cosa vuoi fare?");
                builder.setPositiveButton("Modifica", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        refComuni.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String chiave1 = dataSnapshot.getKey();
                                    if (chiave == chiave1) {
                                        Intent intent = new Intent(context.getApplicationContext(), AggiungiComune.class);
                                        intent.putExtra("key", chiave);
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
                        refComuni.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Toast.makeText(context.getApplicationContext(), "Eliminato con successo", Toast.LENGTH_SHORT).show();
//                                LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());
//                                View layout = inflater.inflate(R.layout.custom_toast, null);
//                                Toast toast = new Toast(context.getApplicationContext());
//                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                                toast.setView(layout);
//                                toast.show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

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
