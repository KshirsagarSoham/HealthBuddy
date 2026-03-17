package com.project.realhealthbuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.realhealthbuddy.Adapter.FaqAdapter;
import com.project.realhealthbuddy.Model.FaqItem;

import java.util.ArrayList;
import java.util.List;

public class HelpSupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.faqRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample FAQ data
        List<FaqItem> faqList = new ArrayList<>();
        faqList.add(new FaqItem(
                "How to track my health data?",
                "Go to Home screen and check the Health Summary section."
        ));

        faqList.add(new FaqItem(
                "How to set medicine reminders?",
                "Go to Medicine Reminder section and add your medicines."
        ));

        faqList.add(new FaqItem(
                "How to log water intake?",
                "Go to Hydration section and tap the water glass icon."
        ));

        faqList.add(new FaqItem(
                "How do I start a meditation session?",
                "Go to the Meditation section, select a session, and tap Start. Follow the instructions on screen."
        ));
        faqList.add(new FaqItem(
                "Can I update my profile information?",
                "Yes, open the Profile section from the sidebar and tap Edit to update your name, number, or email."
        ));
        faqList.add(new FaqItem(
                "How do I enable notifications for reminders?",
                "Go to Settings → Notifications and enable reminders for medicine, water, and other wellness activities."
        ));
        faqList.add(new FaqItem(
                "How do I contact support if I face an issue?",
                "Tap the Contact Support button in this Help & Support screen to send an email directly to our support team."
        ));
        faqList.add(new FaqItem(
                "Is my health data safe?",
                "Yes, all your health data is stored securely on your device and only shared with your permission."
        ));
        faqList.add(new FaqItem(
                "Can I reset the app data?",
                "Yes, go to Settings → Reset App Data. Please note that this will delete all your health logs."
        ));
        // Add more FAQs here

        FaqAdapter adapter = new FaqAdapter(faqList);
        recyclerView.setAdapter(adapter);

        // Contact Support button
        Button contactSupportButton = findViewById(R.id.contactSupportButton);
        contactSupportButton.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:support@realhealthbuddy.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help & Support Request");
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}