package com.project.realhealthbuddy.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.realhealthbuddy.BMI_Activity;
import com.project.realhealthbuddy.Model.HealthSummaryItem;
import com.project.realhealthbuddy.R;
import com.project.realhealthbuddy.SleepActivity;
import com.project.realhealthbuddy.Steps.StepsActivity;
import com.project.realhealthbuddy.WaterActivity;

import java.util.ArrayList;

public class HealthSummaryAdapter
        extends RecyclerView.Adapter<HealthSummaryAdapter.ViewHolder> {

    ArrayList<HealthSummaryItem> list;

    public HealthSummaryAdapter(ArrayList<HealthSummaryItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_health_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HealthSummaryItem item = list.get(position);
        holder.icon.setImageResource(item.icon);
        holder.title.setText(item.title);
        holder.value.setText(item.value);

        holder.itemView.setOnClickListener(v -> {

            if (item.type.equals(HealthSummaryItem.TYPE_BMI)) {
                Intent intent = new Intent(v.getContext(), BMI_Activity.class);
                v.getContext().startActivity(intent);
            }
            else if (item.type.equals(HealthSummaryItem.TYPE_SLEEP)) {
                Intent intent = new Intent(v.getContext(), SleepActivity.class);
                v.getContext().startActivity(intent);
            }
            else if (item.type.equals(HealthSummaryItem.TYPE_WATER)) {
                Intent intent = new Intent(v.getContext(), WaterActivity.class);
                v.getContext().startActivity(intent);

            } else if (item.type.equals(HealthSummaryItem.TYPE_STEPS)) {
                Intent intent = new Intent(v.getContext(), StepsActivity.class);
                v.getContext().startActivity(intent);

            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title, value;

        ViewHolder(View v) {
            super(v);
            icon = v.findViewById(R.id.ivhealthsummaryicon);
            title = v.findViewById(R.id.tvhealthsummarytitle);
            value = v.findViewById(R.id.tvhealthsummaryvalue);
        }
    }
}

