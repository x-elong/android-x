package com.example.eletronicengineer.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.eletronicengineer.R
import com.example.eletronicengineer.db.My.UserSubitemEntity
import com.wjj.easy.qrcodestyle.QRCodeStyle
import kotlinx.android.synthetic.main.activity_my_qrcode.*
import java.io.FileOutputStream
import kotlin.math.log

class MyQRCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_qrcode)
        tv_qr_code_back.setOnClickListener {
            finish()
        }
        val data=intent.getSerializableExtra("user") as UserSubitemEntity
        logo_iv.run {
            qrName=data.name
            qrPhone=data.phone
            qrLocation="百全楼410"
            Glide.with(this@MyQRCodeActivity).load(data.headerImg).into(object:CustomTarget<Drawable>(){
                override fun onLoadCleared(placeholder: Drawable?) {

                }
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    qrAvatar=resource
                }
            })
            Glide.with(this@MyQRCodeActivity).load(R.drawable.ic_smile).into(object:CustomTarget<Drawable>(){
                override fun onLoadCleared(placeholder: Drawable?) {

                }
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    qrContent=resource
                    this@MyQRCodeActivity.runOnUiThread {
                        logo_iv.updateUI()
                    }
                }
            })
        }
        btn_my_qrcode_save_qrcode.setOnClickListener {
            logo_iv.apply {
                val image= Bitmap.createBitmap(measuredWidth,measuredHeight,Bitmap.Config.ARGB_8888)
                draw(Canvas(image))
                val fos=FileOutputStream(context.filesDir)
                image.compress(Bitmap.CompressFormat.PNG,100,fos)
            }
        }
        /*
        val bg = BitmapFactory.decodeResource(resources,R.mipmap.expand)
        val targetBitmap = QRCodeStyle.Builder.builder()
            .setQr(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_dialog_loading))
            .setBg(bg)
            .build().get()
        //logo_iv.setImageBitmap(targetBitmap)
        */
    }
}
