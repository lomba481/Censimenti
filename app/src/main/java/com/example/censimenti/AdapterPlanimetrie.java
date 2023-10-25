package com.example.censimenti;

import android.content.Context;
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

public class AdapterPlanimetrie extends FirebaseRecyclerAdapter<Planimetria, AdapterPlanimetrie.planimetrieViewHolder> {

    CardView cardView;
    Context context;
    private List<String> lista = new ArrayList<String>();
    private String key = null;
    private int posizione = 0;
    private Edificio edificio;

    public AdapterPlanimetrie(@NonNull FirebaseRecyclerOptions<Planimetria> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull AdapterPlanimetrie.planimetrieViewHolder holder, int position, @NonNull Planimetria model) {
        holder.nome.setText(model.getNome());
    }

    @NonNull
    @Override
    public AdapterPlanimetrie.planimetrieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new AdapterPlanimetrie.planimetrieViewHolder(view);
    }

    public class planimetrieViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        public planimetrieViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            nome = itemView.findViewById(R.id.comune);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
