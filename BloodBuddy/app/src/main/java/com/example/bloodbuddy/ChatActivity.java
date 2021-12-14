package com.example.bloodbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bloodbuddy.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());  // took care of all findVIewByID
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("name");
        String mobile = getIntent().getStringExtra("mobile");

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}