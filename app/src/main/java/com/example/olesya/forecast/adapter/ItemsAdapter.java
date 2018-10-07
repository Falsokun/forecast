package com.example.olesya.forecast.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.olesya.forecast.databinding.ItemWeatherInfoBinding;
import com.example.olesya.forecast.pojo.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyHolder> implements AdapterEvents {

    /**
     * Limit number to show info
     */
    private static final int HOURS_LIMIT = 24;

    /**
     * Storing adapter data
     */
    private List<WeatherInfo> mData = new ArrayList<>();

    public ItemsAdapter(List<WeatherInfo> mData) {
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

    @Override
    public void setItems(List<WeatherInfo> weatherInfo) {
        int pos = mData.size();
        mData.clear();
        mData = new ArrayList<>(weatherInfo);
        if (mData.size() == 0) {
            notifyItemRangeRemoved(0, pos);
        } else {
            notifyItemRangeChanged(0, weatherInfo.size());
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        /**
         * Holder binding element
         */
        ItemWeatherInfoBinding mBinding;

        public MyHolder(ItemWeatherInfoBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
