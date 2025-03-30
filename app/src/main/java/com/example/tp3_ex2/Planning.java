package com.example.tp3_ex2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "planning")
public class Planning {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;
    public String horaire;
    public String activite;

    public Planning(String date, String horaire, String activite) {
        this.date = date;
        this.horaire = horaire;
        this.activite = activite;
    }
    public Planning() {}
}