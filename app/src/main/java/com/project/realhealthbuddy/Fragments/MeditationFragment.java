package com.project.realhealthbuddy.Fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.project.realhealthbuddy.R;

import java.util.Locale;

public class MeditationFragment extends Fragment {

    private MaterialCardView cardStressed, cardOverthinking,
            cardLowMood, cardLowEnergy, cardNeutral;

    private TextView tvTimer;
    private Button btnStart, btnPause, btnReset;
    private View breathCircle;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 300000;
    private boolean timerRunning = false;

    private ObjectAnimator scaleXAnimator;
    private ObjectAnimator scaleYAnimator;

    public MeditationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meditation, container, false);

        cardStressed = view.findViewById(R.id.cardStressed);
        cardOverthinking = view.findViewById(R.id.cardOverthinking);
        cardLowMood = view.findViewById(R.id.cardLowMood);
        cardLowEnergy = view.findViewById(R.id.cardLowEnergy);
        cardNeutral = view.findViewById(R.id.cardNeutral);

        tvTimer = view.findViewById(R.id.tvTimer);
        btnStart = view.findViewById(R.id.btnStart);
        btnPause = view.findViewById(R.id.btnPause);
        btnReset = view.findViewById(R.id.btnReset);
        breathCircle = view.findViewById(R.id.breathCircle);

        updateTimerText();

        setMoodClick(cardStressed, "Stressed 😰");
        setMoodClick(cardOverthinking, "Overthinking 🤯");
        setMoodClick(cardLowMood, "Low Mood 😔");
        setMoodClick(cardLowEnergy, "Low Energy 😴");
        setMoodClick(cardNeutral, "Neutral 😐");

        btnStart.setOnClickListener(v -> startTimer());
        btnPause.setOnClickListener(v -> pauseTimer());
        btnReset.setOnClickListener(v -> resetTimer());

        return view;
    }

    private void setMoodClick(MaterialCardView card, String moodName) {
        card.setOnClickListener(v -> {
            resetAllCards();
            card.setCardBackgroundColor(Color.parseColor("#D0E8FF"));
            showMoodBottomSheet(moodName);
        });
    }

    private void resetAllCards() {
        cardStressed.setCardBackgroundColor(Color.WHITE);
        cardOverthinking.setCardBackgroundColor(Color.WHITE);
        cardLowMood.setCardBackgroundColor(Color.WHITE);
        cardLowEnergy.setCardBackgroundColor(Color.WHITE);
        cardNeutral.setCardBackgroundColor(Color.WHITE);
    }

    // 🔥 SMART DYNAMIC BOTTOM SHEET
    private void showMoodBottomSheet(String moodName) {

        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View view = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_dynamic, null);

        TextView tvEmoji = view.findViewById(R.id.tvEmoji);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        EditText etInput = view.findViewById(R.id.etInput);
        Button btnPrimary = view.findViewById(R.id.btnPrimary);
        Button btnClose = view.findViewById(R.id.btnClose);

        etInput.setVisibility(View.GONE);

        switch (moodName) {

            case "Stressed 😰":
                tvEmoji.setText("😰");
                tvTitle.setText("2-Min Quick Calm");
                tvMessage.setText("Use 5-4-3-2-1 grounding method.\nStart slow breathing.");
                btnPrimary.setText("Start Breathing");
                btnPrimary.setOnClickListener(v -> {
                    dialog.dismiss();
                    startTimer();
                });
                break;

            case "Overthinking 🤯":
                tvEmoji.setText("🤯");
                tvTitle.setText("Clear Your Thoughts");
                tvMessage.setText("Write what's on your mind.");
                etInput.setHint("Type your thoughts...");
                etInput.setVisibility(View.VISIBLE);
                btnPrimary.setText("Release");
                btnPrimary.setOnClickListener(v -> etInput.setText(""));
                break;

            case "Low Mood 😔":
                tvEmoji.setText("😔");
                tvTitle.setText("Micro Motivation");
                tvMessage.setText("Small steps still move you forward.\nStand up and stretch.");
                etInput.setHint("Write one thing you're grateful for...");
                etInput.setVisibility(View.VISIBLE);
                btnPrimary.setText("I Did It");
                break;

            case "Low Energy 😴":
                tvEmoji.setText("😴");
                tvTitle.setText("Energy Boost");
                tvMessage.setText("Do 30 sec power breathing.\nDrink water now.");
                btnPrimary.setText("Boost Me");
                btnPrimary.setOnClickListener(v -> {
                    dialog.dismiss();
                    startTimer();
                });
                break;

            case "Neutral 😐":
                tvEmoji.setText("😐");
                tvTitle.setText("Reflection");
                tvMessage.setText("Would you like to meditate for 5 minutes?");
                btnPrimary.setText("Start 5 Min Session");
                btnPrimary.setOnClickListener(v -> {
                    dialog.dismiss();
                    startTimer();
                });
                break;
        }

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(view);
        dialog.show();
    }

    private void startTimer() {
        if (timerRunning) return;

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                stopBreathingAnimation();
            }
        }.start();

        timerRunning = true;
        startBreathingAnimation();
    }

    private void pauseTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        timerRunning = false;
        stopBreathingAnimation();
    }

    private void resetTimer() {
        pauseTimer();
        timeLeftInMillis = 300000;
        updateTimerText();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted =
                String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);
    }

    private void startBreathingAnimation() {
        scaleXAnimator = ObjectAnimator.ofFloat(breathCircle, "scaleX", 1f, 1.4f);
        scaleYAnimator = ObjectAnimator.ofFloat(breathCircle, "scaleY", 1f, 1.4f);

        scaleXAnimator.setDuration(4000);
        scaleYAnimator.setDuration(4000);

        scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);

        scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);

        scaleXAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        scaleXAnimator.start();
        scaleYAnimator.start();
    }

    private void stopBreathingAnimation() {
        if (scaleXAnimator != null) scaleXAnimator.cancel();
        if (scaleYAnimator != null) scaleYAnimator.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) countDownTimer.cancel();
        stopBreathingAnimation();
    }
}