package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.my.*
import com.example.eletronicengineer.utils.FragmentHelper

class MyCertificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_certification)
        FragmentHelper.addFragment(this,CertificationCenterFragment(),R.id.frame_my_certification,"MyCertification")
    }
}
