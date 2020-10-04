package com.example.liquorexpress.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.liquorexpress.R;
import com.example.liquorexpress.models.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category> categoryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.categoryImage);
            name = (TextView) view.findViewById(R.id.categoryName);
        }
    }

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category, parent, false);

        return new CategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.MyViewHolder holder, int position) {
        Category category = categoryList.get(position);
        if (category.getImageUrl() != null || !category.getImageUrl().equals("")) {
            Picasso.get().load(category.getImageUrl()).fit().centerCrop().into(holder.image);
        }
        holder.name.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
