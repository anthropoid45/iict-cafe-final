package com.example.iictbeta2.JavaClasses;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iictbeta2.R;

public class CartViewHolder extends RecyclerView.ViewHolder {

    public TextView nameView, amountView, totalView;
    public ImageButton editButton, clearButton;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        nameView = itemView.findViewById(R.id.item_name_);
        amountView = itemView.findViewById(R.id.item_amount_);
        totalView = itemView.findViewById(R.id.total_view);
        editButton = itemView.findViewById(R.id.edit_item_button);
        clearButton = itemView.findViewById(R.id.clear_item_button);
    }
}
