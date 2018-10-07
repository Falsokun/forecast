package com.example.olesya.forecast.pojo;

public class WeatherResponse {

    private WeatherInfo currently;

    private ListInfo hourly;

    private ListInfo daily;

    public WeatherResponse(WeatherInfo currently, ListInfo hourly, ListInfo daily) {
        this.currently = currently;
        this.hourly = hourly;
        this.daily = daily;
    }

    //region getters and setters
    public WeatherInfo getCurrently() {
        return currently;
    }

    public ListInfo getHourly() {
        return hourly;
    }

    public ListInfo getDaily() {
        return daily;
    }
    //endregion
}
