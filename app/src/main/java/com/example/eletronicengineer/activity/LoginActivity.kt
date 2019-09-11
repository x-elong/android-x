package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.login.LoginFragment
import com.example.eletronicengineer.utils.SysApplication

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SysApplication.getInstance().addActivity(this)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        initFragment()
    }
    private fun initFragment() {
        addFragment(LoginFragment())

    }

    fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_login, fragment)
        transaction.commit()
    }

    fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.frame_login, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

