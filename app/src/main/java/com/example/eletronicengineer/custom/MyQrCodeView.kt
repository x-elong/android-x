package com.example.eletronicengineer.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.layout_my_qrcode.view.*

class MyQrCodeView:View
{
    /*成员*/
    var qrName:String?
    var qrPhone:String?
    var qrLocation:String?
    var qrAvatar:Drawable?
    var qrContent:Drawable?
    lateinit var view: View
    /**
     * @param content 二维码的图片
     * @param name 用户名字
     * @param phone 用户电话
     * @param location 用户位置
     * @param avatar 用户头像
     * */
    constructor(context:Context,name:String,phone:String,location:String,avatar:Drawable,content:Drawable):super(context)
    {
        view=LayoutInflater.from(context).inflate(R.layout.layout_my_qrcode,null)
        qrName=name
        qrPhone=phone
        qrLocation=location
        qrAvatar=avatar
        qrContent=content
    }
    constructor(context: Context):super(context)
    {
        qrName=""
        qrPhone=""
        qrLocation=""
        qrAvatar=context.getDrawable(R.drawable.ic_smile)
        qrContent=context.getDrawable(R.drawable.ic_smile)
    }
    constructor(context: Context,attrs: AttributeSet):super(context,attrs){
        view =LayoutInflater.from(context).inflate(R.layout.activity_login,null)
        qrName=""
        qrPhone=""
        qrLocation=""
        qrAvatar=context.getDrawable(R.drawable.ic_smile)
        qrContent=context.getDrawable(R.drawable.ic_smile)
    }

    override fun onDraw(canvas: Canvas?){
        super.onDraw(canvas)

    }
    fun updateUI()
    {
        view.tv_qr_code_name.text=qrName
        view.tv_qr_code_phone.text=qrPhone
        view.tv_qr_code_location.text=qrLocation
        view.tv_qr_code_avatar.background=qrAvatar
        view.tv_qr_code_content.background=qrContent
    }
}