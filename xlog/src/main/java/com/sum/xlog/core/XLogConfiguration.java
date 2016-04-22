package com.sum.xlog.core;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.lang.Thread.UncaughtExceptionHandler;


/**
 * @ClassName XLogConfig
 * @Description 这个类主要用于配置XLog框架
 * @Author Yao
 * @Date 2015年8月12日 下午5:04:32
 * @Version V1.0
 */
public class XLogConfiguration {

    /**
     * 默认控制台输出日志级别
     */
    public static final byte DEFAULT_CONSOLE_LOG_LEVEL = LogLevel.V;
    /**
     * 默认存储至文件日志级别
     */
    public static final byte DEFAULT_FILE_LOG_LEVEL = LogLevel.D;
    /**
     * 默认LOG文件过期时间
     */
    public static final int DEFAULT_FILE_LOG_RETENTION_PERIOD = 7;
    /**
     * 默认LOG文件大小KB单位
     */
    public static final long DEFAULT_FILE_LOG_DISK_MEMORYSIZE = 1024 * 100; //100m
    /**
     * 默认文件目录名称(获取包名目录错误的情况下使用)
     */
    public static final String DEFAULT_FILE_LOG_DIR_NAME = "Xlog";
    public static final String STRING_NULL = "";
    public static final String LOG_STRING = "log";
    /**
     * 默认日志所属人
     */
    public static final String DEFAULT_LOG_OWEN = "all";

    /**
     * 默认日志文件后缀
     */
    public static final String DEFAULT_LOG_FILE_SUFFIX = ".log";
    /**
     * 默认TAG
     */
    public static final String DEFAULT_TAG = "Xlog";
    /**
     * 符号 "."
     */
    public static final String CHARCHER_POINT = ".";

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
     * 文件日志在记忆体中的存储最大空间
     */
    private long fileLogDiskMemorySize;
    /**
     * 设置日志文件夹名称
     */
    private String fileLogDirName;

    /**
     * 日志打印前缀 比如 "TUS===>" ,这跟逻辑无关，完全是为了跟其他app打印的日志做一些区别
     */
    private String logPrintPrefix;

    /**
     * 日志存放目标文件的后缀名 比如 " .log  , .txt , .avi(你懂的) "
     */
    private String logFileSuffix;

    /**
     * 是否开启crashHandler ,如果不开启的话不会将crash日志写入到文件
     */
    private boolean crashHandlerOpen = true;

    /**
     * 日志文件所有人，如果需要区分日志文件属于哪个账号或者属于某个目标群体那么通过该字段设置
     */
    private String fileLogOwner;

    /**
     * android 上下文
     */
    private Context context;

    /**
     * 默认TAG
     */
    private String defaultTag;

    /**
     * UncaughtException处理类
     */
    private UncaughtExceptionHandler originalHandler = null;

    private OnUpdateCrashInfoListener mOnUpdateCrashInfoListener;


    public XLogConfiguration(Builder builer) {

        this.consoleLogLevel = builer.consoleLogLevel;
        this.fileLogDiskMemorySize = builer.fileLogDiskMemorySize;
        this.fileLogLevel = builer.fileLogLevel;
        this.fileLogRetentionPeriod = builer.fileLogRetentionPeriod;
        this.fileLogDirName = builer.fileLogDirName;
        this.logPrintPrefix = builer.logPrintPrefix;
        this.logFileSuffix = builer.logFileSuffix;
        this.crashHandlerOpen = builer.crashHandlerOpen;
        this.fileLogOwner = builer.fileLogOwner;
        this.context = builer.mContext;
        this.originalHandler = builer.originalHandler;
        this.mOnUpdateCrashInfoListener = builer.mOnUpdateCrashInfoListener;
        this.defaultTag = builer.defaultTag;

    }


    public static XLogConfiguration createDefault(Context context) {
        return new Builder(context).build();
    }


    public byte getConsoleLogLevel() {
        return consoleLogLevel;
    }


