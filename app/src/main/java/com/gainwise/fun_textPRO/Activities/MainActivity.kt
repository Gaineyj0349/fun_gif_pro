package com.gainwise.fun_textPRO.Activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.gainwise.fun_textPRO.BEANS.FunTextDirector
import com.gainwise.fun_textPRO.R
import com.gainwise.fun_textPRO.Utils.Utils
import com.gainwise.fun_textPRO.Utils.VPadapter
import com.gainwise.seed.Display.ScreenSizeHelper
import com.gainwise.seed.Vitals.AllPermissionsHelper
import com.gainwise.seed.Vitals.CrashAllocator
import com.gainwise.seed.Vitals.Crashable
import com.gainwise.seed.Vitals.PermissionsDirective
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import osmandroid.project_basics.Task
import spencerstudios.com.bungeelib.Bungee
import spencerstudios.com.fab_toast.FabToast


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{


    //this classwide pHandler variable will be used to actually call/check/retrieve permissions transactions and requests
    var pHandler = AllPermissionsHelper(PermissionHelper())

    lateinit var prefs: SharedPreferences
    lateinit var  prefsManager: OnSharedPreferenceChangeListener
    lateinit var funTextDirector: FunTextDirector
    lateinit var screenSizeHelper: ScreenSizeHelper
    lateinit var vpadapter: VPadapter
    var handler = Handler()

    var showRate = false





    override fun onCreate(savedInstanceState: Bundle?) {

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("FUNTEXT", "oncreate called")
        setSupportActionBar(toolbar)


        prefs = getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE)
        prefsManager = OnSharedPreferenceChangeListener() { prefs, key ->
            main_top_LL_inner.animate().alpha(0f).setDuration(1500).withEndAction({
                main_top_LL_inner.visibility = View.INVISIBLE
                createFunText()}).start()



            Log.i("FUNTEXT", "prefs called")
        }

        //sets the fragment_text params according to user's screen size

            matchLayoutSizesToUsersDevice()



         vpadapter = VPadapter(this, supportFragmentManager)
        main_viewpager.setAdapter(vpadapter)

        main_viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(main_viewpager))


        funTextDirector = FunTextDirector(this, Utils.getMaxHeightPipScreen(this))




        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        pHandler.handleResult(requestCode, permissions, grantResults)

        val member = Utils.getBetaMember(this)
        if(member){
            Utils.setBetaMember(this, true)
        }
    }

    //This method is the shot caller for the beginning of making text "fun"
    private fun createFunText() {


            Log.i("FUNTEXT", "createFunText called (shotcaller)")

            //fetching the arrayOfTVs


            val arrayOfTVs = funTextDirector.getArrayOfTVs(false)



            main_top_LL_inner.removeAllViews()

            setBGcolorsForView()

            //this will set the width of the linearlayout containing view
            val sizeLL = ArrayList<Int>()

            for (t in arrayOfTVs) {
                if (t.pxToAddForWidth > 0) {
                    sizeLL.add(t.pxToAddForWidth)
                } else {
                    t.measure(0, 0);
                    sizeLL.add(t.measuredWidth)
                }
                Log.i("FUNTEXT", "FunTVWidth = ${t.measuredWidth}")
                main_top_LL_inner.addView(t)

            }





            main_top_LL_inner.setLayoutParams(LinearLayout.LayoutParams(
                    sizeLL.sum(), Utils.getMaxHeightPipScreen(this)
            ));
            main_top_LL_inner.post {
                val width = main_top_LL_inner.getWidth()
                startFlow(width)


                Log.i("FUNTEXT", "innerViewWidth = $width")
            }



    }

    private fun setBGcolorsForView() {
        val color = Utils.getViewBGcolor(this);
        main_top_LL.setBackgroundColor(color)

    }

    //this method starts the flow animation. the width param is width of ll that text is in
    private fun startFlow(width: Int) {

        var fromXfloat = 0f
        var toXfloat = 0f
        val parentViewWidth = Utils.getMaxWidthFullScreen(this)


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
        main_top_LL_inner.startAnimation(translateAnimation)
        main_top_LL_inner.visibility = View.VISIBLE
        main_top_LL_inner.alpha = 1f

    }


    private fun matchLayoutSizesToUsersDevice() {

        //init the variable that will do the calculations, passing activity
        screenSizeHelper = ScreenSizeHelper(this)

        val orientation = this.resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            val params = main_top_LL.getLayoutParams()



            //use BigDecimal for accuracy in calculations
            val width = Utils.BD(screenSizeHelper.displayWidth)
            val height = Utils.BD(screenSizeHelper.displayHeight)

            //pip scroll preview height 100% converted to int
            val pip = ((width*width)/height).intValueExact()


            // Changes the height and width to the specified *pixels*
            params.height = pip
            Log.i("FUNTEXT", "\nheight: $height\nwidth: $width\npip: $pip")
            //write to sharedPrefs if necessary
            if(Utils.noDefaultScreenSizeSet(this)){
                Utils.setScreenParams(this, screenSizeHelper.displayHeight, screenSizeHelper.displayWidth, pip)
            }

            //apply these to pip scroll view
            main_top_LL.setLayoutParams(params)


        }
        else{

            val params = main_top_LL.getLayoutParams()

            //use BigDecimal for accuracy in calculations//also opposite to accomodate first run landscape settings writing to SP
            val height = Utils.BD(screenSizeHelper.displayHeight)
            val width = Utils.BD(screenSizeHelper.displayWidth)


            //pip scroll preview height 100% converted to int
            val pip = ((height*height)/width).intValueExact()

            //write to sharedPrefs if necessary
            if(Utils.noDefaultScreenSizeSet(this)){

                Utils.setScreenParams(this, screenSizeHelper.displayWidth, screenSizeHelper.displayHeight, pip)
                recreate()
            }

            if(Utils.autoRotate(this)){
                val intent = Intent(this@MainActivity, FullscreenActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }else{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }

            // Changes the height and width to the specified *pixels*
            params.height = pip
            //apply these to pip scroll view
            main_top_LL.setLayoutParams(params)

        }


    }





    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

