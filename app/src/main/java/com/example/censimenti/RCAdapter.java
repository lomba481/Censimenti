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
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class RCAdapter extends FirebaseRecyclerAdapter<Comune, RCAdapter.comuniviewHolder> {
    private static DatabaseReference myRef;
    CardView cardView;
    Context context;

    private List<String> lista = new ArrayList<>();
    private String key = null;
    private int posizione = 0;
    private Comune comune;
    public RCAdapter(FirebaseRecyclerOptions<Comune> options) {
        super(options);
    }


    @NonNull
    @Override
    public comuniviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new RCAdapter.comuniviewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull comuniviewHolder holder, int position, @NonNull Comune model) {
        holder.nome.setText(model.getNome());
        holder.nCommessa.setText(model.getnCommessa());

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
