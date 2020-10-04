package com.example.liquorexpress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.liquorexpress.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.regex.Pattern;
import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity {
    private TextView cancelButton;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText addressField;
    private Button signUpButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private final String USER = "user";
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        cancelButton = findViewById(R.id.signUpCancelTextButton);
        firstNameField = findViewById(R.id.signUpFirstNameField);
        lastNameField = findViewById(R.id.signUpLastNameField);
        emailField = findViewById(R.id.signUpEmailField);
        passwordField = findViewById(R.id.signUpPasswordField);
        confirmPasswordField = findViewById(R.id.signUpConfirmPasswordField);
        addressField = findViewById(R.id.signUpAddressField);
        signUpButton = findViewById(R.id.signUpButton);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USER);

        createUser();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoading) {
                    Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void createUser() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstNameField.getText().toString().trim();
                String lastName = lastNameField.getText().toString().trim();
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                String confirmPassword = confirmPasswordField.getText().toString().trim();
                String address = addressField.getText().toString().trim();

                if (firstName == null || firstName.equals("")) {
                    Toasty.warning(SignUpActivity.this, "First Name is Required.", Toast.LENGTH_SHORT, true).show();
                } else if (lastName == null || lastName.equals("")) {
                    Toasty.warning(SignUpActivity.this, "Last Name is Required.", Toast.LENGTH_SHORT, true).show();
                } else if (email == null || email.equals("")) {
                    Toasty.warning(SignUpActivity.this, "Email is Required.", Toast.LENGTH_SHORT, true).show();
                } else if (!validateEmail(email)) {
                    Toasty.warning(SignUpActivity.this, "Email is not valid.", Toast.LENGTH_SHORT, true).show();
                } else if (password == null || password.equals("")) {
                    Toasty.warning(SignUpActivity.this, "Password is Required.", Toast.LENGTH_SHORT, true).show();
                } else if (password.length() <= 8) {
                    Toasty.warning(SignUpActivity.this, "Password must be more than 8 characters.", Toast.LENGTH_SHORT, true).show();
                } else if (!password.equals(confirmPassword)) {
                    Toasty.error(SignUpActivity.this, "Password is not matching.", Toast.LENGTH_SHORT, true).show();
                } else if (address == null || address.equals("")) {
                    Toasty.warning(SignUpActivity.this, "Address is Required.", Toast.LENGTH_SHORT, true).show();
                } else {
                    isLoading = true;
                    signUpButton.setEnabled(false);
                    final User userObject = new User(firstName, lastName, email, address, false);

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                addUserToDatabase(userObject);
                            } else {
                                isLoading = false;
                                signUpButton.setEnabled(true);
                                Toasty.error(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT, true).show();
                            }
                            }
                        }
                    );
                }
            }
        });
    }

    private void addUserToDatabase(User user) {
        String keyId = databaseReference.push().getKey();
        databaseReference.child(keyId).setValue(user);
        Toasty.success(SignUpActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT, true).show();

        Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean validateEmail(String emailStr) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(emailStr).matches();
    }
}