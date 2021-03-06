package com.example.weatherapp.data.internet;

import com.example.weatherapp.data.entity.CurrentWeather;
import com.example.weatherapp.data.entity.ForecastEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {


    @GET("weather")
    Call<CurrentWeather> fetchtCurrentWeather(@Query("q") String city,
                                              @Query("appid") String appid,
                                              @Query("units") String units);


    @GET("forecast")
    Call<ForecastEntity> frcstWeather(@Query("q") String city,
                                      @Query("units") String units,
                                      @Query("appid") String appID);


}


