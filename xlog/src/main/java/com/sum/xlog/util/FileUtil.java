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
import java.util.Locale;


/**
 * 日志文件操作工具类
 */
public class FileUtil {
    private static final String TAG = "FileUtil";
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    private FileUtil() {
    }

    public static String getSdcardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static File getTodayLogFile() {
        XLogConfiguration xLogConfiguration = XLog.getXLogConfiguration();
        File todayLogFile = null;
        if(xLogConfiguration != null){
            todayLogFile = new File(getXLogDirFile(), getTodayLogFileName() + xLogConfiguration.getLogFileSuffix());
        }
        return todayLogFile;
    }

    private static String getTodayLogFileName() {
        Date nowTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.CHINA);
        return simpleDateFormat.format(nowTime);
    }

    public static File getXLogDirFile() {
        XLogConfiguration xLogConfiguration = XLog.getXLogConfiguration();
        if (xLogConfiguration == null)
            return null;

        String fileRootPath = xLogConfiguration.getFileLogRootPath();
        if(fileRootPath == null || fileRootPath.length() == 0)
            fileRootPath = FileUtil.getSdcardPath();
        String fileDirName = xLogConfiguration.getFileLogDirName();

        return new File(fileRootPath, fileDirName);
    }

    @SuppressLint("NewApi")
    public static boolean logFileDirSpaceMax(File dir, long max) {

        boolean result = false;

        try {
            final StatFs stats = new StatFs(dir.getPath());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                result = stats.getBlockSizeLong() >= max;
            } else {
                result = stats.getBlockSize() >= max;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return result;
    }


    public static void delAllFileByDir(File dir) {

        if (null == dir || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles();

        if (null == files || files.length == 0) {

            return;
        }

        for (File file : files) {
            file.delete();
        }

    }

    /**
     * 删除过期日志
     */
    public static void delOutDateFile(File fileDir, int saveDays) {

        if (null == fileDir || !fileDir.isDirectory() || saveDays <= 0) {
            return;
        }

        File[] files = fileDir.listFiles();

        if (null == files || files.length == 0) {
            return;
        }
        for (File file : files) {
            String dateString = file.getName();
            if (canDeleteSDLog(dateString, saveDays)) {
                file.delete();
            }
        }
    }

    /**
     * 条件删除日志
     */
    public static boolean delFilterFile(File fileDir, String filter) {

        if (null == fileDir || !fileDir.isDirectory()) {
            return false;
        }

        File[] files = fileDir.listFiles();

        if (null == files || files.length == 0) {
            return false;
        }
        for (File file : files) {
            String name = file.getName();
            if (TextUtils.isEmpty(filter) || name.contains(filter))
                file.delete();

        }
        return true;
    }

    /**
     * 判断sdcard上的日志文件是否可以删除
     * @param createDateStr 日期串
     * @return true表示可删除，false表示否
     */
    private static boolean canDeleteSDLog(String createDateStr, int saveDays) {
        Calendar calendar = Calendar.getInstance();
        // 删除LOG_SAVE_DAYS天之前日志
        calendar.add(Calendar.DAY_OF_MONTH, -1 * saveDays);
        Date expiredDate = calendar.getTime();
        Date createDate = new Date(DateUtil.string2Millis(createDateStr, DATE_PATTERN, false));
        return createDate.before(expiredDate);
    }

}
