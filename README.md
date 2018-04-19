##XLOG简介
* 自动保存LOG
* 过期删除LOG,控制LOG存储大小
* 自定义Crash操作

[ ![Download](https://api.bintray.com/packages/qiu800820/maven/xlogUtils/images/download.svg) ](https://bintray.com/qiu800820/maven/xlogUtils/_latestVersion)

Gradle构建:
```javascript
compile 'com.sum.xlog:xlog:1.1.2'
```



初始化:<br>
```java
XLogConfiguration.Builder builder = new XLogConfiguration.Builder(MyApplication.this)
				.setConsoleLogLevel(LogLevel.D) //Logger输出最低级别
				.setFileLogLevel(LogLevel.D) //保存至文件最低级别
				.setCrashHandlerOpen(true) //开启异常捕获
				.setOriginalHandler(Thread.getDefaultUncaughtExceptionHandler()) //第三方统计
				.setOnCrashInfoListener(new OnCrashInfoListener() {
					@Override
					public void onUpdateCrashInfo(File file) {
						// 可以根据自己的需求启动另一个进程实现上传文件至服务器,
						// Note: 不能直接做耗时操作，影响后续UncaughtExceptionHandler
						Log.d("onUpdateCrashInfo","onUpdateCrashInfo");
					}
				}) //Crash回调
				.setFileLogRetentionPeriod(7); //过期删除
```

XLog使用方法:<br>
```java

XLog.startMethod();
XLog.d("=== XXX ===");
XLog.d("=== %s,%s ===", "XXX", "XXX");
XLog.endMethod();

FileUtil.getTodayLogFile() //获取当天LOG日志文件
FileUtil.getXLogDirFile() //获取LOG日志文件夹


```
Email： <qiujunsen@163.com><br>
