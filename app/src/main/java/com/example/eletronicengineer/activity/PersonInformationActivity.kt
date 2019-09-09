package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.shoppingmall.PersonInformationFragment

class PersonInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_information)
        initFragment()
    }
    private fun initFragment() {
        addFragment(PersonInformationFragment())
    }
    fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_person_information, fragment)
        transaction.commit()
    }
    fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_person_information, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
