package com.gainwise.fun_textPRO.Fragments

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.gainwise.fun_textPRO.BEANS.SpeedSeekbarListener
import com.gainwise.fun_textPRO.R
import com.gainwise.fun_textPRO.Utils.Utils
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText
import io.ghyeok.stickyswitch.widget.StickySwitch
import kotlinx.android.synthetic.main.fragment_text.*
import org.jetbrains.annotations.NotNull


class FragText : Fragment() {

    internal var view: View? = null
     var ET: EmojiconEditText? = null

    var stickySwitch: StickySwitch? = null
    var seekbar: SeekBar? = null
    var handler = Handler()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        if(view == null){
            view = inflater.inflate(R.layout.fragment_text, container, false)

        }
        ET = view?.findViewById<EmojiconEditText>(R.id.fragment_text_edittext)
        stickySwitch = view?.findViewById(R.id.frag_flow_direction_switch) as StickySwitch
        seekbar = view?.findViewById(R.id.fragment_flow_speed_seekbar) as SeekBar

        setDirectionFlowAndSpeed()


        ET?.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               Log.i("FUNTEXT", "before text changed called")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i("FUNTEXT", "afterText changed called")
                //sequence runnables to control a stillTyping boolean to not overload functions until finished typing
                handler.postDelayed({saveNewMessage(p0.toString())},1000)
                handler.postDelayed({Utils.setStillTyping(activity, false)},650)
                Utils.setStillTyping(activity, true)
                Log.i("FUNTEXT", "onChange: ${p0.toString()}")
            }

        })





        return view
    }



    override fun onResume() {
        super.onResume()
        ET?.setText(Utils.SPgetMessageText(activity))
        val emojIcon = EmojIconActions(activity, view, ET!! , frag_text_emoji)
        emojIcon.ShowEmojIcon()
        emojIcon.setKeyboardListener(object : EmojIconActions.KeyboardListener {
            override fun onKeyboardOpen() {
                if(frag_text_emoji != null){
                    frag_text_emoji.setBackgroundColor(ContextCompat.getColor(context!!,R.color.colorAccent))
                }
                Log.i("FUNTEXT", "open")
            }

            override fun onKeyboardClose() {
                if(frag_text_emoji != null){
                    frag_text_emoji.setBackgroundColor(ContextCompat.getColor(context!!,R.color.colorPrimaryDark))
                }

                Log.i("FUNTEXT", "close")
            }
        })
        frag_text_clear.setOnClickListener { ET?.setText("") }

        stickySwitch?.onSelectedChangeListener = object : StickySwitch.OnSelectedChangeListener {
            override fun onSelectedChange(@NotNull direction: StickySwitch.Direction, @NotNull text: String) {
                if (direction.name.equals("LEFT")) {
                    Utils.setGlobalFlowDirection(activity, true)
                } else {
                    Utils.setGlobalFlowDirection(activity, false)
                }
            }
        }

        seekbar?.setOnSeekBarChangeListener(SpeedSeekbarListener(activity))
        stickySwitch?.textVisibility = StickySwitch.TextVisibility.GONE;
    }



    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
            Log.i("FUNTEXT", "FRAGFLOW isvisible")
            if(stickySwitch == null){

            }else {
                setDirectionFlowAndSpeed()
            }
        }
    }

    private fun setDirectionFlowAndSpeed() {
        if(Utils.getGlobalFlowDirection(activity)){
            stickySwitch?.setDirection(StickySwitch.Direction.LEFT);
        }else{
            stickySwitch?.setDirection(StickySwitch.Direction.RIGHT);
        }
        seekbar?.setProgress(Utils.getGlobalFlowSpeed(activity))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun saveNewMessage(message: String){
        if(!Utils.stillTyping(activity)){
            Utils.SPsetMessageText(activity, message)
        }
    }

}