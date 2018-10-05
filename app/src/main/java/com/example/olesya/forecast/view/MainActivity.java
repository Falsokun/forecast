package com.example.olesya.forecast.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.olesya.forecast.App;
import com.example.olesya.forecast.DarkSkyService;
import com.example.olesya.forecast.R;
import com.example.olesya.forecast.Utils;
import com.example.olesya.forecast.adapter.ViewPagerAdapter;
import com.example.olesya.forecast.databinding.ActivityMainBinding;
import com.example.olesya.forecast.pojo.WeatherResponse;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
        initNavigation();
        initRefreshListener();
//        initSpinnerAdapter();
        setSupportActionBar(mBinding.toolbar);
        getData();
    }

    private void initSpinnerAdapter() {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
//                getResources().getStringArray(R.array.arr_cities));
//        adapter.setDropDownViewResource(R.layout.item_spinner);
//        mBinding.currentWeather.citySp.setAdapter(adapter);
//        String curLocation = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).getString(Utils.PREF_LOCATION, "");
//        mBinding.currentWeather.citySp.setSelection(getPositionByLocation(curLocation));
//        mBinding.currentWeather.citySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //change location
//                getSharedPreferences(getPackageName(), Context.MODE_PRIVATE)
//                        .edit()
//                        .putString(Utils.PREF_LOCATION, getResources().getStringArray(R.array.arr_cities)[position])
//                        .apply();
//                refreshData();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        menu.getItem(2)
                .setChecked(getSharedPreferences(getPackageName(), Context.MODE_PRIVATE)
                        .getBoolean(Utils.PREF_SI, false));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refreshData();
                break;
            case R.id.menu_format:
                item.setChecked(!item.isChecked());
                preferences.edit().putBoolean(Utils.PREF_SI, item.isChecked()).apply();
                refreshData(); //вообще не очень нравится. да и вообще не нравится идея запроса
                // с выдачей данных в си, но наверное это не часто будет выполняться
                break;
            case R.id.menu_save:
                preferences.edit().putBoolean(Utils.PREF_SAVE, item.isChecked()).apply();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRefreshListener() {
        mBinding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        mBinding.container.setVisibility(View.GONE);
        Intent hourly = new Intent();
        hourly.setAction(Utils.ST_CLEAR);
        sendBroadcast(hourly);
        getData();
    }

    private void initNavigation() {
        mBinding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mBinding.container.setCurrentItem(getPositionById(item.getItemId()));
                return true;
            }
        });

        mBinding.container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.bottomNavigation.setSelectedItemId(getIdByPosition(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int getIdByPosition(int position) {
        if (position == 0) {
            return R.id.menu_day;
        }

        return R.id.menu_week;
    }

    private int getPositionById(int id) {
        if (id == R.id.menu_day) {
            return 0;
        }

        return 1;
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
        darkSkyService.weatherOnLocationInfo(getLocation(), getUnits()).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.body() == null)
                    return;

                mBinding.refresh.setRefreshing(false);
                mBinding.container.setVisibility(View.VISIBLE);
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

    public String getUnits() {
        boolean isSi = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE)
                .getBoolean(Utils.PREF_SI, false);
        return isSi ? Utils.DARKSKY_ICON_CONST.UNIT_SI : Utils.DARKSKY_ICON_CONST.UNIT_US;
    }

    public int getPositionByLocation(String location) {
        String[] arr = getResources().getStringArray(R.array.arr_cities);
        for (int i = 0; i < arr.length; i++) {
            if (location.equals(arr[i]))
                return i;
        }

        return 0;
    }

    public String getLocation() {
//        String locationName = (String) mBinding.currentWeather.citySp.getSelectedItem();
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            List<Address> res = geocoder.getFromLocationName(locationName, 1);
//            return res.get(0).getLatitude() + "," + res.get(0).getLatitude();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return "39.701505,47.2357137";
    }
}
