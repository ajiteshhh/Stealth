package com.example.stealth;

import android.os.Bundle;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TermsCondition extends AppCompatActivity {

    ScrollView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terms_condition);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        view = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures());
            int paddingValue = 20; // Adjust this value as needed
            int leftPadding = Math.max(0, insets.left - paddingValue);
            int topPadding = Math.max(0, insets.top - paddingValue);
            int rightPadding = Math.max(0, insets.right - paddingValue);
            int bottomPadding = Math.max(0, insets.bottom - paddingValue);

            view.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
            return WindowInsetsCompat.CONSUMED;
        });

    }
}