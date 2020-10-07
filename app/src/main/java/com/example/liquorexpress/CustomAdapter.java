package com.example.feedback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private ArrayList<Comment> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username, title, description;
        ImageView imageViewIcon;
        RatingBar ratingbar;
//        TextView textViewVersion;

        //        ImageView imageViewIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.username = itemView.findViewById(R.id.comment_username);
            this.ratingbar = itemView.findViewById(R.id.comment_rating);
            this.title = itemView.findViewById(R.id.comment_main_title);
            this.description = itemView.findViewById(R.id.comment_subtext);
            this.imageViewIcon = itemView.findViewById(R.id.comment_user);
        }
    }

    public CustomAdapter(ArrayList<Comment> data) {
        this.dataSet = data;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_view, parent, false);
        view.setOnClickListener(MainActivity.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        TextView username = holder.username;
        TextView desc = holder.description;
        TextView title = holder.title;
        RatingBar rating = holder.ratingbar;
        ImageView imageView = holder.imageViewIcon;

        username.setText(dataSet.get(position).username);
        desc.setText(dataSet.get(position).commentDescription);
        title.setText(dataSet.get(position).commentTitle);
        rating.setRating((float) dataSet.get(position).rating);
//        imageView.setImageResource(dataSet.get(position).userImage());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
