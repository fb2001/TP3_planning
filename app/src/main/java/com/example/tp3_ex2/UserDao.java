package com.example.tp3_ex2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT COUNT(*) FROM users WHERE login = :login")
    int countByLogin(String login);

    @Query("SELECT * FROM users WHERE login = :login AND password = :password")
    User getUserByLoginAndPassword(String login, String password);

    @Update
    void update(User user);

    @Query("SELECT * FROM users WHERE login = :login LIMIT 1")
    User getUserByLogin(String login);


}