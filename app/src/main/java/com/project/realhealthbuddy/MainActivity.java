package com.project.realhealthbuddy;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.realhealthbuddy.Fragments.HistoryFragment;
import com.project.realhealthbuddy.Fragments.HomeFragment;
import com.project.realhealthbuddy.Fragments.MedicineFragment;
import com.project.realhealthbuddy.Fragments.MeditationFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.
        OnNavigationItemSelectedListener {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.homeBottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.homebottommenuHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    HomeFragment homeFragment = new HomeFragment();
    MedicineFragment medicineFragment = new MedicineFragment();
    MeditationFragment meditationFragment = new MeditationFragment();
    HistoryFragment historyFragment = new HistoryFragment();


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.homebottommenuHome)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout,homeFragment).commit();

        } else if (menuItem.getItemId() == R.id.homebottommenuMedicine) {

            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout,medicineFragment).commit();


        } else if (menuItem.getItemId() == R.id.homebottommenuMeditation) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout,meditationFragment).commit();

        } else if (menuItem.getItemId() == R.id.homebottommenuHistory) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout,historyFragment).commit();

        }
        return true;
    }
}