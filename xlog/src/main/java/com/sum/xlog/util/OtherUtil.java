package com.sum.xlog.util;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.util.Date;


public class OtherUtil {
	
	public static final String TAG = "OtherUtil";
	public static final int SPACE_IS_NOT_ENOUGH = -1;

    public static String RUN_PACKAGE_NAME = "";
	
    /**
     * 
     * @Title getAvailableSpace
     * @Description 获取目录下可用空间
     * @param dir
     * @return
     * @Throws
     *
     */
    @SuppressLint("NewApi")
    public static long getAvailableSpace(File dir)
    {

        try
        {
            final StatFs stats = new StatFs(dir.getPath());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            {
                return stats.getBlockSizeLong() * stats.getAvailableBlocksLong();
            }
            else
            {
                return stats.getBlockSize() * stats.getAvailableBlocks();
            }
        }
        catch (Throwable e)
        {
            Log.e(TAG, e.getMessage(), e);
            return SPACE_IS_NOT_ENOUGH;
        }

    }
    
    
    public static String formatLog(String tag,String message, Throwable e, int logLevel){
        
    	// 获取日志时间
        String logTime = DateUtil.formatDate(new Date(),
                DateUtil.TIME_PATTERN_FLAG);
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(logTime);
        stringBuilder.append(",");
        stringBuilder.append(Thread.currentThread().getId());
        stringBuilder.append(",");
        stringBuilder.append(tag);
        stringBuilder.append(",");
        stringBuilder.append(getMethodPositionInfo());
        stringBuilder.append(",");
        stringBuilder.append(logLevel);
        stringBuilder.append(",");
        
        stringBuilder.append(message);
        
        if(null != e){
            stringBuilder.append("\n");
            stringBuilder.append(Log.getStackTraceString(e));
        }

        
        return stringBuilder.toString();
        
    }
    
    
    /**
     * @brief 得到异常所在代码的行数 如果没有行信息,返回-1
     */
    private static String getMethodPositionInfo() {
        String errorPositionMsg = null;
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        for(StackTraceElement trace : traces) {
            if(trace.getClassName().contains(RUN_PACKAGE_NAME)) {
                errorPositionMsg = String.format("\"%s : %s\"", trace.getFileName(),
                        trace.getMethodName());
            }
        }
        return errorPositionMsg;

    }
	
	
	/**
     * @brief 判断手机是否有SD卡
     * @return true表示有，false表示无
     */
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

	public static void closeQuietly(Closeable closeable){
	    
	    if(null != closeable){
	        
	        try
            {
                closeable.close();
            }
            catch (Exception e)
            {
                Log.e(TAG, e.getMessage(),e);
            }
	    }
	    
	}
	
	
	public static void closeQuietly(Cursor cursor){
        
        if(null != cursor){
            
            try
            {
                cursor.close();
            }
            catch (Exception e)
            {
                Log.e(TAG, e.getMessage(),e);
            }
        }
        
    }

}
