package com.example.olesya.forecast.pojo;

public class WeatherResponse {

    private double langitude;

    private double longitude;

    private WeatherInfo currently;

    private ListInfo hourly;

    private ListInfo daily;

    //region getters and setters
    public double getLangitude() {
        return langitude;
    }

    public void setLangitude(double langitude) {
        this.langitude = langitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public WeatherInfo getCurrently() {
        return currently;
    }

    public void setCurrently(WeatherInfo currently) {
        this.currently = currently;
    }

    public ListInfo getHourly() {
        return hourly;
    }

    public void setHourly(ListInfo hourly) {
        this.hourly = hourly;
    }

    public ListInfo getDaily() {
        return daily;
    }

    public void setDaily(ListInfo daily) {
        this.daily = daily;
    }
    //endregion
}
