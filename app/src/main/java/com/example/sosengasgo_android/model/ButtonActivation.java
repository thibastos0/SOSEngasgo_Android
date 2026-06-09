package com.example.sosengasgo_android.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "button_activation_history")
public class ButtonActivation {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;      // Formato: "dd/MM/yyyy"
    private String time;      // Formato: "HH:mm:ss"
    private String location;  // Latitude/Longitude ou endereço

    public ButtonActivation(String date, String time, String location) {
        this.date = date;
        this.time = time;
        this.location = location;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}