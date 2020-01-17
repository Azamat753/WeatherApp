package com.example.weatherapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R;
import com.example.weatherapp.data.entity.CurrentWeather;
import com.example.weatherapp.data.entity.ForecastEntity;
import com.example.weatherapp.data.internet.RetrofitBuilder;
import com.example.weatherapp.ui.base.BaseActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    @BindView(R.id.day)
    TextView day;

    @BindView(R.id.month_year)
    TextView month_year;

    @BindView(R.id.country_spinner)
    Spinner country;

    @BindView(R.id.country_tv)
    TextView country_tv;

    @BindView(R.id.tv_now)
    TextView tv_now;

    @BindView(R.id.tv_today)
    TextView tv_today;

    @BindView(R.id.now_number_txt)
    TextView now_number;

    @BindView(R.id.today_number_txt)
    TextView today_number;

    @BindView(R.id.min_number)
    TextView min_number;

    @BindView(R.id.min_txt)
    TextView min_tv;

    @BindView(R.id.max_txt)
    TextView max_tv;

    @BindView(R.id.tv_wind)
    TextView tv_wind;

    @BindView(R.id.wind_number)
    TextView wind_number;

    @BindView(R.id.tv_pressure)
    TextView pressure_tv;

    @BindView(R.id.pressure_number)
    TextView pressure_number;

    @BindView(R.id.humidity)
    TextView humidity_tv;

    @BindView(R.id.humidity_procent)
    TextView humidity_number;

    @BindView(R.id.cloudliness_big)
    TextView tv_cloudliness_big;

    @BindView(R.id.cloudliness_procent)
    TextView cloundliness_procent;

    @BindView(R.id.sunrise)
    TextView sunrise_tv;

    @BindView(R.id.sunrise_number)
    TextView sunrise_number;


    @BindView(R.id.sunset)
    TextView sunset_tv;


    @BindView(R.id.sunset_number)
    TextView sunset_number;

    @BindView(R.id.search)
    Button search;

    @BindView(R.id.weather_image)
    ImageView weatherImage;


//    @BindView(R.id.day_item)
//    TextView item_day_tv;

//    @BindView(R.id.tvMaxTemp_item)
//    TextView item_tvMaxTemp_tv;
//
//    @BindView(R.id.tvMinTemp_item)
//    TextView item_tvMinTemp_tv;
//
//    @BindView(R.id.image_item)
//    ImageView item_image_tv;

    public static String WEATHER_DATA = "weather";

    private RecyclerView recyclerView;
    private ForecastAdapter adapter;



    @Override
    protected int getViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
      //  initRecycler();
        initListeners();
        // getData();
        fetchCurrentWeather("Bishkek");
        getDay();
        getMonth();
        addInSpinner();
        fetchForcastWeather();
//        sendInDayRecycler();
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }
    private void fetchCurrentWeather(String city) {
        RetrofitBuilder.getService().fetchtCurrentWeather(city,
                "4d63c1acf9a085448b23971128e5eddd", "metric").enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fillViews(response.body());

                    Glide.with(getApplicationContext())
                            .load("http://openweathermap.org/img/wn/"
                                    + response.body().getWeather().get(0).getIcon() + "@2x.png")
                            .into(weatherImage);
                }
            }
            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {

            }
        });
    }

    private void fillViews(CurrentWeather weather) {

        now_number.setText(weather.getMain().getTemp().toString());
        today_number.setText(weather.getMain().getTempMax().toString());
        min_number.setText(weather.getMain().getTempMin().toString());
        wind_number.setText(weather.getWind().getSpeed().toString());
        pressure_number.setText(weather.getMain().getPressure().toString());
        humidity_number.setText(weather.getMain().getHumidity().toString());
        cloundliness_procent.setText(weather.getClouds().getAll().toString());
        sunrise_number.setText(getData(weather.getSys().getSunrise()));
        sunset_number.setText(getData(weather.getSys().getSunset()));


    }

    public void getMonth() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM.yyyy", Locale.getDefault());
        String monthData = dateFormat.format(currentDate);
        month_year.setText(monthData);
    }

    public void getDay() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String dayData = dateFormat.format(currentDate);
        day.setText(dayData);
    }


    public void initListeners() {
        search.setOnClickListener(v -> fetchCurrentWeather(country_tv.getText().toString()));

    }

    public static String getData(Integer sunrise) {
        Date date = new Date(sunrise * 1000L);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT+06:00"));
        return format.format(date);
    }

    public void addInSpinner() {

        Spinner spinner = findViewById(R.id.country_spinner);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Бишкек");
        arrayList.add("Чуй");
        arrayList.add("Нарын");
        arrayList.add("Талас");
        arrayList.add("Баткен");
        arrayList.add("Ош");
        arrayList.add("Джалал-Абад");
        arrayList.add("Москва");
        arrayList.add("Лондон");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Выбран город: " + tutorialsName, Toast.LENGTH_LONG).show();
                country_tv.setText(tutorialsName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initRecycler(ArrayList<CurrentWeather> list) {
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ForecastAdapter();
        recyclerView.setAdapter(adapter);
        adapter.update(list);
    }

    public void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
    }

//    private void getData() {
//        Intent intent = getIntent();
//        ForecastEntity forecastEntity = (ForecastEntity) intent.getSerializableExtra(WEATHER_DATA);
//        adapter.update(forecastEntity.getForecastWeatherList());
//    }

//    public void sendInDayRecycler() {
//        Date currentDate = new Date();
//        DateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
//        String dayData = dateFormat.format(currentDate);
//        //  item_day_tv.setText(dayData);
////    }
//
//        //  public void sendTempInRecycler(CurrentWeather weather) {
//        //    item_tvMaxTemp_tv.setText(weather.getMain().getTempMax().toString());
//        //   item_tvMinTemp_tv.setText(weather.getMain().getTempMin().toString());
//        //}


  //  }
    private void fetchForcastWeather() {
        RetrofitBuilder.getService()
                .frcstWeather("Bishkek","metric","4d63c1acf9a085448b23971128e5eddd")
                .enqueue(new Callback<ForecastEntity>() {
                    @Override
                    public void onResponse(Call<ForecastEntity> call, Response<ForecastEntity> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            initRecycler(response.body().getForecastWeatherList());
                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastEntity> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
