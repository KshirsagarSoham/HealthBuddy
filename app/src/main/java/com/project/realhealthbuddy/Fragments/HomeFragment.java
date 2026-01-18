package com.project.realhealthbuddy.Fragments;

import android.content.Context;
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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.project.realhealthbuddy.Adapter.HealthSummaryAdapter;
import com.project.realhealthbuddy.BreathingBottomSheet;
import com.project.realhealthbuddy.HealthTipsProvider;
import com.project.realhealthbuddy.MainActivity;
import com.project.realhealthbuddy.Model.HealthSummaryItem;
import com.project.realhealthbuddy.R;
import com.project.realhealthbuddy.SleepBottomSheet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    TextView tvUsername,tvDateandTime;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private ArrayList<HealthSummaryItem> list;
    private HealthSummaryAdapter adapter;


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


        String firstName = preferences.getString("username","Guest");
        if (fullName.contains(" ")) {
            firstName = fullName.substring(0, fullName.indexOf(" "));
        }

        tvUsername.setText("Hello, " + firstName + " ! 👋");


        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
        String today = "Today is "+ sdf.format(new Date());
        tvDateandTime.setText(today);


//==========================================================================================================

        list = new ArrayList<>();


        list.add(new HealthSummaryItem(R.drawable.bmi, "BMI Calculator", "0.00",HealthSummaryItem.TYPE_BMI));
        list.add(new HealthSummaryItem(R.drawable.steps, "Steps", "0000",HealthSummaryItem.TYPE_STEPS));
        list.add(new HealthSummaryItem(R.drawable.sleep, "Sleep", "0h 0m",HealthSummaryItem.TYPE_SLEEP));
        list.add(new HealthSummaryItem(R.drawable.water, "Water", "0.0",HealthSummaryItem.TYPE_WATER));
        list.add(new HealthSummaryItem(R.drawable.medical_adherence, "Med Adh", "0",HealthSummaryItem.TYPE_MED));




        RecyclerView rv = view.findViewById(R.id.rvhomehealthsummary);
        rv.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        rv.setHasFixedSize(false);

        adapter = new HealthSummaryAdapter(list);
        rv.setAdapter(adapter);





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
        imgBreathing.setImageResource(R.drawable.breathing);

        TextView tvBreathingTitle = breathing.findViewById(R.id.tvWellnessTitle);
        tvBreathingTitle.setText("Breathing");

        TextView tvBreathingSubtitle = breathing.findViewById(R.id.tvWellnessSubtitle);
        tvBreathingSubtitle.setText("Relax");

        //Walking
        ImageView imgWalk = walk.findViewById(R.id.imgWellnessIcon);
        imgWalk.setImageResource(R.drawable.steps_wellness);

        TextView tvWalkTitle = walk.findViewById(R.id.tvWellnessTitle);
        tvWalkTitle.setText("Walk");

        TextView tvWalkSubtitle = walk.findViewById(R.id.tvWellnessSubtitle);
        tvWalkSubtitle.setText("5 min");

        // Sleep
        ImageView imgSleep = sleep.findViewById(R.id.imgWellnessIcon);
        imgSleep.setImageResource(R.drawable.sleep_wellness);

        TextView tvSleepTitle = sleep.findViewById(R.id.tvWellnessTitle);
        tvSleepTitle.setText("Sleep Tips");

        TextView tvSleepSubtitle = sleep.findViewById(R.id.tvWellnessSubtitle);
        tvSleepSubtitle.setText("Better rest");

        // Water
        ImageView imgWater = water.findViewById(R.id.imgWellnessIcon);
        imgWater.setImageResource(R.drawable.hydration_wellness);

        TextView tvWaterTitle = water.findViewById(R.id.tvWellnessTitle);
        tvWaterTitle.setText("Water");

        TextView tvWaterSubtitle = water.findViewById(R.id.tvWellnessSubtitle);
        tvWaterSubtitle.setText("Drink now");


        breathing.setOnClickListener(v -> {
            BreathingBottomSheet sheet = new BreathingBottomSheet();
            sheet.show(getParentFragmentManager(), "BreathingSheet");
        });

        sleep.setOnClickListener(v -> {
            SleepBottomSheet sheet = new SleepBottomSheet();
            sheet.show(getParentFragmentManager(), "SleepSheet");
        });

        walk.setOnClickListener(v -> {
            Snackbar.make(v,
                            "🚶 Take a 5–10 minute walk today",
                            Snackbar.LENGTH_LONG)
                    .setAction("Done", snack -> {
                        // Optional future logic
                    })
                    .show();
        });

        water.setOnClickListener(v -> {
            Toast.makeText(requireContext(),
                    "💧 Drink a glass of water",
                    Toast.LENGTH_SHORT).show();
        });

//=====================================================================================================================


        TextView tvHealthTip = view.findViewById(R.id.tvHealthTip);
        tvHealthTip.setText(HealthTipsProvider.getRandomTip());












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

    @Override
    public void onResume() {
        super.onResume();
        updateWaterCard();
    }


    public void updateWaterCard() {

        SharedPreferences prefs = requireContext()
                .getSharedPreferences("health_data", Context.MODE_PRIVATE);

        int waterMl = prefs.getInt("water_ml", 0);

        String displayValue;
        if (waterMl == 0) {
            displayValue = "0.0 L";
        } else {
            displayValue = String.format("%.1f L", waterMl / 1000f);
        }

        // Find WATER item and update it
        for (int i = 0; i < list.size(); i++) {
            HealthSummaryItem item = list.get(i);

            if (HealthSummaryItem.TYPE_WATER.equals(item.getType())) {
                item.setValue(displayValue);
                adapter.notifyItemChanged(i);
                break;
            }

        }
    }


}