package com.hosseini.abbas.havakhabar.app.Other;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;

import java.util.Locale;

/**
 * Created by abbas on 2/15/16.
 */
public class WhichFont {

    public WhichFont(){}

    public static Typeface Fontmethod(Context activity, String address){

        String font = address.length() == 0? "fonts/ADastNevis.ttf" : address;

        if (Locale.getDefault().getLanguage().equals("en"))
            font = "fonts/Existence-Light.otf";

        return Typeface.createFromAsset(
                activity.getAssets(), font);

    }
}
