package com.example.liquorexpress.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.liquorexpress.R;
import com.example.liquorexpress.models.CartOrderMap;

import java.util.List;

public class CartOrderMapAdapter extends RecyclerView.Adapter<CartOrderMapAdapter.MyViewHolder> {

    private List<CartOrderMap> cartOrderMapList;

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

    public CartOrderMapAdapter(List<CartOrderMap> cartOrderMapList) {
        this.cartOrderMapList = cartOrderMapList;
    }

    @Override
    public CartOrderMapAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);

        return new CartOrderMapAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartOrderMapAdapter.MyViewHolder holder, int position) {
        CartOrderMap cartOrderMap = cartOrderMapList.get(position);

        holder.name.setText(cartOrderMap.getName());
        holder.qty.setText(Long.toString(cartOrderMap.getQuantity()) +" pcs/ packets");
        holder.price.setText("Rs. "+ String.format("%.2f", cartOrderMap.getPrice()) +"/=");
        holder.add.setVisibility(View.GONE);
        holder.minus.setVisibility(View.GONE);
        holder.remove.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return cartOrderMapList.size();
    }
}
