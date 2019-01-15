@file:JvmName("KotFunctions")
package com.gainwise.fun_textPRO.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import com.gainwise.fun_textPRO.BEANS.Outfit
import java.util.*
import kotlin.collections.ArrayList


fun getRandomColorString(): String{
    val random = Random()
    // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
    val randomNum = random.nextInt(256 * 256 * 256)

    // format it as hexadecimal string (with hashtag and leading zeros)
    return String.format("#%06x", randomNum)
}

data class ContextAndList(val context: Context, val list: ArrayList<Bitmap>){

}

fun getIndexOfTypeFace(name: String): Int{
    Log.i("FUNTEXTfont", "name is $name")
   val fonts =  listOf("Monospace", "Sans Seriff", "Seriff", "Alfa Slab One",
            "Anton", "Archivo", "Joti One", "K2D", "Luckiest Guy", "Notable",
            "Passion One", "Permanent Marker", "Righteous", "Roboto Condensed", "Rubik", "Rubik Mono One", "Timmana"
           ,"Bangers", "Charm", "Cinzel", "Courgette", "Fredericka The Great", "Fredoka One","Grand Hotel",
           "Indie Flower", "Kaushan Script", "Lobster", "Marck Script", "Nanum Pen Script", "Pacifico",
           "Paytone One", "Philosopher", "Satisfy", "Special Elite", "Ubuntu", "Unlock", "ZCOOL KuaiLe")
    return fonts.indexOf(name)
}
fun getStringOfTypeFaceWithIndex(index: Int): String{
    Log.i("FUNTEXTfont", "index is $index")
    val fonts =  listOf("Monospace", "Sans Seriff", "Seriff", "Alfa Slab One",
            "Anton", "Archivo", "Joti One", "K2D", "Luckiest Guy", "Notable",
            "Passion One", "Permanent Marker", "Righteous", "Roboto Condensed", "Rubik", "Rubik Mono One", "Timmana"
            ,"Bangers", "Charm", "Cinzel", "Courgette", "Fredericka The Great", "Fredoka One","Grand Hotel",
            "Indie Flower", "Kaushan Script", "Lobster", "Marck Script", "Nanum Pen Script", "Pacifico",
            "Paytone One", "Philosopher", "Satisfy", "Special Elite", "Ubuntu", "Unlock", "ZCOOL KuaiLe")
    return fonts.get(index)
}


fun getTypeFaceWithIndex(context: Context, index: Int): Typeface{
    return      when(index){
                0->Typeface.MONOSPACE
                1-> Typeface.SANS_SERIF
                2-> Typeface.SERIF
                3-> Typeface.createFromAsset(context.getAssets(), "AlfaSlabOne.ttf")
                4-> Typeface.createFromAsset(context.getAssets(), "Anton-Regular.ttf")
                5-> Typeface.createFromAsset(context.getAssets(), "ArchivoBlack-Regular.ttf")
                6-> Typeface.createFromAsset(context.getAssets(), "JotiOne-Regular.ttf")
                7-> Typeface.createFromAsset(context.getAssets(), "K2D-Bold.ttf")
                8-> Typeface.createFromAsset(context.getAssets(), "LuckiestGuy-Regular.ttf")
                9-> Typeface.createFromAsset(context.getAssets(), "Notable-Regular.ttf")
                10-> Typeface.createFromAsset(context.getAssets(), "PassionOne-Regular.ttf")
                11-> Typeface.createFromAsset(context.getAssets(), "PermanentMarker-Regular.ttf")
                12-> Typeface.createFromAsset(context.getAssets(), "Righteous-Regular.ttf")
                13-> Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Regular.ttf")
                14-> Typeface.createFromAsset(context.getAssets(), "Rubik-Black.ttf")
                15-> Typeface.createFromAsset(context.getAssets(), "RubikMonoOne-Regular.ttf")
                16-> Typeface.createFromAsset(context.getAssets(), "Timmana-Regular.ttf")
        17-> Typeface.createFromAsset(context.getAssets(), "Bangers-Regular.ttf")
        18-> Typeface.createFromAsset(context.getAssets(), "Charm-Regular.ttf")
        19-> Typeface.createFromAsset(context.getAssets(), "Cinzel-Black.ttf")
        20-> Typeface.createFromAsset(context.getAssets(), "Courgette-Regular.ttf")
        21-> Typeface.createFromAsset(context.getAssets(), "FrederickatheGreat-Regular.ttf")
        22-> Typeface.createFromAsset(context.getAssets(), "FredokaOne-Regular.ttf")
        23-> Typeface.createFromAsset(context.getAssets(), "GrandHotel-Regular.ttf")
        24-> Typeface.createFromAsset(context.getAssets(), "IndieFlower.ttf")
        25-> Typeface.createFromAsset(context.getAssets(), "KaushanScript-Regular.ttf")
        26-> Typeface.createFromAsset(context.getAssets(), "Lobster-Regular.ttf")
        27-> Typeface.createFromAsset(context.getAssets(), "MarckScript-Regular.ttf")
        28-> Typeface.createFromAsset(context.getAssets(), "NanumPenScript-Regular.ttf")
        29-> Typeface.createFromAsset(context.getAssets(), "Pacifico-Regular.ttf")
        30-> Typeface.createFromAsset(context.getAssets(), "PaytoneOne-Regular.ttf")
        31-> Typeface.createFromAsset(context.getAssets(), "Philosopher-Regular.ttf")
        32-> Typeface.createFromAsset(context.getAssets(), "Satisfy-Regular.ttf")
        33-> Typeface.createFromAsset(context.getAssets(), "SpecialElite-Regular.ttf")
        34-> Typeface.createFromAsset(context.getAssets(), "Ubuntu-Regular.ttf")
        35-> Typeface.createFromAsset(context.getAssets(), "Unlock-Regular.ttf")
        36-> Typeface.createFromAsset(context.getAssets(), "ZCOOLKuaiLe-Regular.ttf")
                else->Typeface.DEFAULT
            }

}
fun getRandomColorInt(): Int{
    // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
    val nextInt = Random().nextInt(256 * 256 * 256)

    // format it as hexadecimal string (with hashtag and leading zeros)
    val colorCode = String.format("#%06x", nextInt)
    return Color.parseColor(colorCode)
}

