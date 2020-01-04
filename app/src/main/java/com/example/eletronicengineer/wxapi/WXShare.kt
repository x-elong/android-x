package com.example.eletronicengineer.wxapi

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.eletronicengineer.wxapi.WXUtil.Companion.bmpToByteArray
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class WXShare {

    private val APP_ID = "wx69d595e7793f3fb9"
    lateinit var api:IWXAPI
    lateinit var context: Context
    constructor(context: Context){
        api = WXAPIFactory.createWXAPI(context, null)
        this.context = context
        api.registerApp(APP_ID)
    }
    fun shareImage(imgPath:String,scene:Int){
        val bmp = BitmapFactory.decodeFile(imgPath)
        val imgObj = WXImageObject(bmp)
        val thumbBmp = Bitmap.createScaledBitmap(bmp, 200, 200, true)
        bmp.recycle()
        val msg = WXMediaMessage(imgObj)
        msg.thumbData = bmpToByteArray(thumbBmp, true)
        val req = SendMessageToWX.Req()
        req.transaction = System.currentTimeMillis().toString()
        req.message = msg
        req.scene = scene
        api.sendReq(req)
    }
    fun imgShare(context: Context,imgPath:String){
        val bmp = BitmapFactory.decodeFile(imgPath)
        val imgObj = WXImageObject(bmp)
        val msg = WXMediaMessage()
        msg.mediaObject = imgObj
        val thumbBmp = Bitmap.createScaledBitmap(bmp, 200, 200, true)
        bmp.recycle()
    }

}