package com.thakursaab.bloodbank.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SavedSharedPreferences {
    static final String USER_NUMBER= "12345";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String number)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_NUMBER, number);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(USER_NUMBER, "");
    }
}
