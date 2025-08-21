package com.project.realhealthbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.realhealthbuddy.comman.DBhelper;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername,etPassword;
    DBhelper dBhelper;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

CheckBox cbshowpass;
    Button btnLogin,btnRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        dBhelper=new DBhelper(this);

        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLoginLogin);
        btnRegistration = findViewById(R.id.btnLoginRegister);
        cbshowpass =findViewById(R.id.CBshowpasswordLOGIN);

        preferences= PreferenceManager.getDefaultSharedPreferences(this);
editor= preferences.edit();

        if (preferences.getBoolean("islogin", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (etUsername.getText().toString().isEmpty())
                {
                    etUsername.setError("Please enter Username");

                } else if (etUsername.getText().toString().length() < 4)
                {
                    etUsername.setError("Username must be at least 4 character");

                } else if (etPassword.getText().toString().isEmpty())
                {
                    etPassword.setError("Please enter Password");

                } else if (etPassword.getText().toString().length() < 8)
                {
                    etPassword.setError("Password must be at least 8 character");
                } else {
                    String inputName=etUsername.getText().toString();
                    String inputPassword=etPassword.getText().toString();

                   validlogin(inputName, inputPassword);

                }
            }
        });


    cbshowpass.setOnCheckedChangeListener((buttonView, isChecked) ->
    {
    if (isChecked){
        etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }
    else {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
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

    private void validlogin(String inputName, String inputPassword) {
        if (dBhelper.validatelogin(inputName, inputPassword)) {
        // Save to SharedPreferences
        editor.putBoolean("islogin", true);
        editor.putString("username", inputName);
        editor.apply();

        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    } else {
        Toast.makeText(this, "Invalid credentials. Try again or register.", Toast.LENGTH_SHORT).show();
    }
    }

}