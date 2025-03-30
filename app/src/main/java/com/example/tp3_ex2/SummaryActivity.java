package com.example.tp3_ex2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {
    private AppDatabase db;
    private String date;
    private TextView tvDateSummary;
    private TextView[] tvActivities = new TextView[4];
    private MaterialButton btnBack;
    private final String[] horaires = {"08h-10h", "10h-12h", "14h-16h", "16h-18h"};

    private MaterialButton btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        db = AppDatabase.getInstance(this);


        date = getIntent().getStringExtra("date");

        tvDateSummary = findViewById(R.id.tv_date_summary);
        tvDateSummary.setText(date);

        tvActivities[0] = findViewById(R.id.tv_activity_1);
        tvActivities[1] = findViewById(R.id.tv_activity_2);
        tvActivities[2] = findViewById(R.id.tv_activity_3);
        tvActivities[3] = findViewById(R.id.tv_activity_4);

        btnBack = findViewById(R.id.btn_back);


        btnEdit = findViewById(R.id.btn_edit);

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this, PlanningActivity.class);
            intent.putExtra("date", date);
            startActivity(intent);
            finish();
        });
        loadPlanningData();

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this, CalendrieActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }


    private void loadPlanningData() {
        new Thread(() -> {
            List<Planning> planningList = db.planningDao().getPlanningByDate(date);

            Map<String, String> planningMap = new HashMap<>();
            for (Planning p : planningList) {
                planningMap.put(p.horaire, p.activite);
            }

            runOnUiThread(() -> {
                for (int i = 0; i < horaires.length; i++) {
                    String activite = planningMap.getOrDefault(horaires[i], "Aucune activité");
                    tvActivities[i].setText(activite);
                }
            });
        }).start();
    }

}