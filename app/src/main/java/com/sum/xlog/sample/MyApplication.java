package com.sum.xlog.sample;

import android.app.Application;
import android.util.Log;

import com.sum.xlog.core.XLog;
import com.sum.xlog.core.XLogConfiguration;
import com.sum.xlog.crash.OnCrashInfoListener;
import com.sum.xlog.print.LogLevel;

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
				.setOnCrashInfoListener(new OnCrashInfoListener() {
					@Override
					public void onUpdateCrashInfo(File file) {
						// TODO 可以根据自己的需求启动另一个进程实现上传文件至服务器,
						// Note: 不能直接做耗时操作，影响后续UncaughtExceptionHandler
						Log.d("onUpdateCrashInfo","onUpdateCrashInfo");


					}
				}) //Crash回调
				.setFileLogRetentionPeriod(7); //过期删除

		XLog.init(MyApplication.this, builder.build());
		// Default
		// XLog.init(this);


	}

}
