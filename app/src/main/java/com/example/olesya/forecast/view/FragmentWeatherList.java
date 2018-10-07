package com.example.olesya.forecast.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olesya.forecast.FragmentListViewModel;
import com.example.olesya.forecast.R;
import com.example.olesya.forecast.Utils;
import com.example.olesya.forecast.adapter.AdapterEvents;
import com.example.olesya.forecast.databinding.FragmentWeatherListBinding;
import com.example.olesya.forecast.pojo.WeatherInfo;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class FragmentWeatherList extends Fragment {

    /**
     * Fragment model
     */
    private FragmentListViewModel mModel;

    /**
     * Data binding object
     */
    private FragmentWeatherListBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isWeatherToday = false;
        if (getArguments() != null) {
            isWeatherToday = getArguments().getBoolean(Utils.ST_WEATHER_DAY, false);
        }

        mModel = ViewModelProviders.of(this).get(FragmentListViewModel.class);
        mModel.init(getContext(), isWeatherToday);
        int type = isWeatherToday ? Utils.WEATHER_TYPES.DAY : Utils.WEATHER_TYPES.WEEK;
        mModel.getCurrentData(getContext(), type).observe(this, new Observer<List<WeatherInfo>>() {
            @Override
            public void onChanged(@Nullable List<WeatherInfo> weatherInfos) {
                ((AdapterEvents) mModel.getAdapter()).setItems(weatherInfos);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather_list, container, false);
        mBinding.title.setText(mModel.getTitle(getContext()));
        initRecyclerView(mBinding.weatherlistRv);
        return mBinding.getRoot();
    }

    /**
     * Initialization of recyclerview
     *
     * @param recyclerView - object to init
     */
    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        recyclerView.setAdapter(mModel.getAdapter());
    }
}
