package com.example.olesya.forecast.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.olesya.forecast.pojo.WeatherInfo;

@Database(entities = {WeatherInfo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Get database dao
     *
     * @return database dao
     */
    public abstract WeatherInfoDao weatherDao();
}
