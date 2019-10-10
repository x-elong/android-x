package com.example.eletronicengineer.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.item_chat.view.*
import kotlinx.android.synthetic.main.layout_my_qrcode.view.*

class MyQrCodeView:View
{
    /*成员*/
    var qrName:String?
    var qrPhone:String?
    var qrLocation:String?
    var qrAvatar:Drawable?
    var qrContent:Drawable?
    /**
     * @param content 二维码的图片
     * @param name 用户名字
     * @param phone 用户电话
     * @param location 用户位置
     * @param avatar 用户头像
     * */
    constructor(context:Context,name:String,phone:String,location:String,avatar:Drawable,content:Drawable):super(context)
    {
        val view=LayoutInflater.from(context).inflate(R.layout.layout_my_qrcode,null)
        qrName=name
        qrPhone=phone
        qrLocation=location
        qrAvatar=avatar
        qrContent=content
    }
    constructor(context: Context):super(context)
    {
        qrName=null
        qrPhone=null
        qrLocation=null
        qrAvatar=null
        qrContent=null
    }
    fun updateUI()
    {
        tv_qr_code_name.text=qrName
        tv_qr_code_phone.text=qrPhone
        tv_qr_code_location.text=qrLocation
        tv_chat_avatar.background=qrAvatar
        tv_qr_code_content.background=qrContent
    }
}