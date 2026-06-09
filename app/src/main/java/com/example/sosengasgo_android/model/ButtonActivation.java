package com.example.sosengasgo_android.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "button_activation_history",
        indices = {@Index(value = {"userId"}, unique = false)}
)
public class ButtonActivation {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "userId")
    private String userId;       // ID do usuário associado
    @ColumnInfo(name = "date")
    private String date;      // Formato: "dd/MM/yyyy"
    @ColumnInfo(name = "time")
    private String time;      // Formato: "HH:mm:ss"
    @ColumnInfo(name = "location")
    private String location;  // Latitude/Longitude ou endereço
    @ColumnInfo(name = "status")
    private String status;    // "finalizado" ou "cancelado"

    public ButtonActivation() {}

    public ButtonActivation(String userId, String date, String time, String location, String status) {
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.location = location;
        this.status = status;
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}