package com.example.eletronicengineer.activity

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.my.VipPrivilegesFragment
import com.example.eletronicengineer.utils.FragmentHelper

import kotlinx.android.synthetic.main.activity_vip.*

class VipActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vip)
        FragmentHelper.addFragment(this, VipPrivilegesFragment(),R.id.frame_vip,"")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
