package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.my.SubscribeFragment

class SubscribeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribe)
        initData()
    }
    private fun initData() {
        addFragment(SubscribeFragment())
    }

    fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_subscribe, fragment,"Subscribe")
        transaction.commit()
    }

    fun switchFragment(fragment: Fragment, tag:String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.frame_subscribe, fragment,tag)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
