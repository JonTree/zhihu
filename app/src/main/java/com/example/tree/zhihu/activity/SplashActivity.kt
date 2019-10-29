package com.example.tree.zhihu.activity

import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView

import com.example.tree.zhihu.R

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val myThread = object : Thread() {
            //创建子线程
            override fun run() {
                try {
                    Thread.sleep(3000)//使程序休眠
                    val it = Intent(applicationContext, MainActivity::class.java)//启动MainActivity
                    startActivity(it)
                    finish()//关闭当前活动
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        myThread.start()//启动线程
    }
}
