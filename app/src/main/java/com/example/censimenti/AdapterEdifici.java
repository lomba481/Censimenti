package com.example.censimenti;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class AdapterEdifici extends FirebaseRecyclerAdapter<Edificio, AdapterEdifici.edificiViewHolder> {

    CardView cardView;
    Context context;
    private List<String> lista = new ArrayList<String>();
    private String key = null;
    private int posizione = 0;
    private Edificio edificio;

    public AdapterEdifici(@NonNull FirebaseRecyclerOptions<Edificio> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull AdapterEdifici.edificiViewHolder holder, int position, @NonNull Edificio model) {
        holder.nome.setText(model.getNome());
        holder.indirizzo.setText(model.getIndirizzo());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), PlanimetrieActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public AdapterEdifici.edificiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new AdapterEdifici.edificiViewHolder(view);
    }

    public class edificiViewHolder extends RecyclerView.ViewHolder {
        TextView nome, indirizzo;
        public edificiViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            nome = itemView.findViewById(R.id.comune);
            indirizzo = itemView.findViewById(R.id.nCommessa);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }
}
