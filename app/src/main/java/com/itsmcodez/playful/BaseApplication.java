package com.itsmcodez.playful;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import androidx.core.app.NotificationCompat;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class BaseApplication extends Application {
	private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    public static final String CHANNEL_ID = "MUSIC NOTIFICATION SERVICE";
    
	@Override
	public void onCreate() {
		this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				Intent intent = new Intent(getApplicationContext(), DebugActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				intent.putExtra("error", getStackTrace(ex));
				PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 11111, intent, PendingIntent.FLAG_ONE_SHOT);
				AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
				am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, pendingIntent);
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(2);
				uncaughtExceptionHandler.uncaughtException(thread, ex);
			}
		});
        
        createNotificationChannel();
        
		super.onCreate();
	}

    private void createNotificationChannel() {
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        	NotificationChannel notifChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_LOW);
            notifChannel.setShowBadge(true);
            notifChannel.enableLights(false);
            notifChannel.enableVibration(false);
            notifChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(notifChannel);
        }
    }
    
	private String getStackTrace(Throwable th){
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		Throwable cause = th;
		while(cause != null){
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		final String stacktraceAsString = result.toString();
		printWriter.close();
		return stacktraceAsString;
	}
}
