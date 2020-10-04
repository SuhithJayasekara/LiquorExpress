package com.example.liquorexpress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

public class CategoryFoodActivity extends AppCompatActivity {
    private List<Food> foodList = new ArrayList<>();
    private FoodAdapter foodAdapter;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private final String PRODUCT = "product";
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_food);

        recyclerView = findViewById(R.id.categoryFoodRecyclerView);
        foodAdapter = new FoodAdapter(foodList);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(PRODUCT);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(foodAdapter);

        categoryId = getIntent().getStringExtra("id");

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Food food = foodList.get(position);

                Intent intent = new Intent(getApplicationContext(), FoodItemActivity.class);
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
    }

    private void foodData() {
        databaseReference.orderByChild("categoryId").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
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