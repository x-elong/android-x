package com.example.eletronicengineer.fragment.sdf

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ImageDisplayActivity
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.BitmapMap
import com.example.eletronicengineer.utils.GlideLoader
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.lcw.library.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_photo_upload.view.*
import java.io.File
import java.io.FileOutputStream
import java.util.ArrayList

class UploadPhoneFragment:Fragment(){

    private var mImagePaths: ArrayList<String>? = null
    lateinit var imageViewContent:ImageView
    val glideLoader = GlideLoader()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val key=arguments?.getString("key")
        val view=inflater.inflate(R.layout.activity_photo_upload,container,false)
        view.tv_upload_photo_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        imageViewContent = view.iv_uplaod_photo_content
        imageViewContent.setOnClickListener {
            if(mImagePaths == null) {
                ImagePicker.getInstance()
                    .setTitle("图片")//设置标题
                    .showCamera(true)//设置是否显示拍照按钮
                    .showImage(true)//设置是否展示图片
                    .showVideo(true)//设置是否展示视频
                    .showVideo(true)//设置是否展示视频
                    .setSingleType(true)//设置图片视频不能同时选择
                    .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                    .setImagePaths(mImagePaths)//保存上一次选择图片的状态，如果不需要可以忽略
                    .setImageLoader(glideLoader)//设置自定义图片加载器
                    .start(activity, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
                //REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
            }else{
                val intent = Intent(activity,ImageDisplayActivity::class.java)
                intent.putExtra("mImagePaths",mImagePaths)
                activity!!.startActivity(intent)
            }
            //activity!!.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            activity!!.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        return view
    }
    fun refresh(imagePaths: ArrayList<String>) {
        mImagePaths = imagePaths
        glideLoader!!.loadImage(imageViewContent,imagePaths.get(0))
        glideLoader.clearMemoryCache()
    }

}