package com.sum.xlog.sample;

import com.sum.xlog.core.XLog;

/**
 * Created by Sen on 2018/5/29.
 */

public class Test {

    public void show(){
        XLog.d("测试混淆TAG className:%s", getClass().getSimpleName());
        XLog.d("测试混淆TAG className:%s", getClass().getSimpleName());
        XLog.d("测试混淆TAG className:%s", getClass().getSimpleName());
    }

}
