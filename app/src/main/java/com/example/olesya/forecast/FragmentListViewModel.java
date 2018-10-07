package com.example.olesya.forecast;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.olesya.forecast.adapter.ItemsAdapter;
import com.example.olesya.forecast.adapter.WeekAdapter;
import com.example.olesya.forecast.pojo.WeatherInfo;
import com.example.olesya.forecast.view.FragmentWeatherList;

import java.util.ArrayList;
import java.util.List;

public class FragmentListViewModel extends ViewModel {

    private RecyclerView.Adapter mAdapter;
    private boolean isWeatherToday = false;
    private LiveData<List<WeatherInfo>> mData;

    public FragmentListViewModel() {
    }

    public void init(Context context, boolean isWeatherToday) {
        this.isWeatherToday = isWeatherToday;
        if (isWeatherToday) {
            List<WeatherInfo> info = getCurrentData(context, 0).getValue();
            mAdapter = new ItemsAdapter(info == null ? new ArrayList<WeatherInfo>() : info);
        } else {
            List<WeatherInfo> info = getCurrentData(context, 1).getValue();
            mAdapter = new WeekAdapter(info == null ? new ArrayList<WeatherInfo>() : info);
        }
    }

    public LiveData<List<WeatherInfo>> getCurrentData(Context context, int type) {
        if (mData == null) {
            mData = App.getDBInstance(context).weatherDao().getInfo(type);
        }

        return mData;
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public String getTitle(Context context) {
        if (isWeatherToday) {
            return context.getString(R.string.today);
        } else {
            return context.getString(R.string.week);
        }
    }
}
