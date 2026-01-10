package com.project.realhealthbuddy.comman;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "users")
public class User {

        @PrimaryKey(autoGenerate = true)
        public int id;

        public String name;
        public String mobile;
        public String email;

        @ColumnInfo(name = "username")
        public String username;  // UNIQUE handled via DB constraint if needed

        public String password;  // Hash this in production

        // Default constructor for Room
        public User() {}

        public User(String name, String mobile, String email, String username, String password) {
            this.name = name;
            this.mobile = mobile;
            this.email = email;
            this.username = username;
            this.password = password;
        }
    }