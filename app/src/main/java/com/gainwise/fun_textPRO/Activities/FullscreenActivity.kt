package com.gainwise.fun_textPRO.Activities

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import com.gainwise.fun_textPRO.BEANS.FunTextDirector
import com.gainwise.fun_textPRO.R
import com.gainwise.fun_textPRO.Utils.Utils
import kotlinx.android.synthetic.main.activity_fullscreen.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 *
 */
class FullscreenActivity : AppCompatActivity(), Animation.AnimationListener {
    override fun onAnimationRepeat(p0: Animation?) {
    }

    override fun onAnimationEnd(p0: Animation?) {
    }

    override fun onAnimationStart(p0: Animation?) {

    }

    private val mHideHandler = Handler()
    lateinit var funTextDirector: FunTextDirector
    var forcedLand = false
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreen_parent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        fullscreen_content_controls.visibility = View.VISIBLE
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)

        setBGcolorsForView()
        val orientation = this.resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            forcedLand = true
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }


            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        funTextDirector = FunTextDirector(this, Utils.getMaxWidthFullScreen(this))
        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreen_parent.setOnClickListener { toggle() }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
      //  dummy_button.setOnTouchListener(mDelayHideTouchListener)
        //call the method to set the text properties
        createFunText()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(50)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        fullscreen_content_controls.visibility = View.GONE
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        fullscreen_parent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 1500

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }

    //This method is the shot caller for the beginning of making text "fun"
    private fun createFunText() {
        fullscreen_inner.setVisibility(View.INVISIBLE)

        Log.i("ScrollingText", "createFunText called (shotcaller)")

        //fetching the arrayOfTVs


        val arrayOfTVs = funTextDirector.getArrayOfTVs(false)

        fullscreen_inner.removeAllViews()


        //this will set the width of the linearlayout containing view
        val sizeLL = ArrayList<Int>()

        for (t in arrayOfTVs){
            if(t.pxToAddForWidth>0){
                sizeLL.add(t.pxToAddForWidth)
            }else{
                t.measure(0,0);
                sizeLL.add(t.measuredWidth)
            }

            Log.i("FUNTEXT", "FunTVWidth = ${t.measuredWidth}")
            fullscreen_inner.addView(t)
        }



        fullscreen_inner.setLayoutParams(LinearLayout.LayoutParams(
                sizeLL.sum(), Utils.getMaxWidthFullScreen(this)
        ));
        fullscreen_inner.post{
            val width = fullscreen_inner.getWidth()
            startFlow(width)


            Log.i("FUNTEXT", "innerViewWidth = $width") }



    }

    //this method starts the flow animation. the width param is width of ll that text is in
    private fun startFlow(width: Int) {

        var fromXfloat = 0f
        var toXfloat = 0f
        val parentViewWidth = Utils.getMaxHeightFullScreen(this)

        //TODO get from sharedprefs for these settings
        val rightToLeftFlow = Utils.getGlobalFlowDirection(this)
        val speed = Utils.getGlobalFlowSpeed(this)*250

        if(rightToLeftFlow){
            fromXfloat = ((parentViewWidth*.5) + (width*.5)).toFloat()
            toXfloat = -fromXfloat
            Log.i("FUNTEXT", "FromX $fromXfloat , toX $toXfloat")
        }else{
            fromXfloat = -(((parentViewWidth*.5) + (width*.5)).toFloat())
            toXfloat = -(fromXfloat)
            Log.i("FUNTEXT", "FromX $fromXfloat , toX $toXfloat")
        }




        val translateAnimation = TranslateAnimation(
                Animation.ABSOLUTE, fromXfloat,
                Animation.ABSOLUTE, toXfloat,
                Animation.ABSOLUTE,0f,
                Animation.ABSOLUTE,0f)

        // translateAnimation.interpolator = AccelerateDecelerateInterpolator()
        translateAnimation.duration = speed.toLong()
        translateAnimation.repeatCount = Animation.INFINITE
        translateAnimation.setAnimationListener(this)
        fullscreen_inner.startAnimation(translateAnimation)
        fullscreen_inner.setVisibility(View.VISIBLE)
        fullscreen_inner.alpha = 1f

    }
    private fun setBGcolorsForView() {
        val color = Utils.getViewBGcolor(this);
        fullscreen_parent.setBackgroundColor(color)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
          //  Toast.makeText(this, "land", Toast.LENGTH_SHORT).show()


        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
        //    Toast.makeText(this, "por", Toast.LENGTH_SHORT).show()
            if(Utils.autoRotate(this)){
                finish()

            }else{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            // do something here, such as start an Intent to the parent activity.

            val orientation = this.resources.configuration.orientation

            if(Utils.autoRotate(this)){
//
//                    FabToast.makeText(this, "Turn your device portrait to return!",FabToast.LENGTH_LONG,
//                            FabToast.INFORMATION,
//                            FabToast.POSITION_TOP).show()

            }else{

                finish()
            }



        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        this.finish()
    }

    fun restartFlow(v: View){
        hide()
        fullscreen_inner.animate().alpha(0f).setDuration(1500).withEndAction({
            fullscreen_inner.visibility = View.INVISIBLE
                    createFunText()}).start()
        fullscreen_inner.setVisibility(View.INVISIBLE)


    }

    override fun onBackPressed() {
        super.onBackPressed()
//        FabToast.makeText(this, "Turn your device portrait to return!",FabToast.LENGTH_LONG,
//                FabToast.INFORMATION,
//                FabToast.POSITION_TOP).show()
    }

}
