package com.example.pokemon.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {

    @PrimaryKey
    @NonNull
    private String username;
    private String password;

    //Contructor vaci
    public UserEntity() {
    }

//Constructor completo
    public UserEntity(@NonNull String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter y Setters

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}