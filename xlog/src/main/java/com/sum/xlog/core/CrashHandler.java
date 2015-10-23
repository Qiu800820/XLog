package com.sum.xlog.core;

import java.lang.Thread.UncaughtExceptionHandler;


/**
 * @brief 异常处理类 - 当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * @author lu.yao
 * @date 2012-10-9 上午11:24:10
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = CrashHandler.class.getSimpleName();

	/** 系统默认的UncaughtException处理类 */
	private UncaughtExceptionHandler mDefaultHandler;
	/** 第三方UncaughtException处理类*/
	private UncaughtExceptionHandler originalHandler;
	/** CrashHandler实例 */
	private static CrashHandler instance = new CrashHandler();
	/** 当crash发生后的接口回调 **/
	private OnCaughtCrashExceptionListener mCaughtCrashExceptionListener;
	
	/**
	 * 当捕获到Crash异常后会通过该接口回调
	 */
	public interface OnCaughtCrashExceptionListener{
	    void onCaughtCrashException(Thread thread, Throwable ex);
	}
	
	public void setCaughtCrashExceptionListener(OnCaughtCrashExceptionListener mCaughtCrashExceptionListener)
    {
        this.mCaughtCrashExceptionListener = mCaughtCrashExceptionListener;
    }
	
	/**
	 * 保证只有一个CrashHandler实例
	 */
	private CrashHandler() {}

	/**
	 * 获取CrashHandler实例 ,单例模式
	 * 
	 * @return CrashHandler CrashHandler对象
	 */
	public static CrashHandler getInstance() {
		return instance;
	}

	/**
	 * @brief 初始化
	 * @param context
	 *            上下文文本对象
	 * 
	 */
	public void init() {
		init(null);
	}
	
	/**
	 * @brief 初始化
	 * @param UncaughtExceptionHandler
	 *            第三方UncaughtExceptionHandler处理
	 * 
	 */
	public void init(UncaughtExceptionHandler mUncaughtExceptionHandler){
		originalHandler = mUncaughtExceptionHandler; 
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
	    
		if(mCaughtCrashExceptionListener != null){
	    	mCaughtCrashExceptionListener.onCaughtCrashException(thread,ex);
	    }
	    
	    if(originalHandler != null){
	    	originalHandler.uncaughtException(thread, ex);
	    }
	    
	    if(mCaughtCrashExceptionListener == null && originalHandler == null){
	    	//外部不关心异常crash异常，那么我们调用默认异常处理handle去处理异常
	        mDefaultHandler.uncaughtException(thread, ex);
	    }
	}
}
