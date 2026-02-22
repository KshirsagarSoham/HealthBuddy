package com.project.realhealthbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.project.realhealthbuddy.comman.AppDatabase;
import com.project.realhealthbuddy.comman.User;
import com.project.realhealthbuddy.comman.UserDao;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    TextView tvFullName, tvEmail, tvMobile, tvAge, tvHeight, tvWeight, tvGender;
    Button btnEdit;

    private ActivityResultLauncher<Intent> editProfileLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profileImage);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvMobile = findViewById(R.id.tvMobile);
        tvAge = findViewById(R.id.tvAge);
        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);
        tvGender = findViewById(R.id.tvGender);
        btnEdit = findViewById(R.id.btnEditProfile);

        // Launcher for EditProfileActivity
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> loadProfileData() // Reload profile after returning
        );

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            editProfileLauncher.launch(intent);
        });

        loadProfileData();
    }

    private void loadProfileData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Load Age, Height, Weight, Gender (raw values)
        String age = prefs.getString("age", "");
        String height = prefs.getString("height", "");
        String weight = prefs.getString("weight", "");
        String gender = prefs.getString("gender", "");

        tvAge.setText(age);
        tvHeight.setText(height);
        tvWeight.setText(weight);
        tvGender.setText(gender);

        // Load Full Name, Email, Mobile from Room
        AppDatabase db = AppDatabase.getDatabase(this);
        UserDao userDao = db.userDao();

        new Thread(() -> {
            User user = userDao.getUserByUsername(getUsernameFromSharedPref());
            runOnUiThread(() -> {
                if (user != null) {
                    tvFullName.setText(user.getName());
                    tvEmail.setText(user.getEmail());
                    tvMobile.setText(user.getMobile());
                }
            });
        }).start();
    }

    private String getUsernameFromSharedPref() {
        return PreferenceManager.getDefaultSharedPreferences(this)
                .getString("username", "");
    }
}