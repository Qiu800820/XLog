package com.sum.xlog.util;

import android.util.Log;

import java.io.Closeable;

import static com.sum.xlog.util.FileUtil.DATE_PATTERN;


public class OtherUtil {

    private static final String TAG = "OtherUtil";
    private static final String METHOD_NAME = "printLog";


    public static String formatLog(String tag, String message, Throwable e, int logLevel) {

        // 获取日志时间
        String logTime = DateUtil.millis2String(System.currentTimeMillis(), DATE_PATTERN, false);

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

        if (null != e) {
            stringBuilder.append("\n");
            stringBuilder.append(Log.getStackTraceString(e));
        }

        return stringBuilder.toString();

    }



    /**
     * 得到异常所在代码位置信息
     */
    private static StackTraceElement getStackTraceElementInfo() {
        StackTraceElement stackTraceElement = null;
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        int index = 0;
        int size = traces != null?traces.length:0;
        for (int i = 0; i < size; i++) {
            StackTraceElement trace = traces[i];
            if (trace.getMethodName().contains(METHOD_NAME)) {
                index = i + 2;
                break;
            }
        }

        if(index < size){
            stackTraceElement = traces[index];
        }

        return stackTraceElement;
    }

    private static String getMethodPositionInfo() {
        StackTraceElement stackTraceElement = getStackTraceElementInfo();
        String methodPositionInfo = null;
        if(stackTraceElement != null){
            methodPositionInfo = String.format("%s : %s(%s)", stackTraceElement.getFileName(),
                    stackTraceElement.getMethodName(), stackTraceElement.getLineNumber());
        }
        return methodPositionInfo;
    }

    public static String getClassNameInfo(){
        StackTraceElement stackTraceElement = getStackTraceElementInfo();
        return stackTraceElement != null?stackTraceElement.getClassName():null;
    }

    public static String getMethodNameInfo() {
        StackTraceElement stackTraceElement = getStackTraceElementInfo();
        return stackTraceElement != null?stackTraceElement.getMethodName():null;
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
}
