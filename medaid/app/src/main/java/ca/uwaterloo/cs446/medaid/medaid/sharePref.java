package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

public class sharePref extends ContextWrapper {

    public sharePref(Context base) {
        super(base);
    }

    public boolean modifyPref(String prefName, String prefValue){
        SharedPreferences prefs = getSharedPreferences("medaid", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(prefName, prefValue);
        editor.apply();
        return true;
    }

    public  String getPref(String prefName){
        SharedPreferences prefs = getSharedPreferences("medaid", MODE_PRIVATE);
        String restoredPref = prefs.getString(prefName, null);
        return restoredPref;
    }
}
