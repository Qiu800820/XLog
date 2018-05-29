package com.sum.xlog.util;

import android.os.Process;
import android.util.Log;

import com.sum.xlog.print.LogLevel;

import java.io.Closeable;

import static com.sum.xlog.print.XLogPrinterImpl.METHOD_NAME;


public class OtherUtil {

    private static final String TAG = "OtherUtil";


    public static String formatLog(String tag, String message, Throwable e, int logLevel) {

        String logTime = DateUtil.millis2String(System.currentTimeMillis(), "HH:mm:ss.SSS", false);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(logTime);
        stringBuilder.append(",");
        stringBuilder.append(Process.myPid());
        stringBuilder.append(",");
        stringBuilder.append(tag);
        stringBuilder.append(",");
        stringBuilder.append(getMethodPositionInfo());
        stringBuilder.append(",");
        stringBuilder.append(LogLevel.level2String(logLevel));
        if (message != null) {
            stringBuilder.append(",");
            stringBuilder.append(message);
        }


        if (null != e) {
            stringBuilder.append("\n");
            stringBuilder.append(Log.getStackTraceString(e));
        }

        return stringBuilder.toString();

    }


    /**
     * 得到异常所在代码位置信息
     */
    private static StackTraceElement getStackTraceElementInfo(String stackMethod, int stackIndex) {
        StackTraceElement stackTraceElement = null;
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        int index = 0;
        int size = traces != null ? traces.length : 0;
        for (int i = 0; i < size; i++) {
            StackTraceElement trace = traces[i];
            if (trace.getMethodName().contains(stackMethod)) {
                index = i + stackIndex;
                break;
            }
        }

        if (index < size) {
            stackTraceElement = traces[index];
        }

        return stackTraceElement;
    }

    private static String getMethodPositionInfo() {
        StackTraceElement stackTraceElement = getStackTraceElementInfo(METHOD_NAME, 3);
        String methodPositionInfo = null;
        if (stackTraceElement != null) {
            methodPositionInfo = String.format("%s(%s:%s)",
                    stackTraceElement.getMethodName(),
                    stackTraceElement.getFileName(),
                    stackTraceElement.getLineNumber());
        }
        return methodPositionInfo;
    }

    public static String getClassNameInfo(String stackMethod, int stackIndex) {
        StackTraceElement stackTraceElement = getStackTraceElementInfo(stackMethod, stackIndex);
        return stackTraceElement != null?getSimpleFileName(stackTraceElement.getFileName()):null;
    }

    public static String getMethodNameInfo(String stackMethod, int stackIndex) {
        StackTraceElement stackTraceElement = getStackTraceElementInfo(stackMethod, stackIndex);
        return stackTraceElement != null ? stackTraceElement.getMethodName() : null;
    }

    public static void closeQuietly(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    public static String getSimpleFileName(String fileName) {
        if (fileName == null || fileName.length() == 0)
            return "Unknown";
        int index = fileName.indexOf(".");
        if (index > 0) {
            return fileName.substring(0, index);
        }
        return fileName;
    }
}
