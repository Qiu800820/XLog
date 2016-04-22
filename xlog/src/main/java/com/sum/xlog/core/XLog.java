package com.sum.xlog.core;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sum.xlog.util.FileUtil;
import com.sum.xlog.util.OtherUtil;

import java.io.File;

/**
 * @ClassName XLog
 * @Description Xlog框架主要类，用于向使用者提供各种接口
 * @Author Yao
 * @Date 2015年8月12日 下午5:03:13
 * @Version V1.0
 */
public class XLog{
	
	public static final String CONFIG_NOT_NULL = "XLogConfiguration can not be initialized with null";
	public static final String CONTEXT_NOT_APPLICATION = "Context must be a application context";
	public static final String CONTEXT_NOT_NULL = "Context can not be initialized with null";
	public static final String TAG = "XLog";
	private static XLogConfiguration sXLogConfig;
	private static Context sAppContext;
	
	/**
     * @Title init
     * @Description 初始化日志框架，该方法在Android application oncreate 里调用
     */
	public static void init(Context context){
	    init(XLogConfiguration.createDefault(context));

	}
	
	/**
	 * @Title init
	 * @Description 初始化日志框架，该方法在Android application oncreate 里调用
	 */
	public static void init(XLogConfiguration configuration){
		
		if(null == configuration){
			throw new IllegalArgumentException(CONFIG_NOT_NULL);
		}

		if(sXLogConfig == null){
		    
			Log.d(TAG, "Initialize Xlog with configuration");
			sXLogConfig = configuration;
            sAppContext = sXLogConfig.getContext();
			Intent intent = new Intent(sXLogConfig.getContext() , com.sum.xlog.core.LogService.class);
			sXLogConfig.getContext().startService(intent);
			if(sXLogConfig.isCrashHandlerOpen()){
			    CrashHandler.getInstance().init(sXLogConfig.getOriginalHandler());
			    CrashHandler.getInstance().setCaughtCrashExceptionListener(new CrashExceptionLoger(sAppContext));
			}

            OtherUtil.RUN_PACKAGE_NAME = configuration.getContext().getPackageName();
		}else {
			Log.w(TAG, "Try to initialize Xlog which had already been initialized before. To re-init Xlog with new configuration call Xlog.destroy() at first.");
		}
		
	}
	
	/**
	 * 
	 * @Title destroy
	 * @Description 该方法在app 最后一个activity退出的时候释放调用，用以释放资源停止服务(如果需要的话!)
	 * @return true 释放成功 ,fasle 释放失败
	 * @Throws 
	 *
	 */
	public static void destroy(){
		
		if(sXLogConfig != null){
			Intent intent = new Intent(sAppContext,com.sum.xlog.core.LogService.class);
			sAppContext.stopService(intent);
			sXLogConfig = null;
		}else {
			Log.e(TAG, "configuration is null ,can not destory");
		}
	}
	
	public static Context getAppContext(){
	    return sAppContext;
	}
	
	/**
	 * 
	 * @Title clearFileLog
	 * @Description 删除目录下所有文件日志
	 * @return true 删除成功 ， false 删除失败
	 * @Throws 
	 */
	public static boolean clearFileLog(){
		return clearFileLog(null);
	}
	
	/**
	 * @Title clearFileLog
	 * @Description 根据文件名过滤条件删除目录下所有符合条件的文件日志
	 * @return true 删除成功 ， false 删除失败
	 * @Throws 
	 */
	public static boolean clearFileLog(String fileNameFilter){
		String path = FileUtil.getXLogPath();
		File file = new File(path);
		if(!file.exists())
			return false;
		
		return FileUtil.delFilterFile(file, fileNameFilter);
	}

	public static XLogConfiguration getXLogConfiguration(){
		return sXLogConfig;
	}
	
//	private static void throwExceptionIfConfigIsNull(){
//	    
//	    if(null == sXLogConfig){
//	        throw new IllegalArgumentException(CONFIG_NOT_NULL);
//	    }
//	}
	
	private static boolean allowConsoleLogPrint(byte printLevel){
	    return sXLogConfig.getConsoleLogLevel() <= printLevel && sXLogConfig.getConsoleLogLevel() != LogLevel.OFF;
	}
	
	private static boolean allowFileLogPrint(byte printLevel){
	    return sXLogConfig.getFileLogLevel() <= printLevel && sXLogConfig.getFileLogLevel() != LogLevel.OFF;
	}
	
