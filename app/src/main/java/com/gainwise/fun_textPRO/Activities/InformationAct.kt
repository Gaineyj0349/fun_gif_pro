package com.gainwise.fun_textPRO.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gainwise.fun_textPRO.R
import spencerstudios.com.bungeelib.Bungee

class InformationAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPause() {
        super.onPause()
        Bungee.diagonal(this)
    }
}
