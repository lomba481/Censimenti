package com.example.censimenti.Utenti;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.censimenti.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdapterUtente extends FirebaseRecyclerAdapter<Utente, AdapterUtente.utentiviewHolder> {
    private static DatabaseReference myRef;
    CardView cardView;
    Context context;

    private List<String> lista = new ArrayList<String>();
    private String key = null;
    private int posizione = 0;
    private Utente utente;

    public AdapterUtente(FirebaseRecyclerOptions<Utente> options) {
        super(options);
    }


    @NonNull
    @Override
    public utentiviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utente_recyclerview, parent, false);
        return new AdapterUtente.utentiviewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull utentiviewHolder holder, int position, @NonNull Utente model) {
        holder.nome.setText(model.getNome() + " " + model.getCognome());
        holder.email.setText(model.getMail());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Copiato da Seba
                myRef = FirebaseDatabase.getInstance().getReference("utenti");
                DatabaseReference itemRef = getRef(holder.getAbsoluteAdapterPosition());
                String chiave = itemRef.getKey();
                Log.d("chiave", chiave);
//                posizione = holder.getAbsoluteAdapterPosition();


//                Intent intent = new Intent(context.getApplicationContext(), EdificiActivity.class);
//                intent.putExtra("key", chiave);
//                context.startActivity(intent);
            }
        });

    }

    public class utentiviewHolder extends RecyclerView.ViewHolder {
        TextView nome, email;

        public utentiviewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            nome = itemView.findViewById(R.id.utente);
            email = itemView.findViewById(R.id.email);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }
}
