package com.example.acer.bakingapp;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    Context context;
    ArrayList<ModelClass> modelnames ;
    int[] img;
    public  static String id;

    public ListAdapter(Context context, ArrayList<ModelClass> modelnames, int[] img) {

        this.context = context;
        this.modelnames = modelnames;
        this.img = img;

    }

    @NonNull
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(context).inflate(R.layout.design,viewGroup,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.MyViewHolder myViewHolder, final int i) {
        Picasso.with(context).load(img[i]).into(myViewHolder.iv );
        ModelClass mc=modelnames.get(i);
        myViewHolder.tv.setText(mc.recipie_name);
        myViewHolder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ItemListActivity.class);
                id= String.valueOf(i);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        ImageView iv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.recipenames);
            iv=itemView.findViewById(R.id.imageview);

        }
    }
}