package com.example.iictbeta2.JavaClasses;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iictbeta2.R;

public class DataViewHolder extends RecyclerView.ViewHolder {

    public TextView nameView, priceView;
    public ImageView imageView;
    public Button addToCart;

    public DataViewHolder(@NonNull View itemView) {
        super(itemView);

        nameView = itemView.findViewById(R.id.card_name);
        priceView = itemView.findViewById(R.id.card_price);
        imageView = itemView.findViewById(R.id.card_image);
        addToCart = itemView.findViewById(R.id.addToCart);
    }
}
