package com.example.liquorexpress;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.liquorexpress.models.Category;
import com.example.liquorexpress.models.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class FoodEditActivity extends AppCompatActivity {
    private EditText imageUrlField;
    private EditText nameField;
    private EditText priceField;
    private EditText descriptionField;
    private Button editButton;
    private Spinner categorySpinner;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseCategoryReference;
    private Food foodForSave;
    private boolean isEdit = false;
    private boolean isAdd = false;
    private ArrayAdapter<Category> categoryArrayAdapter;
    private List<Category> categoryList = new ArrayList<>();

    private final String PRODUCT = "product";
    private final String CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_edit);

        imageUrlField = findViewById(R.id.foodEditImageUrl);
        nameField = findViewById(R.id.foodEditName);
        priceField = findViewById(R.id.foodEditPrice);
        descriptionField = findViewById(R.id.foodEditDescription);
        editButton = findViewById(R.id.foodEditButton);
        categorySpinner = findViewById(R.id.foodEditCategorySpinner);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(PRODUCT);
        databaseCategoryReference = firebaseDatabase.getReference(CATEGORY);

        categoryArrayAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categoryList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);
                ((TextView)view.findViewById(android.R.id.text1)).setText(categoryList.get(position).getName());
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getDropDownView(position, convertView, parent);
                ((TextView)view.findViewById(android.R.id.text1)).setText(categoryList.get(position).getName());
                return view;
            }
        };
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(categoryArrayAdapter);

        foodForSave = new Food();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        if (id.equals("")) {
            isAdd = true;
            editButton.setText("Add");
        } else {
            String name = intent.getStringExtra("name");
            String price = intent.getStringExtra("price");
            String description = intent.getStringExtra("description");
            String imageUrl = intent.getStringExtra("imageUrl");
            String categoryId = intent.getStringExtra("categoryId");

            foodForSave.setId(id);
            foodForSave.setName(name);
            foodForSave.setPrice(Double.parseDouble(price != null ? price : "0"));
            foodForSave.setDescription(description);
            foodForSave.setImageUrl(imageUrl);
            foodForSave.setCategoryId(categoryId);

            updateUI();

            imageUrlField.setEnabled(false);
            nameField.setEnabled(false);
            priceField.setEnabled(false);
            descriptionField.setEnabled(false);
            categorySpinner.setEnabled(false);
        }

        loadCategories();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdd) {
                    foodForSave.setImageUrl(imageUrlField.getText().toString());
                    foodForSave.setName(nameField.getText().toString());
                    foodForSave.setPrice(Double.parseDouble(priceField.getText().toString()));
                    foodForSave.setDescription(descriptionField.getText().toString());

                    databaseReference.child(databaseReference.push().getKey()).setValue(foodForSave);

                    Toasty.success(FoodEditActivity.this, "Product added successfully.", Toasty.LENGTH_SHORT).show();

                    Intent backIntent = new Intent(FoodEditActivity.this, AdminMainActivity.class);
                    startActivity(backIntent);
                } else {
                    if (isEdit) {
                        foodForSave.setImageUrl(imageUrlField.getText().toString());
                        foodForSave.setName(nameField.getText().toString());
                        foodForSave.setPrice(Double.parseDouble(priceField.getText().toString()));
                        foodForSave.setDescription(descriptionField.getText().toString());
                        foodForSave.setCategoryId(((Category) categorySpinner.getSelectedItem()).getId());

                        databaseReference.child(foodForSave.getId()).setValue(foodForSave);

                        Toasty.success(FoodEditActivity.this, "Product updated successfully.", Toasty.LENGTH_SHORT).show();
                        Intent backIntent = new Intent(FoodEditActivity.this, AdminMainActivity.class);
                        startActivity(backIntent);
                        finish();
                    } else {
                        isEdit = true;

                        imageUrlField.setEnabled(true);
                        nameField.setEnabled(true);
                        priceField.setEnabled(true);
                        descriptionField.setEnabled(true);
                        categorySpinner.setEnabled(true);
                        editButton.setText("Save");
                    }
                }
            }
        });
    }

    private void loadCategories() {
        databaseCategoryReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Category categoryItem;

                categoryList.add(new Category("Select a Category", null));

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot item: dataSnapshot.getChildren()) {
                        categoryItem = item.getValue(Category.class);
                        categoryItem.setId(item.getKey());

                        if (categoryItem != null) {
                            categoryList.add(categoryItem);
                        }
                    }

                    categoryArrayAdapter.notifyDataSetChanged();

                    for (Category item: categoryList) {
                        if (foodForSave.getCategoryId() != null && item.getId() != null && item.getId().equals(foodForSave.getCategoryId())) {
                            categorySpinner.setSelection(categoryList.indexOf(item));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void updateUI() {
        imageUrlField.setText(foodForSave.getImageUrl());
        nameField.setText(foodForSave.getName());
        priceField.setText(Double.toString(foodForSave.getPrice()));
        descriptionField.setText(foodForSave.getDescription());
    }
}