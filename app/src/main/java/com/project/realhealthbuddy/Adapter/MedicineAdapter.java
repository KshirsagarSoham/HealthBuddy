package com.project.realhealthbuddy.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.realhealthbuddy.Model.Medicine;
import com.project.realhealthbuddy.R;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    public interface Listener {
        void onDelete(int position);
        void onDoseTaken(int medicinePos, int doseIndex, boolean taken);
    }

    private final Context  context;
    private List<Medicine> list;
    private final Listener listener;

    public MedicineAdapter(Context context, List<Medicine> list, Listener listener) {
        this.context  = context;
        this.list     = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_medicine, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Medicine m = list.get(position);

        h.tvName.setText(m.getName());
        h.tvDosage.setText(m.getDosage());

        // Build timing string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m.getTimings().size(); i++) {
            sb.append("⏰ ").append(m.getTimings().get(i));
            if (i < m.getTimings().size() - 1) sb.append("   ");
        }
        h.tvTimings.setText(sb.toString());

        // Apply color to band + progress bar
        try {
            int color = Color.parseColor(m.getColor());
            h.colorBand.setBackgroundColor(color);
            h.progressBar.setProgressTintList(ColorStateList.valueOf(color));
        } catch (Exception ignored) {}

        // Progress
        int total = m.getTotalDoses();
        int taken = m.getTakenCount();
        h.progressBar.setMax(total == 0 ? 1 : total);
        h.progressBar.setProgress(taken);
        h.tvProgress.setText(taken + " / " + total);

        // Bind dose rows (up to 3)
        bindDose(h, h.row1, h.check1, h.tag1, m, 0, total);
        bindDose(h, h.row2, h.check2, h.tag2, m, 1, total);
        bindDose(h, h.row3, h.check3, h.tag3, m, 2, total);

        // Delete
        h.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(h.getAdapterPosition());
        });
    }

    private void bindDose(ViewHolder h, LinearLayout row,
                          CheckBox cb, TextView tag,
                          Medicine m, int idx, int total) {
        if (idx < total) {
            row.setVisibility(View.VISIBLE);
            cb.setOnCheckedChangeListener(null); // clear old listener
            cb.setText(m.getTimings().get(idx));
            cb.setChecked(m.getTakenStatus().get(idx));

            // Set initial tag state
            updateTag(tag, m.getTakenStatus().get(idx));

            cb.setOnCheckedChangeListener((btn, isChecked) -> {
                m.getTakenStatus().set(idx, isChecked);

                // Update tag text + color
                updateTag(tag, isChecked);

                // Update progress
                int t = m.getTakenCount();
                h.progressBar.setProgress(t);
                h.tvProgress.setText(t + " / " + m.getTotalDoses());

                if (listener != null)
                    listener.onDoseTaken(h.getAdapterPosition(), idx, isChecked);
            });
        } else {
            row.setVisibility(View.GONE);
        }
    }

    // ── Updates the tag chip: "✓ Done" (green) or "Pending" (blue) ──
    private void updateTag(TextView tag, boolean isDone) {
        if (isDone) {
            tag.setText("✓ Done");
            tag.setTextColor(Color.parseColor("#2E7D32"));       // dark green text
            tag.setBackgroundResource(R.drawable.tag_done_bg);   // green background
        } else {
            tag.setText("Pending");
            tag.setTextColor(Color.parseColor("#5D96E2"));        // azure text
            tag.setBackgroundResource(R.drawable.chip_timing_bg); // pale blue background
        }
    }

    @Override
    public int getItemCount() { return list.size(); }

    public void setList(List<Medicine> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View        colorBand;
        TextView    tvName, tvDosage, tvTimings, tvProgress;
        ProgressBar progressBar;
        ImageButton btnDelete;
        LinearLayout row1, row2, row3;
        CheckBox     check1, check2, check3;
        TextView     tag1, tag2, tag3;

        ViewHolder(@NonNull View v) {
            super(v);
            colorBand   = v.findViewById(R.id.view_color_band);
            tvName      = v.findViewById(R.id.tv_med_name);
            tvDosage    = v.findViewById(R.id.tv_med_dosage);
            tvTimings   = v.findViewById(R.id.tv_med_timings);
            tvProgress  = v.findViewById(R.id.tv_med_progress);
            progressBar = v.findViewById(R.id.pb_med_card);
            btnDelete   = v.findViewById(R.id.btn_med_delete);
            row1        = v.findViewById(R.id.row_dose1);
            row2        = v.findViewById(R.id.row_dose2);
            row3        = v.findViewById(R.id.row_dose3);
            check1      = v.findViewById(R.id.cb_dose1);
            check2      = v.findViewById(R.id.cb_dose2);
            check3      = v.findViewById(R.id.cb_dose3);
            tag1        = v.findViewById(R.id.tag_dose1);
            tag2        = v.findViewById(R.id.tag_dose2);
            tag3        = v.findViewById(R.id.tag_dose3);
        }
    }
}