fun getEmojiByUnicode(unicode: Int): String {
    return String(Character.toChars(unicode))
}

fun convertStringToListForFT(message: String): ArrayList<String>{
    var successive = false;
    val list = ArrayList<String>()
    Log.i("JOSHYO2",  ""+message.length )
    for(i in 0 until message.length) {
        if (Character.isUnicodeIdentifierPart(message.get(i))) {
            list.add(message.get(i).toString())
            successive = false
        } else {
            if (successive) {
            successive = false
            } else {
                val codePoint = Character.codePointAt(message, i)
                Log.i("JOSHYO3",  "" + codePoint)
                if (codePoint < 65000){
                    list.add(getEmojiByUnicode(codePoint))
                }else{
                    list.add(getEmojiByUnicode(codePoint))
                    successive = true
                }


            }
        }
    }
    return list;
}


private val emo_regex = "([\\u20a0-\\u32ff\\ud83c\\udc00-\\ud83d\\udeff\\udbb9\\udce5-\\udbb9\\udcee])"
//fun isEmoji(letter: String) : Boolean{
//    var test = false
//    val matcher = Pattern.compile(emo_regex).matcher(letter)
//    while (matcher.find()) {
//        test = true
//    }
//    return test
//}
//fun isEmoji(message: String){
//    val matcher = Pattern.compile(emo_regex).matcher(message)
//    while (matcher.find()) {
//        Log.i("JOSHYO",  matcher.group())
//    }
//}

 fun isEmoji(message: String): Boolean {
    return message.matches(("(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|" +
            "[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|" +
            "[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|" +
            "[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|" +
            "[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|" +
            "[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|" +
            "[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|" +
            "[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|" +
            "[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|" +
            "[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|" +
            "[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)+").toRegex())
}

//the name of the method explains it well...
fun isTwoArrayListsWithSameValues(list1: ArrayList<String>?, listOutfit: ArrayList<Outfit>?): Boolean {
    val list2 = listOutfit?.map { it.funCharacter }

    for(i in 0 until list1!!.size){
        Log.i("FUNTEXT321", "index " + i + " of list1 is "+ list1.get(i))
    }
    for(i in 0 until list2!!.size){
        Log.i("FUNTEXT321", "index " + i + " of list2 is "+ list2.get(i))
    }
    //null checking
    if (list1 == null && list2 == null)
        return true
    if (list1 == null && list2 != null || list1 != null && list2 == null)
        return false
    for (itemList1 in list1!!) {
        if (!list2!!.contains(itemList1))
            return false
    }

    return true
}

fun sortListByIndex(listOutfit: ArrayList<Outfit>?){
    listOutfit!!.sortBy { it.indexOfFunLetter }
}

