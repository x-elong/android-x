package com.example.eletronicengineer.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.eletronicengineer.R
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.db.My.UserSubitemEntity
import com.example.eletronicengineer.utils.GlideImageLoader
import com.example.eletronicengineer.utils.ToastHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.getQRCode
import com.wjj.easy.qrcodestyle.QRCodeStyle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_qrcode.*
import java.io.File
import java.io.FileOutputStream
import kotlin.math.log

class MyQRCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_qrcode)
        tv_qr_code_back.setOnClickListener {
            finish()
        }

        val loadingDialog = LoadingDialog(this,"正在获取二维码...")
        loadingDialog.show()
        val result = getQRCode().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            loadingDialog.dismiss()
        },{
            loadingDialog.dismiss()
            ToastHelper.mToast(this,"服务器异常")
            it.printStackTrace()
        })
        val headerImg=intent.getStringExtra("headerImg")
        GlideImageLoader().displayImage(logo_iv,headerImg)
//        logo_iv.run {
//            qrName=UnSerializeDataBase.userName
//            qrPhone=UnSerializeDataBase.userPhone
//            qrLocation="百全楼410"
//            Glide.with(this@MyQRCodeActivity).load(headerImg).into(object:CustomTarget<Drawable>(){
//                override fun onLoadCleared(placeholder: Drawable?) {
//
//                }
//                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                    qrAvatar=resource
//                }
//            })
//            Glide.with(this@MyQRCodeActivity).load(R.drawable.ic_smile).into(object:CustomTarget<Drawable>(){
//                override fun onLoadCleared(placeholder: Drawable?) {
//
//                }
//                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                    qrContent=resource
//                    this@MyQRCodeActivity.runOnUiThread {
//
//                    }
//                }
//            })
//        }
        btn_my_qrcode_save_qrcode.setOnClickListener {
            logo_iv.apply {
                val image= Bitmap.createBitmap(measuredWidth,measuredHeight,Bitmap.Config.ARGB_8888)
                draw(Canvas(image))
                val absolutePath = context.cacheDir.absolutePath
                val file = File(absolutePath)
                file.mkdirs()
                if(!file.exists()) {
                    file.mkdirs()
                }
                val path = UnSerializeDataBase.filePath+"image/test.png"
                val fos=FileOutputStream(path)
                image.compress(Bitmap.CompressFormat.PNG,100,fos)
                Log.i("path:","${context.cacheDir.absolutePath}/test.png")
                Log.i("File exists:",File("${context.cacheDir.absolutePath}/test.png").exists().toString())
                var result = "二维码保存失败"
                if(File(path).exists())
                    result = "二维码保存成功"
                ToastHelper.mToast(context,result)
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
