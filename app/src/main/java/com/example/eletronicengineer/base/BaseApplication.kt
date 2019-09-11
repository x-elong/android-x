package com.example.eletronicengineer.base

import android.app.Application
import android.util.Log
import cn.jpush.im.android.api.JMessageClient
import com.example.eletronicengineer.utils.UnSerializeDataBase

class BaseApplication:Application()
{
    override fun onCreate() {
        super.onCreate()
        JMessageClient.init(applicationContext,true)
        JMessageClient.setDebugMode(true)

        Log.i("ic_notification","JMessageClient init")
    }

    override fun onTerminate() {
        if (UnSerializeDataBase.isLogined)
            JMessageClient.logout()
        super.onTerminate()
    }
}