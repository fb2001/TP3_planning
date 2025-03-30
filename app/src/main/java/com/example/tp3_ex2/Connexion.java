package com.example.tp3_ex2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Connexion extends AppCompatActivity {

    private TextInputEditText editLogin, editPassword;
    private MaterialButton btnConnexion;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        editLogin = findViewById(R.id.et_login);
        editPassword = findViewById(R.id.et_password);
        btnConnexion = findViewById(R.id.btn_connexion);
        tvError = findViewById(R.id.tv_error);

        btnConnexion.setOnClickListener(v -> {
            String login = editLogin.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                tvError.setText("Remplissez tous les champs");
                tvError.setVisibility(View.VISIBLE);
                return;
            }

            new Thread(() -> {
                User user = AppDatabase.getInstance(Connexion.this)
                        .userDao()
                        .getUserByLoginAndPassword(login, password);

                runOnUiThread(() -> {
                    if (user != null) {
                        Intent intent = new Intent(Connexion.this, CalendrieActivity.class);
                        intent.putExtra("USER_LOGIN", login);
                        startActivity(intent);
                        finish();
                    } else {
                        tvError.setText("Identifiants incorrects");
                        tvError.setVisibility(View.VISIBLE);
                    }
                });
            }).start();
        });
    }
}