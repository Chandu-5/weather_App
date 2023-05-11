package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.databinding.WeatherRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<WeatherRVModel> weatherRVModelArrayList;

    public WeatherRVAdapter(@NonNull final Context context, @NonNull final ArrayList<WeatherRVModel> weatherRVModelArrayList) {
        this.context = context;
        this.weatherRVModelArrayList = weatherRVModelArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final WeatherRvItemBinding weatherRvItemBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weatherRvItemBinding = WeatherRvItemBinding.bind(itemView);
        }
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, final int position) {
        final WeatherRVModel weatherRVModel = weatherRVModelArrayList.get(position);
        holder.weatherRvItemBinding.TVTemperature.setText(weatherRVModel.getCelsius());
        Picasso.get()
                .load(Constants.HTTP_PREFIX.concat(weatherRVModel.getIcon()))
                .into(holder.weatherRvItemBinding.IVCondition);
        holder.weatherRvItemBinding.TVWindspeed.setText(weatherRVModel.getWindSpeedDisplay());
        final String formattedDateFromTimestampLocal = Constants.getFormattedDateFromTimestampLocal(weatherRVModel.getTime());
        holder.weatherRvItemBinding.TVTime.setText(formattedDateFromTimestampLocal);
    }

    @Override
    public int getItemCount() {
        return weatherRVModelArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void listUpdated() {
        notifyDataSetChanged();
    }
}
