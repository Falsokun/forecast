package com.example.olesya.forecast.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.olesya.forecast.pojo.WeatherInfo;

import java.util.List;

@Dao
public interface WeatherInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInfo(List<WeatherInfo> info);

    @Query("SELECT * FROM weatherinfo WHERE type = :type")
    LiveData<List<WeatherInfo>> getInfo(int type);

    @Query("DELETE FROM weatherinfo")
    void clear();
}
