package com.example.weatherapp;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class Constants {

    public static final String HTTP_PREFIX = "http://";
    public static final String CELSIUS_SUFFIX = " °C";
    public static final String WIND_SPEED_SUFFIX = " Km/h";

    public static final String FORECAST_KEY = "forecast";
    public static final String FORECAST_DAY_KEY = "forecastday";
    public static final String HOUR_KEY = "hour";
    public static final String TIME_EPOCH_KEY = "time_epoch";
    public static final String TIME_KEY = "time";
    public static final String TIME_C_KEY = "temp_c";
    public static final String CONDITION_KEY = "condition";
    public static final String ICON_KEY = "icon";
    public static final String WIND_KPH_KEY = "wind_kph";

    private static final String SINGLE_SPACE = " ";
    private static final String HH_MM_SS = "HH:mm:ss";
    private static final String MMM_DD__YYYY = "MMM dd, yyyy";
    private static final String APP_STANDARD_DATE_FORMAT = MMM_DD__YYYY + SINGLE_SPACE + HH_MM_SS;

    @NotNull
    public static String getFormattedDateFromTimestampLocal(@NotNull final String timestamp) {
        final Timestamp timestampObject = new Timestamp(Long.parseLong(timestamp));
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(APP_STANDARD_DATE_FORMAT, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(timestampObject);
    }
}