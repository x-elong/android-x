package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.activity_my_qrcode.*

class MyQRCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_qrcode)
        tv_qr_code_back.setOnClickListener {
            finish()
        }
    }
}
