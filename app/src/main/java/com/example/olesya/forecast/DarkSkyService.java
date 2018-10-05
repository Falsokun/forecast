package com.example.olesya.forecast;

import com.example.olesya.forecast.pojo.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DarkSkyService {
    @GET("{location}")
    Call<WeatherResponse> weatherOnLocationInfo(@Path("location") String location,
                                                @Query("units") String units);
}
