package com.example.olesya.forecast.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.olesya.forecast.databinding.ItemWeekInfoBinding;
import com.example.olesya.forecast.pojo.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.MyHolder> implements AdapterEvents {

    /**
     *  Storing adapter data
     */
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
        holder.mBinding.dow.setText(info.getDayOfWeek(holder.mBinding.getContext()));
        Glide.with(holder.itemView.getContext()).load(info.getDrawable()).into(holder.mBinding.icon);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void setItems(List<WeatherInfo> weatherInfo) {
        mData.clear();
        mData = new ArrayList<>(weatherInfo);
        notifyItemRangeChanged(0, weatherInfo.size());
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        /**
         * Holder binding
         */
        ItemWeekInfoBinding mBinding;

        public MyHolder(ItemWeekInfoBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
