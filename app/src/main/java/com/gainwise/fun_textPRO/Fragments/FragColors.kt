package com.gainwise.fun_textPRO.Fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.arunsoft.colorpickerlib.ColorPickerDialog
import com.gainwise.fun_textPRO.R
import com.gainwise.fun_textPRO.Utils.Utils
import com.gainwise.fun_textPRO.Utils.Utils.setGlobalTextColor
import com.gainwise.fun_textPRO.Utils.Utils.setViewBGcolor
import kotlinx.android.synthetic.main.fragment_colors.*


class FragColors : Fragment() {

    internal var view: View? = null

    var bgColorLL: LinearLayout? = null
    var textColorLL: LinearLayout? = null
    var bgColorLLtoChange: LinearLayout? = null
    var textColorLLtoChange: LinearLayout? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(view == null){
            view = inflater.inflate(R.layout.fragment_colors, container, false)
        }

        bgColorLL = view?.findViewById(R.id.frag_color_bgcolor_card) as LinearLayout
        textColorLL = view?.findViewById(R.id.frag_color_textcolor_card) as LinearLayout
        bgColorLLtoChange = view?.findViewById(R.id.frag_color_bgcolor_card_LL_to_change) as LinearLayout
        textColorLLtoChange = view?.findViewById(R.id.frag_color_textcolor_card_LL_to_change) as LinearLayout


        bgColorLL?.setOnClickListener{launchBGcolorDialog()}
        textColorLL?.setOnClickListener{launchTextColorDialog()}
        return view

    }

    private fun launchTextColorDialog() {
        ColorPickerDialog.Builder(context)
                //.setCurrentColor(InitialOutfit.BGcolor())
                .setTitle("Text Color")
                .setShowAlphaBar(true)
                .setListener(object : ColorPickerDialog.OnColorSelectedListener {
            override fun onColorSelected(color: Int) {
                setGlobalTextColor(activity, color)
                textColorLLtoChange?.setBackgroundColor(color)
                frag_colors_tv2.text = color.toString()
                if(color == Color.BLACK){
                    frag_colors_tv2.setTextColor(Color.WHITE)
                }
                if(color == Color.WHITE){
                    frag_colors_tv2.setTextColor(Color.BLACK)
                }
            }

            override fun onCancelled() {

            }
        }).show()
    }

    private fun launchBGcolorDialog() {
        ColorPickerDialog.Builder(context)
                .setCurrentColor(Utils.getViewBGcolor(activity))
                .setTitle("Background Color")
                .setShowAlphaBar(true)
                .setListener(object : ColorPickerDialog.OnColorSelectedListener {
                    override fun onColorSelected(color: Int) {
                        setViewBGcolor(activity, color)
                        bgColorLLtoChange?.setBackgroundColor(color)
                        frag_colors_tv1.text = color.toString()
                        if(color == Color.BLACK){
                            frag_colors_tv1.setTextColor(Color.WHITE)
                        }
                        if(color == Color.WHITE){
                            frag_colors_tv1.setTextColor(Color.BLACK)
                        }
                    }

                    override fun onCancelled() {

                    }
                }).show()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {

         //updateBGcolors()


        }
    }

    override fun onResume() {
        super.onResume()
        updateBGcolors()
    }

    private fun updateBGcolors() {
        bgColorLLtoChange?.setBackgroundColor(Utils.getViewBGcolor(activity))
        textColorLLtoChange?.setBackgroundColor(Utils.getGlobalTextColor(activity))
        frag_colors_tv1.text = Utils.getViewBGcolor(activity).toString()
        frag_colors_tv2.text = Utils.getGlobalTextColor(activity).toString()
        if(Utils.getViewBGcolor(activity) == Color.BLACK){
           frag_colors_tv1.setTextColor(Color.WHITE)

        }
        if(Utils.getGlobalTextColor(activity) == Color.BLACK){
            frag_colors_tv2.setTextColor(Color.WHITE)
        }
        if(Utils.getViewBGcolor(activity) == Color.WHITE){
            frag_colors_tv1.setTextColor(Color.BLACK)
        }
        if(Utils.getGlobalTextColor(activity) == Color.WHITE){
            frag_colors_tv2.setTextColor(Color.BLACK)
        }
    }
}