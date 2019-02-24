package com.example.tree.zhihu.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.tree.zhihu.MainActivity;
import com.example.tree.zhihu.R;

public class SplashActivity extends AppCompatActivity {

    TextView splash_1;
    TextView splash_2;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    splash_1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    splash_2.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        splash_1 = (TextView) findViewById(R.id.splash_1);
        splash_2 = (TextView) findViewById(R.id.splash_2);
        Thread myThread=new Thread(){//创建子线程
            @Override
            public void run() {
                try{
                    sleep(500);//使程序休眠五秒
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    sleep(1000);
                    Message message1 = new Message();
                    message1.what = 2;
                    handler.sendMessage(message1);
                    sleep(1000);
                    Intent it=new Intent(getApplicationContext(), MainActivity.class);//启动MainActivity
                    startActivity(it);
                    finish();//关闭当前活动
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }
}
