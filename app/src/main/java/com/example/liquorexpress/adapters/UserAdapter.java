package com.example.liquorexpress.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.liquorexpress.R;
import com.example.liquorexpress.models.User;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<User> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView email;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.itemUserName);
            email = (TextView) view.findViewById(R.id.itemUserEmail);
        }
    }

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, parent, false);

        return new UserAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.MyViewHolder holder, int position) {
        User user = userList.get(position);

        String name = user.getFirstName() +" "+ user.getLastName();
        holder.name.setText(user.getIsAdmin() ? name +" (Admin)" : name);
        holder.email.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
