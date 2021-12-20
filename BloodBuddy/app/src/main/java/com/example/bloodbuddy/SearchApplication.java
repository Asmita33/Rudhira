package com.example.bloodbuddy;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;

import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.search.MapboxSearchSdk;

public class SearchApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MapboxSearchSdk.initialize(this, getString(R.string.mapbox_access_token),
                LocationEngineProvider.getBestLocationEngine(this));
    }

}
