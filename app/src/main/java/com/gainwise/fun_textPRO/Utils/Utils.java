package com.gainwise.fun_textPRO.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.gainwise.fun_textPRO.BEANS.Outfit;
import com.gainwise.fun_textPRO.Fragments.SettingsFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Utils {

    //the key for the prefs is DEFAULT


    //int keys for the height is displayHeight/displayWidth/pipHeightMax
    public static void setScreenParams(Context context, int height, int width, int pipHeightMax){
        SharedPreferences.Editor editor = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        editor.putInt("displayHeight", height);
        editor.putInt("displayWidth", width);
        editor.putInt("pipHeightMax", pipHeightMax);
        editor.apply();

    }

    public static int getMaxHeightFullScreen(Context context){
        SharedPreferences prefs = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return prefs.getInt("displayHeight", 0);
    }

    public static int getMaxWidthFullScreen(Context context){
        SharedPreferences prefs = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return prefs.getInt("displayWidth", 0);
    }

    public static int getMaxHeightPipScreen(Context context){
        SharedPreferences prefs = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return prefs.getInt("pipHeightMax", 0);
    }


    public static BigDecimal BD(int num){
        return new BigDecimal(num);
    }

    public static BigDecimal BD(float num){
        return new BigDecimal(num);
    }

    public static boolean noDefaultScreenSizeSet(Context context) {
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        int first = pref.getInt("displayHeight", 0);

        if(first == 0){
            return true;
        }else{
            return false;
        }
    }



    //gets message text from prefs
    public static String SPgetMessageText(Context context){
        ArrayList<Outfit> list = getOutfitList(context);
        if(list.size()>0){
            StringBuilder sb = new StringBuilder();
            for(int i = 0 ; i<list.size(); i++){
                sb.append(list.get(i).getFunCharacter());
            }
            return sb.toString();
        }else{
            return  InitialOutfit.startingText();
        }


    }


        //sets message via individual outfit object indexes
    public static void SPsetMessageTextOnly(Context context, String message) {

        //keep all other attributes the same, just update the message.
        ArrayList<Outfit> list = new ArrayList<>();
        int numForBold = getGlobalBold(context);
        int numForItalic = getGlobalItalic(context);
        int numForOutfit = 0;

        if (numForItalic == 0 && numForBold == 0) {
            numForOutfit = 0;
        }
        if (numForItalic == 1 && numForBold == 0) {
            numForOutfit = 1;
        }
        if (numForItalic == 0 && numForBold == 1) {
            numForOutfit = 2;
        }
        if (numForItalic == 1 && numForBold == 1) {
            numForOutfit = 3;
        }


        for (int i = 0; i < message.length(); i++) {
            Outfit outfit = new Outfit();
            outfit.setIndexOfFunLetter(i);
            outfit.setPaddingNumber(SPgetPaddingForFT(context));
            outfit.setFunCharacter(String.valueOf(message.charAt(i)));
            outfit.setStyleBoldAndItalic(numForOutfit);
            outfit.setSizePercent(SPgetMessageTextSizePercentage(context));
            outfit.setTextColor(getGlobalTextColor(context));
            list.add(outfit);
        }
        saveOutfitJSON(context, list);

    }


    //sets message via individual outfit object indexes
    public static void SPsetMessageText(Context context, String message) {

        //TODO accomodate emojis
        Log.i("FUNTEXT", "SPsetMessageText called with message " + message);

        Log.i("FUNTEXT12", "message length " + message.length());
        ArrayList<String> messageList =  KotFunctions.convertStringToListForFT(message);
        Log.i("FUNTEXT12", "messageList size " + messageList.size());
        //keep all other attributes the same, just update the message.
        ArrayList<Outfit> list = new ArrayList<>();
        int numForBold = getGlobalBold(context);
        int numForItalic = getGlobalItalic(context);
        int numForOutfit = 0;

        if (numForItalic == 0 && numForBold == 0){
            numForOutfit = 0;
        }
        if (numForItalic == 1 && numForBold == 0){
            numForOutfit = 1;
        }
        if (numForItalic == 0 && numForBold == 1){
            numForOutfit = 2;
        }
        if (numForItalic == 1 && numForBold == 1){
            numForOutfit = 3;
        }


        if(messageList.size() == 0){
            //empty, save blank

        }else{
            list = getOutfitList(context);
            Log.i("FUNTEXT12", "list size " + list.size());

            if(messageList.size() == list.size()){
                if(KotFunctions.isTwoArrayListsWithSameValues(messageList,list)){
                    //same number of characters, same word - DO NOTHING
                    Log.i("FUNTEXT12", "list equal size, message is same");

                }else{
                    //same number of characters, different word - Change letters only
                    Log.i("FUNTEXT12", "list equal size, message is not same");
                    for(int i = 0; i< messageList.size(); i++){
                        list.get(i).setFunCharacter(String.valueOf(messageList.get(i)));
                    }

                }

            }else if(messageList.size()>list.size()){
                Log.i("FUNTEXT12", "messageList size greater than list size");
                //new word is bigger than old word - find index of change/ push new indexes for current word addd in new

                boolean inMiddle = false;
                int indexOfChange = 0;
                int changeLength = 0;

                for(int i = 0; i< list.size(); i++) {
                    if(!list.get(i).getFunCharacter().equals(messageList.get(i))){
                        inMiddle = true;
                        indexOfChange = i;
                        changeLength = messageList.size() - list.size();
                        break;
                    }
                }

                if(inMiddle){
                    for(int i = indexOfChange; i < list.size(); i++){
                        int temp =  list.get(i).getIndexOfFunLetter()+changeLength;
                        list.get(i).setIndexOfFunLetter(temp);
                    }

                    for (int i = 0; i< changeLength; i++){
                        Outfit outfit = new Outfit();
                        outfit.setPaddingNumber(SPgetPaddingForFT(context));
                        outfit.setIndexOfFunLetter(i + indexOfChange);
                        outfit.setFunCharacter(String.valueOf(messageList.get(i + indexOfChange)));
                        outfit.setStyleBoldAndItalic(numForOutfit);
                        outfit.setSizePercent(SPgetMessageTextSizePercentage(context));
                        outfit.setTextColor(getGlobalTextColor(context));
                        list.add(outfit);
                    }
                }else{
                    for(int i = list.size(); i< messageList.size();i++){
                        Outfit outfit = new Outfit();
                        outfit.setPaddingNumber(SPgetPaddingForFT(context));
                        outfit.setIndexOfFunLetter(i);
                        outfit.setFunCharacter(String.valueOf(messageList.get(i)));
                        outfit.setStyleBoldAndItalic(numForOutfit);
                        outfit.setSizePercent(SPgetMessageTextSizePercentage(context));
                        outfit.setTextColor(getGlobalTextColor(context));
                        list.add(outfit);
                    }
                }

                    KotFunctions.sortListByIndex(list);

                    Log.i("FUNTEXT99", "index Changed at " + indexOfChange);


                }else{
                    if(list.size() == 0) {
                        //new save - nothing in the bank lolz algo life
                        for (int i = 0; i < messageList.size(); i++) {
                            Outfit outfit = new Outfit();
                            outfit.setPaddingNumber(SPgetPaddingForFT(context));
                            outfit.setIndexOfFunLetter(i);
                            outfit.setFunCharacter(String.valueOf(messageList.get(i)));
                            outfit.setStyleBoldAndItalic(numForOutfit);
                            outfit.setSizePercent(SPgetMessageTextSizePercentage(context));
                            outfit.setTextColor(getGlobalTextColor(context));
                            list.add(outfit);
                        }

                    }else{
                        //new word is SMALLER than old word
                        Log.i("FUNTEXT", "smaller word ");
                        int indexOfChange = 0;
                        int changeLength = 0;

                        for(int i = 0; i< list.size();i++){
                            if(i<messageList.size()){
                                if(!list.get(i).getFunCharacter().equals(messageList.get(i))){
                                    indexOfChange = i;
                                    changeLength = list.size() -  messageList.size();
                                    break;
                                }
                            }else{
                                indexOfChange = i;
                                changeLength =list.size() -  messageList.size();
                                break;
                            }
                        }
                        //create int array to hold values for
                        int indexesToRemove[] = new int[changeLength];

                        Log.i("FUNTEXT44", "list size before remove " + list.size());
                        //remove the changed amount by index
                        for(int i = 0; i<changeLength; i++){
                            int num = i+indexOfChange;
                            indexesToRemove[i]=num;
                        }
                        //remove in reverse as to preserve proper indexes
                        for(int i = changeLength; i>0; i--){
                            list.remove(indexesToRemove[i-1]);
                        }



                        Log.i("FUNTEXT44", "list size after remove " + list.size());

                        //reorder indexes
                        for(int i = 0 ; i< list.size(); i++)
                        {
                            list.get(i).setIndexOfFunLetter(i);
                        }

                        Log.i("FUNTEXT99", "index Changed at " + indexOfChange);
                    }
            }
        }

        //take care of chunk replacements if necessary
        if(!KotFunctions.isTwoArrayListsWithSameValues(messageList, list)){
            for(int i = 0; i<list.size(); i++){
                list.get(i).setIndexOfFunLetter(i);
                list.get(i).setFunCharacter(messageList.get(i));
            }
        }


        saveOutfitJSON(context, list);

    }

    //gets message text size from prefs % = calculations will be done depending on pip/full
    public static int SPgetMessageTextSizePercentage(Context context){
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return pref.getInt("messageTextSize", 70);
    }

    //sets message text size from prefs % = calculations will be done depending on pip/full
    public static void SPsetMessageTextSizePercentage(Context context, int percent){
        Log.i("FUNTEXT textsizeP", "size is int value percent of " +percent);
        SharedPreferences.Editor edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        edit.putInt("messageTextSize", percent);
        edit.apply();

        ArrayList<Outfit> list = getOutfitList(context);
        if(list.size()>0){
            for(int i = 0 ; i<list.size(); i++){
              list.get(i).setSizePercent(percent);
            }
            saveOutfitJSON(context, list);
        }
    }

    public static float float2dec(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }



    public static void saveOutfitJSON(Context context, ArrayList<Outfit> listIn){
        Log.i("FUNTEXT", "saveOutfitJSON called wish list size of "+listIn.size());
        Gson gson = new Gson();
        String json = gson.toJson(listIn);
        Log.i("FUNTEXT", json);
        SharedPreferences.Editor edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        edit.putString("outfitJSON", json);
        edit.apply();

    }

    public static ArrayList<Outfit> getOutfitList(Context context){
        Log.i("FUNTEXT", "getOutFitList called");
        Gson gson = new Gson();
        ArrayList<Outfit> list = new ArrayList<Outfit>();
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        String json =  pref.getString("outfitJSON", "");
        if(json.length()>0){
            Log.i("FUNTEXT", "getOutFitList1 json "+ json);
            Type collectionType = new TypeToken<Collection<Outfit>>(){}.getType();
            list = gson.fromJson(json, collectionType);
            Log.i("FUNTEXT", "getOutFitList1 returning list size of "+ list.size());
            return list;
        }else{
            Log.i("FUNTEXT", "getOutFitList2 returning list size of "+ list.size());
            return list;
        }
    }

    public static int getViewBGcolor(Context context){
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return pref.getInt("bgColor", InitialOutfit.BGcolor());
    }

    //sets message text size from prefs % = calculations will be done depending on pip/full
    public static void setViewBGcolor(Context context, int color){
        SharedPreferences.Editor edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        edit.putInt("bgColor", color);
        edit.apply();
    }

    public static int getGlobalTextColor(Context context){
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return pref.getInt("textColor", InitialOutfit.textColor());
    }

    //sets message text size from prefs % = calculations will be done depending on pip/full
    public static void setGlobalTextColor(Context context, int color){
        SharedPreferences.Editor edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        edit.putInt("textColor", color);
        edit.apply();

        ArrayList<Outfit> list = getOutfitList(context);
        if(list.size()>0){
            for(int i = 0 ; i<list.size(); i++){
                list.get(i).setTextColor(color);
            }
            saveOutfitJSON(context, list);
        }
    }

    public static void setGlobalBold(Context context, boolean boldOrNot){
        int numForOutfit = 0;
        int numForBold = 0;
        if(boldOrNot){
            numForBold = 1;
        }

        SharedPreferences.Editor edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        edit.putInt("boldOrNot", numForBold);
        edit.apply();

        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        int numForItalic = pref.getInt("italicOrNot", 0);

        if (numForItalic == 0 && numForBold == 0){
            numForOutfit = 0;
        }
        if (numForItalic == 1 && numForBold == 0){
            numForOutfit = 1;
        }
        if (numForItalic == 0 && numForBold == 1){
            numForOutfit = 2;
        }
        if (numForItalic == 1 && numForBold == 1){
            numForOutfit = 3;
        }

        ArrayList<Outfit> list = getOutfitList(context);
        if(list.size()>0){
            for(int i = 0 ; i<list.size(); i++){
                list.get(i).setStyleBoldAndItalic(numForOutfit);
            }
            saveOutfitJSON(context, list);
        }
    }
    public static void setGlobalItalic(Context context, boolean italicOrNot){
        int numForOutfit = 0;
        int numForItalic = 0;
        if(italicOrNot){
            numForItalic = 1;
        }

        SharedPreferences.Editor edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        edit.putInt("italicOrNot", numForItalic);
        edit.apply();

        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        int numForBold = pref.getInt("boldOrNot", 0);

        if (numForItalic == 0 && numForBold == 0){
            numForOutfit = 0;
        }
        if (numForItalic == 1 && numForBold == 0){
            numForOutfit = 1;
        }
        if (numForItalic == 0 && numForBold == 1){
            numForOutfit = 2;
        }
        if (numForItalic == 1 && numForBold == 1){
            numForOutfit = 3;
        }

        ArrayList<Outfit> list = getOutfitList(context);
        if(list.size()>0){
            for(int i = 0 ; i<list.size(); i++){
                list.get(i).setStyleBoldAndItalic(numForOutfit);
            }
            saveOutfitJSON(context, list);
        }
    }
    public static boolean stillTyping(Context context){
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS2", Context.MODE_PRIVATE);
        return pref.getBoolean("stillTyping", false);
    }
    public static void setStillTyping(Context context, boolean stillTyping){
        SharedPreferences.Editor pref = context.getSharedPreferences("DEFAULTS2", Context.MODE_PRIVATE).edit();
        pref.putBoolean("stillTyping", stillTyping);
        pref.apply();
    }
    public static boolean didCrash(Context context){
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return pref.getBoolean("CRASHED", false);
    }
    public static void setCrash(Context context, boolean paramDidCrash){
        SharedPreferences.Editor pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
         pref.putBoolean("CRASHED", paramDidCrash);
         pref.apply();
    }
    public static String getCrashReport(Context context){
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return pref.getString("CRASH", "");
    }
    public static void setCrashReport(Context context, String crashString){
        SharedPreferences.Editor pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        pref.putString("CRASH", crashString);
        pref.apply();
    }

    public static int getGlobalItalic(Context context){
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return pref.getInt("italicOrNot", 0);
    }
    public static int getGlobalBold(Context context){
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return pref.getInt("boldOrNot", 0);
    }

    //sets flow global direction
    public static void setGlobalFlowDirection(Context context, boolean rightToLeft){
        SharedPreferences.Editor edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        edit.putBoolean("rightToLeft", rightToLeft);
        edit.apply();

    }
    public static boolean getGlobalFlowDirection(Context context){
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return pref.getBoolean("rightToLeft", true);
    }

    //sets flow global speed
    public static void setGlobalFlowSpeed(Context context, int speed){
        SharedPreferences.Editor edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        edit.putInt("globalSpeed", speed);
        edit.apply();

    }

    public static int getGlobalFlowSpeed(Context context){
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return pref.getInt("globalSpeed", 30);
    }
    public static void setGlobalTypeFaceInt(Context context, int indexOfFont) {
        SharedPreferences.Editor edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        edit.putInt("typefaceInt", indexOfFont);
        edit.apply();
    }
    public static int getGlobalTypeFaceInt(Context context){
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return pref.getInt("typefaceInt", 7);
    }

    public static void randomizeAllColors(Context context){

        ArrayList<Outfit> list = getOutfitList(context);
        if(list.size()>0){
            for(int i = 0 ; i<list.size(); i++){
                list.get(i).setTextColor(KotFunctions.getRandomColorInt());
            }
            saveOutfitJSON(context, list);
        }
    }

    public static void randomizeTextSize(Context context){
        Random random = new Random();
        ArrayList<Outfit> list = getOutfitList(context);
        if(list.size()>0){
            for(int i = 0 ; i<list.size(); i++){
                list.get(i).setSizePercent(random.nextInt(34)+50);
            }
            saveOutfitJSON(context, list);
        }
    }
    public static void defaults(Context context){
        SPsetMessageText(context, "Welcome, Thank You!");
        setStillTyping(context, false);
        setGlobalBold(context,false);
        setGlobalFlowDirection(context, true);
        setGlobalFlowSpeed(context, 30);
        setGlobalItalic(context, false);
        setGlobalTypeFaceInt(context, 7);
        setGlobalTextColor(context, InitialOutfit.textColor());
        setViewBGcolor(context, InitialOutfit.BGcolor());
        SPsetMessageTextSizePercentage(context, 70);
    }

    public static void setShowAd(Context context, boolean needTo){
        SharedPreferences.Editor edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        edit.putBoolean("showAd", needTo);
        edit.apply();
    }
    public static boolean getShowAd(Context context){
        SharedPreferences prefs = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return prefs.getBoolean("showAd", false);

    }
    public static boolean getBetaMember(Context context){
        SharedPreferences prefs = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return prefs.getBoolean("betaMember", false);

    }

    public static void setBetaMember(Context context, boolean beta){
        SharedPreferences.Editor edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        edit.putBoolean("showAd", beta);
        edit.apply();
    }

    public static int getGIFsaveFrameRate(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(SettingsFragment.GIFFRAMERATE, "15"));
    }
    public static int getEmojiSizeForGIFSave(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(SettingsFragment.EMOJISIZE, "15"));
    }

    public static boolean showEmojiHint(Context context){
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS2", Context.MODE_PRIVATE);
        return pref.getBoolean("emojiHint", true);
    }
    public static void setShowEmojiHint(Context context, boolean show){
        SharedPreferences.Editor pref = context.getSharedPreferences("DEFAULTS2", Context.MODE_PRIVATE).edit();
        pref.putBoolean("emojiHint", show);
        pref.apply();
    }

    public static boolean autoRotate(Context context) {
//        String on = PreferenceManager.getDefaultSharedPreferences(context).getString(SettingsFragment.AUTOFULLSCREEN, "ON");
//        if (on.equals("ON")){
//            Log.i("FUNTEXT", "AUTOROTATE ON");
//            return true;
//        }else{
//            return false;
//        }
        return false;
    }

    public static boolean quickNotifications(Context context) {
        String on = PreferenceManager.getDefaultSharedPreferences(context).getString(SettingsFragment.AUTOFULLSCREEN, "ON");
        if (on.equals("ON")){
            Log.i("FUNTEXT", "AUTOROTATE ON");
            return true;
        }else{
            return false;
        }

    }

    //gets message text size from prefs % = calculations will be done depending on pip/full
    public static int SPgetPaddingForFT(Context context){
        SharedPreferences pref = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE);
        return pref.getInt("paddingNumber", 0);
    }

    //sets message text size from prefs % = calculations will be done depending on pip/full
    public static void SPsetPaddingForFT(Context context, int number){
        SharedPreferences.Editor edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit();
        edit.putInt("paddingNumber", number);
        edit.apply();

        ArrayList<Outfit> list = getOutfitList(context);
        if(list.size()>0){
            for(int i = 0 ; i<list.size(); i++){
                list.get(i).setPaddingNumber(number);
            }
            saveOutfitJSON(context, list);
        }
    }


}

