package com.sum.xlog.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sum.xlog.core.XLog;
import com.sum.xlog.util.FileUtil;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XLog.startMethod(TAG, "onCreate");
        setContentView(R.layout.activity_main);


        //因为XLog有缓冲区，  满20条才会写入文件  降低CPU负担
        for(int i = 0; i < 10; i++){
            XLog.d("=== XXX ===");
            XLog.d(TAG, "=== xxx ===");
            XLog.d(TAG, "=== %s,%s ===", "XXX", "XXX");
        }

        String a = "Null";
        try{
            a.length();
        }catch (NullPointerException e){
            XLog.e(TAG,"=== %s Exception ===", e, a);
        }

        Button sendLog = (Button)findViewById(R.id.send_log);
        sendLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent(Intent.ACTION_SEND);
                data.putExtra(Intent.EXTRA_EMAIL,new String[] {"test@test.com"});
                data.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                data.putExtra(Intent.EXTRA_TEXT, "这是我的LOG日志");
                data.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + FileUtil.getTodayLogFilePath()));
                data.setType("message/rfc882");
                startActivity(Intent.createChooser(data, getString(R.string.app_name)));
            }
        });


        XLog.endMethod(TAG, "onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 最后一个Activity退出销毁
        XLog.destroy();
    }
}
