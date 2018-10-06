package com.example.olesya.forecast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
    public static final String ST_WEATHER_DAY = "ST_WEATHER_DAY";
    public static final String ST_WEATHER_WEEK = "ST_WEATHER_WEEK";
    public static final String ST_WEATHER_OBJ = "ST_WEATHER_OBJ";
    public static final String ST_CLEAR = "ST_CLEAR";
    public static final String PREF_SI = "com.example.olesya.forecast.pref_si";
    public static final String PREF_SAVE = "com.example.olesya.forecast.pref_save";
    public static final String PREF_LOCATION = "com.example.olesya.forecast.pref_location";

    public class DARKSKY_ICON_CONST {
        public static final String CLEAR_DAY = "clear-day";
        public static final String CLEAR_NIGHT = "clear-night";
        public static final String RAIN = "rain";
        public static final String SNOW = "snow";
        public static final String SLEET = "sleet";
        public static final String WIND = "wind";
        public static final String FOG = "fog";
        public static final String CLOUDY = "cloudy";
        public static final String PARTLY_CLOUDY_DAY = "partly-cloudy-day";
        public static final String PARTLY_CLOUDY_NIGHT = "partly-cloud-night";

        public static final String UNIT_SI = "si";
        public static final String UNIT_US = "us";
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
