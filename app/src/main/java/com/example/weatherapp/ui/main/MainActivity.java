package com.example.weatherapp.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R;
import com.example.weatherapp.data.entity.CurrentWeather;
import com.example.weatherapp.data.entity.ForecastEntity;
import com.example.weatherapp.data.internet.RetrofitBuilder;
import com.example.weatherapp.ui.base.BaseActivity;
import com.example.weatherapp.ui.base.MyForegroundService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.security.Permission;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.Inflater;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.weatherapp.ui.base.MyForegroundService.IS_SERVICE_ACTIVE;

public class MainActivity extends BaseActivity {


    private FusedLocationProviderClient fusedLocationProviderClient;
    private final int REQUEST_CODE = 1001;

    String[] permissions = new String[2];


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



    @BindView(R.id.notification_btn)
    Button not_ch;

    @BindView(R.id.btnStart)
    Button start;

    @BindView(R.id.btnStop)
    Button stop;

//@BindView(R.id.toolbar_main)
  //  Toolbar toolbarMain;

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
        //setSupportActionBar(toolbarMain);
        initViews();
        initListeners();
        fetchCurrentWeather("Bishkek");
        getDay();
        getMonth();
        addInSpinner();
        fetchForcastWeather("Bishkek");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
        permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;

        if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();


        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_CODE);
            }


        }

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            for (int results : grantResults) {
                if (results == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                }

            }
        }
    }

    void getLastLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
               // if (location!=null)
                //search.setText(String.valueOf(location.getLatitude()));
            }
        });
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
        search.setOnClickListener(v ->
                fetchCurrentWeather(country_tv.getText().toString())

             );
        search.setOnClickListener(v -> fetchForcastWeather(country_tv.getText().toString()));
        Log.e("-------","--------");


        not_ch.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,NotificationActivity.class)));
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
                Toast.makeText(parent.getContext(), "Выберите город: " , Toast.LENGTH_LONG).show();

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

    private void getData() {
        Intent intent = getIntent();
        ForecastEntity forecastEntity = (ForecastEntity) intent.getSerializableExtra(WEATHER_DATA);
        adapter.update(forecastEntity.getForecastWeatherList());
   }

    private void fetchForcastWeather(String s) {
        RetrofitBuilder.getService()
                .frcstWeather(s, "metric", "4d63c1acf9a085448b23971128e5eddd")
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
        private void actionService(boolean isActivated) {
            Intent intent = new Intent(this, MyForegroundService.class);
            intent.putExtra(IS_SERVICE_ACTIVE, isActivated);
            startService(intent);
        }
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnStart:
                    actionService(true);
                    start.setVisibility(View.GONE);
                    stop.setVisibility(View.VISIBLE);
                    break;
                case R.id.btnStop:
                    actionService(false);
                    stop.setVisibility(View.GONE);
                    start.setVisibility(View.VISIBLE);
                    break;
            }
        }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_service,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.service_start:
//                actionService(true);
//                    break;
//            case R.id.service_stop:
//                actionService(false);
//                    break;
//        }
//        return super.onOptionsItemSelected(item);
    

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Удачного дня \uD83D\uDE0A ", Toast.LENGTH_SHORT).show();

    }


}
