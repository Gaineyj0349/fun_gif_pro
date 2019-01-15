package com.gainwise.fun_textPRO.Fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.PopupMenu
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.gainwise.fun_textPRO.R
import com.gainwise.fun_textPRO.Utils.Utils
import com.gainwise.fun_textPRO.Utils.getIndexOfTypeFace
import com.gainwise.fun_textPRO.Utils.getStringOfTypeFaceWithIndex
import com.gainwise.fun_textPRO.Utils.getTypeFaceWithIndex
import kotlinx.android.synthetic.main.fragment_font.*
import spencerstudios.com.fab_toast.FabToast


class FragFont : Fragment() {

    internal var view: View? = null
    var bold_box: CheckBox? = null
    var italic_box: CheckBox? = null
    var TVsize: TextView? = null
    var TVpadd: TextView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(view == null){
            view = inflater.inflate(R.layout.fragment_font, container, false)
        }
        TVsize = view?.findViewById<TextView>(R.id.fragment_text_tv_size)
        TVpadd = view?.findViewById<TextView>(R.id.fragment_text_tv_padd)
        bold_box = view?.findViewById<CheckBox>(R.id.frag_font_checkbox_bold)
        italic_box = view?.findViewById<CheckBox>(R.id.frag_font_checkbox_italic)

        val textSizeUp = view?.findViewById<FloatingActionButton>(R.id.fragment_text_tv_size_arrow_right)
        val textSizeDown = view?.findViewById<FloatingActionButton>(R.id.fragment_text_tv_size_arrow_left)


        val paddSizeUp = view?.findViewById<FloatingActionButton>(R.id.fragment_text_tv_padd_arrow_right)
        val paddSizeDown = view?.findViewById<FloatingActionButton>(R.id.fragment_text_tv_padding_arrow_left)

        textSizeUp?.setOnClickListener{increaseTextSize()}
        textSizeDown?.setOnClickListener{decreaseTextSize()}

        paddSizeUp?.setOnClickListener{increasePaddSize()}
        paddSizeDown?.setOnClickListener{decreasePaddSize()}

        return view
    }



    private fun showPopUp() {
        val popup = PopupMenu(context!!, frag_font_tap_change)
        popup.menuInflater.inflate(R.menu.font_menu, popup.menu)

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                Log.i("FUNTEXTfont", "title of picked font is ${item.title.toString()}")

                Utils.setGlobalTypeFaceInt(activity, getIndexOfTypeFace(item.title.toString()))

                updateShowFont()
                return true
            }
        })

        popup.show()//showing popup menu

    }

    private fun boldBoxClicked() {
        if(bold_box!!.isChecked){
            Utils.setGlobalBold(activity, true)
        }else{
            Utils.setGlobalBold(activity,false)
        }
    }
    private fun italicBoxClicked() {

        if(italic_box!!.isChecked){
            Utils.setGlobalItalic(activity, true)
        }else{
            Utils.setGlobalItalic(activity,false)
        }
    }

    override fun onResume() {
        super.onResume()
        bold_box?.setOnClickListener{boldBoxClicked()}
        italic_box?.setOnClickListener{italicBoxClicked()}

        frag_font_tap_change.setOnClickListener{showPopUp()}
        setTVP(Utils.SPgetMessageTextSizePercentage(activity))
        setTVPadd(Utils.SPgetPaddingForFT(activity))
        updateShowFont()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {

                setCheckBoxes()


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setCheckBoxes() {
        Log.i("FUNTEXT", "boldnum = ${Utils.getGlobalBold(activity)}")
        if(Utils.getGlobalBold(activity) == 0){
            bold_box?.isChecked = false
        }else{
            bold_box?. isChecked = true

        }
        Log.i("FUNTEXT", "italicnum = ${Utils.getGlobalItalic(activity)}")
        if(Utils.getGlobalItalic(activity) == 0){
            italic_box?.isChecked = false
        }else{
            italic_box?. isChecked = true
        }
    }


    private fun increaseTextSize(){
        var current = Utils.SPgetMessageTextSizePercentage(activity)
        current += 5
        if(current<101){
            Utils.SPsetMessageTextSizePercentage(activity, current)
            setTVP(current)
        }else(FabToast.makeText
        (activity,"Maximum Reached!", FabToast.LENGTH_LONG, FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show())
    }
    private fun decreaseTextSize() {
        var current = Utils.SPgetMessageTextSizePercentage(activity)
        current -= 5
        if(current>0){
            Utils.SPsetMessageTextSizePercentage(activity, current)
            Log.i("FUNTEXT", "current: $current")
            setTVP(current)
        }else(FabToast.makeText
        (activity,"Minimum Reached!", FabToast.LENGTH_LONG, FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show())
    }

    //sets textview percentage
    fun setTVP(current: Int){
        TVsize!!.setText("$current%")

    }
    fun setTVPadd(current: Int){
        TVpadd!!.setText("$current%")

    }

    fun updateShowFont(){
        val index = Utils.getGlobalTypeFaceInt(context)
        val typeface = getTypeFaceWithIndex(context!!, index)
        frag_font_tv_for_font.text = getStringOfTypeFaceWithIndex(index)
        frag_font_tv_for_font.typeface = typeface
    }

    private fun increasePaddSize(){
        var current = Utils.SPgetPaddingForFT(activity)
        current += 5
        if(current<101){
            Utils.SPsetPaddingForFT(activity, current)
            setTVPadd(current)
        }else(FabToast.makeText
        (activity,"Maximum Reached!", FabToast.LENGTH_LONG, FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show())
    }
    private fun decreasePaddSize() {
        var current = Utils.SPgetPaddingForFT(activity)
        current -= 5
        if(current>-1){
            Utils.SPsetPaddingForFT(activity, current)
            Log.i("FUNTEXT", "current: $current")
            setTVPadd(current)
        }else(FabToast.makeText
        (activity,"Minimum Reached!", FabToast.LENGTH_LONG, FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show())
    }

}