package com.gainwise.fun_textPRO.Utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.gainwise.fun_textPRO.BEANS.FunTextDirector
import com.gainwise.fun_textPRO.R
import java.io.*
import java.util.*


class ScreenShot {

    companion object {
//        fun takeScreenShot(v: View): Bitmap {
//            v.isDrawingCacheEnabled = true
//            v.buildDrawingCache(true)
//            var b = Bitmap.createBitmap(v.drawingCache)
//            v.isDrawingCacheEnabled = false
//            return b
//        }

        fun takeScreenShot(view: View): Bitmap {

            val bitmap = Bitmap.createBitmap(view.width,
                    view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }

        fun compress(bitmap: Bitmap): Bitmap{
            val out = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 1, out)
            val decoded = BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))
            return decoded;
        }


        fun generateGIF(context: Context, bitmaps: ArrayList<Bitmap>, os: FileOutputStream, num: Int, notificationManagerCompat: NotificationManagerCompat){
            val builder = NotificationCompat.Builder(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel
                val name = "default use"
                val description = "get reminders from this app"
                val importance = NotificationManager.IMPORTANCE_DEFAULT

                val mChannel = NotificationChannel("ChannelID", name, importance)
                mChannel.description = description
                mChannel.setSound(null, null)
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                val notificationManager = context.getSystemService(
                        NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(mChannel)
                builder.setChannelId("ChannelID")
            }
            builder.setSmallIcon(R.drawable.ic_mood_black_24dp)
            builder.setContentTitle("GIF Render and Save")
            builder.setContentText("progress..")
            builder.priority = NotificationCompat.PRIORITY_DEFAULT

            val max_progress = bitmaps.count()
            var current_progress = 0
            builder.setProgress(max_progress, current_progress, false)

            notificationManagerCompat.notify(num, builder.build())


            val writer = AnimatedGIFWriter(true)
            // Use -1 for both logical screen width and height to use the first frame dimension
            writer.prepareForWrite(os, -1, -1)
            for(bitmap in bitmaps){
                builder.setProgress(max_progress, current_progress++, false)
                notificationManagerCompat.notify(num, builder.build())
                writer.writeFrame(os, bitmap);
                // Keep adding frame here
            }
            notificationManagerCompat.cancel(num);
            writer.finishWrite(os);
            // And you are done!!!

        }

        fun placeFileInDirectory(context: Context, bitmaps: ArrayList<Bitmap>){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){

            }else{
                val sdCard = Environment.getExternalStorageDirectory()
                val dir = File(sdCard.absolutePath + "/FUNgif")
                if(!dir.exists()){
                    dir.mkdirs()
                }
                val fileName = String.format("FunText"+System.currentTimeMillis()+".gif")
                val fullName = sdCard.absolutePath + "/FUNgif/" + fileName
                val outFile = File(dir, fileName)
                val notificationManagerCompat = NotificationManagerCompat.from(context)
                val num = Random().nextInt(100000)
                Log.i("FUNTEXT", "full path and filename is ${outFile.toString()}")
                try {

                    val out = FileOutputStream(outFile)

                    generateGIF(context, bitmaps,out, num,notificationManagerCompat )



                    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    intent.data = Uri.fromFile(outFile)
                    context.sendBroadcast(intent)

                    if(Utils.quickNotifications(context)){
                        FunTextDirector.showNotification(context,fullName)
                    }


                    Log.i("FUNTEXT", "Successfully wrote file to storage")

                } catch (e: IOException) {
                    notificationManagerCompat.cancel(num);
                    Log.i("FUNTEXT", "Exception caught while writing file because of reason: \n\n ${e.message}")

                }
            }


        }


//        fun generateGIF(bitmaps: ArrayList<Bitmap>): ByteArray {
//
//            val bos = ByteArrayOutputStream()
//            val encoder = AnimatedGifEncoder()
//            encoder.start(bos)
//            for (bitmap in bitmaps) {
//                encoder.addFrame(compress(bitmap))
//            }
//            encoder.finish()
//            return bos.toByteArray()
//        }



//        fun placeFileInDirectory(context: Context, bitmaps: ArrayList<Bitmap>){
//            if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED){
//
//            }else{
//                val sdCard = Environment.getExternalStorageDirectory()
//                val dir = File(sdCard.absolutePath + "/FunTextGIF")
//                val fileName = String.format("FunText"+System.currentTimeMillis()+".gif")
//                val fullName = sdCard.absolutePath + "/FunTextGIF/" + fileName
//                val outFile = File(dir, fileName)
//
//                Log.i("FUNTEXT", "full path and filename is ${outFile.toString()}")
//                try {
//
//                    val out = FileOutputStream(outFile)
//                    out.write(generateGIF(bitmaps))
//                    out.flush()
//                    out.close()
//
//                    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//                    intent.data = Uri.fromFile(outFile)
//                    context.sendBroadcast(intent)
//
//                    FunTextDirector.showNotification(context,fullName)
//
//
//                    Log.i("FUNTEXT", "Successfully wrote file to storage")
//
//                } catch (e: IOException) {
//                    Log.i("FUNTEXT", "Exception caught while writing file because of reason: \n\n ${e.message}")
//
//                }
//            }
//
//
//        }
        fun placeFile(context: Context, bitmap: Bitmap){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){

            }else{
                val sdCard = Environment.getExternalStorageDirectory()
                val dir = File(sdCard.absolutePath + "/FunTextGIF")
                if(!dir.exists()){
                    dir.mkdirs()
                }
                val fileName = String.format("FunText"+System.currentTimeMillis()+".gif")
                val fullName = sdCard.absolutePath + "/FunTextGIF/" + fileName
                val outFile = File(dir, fileName)

                Log.i("FUNTEXT", "full path and filename is ${outFile.toString()}")
                try {

                    val out = FileOutputStream(outFile)
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
                    out.flush()
                    out.close()

                    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    intent.data = Uri.fromFile(outFile)
                    context.sendBroadcast(intent)

                    FunTextDirector.showNotification(context,fullName)


                    Log.i("FUNTEXT", "Successfully wrote file to storage")

                } catch (e: IOException) {
                    Log.i("FUNTEXT", "Exception caught while writing file because of reason: \n\n ${e.message}")

                }
            }


        }

    }

    }



