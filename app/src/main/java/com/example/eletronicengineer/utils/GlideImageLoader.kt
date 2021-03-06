package com.example.eletronicengineer.utils

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.eletronicengineer.R
import com.yancy.gallerypick.inter.ImageLoader
import com.yancy.gallerypick.widget.GalleryImageView


class GlideImageLoader : ImageLoader {
    override fun displayImage(
        activity: Activity?,
        context: Context?,
        path: String?,
        galleryImageView: GalleryImageView?,
        width: Int,
        height: Int
    ) {
        Glide.with(context!!)
            .load(path)
            .placeholder(R.mipmap.gallery_pick_photo)
            .centerCrop()
            .into(galleryImageView!!)
    }
    fun displayImage(activity: Activity?,context: Context?, path: String?, galleryImageView: GalleryImageView?){
        displayImage(activity,context,path,galleryImageView,0,0)
    }
    fun displayImage(galleryImageView: ImageView, path: String){
//         galleryImageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        val imagePath =
            if(path.contains("www.ycdlfw.com")) {
                if(path.contains("http://") )
                    path
                else
                    "http://${path}"
            } else
                path
        Glide.with(galleryImageView.context).load(imagePath).placeholder(R.mipmap.gallery_pick_photo).centerCrop()
            .into(galleryImageView)
    }
    fun displayImage(galleryImageView: ImageView, path: Int){
        Glide.with(galleryImageView.context).load(path).placeholder(R.mipmap.gallery_pick_photo).centerCrop()
            .into(galleryImageView)
    }
    override fun clearMemoryCache() {

    }

}