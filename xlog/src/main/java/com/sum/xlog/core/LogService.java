package com.sum.xlog.core;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @ClassName LogService
 * @Description 这个Service主要是用于将日志写入到文件里
 * @Author Robert
 * @Date 2015年8月12日 下午5:01:52
 * @Version V1.0
 */
public class LogService extends Service{
    
    public static final String COMMACD_LOG = "LOG";
    public static final String EXTRA_KEY_LOG = "extra_log";
    
	XLogConfiguration mConfiguration;
	Handler mHandler;
	
	public LogService(XLogConfiguration configuration) {
		mConfiguration = configuration;
	}
	
	public LogService() {
		// Empty default constructor
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("LogService", "LogService onCreate");
		FileLogHelper.getInstance();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent == null)
			return super.onStartCommand(intent, flags, startId);
	    String action = intent.getAction();
	    if(null == action || action.trim().length() <= 0){
	        return super.onStartCommand(intent, flags, startId);
	    }else if (COMMACD_LOG.equals(action)) {
            onCommandLog(intent);
        }
		return super.onStartCommand(intent, flags, startId);
	}

	private void onCommandLog(Intent intent)
    {
        String log = intent.getStringExtra(EXTRA_KEY_LOG);
        FileLogHelper.getInstance().logToFile(log, null, null, LogLevel.D);
    }

    @Override
	public IBinder onBind(Intent intent) {
		return null;
	}
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("LogService", "LogService onDestroy");
        FileLogHelper.getInstance().relase();
    }

}
