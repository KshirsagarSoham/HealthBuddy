package com.project.realhealthbuddy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.realhealthbuddy.Adapter.DoseAdapter;
import com.project.realhealthbuddy.Model.DoseModel;
import com.project.realhealthbuddy.Model.Medicine;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MedicalAdherenceActivity extends AppCompatActivity {

    ProgressBar adherenceProgress;
    TextView adherencePercent, doseSummary;
    TextView takenCount, missedCount, upcomingCount;

    RecyclerView recyclerView;
    DoseAdapter adapter;
    List<DoseModel> doseList;

    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_adherence);

        // Initialize views
        adherenceProgress = findViewById(R.id.adherenceProgress);
        adherencePercent = findViewById(R.id.adherencePercent);
        doseSummary = findViewById(R.id.doseSummary);

        takenCount = findViewById(R.id.takenCount);
        missedCount = findViewById(R.id.missedCount);
        upcomingCount = findViewById(R.id.upcomingCount);

        recyclerView = findViewById(R.id.recyclerViewDoses);
        backbutton = findViewById(R.id.backbutton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load medicines from SharedPreferences
        loadMedicines();

        adapter = new DoseAdapter(doseList);
        recyclerView.setAdapter(adapter);

        calculateStats();

        backbutton.setOnClickListener(v -> finish());
    }

    // ─────────────────────────────────────────────
    // LOAD MEDICINES FROM PREFS (saved by fragment)
    // ─────────────────────────────────────────────

    private void loadMedicines() {

        doseList = new ArrayList<>();

        SharedPreferences sp = getSharedPreferences("med_prefs", MODE_PRIVATE);

        String json = sp.getString("medicines", null);

        if (json == null) return;

        try {

            org.json.JSONArray arr = new org.json.JSONArray(json);

            for (int i = 0; i < arr.length(); i++) {

                org.json.JSONObject o = arr.getJSONObject(i);

                String name = o.getString("name");

                org.json.JSONArray timings = o.getJSONArray("timings");

                for (int j = 0; j < timings.length(); j++) {

                    String time = timings.getString(j);

                    doseList.add(new DoseModel(name, time, false));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ─────────────────────────────────────────────
    // CALCULATE ADHERENCE STATS
    // ─────────────────────────────────────────────

    private void calculateStats() {

        int taken = 0;
        int missed = 0;
        int upcoming = 0;

        for (DoseModel dose : doseList) {

            if (dose.isTaken()) {

                taken++;

            } else {

                try {

                    SimpleDateFormat format =
                            new SimpleDateFormat("HH:mm");

                    Calendar now = Calendar.getInstance();

                    Calendar doseCal = Calendar.getInstance();
                    doseCal.setTime(format.parse(dose.getTime()));

                    doseCal.set(now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH));

                    if (now.after(doseCal)) {

                        missed++;

                    } else {

                        upcoming++;

                    }

                } catch (Exception e) {

                    upcoming++;

                }
            }
        }

        int total = doseList.size();

        takenCount.setText(String.valueOf(taken));
        missedCount.setText(String.valueOf(missed));
        upcomingCount.setText(String.valueOf(upcoming));

        int percentage = 0;

        if (total > 0) {
            percentage = (taken * 100) / total;
        }

        adherenceProgress.setProgress(percentage);
        adherencePercent.setText(percentage + "%");
        doseSummary.setText(taken + " / " + total + " doses taken");
    }


}