	private static String getDefaultTag()
    {
        return sXLogConfig.getDefaultTag();
    }
	/****          Log.v           ******/
    public static void v(String msg, Throwable throwable)
    {
        if(null == msg || null == throwable || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.V)){
            Log.v(getDefaultTag(), msg, throwable);
        }
        if(allowFileLogPrint(LogLevel.V)){
            FileLogHelper.getInstance().logToFile(msg, throwable, getDefaultTag(), LogLevel.V);
        }
    }
    
    public static void v(String msg)
    {
    	if(null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.V)){
            Log.v(getDefaultTag(), msg);
        }
        if(allowFileLogPrint(LogLevel.V)){
            FileLogHelper.getInstance().logToFile(msg, null, getDefaultTag(), LogLevel.V);
        }
        
    }

    public static void v(String tag, String msg)
    {
    	if(null == tag || null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.V)){
            Log.v(tag, msg);
        }
        if(allowFileLogPrint(LogLevel.V)){
            FileLogHelper.getInstance().logToFile(msg, null, tag, LogLevel.V);
        }
        
    }
    
    public static void v(String tag, String msg, Object... args)
    {
    	if(null == tag || null == msg || null == sXLogConfig){
            return;
        }
    	
    	// 加上前缀, 并格式化字符串
    	try {
    		msg = String.format(msg, args);
    	} catch (Exception e1) {
    		Log.e(TAG, "log->exception:" + e1.getMessage());
    	}
    	
        if(allowConsoleLogPrint(LogLevel.V)){
            Log.v(tag, msg);
        }
        if(allowFileLogPrint(LogLevel.V)){
            FileLogHelper.getInstance().logToFile(msg, null, tag, LogLevel.V);
        }
        
    }

    public static void v(String tag, String msg, Throwable throwable)
    {
    	if(null == tag ||null == msg || null == throwable || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.V)){
            Log.v(tag, msg);
        }
        if(allowFileLogPrint(LogLevel.V)){
            FileLogHelper.getInstance().logToFile(msg, throwable, tag, LogLevel.V);
        }
        
    }
    /****          Log.d           ******/
    public static void d(String msg)
    {
        if(null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.D)){
            Log.d(getDefaultTag(), msg);
        }
        if(allowFileLogPrint(LogLevel.D)){
        	FileLogHelper.getInstance().logToFile(msg, null, getDefaultTag(), LogLevel.D);
        }
        
    }

    public static void d(String tag, String msg)
    {
       if(null == tag || null == msg ||null == sXLogConfig){
           return;
       }

       if(allowConsoleLogPrint(LogLevel.D)){
           Log.d(tag, msg);
       }
       if(allowFileLogPrint(LogLevel.D)){
    	   FileLogHelper.getInstance().logToFile(msg, null, tag, LogLevel.D);
       }
        
    }
    
    public static void d(String tag, String msg, Object... args)
    {
    	if(null == tag || null == msg || null == sXLogConfig){
            return;
        }
    	
    	// 加上前缀, 并格式化字符串
    	try {
    		msg = String.format(msg, args);
    	} catch (Exception e1) {
    		Log.e(TAG, "log->exception:" + e1.getMessage());
    	}
    	
        if(allowConsoleLogPrint(LogLevel.D)){
            Log.d(tag, msg);
        }
        if(allowFileLogPrint(LogLevel.D)){
            FileLogHelper.getInstance().logToFile(msg, null, tag, LogLevel.D);
        }
        
    }

    public static void d(String msg, Throwable throwable)
    {
        if(null == msg || null == throwable || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.D)){
            Log.d(getDefaultTag(), msg, throwable);
        }
        if(allowFileLogPrint(LogLevel.D)){
        	FileLogHelper.getInstance().logToFile(msg, throwable, getDefaultTag(), LogLevel.D);
        }
        
    }

    
    public static void d(String tag, String msg, Throwable throwable)
    {
        if(null == tag || null == msg || null == throwable || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.D)){
            Log.d(tag, msg, throwable);
        }
        if(allowFileLogPrint(LogLevel.D)){
        	FileLogHelper.getInstance().logToFile(msg, throwable, tag, LogLevel.D);
        }
        
    }

    /****          Log.i           ******/
    public static void i(String msg)
    {
        if(null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.I)){
            Log.i(getDefaultTag(), msg);
        }
        if(allowFileLogPrint(LogLevel.I)){
        	FileLogHelper.getInstance().logToFile(msg, null, getDefaultTag(), LogLevel.I);
        }
        
    }

    public static void i(String tag, String msg)
    {
    	if(null == tag || null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.I)){
            Log.i(tag, msg);
        }
        if(allowFileLogPrint(LogLevel.I)){
        	FileLogHelper.getInstance().logToFile(msg, null, tag, LogLevel.I);
        }
        
    }
    
    public static void i(String tag, String msg, Object... args)
    {
    	if(null == tag || null == msg || null == sXLogConfig){
            return;
        }
    	
    	// 加上前缀, 并格式化字符串
    	try {
    		msg = String.format(msg, args);
    	} catch (Exception e1) {
    		Log.e(TAG, "log->exception:" + e1.getMessage());
    	}
    	
        if(allowConsoleLogPrint(LogLevel.I)){
            Log.i(tag, msg);
        }
        if(allowFileLogPrint(LogLevel.I)){
            FileLogHelper.getInstance().logToFile(msg, null, tag, LogLevel.I);
        }
        
    }

    public static void i(String msg, Throwable throwable)
    {
    	if(null == throwable || null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.I)){
            Log.i(getDefaultTag(), msg, throwable);
        }
        if(allowFileLogPrint(LogLevel.I)){
        	FileLogHelper.getInstance().logToFile(msg, throwable, getDefaultTag(), LogLevel.I);
        }
        
    }

    public static void i(String tag, String msg, Throwable throwable)
    {
    	if(null == tag || null == throwable || null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.I)){
            Log.i(tag, msg, throwable);
        }
        if(allowFileLogPrint(LogLevel.I)){
        	FileLogHelper.getInstance().logToFile(msg, throwable, tag, LogLevel.I);
        }
        
    }
    /****          Log.w           ******/
    public static void w(String msg)
    {
    	if(null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.W)){
            Log.w(getDefaultTag(), msg);
        }
        if(allowFileLogPrint(LogLevel.W)){
        	FileLogHelper.getInstance().logToFile(msg, null, getDefaultTag(), LogLevel.W);
        }
        
    }

    public static void w(String tag, String msg)
    {
    	if(null == tag || null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.W)){
            Log.w(tag, msg);
        }
        if(allowFileLogPrint(LogLevel.W)){
        	FileLogHelper.getInstance().logToFile(msg, null, tag, LogLevel.W);
        }
        
    }
    
    public static void w(String tag, String msg, Object... args)
    {
    	if(null == tag || null == msg || null == sXLogConfig){
            return;
        }
    	
    	// 加上前缀, 并格式化字符串
    	try {
    		msg = String.format(msg, args);
    	} catch (Exception e1) {
    		Log.e(TAG, "log->exception:" + e1.getMessage());
    	}
    	
        if(allowConsoleLogPrint(LogLevel.W)){
            Log.w(tag, msg);
        }
        if(allowFileLogPrint(LogLevel.W)){
            FileLogHelper.getInstance().logToFile(msg, null, tag, LogLevel.W);
        }
        
    }

    public static void w(String msg, Throwable throwable)
    {
    	if(null == throwable || null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.W)){
            Log.w(getDefaultTag(), msg, throwable);
        }
        if(allowFileLogPrint(LogLevel.W)){
        	FileLogHelper.getInstance().logToFile(msg, throwable, getDefaultTag(), LogLevel.W);
        }
        
    }

    public static void w(String tag, String msg, Throwable throwable)
    {
    	if(null == tag || null == throwable || null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.W)){
            Log.w(tag, msg, throwable);
        }
        if(allowFileLogPrint(LogLevel.W)){
        	FileLogHelper.getInstance().logToFile(msg, throwable, tag, LogLevel.W);
        }
    }
    /****          Log.e           ******/
    public static void e(String msg)
    {
    	if(null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.E)){
            Log.w(getDefaultTag(), msg);
        }
        if(allowFileLogPrint(LogLevel.E)){
        	FileLogHelper.getInstance().logToFile(msg, null, getDefaultTag(), LogLevel.E);
        }
    }

    public static void e(String tag, String msg)
    {
    	if(null == tag || null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.E)){
            Log.w(tag, msg);
        }
        if(allowFileLogPrint(LogLevel.E)){
        	FileLogHelper.getInstance().logToFile(msg, null, tag, LogLevel.E);
        }
        
    }

    public static void e(String msg, Throwable throwable)
    {
    	if(null == throwable || null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.E)){
            Log.w(getDefaultTag(), msg, throwable);
        }
        if(allowFileLogPrint(LogLevel.E)){
        	FileLogHelper.getInstance().logToFile(msg, throwable, getDefaultTag(), LogLevel.E);
        }
        
    }

    public static void e(String tag, String msg, Throwable throwable)
    {
    	if(null == tag || null == throwable || null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.E)){
            Log.w(tag, msg, throwable);
        }
        if(allowFileLogPrint(LogLevel.E)){
        	FileLogHelper.getInstance().logToFile(msg, throwable, tag, LogLevel.E);
        }
        
    }
    
    public static void e(String tag, String msg, Throwable throwable, Object... args)
    {
    	if(null == throwable || null == tag || null == msg || null == sXLogConfig){
            return;
        }
    	
    	// 加上前缀, 并格式化字符串
    	try {
    		msg = String.format(msg, args);
    	} catch (Exception e1) {
    		Log.e(TAG, "log->exception:" + e1.getMessage());
    	}
    	
        if(allowConsoleLogPrint(LogLevel.E)){
            Log.e(tag, msg, throwable);
        }
        if(allowFileLogPrint(LogLevel.E)){
            FileLogHelper.getInstance().logToFile(msg, throwable, tag, LogLevel.E);
        }
        
    }
    
    /****          Log.wtf   What the FUCK        ******/
    public static void wtf(String msg)
    {
    	if(null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.WTF)){
            Log.wtf(getDefaultTag(), msg);
        }
        if(allowFileLogPrint(LogLevel.WTF)){
        	FileLogHelper.getInstance().logToFile(msg, null, getDefaultTag(), LogLevel.E);
        }
        
    }

    public static void wtf(String tag, String msg)
    {
    	if(null == tag || null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.WTF)){
            Log.wtf(tag, msg);
        }
        if(allowFileLogPrint(LogLevel.WTF)){
        	FileLogHelper.getInstance().logToFile(msg, null, tag, LogLevel.E);
        }
        
    }
    
    public static void wtf(String tag, String msg, Object... args)
    {
    	if(null == tag || null == msg || null == sXLogConfig){
            return;
        }
    	
    	// 加上前缀, 并格式化字符串
    	try {
    		msg = String.format(msg, args);
    	} catch (Exception e1) {
    		Log.e(TAG, "log->exception:" + e1.getMessage());
    	}
    	
        if(allowConsoleLogPrint(LogLevel.WTF)){
            Log.wtf(tag, msg);
        }
        if(allowFileLogPrint(LogLevel.WTF)){
            FileLogHelper.getInstance().logToFile(msg, null, tag, LogLevel.E);
        }
        
    }

    public static void wtf(String msg, Throwable throwable)
    {
    	if(null == throwable || null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.WTF)){
            Log.wtf(getDefaultTag(), msg);
        }
        if(allowFileLogPrint(LogLevel.WTF)){
        	FileLogHelper.getInstance().logToFile(msg, throwable, getDefaultTag(), LogLevel.E);
        }
        
    }

    public static void wtf(String tag, String msg, Throwable throwable)
    {
    	if(null == throwable || null == tag || null == msg || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.WTF)){
            Log.wtf(tag, msg,throwable);
        }
        if(allowFileLogPrint(LogLevel.WTF)){
        	FileLogHelper.getInstance().logToFile(msg, throwable, tag, LogLevel.E);
        }
        
    }
    
    /*project */ static void crash(String msg){
    	FileLogHelper.getInstance().logToFile(msg, null, null, LogLevel.E);
    }

    public static void startMethod(String tag, String method){
        if(null == tag || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.D)){
            Log.d(tag, "=== " + method  + " Start===");
        }
        if(allowFileLogPrint(LogLevel.D)){
            FileLogHelper.getInstance().logToFile("=== " + method  + " Start===", null, tag, LogLevel.D);
        }
    }

    public static void endMethod(String tag, String method){
        if(null == tag || null == sXLogConfig){
            return;
        }
        if(allowConsoleLogPrint(LogLevel.D)){
            Log.d(tag, "=== " + method  + " End===");
        }
        if(allowFileLogPrint(LogLevel.D)){
            FileLogHelper.getInstance().logToFile("=== " + method  + " End===", null, tag, LogLevel.D);
        }
    }

}
