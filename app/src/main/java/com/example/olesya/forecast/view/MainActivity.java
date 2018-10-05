package com.example.olesya.forecast.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.olesya.forecast.App;
import com.example.olesya.forecast.DarkSkyService;
import com.example.olesya.forecast.R;
import com.example.olesya.forecast.Utils;
import com.example.olesya.forecast.adapter.ViewPagerAdapter;
import com.example.olesya.forecast.databinding.ActivityMainBinding;
import com.example.olesya.forecast.pojo.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initViewPager();
        getData();
    }

    private void initViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle todayArgs = new Bundle();
        todayArgs.putBoolean(Utils.ST_WEATHER_DAY, true);
        Fragment todayFragment = new FragmentWeatherList();
        todayFragment.setArguments(todayArgs);
        adapter.addItem(todayFragment);

        Fragment weekFragment = new FragmentWeatherList();
        adapter.addItem(weekFragment);
        mBinding.container.setAdapter(adapter);
    }

    public void getData() {
        DarkSkyService darkSkyService = App.getRetrofitInstance().create(DarkSkyService.class);
        darkSkyService.weatherOnLocationInfo("42.3601,-71.0589").enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.body() == null)
                    return;

                Intent hourly = new Intent();
                hourly.setAction(Utils.ST_WEATHER_DAY);
                hourly.putExtra(Utils.ST_WEATHER_OBJ, response.body().getHourly());
                sendBroadcast(hourly);

                Intent daily = new Intent();
                daily.setAction(Utils.ST_WEATHER_WEEK);
                daily.putExtra(Utils.ST_WEATHER_OBJ, response.body().getDaily());
                sendBroadcast(daily);
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Log.d("MSG", "MSG");
            }
        });
    }
}
