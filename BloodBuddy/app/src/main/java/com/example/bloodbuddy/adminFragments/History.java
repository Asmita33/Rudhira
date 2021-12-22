package com.example.bloodbuddy.adminFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bloodbuddy.R;
import com.example.bloodbuddy.bloodRequest.History.Donors;
import com.example.bloodbuddy.bloodRequest.History.RegisteredRequests;

public class History extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Button donors,registeredRequests;

    public History() {
        // Required empty public constructor
    }

    public static History newInstance(String param1, String param2) {
        History fragment = new History();
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

        View v=inflater.inflate(R.layout.fragment_history, container, false);
        donors=v.findViewById(R.id.donors);
        registeredRequests=v.findViewById(R.id.registered_request);

        //Intent to donors history activity
        donors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent i=new Intent(getContext(),Donors.class);
                        startActivity(i);
            }
        });
        //Intent to registered request activity
        registeredRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                       Intent i=new Intent(getContext(), RegisteredRequests.class);
                       startActivity(i);
            }
        });

        return v;
    }
}