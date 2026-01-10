package com.project.realhealthbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import androidx.room.Room;
import android.content.Context;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.realhealthbuddy.comman.AppDatabase;
import com.project.realhealthbuddy.comman.DBhelper;
import com.project.realhealthbuddy.comman.User;
import com.project.realhealthbuddy.comman.UserDao;

public class RegistrationActivity extends AppCompatActivity {

    EditText etName,etMobileNo, etEmailID,etUsername,etPassword;
    AppCompatButton btnRegister;
    CheckBox cbShowHidePassword;
    DBhelper dBhelper;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        etName = findViewById(R.id.etRegisterName);
        etMobileNo = findViewById(R.id.etRegisterMobileNo);
        etEmailID = findViewById(R.id.etRegisterEmail);
        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        cbShowHidePassword = findViewById(R.id.cbshowhidepassword);
        btnRegister = findViewById(R.id.btnRegisterCreateAccount);

        dBhelper=new DBhelper(this);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor=preferences.edit();




        cbShowHidePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (etName.getText().toString().isEmpty()) {
                    etName.setError("Please Enter Your Name");

                } else if (etMobileNo.getText().toString().isEmpty()) {
                    etMobileNo.setError("Please Enter Your Mobile Number");

                } else if (etMobileNo.getText().toString().length() != 10) {
                    etMobileNo.setError("Mobile No. must be 10 characters");

                } else if (etEmailID.getText().toString().isEmpty()) {
                    etEmailID.setError("Please Enter Your E-mail");

                } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmailID.getText().toString()).matches()) {
                    etEmailID.setError("Invalid E-mail Address");

                } else if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Please Enter Your Username");

                } else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please Enter Your Password");

                } else {
                    String name= etName.getText().toString();
                    String mobile= etMobileNo.getText().toString();
                    String email= etEmailID.getText().toString();
                    String username= etUsername.getText().toString();
                    String password = etPassword.getText().toString();


                   /* boolean inserted = dBhelper.registeruser(name, mobile, email, username, password);
                    if (inserted) {
                        editor.putString("name", name);
                        editor.putString("mobileno",  mobile);
                        editor.putString("email", email);
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putBoolean("islogin", true);
                        editor.apply();



*/

// In onCreate or method
                    // Get Room database and DAO (add as class members or singleton)
                    AppDatabase db = AppDatabase.getDatabase(RegistrationActivity.this);
                    UserDao userDao = db.userDao();

// Insert user
                    User newUser = new User(name, mobile, email, username, password);  // Hash password first in production
                    long result = userDao.insertUser(newUser);
                    boolean inserted = result != -1;

                    if (inserted) {
                        editor.putString("name", name);
                        editor.putString("mobileno", mobile);
                        editor.putString("email", email);
                        editor.putString("username", username);
                        // Remove or hash password storage: editor.remove("password");
                        editor.putBoolean("islogin", true);
                        editor.apply();

                        // Optional: Show success and navigate
                        Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        String fullname = etUsername.getText().toString().trim();
        editor.putString("username",fullname);
        editor.apply();

    }
}