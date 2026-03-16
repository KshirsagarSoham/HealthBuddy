package com.project.realhealthbuddy;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class BMI_Activity extends AppCompatActivity {

    ImageView back;

    TextInputEditText etHeight, etWeight, etAge;

    TextView tvBmiValue, tvResult, tvHealthTip;
    TextView barUnder, barNormal, barOver, barObese;
    TextView tvInfoTitle, tvInfo;

    ProgressBar bmiMeter;

    View resultLayout;
    ImageView bmiPointer;
    LinearLayout bmiBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        back = findViewById(R.id.backbutton);

        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etAge = findViewById(R.id.etAge);

        tvBmiValue = findViewById(R.id.tvBmiValue);
        tvResult = findViewById(R.id.tvResult);
        tvHealthTip = findViewById(R.id.tvHealthTip);

        barUnder = findViewById(R.id.barUnder);
        barNormal = findViewById(R.id.barNormal);
        barOver = findViewById(R.id.barOver);
        barObese = findViewById(R.id.barObese);

        tvInfoTitle = findViewById(R.id.tvBmiInfoTitle);
        tvInfo = findViewById(R.id.tvBmiInfo);

        bmiMeter = findViewById(R.id.bmiMeter);

        resultLayout = findViewById(R.id.resultLayout);

        bmiPointer = findViewById(R.id.bmiPointer);
        bmiBar = findViewById(R.id.bmiBar);

        findViewById(R.id.btnCalculate).setOnClickListener(v -> calculateBMI());
        findViewById(R.id.btnReset).setOnClickListener(v -> resetFields());

        back.setOnClickListener(v -> finish());

        tvInfoTitle.setOnClickListener(v -> {
            if (tvInfo.getVisibility() == View.GONE)
                tvInfo.setVisibility(View.VISIBLE);
            else
                tvInfo.setVisibility(View.GONE);
        });
    }

    private void calculateBMI() {

        String heightStr = etHeight.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Enter height and weight", Toast.LENGTH_SHORT).show();
            return;
        }

        double height = Double.parseDouble(heightStr) / 100;
        double weight = Double.parseDouble(weightStr);

        double bmi = weight / (height * height);

        movePointer(bmi);

        String bmiFormatted = String.format("%.1f", bmi);

        tvBmiValue.setText(bmiFormatted);

        bmiMeter.setProgress((int) bmi);

        String category;
        String tip;

        if (bmi < 18.5) {
            category = "Underweight";
            tip = "You are underweight. Increase calorie intake.";
            highlightBar(barUnder);
        } else if (bmi < 25) {
            category = "Normal";
            tip = "Great! Maintain balanced diet and exercise.";
            highlightBar(barNormal);
        } else if (bmi < 30) {
            category = "Overweight";
            tip = "Try regular exercise and reduce sugary foods.";
            highlightBar(barOver);
        } else {
            category = "Obese";
            tip = "Consult a doctor and start a fitness plan.";
            highlightBar(barObese);
        }

        tvResult.setText(category);
        tvHealthTip.setText("💡 " + tip);

        SharedPreferences prefs = getSharedPreferences("health_data", MODE_PRIVATE);
        prefs.edit().putString("bmi_value", bmiFormatted).apply();

        resultLayout.setVisibility(View.VISIBLE);
    }

    private void highlightBar(TextView selected) {

        barUnder.setAlpha(0.5f);
        barNormal.setAlpha(0.5f);
        barOver.setAlpha(0.5f);
        barObese.setAlpha(0.5f);

        selected.setAlpha(1f);
    }

    private void resetFields() {

        etAge.setText("");
        etHeight.setText("");
        etWeight.setText("");

        resultLayout.setVisibility(View.GONE);

        barUnder.setAlpha(1f);
        barNormal.setAlpha(1f);
        barOver.setAlpha(1f);
        barObese.setAlpha(1f);
    }

    private void movePointer(double bmi) {

        bmiBar.post(() -> {

            int barWidth = bmiBar.getWidth();
            float segmentWidth = barWidth / 4f;

            float position;

            if (bmi < 18.5) {
                position = (float) ((bmi / 18.5) * segmentWidth);

            } else if (bmi < 25) {
                position = segmentWidth +
                        (float) ((bmi - 18.5) / (25 - 18.5) * segmentWidth);

            } else if (bmi < 30) {
                position = segmentWidth * 2 +
                        (float) ((bmi - 25) / (30 - 25) * segmentWidth);

            } else {
                double cappedBmi = Math.min(bmi, 40);
                position = segmentWidth * 3 +
                        (float) ((cappedBmi - 30) / (40 - 30) * segmentWidth);
            }

            position -= bmiPointer.getWidth() / 2f;

            bmiPointer.animate()
                    .translationX(position)
                    .setDuration(600)
                    .setInterpolator(new OvershootInterpolator(1.2f))
                    .start();
        });
    }
}