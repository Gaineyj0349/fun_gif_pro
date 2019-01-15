package com.gainwise.fun_textPRO.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gainwise.fun_textPRO.R;

import spencerstudios.com.bungeelib.Bungee;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bungee.diagonal(this);
    }
}
