package com.project.realhealthbuddy;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class WelcomeActivity extends AppCompatActivity {

    private MaterialButton btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Initially both buttons are transparent
        deselectButton(btnSignIn);
        deselectButton(btnSignUp);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(btnSignIn);
                deselectButton(btnSignUp);
                // TODO: Navigate to LoginActivity
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(btnSignUp);
                deselectButton(btnSignIn);
                // TODO: Navigate to RegistrationActivity
                Intent intent = new Intent(WelcomeActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void selectButton(MaterialButton button) {
        // Solid white background when selected
        button.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        button.setTextColor(Color.BLACK);
    }

    private void deselectButton(MaterialButton button) {
        // Semi-transparent white when unselected
        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#33FFFFFF")));
        button.setTextColor(Color.WHITE);
    }
}