package com.project.realhealthbuddy.comman;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

@Dao
public interface UserDao {
    @Insert
    long insertUser(User user);  // Returns row ID like your registeruser

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User validateLogin(String username, String password);  // Returns User if valid

    @Update
    int updateUser(User user);  // Update by primary key


    @Delete
    int deleteUser(User user);  // Delete by object
}