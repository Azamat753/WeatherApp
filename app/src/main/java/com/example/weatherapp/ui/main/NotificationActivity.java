package com.example.weatherapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.R;
import com.example.weatherapp.ui.base.BaseMapActivity;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

public class NotificationActivity extends BaseMapActivity {

Button start,stop;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_notification;
    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notification);
//    }

    public void back(View view) {
        startActivity(new Intent(this,MainActivity.class));
    }


    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return false;
    }
}