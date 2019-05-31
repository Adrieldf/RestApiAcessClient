package com.ucs.adriel.restapiacessclient.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.ucs.adriel.restapiacessclient.MainActivity;
import com.ucs.adriel.restapiacessclient.R;
import com.ucs.adriel.restapiacessclient.editCards;
import com.ucs.adriel.restapiacessclient.model.Card;

import java.util.List;

public class MtgAdapter extends RecyclerView.Adapter<MtgAdapter.ViewHolder> {
    private List<Card> cards;
    public MtgAdapter(List<Card> cards) {
        this.cards = cards;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,viewGroup,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position)
    {
        viewHolder.setData(cards.get(position));
    }
    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtDescription;
        private TextView txtTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtDescription = itemView.findViewById(R.id.lblDescription);
            txtTitle = itemView.findViewById(R.id.lblTitle);
        }
        private void setData(Card card) {
            txtTitle.setText(card.getName() + " - " + card.getType());
            txtDescription.setText(card.getText());
        }
        public void onClick(View view) {
            Toast.makeText(view.getContext(),"Você selecionou " + cards.get(getLayoutPosition()).getName(),Toast.LENGTH_LONG).show();
         //   Intent intent = new Intent(MainActivity.this, editCards.class);
          //  intent.putExtra("ID", mainCards.get(viewHolder.getAdapterPosition()).getId());
         //   startActivity(intent);
        }
    }
}