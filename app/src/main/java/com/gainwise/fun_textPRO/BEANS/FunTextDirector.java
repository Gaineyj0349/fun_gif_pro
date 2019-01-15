package com.gainwise.fun_textPRO.BEANS;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.gainwise.fun_textPRO.R;
import com.gainwise.fun_textPRO.Utils.ConvUtil;
import com.gainwise.fun_textPRO.Utils.KotFunctions;
import com.gainwise.fun_textPRO.Utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class FunTextDirector {

    private String message;
    private Context context;
    private int maxHeight;


    public FunTextDirector(Context context, int maxHeight)
    {
        this.maxHeight = maxHeight;
        this.context = context;
    }


    //this method builds the message as individual textviews.
    public ArrayList<FunTextView> getArrayOfTVs(boolean forGIFSAVE){
        int emojiSize = Utils.getEmojiSizeForGIFSave(context);
        ArrayList<Outfit> arrayOfOutfits = Utils.getOutfitList(context);

        ArrayList<FunTextView> arrayOfFunTVs = new ArrayList<>();
        Typeface typeface = KotFunctions.getTypeFaceWithIndex(context,Utils.getGlobalTypeFaceInt(context));
        Typeface emojiTypeFace = KotFunctions.getTypeFaceWithIndex(context,0);
        if(arrayOfOutfits.size()>0) {
            for (int i = 0; i < arrayOfOutfits.size(); i++) {

                //TODO make check for padding

                FunTextView t;
                if(arrayOfOutfits.get(i).getPaddingNumber() >0){
                    Log.i("FUNTEXT832", "Custom padding");
                    //custom padding
                     t = new FunTextView(context, false, maxHeight, arrayOfOutfits.get(i).getPaddingNumber());
                }else{
                    Log.i("FUNTEXT832", "Default padding");
                    //default padding
                     t = new FunTextView(context, true, maxHeight, arrayOfOutfits.get(i).getPaddingNumber());
                }


                if(forGIFSAVE){
                    if(KotFunctions.isEmoji(arrayOfOutfits.get(i).getFunCharacter())){
                        Log.i("FUNTEXT323", "index is emoji : " + i);
                        t.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                ConvUtil.getTextSizeWithPercentAndMax(emojiSize,maxHeight));
                        t.getTextView().setTypeface(emojiTypeFace, ConvUtil.returnTypeOfStyle(arrayOfOutfits.get(i).getStyleBoldAndItalic()));

                    }else{
                        t.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                ConvUtil.getTextSizeWithPercentAndMax(arrayOfOutfits.get(i).getSizePercent(), maxHeight));
                        t.getTextView().setTypeface(typeface, ConvUtil.returnTypeOfStyle(arrayOfOutfits.get(i).getStyleBoldAndItalic()));

                    }
                }else{
                    if(KotFunctions.isEmoji(arrayOfOutfits.get(i).getFunCharacter())){
                        t.getTextView().setTypeface(emojiTypeFace, ConvUtil.returnTypeOfStyle(arrayOfOutfits.get(i).getStyleBoldAndItalic()));

                    }else{
                        t.getTextView().setTypeface(typeface, ConvUtil.returnTypeOfStyle(arrayOfOutfits.get(i).getStyleBoldAndItalic()));

                    }
                    t.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            ConvUtil.getTextSizeWithPercentAndMax(arrayOfOutfits.get(i).getSizePercent(), maxHeight));

                }

                if(Utils.getGlobalItalic(context) == 1){
                      t.getTextView().setText(arrayOfOutfits.get(i).getFunCharacter());
                }else{
                    t.getTextView().setText(arrayOfOutfits.get(i).getFunCharacter());
                }

                t.getTextView().setTextColor(arrayOfOutfits.get(i).getTextColor());


                arrayOfFunTVs.add(t);
            }
        }


        return arrayOfFunTVs;

    }

    public static void showNotification(Context context, String fullPath) {
        RemoteViews contentView = new RemoteViews("com.gainwise.fun_textPRO", R.layout.custom_notification);
        contentView.setImageViewResource(R.id.image, R.mipmap.icon);
        contentView.setTextViewText(R.id.notificationtext, "Your GIF has saved! Click to Share");

        Notification.Builder builder =
                new Notification.Builder(context);

        builder.setSmallIcon
                (R.drawable.ic_mood_black_24dp);

        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(Uri.parse("file://" + fullPath), "image/gif");
        Uri uri = Uri.fromFile(new File(fullPath));
        intent.putExtra(Intent.EXTRA_STREAM, uri);


        PendingIntent pendingIntent;

            pendingIntent =
                    PendingIntent.getActivity(context, 1
                            , intent, 0);


        builder.setContentIntent(pendingIntent);
        builder.setContent(contentView);
        builder.setContentTitle("Your FunText GIF has saved! Click to Share!");
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setDefaults(Notification.DEFAULT_ALL);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            CharSequence name = "default use";
            String description = "get reminders from this app";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel("ChannelID", name, importance);
            mChannel.setDescription(description);
            mChannel.setSound(null, null);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            builder.setChannelId("ChannelID");
        }

        Notification notification = builder.build();


        NotificationManager notificationMgr = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);


        notificationMgr.notify(new Random().nextInt(100000), notification);
    }


}
