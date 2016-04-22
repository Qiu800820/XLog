package com.sum.xlog.sample;

import android.app.Application;

import com.sum.xlog.core.LogLevel;
import com.sum.xlog.core.OnUpdateCrashInfoListener;
import com.sum.xlog.core.XLog;
import com.sum.xlog.core.XLogConfiguration;

import java.io.File;


public class MyApplication extends Application {


	
	@Override
	public void onCreate() {
		super.onCreate();

		XLogConfiguration.Builder builder = new XLogConfiguration.Builder(MyApplication.this)
				.setConsoleLogLevel(LogLevel.D) //Logger输出最低级别
				.setFileLogLevel(LogLevel.D) //保存至文件最低级别
				.setCrashHandlerOpen(true) //开启异常捕获
				.setOriginalHandler(Thread.getDefaultUncaughtExceptionHandler()) //第三方统计
				.setDefaultTag("Qiu800820")
				.setOnUpdateCrashInfoListener(new OnUpdateCrashInfoListener() {
					@Override
					public void onUpdateCrashInfo(File file) {
						// TODO 可以根据自己的需求实现上传文件至服务器
					}
				}) //Crash自动上传处理

				.setFileLogRetentionPeriod(7); //过期删除

		XLog.init(builder.build());
		// Default
		// XLog.init(this);


	}

}
