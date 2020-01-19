package com.example.iictbeta2.JavaClasses;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iictbeta2.R;

public class CartViewHolder extends RecyclerView.ViewHolder {

    public TextView nameView, amountView;
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        nameView = itemView.findViewById(R.id.item_name_);
        amountView = itemView.findViewById(R.id.item_amount_);
    }
}
