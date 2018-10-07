package com.example.olesya.forecast;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.example.olesya.forecast.pojo.WeatherInfo;
import com.example.olesya.forecast.pojo.WeatherResponse;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel {

    /**
     * Gets refresh listener
     *
     * @param context       - context param
     * @param refreshLayout - handles refreshlayout state
     * @return refresh listener
     */
    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener(final Context context,
                                                                     final SwipeRefreshLayout refreshLayout) {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(context, refreshLayout);
            }
        };
    }

    /**
     * Got new data from api and upload it to local database
     *
     * @param context       - context param
     * @param refreshLayout - notifies about end of loading operation
     */
    public void refreshData(final Context context, final SwipeRefreshLayout refreshLayout) {
        DarkSkyService darkSkyService = App.getRetrofitInstance().create(DarkSkyService.class);
        if (Utils.isInternetAvailable(context)) {
            App.getDBInstance(context).weatherDao().clear();
        }

        darkSkyService.weatherOnLocationInfo(getLocation(context),
                getCurrentUnits(context), Locale.getDefault().getLanguage())
                .enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull final Response<WeatherResponse> response) {
                if (response.body() == null)
                    return;

                refreshLayout.setRefreshing(false);
                ArrayList<WeatherInfo> typed = initTypes(response.body());
                saveToRoom(context, typed);
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(context, context.getString(R.string.err_no_internet), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Creates an array of WeatherInfo type with initialized field "type",
     * which corresponds to types of data stored
     *
     * @param body - data got from api
     * @return arraylist of weatherinfo, which stores all data needed
     */
    private ArrayList<WeatherInfo> initTypes(WeatherResponse body) {
        ArrayList<WeatherInfo> info = new ArrayList<>();
        for (WeatherInfo wi : body.getHourly().getData()) {
            wi.setType(Utils.WEATHER_TYPES.DAY);
            info.add(wi);
        }

        for (WeatherInfo wi : body.getDaily().getData()) {
            wi.setType(Utils.WEATHER_TYPES.WEEK);
            info.add(wi);
        }

        body.getCurrently().setType(Utils.WEATHER_TYPES.CURRENT);
        info.add(body.getCurrently());
        return info;
    }

    /**
     * Saving {@param data} to database
     *
     * @param context - param to access db
     * @param data    - data to save
     */
    private void saveToRoom(Context context, List<WeatherInfo> data) {
        App.getDBInstance(context).weatherDao().clear();
        App.getDBInstance(context).weatherDao().insertInfo(data);
    }

    /**
     * Get units which are used to display temperature
     *
     * @param context - context param
     * @return darksky api const <value>si</value> or <value>us</value>
     */
    public String getCurrentUnits(Context context) {
        boolean isSi = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE)
                .getBoolean(Utils.PREF_SI, false);
        return isSi ? Utils.DARKSKY_CONST.UNIT_SI : Utils.DARKSKY_CONST.UNIT_US;
    }

    /**
     * Gets id from menu layout due to position
     *
     * @param position - position in menu
     * @return {@param position}'s index
     */
    public int getIdByPosition(int position) {
        if (position == 0) {
            return R.id.menu_day;
        }

        return R.id.menu_week;
    }

    /**
     * Gets id from menu layout due to position
     *
     * @param id - id in menu
     * @return {@param id}'s position
     */
    public int getPositionById(int id) {
        if (id == R.id.menu_day) {
            return 0;
        }

        return 1;
    }

    /**
     * Find position from location
     *
     * @param context  - context to get access to resourses
     * @param location - location name
     * @return location's index in R.array.cities
     */
    public int getPositionByLocation(Context context, String location) {
        String[] arr = context.getResources().getStringArray(R.array.arr_cities);
        return Arrays.asList(arr).indexOf(location);
    }

    /**
     * Returns location name due to last value chosen by user or default
     *
     * @param context - context variable
     * @return location in format 'latitude,longitude'
     */
    private String getLocation(Context context) {
        String defaultCity = context.getResources().getStringArray(R.array.arr_cities)[0];
        String locationName = context
                .getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE)
                .getString(Utils.PREF_LOCATION, defaultCity);
        int index = getPositionByLocation(context, locationName);
        String enLocation = Utils.CITIES[index];
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> res = geocoder.getFromLocationName(enLocation, 1);
            return res.get(0).getLatitude() + "," + res.get(0).getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns spinner listener which reacts on city changing
     * And uploads new data to db
     *
     * @param context       - context element
     * @param refreshLayout - reacts on uploading data change
     * @return itemsSelectedListener
     */
    public MaterialSpinner.OnItemSelectedListener getOnSpinnerSelectedListener(final Context context,
                                                                               final SwipeRefreshLayout refreshLayout) {
        return new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE)
                        .edit()
                        .putString(Utils.PREF_LOCATION, context.getResources().getStringArray(R.array.arr_cities)[position])
                        .apply();
                refreshData(context, refreshLayout);
                refreshLayout.setRefreshing(true);
            }
        };
    }
}
