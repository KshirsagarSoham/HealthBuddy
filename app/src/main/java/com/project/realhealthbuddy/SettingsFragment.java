package com.project.realhealthbuddy;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;

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
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}
