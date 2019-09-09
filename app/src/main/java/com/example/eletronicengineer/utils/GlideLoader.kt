package com.example.eletronicengineer.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.example.eletronicengineer.R
import com.lcw.library.imagepicker.utils.ImageLoader


class GlideLoader : ImageLoader{

    val mOptions = RequestOptions().centerCrop()
        .dontAnimate()
        .format(DecodeFormat.PREFER_RGB_565)
        .placeholder(R.mipmap.icon_image_default)
        .error(R.mipmap.icon_image_error)
    val mPreOptions = RequestOptions()
        .skipMemoryCache(true)
        .error(R.mipmap.icon_image_error)
    override fun loadImage(imageView: ImageView?, imagePath: String?) {
        //小图加载
        imageView!!.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(imageView!!.context).load(imagePath).apply(mOptions).into(imageView)
    }
    override fun loadPreImage(imageView: ImageView?, imagePath: String?) {
        //大图加载
        imageView!!.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(imageView!!.context).load(imagePath).apply(mPreOptions).into(imageView)
    }
    override fun clearMemoryCache() {
        //清理缓存
    }
}