package com.example.olesya.forecast.adapter;

import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.olesya.forecast.databinding.ItemWeatherInfoBinding;
import com.example.olesya.forecast.pojo.WeatherInfo;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyHolder> {

    private static final int HOURS_LIMIT = 24;
    private ArrayList<WeatherInfo> mData;

    public ItemsAdapter(ArrayList<WeatherInfo> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(ItemWeatherInfoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        WeatherInfo info = mData.get(position);
        holder.mBinding.setInfo(info);
        holder.mBinding.setContext(holder.mBinding.getRoot().getContext());
        Glide.with(holder.itemView.getContext()).load(info.getDrawable()).into(holder.mBinding.icon);
    }

    @Override
    public int getItemCount() {
        return mData.size() > HOURS_LIMIT ? HOURS_LIMIT : mData.size();
    }

    public void addItems(ArrayList<WeatherInfo> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ItemWeatherInfoBinding mBinding;

        public MyHolder(ItemWeatherInfoBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
