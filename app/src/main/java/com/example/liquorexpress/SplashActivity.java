package com.example.liquorexpress;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sharedPreferences = getApplicationContext().getSharedPreferences("liquorExpress", MODE_PRIVATE);

        final boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        final boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (isLoggedIn) {
                    if (isAdmin) {
                        intent = new Intent(SplashActivity.this, AdminMainActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    }
                } else {
                    intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}