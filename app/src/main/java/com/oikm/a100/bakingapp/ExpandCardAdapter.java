package com.oikm.a100.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.oikm.a100.bakingapp.Model.Ingredients;

import java.util.List;

public class ExpandCardAdapter extends RecyclerView.Adapter<ExpandCardAdapter.ViewHolder> {
    private List<Ingredients> bakeries;
    private final OnClickListener clickListener;
    public ExpandCardAdapter(List<Ingredients> bakery, OnClickListener clickListener){
        this.bakeries = bakery;
        this.clickListener = clickListener;
    }
    public interface OnClickListener{
        void onStepsClick(int i);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutID = R.layout.expand_list_item;
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToRoot = false;
        View view = inflater.inflate(layoutID,viewGroup, attachToRoot);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredient;
        public TextView measure;
        public TextView quantity;
        public Button stepsBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.ingredientsText);
            measure = itemView.findViewById(R.id.measureText);
            quantity = itemView.findViewById(R.id.quantityText);
            stepsBtn = itemView.findViewById(R.id.stepsBtn);
            stepsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getAdapterPosition();
                    clickListener.onStepsClick(i);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String ingredient = bakeries.get(i).getIngredient();
        String measure = bakeries.get(i).getMeasure();
        String quantity = String.valueOf(bakeries.get(i).getQuantity());
        viewHolder.ingredient.setText(ingredient);
        viewHolder.quantity.setText(quantity);
        viewHolder.measure.setText(measure);
    }

    @Override
    public int getItemCount() {
        if (bakeries == null) {
            return 0;
        }else
            return bakeries.size();
    }
}