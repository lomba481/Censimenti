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

public class AdapterPlanimetrie extends FirebaseRecyclerAdapter<Planimetria, AdapterPlanimetrie.planimetrieViewHolder> {
    private static DatabaseReference myRef;
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

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                myRef = FirebaseDatabase.getInstance().getReference("planimetrie");
//                DatabaseReference itemRef = getRef(holder.getAbsoluteAdapterPosition());
//                String chiave = itemRef.getKey();
//                Log.d("chiave", chiave);
////                posizione = holder.getAbsoluteAdapterPosition();
//
//
//                Intent intent = new Intent(context.getApplicationContext(), EdificiActivity.class);
//                intent.putExtra("key", chiave);
//                context.startActivity(intent);
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
            context = itemView.getContext();
            nome = itemView.findViewById(R.id.piano);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
