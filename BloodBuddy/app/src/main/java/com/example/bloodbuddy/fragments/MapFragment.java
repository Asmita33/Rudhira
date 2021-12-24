package com.example.bloodbuddy.fragments;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bloodbuddy.MapSearchActivity;
import com.example.bloodbuddy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
//import com.mapbox.maps.
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements LocationListener, PermissionsListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MapView mapview;
    FloatingActionButton mapfab;

    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
//    private LocationEngine locationEngine;
//    private LocationLayerPlugin; deprecated
    private LocationComponentPlugin locationComponentPlugin;
    private Location originLocation;



    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapview = view.findViewById(R.id.mapView);

        mapview.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(14.0).build());
        mapview.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
            }
        });

        mapfab = view.findViewById(R.id.map_fab);
        mapfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(getContext(), MapSearchActivity.class);
                startActivity(i);
            }
        });

//        enableLocationPermission();
//        initLocationComponent();
//        setupGesturesListener();
        mapboxMap = mapview.getMapboxMap();

        return view;
    }

    private void enableLocationComponent(Style loadedMapStyle)
    {
        if(PermissionsManager.areLocationPermissionsGranted(getContext())){
//            locationComponentPlugin = mapboxM
        }
    }



    private void enableLocationPermission() {
        if(PermissionsManager.areLocationPermissionsGranted(getContext())){
            // we have the permission
        }
        else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    // why we need access
    @Override
    public void onExplanationNeeded(List<String> list) {
        Toast.makeText(getContext(), "We need this permission to show your location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted)
            enableLocationPermission();
    }
}