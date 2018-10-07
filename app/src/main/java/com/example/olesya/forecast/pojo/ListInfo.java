package com.example.olesya.forecast.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to parse data from api via gson
 */
public class ListInfo implements Serializable {

    /**
     *
     */
    private String summary;

    private String icon;

    private ArrayList<WeatherInfo> data;

    //region setters and getters
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<WeatherInfo> getData() {
        return data;
    }

    public void setData(ArrayList<WeatherInfo> data) {
        this.data = data;
    }
    //endregion
}
