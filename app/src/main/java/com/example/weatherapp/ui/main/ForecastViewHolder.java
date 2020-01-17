package com.example.weatherapp.ui.main;

import android.annotation.SuppressLint;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R;
import com.example.weatherapp.data.entity.CurrentWeather;
import com.utils.DateParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ForecastViewHolder extends RecyclerView.ViewHolder {
    private TextView tvMinTemp_item;
    private TextView tvMaxTemp_item;
    private TextView tvday_item;
    private ImageView image_item;

    public ForecastViewHolder(@NonNull View itemView) {
        super(itemView);
        tvday_item=itemView.findViewById(R.id.day_item);
        tvMinTemp_item = itemView.findViewById(R.id.tvMinTemp_item);
        tvMaxTemp_item = itemView.findViewById(R.id.tvMaxTemp_item);
        image_item = itemView.findViewById(R.id.image_item);
    }

    @SuppressLint({"StringFormatInvalid", "StringFormatMatches"})
    public void bind(String dt, String max, String min, String img) {
        try {
            tvday_item.setText(DateParser.foreCastDate(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvMinTemp_item.setText(max);
        tvMaxTemp_item.setText(min);
        Glide.with(itemView)
                .load("http://openweathermap.org/img/wn/" + img + "@2x.png")
                .into(image_item);
    }
}
