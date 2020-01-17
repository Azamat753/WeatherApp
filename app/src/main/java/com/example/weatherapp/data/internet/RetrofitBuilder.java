package com.example.weatherapp.data.internet;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    private static RetrofitService retrofitService;


    public static RetrofitService getService() {
        if (retrofitService == null) retrofitService = buildRetrofit();

        return retrofitService;


    }

    public static RetrofitService buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitService.class);
    }
}
