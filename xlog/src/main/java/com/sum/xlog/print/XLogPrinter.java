package com.sum.xlog.print;

public interface XLogPrinter {

    void v(String msg, Object... args);

    void v(String msg, Throwable throwable);

    void d(String msg, Object... args);

    void d(String msg, Throwable throwable);

    void i(String msg, Object... args);

    void i(String msg, Throwable throwable);

    void w(String msg, Object... args);

    void w(String msg, Throwable throwable);

    void e(String msg, Object... args);

    void e(String msg, Throwable throwable, Object... args);

    void wtf(String msg, Object... args);

    void wtf(String msg, Throwable throwable);

    void crash(String msg);

    void startMethod();

    void endMethod();

}
