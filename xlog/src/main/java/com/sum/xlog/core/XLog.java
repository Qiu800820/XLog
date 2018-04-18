package com.sum.xlog.core;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sum.xlog.crash.CrashExceptionLogger;
import com.sum.xlog.crash.CrashHandler;
import com.sum.xlog.print.XLogPrinter;
import com.sum.xlog.print.XLogPrinterImpl;
import com.sum.xlog.util.FileUtil;

import java.io.File;


/**
 *
 */
public class XLog {

    private static final String CONFIG_NOT_NULL = "XLogConfiguration can not be initialized with null";
    private static final String TAG = "XLog";
    private static XLogConfiguration xLogConfig;
    private static XLogPrinter xLogPrinter;

    private XLog() {
    }


    public static void init(Context context) {
        init(context, XLogConfiguration.createDefault(context));

    }

    public static void init(Context context, XLogConfiguration configuration) {

        if (null == configuration) {
            throw new IllegalArgumentException(CONFIG_NOT_NULL);
        }

        if (xLogConfig == null) {
            Log.d(TAG, "Initialize XLog with configuration");
            xLogConfig = configuration;
            Intent intent = new Intent(context, LogService.class);
            context.startService(intent);
            if (xLogConfig.isCrashHandlerOpen()) {
                CrashHandler.getInstance().init(xLogConfig.getOriginalHandler());
                CrashHandler.getInstance().setCaughtCrashExceptionListener(new CrashExceptionLogger(context));
            }

            if(xLogPrinter == null){
                xLogPrinter = new XLogPrinterImpl(xLogConfig);
            }
        } else {
            Log.w(TAG, "Try to initialize XLog which had already been initialized before");
        }

    }

    /**
     * 手动销毁LogService
     */
    public static void destroy(Context context) {
        xLogConfig = null;
        Intent intent = new Intent(context, LogService.class);
        context.stopService(intent);
    }

    /**
     * 手动删除日志文件
     * @param fileNameFilter 过滤文件
     */
    public static boolean clearFileLog(String fileNameFilter) {
        File file = FileUtil.getXLogDirFile();
        return file != null && file.exists() && FileUtil.delFilterFile(file, fileNameFilter);
    }

    public static XLogConfiguration getXLogConfiguration() {
        return xLogConfig;
    }

    public static void v(String msg, Object... args){
        xLogPrinter.v(msg, args);
    }

    public static void v(String msg, Throwable throwable){
        xLogPrinter.v(msg, throwable);
    }

    public static void d(String msg, Object... args){
        xLogPrinter.d(msg, args);
    }

    public static void d(String msg, Throwable throwable){
        xLogPrinter.d(msg, throwable);
    }

    public static void i(String msg, Object... args){
        xLogPrinter.i(msg, args);
    }

    public static void i(String msg, Throwable throwable){
        xLogPrinter.i(msg, throwable);
    }

    public static void w(String msg, Object... args){
        xLogPrinter.w(msg, args);
    }

    public static void w(String msg, Throwable throwable){
        xLogPrinter.w(msg, throwable);
    }

    public static void e(String msg, Throwable throwable, Object... args){
        xLogPrinter.e(msg, throwable, args);
    }

    public static void wtf(String msg, Object... args){
        xLogPrinter.wtf(msg, args);
    }

    public static void wtf(String msg, Throwable throwable){
        xLogPrinter.wtf(msg, throwable);
    }

    public static void crash(String msg){
        xLogPrinter.crash(msg);
    }

    public static void startMethod(){
        xLogPrinter.startMethod();
    }

    public static void endMethod(){
        xLogPrinter.endMethod();
    }

}
