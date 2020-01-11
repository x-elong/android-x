package com.example.eletronicengineer.activity

import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
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
import com.example.eletronicengineer.wxapi.WXShare
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.wjj.easy.qrcodestyle.QRCodeStyle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_qrcode.*
import kotlinx.android.synthetic.main.dialog_qrcode.view.*
import kotlinx.android.synthetic.main.dialog_recommended_application.view.*
import java.io.File
import java.io.FileOutputStream
import kotlin.math.log

class MyQRCodeActivity : AppCompatActivity() {

    var qrCodePath = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_qrcode)
        tv_qr_code_back.setOnClickListener {
            finish()
        }
        val QRCodeImg=intent.getStringExtra("QRCodeImg")
        GlideImageLoader().displayImage(logo_iv,QRCodeImg)

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
            val pref = PreferenceManager.getDefaultSharedPreferences(this)
             qrCodePath = pref.getString("qrCodePath","")
            logo_iv.apply {
                val image= Bitmap.createBitmap(measuredWidth,measuredHeight,Bitmap.Config.ARGB_8888)

                    val name = QRCodeImg.substring(QRCodeImg.lastIndexOf("/"),QRCodeImg.lastIndexOf("?"))
                    val filePath = "${UnSerializeDataBase.filePath}/image"
                    //缓存的二维码路径与数据库的不一样
                    if(QRCodeImg!=name){
                        val file = File("${filePath}/${QRCodeImg}")
                        if(file.isFile && file.exists())
                            file.delete()
                        qrCodePath = name
                        pref.edit().putString("qrCodePath",qrCodePath).apply()
                    }
                    //判断文件夹是否存在 不存在就创建
                    val file = File(filePath)
                    file.mkdirs()
                    if(!file.exists()) {
                        file.mkdirs()
                    }

                    val path = "${filePath}/${name}"
//                            写入到文件夹下
                    val fos= FileOutputStream(path)
                    image.compress(Bitmap.CompressFormat.PNG,100,fos)
                    var result = "二维码保存失败"
                    Log.i("qrCode Url",path)
                    if(File(path).exists()){
                        MediaStore.Images.Media.insertImage(context.contentResolver,
                            path, name, null)
                        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(path)))
                        //通知相册更新
//                                val intent = Intent()
//                                intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//                                intent.setData(Uri.fromFile(File(path)))
//                                mView.context.sendBroadcast(intent)
                        result = "二维码保存成功"
                        val dialog = BottomSheetDialog(context!!)
                        val dialogView = LayoutInflater.from(context!!).inflate(R.layout.dialog_recommended_application,null)
                        dialogView.view_wx_time_line.setOnClickListener {
                            WXShare(context).shareImage(path, SendMessageToWX.Req.WXSceneTimeline)
                            dialog.dismiss()
                        }
                        dialogView.view_wx_session.setOnClickListener {
                            WXShare(context).shareImage(path, SendMessageToWX.Req.WXSceneSession)
                            dialog.dismiss()
                        }
                        dialogView.view_qq.setOnClickListener {
                            Log.i("headerImg",QRCodeImg)
                            val qqIntent = Intent(Intent.ACTION_SEND)
                            qqIntent.setPackage("com.tencent.mobileqq")
                            qqIntent.setType("image/*")
                            qqIntent.putExtra(Intent.EXTRA_STREAM, path)
                            qqIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                            qqIntent.setComponent(ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity"))
                            startActivity(Intent.createChooser(qqIntent, "推荐电企通"))
                            dialog.dismiss()
                        }
                        dialogView.tv_cancel.setOnClickListener {
                            dialog.dismiss()
                        }

                        dialog.setCanceledOnTouchOutside(false)
                        dialog.setContentView(dialogView)
                        dialog.show()
                    }

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
