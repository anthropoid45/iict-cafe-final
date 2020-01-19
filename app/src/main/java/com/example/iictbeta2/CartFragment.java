package com.example.iictbeta2;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iictbeta2.JavaClasses.CartItems;
import com.example.iictbeta2.JavaClasses.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private DatabaseReference ref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.cart_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ref = FirebaseDatabase.getInstance().getReference().child("cart").child(FirebaseAuth.getInstance().getUid());

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<CartItems>().setQuery(ref, CartItems.class).build();
        FirebaseRecyclerAdapter<CartItems, CartViewHolder> adapter = new FirebaseRecyclerAdapter<CartItems, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull CartItems cartItems) {
                String string = "x"+cartItems.getAmount().toString();
                cartViewHolder.nameView.setText(cartItems.getItem_name());
                cartViewHolder.amountView.setText(string);
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartviweholder, parent, false);
                return new CartViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
