package com.gainwise.fun_textPRO.BEANS;

import android.content.Context;
import android.widget.SeekBar;

import com.gainwise.fun_textPRO.Utils.Utils;

public class SpeedSeekbarListener implements SeekBar.OnSeekBarChangeListener {

    Context context;

    public SpeedSeekbarListener(Context context) {
        this.context = context;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        Utils.setGlobalFlowSpeed(context, i+1);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