//            R.id.information -> {
//
//                startActivity(Intent(this@MainActivity, InformationAct::class.java))
//
//            }
            R.id.settings -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))

            }
            R.id.nav_share -> {
            Task.ShareApp(this, "com.gainwise.fun_textPRO","FUN.gif", "Create Custom GIFS and FUN Scrolling Marquee!")
            }
            R.id.nav_rate -> {
              Task.RateApp(this,"com.gainwise.fun_textPRO" )
            }

//            R.id.test -> {
//              // android.os.Process.killProcess(android.os.Process.myPid());
//            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onResume() {
        super.onResume()
        main_top_LL_inner.animate().alpha(0f).setDuration(1500).withEndAction({
            main_top_LL_inner.visibility = View.INVISIBLE
            createFunText()}).start()
        if(Utils.getShowAd(this)){

            Utils.setShowAd(this,false)
        }


        prefs.registerOnSharedPreferenceChangeListener(prefsManager);
        //call the method to set the text properties



    }

    override fun onPause() {
        super.onPause()
        main_top_LL_inner.removeAllViews()
        Log.i("FUNTEXT", "MainAct on Pause")
        prefs.unregisterOnSharedPreferenceChangeListener(prefsManager)
        if(!showRate){
            Bungee.diagonal(this)
        }

    }

    override fun onStop() {
        super.onStop()
        Log.i("FUNTEXT", "MainAct on Stop")

    }
     fun showPopUp(v: View) {
         //TODO make permissions
         if(pHandler.needPermissions(pHandler.permissionsDirective.permissionsToRequest())){
             pHandler.requestPermissions();
         }else{
             pHandler.permissionsDirective.executeOnPermissionGranted();
         }


    }

    fun permissionDenied(){
        FabToast.makeText(this, "Permission is needed to save the GIF!",
                FabToast.LENGTH_LONG, FabToast.ERROR, FabToast.POSITION_DEFAULT).show()
    }
    fun permissionGranted(){
        val popup = PopupMenu(this, playfab)
        popup.menuInflater.inflate(R.menu.playfab_menu, popup.menu)



        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId){
                    R.id.playfab1->{
                        if(Utils.showEmojiHint(this@MainActivity)){
                            showDialogEmoji()
                        }else{
                            startActivity(Intent(this@MainActivity, GifSaveActivity::class.java))

                        }
                    }
                    R.id.playfab2->{
                        if(Utils.autoRotate(this@MainActivity)){

                            FabToast.makeText(this@MainActivity, "Turn your device landscape for Full Screen!!",FabToast.LENGTH_LONG,
                                    FabToast.INFORMATION,
                                    FabToast.POSITION_DEFAULT).show()

                        }else{
                            startActivity(Intent(this@MainActivity, FullscreenActivity::class.java))                        }


                    }
                    R.id.playfab3->{
                        Utils.defaults(this@MainActivity)
                        recreate()
                    }
                }
                return true
            }
        })

        popup.show()//showing popup menu
    }

    private fun showDialogEmoji() {
        val dialog = Dialog(this)
        val view = layoutInflater.inflate(R.layout.emoji_dialog, null)
        dialog.setContentView(view)
        var never = view.findViewById<Button>(R.id.emoji_donotshow);
        var ok = view.findViewById<Button>(R.id.emoji_ok);

        never.setOnClickListener({
           Utils.setShowEmojiHint(this@MainActivity, false)
            startActivity(Intent(this@MainActivity, GifSaveActivity::class.java))
            dialog.dismiss()

        })
        ok.setOnClickListener({
            startActivity(Intent(this@MainActivity, GifSaveActivity::class.java))
            dialog.dismiss()
        })

        dialog.show()
    }

    inner class PermissionHelper : PermissionsDirective {
       override val activity: Activity
           get() = Outer@ this@MainActivity
       override val requestCode: Int
           get() = 15

       override fun executeOnPermissionDenied() {
           permissionDenied()
       }

       override fun executeOnPermissionGranted() {
          permissionGranted()
       }

       override fun permissionsToRequest(): Array<String?> {
           return arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
       }
   }


    override fun onConfigurationChanged(newConfig: Configuration) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {

       //     Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
            if(Utils.autoRotate(this)){
                val intent = Intent(this@MainActivity, FullscreenActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }else{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }

        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
         //   Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
        }
    }

    internal var double_backpressed = false
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {


            var prefs = getSharedPreferences("AUTO_PREF", Context.MODE_PRIVATE)
            var show = prefs.getBoolean("showRateDialog", true)

            if (show) {
                showRate = true
                var dialog = Dialog(Home@ this)
                var view = layoutInflater.inflate(R.layout.rate_buy_exit, null)
                var llrate = view.findViewById<LinearLayout>(R.id.llrate);
                var llnever = view.findViewById<LinearLayout>(R.id.llnever);
                var lllater = view.findViewById<LinearLayout>(R.id.lllater);

                llrate.setOnClickListener({
                    Task.RateApp(Home@ this, "com.gainwise.fun_textPRO")
                    val editor = getSharedPreferences("AUTO_PREF", MODE_PRIVATE).edit()
                    editor.putBoolean("showRateDialog", false)
                    editor.apply()
                })
                llnever.setOnClickListener({
                    val editor = getSharedPreferences("AUTO_PREF", MODE_PRIVATE).edit()
                    editor.putBoolean("showRateDialog", false)
                    editor.apply()
                    dialog.dismiss()
                    super.onBackPressed()
                })
                lllater.setOnClickListener({
                    dialog.dismiss()
                    super.onBackPressed()

                })
                dialog.setContentView(view)
                dialog.show()

            } else {


                if (double_backpressed) {
                    super.onBackPressed()
                    return
                }
                this.double_backpressed = true
                FabToast.makeText(Home@ this,
                        "Click back again to exit.", Toast.LENGTH_SHORT, FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show()

                Handler().postDelayed({ double_backpressed = false }, 2000)
            }
        }
    }

    val crashable = object : Crashable{
        override fun executeOnCrash() {
            //blank for now, but will capture crash data
        }

    }
    val allocator = CrashAllocator(crashable , MainActivity@this , "DEFAULTS")

}

