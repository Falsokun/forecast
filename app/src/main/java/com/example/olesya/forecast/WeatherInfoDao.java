package com.example.olesya.forecast;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInfo(WeatherInfo info);

    @Query("SELECT * FROM weatherinfo WHERE type = :type")
    LiveData<List<WeatherInfo>> getInfo(int type);

    @Query("DELETE FROM weatherinfo where type = :type")
    void clear(int type);
}
