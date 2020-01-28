package com.example.weatherapp.ui.base;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weatherapp.R;
import com.example.weatherapp.data.NotificationHelper;
import com.example.weatherapp.data.internet.SendLocation;
import com.example.weatherapp.ui.main.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMapActivity extends BaseMap implements PermissionsListener, MapboxMap.OnMapClickListener {

    protected MapView mapView;
    protected MapboxMap map;
    private LocationComponent locationComponent;
    private PermissionsManager permissionsManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    ArrayList<Double> locations = new ArrayList<>();
    NotificationHelper notificationHelper;
    SendLocation sendLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(this, "pk.eyJ1Ijoicm9qc2FzaGEiLCJhIjoiY2p6MTB6ZjI2MGd4aTNlbXl4Mzd5YTV1dSJ9.uf5mvNIAZZEg4UpGGd5w7Q");
        super.onCreate(savedInstanceState);
        initViews();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(Style.SATELLITE_STREETS, style -> {
            map = mapboxMap;
            mapboxMap.addOnMapClickListener(this);
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            cameraUpdate(new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    });
            BuildingPlugin buildingPlugin = new BuildingPlugin(mapView, map, style);
            buildingPlugin.setVisibility(true);

            enableLocationComponent(style);

        }));

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void cameraUpdate(LatLng latLng) {
        CameraPosition position = new CameraPosition.Builder()
                .target(latLng) // Sets the new camera position
                .zoom(17) // Sets the zoom
                .bearing(180) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 7000);
    }

    private void enableLocationComponent(Style style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(BaseMapActivity.this, location -> {
                if (location != null) {
                    locations.add(location.getLatitude());
                    locations.add(location.getLongitude());
                    Log.e("TAG", "enableLocationComponent: " + location.getLatitude() + " - " + location.getLongitude());
                    notificationHelper = new NotificationHelper(location.getLongitude(), location.getLatitude());
                }
            });

            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .elevation(5)
                    .accuracyAlpha(.6f)
                    .accuracyColor(Color.GREEN)
                    .build();
            locationComponent = map.getLocationComponent();
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, style)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();

            // Activate with options
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            locationComponent.setCameraMode(CameraMode.TRACKING);

            locationComponent.setRenderMode(RenderMode.COMPASS);
            Location location = locationComponent.getLastKnownLocation();
            if (location != null) {
                CameraPosition position = new CameraPosition.Builder().target(new LatLng(location.getLatitude()
                        , location.getLongitude())).zoom(12).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000);

            }

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void initViews() {
        mapView = findViewById(R.id.mapView);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            map.getStyle(style -> enableLocationComponent(style));
        }

    }
}



