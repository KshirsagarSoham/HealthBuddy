package com.project.realhealthbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.realhealthbuddy.Fragments.HomeFragment;

public class BMI_Activity extends AppCompatActivity {

    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        back = findViewById(R.id.backbutton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText etHeight = findViewById(R.id.etHeight);
        EditText etWeight = findViewById(R.id.etWeight);
        Button btnCalculate = findViewById(R.id.btnCalculate);

        TextView tvBmiValue = findViewById(R.id.tvBmiValue);
        TextView tvResult = findViewById(R.id.tvResult);

        LinearLayout resultLayout = findViewById(R.id.resultLayout);

        btnCalculate.setOnClickListener(v -> {

            String heightStr = etHeight.getText().toString().trim();
            String weightStr = etWeight.getText().toString().trim();

            // ✅ Input validation
            if (heightStr.isEmpty() || weightStr.isEmpty()) {
                Toast.makeText(this, "Please enter height and weight", Toast.LENGTH_SHORT).show();
                return;
            }

            double heightCm = Double.parseDouble(heightStr);
            double weightKg = Double.parseDouble(weightStr);

            // Prevent zero crash
            if (heightCm <= 0 || weightKg <= 0) {
                Toast.makeText(this, "Invalid values entered", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ BMI Formula
            double heightM = heightCm / 100.0;
            double bmi = weightKg / (heightM * heightM);

            // Round to 1 decimal
            String bmiFormatted = String.format("%.1f", bmi);

            SharedPreferences prefs = getSharedPreferences("health_data", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("bmi_value", bmiFormatted);
            editor.apply();

            tvBmiValue.setText(bmiFormatted);

            // ✅ Category Logic
            String category;

            if (bmi < 18.5) {
                category = "Underweight";
            } else if (bmi < 25) {
                category = "Normal";
            } else if (bmi < 30) {
                category = "Overweight";
            } else {
                category = "Obese";
            }

            tvResult.setText(category);

            if (bmi < 18.5) {
                tvResult.setTextColor(Color.BLUE);
            } else if (bmi < 25) {
                tvResult.setTextColor(Color.GREEN);
            } else if (bmi < 30) {
                tvResult.setTextColor(Color.rgb(255,165,0)); // Orange
            } else {
                tvResult.setTextColor(Color.RED);
            }

            // Show layout
            resultLayout.setVisibility(View.VISIBLE);

            // Animate
            resultLayout.setAlpha(0f);
            resultLayout.setTranslationY(100f);

            resultLayout.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(400)
                    .start();



//            Intent resultIntent = new Intent();
//            resultIntent.putExtra("bmi_value", bmiFormatted);
//            setResult(RESULT_OK, resultIntent);


        });






    }
}