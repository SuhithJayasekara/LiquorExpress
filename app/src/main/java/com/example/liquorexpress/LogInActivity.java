package com.example.liquorexpress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.liquorexpress.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import es.dmoral.toasty.Toasty;

public class LogInActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private Button logInButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private final String USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        emailField = findViewById(R.id.logInEmailField);
        passwordField = findViewById(R.id.logInPasswordField);
        logInButton = findViewById(R.id.logInButton);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USER);

        sharedPreferences = getApplicationContext().getSharedPreferences("liquorExpress", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (email == null || email.equals("")) {
                    Toasty.warning(LogInActivity.this, "Please enter Email.", Toast.LENGTH_SHORT, true).show();
                } else if (password == null || password.equals("")) {
                    Toasty.warning(LogInActivity.this, "Please enter Password.", Toast.LENGTH_SHORT, true).show();
                } else {
                    logInButton.setEnabled(false);

                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                    if (firebaseUser != null && firebaseUser.getEmail() != null) {
                                        databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                User loggedUser = null;
                                                if (dataSnapshot.getChildrenCount() == 1) {
                                                    for (DataSnapshot user: dataSnapshot.getChildren()) {
                                                        loggedUser = user.getValue(User.class);
                                                    }

                                                    if (loggedUser != null) {
                                                        editor.putString("loggedUserName", loggedUser.getFirstName() +" "+ loggedUser.getLastName());
                                                        editor.putBoolean("isAdmin", loggedUser.getIsAdmin());
                                                        editor.putBoolean("isLoggedIn", true);
                                                        editor.apply();

                                                        Toasty.success(LogInActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT, true).show();
                                                        if (loggedUser.getIsAdmin()) {
                                                            Intent intent = new Intent(LogInActivity.this, AdminMainActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                logInButton.setEnabled(true);
                                                Toasty.warning(LogInActivity.this, "Login failed.", Toast.LENGTH_SHORT, true).show();
                                            }
                                        });
                                    }
                                } else {
                                    logInButton.setEnabled(true);
                                    Toasty.error(LogInActivity.this, "Login failed. Please check email or password.", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        }
                    );
                }
            }
        });
    }
}