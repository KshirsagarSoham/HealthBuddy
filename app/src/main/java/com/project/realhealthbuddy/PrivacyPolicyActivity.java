package com.project.realhealthbuddy;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // WebView
        webView = findViewById(R.id.privacy_webview);
        webView.getSettings().setJavaScriptEnabled(false);

        // Load HTML with theme colors
        loadPrivacyPolicy();
    }

    private void loadPrivacyPolicy() {
        int bgColor = ContextCompat.getColor(this, R.color.webview_bg);
        int textColor = ContextCompat.getColor(this, R.color.webview_text);
        int headingColor = ContextCompat.getColor(this, R.color.webview_heading);
        int linkColor = ContextCompat.getColor(this, R.color.webview_link);

        String htmlContent = readHTMLFromAssets("privacy_policy.html");

        String finalHtml = "<html><head>"
                + "<meta charset=\"utf-8\">"
                + "<style>"
                + "body { background-color:" + toHex(bgColor) + "; color:" + toHex(textColor) + "; font-family:sans-serif; padding:16px; line-height:1.6; }"
                + "h1,h2,h3 { color:" + toHex(headingColor) + "; }"
                + "a { color:" + toHex(linkColor) + "; text-decoration:none; }"
                + "ul { margin-left:20px; }"
                + "</style>"
                + "</head><body>"
                + htmlContent
                + "</body></html>";

        webView.loadDataWithBaseURL(null, finalHtml, "text/html", "utf-8", null);
    }

    private String readHTMLFromAssets(String filename) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = getAssets().open(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String toHex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}