package com.example.tp3_ex2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText etNom, etPrenom, etDateNaissance, etTelephone, etEmail;
    private CheckBox cbSport, cbMusique, cbLecture, cbVoyage;
    private MaterialButton btnSave;
    private AppDatabase db;
    private String currentUserLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = AppDatabase.getInstance(this);
        currentUserLogin = getIntent().getStringExtra("USER_LOGIN");

        if (currentUserLogin == null) {
            Toast.makeText(this, "Utilisateur non identifié", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        loadUserData();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("USER_LOGIN", currentUserLogin);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void initViews() {
        etNom = findViewById(R.id.et_nom);
        etPrenom = findViewById(R.id.et_prenom);
        etDateNaissance = findViewById(R.id.et_date_naissance);
        etTelephone = findViewById(R.id.et_telephone);
        etEmail = findViewById(R.id.et_email);

        cbSport = findViewById(R.id.cb_sport);
        cbMusique = findViewById(R.id.cb_musique);
        cbLecture = findViewById(R.id.cb_lecture);
        cbVoyage = findViewById(R.id.cb_voyage);

        btnSave = findViewById(R.id.btn_save);

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });


        etDateNaissance.setOnClickListener(v -> showDatePickerDialog());
        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadUserData() {
        new AsyncTask<Void, Void, User>() {
            @Override
            protected User doInBackground(Void... voids) {
                return db.userDao().getUserByLogin(currentUserLogin);
            }

            @Override
            protected void onPostExecute(User user) {
                if (user != null) {
                    etNom.setText(user.nom);
                    etPrenom.setText(user.prenom);
                    etDateNaissance.setText(user.dateNaissance);
                    etTelephone.setText(user.telephone);
                    etEmail.setText(user.email);

                    if (user.centresInteret != null) {
                        String[] interets = user.centresInteret.split(",");
                        for (String interet : interets) {
                            switch (interet.trim()) {
                                case "Sport": cbSport.setChecked(true); break;
                                case "Musique": cbMusique.setChecked(true); break;
                                case "Lecture": cbLecture.setChecked(true); break;
                                case "Voyage": cbVoyage.setChecked(true); break;
                            }
                        }
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this,
                            "Utilisateur non trouvé", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }.execute();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etDateNaissance.setText(date);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void saveProfile() {
        String nom = etNom.getText().toString().trim();
        String prenom = etPrenom.getText().toString().trim();
        String dateNaissance = etDateNaissance.getText().toString().trim();
        String telephone = etTelephone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (nom.isEmpty() || prenom.isEmpty() || dateNaissance.isEmpty() ||
                telephone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder centresInteret = new StringBuilder();
        if (cbSport.isChecked()) centresInteret.append("Sport,");
        if (cbMusique.isChecked()) centresInteret.append("Musique,");
        if (cbLecture.isChecked()) centresInteret.append("Lecture,");
        if (cbVoyage.isChecked()) centresInteret.append("Voyage,");
        if (centresInteret.length() > 0) {
            centresInteret.setLength(centresInteret.length() - 1);
        }

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    User user = db.userDao().getUserByLogin(currentUserLogin);
                    if (user != null) {
                        user.nom = nom;
                        user.prenom = prenom;
                        user.dateNaissance = dateNaissance;
                        user.telephone = telephone;
                        user.email = email;
                        user.centresInteret = centresInteret.toString();
                        db.userDao().update(user);
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    Log.e("EditProfile", "Erreur mise à jour", e);
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    Toast.makeText(EditProfileActivity.this,
                            "Profil mis à jour", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this,
                            "Erreur de mise à jour", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}