package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.shoppingmall.PersonInformationFragment
import com.example.eletronicengineer.utils.FragmentHelper

class PersonInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_information)
        FragmentHelper.addFragment(this,PersonInformationFragment(),R.id.frame_person_information,"")
    }
}
