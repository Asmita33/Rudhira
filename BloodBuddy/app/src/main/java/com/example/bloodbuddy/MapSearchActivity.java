package com.example.bloodbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.ui.view.SearchBottomSheetView;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.search.CategorySearchEngine;
import com.mapbox.search.CategorySearchOptions;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchCallback;
import com.mapbox.search.SearchRequestTask;
import com.mapbox.search.result.SearchResult;

import java.util.List;

public class MapSearchActivity extends AppCompatActivity
{
    SearchBottomSheetView searchBottomSheetView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Application app = getApplication();

        MapboxSearchSdk.initialize(app, getString(R.string.mapbox_access_token)
        , LocationEngineProvider.getBestLocationEngine(this));

        setContentView(R.layout.activity_map_search);

        searchBottomSheetView = findViewById(R.id.map_search_view);
        searchBottomSheetView.initializeSearch(savedInstanceState,
                new SearchBottomSheetView.Configuration());

        if(!checkAccessFineLocPermission()){
//            ActivityCompat.requestPermissions(this, );

        }

    }


    private boolean checkAccessFineLocPermission()
    {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}
