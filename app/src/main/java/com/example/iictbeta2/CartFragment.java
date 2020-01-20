package com.example.iictbeta2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iictbeta2.JavaClasses.CartItems;
import com.example.iictbeta2.JavaClasses.CartViewHolder;
import com.example.iictbeta2.JavaClasses.OrderDetails;
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

import java.util.ArrayList;
import java.util.Map;

public class CartFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private DatabaseReference ref;
    private Integer amount, curr_balance, total, flag1 = 0, flag2 = 0, subtotal;
    private String order_string = "", display_name, table_no, itemName;
    private TextView amountView, itemNameView, subtotalview;
    private Button place_order;
    private AlertDialog.Builder mBuilder;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.cart_recyclerView);
        subtotalview = view.findViewById(R.id.subtotal_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseDatabase.getInstance().getReference().child("cart").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int sum = 0;
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object price = map.get("price"), amnt = map.get("amount");
                            if(price != null && amnt != null) sum += Integer.parseInt(String.valueOf(price)) * Integer.parseInt(String.valueOf(amnt));
                        }
                        subtotal = sum;
                        subtotalview.setText("Total = " + String.valueOf(sum) + " Taka");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        place_order = view.findViewById(R.id.place_order_button);
        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag1 = flag2 = 0;
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    if (ds.getKey().equals("balance")){
                                        flag1++;
                                        curr_balance = ds.getValue(Integer.class);
                                    }
                                    if (ds.getKey().equals("display_name")){
                                        flag1++;
                                        display_name = ds.getValue().toString();
                                    }

                                    if(flag1 == 2){

                                        Toast.makeText(getActivity(), "Aise" + curr_balance.toString(), Toast.LENGTH_LONG).show();

                                        if(curr_balance > subtotal && subtotal > 0) {

                                            FirebaseDatabase.getInstance().getReference().child("cart")
                                                    .child(FirebaseAuth.getInstance().getUid())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            order_string = "";
                                                            int n = (int) dataSnapshot.getChildrenCount();

                                                            for(DataSnapshot ds: dataSnapshot.getChildren()){
                                                                Map<String, Object> map = (Map<String, Object>) ds.getValue();
                                                                Object obj1 = map.get("item_name");
                                                                Object obj2 = map.get("amount");
                                                                itemName = obj1.toString();
                                                                amount = Integer.parseInt(String.valueOf(obj2));

                                                                order_string += itemName + "  x" + amount.toString() + "\n";
                                                                flag2++;

                                                                if(flag2 == n){
                                                                    order_string += "Total = " + subtotal.toString() + " Taka";
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                                                    String msg = order_string;
                                                                    builder.setTitle("Order Details").setMessage(msg)
                                                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    //ref.child(item_id).removeValue();
                                                                                    OrderDetails order = new OrderDetails();
                                                                                    order.setUid(FirebaseAuth.getInstance().getUid());
                                                                                    order.setDisplay_name(display_name);
                                                                                    order.setTable_no("5");

                                                                                    String oid = FirebaseDatabase.getInstance().getReference()
                                                                                            .child("orders").push().getKey();

                                                                                    order.setOid(oid);

                                                                                    FirebaseDatabase.getInstance().getReference()
                                                                                            .child("orders").child(oid).setValue(order)
                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    if(task.isSuccessful()){

                                                                                                    } else {

                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                }
                                                                            })
                                                                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    dialog.dismiss();
                                                                                }
                                                                            });

                                                                    AlertDialog dialog = builder.create();
                                                                    dialog.show();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

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
                total = cartItems.getAmount()*cartItems.getPrice();

                final String item_name = cartItems.getItem_name();
                final String item_id = cartItems.getItem_id();
                final String itemName = cartItems.getItem_name();
                final String itemID = cartItems.getItem_id();
                cartViewHolder.nameView.setText(cartItems.getItem_name());
                cartViewHolder.amountView.setText(string);
                cartViewHolder.totalView.setText(total.toString() + "/=");

                cartViewHolder.clearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        String msg = "Are you sure to remove " + item_name + " from your cart?";

                        builder.setTitle("Remove " + item_name).setMessage(msg)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ref.child(item_id).removeValue();
                                    }
                                })
                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

                cartViewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBuilder = new AlertDialog.Builder(getContext());
                        mView = getLayoutInflater().inflate(R.layout.dialog_add_to_cart, null);

                        amountView = mView.findViewById(R.id.amount_view);
                        itemNameView = mView.findViewById(R.id.item_name_view);
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

                        itemNameView.setText(itemName);

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

                                    FirebaseDatabase.getInstance().getReference().child("cart").child(uid)
                                            .child(itemID).child("amount").setValue(amount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                FirebaseDatabase.getInstance().getReference().child("cart").child(uid)
                                                        .child(itemID).child("item_id").setValue(itemID).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            FirebaseDatabase.getInstance().getReference().child("cart").child(uid)
                                                                    .child(itemID).child("item_name").setValue(itemName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        Toast.makeText(getActivity(), "Cart updated", Toast.LENGTH_LONG).show();
                                                                        alertDialog.dismiss();
                                                                    } else {
                                                                        Toast.makeText(getActivity(), "Something is wrong, try again.", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            });
                                                            //Toast.makeText(getActivity(), "Item added to your cart", Toast.LENGTH_LONG).show();
                                                            //alertDialog.dismiss();
                                                        } else {
                                                            Toast.makeText(getActivity(), "Something is wrong, try again.", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });

                                                //Toast.makeText(getActivity(), "Item added to your cart", Toast.LENGTH_LONG).show();
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
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartviweholder, parent, false);
                return new CartViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
