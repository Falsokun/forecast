package com.example.olesya.forecast.view;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.olesya.forecast.App;
import com.example.olesya.forecast.MainActivityViewModel;
import com.example.olesya.forecast.R;
import com.example.olesya.forecast.Utils;
import com.example.olesya.forecast.adapter.ViewPagerAdapter;
import com.example.olesya.forecast.databinding.ActivityMainBinding;
import com.example.olesya.forecast.pojo.WeatherInfo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * Binding element
     */
    private ActivityMainBinding mBinding;

    /**
     * View model
     */
    private MainActivityViewModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initViewPager();
        initNavigation();
        mModel = new MainActivityViewModel();
        mModel.refreshData(this, mBinding.refresh);
        mBinding.refresh.setOnRefreshListener(mModel.getOnRefreshListener(this, mBinding.refresh));
        initSpinnerAdapter();
        initObserver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        menu.getItem(1)
                .setChecked(getSharedPreferences(getPackageName(), Context.MODE_PRIVATE)
                        .getBoolean(Utils.PREF_SI, false));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                mModel.refreshData(this, mBinding.refresh);
                break;
            case R.id.menu_format:
                item.setChecked(!item.isChecked());
                preferences.edit().putBoolean(Utils.PREF_SI, item.isChecked()).apply();
                mModel.refreshData(this, mBinding.refresh);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initializes observer to data which are loaded to database (appeared new data from api)
     */
    private void initObserver() {
        App.getDBInstance(this).weatherDao().getInfo(2).observe(this, new Observer<List<WeatherInfo>>() {
            @Override
            public void onChanged(@Nullable List<WeatherInfo> weatherInfos) {
                if (weatherInfos != null && weatherInfos.size() != 0) {
                    mBinding.currentWeather.setInfo(weatherInfos.get(0));
                }
            }
        });
    }

    /**
     * Initializes view pager with fragments current day's forecast and week's one
     */
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

    /**
     * Initializing navigation elements and collaboration among it
     */
    private void initNavigation() {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mBinding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mBinding.container.setCurrentItem(mModel.getPositionById(item.getItemId()));
                return true;
            }
        });

        mBinding.container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.bottomNavigation.setSelectedItemId(mModel.getIdByPosition(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Initializes spinner adapter for choosing city
     */
    private void initSpinnerAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.arr_cities));
        mBinding.currentWeather.citySp.setAdapter(adapter);
        String defaultCity = "Ростов-на-Дону";
        String curLocation = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE)
                .getString(Utils.PREF_LOCATION, defaultCity);
        mBinding.currentWeather.citySp
                .setSelectedIndex(mModel.getPositionByLocation(this, curLocation));
        mBinding.currentWeather.citySp
                .setOnItemSelectedListener(mModel.getOnSpinnerSelectedListener(this, mBinding.refresh));
    }
}
