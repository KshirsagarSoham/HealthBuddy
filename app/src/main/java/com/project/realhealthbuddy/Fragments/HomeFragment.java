package com.project.realhealthbuddy.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.realhealthbuddy.Adapter.HealthSummaryAdapter;
import com.project.realhealthbuddy.Model.HealthSummaryItem;
import com.project.realhealthbuddy.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    TextView tvUsername,tvDateandTime;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_home, container, false);

         tvUsername = view.findViewById(R.id.tvhomeusername);
         tvDateandTime = view.findViewById(R.id.tvhomedayanddate);

        preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor=preferences.edit();

        String fullName = preferences.getString("username","User");

        String firstName = fullName;
        if (fullName.contains(" ")) {
            firstName = fullName.substring(0, fullName.indexOf(" "));
        }

        tvUsername.setText("Hello, " + firstName + " ! 👋");


        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String today = "Today is " + sdf.format(new Date());
        tvDateandTime.setText(today);

//==========================================================================================================

        ArrayList<HealthSummaryItem> list = new ArrayList<>();

        list.add(new HealthSummaryItem(R.drawable.bmi, "BMI Calculator", "22.4"));
        list.add(new HealthSummaryItem(R.drawable.steps, "Steps", "4523"));
        list.add(new HealthSummaryItem(R.drawable.sleep, "Sleep", "7h 10m"));
        list.add(new HealthSummaryItem(R.drawable.water, "Water", "2.1 L"));
        list.add(new HealthSummaryItem(R.drawable.medical_adherence, "Med Adh", "85%"));

        RecyclerView rv = view.findViewById(R.id.rvhomehealthsummary);
        rv.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        rv.setHasFixedSize(false);
        rv.setAdapter(new HealthSummaryAdapter(list));




        return view;
    }
}