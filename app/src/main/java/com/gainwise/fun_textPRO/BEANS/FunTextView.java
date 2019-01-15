package com.gainwise.fun_textPRO.BEANS;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class FunTextView extends LinearLayout {

    TextView textView;
    private int pxToAddForWidth = 0;

    public FunTextView(Context context, boolean defaultWrapping, int maxSize, int paddingSize) {
        super(context);
        this.setClipChildren(false);
        this.setClipToPadding(false);
        textView = new TextView(context);
        textView.setIncludeFontPadding(false);
        textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        this.setGravity(Gravity.CENTER);
        this.setOrientation(LinearLayout.VERTICAL);

        //

        if(defaultWrapping){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            this.setLayoutParams(params);
        }else{
            //paddingSize is 5-100 increments of 5, multiply for ratio reasons
            Log.i("FUNTEXT2321", ""+maxSize);
            Log.i("FUNTEXT2321", ""+paddingSize);
            pxToAddForWidth = Math.round(maxSize*paddingSize/100);
            Log.i("FUNTEXT2321", ""+pxToAddForWidth);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pxToAddForWidth,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            this.setLayoutParams(params);
        }

        this.addView(textView);

    }

    public void setTextView(EmojiconTextView textView) {
        this.textView = textView;
    }

    public int getPxToAddForWidth() {
        return pxToAddForWidth;
    }

    public void setPxToAddForWidth(int pxToAddForWidth) {
        this.pxToAddForWidth = pxToAddForWidth;
    }

    public TextView getTextView() {
        return textView;
    }
}
