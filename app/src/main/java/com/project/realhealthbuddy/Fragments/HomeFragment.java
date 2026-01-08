package com.project.realhealthbuddy.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.project.realhealthbuddy.Adapter.HealthSummaryAdapter;
import com.project.realhealthbuddy.MainActivity;
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


//==============================================================================================================

        MaterialCardView mcvAddMed,mcvProgress,mcvMeditation;

        mcvMeditation = view.findViewById(R.id.mcvhomemeditation);
        mcvProgress = view.findViewById(R.id.mcvhomeprogress);
        mcvAddMed = view.findViewById(R.id.mcvhomeaddmedicine);


        mcvMeditation.setOnClickListener(v -> navigateToTab(R.id.homebottommenuMeditation));


//================================================================================================================

        View med1 = view.findViewById(R.id.medicineItem1);
        View med2 = view.findViewById(R.id.medicineItem2);
        TextView viewAll = view.findViewById(R.id.tvViewAllMedicines);

        TextView name1 = med1.findViewById(R.id.tvMedicineName);
        TextView time1 = med1.findViewById(R.id.tvMedicineTime);
        TextView status1 = med1.findViewById(R.id.tvMedicineStatus);

        TextView name2 = med2.findViewById(R.id.tvMedicineName);
        TextView time2 = med2.findViewById(R.id.tvMedicineTime);
        TextView status2 = med2.findViewById(R.id.tvMedicineStatus);

        //  for hiding/showing medicine list
        LinearLayout layoutNoMedicine = view.findViewById(R.id.layoutNoMedicine);
        LinearLayout layoutMedicineList = view.findViewById(R.id.layoutMedicineList);

       // TEMPORARY flag (later this comes from DB)
        boolean hasMedicinesToday = false;

        if (!hasMedicinesToday) {
            layoutMedicineList.setVisibility(View.GONE);
            layoutNoMedicine.setVisibility(View.VISIBLE);
        }
        else {
            layoutMedicineList.setVisibility(View.VISIBLE);
            layoutNoMedicine.setVisibility(View.GONE);
        }



        // Smaller text for Home
        name1.setTextSize(14);
        time1.setTextSize(12);
        status1.setTextSize(11);

        name2.setTextSize(14);
        time2.setTextSize(12);
        status2.setTextSize(11);

        name1.setText("Paracetamol");
        time1.setText("9:00 AM");
        status1.setText("Upcoming");

        name2.setText("Vitamin D");
        time2.setText("2:00 PM");
        status2.setText("Upcoming");

        status1.setBackgroundResource(R.drawable.bg_status_upcoming);
        status2.setBackgroundResource(R.drawable.bg_status_upcoming);


        med1.setOnClickListener(v -> openMedicineFragment());
        med2.setOnClickListener(v -> openMedicineFragment());

        //mcvAddMed.setOnClickListener(v -> openMedicineFragment());

        viewAll.setOnClickListener(v -> openMedicineFragment());

//============================================================================================================

        View breathing = view.findViewById(R.id.actionBreathing);
        View walk = view.findViewById(R.id.actionWalk);
        View sleep = view.findViewById(R.id.actionSleep);
        View water = view.findViewById(R.id.actionWater);

        //Breathing
        ImageView imgBreathing = breathing.findViewById(R.id.imgWellnessIcon);
        imgBreathing.setImageResource(R.drawable.meditation_ui);

        TextView tvBreathingTitle = breathing.findViewById(R.id.tvWellnessTitle);
        tvBreathingTitle.setText("Breathing");

        TextView tvBreathingSubtitle = breathing.findViewById(R.id.tvWellnessSubtitle);
        tvBreathingSubtitle.setText("Relax");

        //Walking
        ImageView imgWalk = walk.findViewById(R.id.imgWellnessIcon);
        imgWalk.setImageResource(R.drawable.steps);

        TextView tvWalkTitle = walk.findViewById(R.id.tvWellnessTitle);
        tvWalkTitle.setText("Walk");

        TextView tvWalkSubtitle = walk.findViewById(R.id.tvWellnessSubtitle);
        tvWalkSubtitle.setText("5 min");

        // Sleep
        ImageView imgSleep = sleep.findViewById(R.id.imgWellnessIcon);
        imgSleep.setImageResource(R.drawable.sleep);

        TextView tvSleepTitle = sleep.findViewById(R.id.tvWellnessTitle);
        tvSleepTitle.setText("Sleep");

        TextView tvSleepSubtitle = sleep.findViewById(R.id.tvWellnessSubtitle);
        tvSleepSubtitle.setText("Better rest");

        // Water
        ImageView imgWater = water.findViewById(R.id.imgWellnessIcon);
        imgWater.setImageResource(R.drawable.water);

        TextView tvWaterTitle = water.findViewById(R.id.tvWellnessTitle);
        tvWaterTitle.setText("Water");

        TextView tvWaterSubtitle = water.findViewById(R.id.tvWellnessSubtitle);
        tvWaterSubtitle.setText("Drink now");








        return view;
    }
    private void navigateToTab(int menuId) {
        // Get BottomNavigationView from the parent activity
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.homeBottomNavigationView);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(menuId);
        }
    }
    private void openMedicineFragment() {
        FragmentTransaction transaction =
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

        transaction.replace(R.id.homeframelayout, new MedicineFragment());
        transaction.addToBackStack(null);
        transaction.commit();

        // Update BottomNavigationView selection
        BottomNavigationView bottomNav =
                requireActivity().findViewById(R.id.homeBottomNavigationView);

        bottomNav.setSelectedItemId(R.id.homebottommenuMedicine);
    }

}