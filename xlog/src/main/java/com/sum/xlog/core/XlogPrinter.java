package com.sum.xlog.core;

public interface XlogPrinter
{

    public void v(String msg);
    public void v(String tag, String msg);
    public void v(String tag, String msg, Object... arg);
    public void v(String msg, Throwable throwable);
    public void v(String tag, String msg, Throwable throwable);
    
    public void d(String msg);
    public void d(String tag, String msg);
    public void d(String tag, String msg, Object... arg);
    public void d(String msg, Throwable throwable);
    public void d(String tag, String msg, Throwable throwable);
    
    public void i(String msg);
    public void i(String tag, String msg);
    public void i(String tag, String msg, Object... arg);
    public void i(String msg, Throwable throwable);
    public void i(String tag, String msg, Throwable throwable);
    
    public void w(String msg);
    public void w(String tag, String msg);
    public void w(String tag, String msg, Object... arg);
    public void w(String msg, Throwable throwable);
    public void w(String tag, String msg, Throwable throwable);
    
    public void e(String msg);
    public void e(String tag, String msg);
    public void e(String tag, String msg, Object... arg);
    public void e(String msg, Throwable throwable);
    public void e(String tag, String msg, Throwable throwable);
    
    public void wtf(String msg);
    public void wtf(String tag, String msg);
    public void wtf(String tag, String msg, Object... arg);
    public void wtf(String msg, Throwable throwable);
    public void wtf(String tag, String msg, Throwable throwable);
    
    
}
