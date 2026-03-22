package com.project.realhealthbuddy;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class WalkBottomSheet extends BottomSheetDialogFragment {

    TextView tvSteps, tvTimer;
    Button btnStart, btnStop;

    private int stepsAtStart = 0;
    private boolean isWalking = false;

    private long startTime = 0;
    private Handler handler = new Handler();

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {

            if (!isWalking) return;

            // ⏱ Timer logic
            long millis = System.currentTimeMillis() - startTime;

            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            tvTimer.setText(String.format("%02d:%02d", minutes, seconds));

            // 👣 Live steps logic (SESSION steps)
            int stepsNow = getCurrentSteps();
            int walked = stepsNow - stepsAtStart;

//            tvSteps.setText("Live: " + getCurrentSteps());
              tvSteps.setText("🚶 Walking... Keep going!");

            handler.postDelayed(this, 1000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottomsheet_walk, container, false);

        tvSteps = view.findViewById(R.id.tvWalkSteps);
        tvTimer = view.findViewById(R.id.tvWalkTimer);
        btnStart = view.findViewById(R.id.btnWalkStart);
        btnStop = view.findViewById(R.id.btnWalkStop);

        // ✅ Show today's steps initially
        tvSteps.setText("Press Start to begin 🚶");
        tvTimer.setText("00:00");

        btnStart.setOnClickListener(v -> startWalk());
        btnStop.setOnClickListener(v -> stopWalk());

        return view;
    }

    private void startWalk() {
        isWalking = true;

        startTime = System.currentTimeMillis();
        stepsAtStart = getCurrentSteps();

        handler.post(timerRunnable);
    }

    private void stopWalk() {
        isWalking = false;

        handler.removeCallbacks(timerRunnable);

        int stepsNow = getCurrentSteps();
        int walked = stepsNow - stepsAtStart;

        tvSteps.setText("You Walked: " + walked + " steps");
    }

    private int getCurrentSteps() {
        return com.project.realhealthbuddy.Steps.StepService.CURRENT_STEPS;
    }
}