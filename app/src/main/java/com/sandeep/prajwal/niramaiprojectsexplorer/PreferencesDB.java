package com.sandeep.prajwal.niramaiprojectsexplorer;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by KingCobra on 28/09/19.
 */

public class PreferencesDB {

    public static void putString(Context context,String key, String sortMethod) {
    SharedPreferences pref =context.getSharedPreferences("MyPref", 0); // 0 - for private mode
    SharedPreferences.Editor editor = pref.edit();
    editor.putString(key, sortMethod); // Storing string
        editor.apply();
}

    public static String getString(Context context, String currentSort) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        return  pref.getString(currentSort, ""); // getting String
    }
}