    public byte getFileLogLevel() {
        return fileLogLevel;
    }

    public String getDefaultTag() {
        return defaultTag;
    }

    public void setDefaultTag(String defaultTag) {
        this.defaultTag = defaultTag;
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


    public String getLogPrintPrefix() {
        return logPrintPrefix;
    }


    public String getLogFileSuffix() {
        return logFileSuffix;
    }

    public boolean isCrashHandlerOpen() {
        return crashHandlerOpen;
    }


    public String getFileLogOwner() {
        return fileLogOwner;
    }


    public Context getContext() {
        return context;
    }

    public UncaughtExceptionHandler getOriginalHandler() {
        return originalHandler;
    }

    public OnUpdateCrashInfoListener getOnUpdateCrashInfoListener() {
        return mOnUpdateCrashInfoListener;
    }

    public static class Builder {

        /**
         * android 上下文
         */
        private Context mContext;

        private byte consoleLogLevel = -1;
        private byte fileLogLevel = -1;
        private int fileLogRetentionPeriod;
        private long fileLogDiskMemorySize;
        private String fileLogDirName;
        private String logPrintPrefix;
        private String logFileSuffix;
        private String defaultTag;
        private boolean crashHandlerOpen;
        private String fileLogOwner;
        private UncaughtExceptionHandler originalHandler = null;
        private OnUpdateCrashInfoListener mOnUpdateCrashInfoListener = null;

        public Builder(Context context) {
            mContext = context.getApplicationContext();
        }

        /**
         * 设置其他异常处理类，主要兼容统计(可不设置)
         *
         * @param originalHandler 原始异常处理类 {@link UncaughtExceptionHandler}
         */
        public Builder setOriginalHandler(UncaughtExceptionHandler originalHandler) {
            this.originalHandler = originalHandler;
            return this;
        }

        /**
         * 设置Crash用户上传日志信息
         *
         * @param mOnUpdateCrashInfoListener 用户上传事件{@link OnUpdateCrashInfoListener}
         */
        public Builder setOnUpdateCrashInfoListener(OnUpdateCrashInfoListener mOnUpdateCrashInfoListener) {
            this.mOnUpdateCrashInfoListener = mOnUpdateCrashInfoListener;
            return this;
        }

        /**
         * 设置控制台输出级别日志,设置此级别后 大于等于该级别的日志将会被输出到控制台
         *
         * @param logLevel 日志级别 {@link LogLevel}
         */
        public Builder setConsoleLogLevel(byte logLevel) {
            this.consoleLogLevel = logLevel;
            return this;
        }

        /**
         * 设置控制台输出级别日志 , 设置此级别后 大于等于该级别的日志将会被输出到文件
         *
         * @param logLevel 文件日志输出级别
         */
        public Builder setFileLogLevel(byte logLevel) {
            this.fileLogLevel = logLevel;
            return this;
        }

        /**
         * 设置Log文件保存在磁盘里的天数
         *
         * @param retentionPeriod 保存天数
         */
        public Builder setFileLogRetentionPeriod(int retentionPeriod) {
            this.fileLogRetentionPeriod = retentionPeriod;
            return this;
        }

        /**
         * 设置Log文件保存在磁盘里的目标文件夹控件大小，当文件日志在文件夹下已经超过这个大小限制那么将不再继续写入
         *
         * @param diskMemorySize 保存的磁盘空间大小
         */
        public Builder setFileLogDiskMemorySize(long diskMemorySize) {
            this.fileLogDiskMemorySize = diskMemorySize;
            return this;
        }

        /**
         * 设置日志打印前缀 比如 "TUS===>" ,这跟逻辑无关，完全是为了跟其他app打印的日志做一些区别
         *
         * @param printPrefix 日志前缀
         */
        public Builder setLogPrintPrefix(String printPrefix) {
            this.logPrintPrefix = printPrefix;
            return this;
        }

        /**
         * 设置日志存放目标文件的后缀名 比如 " .log  , .txt , .avi(你懂的)
         *
         * @param fileSuffix 文件名后缀
         */
        public Builder setlogPrintPrefix(String fileSuffix) {
            fileSuffix = checkFileSuffix(fileSuffix);
            this.logFileSuffix = fileSuffix;
            return this;
        }


        /**
         * 设置日志文件保存在磁盘(默认为外置sdcard根目录)下的文件夹名，如果不设置此项那么，缺省文件夹名使用当前程序包名,用 "_" 替换包名中的 "." 符号
         *
         * @param dirName 日志保存的文件夹名称
         * @return
         */
        public Builder setFileLogDirName(String dirName) {
            this.fileLogDirName = dirName;
            return this;
        }

        /**
         * 设置是否开启crashHandler ，开启后将会把crash日志记录在文件中，如果有设置crashUploadTo，那么将自动上传crash文件到服务器
         *
         * @param open 是否打开crashHandler
         */
        public Builder setCrashHandlerOpen(boolean open) {
            this.crashHandlerOpen = open;
            return this;
        }

        /***
         * 设置日志文件所有人，如果需要区分日志文件属于哪个账号或者属于某个目标群体那么通过该字段设置
         *
         * @param owener 所有人标识
         */
        public Builder setFileLogOwener(String owener) {
            this.fileLogOwner = owener;
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
         * 获取默认TAG(当用户不传TAG情况下使用)
         */
        public String getDefaultTag() {
            return defaultTag;
        }

        /**
         * 默认TAG(当用户不传TAG情况下使用)
         *
         * @param defaultTag tag
         */
        public Builder setDefaultTag(String defaultTag) {
            this.defaultTag = defaultTag;
            return this;
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
                fileLogDiskMemorySize = DEFAULT_FILE_LOG_DISK_MEMORYSIZE;
            }
            if (null == fileLogDirName || fileLogDirName.length() == 0) {
                fileLogDirName = getDefaultFileLogDirName(mContext);
            }
            if (null == logPrintPrefix) {
                logPrintPrefix = STRING_NULL;
            }
            if (null == logFileSuffix || logFileSuffix.length() == 0) {
                logFileSuffix = DEFAULT_LOG_FILE_SUFFIX;
            }
            if (null == fileLogOwner) {
                fileLogOwner = DEFAULT_LOG_OWEN;
            }

            if (TextUtils.isEmpty(defaultTag)) {
                defaultTag = DEFAULT_TAG;
            }

        }


        private String getDefaultFileLogDirName(Context mContext) {

            try {
                String packageName = mContext.getPackageName();
                packageName = packageName.replace(".", "_");
                packageName = String.format("%s%s%s", packageName, "_", LOG_STRING);
                return packageName;
            } catch (Exception e) {
                Log.e("XlogConfiguration", "getDefaultFileLogDirName has been exception", e);
            }

            return DEFAULT_FILE_LOG_DIR_NAME;
        }

        private String checkFileSuffix(String fileSuffix) {

            String checkedFileSuffix = DEFAULT_LOG_FILE_SUFFIX;

            if (null == fileSuffix || fileSuffix.length() == 0) {
                return checkedFileSuffix;
            }

            if (fileSuffix.indexOf(CHARCHER_POINT) < 0) {
                // log -> .log
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(CHARCHER_POINT);
                stringBuilder.append(fileSuffix);
                checkedFileSuffix = stringBuilder.toString();

            } else if (fileSuffix.indexOf(CHARCHER_POINT) == 0 && fileSuffix.length() == 1) {
                return checkedFileSuffix;
            } else { //asd.log -> log
                String[] subStrings = fileSuffix.split(CHARCHER_POINT);
                if (subStrings.length > 1) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(CHARCHER_POINT);
                    stringBuilder.append(subStrings[1]);
                    checkedFileSuffix = stringBuilder.toString();
                }
            }

            return checkedFileSuffix;
        }


    }


}
