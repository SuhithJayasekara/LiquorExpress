package com.example.feedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    //    private static ArrayList<Comment> data;
    static View.OnClickListener myOnClickListener;
    private static ExtendedFloatingActionButton fab;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
//        myOnClickListener = new MyOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.viewcomments);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        fab = (ExtendedFloatingActionButton) findViewById(R.id.newcomment);
        fab.setOnClickListener(myOnClickListener);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("comments");

        // Read from the database
        // Attach a listener to read the data at our posts reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectData((Map<String, Object>) dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), WriteComment.class);
//            myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

    private ArrayList<Comment> collectData(Map<String, Object> users) {

        ArrayList<Comment> comments = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            Map item = (Map) entry.getValue();
            System.out.println(item);
            Comment comment = new Comment(item.get("username").toString(),
                    Double.valueOf(item.get("rating").toString()),
                    null,
                    item.get("commentTitle").toString(),
                    item.get("commentDescription").toString());
            //Get user map
//
            //Get phone field and append to list
            comments.add(comment);
        }
        adapter = new CustomAdapter(comments);
        recyclerView.setAdapter(adapter);
        return comments;
//        System.out.println(comments.size());
    }
//
//    private class MyOnClickListener implements View.OnClickListener {
//        private final Context context;
//
//        private MyOnClickListener(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void onClick(View v) {
//            Intent myIntent = new Intent(context, WriteComment.class);
////            myIntent.putExtra("key", value); //Optional parameters
//            MainActivity.this.startActivity(myIntent);
//        }
//
//        private void showDialog(View v) {
//
//
//        }
////        private void removeItem(View v) {
////            int selectedItemPosition = recyclerView.getChildPosition(v);
////            RecyclerView.ViewHolder viewHolder
////                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
////            TextView textViewName
////                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
////            String selectedName = (String) textViewName.getText();
////            int selectedItemId = -1;
////            for (int i = 0; i < MyData.nameArray.length; i++) {
////                if (selectedName.equals(MyData.nameArray[i])) {
////                    selectedItemId = MyData.id_[i];
////                }
////            }
////            removedItems.add(selectedItemId);
////            data.remove(selectedItemPosition);
////            adapter.notifyItemRemoved(selectedItemPosition);
////        }
//    }


}