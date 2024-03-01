package com.itsmcodez.playful.utils;
import android.util.Log;
import java.util.ArrayList;

public final class AppUtils {
    public static final String TAG = "AppUtils";
    private static ArrayList<String> registrars = new ArrayList<>();
    
    public AppUtils(){}
    
    public static void addRegistrar(String registrar) {
        Log.d(TAG, "Adding " + registrar);
    	registrars.add(registrar);
    }
}
