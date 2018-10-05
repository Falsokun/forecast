package com.example.olesya.forecast;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App {

    private final static String DARKSKY_API_KEY = "a945835dbe2c733e2335febb12f41294";
    private final static String QUERY_EXCLUDE = "flags,minutely";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.darksky.net/forecast/" + DARKSKY_API_KEY + "/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
