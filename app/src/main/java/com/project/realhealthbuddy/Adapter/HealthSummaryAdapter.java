package com.project.realhealthbuddy.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.realhealthbuddy.Model.HealthSummaryItem;
import com.project.realhealthbuddy.R;

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

