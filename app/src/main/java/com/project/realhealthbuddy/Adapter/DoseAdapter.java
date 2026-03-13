package com.project.realhealthbuddy.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.realhealthbuddy.Model.DoseModel;
import com.project.realhealthbuddy.R;

import java.util.List;

public class DoseAdapter extends RecyclerView.Adapter<DoseAdapter.ViewHolder> {

    List<DoseModel> doseList;

    public DoseAdapter(List<DoseModel> doseList) {
        this.doseList = doseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dose_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DoseModel dose = doseList.get(position);

        holder.medicineName.setText(dose.getName());
        holder.medicineTime.setText(dose.getTime());

        String status = getDoseStatus(dose);

        holder.doseStatus.setText(status);

        if (status.equals("Taken")) {

            holder.doseStatus.setTextColor(0xFF4CAF50);

        } else if (status.equals("Missed")) {

            holder.doseStatus.setTextColor(0xFFF44336);

        } else {

            holder.doseStatus.setTextColor(0xFF757575);
        }
    }

    @Override
    public int getItemCount() {
        return doseList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView medicineName;
        TextView medicineTime;
        TextView doseStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            medicineName = itemView.findViewById(R.id.medicineName);
            medicineTime = itemView.findViewById(R.id.medicineTime);
            doseStatus = itemView.findViewById(R.id.doseStatus);
        }
    }

    private String getDoseStatus(DoseModel dose) {

        if (dose.isTaken()) {
            return "Taken";
        }

        try {

            java.text.SimpleDateFormat format =
                    new java.text.SimpleDateFormat("HH:mm");

            java.util.Calendar now = java.util.Calendar.getInstance();

            java.util.Date parsedTime = format.parse(dose.getTime());

            java.util.Calendar doseCal = java.util.Calendar.getInstance();
            doseCal.setTime(parsedTime);

            // set today's date with medicine time
            doseCal.set(now.get(java.util.Calendar.YEAR),
                    now.get(java.util.Calendar.MONTH),
                    now.get(java.util.Calendar.DAY_OF_MONTH));

            if (now.after(doseCal)) {
                return "Missed";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Upcoming";
    }
}