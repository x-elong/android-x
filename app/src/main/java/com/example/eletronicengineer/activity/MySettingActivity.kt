package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.my.MySettingFragment
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
        addFragment((MySettingFragment()))
    }

    fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_my_setting, fragment,"setting")
        transaction.commit()
    }

    fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.frame_my_setting, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
