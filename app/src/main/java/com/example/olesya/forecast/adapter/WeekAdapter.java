package com.example.olesya.forecast.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.olesya.forecast.databinding.ItemWeekInfoBinding;
import com.example.olesya.forecast.pojo.WeatherInfo;

import java.util.List;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.MyHolder> implements AdapterEvents {

    private static final int HOURS_LIMIT = 24;
    private List<WeatherInfo> mData;

    public WeekAdapter(List<WeatherInfo> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public WeekAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeekAdapter.MyHolder(ItemWeekInfoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeekAdapter.MyHolder holder, int position) {
        WeatherInfo info = mData.get(position);
        holder.mBinding.setInfo(info);
        holder.mBinding.setContext(holder.mBinding.getRoot().getContext());
        Glide.with(holder.itemView.getContext()).load(info.getDrawable()).into(holder.mBinding.icon);
    }

    @Override
    public int getItemCount() {
        return mData.size() > HOURS_LIMIT ? HOURS_LIMIT : mData.size();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public void addItems(List<WeatherInfo> vals) {
        mData.addAll(vals);
        notifyDataSetChanged();
    }

    @Override
    public void setItems(List<WeatherInfo> weatherInfo) {
        mData.clear();
        mData.addAll(weatherInfo);
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ItemWeekInfoBinding mBinding;

        public MyHolder(ItemWeekInfoBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
