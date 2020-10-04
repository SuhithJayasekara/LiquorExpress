package com.example.liquorexpress.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.liquorexpress.R;
import com.example.liquorexpress.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private List<Order> orderList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView email;
        public TextView price;
        public TextView done;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.itemOrderName);
            email = (TextView) view.findViewById(R.id.itemOrderEmail);
            price = (TextView) view.findViewById(R.id.itemOrderPrice);
            done = (TextView) view.findViewById(R.id.itemOrderDone);
        }
    }

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order, parent, false);

        return new OrderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.MyViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.name.setText(order.getName());
        holder.email.setText(order.getEmail());
        holder.price.setText("Rs. "+ String.format("%.2f", order.getTotal()) +"/=");
        holder.done.setVisibility(order.getIsComplete() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
