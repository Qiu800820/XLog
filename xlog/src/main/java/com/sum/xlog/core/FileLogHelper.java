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

public class FileLogHelper {
    private static final String TAG = "FileLogHelper";
    private static final int LOG_CACHE_POLL_SIZE = 20;
    private List<String> logCache = null;
    private ExecutorService mExecutorService;
    private ReentrantLock mReentrantLock;
    private boolean released = false;
    private static FileLogHelper INSTANCE = null;


    public FileLogHelper() {
        logCache = new ArrayList<String>(LOG_CACHE_POLL_SIZE);
        mExecutorService = Executors.newSingleThreadExecutor();
        mReentrantLock = new ReentrantLock();
    }

    public static FileLogHelper getInstance() {
        if (null == INSTANCE) {
            synchronized (FileLogHelper.class) {
                if (null == INSTANCE) {
                    INSTANCE = new FileLogHelper();
                }
            }
        }
        return INSTANCE;
    }

    public void logToFile(String log, Throwable e, String tag, int logLevel) {
        logToFile(log, e, tag, logLevel, false);
    }

    public void logToFile(String log, Throwable e, String tag, int logLevel, boolean isForceSave) {
        if (released) {
            return;
        }

        String logMsg = OtherUtil.formatLog(tag, log, e, logLevel);

        addLogToCache(logMsg);
        if (getCacheSize() >= LOG_CACHE_POLL_SIZE || isForceSave) {

            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    saveToFile();
                }
            });
        }
    }

    private void saveToFile() {
        String logFilePath = initLogFile();

        if (null != logFilePath && logFilePath.trim().length() > 0) {
            BufferedWriter bw = null;
            mReentrantLock.lock();
            try {
                bw = new BufferedWriter(new FileWriter(logFilePath, true), 1024);
                for (String log : logCache) {
                    bw.write(log);
                    bw.newLine();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);

            } finally {
                mReentrantLock.unlock();
                OtherUtil.closeQuietly(bw);
            }
        }
        clearCache();
    }


    /**
     * 初始化Log文件路径
     *
     * @return String Log文件路径
     */
    private String initLogFile() {
        String result = null;

        try {
            File fileDir = FileUtil.getXLogDirFile();
            if(fileDir != null && !fileDir.exists()){
                fileDir.mkdirs();
            }

            File file = FileUtil.getTodayLogFile();
            if (file != null && !file.exists()) {
                file.createNewFile();
                result = file.getAbsolutePath();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return result;
    }

    //释放资源
    public void destroy() {
        released = true;
        if (null != mExecutorService && !mExecutorService.isShutdown()) {
            mExecutorService.shutdown();
        }
        mExecutorService = null;
        clearCache();
        if (mReentrantLock.isLocked())
            mReentrantLock.unlock();
    }


    private void addLogToCache(String log) {

        if (null == logCache) {
            return;
        }

        mReentrantLock.lock();
        try {
            logCache.add(log);
        } finally {
            mReentrantLock.unlock();
        }
    }

    private void clearCache() {

        mReentrantLock.lock();
        try {
            logCache.clear();
            if (released) {
                logCache = null;
            }
        } finally {
            mReentrantLock.unlock();
        }
    }


    private int getCacheSize() {

        if (null == logCache) {
            return 0;
        }
        mReentrantLock.lock();
        try {
            return logCache.size();
        } finally {
            mReentrantLock.unlock();
        }

    }

    public void deleteExpireFile(){
        XLogConfiguration xLogConfiguration = XLog.getXLogConfiguration();
        if(xLogConfiguration == null)
            return;
        File fileDir = FileUtil.getXLogDirFile();
        FileUtil.delOutDateFile(fileDir, xLogConfiguration.getFileLogRetentionPeriod());

        boolean logFileDirSpaceMax = FileUtil.logFileDirSpaceMax(fileDir, xLogConfiguration.getFileLogDiskMemorySize());
        if (logFileDirSpaceMax) {
            FileUtil.delAllFileByDir(fileDir);
        }
    }

}
