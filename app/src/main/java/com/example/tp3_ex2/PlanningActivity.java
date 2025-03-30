package com.example.tp3_ex2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class PlanningActivity extends AppCompatActivity {
    private TextInputEditText[] editTexts = new TextInputEditText[4];
    private String[] horaires = {"08h-10h", "10h-12h", "14h-16h", "16h-18h"};
    private AppDatabase db;
    private String date;
    private TextView tvDateHeader;
    private MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);

        db = AppDatabase.getInstance(this);

        date = getIntent().getStringExtra("date");

        tvDateHeader = findViewById(R.id.tv_date_header);
        tvDateHeader.setText("Planning du " + date);

        editTexts[0] = findViewById(R.id.editText1);
        editTexts[1] = findViewById(R.id.editText2);
        editTexts[2] = findViewById(R.id.editText3);
        editTexts[3] = findViewById(R.id.editText4);

        btnSave = findViewById(R.id.btn_save);

        loadExistingData();

        btnSave.setOnClickListener(v -> savePlanningData());
    }

    private void loadExistingData() {
        new Thread(() -> {
            List<Planning> existingPlanning = db.planningDao().getPlanningByDate(date);
            runOnUiThread(() -> {
                if (existingPlanning.isEmpty()) {
                    Log.d("PlanningActivity", "Aucune donnée trouvée pour la date: " + date);
                    return;
                }

                for (Planning p : existingPlanning) {
                    Log.d("PlanningActivity", "Horaire: " + p.horaire + ", Activité: " + p.activite);
                    for (int i = 0; i < horaires.length; i++) {
                        if (horaires[i].equals(p.horaire)) {
                            editTexts[i].setText(p.activite);
                            break;
                        }
                    }
                }
            });
        }).start();
    }

    private void savePlanningData() {
        new Thread(() -> {
            db.planningDao().deletePlanningByDate(date);

            for (int i = 0; i < horaires.length; i++) {
                String activite = editTexts[i].getText() != null ?
                        editTexts[i].getText().toString().trim() : "";

                if (activite.isEmpty()) {
                    activite = "Aucune activité";
                }

                Planning planning = new Planning();
                planning.date = date;
                planning.horaire = horaires[i];
                planning.activite = activite;
                db.planningDao().insert(planning);
            }

            runOnUiThread(() -> {
                Intent intent = new Intent(PlanningActivity.this, SummaryActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
                finish();
            });
        }).start();
    }
}