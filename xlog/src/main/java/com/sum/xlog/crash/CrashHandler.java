package com.sum.xlog.crash;

import android.os.Process;

import java.lang.Thread.UncaughtExceptionHandler;


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
	 * 保证只有一个CrashHandler实例
	 */
	private CrashHandler() {}

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
	 * 获取CrashHandler实例 ,单例模式
	 * 
	 * @return CrashHandler CrashHandler对象
	 */
	public static CrashHandler getInstance() {
		return instance;
	}


	public void init() {
		init(null);
	}
	
	/**
	 * 初始化
	 * @param mUncaughtExceptionHandler 第三方UncaughtExceptionHandler处理
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
	    	//外部不关心crash异常，那么我们调用默认handle去处理异常
	        mDefaultHandler.uncaughtException(thread, ex);
	    }else{
			android.os.Process.killProcess(Process.myPid());
		}
	}
}
