package com.example.liquorexpress;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.liquorexpress.adapters.FoodAdapter;
import com.example.liquorexpress.models.Food;
import com.example.liquorexpress.utils.RecyclerTouchListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    private List<Food> foodList = new ArrayList<>();
    private FoodAdapter foodAdapter;
    private RecyclerView recyclerView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private final String PRODUCT = "product";

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.homeRecyclerView);
        foodAdapter = new FoodAdapter(foodList);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(PRODUCT);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("liquorExpress", MODE_PRIVATE);
        final boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(foodAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Food food = foodList.get(position);

                Intent intent;

                if (isAdmin) {
                    intent = new Intent(getActivity().getApplicationContext(), FoodEditActivity.class);
                } else {
                    intent = new Intent(getActivity().getApplicationContext(), FoodItemActivity.class);
                }
                intent.putExtra("id", food.getId());
                intent.putExtra("name", food.getName());
                intent.putExtra("price", Double.toString(food.getPrice()));
                intent.putExtra("description", food.getDescription());
                intent.putExtra("imageUrl", food.getImageUrl());
                intent.putExtra("categoryId", food.getCategoryId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) { }
        }));

        foodData();

        return view;
    }

    private void foodData() {
        databaseReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Food foodItem;

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot item: dataSnapshot.getChildren()) {
                        foodItem = item.getValue(Food.class);
                        foodItem.setId(item.getKey());

                        if (foodItem != null) {
                            foodList.add(foodItem);
                        }
                    }
                    foodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}