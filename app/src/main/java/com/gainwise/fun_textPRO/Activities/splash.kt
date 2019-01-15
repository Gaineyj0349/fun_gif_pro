package com.gainwise.fun_textPRO.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.gainwise.fun_textPRO.R
import com.gainwise.fun_textPRO.Utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_splash.*
import spencerstudios.com.bungeelib.Bungee

class splash : AppCompatActivity(), Animation.AnimationListener {
    override fun onAnimationRepeat(p0: Animation?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAnimationEnd(p0: Animation?) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()}

    override fun onAnimationStart(p0: Animation?) {
        splashimage.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Utils.didCrash(this)){
            startActivity(Intent(this,CrashAct::class.java))
            finish()
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)


        splashimage.visibility = View.INVISIBLE
        Picasso.get().load(R.drawable.splash).into(splashimage)
        val alphaAnimation = AlphaAnimation(0f,1f)
        alphaAnimation.duration = 500
        alphaAnimation.setAnimationListener(this)
        splashimage.animation = alphaAnimation
        alphaAnimation.start()


    }

    override fun onPause() {
        super.onPause()
        Bungee.fade(this)

    }

    override fun onStop() {
        super.onStop()

    }
}
