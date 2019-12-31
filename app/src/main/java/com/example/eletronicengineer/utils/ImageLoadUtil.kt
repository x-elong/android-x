package com.example.eletronicengineer.utils

import android.app.Activity
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class ImageLoadUtil {
    companion object{
        fun scaledDrawable(res: Resources, drawable: Drawable?, width: Int, height: Int):Drawable{
            val bitmapDrawable = drawable as BitmapDrawable?
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds=true
            Log.i("bitmap width and height",""+bitmapDrawable!!.bitmap.width +"  " + bitmapDrawable.bitmap.height)
            Log.i("bitmap width and height",""+bitmapDrawable.bitmap.getScaledWidth(width) +"  " +bitmapDrawable.bitmap.getScaledHeight(height))
            val scaleWidth = 1.0*bitmapDrawable.bitmap.getScaledWidth(width)/bitmapDrawable.bitmap.width
            val scaleHeight = 1.0*bitmapDrawable.bitmap.getScaledWidth(height)/bitmapDrawable.bitmap.height
            val scale = if(scaleWidth>scaleHeight) scaleWidth else scaleHeight
            Log.i("scale","${scale} ${width/scaleWidth} ${height/scaleHeight}")
            val b = Bitmap.createScaledBitmap(bitmapDrawable.bitmap,(width/scaleWidth).toInt(),(height/scaleHeight).toInt(),false)
            Log.i("Bitmap Byte Count",b.byteCount.toString()+"")
            return BitmapDrawable(res,b)
        }

        fun loadBackgound(v: View, context: Context,resId: Int){
            //v.measure(View.MeasureSpec.AT_MOST ,View.MeasureSpec.EXACTLY)
            val width = v.measuredWidth
            val height = v.measuredHeight
            Log.i("view width","" +width)
            Log.i("view height",""+height)
            Glide.with(context).load(scaledDrawable(
                context.resources,
                ContextCompat.getDrawable(context,resId) ,width ,height)).override(width,height).into(object :
                CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    v.background = resource
                }

            })
        }
    }
}