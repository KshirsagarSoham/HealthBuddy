package com.project.realhealthbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Handler h1=new Handler();
        h1.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences =
                        PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);

                boolean isLoggedIn = preferences.getBoolean("islogin", false);

                Intent intent;

                if (isLoggedIn) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                }

                startActivity(intent);
                finish();
            }
        },3000);

    }
}