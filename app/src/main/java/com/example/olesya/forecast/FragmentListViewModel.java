package com.example.olesya.forecast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;

import com.example.olesya.forecast.adapter.AdapterEvents;
import com.example.olesya.forecast.adapter.ItemsAdapter;
import com.example.olesya.forecast.adapter.WeekAdapter;
import com.example.olesya.forecast.pojo.ListInfo;
import com.example.olesya.forecast.pojo.WeatherInfo;

import java.util.ArrayList;

public class FragmentListViewModel extends BaseObservable {

    private RecyclerView.Adapter mAdapter;
    private BroadcastReceiver mReceiver;
    private boolean isWeatherToday = false;

    public FragmentListViewModel(boolean isWeatherToday) {
        this.isWeatherToday = isWeatherToday;
        mReceiver = initReceiver();
        if (isWeatherToday) {
            mAdapter = new ItemsAdapter(new ArrayList<WeatherInfo>());
        } else {
            mAdapter = new WeekAdapter(new ArrayList<WeatherInfo>());
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Bindable
    public boolean isEmpty() {
        return mAdapter.getItemCount() == 0;
    }

    public BroadcastReceiver getReceiver() {
        return mReceiver;
    }

    public boolean isWeatherToday() {
        return isWeatherToday;
    }

    private BroadcastReceiver initReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null && intent.getAction().equals(Utils.ST_CLEAR)) {
                    ((AdapterEvents)mAdapter).clear();
                    return;
                }

                if (intent.getExtras() == null)
                    return;


                ListInfo info = (ListInfo)intent.getExtras().getSerializable(Utils.ST_WEATHER_OBJ);
                if (info == null)
                    return;

                ((AdapterEvents)mAdapter).addItems(info.getData());
                notifyPropertyChanged(BR.empty);
            }
        };
    }
}
