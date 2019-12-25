package com.example.iictbeta2;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iictbeta2.JavaClasses.DataDemo;
import com.example.iictbeta2.JavaClasses.DataViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private DatabaseReference ref;

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
                        dataViewHolder.nameView.setText(dataDemo.getItem_name());
                        dataViewHolder.priceView.setText(dataDemo.getPrice());
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


