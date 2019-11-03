package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.my.MySettingFragment
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.SysApplication
import kotlinx.android.synthetic.main.activity_my_setting.*

class MySettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SysApplication.getInstance().addActivity(this)
        setContentView(R.layout.activity_my_setting)
        initData()
        Log.i("","")
    }
    private fun initData() {
        FragmentHelper.addFragment(this,MySettingFragment(),R.id.frame_my_setting,"setting")

    }
}
