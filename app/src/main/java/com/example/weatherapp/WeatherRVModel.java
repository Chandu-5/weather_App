package com.example.weatherapp;

import android.provider.SyncStateContract;

import androidx.annotation.NonNull;

public class WeatherRVModel {

    private String time;
    private String temperature;
    private String icon;
    private String windSpeed;

    public WeatherRVModel(String time, String temperature, String icon, String windSpeed) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.windSpeed = windSpeed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getCelsius() {
        return (temperature + Constants.CELSIUS_SUFFIX);
    }

    public String getWindSpeedDisplay() {
        return (windSpeed + Constants.WIND_SPEED_SUFFIX);
    }

    @NonNull
    @Override
    public String toString() {
        return "WeatherRVModel{" +
                "time='" + time + '\'' +
                ", temperature='" + temperature + '\'' +
                ", icon='" + icon + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                '}';
    }
}