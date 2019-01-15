package com.gainwise.fun_textPRO.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gainwise.fun_textPRO.R
import com.gainwise.fun_textPRO.Utils.Utils
import kotlinx.android.synthetic.main.fragment_funify.*


class FragFunify : Fragment() {

    internal var view: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(view == null){
            view = inflater.inflate(R.layout.fragment_funify, container, false)
        }


        return view

    }

    override fun onResume() {
        super.onResume()
        frag_funify_randomize_colors_card.setOnClickListener{Utils.randomizeAllColors(activity)}
        frag_funify_randomize_size_card.setOnClickListener{Utils.randomizeTextSize(activity)}

    }
}