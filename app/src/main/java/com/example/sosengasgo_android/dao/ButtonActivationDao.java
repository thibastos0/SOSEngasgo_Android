package com.example.sosengasgo_android.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sosengasgo_android.model.ButtonActivation;

import java.util.List;

@Dao
public interface ButtonActivationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ButtonActivation activation);

    @Query("SELECT * FROM button_activation_history ORDER BY id DESC")
    LiveData<List<ButtonActivation>> getAllActivations();

    @Update
    void update(ButtonActivation activation);

    @Query("UPDATE button_activation_history SET status = :status WHERE id = :id AND userId = :userId")
    void updateStatus(long id, String userId, String status);

    @Query("DELETE FROM button_activation_history WHERE userId = :userId")
    void deleteAll(String userId);
}