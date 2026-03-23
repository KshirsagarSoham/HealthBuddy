package com.project.realhealthbuddy.Steps.Data;


import android.content.Context;
import android.content.SharedPreferences;
import com.project.realhealthbuddy.Model.Medicine;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class MedicineDataManager {
    private static final String PREFS = "med_prefs";
    private static final String KEY = "medicines";

    public static List<Medicine> getTodayMedicines(Context context) {
        List<Medicine> todayMedicines = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = sp.getString(KEY, null);

        if (json == null) return todayMedicines;

        try {
            JSONArray arr = new JSONArray(json);
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                String startDate = o.getString("startDate");

                // Show medicines from today onwards
                if (isTodayOrFuture(startDate, today)) {
                    List<String> timings = new ArrayList<>();
                    JSONArray ta = o.getJSONArray("timings");
                    for (int j = 0; j < ta.length(); j++) {
                        timings.add(ta.getString(j));
                    }

                    Medicine med = new Medicine(
                            o.getString("id"),
                            o.getString("name"),
                            o.getString("dosage"),
                            timings,
                            o.getString("startDate"),
                            o.getString("color")
                    );

                    // Load taken status
                    List<Boolean> status = new ArrayList<>();
                    try {
                        JSONArray sa = o.getJSONArray("takenStatus");
                        for (int j = 0; j < sa.length(); j++) {
                            status.add(sa.getBoolean(j));
                        }
                    } catch (Exception e) {
                        // Default false if no status
                        for (int j = 0; j < timings.size(); j++) status.add(false);
                    }
                    med.setTakenStatus(status);

                    todayMedicines.add(med);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return todayMedicines;
    }

    private static boolean isTodayOrFuture(String startDate, String today) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdf.parse(startDate).getTime() <= sdf.parse(today).getTime();
        } catch (Exception e) {
            return true;
        }
    }
}