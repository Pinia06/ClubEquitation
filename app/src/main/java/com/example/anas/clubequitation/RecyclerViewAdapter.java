package com.example.anas.clubequitation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Item> itemList;
    private Item currentObject;
    private Context mcontext;

    public RecyclerViewAdapter(Context mcontext,ArrayList<Item> itemList) {
        this.itemList = itemList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        currentObject = itemList.get(i);

        viewHolder.dateDebutTextView.setText(currentObject.getDateDebutJob());
        viewHolder.nomJobTextView.setText(currentObject.getNomJob());
        viewHolder.dateFinTextView.setText(currentObject.getDateFinJob());
        viewHolder.heureDebutTextView.setText(currentObject.getHeureDebutJob());
        viewHolder.heureFinTextView.setText(currentObject.getHeureFinJob());
        viewHolder.salarieTextView.setText(currentObject.getNomPersonne());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

         TextView dateDebutTextView;
         TextView nomJobTextView;
         TextView dateFinTextView;
         TextView heureDebutTextView;
         TextView heureFinTextView;
         TextView salarieTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateDebutTextView = itemView.findViewById(R.id.tv_date_debut);
            nomJobTextView = itemView.findViewById(R.id.tv_nom_type);
            dateFinTextView = itemView.findViewById(R.id.tv_date_fin);
            heureDebutTextView = itemView.findViewById(R.id.tv_heure_debut);
            heureFinTextView = itemView.findViewById(R.id.tv_heure_fin);
            salarieTextView = itemView.findViewById(R.id.tv_salarie);

        }
    }
}
