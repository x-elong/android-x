package com.example.eletronicengineer.utils

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide

class MApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mApplication = this
    }

    companion object {

        var mApplication: MApplication? = null
            private set
    }
}
//class MApplication : Application() {
//
//    private lateinit var mApplication: MApplication
//
//    override fun onCreate() {
//        super.onCreate()
//        mApplication = this
//    }
//
//
//    fun getContext(): MApplication {
//        return mApplication
//    }
//}