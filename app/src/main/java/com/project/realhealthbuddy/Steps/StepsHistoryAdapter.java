package com.project.realhealthbuddy.Steps;


import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project.realhealthbuddy.R;
import com.project.realhealthbuddy.Steps.Data.StepsEntity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class StepsHistoryAdapter extends RecyclerView.Adapter<StepsHistoryAdapter.ViewHolder> {

    private List<StepsEntity> list;
    private String todayDate;



    public StepsHistoryAdapter(List<StepsEntity> list, String todayDate) {
        this.list = list;
        this.todayDate = todayDate;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StepsEntity entity = list.get(position);


        // Format Date
        try {
            SimpleDateFormat inputFormat =
                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            SimpleDateFormat outputFormat =
                    new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());

            holder.dayText.setText(
                    outputFormat.format(inputFormat.parse(entity.date))
            );

        } catch (Exception e) {
            holder.dayText.setText(entity.date);
        }

        holder.stepsText.setText(entity.steps + " steps");


        // 🎯 Highlight Today
        if (entity.date.equals(todayDate)) {

            holder.dayText.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.black)
            );

            holder.stepsText.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.black)
            );

            holder.stepsText.setTypeface(null, Typeface.BOLD);

        } else {

            holder.dayText.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.gray)
            );

            holder.stepsText.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.gray)
            );

            holder.stepsText.setTypeface(null, Typeface.NORMAL);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView dayText, stepsText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayText);
            stepsText = itemView.findViewById(R.id.stepsHistoryText);
        }
    }
    public void updateList(List<StepsEntity> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

}
