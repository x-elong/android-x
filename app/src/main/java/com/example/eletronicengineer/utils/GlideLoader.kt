package com.example.eletronicengineer.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.example.eletronicengineer.R
import com.lcw.library.imagepicker.utils.ImageLoader
import de.hdodenhof.circleimageview.CircleImageView


class GlideLoader : ImageLoader{

    var scaleType = ImageView.ScaleType.CENTER_INSIDE
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
        if(imageView is CircleImageView)
            scaleType = ImageView.ScaleType.CENTER_CROP
        imageView!!.scaleType = scaleType
        Glide.with(imageView.context).load(imagePath).apply(mOptions).into(imageView)
    }
    fun loadImage(imageView: ImageView?, imageID: Int) {
        //小图加载
        if(imageView is CircleImageView)
            scaleType = ImageView.ScaleType.CENTER_CROP
        imageView!!.scaleType = scaleType
        Glide.with(imageView.context).load(imageID).apply(mOptions).into(imageView)
    }
    override fun loadPreImage(imageView: ImageView?, imagePath: String?) {
        //大图加载
        if(imageView is CircleImageView)
            scaleType = ImageView.ScaleType.CENTER_CROP
        imageView!!.scaleType = scaleType
        Glide.with(imageView.context).load(imagePath).apply(mPreOptions).into(imageView)
    }
    fun loadPreImage(imageView: ImageView?, imageID: Int) {
        //大图加载
        if(imageView is CircleImageView)
            scaleType = ImageView.ScaleType.CENTER_CROP
        imageView!!.scaleType = scaleType
        Glide.with(imageView.context).load(imageID).apply(mPreOptions).into(imageView)
    }
    override fun clearMemoryCache() {
        //清理缓存
    }
}