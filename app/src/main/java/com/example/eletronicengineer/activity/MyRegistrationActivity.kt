package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.my.MyRegistrationFragment
import com.example.eletronicengineer.utils.FragmentHelper
import kotlinx.android.synthetic.main.activity_my_registration.*

class MyRegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_registration)
        FragmentHelper.addFragment(this,MyRegistrationFragment(),R.id.frame_my_registration,"")
    }
    fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.frame_my_registration, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
