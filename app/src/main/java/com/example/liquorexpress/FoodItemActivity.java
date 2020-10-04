package com.example.liquorexpress;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.liquorexpress.models.Cart;
import com.example.liquorexpress.models.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import es.dmoral.toasty.Toasty;

public class FoodItemActivity extends AppCompatActivity {
    private ImageView foodImage;
    private TextView foodName;
    private TextView foodPrice;
    private TextView foodDescription;
    private EditText foodQty;
    private Button addToCartButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Food foodItem;

    private final String CART = "cart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);

        foodImage = findViewById(R.id.foodItemImage);
        foodName = findViewById(R.id.foodItemName);
        foodPrice = findViewById(R.id.foodItemPrice);
        foodDescription = findViewById(R.id.foodItemDescription);
        foodQty = findViewById(R.id.foodItemQty);
        addToCartButton = findViewById(R.id.foodItemAddToCart);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(CART);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("description");
        String imageUrl = intent.getStringExtra("imageUrl");

        foodItem = new Food();
        foodItem.setId(id);
        foodItem.setName(name);
        foodItem.setPrice(Double.parseDouble(price));
        foodItem.setDescription(description);
        foodItem.setImageUrl(imageUrl);

        if (!imageUrl.equals("")) {
            Picasso.get().load(foodItem.getImageUrl()).fit().centerCrop().into(foodImage);
        }
        foodName.setText(foodItem.getName());
        foodPrice.setText("Rs. "+ String.format("%.2f", foodItem.getPrice()) +"/=");
        foodDescription.setText(foodItem.getDescription());

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qty = foodQty.getText().toString().trim();

                if (qty.equals("")) {
                    Toasty.warning(FoodItemActivity.this, "Please enter an amount.", Toasty.LENGTH_SHORT).show();
                } else {
                    Cart cart = new Cart();
                    cart.setUserId(firebaseAuth.getCurrentUser().getUid());
                    cart.setProductId(foodItem.getId());
                    cart.setName(foodItem.getName());
                    cart.setItemPrice(foodItem.getPrice());
                    cart.setQuantity(Long.parseLong(qty));
                    databaseReference.child(databaseReference.push().getKey()).setValue(cart);

                    Toasty.success(FoodItemActivity.this, "Added to cart successfully.", Toasty.LENGTH_SHORT).show();

                    Intent intentBack = new Intent(FoodItemActivity.this, MainActivity.class);
                    startActivity(intentBack);
                }
            }
        });
    }
}