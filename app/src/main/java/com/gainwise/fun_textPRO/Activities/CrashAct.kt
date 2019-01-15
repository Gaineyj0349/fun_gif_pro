package com.gainwise.fun_textPRO.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.gainwise.fun_textPRO.R
import com.gainwise.fun_textPRO.Utils.Utils
import com.gainwise.fun_textPRO.Utils.getTypeFaceWithIndex
import kotlinx.android.synthetic.main.activity_crash.*
import kotlinx.android.synthetic.main.content_crash.*
import spencerstudios.com.bungeelib.Bungee
import spencerstudios.com.fab_toast.FabToast


class CrashAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Utils.setCrash(this, false);
        val crash = Utils.getCrashReport(this)
        Utils.defaults(this)

        val stringBuilder = StringBuilder()
        stringBuilder.append("OH NO! There was a crash. App defaults have been restored! " +
                "Below is what we know! If you tap the email button you can send this to the developer so we can " +
                "get a fix for this issue!!\n\n")
        stringBuilder.append(crash)
        crashTextView.setText(stringBuilder.toString())
        crashTextView.typeface = getTypeFaceWithIndex(this, 10)
        fab_email.setOnClickListener { view ->
           FabToast.makeText(this, "Thank you so much for your time!", FabToast.LENGTH_LONG, FabToast.INFORMATION
           ,FabToast.POSITION_DEFAULT).show()
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:") // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("realgainwise@gmail.com"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "FUN.gif crash report")
                intent.putExtra(Intent.EXTRA_TEXT, crash);
                startActivity(intent)
                finish()
            } catch (ex: android.content.ActivityNotFoundException) {
                FabToast.makeText(this, "There are no email client installed on your device.", FabToast.LENGTH_LONG, FabToast.ERROR
                        ,FabToast.POSITION_DEFAULT).show()
            }

        }
    }

    override fun onPause() {
        super.onPause()
        Bungee.card(this)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            startActivity(Intent(this,splash::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
        }
}
