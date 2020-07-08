package com.example.acer.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Ingredients extends RecyclerView.Adapter<Ingredients.MyViewHolder> {

    Context context;
    ArrayList<IngredientsModel> models_ingre;

    public Ingredients(Context context, ArrayList<IngredientsModel> models_ingre) {
        this.context = context;
        this.models_ingre = models_ingre;
    }

    @NonNull
    @Override
    public Ingredients.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_list_content, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Ingredients.MyViewHolder myViewHolder, int i) {

        IngredientsModel ingredientsModel=models_ingre.get(i);
        myViewHolder.mIngreView.setText( ingredientsModel.quantity+"\t"+
                ingredientsModel.measure+"\t"+ingredientsModel.ingredient);

    }

    @Override
    public int getItemCount() {
        return models_ingre.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mIngreView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mIngreView =  itemView.findViewById(R.id.id_text);
        }
    }
}
