#XLOG简介

*自动保存LOG<br>
*过期删除LOG,控制LOG存储大小<br>
*一键上传LOG<br>
*自定义Crash操作<br>


需要声明服务:<br>
\<service android:name="com.sum.xlog.core.LogService" /><br>

使用方法:<br>
XLogConfiguration.Builder builder = new XLogConfiguration.Builder(MyApplication.this)<br>
				.setConsoleLogLevel(LogLevel.D) //Logger输出最低级别<br>
				.setFileLogLevel(LogLevel.D) //保存至文件最低级别<br>
				.setCrashHandlerOpen(true) //开启异常捕获<br>
				.setOriginalHandler(Thread.getDefaultUncaughtExceptionHandler()) //第三方统计<br>
				.setOnUpdateCrashInfoListener(null) //Crash自动上传处理<br>
				.setFileLogRetentionPeriod(7); //过期删除<br>
		XLog.init(builder.build());<br>


XLog.startMethod(TAG, "onCreate");<br>
XLog.endMethod(TAG, "onCreate");<br>
