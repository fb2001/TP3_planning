package com.example.tp3_ex2;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface PlanningDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Planning planning);

    @Query("SELECT * FROM planning WHERE date = :date ORDER BY horaire")
    List<Planning> getPlanningByDate(String date);

    @Query("DELETE FROM planning WHERE date = :date")
    void deletePlanningByDate(String date);
}