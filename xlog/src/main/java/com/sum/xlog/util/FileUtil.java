package com.sum.xlog.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.sum.xlog.core.XLog;
import com.sum.xlog.core.XLogConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 
 * @ClassName FileUtil
 * @Description 文件读写工具类
 * @Author Yao
 * @Date 2015年8月12日 下午5:05:26
 * @Version v1.0
 */
public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    
    public static String getSdcardPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
    
    public static String getTodayLogFileName(){
        Date nowTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        return simpleDateFormat.format(nowTime);
    }
    
    public static String getXLogPath(){
    	String path = null;
    	
    	XLogConfiguration xLogConfiguration = XLog.getXLogConfiguration();
    	
    	if(xLogConfiguration == null)
    		return null;
    	
    	String fileDirName = xLogConfiguration.getFileLogDirName();
        String fileOwenName = xLogConfiguration.getFileLogOwner();
        StringBuilder dirPathBuilder = new StringBuilder();
        dirPathBuilder.append(getSdcardPath());
        dirPathBuilder.append(File.separator);
        dirPathBuilder.append(fileDirName);
        dirPathBuilder.append(File.separator);
        dirPathBuilder.append(fileOwenName);
        
        path = dirPathBuilder.toString();
    	return path;
    }
    
    @SuppressLint("NewApi")
    public static boolean logFileDirSpaceMax(File dir , long max)
    {
        
        boolean result = false;
        
        try
        {
            final StatFs stats = new StatFs(dir.getPath());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            {
               result =  stats.getBlockSizeLong() >= max ;
            }
            else
            {
               result =  stats.getBlockSize() >= max;
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage(), e);
        }
        
        return result;
    }
    
    
    public static void delAllFileByDir(File dir){
        
        if(null == dir || !dir.isDirectory()){
            return;
        }
        File [] files = dir.listFiles();
        
        if(null == files || files.length == 0){
            
            return;
        }
        
        for (File file : files)
        {
            file.delete();
        }
        
    }

    /**
     * 删除过期日志
     */
    public static void delOutDateFile(File fileDir, int saveDays)
    {
        
        if(null == fileDir || !fileDir.isDirectory() || saveDays <= 0){
            return;
        }
        
        File [] files = fileDir.listFiles();
        
        if(null == files || files.length == 0){
            return;
        }
        for (File file : files)
        {
            String dateString = file.getName();
            if(canDeleteSDLog(dateString, saveDays)){
                file.delete();
            }
        }
    }
    
    /**
     * 条件删除日志
     */
    public static boolean delFilterFile(File fileDir, String filter)
    {
        
        if(null == fileDir || !fileDir.isDirectory()){
            return false;
        }
        
        File [] files = fileDir.listFiles();
        
        if(null == files || files.length == 0){
            return false;
        }
        for (File file : files)
        {
            String name = file.getName();
            if(TextUtils.isEmpty(filter) || name.contains(filter))
            	file.delete();
            
        }
        return true;
    }
    
    
    /**
     * @brief 判断sdcard上的日志文件是否可以删除
     * @param createDateStr
     *            日期串
     * @return true表示可删除，false表示否
     */
    public static boolean canDeleteSDLog(String createDateStr , int saveDays) {
        Calendar calendar = Calendar.getInstance();
        // 删除LOG_SAVE_DAYS天之前日志
        calendar.add(Calendar.DAY_OF_MONTH, -1 * saveDays);
        Date expiredDate = calendar.getTime();
        Date createDate = DateUtil.parseDate(createDateStr,
                DateUtil.DATE_PATTERN_FLAG);
        return createDate != null ? 
                createDate.before(expiredDate) : false;
    }
    
}
