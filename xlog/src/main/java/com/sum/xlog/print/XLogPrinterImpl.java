package com.sum.xlog.print;

import android.util.Log;

import com.sum.xlog.core.FileLogHelper;
import com.sum.xlog.core.XLogConfiguration;
import static com.sum.xlog.util.OtherUtil.getClassNameInfo; 
import static com.sum.xlog.util.OtherUtil.getMethodNameInfo; 

/**
 * Created by Sen on 2018/4/18.
 */

public class XLogPrinterImpl implements XLogPrinter {

    private XLogConfiguration xLogConfiguration;
    private static final String TAG = "XLogPrinterImpl";
    private static final String METHOD_END_MSG = "=== onMethod End ===";
    private static final String METHOD_START_MSG = "=== onMethod Start ===";

    public XLogPrinterImpl(XLogConfiguration xLogConfiguration){
        this.xLogConfiguration = xLogConfiguration;
    }

    @Override
    public void v(String msg, Object... args) {
        printLog(getClassNameInfo(), String.format(msg, args), LogLevel.V, null);
    }

    @Override
    public void v(String msg, Throwable throwable) {
        printLog(getClassNameInfo(), msg, LogLevel.V, throwable);
    }

    @Override
    public void d(String msg, Object... args) {
        printLog(getClassNameInfo(), String.format(msg, args), LogLevel.D, null);
    }

    @Override
    public void d(String msg, Throwable throwable) {
        printLog(getClassNameInfo(), msg, LogLevel.D, throwable);
    }

    @Override
    public void i(String msg, Object... args) {
        printLog(getClassNameInfo(), String.format(msg, args), LogLevel.I, null);
    }

    @Override
    public void i(String msg, Throwable throwable) {
        printLog(getClassNameInfo(), msg, LogLevel.I, throwable);
    }

    @Override
    public void w(String msg, Object... args) {
        printLog(getClassNameInfo(), String.format(msg, args), LogLevel.W, null);
    }

    @Override
    public void w(String msg, Throwable throwable) {
        printLog(getClassNameInfo(), msg, LogLevel.W, throwable);
    }

    @Override
    public void e(String msg, Throwable throwable, Object... args) {
        printLog(getClassNameInfo(), String.format(msg, args), LogLevel.E, throwable);
    }

    @Override
    public void wtf(String msg, Object... args) {
        printLog(getClassNameInfo(), String.format(msg, args), LogLevel.WTF, null);
    }

    @Override
    public void wtf(String msg, Throwable throwable) {
        printLog(getClassNameInfo(), msg, LogLevel.WTF, throwable);
    }

    @Override
    public void crash(String msg) {
        Log.e(TAG, msg);
        // todo send commend to LogService
        FileLogHelper.getInstance().logToFile(msg, null, null, LogLevel.E, true);
    }

    @Override
    public void startMethod() {
        printLog(getMethodNameInfo(), METHOD_START_MSG, LogLevel.D, null);
    }

    @Override
    public void endMethod() {
        printLog(getMethodNameInfo(), METHOD_END_MSG, LogLevel.D, null);
    }

    private void printLog(String tag, String msg, int logLevel, Throwable throwable){
        if(msg == null){
            return;
        }
        if (allowConsoleLogPrint(logLevel)) {
            switch (logLevel){
                case LogLevel.V:
                    Log.v(getMethodNameInfo(), msg, throwable);
                    break;
                case LogLevel.D:
                    Log.d(getMethodNameInfo(), msg, throwable);
                    break;
                case LogLevel.I:
                    Log.i(getMethodNameInfo(), msg, throwable);
                    break;
                case LogLevel.W:
                    Log.w(getMethodNameInfo(), msg, throwable);
                    break;
                case LogLevel.E:
                    Log.e(getMethodNameInfo(), msg, throwable);
                    break;
                case LogLevel.WTF:
                    Log.wtf(getMethodNameInfo(), msg, throwable);
                    break;
                default:
                    Log.d(getMethodNameInfo(), msg, throwable);
                    break;
            }
        }
        if (allowFileLogPrint(logLevel)) {
            // todo send commend to LogService
            FileLogHelper.getInstance().logToFile(msg, throwable, tag, logLevel);
        }
    }

    private boolean allowConsoleLogPrint(int printLevel) {
        return xLogConfiguration.getConsoleLogLevel() <= printLevel;
    }

    private boolean allowFileLogPrint(int printLevel) {
        return xLogConfiguration.getFileLogLevel() <= printLevel;
    }




}
