package com.project.realhealthbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.project.realhealthbuddy.comman.AppDatabase;
import com.project.realhealthbuddy.comman.User;
import com.project.realhealthbuddy.comman.UserDao;

public class EditProfileActivity extends AppCompatActivity {

    TextInputEditText etFullName, etMobile, etEmail, etAge, etHeight, etWeight, etGender;
    Button btnSave;
    private ImageView profileImage, btnAddPhoto;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etFullName = findViewById(R.id.etFullName);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etGender = findViewById(R.id.etGender);
        btnSave = findViewById(R.id.btnSaveProfile);

        profileImage = findViewById(R.id.profileImage);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        AppDatabase db = AppDatabase.getDatabase(this);
        UserDao userDao = db.userDao();

        // Load Full Name, Mobile, Email from Room
        new Thread(() -> {
            User user = userDao.getUserByUsername(getUsernameFromSharedPref());
            runOnUiThread(() -> {
                if (user != null) {
                    etFullName.setText(user.getName());
                    etMobile.setText(user.getMobile());
                    etEmail.setText(user.getEmail());
                }
            });
        }).start();

        // Load Age, Height, Weight, Gender from SharedPreferences
        etAge.setText(prefs.getString("age", ""));
        etHeight.setText(prefs.getString("height", ""));
        etWeight.setText(prefs.getString("weight", ""));
        etGender.setText(prefs.getString("gender", ""));

        btnSave.setOnClickListener(v -> {
            // Save Full Name, Mobile, Email in Room
            new Thread(() -> {
                User user = userDao.getUserByUsername(getUsernameFromSharedPref());
                if (user != null) {
                    user.setName(etFullName.getText().toString());
                    user.setMobile(etMobile.getText().toString());
                    user.setEmail(etEmail.getText().toString());
                    userDao.updateUser(user);
                }
            }).start();

            // Save Age, Height, Weight, Gender in SharedPreferences (raw values only)
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("age", etAge.getText() != null ? etAge.getText().toString() : "");
            editor.putString("height", etHeight.getText() != null ? etHeight.getText().toString() : "");
            editor.putString("weight", etWeight.getText() != null ? etWeight.getText().toString() : "");
            editor.putString("gender", etGender.getText() != null ? etGender.getText().toString() : "");
            editor.apply();

            Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();

            // Notify ProfileActivity to refresh instantly
            setResult(RESULT_OK);
            finish();
        });


        btnAddPhoto.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

            startActivityForResult(intent, PICK_IMAGE);

        });


    }

    private String getUsernameFromSharedPref() {
        return PreferenceManager.getDefaultSharedPreferences(this)
                .getString("username", "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();

            // Take persistent permission
            getContentResolver().takePersistableUriPermission(
                    imageUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            // Show image immediately
            profileImage.setImageURI(imageUri);

            // Save URI
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("profile_image", imageUri.toString());
            editor.apply();
        }
    }
}