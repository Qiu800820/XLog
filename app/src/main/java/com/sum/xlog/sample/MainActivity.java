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
        XLog.startMethod();
        setContentView(R.layout.activity_main);


        //因为XLog有缓冲区，  满20条才会写入文件  降低CPU负担
        for(int i = 0; i < 10; i++){
            XLog.d("=== XXX ===");
            XLog.d("=== %s,%s ===", "XXX", "XXX");
        }

        String a = null;
        try{
            a.length();
        }catch (NullPointerException e){
            XLog.e("=== %s Exception ===", e, "Null");
        }

        Button sendLog = (Button)findViewById(R.id.send_log);
        sendLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent(Intent.ACTION_SEND);
                data.putExtra(Intent.EXTRA_EMAIL, new String[]{"test@test.com"});
                data.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                data.putExtra(Intent.EXTRA_TEXT, "这是我的LOG日志");
                data.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + FileUtil.getTodayLogFile().getAbsolutePath()));
                data.setType("message/rfc882");
                startActivity(Intent.createChooser(data, getString(R.string.app_name)));
            }
        });

        Button testCrash = findViewById(R.id.testCrash);
        testCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = null;
                a.length();
            }
        });


        XLog.endMethod();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
