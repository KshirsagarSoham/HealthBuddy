package com.project.realhealthbuddy;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.SwitchPreferenceCompat;
import android.widget.Toast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        /// /////////////////////////////////////////////////////////////////////////
        // for language changing

        ListPreference languagePref = findPreference("language_preference");

        if (languagePref != null) {
            languagePref.setOnPreferenceChangeListener((preference, newValue) -> {

                String selectedValue = newValue.toString();

                if (selectedValue.equals("hi") || selectedValue.equals("mr")) {

                    new AlertDialog.Builder(requireContext())
                            .setTitle("Coming Soon")
                            .setMessage("Sorry for inconvenience ! \nHindi and Marathi support will be added in future updates.")
                            .setPositiveButton("OK", null)
                            .show();

                    // Reset back to English
                    languagePref.setValue("en");

                    return false; // Prevent change
                }

                return true; // Allow English selection
            });
        }

        SwitchPreferenceCompat notificationPref =
                findPreference("notifications_enabled");

        if (notificationPref != null) {
            notificationPref.setOnPreferenceChangeListener((preference, newValue) -> {

                boolean isEnabled = (boolean) newValue;

                if (isEnabled) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                        if (ContextCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.POST_NOTIFICATIONS)
                                == PackageManager.PERMISSION_GRANTED) {

                            Toast.makeText(requireContext(),
                                    "Notifications Enabled",
                                    Toast.LENGTH_SHORT).show();

                            return true; // Permission already granted

                        } else {
                            notificationPermissionLauncher.launch(
                                    Manifest.permission.POST_NOTIFICATIONS);
                            return false; // Wait for permission result
                        }

                    } else {

                        Toast.makeText(requireContext(),
                                "Notifications Enabled",
                                Toast.LENGTH_SHORT).show();

                        return true; // Below Android 13
                    }

                } else {

                    // 🔔 User turned OFF
                    Toast.makeText(requireContext(),
                            "Notifications Disabled",
                            Toast.LENGTH_SHORT).show();

                    return true; // Allow turning off
                }
            });
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // for notifications
    private ActivityResultLauncher<String> notificationPermissionLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notificationPermissionLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        isGranted -> {

                            SwitchPreferenceCompat notificationPref =
                                    findPreference("notifications_enabled");

                            if (notificationPref != null) {

                                if (isGranted) {
                                    notificationPref.setChecked(true);
                                } else {
                                    notificationPref.setChecked(false);
                                }
                            }
                        });
    }
}
