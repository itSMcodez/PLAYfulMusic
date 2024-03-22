package com.itsmcodez.playful.utils;
import android.content.ServiceConnection;
import android.util.Log;

public final class ServiceUtils {
    public static final String TAG = "ServiceUtils";
    public static final String ARGS = "com.itsmcodez.playful." + TAG;
    public static final int BIND_AUTO_CREATE = 1;
    
    static{
        AppUtils.addRegistrar(ServiceUtils.getRegistrar());
    }
    
    public static ServiceConnection getUpStreamConnection(ServiceConnection serviceConnection) {
        getRegistrar();
    	return serviceConnection;
    }
    
    public static String getRegistrar() {
        Log.i(TAG, "Accessing registrar...");
        return ServiceUtils.ARGS;
    }
}
