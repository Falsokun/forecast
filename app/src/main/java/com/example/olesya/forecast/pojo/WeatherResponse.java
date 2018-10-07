package com.example.olesya.forecast.pojo;

/**
 * Response object from api
 */
public class WeatherResponse {

    /**
     * Current weather info
     */
    private WeatherInfo currently;

    /**
     * Information about weather in future time
     * Data is presented by hours
     */
    private ListInfo hourly;

    /**
     * Information about weather in future time
     * Data is presented by days
     */
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
