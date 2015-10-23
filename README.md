XLOG简介

*自动保存LOG
*过期删除LOG,控制LOG存储大小
*一键上传LOG
*自定义Crash操作


需要声明服务:
<service android:name="com.sum.xlog.core.LogService" />

使用方法:
XLogConfiguration.Builder builder = new XLogConfiguration.Builder(MyApplication.this)
				.setConsoleLogLevel(LogLevel.D) //Logger输出最低级别
				.setFileLogLevel(LogLevel.D) //保存至文件最低级别
				.setCrashHandlerOpen(true) //开启异常捕获
				.setOriginalHandler(Thread.getDefaultUncaughtExceptionHandler()) //第三方统计
				.setOnUpdateCrashInfoListener(null) //Crash自动上传处理
				.setFileLogRetentionPeriod(7); //过期删除
		XLog.init(builder.build());


XLog.startMethod(TAG, "onCreate");
XLog.endMethod(TAG, "onCreate");
