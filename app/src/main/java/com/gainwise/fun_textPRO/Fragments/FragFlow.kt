package com.gainwise.fun_textPRO.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gainwise.fun_textPRO.R


class FragFlow : Fragment() {

    internal var view: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i("FUNTEXT", "FRAGFLOW oncreateview")
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_flow, container, false)
        }



        return view
    }

    override fun onResume() {
        Log.i("FUNTEXT", "FRAGFLOW onresume")
        super.onResume()

    }


}
