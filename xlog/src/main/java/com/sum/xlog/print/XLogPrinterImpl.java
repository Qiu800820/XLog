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
    public static final String METHOD_NAME = "printLog";
    private static final String METHOD_END_MSG_FORMAT = "=== %s method end ===";
    private static final String METHOD_START_MSG_FORMAT = "=== %s method start ===";

    public XLogPrinterImpl(XLogConfiguration xLogConfiguration){
        this.xLogConfiguration = xLogConfiguration;
    }

    @Override
    public void v(String msg, Object... args) {
        printLog(String.format(msg, args), LogLevel.V, null);
    }

    @Override
    public void v(String msg, Throwable throwable) {
        printLog(msg, LogLevel.V, throwable);
    }

    @Override
    public void d(String msg, Object... args) {
        printLog(String.format(msg, args), LogLevel.D, null);
    }

    @Override
    public void d(String msg, Throwable throwable) {
        printLog(msg, LogLevel.D, throwable);
    }

    @Override
    public void i(String msg, Object... args) {
        printLog(String.format(msg, args), LogLevel.I, null);
    }

    @Override
    public void i(String msg, Throwable throwable) {
        printLog(msg, LogLevel.I, throwable);
    }

    @Override
    public void w(String msg, Object... args) {
        printLog(String.format(msg, args), LogLevel.W, null);
    }

    @Override
    public void w(String msg, Throwable throwable) {
        printLog(msg, LogLevel.W, throwable);
    }

    @Override
    public void e(Throwable throwable) {
        printLog(null, LogLevel.E, throwable);
    }

    @Override
    public void e(String msg, Object... args) {
        printLog(String.format(msg, args), LogLevel.E, null);
    }

    @Override
    public void e(String msg, Throwable throwable, Object... args) {
        printLog(String.format(msg, args), LogLevel.E, throwable);
    }

    @Override
    public void wtf(String msg, Object... args) {
        printLog(String.format(msg, args), LogLevel.WTF, null);
    }

    @Override
    public void wtf(String msg, Throwable throwable) {
        printLog(msg, LogLevel.WTF, throwable);
    }

    @Override
    public void crash(String msg) {
        FileLogHelper.getInstance().logToFile(msg, null, null, LogLevel.E, true);
    }

    @Override
    public void startMethod() {
        printLog(String.format(METHOD_START_MSG_FORMAT, getMethodNameInfo("startMethod", 2)),
                LogLevel.D, null);
    }

    @Override
    public void endMethod() {
        printLog(String.format(METHOD_END_MSG_FORMAT, getMethodNameInfo("endMethod", 2)),
                LogLevel.D, null);
    }

    private void printLog(String msg, int logLevel, Throwable throwable){
        if(msg == null && logLevel != LogLevel.E){
            return;
        }
        String tag = getClassNameInfo(METHOD_NAME, 3);
        if (allowConsoleLogPrint(logLevel)) {
            switch (logLevel){
                case LogLevel.V:
                    Log.v(tag, msg, throwable);
                    break;
                case LogLevel.D:
                    Log.d(tag, msg, throwable);
                    break;
                case LogLevel.I:
                    Log.i(tag, msg, throwable);
                    break;
                case LogLevel.W:
                    Log.w(tag, msg, throwable);
                    break;
                case LogLevel.E:
                    Log.e(tag, msg, throwable);
                    break;
                case LogLevel.WTF:
                    Log.wtf(tag, msg, throwable);
                    break;
                default:
                    Log.d(tag, msg, throwable);
                    break;
            }
        }
        if (allowFileLogPrint(logLevel)) {
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
