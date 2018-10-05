package com.example.olesya.forecast;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App {

    private final static String DARKSKY_API_KEY = "a945835dbe2c733e2335febb12f41294";
    private final static String QUERY_EXCLUDE = "flags,minutely";
    private static Retrofit retrofit;
    private static AppDatabase db;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.darksky.net/forecast/" + DARKSKY_API_KEY + "/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static AppDatabase getDBInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context,
                    AppDatabase.class, "weather-info").build();
        }

        return db;
    }
}
