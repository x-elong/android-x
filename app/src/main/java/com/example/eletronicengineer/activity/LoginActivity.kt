package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.login.LoginFragment
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.PermissionHelper

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        PermissionHelper.getPermission(this,1)
        FragmentHelper.addFragment(this,LoginFragment(),R.id.frame_login,"")
    }
}
