package com.project.realhealthbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
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
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.
        OnNavigationItemSelectedListener {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Zenith Health");

        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor=preferences.edit();

        bottomNavigationView = findViewById(R.id.homeBottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.homebottommenuHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {  // prevent overlapping on rotation
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.homeframelayout, new HomeFragment())
                    .commit();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logoutMenu)
        {
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            editor.putBoolean("islogin",false).commit();

        }
        return true;
    }


}