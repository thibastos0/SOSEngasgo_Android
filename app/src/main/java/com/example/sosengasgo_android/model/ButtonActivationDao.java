package com.example.sosengasgo_android.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ButtonActivationDao {

    @Insert
    void insert(ButtonActivation activation);

    @Query("SELECT * FROM button_activation_history ORDER BY id DESC")
    LiveData<List<ButtonActivation>> getAllActivations();

    @Query("DELETE FROM button_activation_history")
    void deleteAll();
}