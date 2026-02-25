package com.project.realhealthbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.project.realhealthbuddy.Fragments.HistoryFragment;
import com.project.realhealthbuddy.Fragments.HomeFragment;
import com.project.realhealthbuddy.Fragments.MedicineFragment;
import com.project.realhealthbuddy.Fragments.MeditationFragment;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.
        OnNavigationItemSelectedListener {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applySavedTheme();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // for dynamic username in sidebar
        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);

        TextView userName = headerView.findViewById(R.id.userName);

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        String fullName = preferences.getString("username", "User");

        String firstName = fullName;
        if (fullName.contains(" ")) {
            firstName = fullName.substring(0, fullName.indexOf(" "));
        }

        // Capitalize first letter
        firstName = firstName.substring(0,1).toUpperCase() +
                firstName.substring(1).toLowerCase();

        userName.setText(firstName);
        toolbar.setTitle("Zenith Health");

//sidebar items clicking


        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                // Open ProfileActivity
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_notifications) {
                //Toast.makeText(MainActivity.this, "Notifications clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_dark_mode) {
               // Toast.makeText(MainActivity.this, "Dark Mode clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_language) {
                //Toast.makeText(MainActivity.this, "Language clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);

            }  else if (id == R.id.nav_privacy) {
                Toast.makeText(MainActivity.this, "Privacy Policy ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, PrivacyPolicyActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_about) {
                Toast.makeText(MainActivity.this, "About App clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AboutUsActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_help) {
                Toast.makeText(MainActivity.this, "Help & Support clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HelpSupportActivity.class);
                startActivity(intent);


            }  else if (id == R.id.nav_logout) {
            // Clear SharedPreferences (Logout)
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            pref.edit().clear().apply();

            // Navigate to LoginActivity
            Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        }

            // Close the drawer after selection
            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });

//        MenuItem logoutItem = navigationView.getMenu().findItem(R.id.nav_logout);
//
//        Drawable icon = logoutItem.getIcon();
//        if (icon != null) {
//            icon.setTint(Color.parseColor("#E53935"));
//            logoutItem.setIcon(icon);
//        }

        /// //////////////////////////////////////////////////////////////////////////////////////

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

        } else if (menuItem.getItemId() == R.id.homebottommenuprogress) {
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

    private void applySavedTheme() {

        android.content.SharedPreferences prefs =
                androidx.preference.PreferenceManager
                        .getDefaultSharedPreferences(this);

        String theme = prefs.getString("theme_preference", "system");

        switch (theme) {
            case "light":
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
                break;

            case "dark":
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES);
                break;

            default:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }


}