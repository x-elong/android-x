package com.example.eletronicengineer.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.login.LoginFragment
import com.example.eletronicengineer.utils.*
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta

class LoginActivity: AppCompatActivity() {

    private lateinit var pref: SharedPreferences
    private lateinit var editor:SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        SysApplication.getInstance().addActivity(this)
        PermissionHelper.getPermission(this,1)

        pref = PreferenceManager.getDefaultSharedPreferences(this)
//        Beta.checkUpgrade(false,false)
//        Beta.autoInit = true
//        Beta.autoCheckUpgrade = true
//        Beta.showInterruptedStrategy = true
//        Beta.enableNotification = true
//        Beta.canShowApkInfo = true



//        AddUtil.addContact(this)
    }

    override fun onStart() {
        super.onStart()
        UpdateApplication.getInstance().detectionUpdate(this)
        if(isInvalid() || UnSerializeDataBase.isLogined){
            if(UnSerializeDataBase.isLogined){
                UnSerializeDataBase.isLogined = false
                editor.putLong("lastLoginTime",0)
                editor.apply()
            }
            FragmentHelper.addFragment(this,LoginFragment(),R.id.frame_login,"")
        } else{
            UnSerializeDataBase.userToken = pref.getString("token","")
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
    fun isInvalid():Boolean{

        val lastLoginTime = pref.getLong("lastLoginTime",0)
        val nowTime = System.currentTimeMillis()
        val diff = nowTime - lastLoginTime
        val days = diff / (1000 * 60 * 60 * 24)
        val hours = (diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
        val minutes = (diff % (1000 * 60 * 60)) / (1000 * 60)
        Log.i("diff time(days hours):","${days}:${hours}")
        if(days>0 || hours>=2){
            return true
        }
        editor = pref.edit()
        editor.putLong("lastLoginTime",nowTime)
        editor.apply()
        return false
    }
}
