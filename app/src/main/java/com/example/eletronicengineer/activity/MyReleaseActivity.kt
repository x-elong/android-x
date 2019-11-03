package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.my.MyReleaseFragment
import com.example.eletronicengineer.utils.FragmentHelper

class MyReleaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_release)
        FragmentHelper.addFragment(this,MyReleaseFragment(),R.id.frame_my_release,"MyRelease")
    }
}
