package com.example.liquorexpress.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.liquorexpress.R;
import com.example.liquorexpress.models.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    private List<Food> foodList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name, price;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.foodImage);
            name = (TextView) view.findViewById(R.id.foodName);
            price = (TextView) view.findViewById(R.id.foodPrice);
        }
    }

    public FoodAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_food, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Food food = foodList.get(position);
        if (food.getImageUrl() != null || !food.getImageUrl().equals("")) {
            Picasso.get().load(food.getImageUrl()).fit().centerCrop().into(holder.image);
        }
        holder.name.setText(food.getName());
        holder.price.setText("Rs. "+ String.format("%.2f", food.getPrice()) +"/=");
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
