package com.sum.xlog.core;

import android.util.Log;

import com.sum.xlog.util.FileUtil;
import com.sum.xlog.util.OtherUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class FileLogHelper
{
    public static final String TAG = "FileLogHelper";
    public static final int LOG_CACHE_POLL_SIZE = 30;
    private List<String> logCache = null;
    private ExecutorService mExecutorService;
    private ReentrantLock mReentrantLock;
    private boolean relased = false;
    public static FileLogHelper INSTANCE = null;
    

    public static FileLogHelper getInstance(){
        
        if(null == INSTANCE){
            synchronized (FileLogHelper.class){
                if(null == INSTANCE){
                    INSTANCE = new FileLogHelper();
                }
            }
        }
        return INSTANCE;
    }
    
    public FileLogHelper()
    {
        logCache = new ArrayList<String>(LOG_CACHE_POLL_SIZE);
        mExecutorService = Executors.newSingleThreadExecutor();
        mReentrantLock = new ReentrantLock();
    }
    
    public void logToFile(String log, Throwable e, String tag){
        
        if(relased){
            return;
        }
        
        String logMsg = OtherUtil.formatLog(tag, log, e);
        
        addLogTocache(logMsg);
        if(getCacheSize() >= LOG_CACHE_POLL_SIZE){
            
            mExecutorService.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    saveToFile();
                }
            });
        }
    }

    private void saveToFile()
    {
        String logFilePath = initLogFile();
        
        if (null != logFilePath && logFilePath.trim().length() > 0)
        {
            
            
            BufferedWriter bw = null;
            mReentrantLock.lock();
            try
            {
                bw = new BufferedWriter(new FileWriter(logFilePath, true), 1024);
                for (String log : logCache) {
                    bw.write(log);
                    bw.newLine();
                }
            }
            catch (Exception e)
            {
               Log.e(TAG, e.getMessage(),e);
            
            }finally
            {
                mReentrantLock.unlock();
                OtherUtil.closeQuietly(bw);
            }
        }
        
        clearCache(false);
    }
    
    
    //使用配置完成
    private String initLogFile()
    {
        String result = null;
        
        if(!OtherUtil.hasSDCard()){
            return result;
        }
        
        XLogConfiguration xLogConfiguration = XLog.getXLogConfiguration();
        
        try
        {
            String fileDirName = xLogConfiguration.getFileLogDirName();
            String fileOwenName = xLogConfiguration.getFileLogOwner();
            StringBuilder dirPathBuilder = new StringBuilder();
            dirPathBuilder.append(FileUtil.getSdcardPath());
            dirPathBuilder.append(File.separator);
            dirPathBuilder.append(fileDirName);
            dirPathBuilder.append(File.separator);
            dirPathBuilder.append(fileOwenName);
            
            File fileDir = new File(dirPathBuilder.toString());
            
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }
            
            FileUtil.delOutDateFile(fileDir,xLogConfiguration.getFileLogRetentionPeriod());
            
            boolean logFileDirSpaceMax = FileUtil.logFileDirSpaceMax(fileDir, xLogConfiguration.getFileLogDiskMemorySize());
            
            if(logFileDirSpaceMax){
                FileUtil.delAllFileByDir(fileDir);
            }
            
            StringBuilder fileNameBuilder = new StringBuilder();
            fileNameBuilder.append(dirPathBuilder.toString());
            fileNameBuilder.append(File.separator);
            fileNameBuilder.append(FileUtil.getTodayLogFileName());
            fileNameBuilder.append(xLogConfiguration.getLogFileSuffix());
            
            File file = new File(fileNameBuilder.toString());
            
            if(!file.exists()){
                file.createNewFile();
            }
            
            result = file.getAbsolutePath();
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage(),e);
        }
        
        return result;
    }
    
    //释放资源
    public void relase()
    {
        relased = true;
        if(null != mExecutorService && !mExecutorService.isShutdown()){
            mExecutorService.shutdown();
        }
        mExecutorService = null;
        relaseCache();
        if(mReentrantLock.isLocked())
            mReentrantLock.unlock();
    }
    
    
    public void addLogTocache(String log){
        
        if(null == logCache){
            return;
        }
        
        mReentrantLock.lock();
        try
        {
            logCache.add(log);
        }finally{
            mReentrantLock.unlock();
        }
    }
    
    
    public void relaseCache(){
        clearCache(true);
    }
    
    public void clearCache(boolean relase){
        
        mReentrantLock.lock();
        try
        {
            logCache.clear();
            if(relase){
                logCache = null;
            }
        }finally{
            mReentrantLock.unlock();
        }
    }
    
    
    public int getCacheSize(){
        
        if(null == logCache){
            return 0;
        }
        
        mReentrantLock.lock();
        
        try
        {
            return logCache.size();
        }finally{
            mReentrantLock.unlock();
        }
        
    }

}
