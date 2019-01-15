@file:JvmName("ConvUtil")
package com.gainwise.fun_textPRO.Utils

import android.graphics.Typeface

fun getTextSizeWithPercentAndMax(percent: Int, maxInPX: Int): Float{
    return ((percent * .01)*maxInPX).toFloat();
}

fun returnTypeOfStyle(numForBoldOrItalic: Int): Int{
   return when(numForBoldOrItalic){
        1 -> Typeface.ITALIC
        2 -> Typeface.BOLD
        3 -> Typeface.BOLD_ITALIC
        else -> 0
    }
}