package com.r.uebook.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.r.uebook.R;
import com.r.uebook.ui.login_register.LoginActivity;
import com.r.uebook.utils.AppUtils;
import com.r.uebook.utils.ApplicationConstants;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(() -> {
            if (AppUtils.isLoggedIn()) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
            finish();
        }, ApplicationConstants.SPLASH_TIME_OUT);
    }

}