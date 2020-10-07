package com.example.feedback;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anjalee.feedback.CommentList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteComment extends AppCompatActivity {

    private static Button cancel, post;
    private static RatingBar ratingBar;
    private static TextView ratingtext;
    private static EditText title, description;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        cancel = findViewById(R.id.cancelbutton);
        ratingBar = findViewById(R.id.ratingBar);
        post = findViewById(R.id.postbutton);
        ratingtext = findViewById(R.id.ratingtext);
        title = findViewById(R.id.txtcommenttitle);
        description = findViewById(R.id.comment_description);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("comments");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int rating = (int) v;
                String message = null;

                switch (rating) {
                    case 0:
                        message = "We are really sorry! ";
                        break;
                    case 1:
                        message = "Sorry for hear that!";
                        break;
                    case 2:
                        message = "We always accept suggestions!";
                        break;
                    case 3:
                        message = "Good enough";
                        break;
                    case 4:
                        message = "Great! Thank you!";
                        break;
                    case 5:
                        message = "Awesome! You are the best!";
                        break;


                }
                ratingtext.setText(message);
//                Toast.makeText(getApplicationContext(), ratingBar.getRating() + "", Toast.LENGTH_LONG).show();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Comment comment = new Comment("Anjalee", ratingBar.getRating(), null, title.getText().toString(), description.getText().toString());
                String keyId = myRef.push().getKey();
                myRef.child(keyId).setValue(comment);

                Toast.makeText(getApplicationContext(), "Thank you for your Feedback!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}