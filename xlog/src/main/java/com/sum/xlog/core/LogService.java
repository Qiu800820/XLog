package com.sum.xlog.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.sum.xlog.print.LogLevel;

public class LogService extends Service {

    public static final String COMMEND_LOG = "LOG";
    public static final String EXTRA_KEY_LOG = "extra_log";


    @Override
    public void onCreate() {
        super.onCreate();
        FileLogHelper.getInstance().deleteExpireFile();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && COMMEND_LOG.equals(intent.getAction())) {
            onCommandLog(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void onCommandLog(Intent intent) {
        String log = intent.getStringExtra(EXTRA_KEY_LOG);
        FileLogHelper.getInstance().logToFile(log, null, null, LogLevel.D);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LogService", "LogService onDestroy");
        FileLogHelper.getInstance().destroy();
    }

}
