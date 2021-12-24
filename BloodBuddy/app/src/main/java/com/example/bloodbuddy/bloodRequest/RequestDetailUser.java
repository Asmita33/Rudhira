package com.example.bloodbuddy.bloodRequest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.bloodbuddy.R;
import com.example.bloodbuddy.databinding.ActivityRequestDetailAdminBinding;
import com.example.bloodbuddy.databinding.ActivityRequestDetailUserBinding;

public class RequestDetailUser extends AppCompatActivity {
    ActivityRequestDetailUserBinding activityRequestDetailUserBinding;
    private String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRequestDetailUserBinding=  ActivityRequestDetailUserBinding.inflate(getLayoutInflater());
        View view=activityRequestDetailUserBinding.getRoot();
        setContentView(view);

        number=getIntent().getStringExtra("mobile");

    }
}