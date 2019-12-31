package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.my.HelpCenterFragment
import com.example.eletronicengineer.utils.FragmentHelper

class HelpCenterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_center)
        FragmentHelper.addFragment(this,HelpCenterFragment(),R.id.frame_help_center,"")
    }
}
