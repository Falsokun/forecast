package com.example.olesya.forecast.pojo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import com.example.olesya.forecast.R;
import com.example.olesya.forecast.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Entity
public class WeatherInfo implements Serializable {
    @Expose
    private int type;

    @PrimaryKey
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

    public WeatherInfo(int type, long timeInMillis, String icon, float temperature,
                       float temperatureHigh, float temperatureLow, float realFeel,
                       double humidity, double windSpeed, String summary) {
        this.type = type;
        this.timeInMillis = timeInMillis;
        this.icon = icon;
        this.temperature = temperature;
        this.temperatureHigh = temperatureHigh;
        this.temperatureLow = temperatureLow;
        this.realFeel = realFeel;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.summary = summary;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getStringWindSpeed() {
        return String.valueOf(windSpeed);
    }

    public String getStringHumidity() {
        return String.valueOf(humidity);
    }

    public String getStringMaxMinTemperature() {
        return String.valueOf((int) temperatureHigh) + (char) 0x00B0 + "\n" + String.valueOf((int) temperatureLow) + (char) 0x00B0;
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
