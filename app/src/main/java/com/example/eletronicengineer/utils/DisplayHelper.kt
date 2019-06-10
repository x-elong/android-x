package com.example.eletronicengineer.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.Toast
import com.example.eletronicengineer.R

/**
 * 显示助手 提供dp px sp互相转换的函数
 */
class DisplayHelper
{
    companion object
    {
        fun addWaterCaption(context: Context,bmp: Bitmap, vararg content:String): Drawable
        {
            val canvas= Canvas(bmp)
            val contentPaint= Paint()

            contentPaint.textSize=context.resources.getDimensionPixelSize(R.dimen.font_tv_normal_16).toFloat()

            //用rect获取当前文本的宽和高 用于定位

            var rightMargin=context.resources.getDimensionPixelSize(R.dimen.general_10)
            var bottomMargin=context.resources.getDimensionPixelSize(R.dimen.general_10)
            contentPaint.color= Color.BLACK

            val screenWidth=context.resources.displayMetrics.widthPixels
            val screenHeight=context.resources.displayMetrics.heightPixels
            var preY=-1
            for (text in content)
            {
                var rect= Rect()
                contentPaint.getTextBounds(text,0,text.length,rect)
                var textWidth=rect.width()
                var textHeight=rect.height()
                if (preY==-1)
                {
                    canvas.drawText(text,(screenWidth-rightMargin-textWidth).toFloat(),(screenHeight-bottomMargin-textHeight).toFloat(),contentPaint)
                    preY=screenHeight-bottomMargin-textHeight
                }
                else
                {
                    canvas.drawText(text,(screenWidth-rightMargin-textWidth).toFloat(),(preY-bottomMargin-textHeight).toFloat(),contentPaint)
                    preY-=bottomMargin+textHeight
                }
                if (preY<0)
                {
                    Toast.makeText(context,"文本已超出范围!",Toast.LENGTH_SHORT).show()
                    return BitmapDrawable(context.resources,bmp)
                }
            }
            val bitmapDrawable= BitmapDrawable(context.resources,bmp)
            return bitmapDrawable
        }
    }
    fun PixelToDip(context: Context,pxValue:Float):Int
    {
        val scale=context.resources.displayMetrics.density
        return (pxValue/scale+0.5f).toInt()
    }
    fun DipToPixel(context: Context,dpValue:Float):Int
    {
        val scale=context.resources.displayMetrics.density
        return (dpValue*scale+0.5f).toInt()
    }
    fun PixelToSp(context: Context,pxValue: Float):Int
    {
        val fontScale=context.resources.displayMetrics.scaledDensity
        return (pxValue/fontScale+0.5f).toInt()
    }
    fun SpToPixel(context: Context,spValue:Float):Int
    {
        val fontScale=context.resources.displayMetrics.scaledDensity
        return (spValue*fontScale+0.5f).toInt()
    }

}