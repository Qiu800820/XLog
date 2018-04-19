package com.sum.xlog.core;

import android.content.Context;

import com.sum.xlog.crash.OnCrashInfoListener;
import com.sum.xlog.print.LogLevel;

import java.lang.Thread.UncaughtExceptionHandler;

public class XLogConfiguration {

    /**
     * 默认控制台输出日志级别
     */
    private static final byte DEFAULT_CONSOLE_LOG_LEVEL = LogLevel.V;
    /**
     * 默认存储至文件日志级别
     */
    private static final byte DEFAULT_FILE_LOG_LEVEL = LogLevel.D;
    /**
     * 默认LOG文件过期时间
     */
    private static final int DEFAULT_FILE_LOG_RETENTION_PERIOD = 7;
    /**
     * 默认LOG文件大小KB单位
     */
    private static final long DEFAULT_FILE_LOG_DISK_MEMORY_SIZE = 1024 * 100L; //100m
    /**
     * 默认文件目录名称
     */
    private static final String DEFAULT_FILE_LOG_DIR_NAME = "XLog";
    /**
     * 默认日志文件后缀
     */
    private static final String DEFAULT_LOG_FILE_SUFFIX = ".log";
    /**
     * 符号 "."
     */
    private static final String CHAR_POINT = ".";

    /***
     * 控制台日志输出级别，大于等于改级别的日志都将被输出到控制台
     */
    private byte consoleLogLevel;
    /**
     * 文件日志输出级别，大于等于改级别的日志都将被输出到文件
     */
    private byte fileLogLevel;
    /**
     * 文件日志保存天数，如果超过这个值的文件会被删除掉
     */
    private int fileLogRetentionPeriod;
    /**
     * 日志文件存储路径
     */
    private String fileLogRootPath;
    /**
     * 文件日志在记忆体中的存储最大空间
     */
    private long fileLogDiskMemorySize;
    /**
     * 设置日志文件夹名称
     */
    private String fileLogDirName;
    /**
     * 日志存放目标文件的后缀名 比如 " .log  , .txt "
     */
    private String logFileSuffix;
    /**
     * 是否开启crashHandler ,如果不开启的话不会将crash日志写入到文件
     */
    private boolean crashHandlerOpen = true;
    /**
     * UncaughtException处理类
     */
    private UncaughtExceptionHandler originalHandler = null;

    private OnCrashInfoListener mOnCrashInfoListener;


    XLogConfiguration(Builder builder) {
        this.consoleLogLevel = builder.consoleLogLevel;
        this.fileLogDiskMemorySize = builder.fileLogDiskMemorySize;
        this.fileLogLevel = builder.fileLogLevel;
        this.fileLogRetentionPeriod = builder.fileLogRetentionPeriod;
        this.fileLogDirName = builder.fileLogDirName;
        this.logFileSuffix = builder.logFileSuffix;
        this.crashHandlerOpen = builder.crashHandlerOpen;
        this.originalHandler = builder.originalHandler;
        this.mOnCrashInfoListener = builder.mOnCrashInfoListener;
        this.fileLogRootPath = builder.fileLogRootPath;

    }

    static XLogConfiguration createDefault(Context context) {
        return new Builder(context).build();
    }

    public byte getConsoleLogLevel() {
        return consoleLogLevel;
    }

    public byte getFileLogLevel() {
        return fileLogLevel;
    }

    public int getFileLogRetentionPeriod() {
        return fileLogRetentionPeriod;
    }

    public long getFileLogDiskMemorySize() {
        return fileLogDiskMemorySize;
    }

    public String getFileLogDirName() {
        return fileLogDirName;
    }

    public String getFileLogRootPath() {
        return fileLogRootPath;
    }

    public String getLogFileSuffix() {
        return logFileSuffix;
    }

    public boolean isCrashHandlerOpen() {
        return crashHandlerOpen;
    }

    public UncaughtExceptionHandler getOriginalHandler() {
        return originalHandler;
    }

    public OnCrashInfoListener getOnCrashInfoListener() {
        return mOnCrashInfoListener;
    }

    public static class Builder {
        private byte consoleLogLevel = -1;
        private byte fileLogLevel = -1;
        private int fileLogRetentionPeriod;
        private long fileLogDiskMemorySize;
        private String fileLogDirName;
        private String logFileSuffix;
        private boolean crashHandlerOpen;
        private UncaughtExceptionHandler originalHandler = null;
        private OnCrashInfoListener mOnCrashInfoListener = null;
        private String fileLogRootPath;

        public Builder(Context context){
            setFileLogDirName(context.getPackageName());
            setFileLogRootPath(context.getFilesDir().getAbsolutePath());
        }

        /**
         * 设置其他异常处理类，主要兼容统计(可不设置)
         * @param originalHandler 原始异常处理类 {@link UncaughtExceptionHandler}
         */
        public Builder setOriginalHandler(UncaughtExceptionHandler originalHandler) {
            this.originalHandler = originalHandler;
            return this;
        }

