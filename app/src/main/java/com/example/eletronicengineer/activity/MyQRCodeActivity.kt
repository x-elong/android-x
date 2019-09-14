package com.example.eletronicengineer.activity

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eletronicengineer.R
import com.wjj.easy.qrcodestyle.QRCodeStyle
import kotlinx.android.synthetic.main.activity_my_qrcode.*

class MyQRCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_qrcode)
        tv_qr_code_back.setOnClickListener {
            finish()
        }
        val bg = BitmapFactory.decodeResource(resources,R.mipmap.expand)
        val targetBitmap = QRCodeStyle.Builder.builder()
            .setQr(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_dialog_loading))
            .setBg(bg)
            .build().get()
        logo_iv.setImageBitmap(targetBitmap)
    }
}
