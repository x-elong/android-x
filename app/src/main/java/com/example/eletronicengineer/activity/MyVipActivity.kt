package com.example.eletronicengineer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.fragment.my.VipPrivilegesFragment
import com.example.eletronicengineer.utils.FragmentHelper
import kotlinx.android.synthetic.main.activity_my_vip.*

class MyVipActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vip)
        FragmentHelper.addFragment(this, VipPrivilegesFragment(),R.id.frame_vip,"")
    }
}
