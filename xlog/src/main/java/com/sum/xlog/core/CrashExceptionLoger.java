package com.sum.xlog.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import com.sum.xlog.util.FileUtil;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CrashExceptionLoger implements CrashHandler.OnCaughtCrashExceptionListener
{
    
    public static final String TAG = "CrashExceptionProcess";
    /** 当crash异常发生时该使用该线程处理余下事宜 **/
    private ExecutorService mProcessExecutor;
    /** 引用程序Context **/
    private Context mAppContext;
    /** 用来存储设备信息和异常信息 */
    private Map<String, String> infos = new HashMap<String, String>();
    
    public CrashExceptionLoger(Context context)
    {
        mAppContext = context;
        mProcessExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onCaughtCrashException(Thread thread, Throwable ex)
    {
        final Throwable fex = ex;
        
        mProcessExecutor.execute(new Runnable()
        {
            
            @Override
            public void run()
            {
                collectDeviceInfo(mAppContext);
                logCrashInfo(fex);
                OnUpdateCrashInfoListener mOnUpdateCrashInfoListener = XLog.getXLogConfiguration().getOnUpdateCrashInfoListener();
                
                if(mOnUpdateCrashInfoListener != null){
                	File file = new File(FileUtil.getTodayLogFilePath());
                	if(file.exists())
	                	mOnUpdateCrashInfoListener.onUpdateCrashInfo(file);
                }
                
            }
        });
    }
    
    /**
     * 收集设备参数信息
     * @param ctx 上下文文本对象
     */
    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }
    
    
    private void logCrashInfo(Throwable ex){
        
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        infos.clear();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String result = writer.toString();
        printWriter.close();
        String crashLog = sb.append(result).toString();
        
        XLog.crash(crashLog);
    }
    
    

}
