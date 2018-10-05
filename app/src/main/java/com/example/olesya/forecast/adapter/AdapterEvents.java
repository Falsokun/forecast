package com.example.olesya.forecast.adapter;

import com.example.olesya.forecast.pojo.WeatherInfo;

import java.util.List;

public interface AdapterEvents {

    void clear();

    void addItems(List<WeatherInfo> vals);
}
