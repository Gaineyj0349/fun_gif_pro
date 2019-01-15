package com.gainwise.fun_textPRO.BEANS;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class Outfit {

    //the index of the Stringacter in the message
    @SerializedName("in")
    private int indexOfFunLetter;

    //the Stringacter in the message that scrolls
    @SerializedName("ch")
    private String funCharacter;

    //percentage of size relative to the max. 100% = max size
    @SerializedName("si")
    private int sizePercent;

    //number of px to subtract from layoutparams in FT construction
    @SerializedName("pn")
    private int paddingNumber;


    //style of letter 0 = normal, 1 = italic only , 2 = bold only, 3 = bold and italic
    @SerializedName("st")
    private int styleBoldAndItalic;

    //text color
    @SerializedName("tc")
    private int textColor;

    public int getIndexOfFunLetter() {
        return indexOfFunLetter;
    }

    public void setIndexOfFunLetter(int indexOfFunLetter) {
        this.indexOfFunLetter = indexOfFunLetter;
    }

    public String getFunCharacter() {
        return funCharacter;
    }

    public void setFunCharacter(String funCharacter) {
        this.funCharacter = funCharacter;
    }

    public int getSizePercent() {
        return sizePercent;
    }

    public void setSizePercent(int sizePercent) {
        this.sizePercent = sizePercent;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getStyleBoldAndItalic() {
        return styleBoldAndItalic;
    }

    public void setStyleBoldAndItalic(int styleBoldAndItalic) {
        this.styleBoldAndItalic = styleBoldAndItalic;
    }

    public void printInfoWithLogKey(String key){
        Log.i(key,
                "index = " + this.indexOfFunLetter
                    + "\t\tletter = " + this.funCharacter);

    }

    public int getPaddingNumber() {
        return paddingNumber;
    }

    public void setPaddingNumber(int paddingNumber) {
        this.paddingNumber = paddingNumber;
    }
}
