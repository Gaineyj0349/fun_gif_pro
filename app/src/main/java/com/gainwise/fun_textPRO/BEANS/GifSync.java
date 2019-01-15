package com.gainwise.fun_textPRO.BEANS;

import android.os.AsyncTask;

import com.gainwise.fun_textPRO.Utils.ContextAndList;
import com.gainwise.fun_textPRO.Utils.ScreenShot;

public class GifSync extends AsyncTask<ContextAndList, Void, Void> {
    @Override
    protected Void doInBackground(ContextAndList... contextAndLists) {
        ScreenShot.Companion.placeFileInDirectory(contextAndLists[0].getContext(),contextAndLists[0].getList());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
