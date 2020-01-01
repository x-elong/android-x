package com.example.eletronicengineer.wxapi

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage

class WXShare {
    companion object{
        fun imgShare(context: Context,imgPath:String){
            val bmp = BitmapFactory.decodeFile(imgPath)
            val imgObj = WXImageObject(bmp)
            val msg = WXMediaMessage()
            msg.mediaObject = imgObj
            val thumbBmp = Bitmap.createScaledBitmap(bmp, 200, 200, true)
            bmp.recycle()

        }
    }
}