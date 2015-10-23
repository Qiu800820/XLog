package com.sum.xlog.sample;

import android.app.Activity;
import android.os.Bundle;

import com.sum.xlog.core.XLog;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XLog.startMethod(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        String a = "Null";
        try{
            a.length();
        }catch (NullPointerException e){
            XLog.e(TAG,"=== %s Exception ===", e, a);
        }
        XLog.endMethod(TAG, "onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 最后一个Activity退出销毁
        XLog.destroy();
    }
}