        /**
         * 设置Crash用户上传日志信息
         * @param mOnCrashInfoListener 用户上传事件{@link OnCrashInfoListener}
         */
        public Builder setOnCrashInfoListener(OnCrashInfoListener mOnCrashInfoListener) {
            this.mOnCrashInfoListener = mOnCrashInfoListener;
            return this;
        }

        /**
         * 设置控制台输出级别日志,设置此级别后 大于等于该级别的日志将会被输出到控制台
         * @param logLevel 日志级别 {@link LogLevel}
         */
        public Builder setConsoleLogLevel(byte logLevel) {
            this.consoleLogLevel = logLevel;
            return this;
        }

        /**
         * 设置控制台输出级别日志 , 设置此级别后 大于等于该级别的日志将会被输出到文件
         * @param logLevel 文件日志输出级别
         */
        public Builder setFileLogLevel(byte logLevel) {
            this.fileLogLevel = logLevel;
            return this;
        }

        /**
         * 设置Log文件保存在磁盘里的天数
         * @param retentionPeriod 保存天数
         */
        public Builder setFileLogRetentionPeriod(int retentionPeriod) {
            this.fileLogRetentionPeriod = retentionPeriod;
            return this;
        }

        /**
         * 设置Log文件保存在磁盘里的目标文件夹控件大小，当文件日志在文件夹下已经超过这个大小限制那么将不再继续写入
         * @param diskMemorySize 保存的磁盘空间大小
         */
        public Builder setFileLogDiskMemorySize(long diskMemorySize) {
            this.fileLogDiskMemorySize = diskMemorySize;
            return this;
        }

        /**
         * 设置日志存放目标文件的后缀名 比如 " .log  , .txt "
         * @param fileSuffix 文件名后缀
         */
        public Builder setlogPrintPrefix(String fileSuffix) {
            fileSuffix = checkFileSuffix(fileSuffix);
            this.logFileSuffix = fileSuffix;
            return this;
        }


        /**
         * 设置日志文件保存在磁盘(默认为外置sdcard根目录)下的文件夹名，如果不设置此项那么，缺省文件夹名使用当前程序包名
         * @param dirName 日志保存的文件夹名称
         */
        public Builder setFileLogDirName(String dirName) {
            this.fileLogDirName = dirName;
            return this;
        }

        /**
         * 设置是否开启crashHandler ，开启后将会把crash日志记录在文件中，如果有设置crashUploadTo，那么将自动上传crash文件到服务器
         * @param open 是否打开crashHandler
         */
        public Builder setCrashHandlerOpen(boolean open) {
            this.crashHandlerOpen = open;
            return this;
        }

        /**
         * 日志文件存储根路径
         */
        public Builder setFileLogRootPath(String fileLogRootPath){
            this.fileLogRootPath = fileLogRootPath;
            return this;
        }

        /**
         * 创建实例，没有设置的字段全部赋给默认值
         */
        public XLogConfiguration build() {
            checkNullFiled();
            return new XLogConfiguration(this);
        }

        /**
         * 检查空字段并附上默认值
         */
        private void checkNullFiled() {

            if (-1 == consoleLogLevel) {
                consoleLogLevel = DEFAULT_CONSOLE_LOG_LEVEL;
            }
            if (-1 == fileLogLevel) {
                fileLogLevel = DEFAULT_FILE_LOG_LEVEL;
            }
            if (fileLogRetentionPeriod == 0) {
                fileLogRetentionPeriod = DEFAULT_FILE_LOG_RETENTION_PERIOD;
            }
            if (fileLogDiskMemorySize == 0) {
                fileLogDiskMemorySize = DEFAULT_FILE_LOG_DISK_MEMORY_SIZE;
            }
            if (null == fileLogDirName || fileLogDirName.length() == 0) {
                fileLogDirName = DEFAULT_FILE_LOG_DIR_NAME;
            }
            if (null == logFileSuffix || logFileSuffix.length() == 0) {
                logFileSuffix = DEFAULT_LOG_FILE_SUFFIX;
            }
        }

        private String checkFileSuffix(String fileSuffix) {
            String checkedFileSuffix = DEFAULT_LOG_FILE_SUFFIX;

            if (null == fileSuffix || fileSuffix.length() == 0) {
                return checkedFileSuffix;
            }

            if (!fileSuffix.contains(CHAR_POINT)) {
                // log -> .log
                checkedFileSuffix = CHAR_POINT + fileSuffix;
            } else if (fileSuffix.indexOf(CHAR_POINT) == 0 && fileSuffix.length() == 1) {
                //.log -> .log
                return checkedFileSuffix;
            } else {
                //asd.log -> .log
                String[] subStrings = fileSuffix.split(CHAR_POINT);
                if (subStrings.length > 1) {
                    checkedFileSuffix = CHAR_POINT + subStrings[1];
                }
            }

            return checkedFileSuffix;
        }


    }


}
