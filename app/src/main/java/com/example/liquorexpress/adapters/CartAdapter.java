package com.example.liquorexpress.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liquorexpress.R;
import com.example.liquorexpress.models.Cart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private List<Cart> cartList;
    private Context context;
    private DatabaseReference databaseReference;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView qty;
        public TextView price;
        public ImageButton add;
        public ImageButton minus;
        public ImageButton remove;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.itemCartName);
            qty = (TextView) view.findViewById(R.id.itemCartQty);
            price = (TextView) view.findViewById(R.id.itemCartPrice);
            add = (ImageButton) view.findViewById(R.id.itemCartPlusButton);
            minus = (ImageButton) view.findViewById(R.id.itemCartMinusButton);
            remove = (ImageButton) view.findViewById(R.id.itemCartRemoveButton);
        }
    }

    public CartAdapter(Context context, DatabaseReference databaseReference, List<Cart> cartList) {
        this.cartList = cartList;
        this.context = context;
        this.databaseReference = databaseReference;
    }

    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);

        return new CartAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.MyViewHolder holder, int position) {
        final Cart cart = cartList.get(position);
        final int index = position;

        holder.name.setText(cart.getName());
        holder.qty.setText(Long.toString(cart.getQuantity()) +" pcs/ packets");
        holder.price.setText("Rs. "+ String.format("%.2f", cart.getQuantity() * cart.getItemPrice()) +"/=");

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.setQuantity(cart.getQuantity() + 1);
                holder.qty.setText(Long.toString(cart.getQuantity()) +" pcs/ packets");
                holder.price.setText("Rs. "+ String.format("%.2f", cart.getQuantity() * cart.getItemPrice()) +"/=");
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.setQuantity(cart.getQuantity() - 1);
                holder.qty.setText(Long.toString(cart.getQuantity()) +" pcs/ packets");
                holder.price.setText("Rs. "+ String.format("%.2f", cart.getQuantity() * cart.getItemPrice()) +"/=");
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Confirmation")
                        .setMessage("Do you really want to delete?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Cart cart = cartList.get(index);

                                databaseReference.orderByKey().equalTo(cart.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getChildrenCount() == 1) {
                                            for (DataSnapshot cartObj: dataSnapshot.getChildren()) {
                                                cartObj.getRef().removeValue();
                                            }

                                            cartList.remove(index);
                                            notifyDataSetChanged();

                                            Toasty.success(context, "Item has been removed.", Toasty.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                });
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
