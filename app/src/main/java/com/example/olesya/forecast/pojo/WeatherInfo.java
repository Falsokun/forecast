package com.example.olesya.forecast.pojo;

import android.content.Context;

import com.example.olesya.forecast.R;
import com.example.olesya.forecast.Utils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeatherInfo implements Serializable {

    @SerializedName("time")
    private long timeInMillis;

    private String icon;

    /**
     * In Fahrenheits
     */
    private float temperature;

    private float temperatureHigh;

    private float temperatureLow;

    @SerializedName("apparentTemperature")
    private float realFeel;

    private double humidity;

    private double windSpeed;

    private String summary;

    //region getters and setters

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public String getIcon() {
        return icon;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getTemperatureHigh() {
        return temperatureHigh;
    }

    public float getTemperatureLow() {
        return temperatureLow;
    }

    public float getRealFeel() {
        return realFeel;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getSummary() {
        return summary;
    }
    //endregion

    public String getStringTemperature() {
        return String.valueOf((int)temperature) + (char) 0x00B0;
    }

    public String getStringTime() {
        return new Date(timeInMillis).toString();
    }

    public String getStringDayTime() {
        String pattern = "EEEE, dd HH:00";
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(timeInMillis * 1000));
    }

    public String getDate() {
        String pattern = "dd.MM.yy";
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(timeInMillis * 1000));
    }

    public String getHour() {
        String pattern = "HH:00";
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(timeInMillis * 1000));
    }

    public String getDayOfWeek(Context context) {
        String patternDayOfWeek = "EEEE";
        String todayDOW = new SimpleDateFormat(patternDayOfWeek, Locale.getDefault())
                .format(Calendar.getInstance().getTime());
        Date current = new Date(timeInMillis * 1000);
        String currentDOW = new SimpleDateFormat(patternDayOfWeek, Locale.getDefault()).format(current);
        if (todayDOW.equals(currentDOW)) {
            return context.getString(R.string.today);
        } else {
            return currentDOW;
        }
    }

    public int getDrawable() {
        switch (icon) {
            case Utils.DARKSKY_ICON_CONST.CLEAR_NIGHT:
                return R.drawable.ic_moon;
            case Utils.DARKSKY_ICON_CONST.RAIN:
                return R.drawable.ic_rain;
            case Utils.DARKSKY_ICON_CONST.SNOW:
                return R.drawable.ic_snow;
            case Utils.DARKSKY_ICON_CONST.SLEET:
                return R.drawable.ic_sleet;
            case Utils.DARKSKY_ICON_CONST.WIND:
                return R.drawable.ic_wind;
            case Utils.DARKSKY_ICON_CONST.FOG:
                return R.drawable.ic_mist;
            case Utils.DARKSKY_ICON_CONST.CLOUDY:
                return R.drawable.ic_cloud;
            case Utils.DARKSKY_ICON_CONST.PARTLY_CLOUDY_DAY:
                return R.drawable.ic_partly_cloud_day;
            case Utils.DARKSKY_ICON_CONST.PARTLY_CLOUDY_NIGHT:
                return R.drawable.ic_partly_cloud_night;
            default:
                return R.drawable.ic_sun;
        }
    }
}
