package com.example.sosengasgo_android.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sosengasgo_android.model.ButtonActivation;

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