package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.activity_my_signature.*

class MySignatureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_signature)
        tv_my_signature_back.setOnClickListener {
            finish()
        }
    }
}
