package com.example.eletronicengineer.utils

import android.app.Activity
import android.app.Application

class SysApplication :Application{

    private val activityList:MutableList<Activity> = ArrayList()
    companion object {
        private var instance: SysApplication? = null

        fun getInstance(): SysApplication {
            if (null == instance) {
                instance = SysApplication()
            }
            return (instance as SysApplication)
        }
    }
    constructor(){}

    override fun onCreate() {
        super.onCreate()
    }

    fun addActivity(activity: Activity){
        activityList.add(activity)
    }

    fun exit(){
        for (activity in activityList) {
            activity.finish()
        }
        activityList.clear();
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}