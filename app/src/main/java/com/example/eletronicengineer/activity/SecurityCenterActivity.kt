package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.activity_security_center.*

class SecurityCenterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_center)
        tv_security_center_back.setOnClickListener {
            finish()
        }
    }
}
