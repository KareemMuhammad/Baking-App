package com.oikm.a100.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.oikm.a100.bakingapp.Model.Bakery;

import java.util.List;

public class BakeryAdapter extends RecyclerView.Adapter<BakeryAdapter.ViewHolder> {
   private List<Bakery> bakeries;
   private final OnClickListener clickListener;

    public BakeryAdapter(List<Bakery> bakeries, OnClickListener clickListener) {
        this.bakeries = bakeries;
        this.clickListener = clickListener;
    }
    public interface OnClickListener{
      void onButtonClick( int i);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutID = R.layout.card_view;
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToRoot = false;
        View view = inflater.inflate(layoutID,viewGroup, attachToRoot);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public Button ingredientBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            ingredientBtn = itemView.findViewById(R.id.videoButton);
            ingredientBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getAdapterPosition();
                    clickListener.onButtonClick(i);
                }
            });

        }
    }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Bakery bakery = bakeries.get(i);
            viewHolder.nameText.setText(bakery.getName());
        }

        @Override
        public int getItemCount() {
            if (bakeries == null) {
                return 0;
            } else
                return bakeries.size();
        }

        public void setData(List<Bakery> posts) {
            bakeries = posts;
        }

}
