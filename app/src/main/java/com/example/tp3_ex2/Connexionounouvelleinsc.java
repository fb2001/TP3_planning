package com.example.tp3_ex2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Connexionounouvelleinsc extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connexionounouvelleinsc);

        CardView cardInscription = findViewById(R.id.card_inscription);
        CardView cardConnexion = findViewById(R.id.card_connexion);

        cardInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Connexionounouvelleinsc.this, MainActivity.class);
                startActivity(intent);
            }
        });

        cardConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Connexionounouvelleinsc.this, Connexion.class);
                startActivity(intent);
            }
        });
    }
}