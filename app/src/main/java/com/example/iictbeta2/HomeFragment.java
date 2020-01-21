package com.example.iictbeta2;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iictbeta2.JavaClasses.CartItems;
import com.example.iictbeta2.JavaClasses.DataDemo;
import com.example.iictbeta2.JavaClasses.DataViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private DatabaseReference ref;

    Integer amount = 0, price;

    private TextView amountView, itenNameView;
    private AlertDialog.Builder mBuilder;
    private View mView;

    public HomeFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.home_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ref = FirebaseDatabase.getInstance().getReference().child("food");


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<DataDemo>().
                setQuery(ref, DataDemo.class).build();

        FirebaseRecyclerAdapter<DataDemo, DataViewHolder> adapter =
                new FirebaseRecyclerAdapter<DataDemo, DataViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull DataViewHolder dataViewHolder, int i, @NonNull DataDemo dataDemo) {

                        final String itemName = dataDemo.getItem_name();
                        final String itemID = dataDemo.getItem_id();
                        dataViewHolder.nameView.setText(itemName);
                        dataViewHolder.setImageView(dataDemo.getImage_url());
                        final Integer price = dataDemo.getPrice();
                        dataViewHolder.priceView.setText(price.toString() + " Taka");
                        dataViewHolder.addToCart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                mBuilder = new AlertDialog.Builder(getContext());
                                mView = getLayoutInflater().inflate(R.layout.dialog_add_to_cart, null);

                                amountView = mView.findViewById(R.id.amount_view);
                                itenNameView = mView.findViewById(R.id.item_name_view);
                                Button plus_btn = mView.findViewById(R.id.plus_button);
                                Button minus_btn = mView.findViewById(R.id.minus_button);
                                Button cancel_btn = mView.findViewById(R.id.cancel_button);
                                Button ok_btn = mView.findViewById(R.id.ok_button);

                                try{
                                    FirebaseDatabase.getInstance().getReference().child("cart")
                                            .child(FirebaseAuth.getInstance().getUid()).child(itemID).child("amount")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    amount = dataSnapshot.getValue(Integer.class);
                                                    if(amount == null) amount = 0;
                                                    amountView.setText(amount.toString());
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                } catch (Exception e){
                                    Toast.makeText(getActivity(), "Mara Khaise", Toast.LENGTH_LONG).show();
                                }

                                mBuilder.setView(mView);
                                final AlertDialog alertDialog = mBuilder.create();
                                alertDialog.show();

                                itenNameView.setText(itemName);

                                plus_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        amount++;
                                        amountView.setText(amount.toString());
                                    }
                                });

                                minus_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(amount > 0) amount--;
                                        amountView.setText(amount.toString());
                                    }
                                });

                                ok_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(amount > 0){
                                            final String uid = FirebaseAuth.getInstance().getUid();

                                            CartItems cartItems = new CartItems();
                                            cartItems.setAmount(amount);
                                            cartItems.setItem_id(itemID);
                                            cartItems.setPrice(price);
                                            cartItems.setItem_name(itemName);

                                            FirebaseDatabase.getInstance().getReference().child("cart").child(uid)
                                                    .child(itemID).setValue(cartItems).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getActivity(), "Item added to your cart", Toast.LENGTH_LONG).show();
                                                        alertDialog.dismiss();
                                                    } else {
                                                        Toast.makeText(getActivity(), "Something is wrong, try again.", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }

                                    }
                                });

                                cancel_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder, parent, false);
                        return new DataViewHolder(view);
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}


