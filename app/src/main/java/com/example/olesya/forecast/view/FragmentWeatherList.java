package com.example.olesya.forecast.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.olesya.forecast.R;
import com.example.olesya.forecast.Utils;
import com.example.olesya.forecast.adapter.ItemsAdapter;
import com.example.olesya.forecast.pojo.ListInfo;
import com.example.olesya.forecast.pojo.WeatherInfo;

import java.util.ArrayList;

public class FragmentWeatherList extends Fragment {

    private boolean isWeatherToday = false;
    private ItemsAdapter mAdapter;
    private BroadcastReceiver mReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isWeatherToday = getArguments().getBoolean(Utils.ST_WEATHER_DAY, false);
        }

        mReceiver = initReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_weather_list, container, false);
        if (isWeatherToday) {
            ((TextView)root.findViewById(R.id.title)).setText("today");
        } else {
            ((TextView)root.findViewById(R.id.title)).setText("7 days");
        }

        initAdapter((RecyclerView)root.findViewById(R.id.weatherlist_rv));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        if (isWeatherToday) {
            filter.addAction(Utils.ST_WEATHER_DAY);
        } else {
            filter.addAction(Utils.ST_WEATHER_WEEK);
        }

        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    private void initAdapter(RecyclerView recyclerView) {
        mAdapter = new ItemsAdapter(new ArrayList<WeatherInfo>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    private BroadcastReceiver initReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
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
            }
        };
    }
}
