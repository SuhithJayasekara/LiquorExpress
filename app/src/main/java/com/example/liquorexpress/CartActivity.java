package com.example.liquorexpress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.liquorexpress.adapters.CartAdapter;
import com.example.liquorexpress.models.Cart;
import com.example.liquorexpress.models.CartOrderMap;
import com.example.liquorexpress.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import es.dmoral.toasty.Toasty;

public class CartActivity extends AppCompatActivity {
    private List<Cart> cartList = new ArrayList<>();
    private CartAdapter cartAdapter;
    private RecyclerView recyclerView;
    private Button checkoutButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseCartReference;
    private DatabaseReference databaseOrderReference;
    private DatabaseReference databaseCartOrderMapReference;
    private SharedPreferences sharedPreferences;

    private final String CART = "cart";
    private final String ORDER = "order";
    private final String CART_ORDER_MAP = "cartOrderMap";

    private String userEmail;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cartRecyclerView);
        checkoutButton = findViewById(R.id.cartCheckoutButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseCartReference = firebaseDatabase.getReference(CART);
        databaseOrderReference = firebaseDatabase.getReference(ORDER);
        databaseCartOrderMapReference = firebaseDatabase.getReference(CART_ORDER_MAP);

        cartAdapter = new CartAdapter(this, databaseCartReference, cartList);

        sharedPreferences = getApplicationContext().getSharedPreferences("liquorExpress", MODE_PRIVATE);
        userEmail = firebaseAuth.getCurrentUser().getEmail();
        userName = sharedPreferences.getString("loggedUserName", "");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = databaseOrderReference.push().getKey();
                double total = 0;

                for(Cart item: cartList) {
                    String insideId = databaseCartOrderMapReference.push().getKey();

                    total += item.getQuantity() * item.getItemPrice();

                    CartOrderMap cartOrderMap = new CartOrderMap();
                    cartOrderMap.setOrderId(id);
                    cartOrderMap.setProductId(item.getProductId());
                    cartOrderMap.setName(item.getName());
                    cartOrderMap.setPrice(item.getItemPrice() * item.getQuantity());
                    cartOrderMap.setQuantity(item.getQuantity());

                    databaseCartReference.child(item.getId()).setValue(item);
                    databaseCartOrderMapReference.child(insideId).setValue(cartOrderMap);
                }

                databaseCartReference.orderByChild("userId").equalTo(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot cartObj: dataSnapshot.getChildren()) {
                                cartObj.getRef().removeValue();
                            }

                            cartList = new ArrayList<>();
                            cartAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

                Order order = new Order();
                order.setUserId(firebaseAuth.getCurrentUser().getUid());
                order.setName(userName);
                order.setEmail(userEmail);
                order.setTotal(total);
                order.setIsComplete(false);

                databaseOrderReference.child(id).setValue(order);

                Toasty.success(CartActivity.this, "Your order placed successfully.", Toasty.LENGTH_SHORT).show();

                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        cartData();
    }

    private void cartData() {
        databaseCartReference.orderByChild("userId").equalTo(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Cart cartItem;

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot cartObj: dataSnapshot.getChildren()) {
                        cartItem = cartObj.getValue(Cart.class);
                        cartItem.setId(cartObj.getKey());

                        if (cartItem != null) {
                            cartList.add(cartItem);
                        }
                    }
                    cartAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}