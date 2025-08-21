package com.project.realhealthbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername,etPassword;


    Button btnLogin,btnRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLoginLogin);
        btnRegistration = findViewById(R.id.btnLoginRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (etUsername.getText().toString().isEmpty())
                {
                    etUsername.setError("Please enter Username");

                } else if (etUsername.getText().toString().length() < 8)
                {
                    etUsername.setError("Username must be at least 8 character");

                } else if (etPassword.getText().toString().isEmpty())
                {
                    etPassword.setError("Please enter Password");

                } else if (etPassword.getText().toString().length() < 8)
                {
                    etPassword.setError("Password must be at least 8 character");
                }

                else {

                   Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                   startActivity(intent);


                }
            }
        });



        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });


    }
}