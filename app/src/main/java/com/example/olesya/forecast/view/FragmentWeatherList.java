package com.example.olesya.forecast.view;

import android.content.IntentFilter;
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
import com.example.olesya.forecast.adapter.ItemsAdapter;
import com.example.olesya.forecast.databinding.FragmentWeatherListBinding;

public class FragmentWeatherList extends Fragment {

    private FragmentListViewModel mModel;
    private FragmentWeatherListBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isWeatherToday = false;
        if (getArguments() != null) {
            isWeatherToday = getArguments().getBoolean(Utils.ST_WEATHER_DAY, false);
        }

        mModel = new FragmentListViewModel(isWeatherToday);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather_list, container, false);
        if (mModel.isWeatherToday()) {
            mBinding.title.setText("today");
        } else {
            mBinding.title.setText("7 days");
        }

        initAdapter(mBinding.weatherlistRv);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Utils.ST_CLEAR);
        if (mModel.isWeatherToday()) {
            filter.addAction(Utils.ST_WEATHER_DAY);
        } else {
            filter.addAction(Utils.ST_WEATHER_WEEK);
        }

        getActivity().registerReceiver(mModel.getReceiver(), filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mModel.getReceiver());
    }

    private void initAdapter(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mModel.getAdapter());
    }
}
