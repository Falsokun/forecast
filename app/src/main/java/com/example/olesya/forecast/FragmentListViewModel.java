package com.example.olesya.forecast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.olesya.forecast.adapter.ItemsAdapter;
import com.example.olesya.forecast.pojo.ListInfo;
import com.example.olesya.forecast.pojo.WeatherInfo;

import java.util.ArrayList;

public class FragmentListViewModel extends BaseObservable {

    private ItemsAdapter mAdapter;
    private BroadcastReceiver mReceiver;
    private boolean isWeatherToday = false;

    public FragmentListViewModel(boolean isWeatherToday) {
        this.isWeatherToday = isWeatherToday;
        mReceiver = initReceiver(isWeatherToday);
        mAdapter = new ItemsAdapter(new ArrayList<WeatherInfo>());
    }

    public ItemsAdapter getAdapter() {
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

    private BroadcastReceiver initReceiver(final boolean isWeatherToday) {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null && intent.getAction().equals(Utils.ST_CLEAR)) {
                    mAdapter.clear();
                    return;
                }

                if (intent.getExtras() == null)
                    return;


                ListInfo info = (ListInfo)intent.getExtras().getSerializable(Utils.ST_WEATHER_OBJ);
                if (info == null)
                    return;

                if (isWeatherToday) {
                    mAdapter.addItems(info.getData());
                } else {
                    mAdapter.addItems(info.getData());
                }

                notifyPropertyChanged(BR.empty);
            }
        };
    }
}
