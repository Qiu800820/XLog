##XLOG简介
* 自动保存LOG
* 过期删除LOG,控制LOG存储大小
* 自定义上传LOG
* 自定义Crash操作

[ ![Download](https://api.bintray.com/packages/qiu800820/maven/xlogUtils/images/download.svg) ](https://bintray.com/qiu800820/maven/xlogUtils/_latestVersion)

Gradle构建:
```javascript
compile 'com.sum.xlog:xlog:1.0.2'
```

需要声明服务:
```xml
<service android:name="com.sum.xlog.core.LogService" />
```

需要声明权限:
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

初始化:<br>
```java
XLogConfiguration.Builder builder = new XLogConfiguration.Builder(MyApplication.this)
				.setConsoleLogLevel(LogLevel.D) //Logger输出最低级别
				.setFileLogLevel(LogLevel.D) //保存至文件最低级别
				.setCrashHandlerOpen(true) //开启异常捕获
				.setDefaultTag("Qiu800820") //默认TAG
				.setOriginalHandler(Thread.getDefaultUncaughtExceptionHandler()) //第三方统计
				.setOnUpdateCrashInfoListener(null) //Crash自动上传处理
				.setFileLogRetentionPeriod(7); //过期删除
		XLog.init(builder.build());
```

XLog使用方法:<br>
```java

XLog.startMethod(TAG, "方法名");
XLog.d("=== LOG信息 ==="); //默认TAG
XLog.d(TAG, "=== LOG信息 ==="); //自定义TAG
XLog.d(TAG, "=== %s,%s ===", "XXX", "XXX"); //String格式化
XLog.endMethod(TAG, "方法名");

FileUtil.getTodayLogFilePath() //获取当天LOG日志文件路径
FileUtil.getXLogPath() //获取LOG日志文件夹路径


```
Email： <qiujunsen@163.com><br>
