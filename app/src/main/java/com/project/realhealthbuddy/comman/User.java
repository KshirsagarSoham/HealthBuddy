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

    // ---- Getters ----
    public int getId() { return id; }
    public String getName() { return name; }
    public String getMobile() { return mobile; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    // ---- Setters (optional) ----
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    }