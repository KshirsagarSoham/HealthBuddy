package com.project.realhealthbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Handler h1=new Handler();
        h1.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i1=new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(i1);
                finish();
            }
        },3000);

    }
}