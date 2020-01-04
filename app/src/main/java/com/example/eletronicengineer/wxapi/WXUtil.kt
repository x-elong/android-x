package com.example.eletronicengineer.wxapi

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

class WXUtil {
    companion object{
        fun bmpToByteArray(bmp: Bitmap,needRecycle:Boolean):ByteArray{
            val output = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, output)
            if (needRecycle) {
                bmp.recycle()
            }
            val result = output.toByteArray()
            try {
                output.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }
    }
}