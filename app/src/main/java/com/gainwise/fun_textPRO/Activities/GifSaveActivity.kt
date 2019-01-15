package com.gainwise.fun_textPRO.Activities

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.gainwise.fun_textPRO.BEANS.FunTextDirector
import com.gainwise.fun_textPRO.BEANS.GifSync
import com.gainwise.fun_textPRO.R
import com.gainwise.fun_textPRO.Utils.ContextAndList
import com.gainwise.fun_textPRO.Utils.ScreenShot
import com.gainwise.fun_textPRO.Utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_gif_save.*
import spencerstudios.com.fab_toast.FabToast

class GifSaveActivity : AppCompatActivity(), Animation.AnimationListener{

    lateinit var translateAnimation: TranslateAnimation

    var needToAddWaterMark = false
    override fun onAnimationRepeat(p0: Animation?) {

    }

    override fun onAnimationStart(p0: Animation?) {

    }

    override fun onAnimationEnd(p0: Animation?) {
        if(p0 == translateAnimation){
            gif_save_main_top_LL_inner.visibility = View.GONE
            //offset maxcount if finished
            if(counter< maxCount){
                maxCount = counter+2
            }

            Log.i("FUNTEXT", "ended")
        }

    }

    lateinit var funTextDirector: FunTextDirector
    var speed = 0
    var maxCount = 0
    var actIsVisible = true
    var counter = 0;
    var timeBetweenSnaps = 0L
    lateinit var list: ArrayList<Bitmap>
    var handler = Handler()
    var  waterMarkSize = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif_save)


        //TODO, create and fetch needToAddWaterMark boolearn from SP

        speed = Utils.getGlobalFlowSpeed(this)*250
        val framesPerSecond = Utils.getGIFsaveFrameRate(this)
        maxCount = (speed*.001*framesPerSecond*.83).toInt()
        timeBetweenSnaps = (speed/maxCount).toLong()
        Log.i("FUNTEXT", "maxCount is " + maxCount + "\n" +
                "speed is " + speed + "\n" +
                "timebetweensnaps is " + timeBetweenSnaps + "\n" )

        actIsVisible = true
        val maxPipHeight = Utils.getMaxHeightPipScreen(this)
         waterMarkSize = (.2*maxPipHeight).toFloat()

        counter = 0;
        list = ArrayList<Bitmap>()
        matchLayoutSizesToUsersDevice()
        funTextDirector = FunTextDirector(this, maxPipHeight)

    }

    private fun matchLayoutSizesToUsersDevice() {

        val params = gif_save_main_top_LL.getLayoutParams()
        val params3 = gif_save_main_top_RL.getLayoutParams()
        //pip scroll preview height 100% converted to int
        val pip = Utils.getMaxHeightPipScreen(this)


        // Changes the height and width to the specified *pixels*
        params.height = pip
        params3.height = pip
        //apply these to pip scroll view
        gif_save_main_top_LL.setLayoutParams(params)
        gif_save_main_top_RL.setLayoutParams(params3)

    }



    //This method is the shot caller for the beginning of making text "fun"
    private fun createFunText() {

        Log.i("ScrollingText", "createFunText called (shotcaller)")

        //fetching the arrayOfTVs


        val arrayOfTVs = funTextDirector.getArrayOfTVs(true)

        gif_save_main_top_LL_inner.removeAllViews()

        setBGcolorsForView()

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
            gif_save_main_top_LL_inner.addView(t)
        }
        gif_save_main_top_LL.removeAllViews()
        gif_save_main_top_LL.addView(gif_save_main_top_LL_inner)

        if(needToAddWaterMark){

            // Initialize a new ImageView widget
            val iv = AppCompatImageView(this)

            // Set an image for ImageView
         //   iv.setImageDrawable(drawable(R.drawable.animal))
            Picasso.get().load(R.mipmap.icon).into(iv);
            // Create layout parameters for ImageView
            val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)

            // Add rule to layout parameters
            // Add the ImageView below to Button
            lp.addRule(RelativeLayout.ALIGN_PARENT_START)
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            lp.setMargins(0,0,0,0)
            // Add layout parameters to ImageView
            iv.layoutParams = lp



            // Finally, add the ImageView to layout
            gif_save_main_top_RL.addView(iv)



        }


        gif_save_main_top_LL_inner.setLayoutParams(LinearLayout.LayoutParams(
                sizeLL.sum(), Utils.getMaxHeightPipScreen(this)
        ));
        gif_save_main_top_LL_inner.post{
            val width = gif_save_main_top_LL_inner.getWidth()
            startFlow(width)


            Log.i("FUNTEXT", "innerViewWidth = $width") }



    }

    override fun onStart() {
        super.onStart()
        createFunText()


    }

    private fun setBGcolorsForView() {
        val color = Utils.getViewBGcolor(this);
        gif_save_main_top_LL.setBackgroundColor(color)

    }

    //this method starts the flow animation. the width param is width of ll that text is in
    private fun startFlow(width: Int) {

        var fromXfloat = 0f
        var toXfloat = 0f
        val parentViewWidth = Utils.getMaxWidthFullScreen(this)

        //TODO get from sharedprefs for these settings
        val rightToLeftFlow = Utils.getGlobalFlowDirection(this)


        if(rightToLeftFlow){
            fromXfloat = ((parentViewWidth*.5) + (width*.5)).toFloat()
            toXfloat = -fromXfloat
            Log.i("FUNTEXT", "FromX $fromXfloat , toX $toXfloat")
        }else{
            fromXfloat = -(((parentViewWidth*.5) + (width*.5)).toFloat())
            toXfloat = -(fromXfloat)
            Log.i("FUNTEXT", "FromX $fromXfloat , toX $toXfloat")
        }




         translateAnimation = TranslateAnimation(
                Animation.ABSOLUTE, fromXfloat,
                Animation.ABSOLUTE, toXfloat,
                Animation.ABSOLUTE,0f,
                Animation.ABSOLUTE,0f)


        translateAnimation.duration = speed.toLong()
        translateAnimation.setAnimationListener(this)
        gif_save_main_top_LL_inner.startAnimation(translateAnimation)
        takeSnapForGif()

    }

    fun saveGif(){

            Log.i("FUNTEXT", "list size" + list.size)
            try{
             GifSync().execute(ContextAndList(this,list))
                Utils.setShowAd(this, true)
            }catch(e: Exception){
                Log.i("FUNTEXT", e.message)
            }

       
    }
    
    fun takeSnapForGif(){
        if(actIsVisible){
            
            Log.i("FUNTEXT", "takeSnapCalled")


            if(counter< maxCount){
                ++counter
                handler.postDelayed({

                    list.add(ScreenShot.takeScreenShot(gif_save_main_top_RL))
                    takeSnapForGif()
                }, timeBetweenSnaps)
            }else{
                saveGif()
                FabToast.makeText(this, "Success, your GIF is processing!!",
                        FabToast.LENGTH_LONG, FabToast.SUCCESS, FabToast.POSITION_DEFAULT).show()
                finish()
            }

       
        }else{
            this.finish()
        }
       
    }

    override fun onPause() {
        super.onPause()
        actIsVisible = false
    }



}






