package com.project.realhealthbuddy.Fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.realhealthbuddy.Adapter.MedicineAdapter;
import com.project.realhealthbuddy.Model.Medicine;
import com.project.realhealthbuddy.R;
import com.project.realhealthbuddy.comman.MedicineAlarmReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class MedicineFragment extends Fragment implements MedicineAdapter.Listener {

    // ── Views ──
    private RecyclerView          rvMedicines;
    private MedicineAdapter       adapter;
    private List<Medicine>        medicineList;
    private FloatingActionButton  fabAdd;
    private View                  emptyState;
    private TextView              tvOverallProgress, tvOverallPercent, tvMedCount;
    private ProgressBar           pbOverall;

    // ── Palette colors (Azure palette) ──
    private static final String[] COLORS = {
            "#5D96E2", "#80ADE9", "#A3C4F0",
            "#638ECB", "#8CC2EE", "#B1C9EF"
    };
    private int colorIdx = 0;

    // ── SharedPreferences keys ──
    private static final String PREFS = "med_prefs";
    private static final String KEY   = "medicines";

    // ─────────────────────────────────────────────
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind views
        rvMedicines       = view.findViewById(R.id.rv_medicines);
        fabAdd            = view.findViewById(R.id.fab_add_medicine);
        emptyState        = view.findViewById(R.id.layout_empty_state);
        tvOverallProgress = view.findViewById(R.id.tv_overall_progress);
        tvOverallPercent  = view.findViewById(R.id.tv_overall_percent);
        tvMedCount        = view.findViewById(R.id.tv_medicine_count);
        pbOverall         = view.findViewById(R.id.pb_overall);

        // Load saved medicines
        medicineList = loadFromPrefs();

        // Setup RecyclerView
        adapter = new MedicineAdapter(requireContext(), medicineList, this);
        rvMedicines.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvMedicines.setAdapter(adapter);
        rvMedicines.setNestedScrollingEnabled(false);

        // FAB click
        fabAdd.setOnClickListener(v -> showAddDialog());

        refreshUI();
    }

    // ─────────────────────────────────────────────
    //  ADD MEDICINE DIALOG
    // ─────────────────────────────────────────────
    private void showAddDialog() {
        View dv = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_add_medicine, null);

        TextInputEditText etName   = dv.findViewById(R.id.et_medicine_name);
        TextInputEditText etDosage = dv.findViewById(R.id.et_dosage);
        Button            btnTime  = dv.findViewById(R.id.btn_add_timing);
        TextView          tvTimes  = dv.findViewById(R.id.tv_selected_timings);

        List<String> picked = new ArrayList<>();

        btnTime.setOnClickListener(v -> {
            if (picked.size() >= 3) {
                Toast.makeText(requireContext(),
                        "Maximum 3 timings allowed", Toast.LENGTH_SHORT).show();
                return;
            }
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(requireContext(), (tp, hour, min) -> {
                String t = String.format("%02d:%02d", hour, min);
                picked.add(t);
                tvTimes.setText("⏰ " + android.text.TextUtils.join("   ⏰ ", picked));
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        new AlertDialog.Builder(requireContext())
                .setTitle("Add Medicine")
                .setView(dv)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name   = etName.getText() != null
                            ? etName.getText().toString().trim() : "";
                    String dosage = etDosage.getText() != null
                            ? etDosage.getText().toString().trim() : "";

                    if (name.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "Please enter medicine name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (picked.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "Please pick at least one timing", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String color = COLORS[colorIdx % COLORS.length];
                    colorIdx++;

                    String today = android.text.format.DateFormat
                            .getDateFormat(requireContext())
                            .format(new java.util.Date());

                    Medicine med = new Medicine(
                            UUID.randomUUID().toString(),
                            name,
                            dosage.isEmpty() ? "1 tablet" : dosage,
                            picked, today, color
                    );

                    medicineList.add(med);
                    adapter.notifyItemInserted(medicineList.size() - 1);

                    // ── Android 12+ (API 31+) exact alarm permission check ──
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        AlarmManager alarmManager = (AlarmManager)
                                requireContext().getSystemService(Context.ALARM_SERVICE);
                        if (!alarmManager.canScheduleExactAlarms()) {
                            // Medicine saved — user just needs to grant permission
                            saveToPrefs();
                            refreshUI();
                            Toast.makeText(requireContext(),
                                    "Please allow 'Alarms & Reminders' for " + name,
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(
                                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                            startActivity(intent);
                            return;
                        }
                    }

                    // ── Permission granted — schedule alarms ──
                    scheduleAlarms(med);
                    saveToPrefs();
                    refreshUI();

                    Toast.makeText(requireContext(),
                            name + " added with " + picked.size() + " alarm(s)!",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ─────────────────────────────────────────────
    //  ALARM SCHEDULING
    // ─────────────────────────────────────────────
    private void scheduleAlarms(Medicine med) {
        AlarmManager am = (AlarmManager)
                requireContext().getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        for (int i = 0; i < med.getTimings().size(); i++) {
            String[] parts = med.getTimings().get(i).split(":");
            int hour = Integer.parseInt(parts[0]);
            int min  = Integer.parseInt(parts[1]);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE,      min);
            cal.set(Calendar.SECOND,      0);
            cal.set(Calendar.MILLISECOND, 0);
            if (cal.getTimeInMillis() <= System.currentTimeMillis())
                cal.add(Calendar.DAY_OF_YEAR, 1);

            int notifId = (med.getId() + i).hashCode();

            Intent intent = new Intent(requireContext(), MedicineAlarmReceiver.class);
            intent.putExtra(MedicineAlarmReceiver.EXTRA_MED_NAME,   med.getName());
            intent.putExtra(MedicineAlarmReceiver.EXTRA_MED_DOSAGE, med.getDosage());
            intent.putExtra(MedicineAlarmReceiver.EXTRA_TIMING,     med.getTimings().get(i));
            intent.putExtra(MedicineAlarmReceiver.EXTRA_NOTIF_ID,   notifId);

            PendingIntent pi = PendingIntent.getBroadcast(
                    requireContext(), notifId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                am.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            else
                am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
        }
    }

    private void cancelAlarms(Medicine med) {
        AlarmManager am = (AlarmManager)
                requireContext().getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;
        for (int i = 0; i < med.getTimings().size(); i++) {
            int notifId = (med.getId() + i).hashCode();
            Intent intent = new Intent(requireContext(), MedicineAlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(
                    requireContext(), notifId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            am.cancel(pi);
        }
    }

    // ─────────────────────────────────────────────
    //  ADAPTER CALLBACKS
    // ─────────────────────────────────────────────
    @Override
    public void onDelete(int position) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Remove Medicine")
                .setMessage("Remove \"" + medicineList.get(position).getName()
                        + "\" and cancel its alarms?")
                .setPositiveButton("Remove", (d, w) -> {
                    Medicine removed = medicineList.remove(position);
                    cancelAlarms(removed);
                    adapter.notifyItemRemoved(position);
                    saveToPrefs();
                    refreshUI();
                    Toast.makeText(requireContext(),
                            removed.getName() + " removed", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDoseTaken(int medicinePos, int doseIndex, boolean taken) {
        saveToPrefs();
        refreshUI();
    }

    // ─────────────────────────────────────────────
    //  UI REFRESH
    // ─────────────────────────────────────────────
    private void refreshUI() {
        boolean empty = medicineList.isEmpty();
        emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
        rvMedicines.setVisibility(empty ? View.GONE : View.VISIBLE);
        tvMedCount.setText(medicineList.size() + " active");

        int totalDoses = 0, takenDoses = 0;
        for (Medicine m : medicineList) {
            totalDoses += m.getTotalDoses();
            takenDoses += m.getTakenCount();
        }

        int pct = totalDoses > 0 ? (takenDoses * 100 / totalDoses) : 0;
        pbOverall.setProgress(pct);
        tvOverallPercent.setText(pct + "%");
        tvOverallProgress.setText(empty
                ? "No medicines added yet"
                : takenDoses + " of " + totalDoses + " doses taken today");
    }

    // ─────────────────────────────────────────────
    //  PERSISTENCE — SharedPreferences + JSON
    // ─────────────────────────────────────────────
    private void saveToPrefs() {
        SharedPreferences sp = requireContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        JSONArray arr = new JSONArray();
        for (Medicine m : medicineList) {
            try {
                JSONObject o = new JSONObject();
                o.put("id",        m.getId());
                o.put("name",      m.getName());
                o.put("dosage",    m.getDosage());
                o.put("startDate", m.getStartDate());
                o.put("color",     m.getColor());

                JSONArray timings = new JSONArray();
                for (String t : m.getTimings()) timings.put(t);
                o.put("timings", timings);

                JSONArray status = new JSONArray();
                for (Boolean b : m.getTakenStatus()) status.put(b);
                o.put("takenStatus", status);

                arr.put(o);
            } catch (JSONException e) { e.printStackTrace(); }
        }
        sp.edit().putString(KEY, arr.toString()).apply();
    }

    private List<Medicine> loadFromPrefs() {
        List<Medicine> list = new ArrayList<>();
        SharedPreferences sp = requireContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = sp.getString(KEY, null);
        if (json == null) return list;
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                List<String> timings = new ArrayList<>();
                JSONArray ta = o.getJSONArray("timings");
                for (int j = 0; j < ta.length(); j++) timings.add(ta.getString(j));

                Medicine m = new Medicine(
                        o.getString("id"),
                        o.getString("name"),
                        o.getString("dosage"),
                        timings,
                        o.getString("startDate"),
                        o.getString("color")
                );

                List<Boolean> status = new ArrayList<>();
                JSONArray sa = o.getJSONArray("takenStatus");
                for (int j = 0; j < sa.length(); j++) status.add(sa.getBoolean(j));
                m.setTakenStatus(status);

                list.add(m);
            }
        } catch (JSONException e) { e.printStackTrace(); }
        return list;
    }
}