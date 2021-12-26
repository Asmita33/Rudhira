package com.example.bloodbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bloodbuddy.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {

    TextView textView;
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        textView = findViewById(R.id.SplashAppName);
        lottieAnimationView = findViewById(R.id.lottieSplash);

//        textView.animate().translationX(-1400).setDuration(2700).setStartDelay(0);




        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);

    }
}