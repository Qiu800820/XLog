package com.sum.xlog.util;

import java.io.Closeable;
import java.io.File;
import java.util.Date;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;


public class OtherUtil {
	
	public static final String TAG = "OtherUtil";
	public static final int SPACE_IS_NOT_ENOUGH = -1;
	
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
    
    
    public static String formatLog(String tag,String message, Throwable e){
        
    	// 获取日志时间
        String logTime = DateUtil.formatDate(new Date(),
                DateUtil.TIME_PATTERN_FLAG);
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(logTime);
        
        stringBuilder.append(Thread.currentThread().getId());
        stringBuilder.append(tag);
        stringBuilder.append(getLineNumber(e));
        
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
    private static int getLineNumber(Throwable e) {
        int lineNum = -1;
        if (e != null) {
            StackTraceElement[] trace = e.getStackTrace();
            if (trace != null && trace.length > 0)
                lineNum = trace[0].getLineNumber();
        }
        return lineNum;

    }
	
	
	/**
     * @brief 判断手机是否有SD卡
     * @return true表示有，false表示无
     */
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
    
	public static StackTraceElement getCurrentStackTraceElement() {
		return Thread.currentThread().getStackTrace()[3];
	}

	public static StackTraceElement getCallerStackTraceElement() {
		return Thread.currentThread().getStackTrace()[4];
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
