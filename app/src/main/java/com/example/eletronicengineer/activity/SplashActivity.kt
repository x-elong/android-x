package com.example.eletronicengineer.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.eletronicengineer.R

class SplashActivity : AppCompatActivity() {
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            next()
        }
    }

    private operator fun next() {
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //去除状态栏
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        handler.postDelayed({
            //1.5秒钟后调用，这里
            handler.sendEmptyMessage(0)
        }, 1500)
    }
}




