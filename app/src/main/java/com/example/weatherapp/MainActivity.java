package com.example.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSION_CODE = 1;

    private ActivityMainBinding mainBinding;
    private final ArrayList<WeatherRVModel> weatherRVModelArrayList = new ArrayList<>();
    private WeatherRVAdapter weatherRVAdapter;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModelArrayList);
        mainBinding.RVWeather.setAdapter(weatherRVAdapter);

        getLocation();
        getWeatherInfo();

        mainBinding.IVSearch.setOnClickListener(v -> {
            final String city = Objects.requireNonNull(mainBinding.EdtCity.getText()).toString();
            if (city.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please Enter Cityname", Toast.LENGTH_LONG).show();
//                Toast.makeText(MainActivity.this, R.string.please_enter_city_name, Toast.LENGTH_LONG).show();
            } else {
                cityName = city;
                mainBinding.TVCityName.setText(cityName);
                getWeatherInfo();
            }
        });
    }

    private void getLocation() {
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
        final Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityName = getCityName(location.getLongitude(), location.getLatitude());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "PERMISSION GRANTED", Toast.LENGTH_LONG).show();
//                Toast.makeText(MainActivity.this, R.string.permission_granted, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Please provide the permissions", Toast.LENGTH_LONG).show();
//                Toast.makeText(MainActivity.this, R.string.please_provide_the_permissions, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private String getCityName(final double longitude, final double latitude) {
        String newCityName = "Not Found";
        final Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            final List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);
            for (Address adr : addresses) {
                if (adr != null) {
                    final String city = adr.getLocality();
                    if (city != null && !city.equals("")) {
                        newCityName = city;
                    } else {
                        Log.d("TAG", "CITY NOT FOUND");
                        Toast.makeText(this, "User city Not Found.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return newCityName;
    }

    private void getWeatherInfo() {
        if (cityName == null) {
            getLocation();
        }
        final String url = "http://api.weatherapi.com/v1/forecast.json?key=28b5f124cd884445966170416231504&q=" + cityName + "&days=1&aqi=yes&alerts=yes";
        mainBinding.TVCityName.setText(cityName);
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::updateUIForResponse,
                error -> {
                    // onErrorResponse.
                    error.printStackTrace();
                    Toast.makeText(MainActivity.this, "Please Enter Valid City Name", Toast.LENGTH_LONG).show();
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void updateUIForResponse(final @Nullable JSONObject response) {
        runOnUiThread(() -> updateUI(response));
    }

    private void updateUI(final @Nullable JSONObject response) {
        if (response == null) {
            Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_LONG).show();
            finish();
        } else {
            mainBinding.PBLoading.setVisibility(View.GONE);
            mainBinding.RLHome.setVisibility(View.VISIBLE);
            weatherRVModelArrayList.clear();
            try {
                final String temperature = response.getJSONObject("current").getString("temp_c");
                mainBinding.TVTemperature.setText(temperature);
                mainBinding.TVTemperature.append(Constants.CELSIUS_SUFFIX.trim());
                final int isDay = response.getJSONObject("current").getInt("is_day");
                final String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                final String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                Picasso.get().load(Constants.HTTP_PREFIX.concat(conditionIcon)).into(mainBinding.IVIcon);
                mainBinding.TVCondition.setText(condition);
                // TODO:
                /*if (isDay == 1) {
                    Picasso.get().load("").into(mainBinding.IVBlack);
                } else {
                    Picasso.get().load("").into(mainBinding.IVBlack);
                }*/
                final JSONObject forecastObj = response.getJSONObject(Constants.FORECAST_KEY);
                final JSONObject forecastDay = forecastObj.getJSONArray(Constants.FORECAST_DAY_KEY).getJSONObject(0);
                final JSONArray hourArray = forecastDay.getJSONArray(Constants.HOUR_KEY);
                for (int i = 0; i < hourArray.length(); i++) {
                    final JSONObject hourObj = hourArray.getJSONObject(i);
                    final long timeEpoch = hourObj.getLong(Constants.TIME_EPOCH_KEY);
                    final int temperatureCelsius = hourObj.getInt(Constants.TIME_C_KEY);
                    final String img = hourObj.getJSONObject(Constants.CONDITION_KEY).getString(Constants.ICON_KEY);
                    final int windInKpH = hourObj.getInt(Constants.WIND_KPH_KEY);
                    weatherRVModelArrayList.add(new WeatherRVModel(String.valueOf(timeEpoch), String.valueOf(temperatureCelsius), img, String.valueOf(windInKpH)));
                }
                weatherRVAdapter.listUpdated();
            } catch (JSONException e) {
//                Toast.makeText(MainActivity.this, R.string.error_occurred, Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                throw new RuntimeException(e);
            }
        }
    }
}

// API Key : 28b5f124cd884445966170416231504