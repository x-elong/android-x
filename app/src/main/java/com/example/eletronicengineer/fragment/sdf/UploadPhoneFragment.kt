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

    companion object{
        fun newInstance(args:Bundle): UploadPhoneFragment
        {
            val uploadPhoneFragment= UploadPhoneFragment()
            uploadPhoneFragment.arguments=args
            return uploadPhoneFragment
        }
    }
    var position=0
    private var mImagePaths: ArrayList<String> = ArrayList()
    lateinit var imageViewContent:ImageView
    lateinit var key:String
    var total = 3
    val glideLoader = GlideLoader()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.activity_photo_upload,container,false)
        key = arguments!!.getString("key")
        //获取当前照片的所在位置 若只有一张照片则不必传值 默认为0
        position=arguments!!.getInt("position")
        total = arguments!!.getInt("total")
        //refresh(mImagePaths!!)
        if(total==0)total=3
        view.tv_upload_photo_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        imageViewContent = view.iv_uplaod_photo_content

        for (j in 0 until total){
            mImagePaths.add("")
        }
        for (i in UnSerializeDataBase.imgList){
            if(i.key==key){
                val ImagesPath = i.path.split("|")
                mImagePaths[position]=ImagesPath[position]
                if(mImagePaths[position]!="")
                refresh(mImagePaths[position])
                break
            }
        }
        imageViewContent.setOnClickListener {
            if(mImagePaths[position]=="") {
                ImagePicker.getInstance()
                    .setTitle("图片")//设置标题
                    .showCamera(true)//设置是否显示拍照按钮
                    .showImage(true)//设置是否展示图片
                    .showVideo(true)//设置是否展示视频
                    .showVideo(true)//设置是否展示视频
                    .setSingleType(true)//设置图片视频不能同时选择
                    .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                    //.setImagePaths(mImagePaths)//保存上一次选择图片的状态，如果不需要可以忽略
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
    fun refresh(imagePaths: String) {
        if(mImagePaths[position]==""){
            var isInList = false
            for (j in UnSerializeDataBase.imgList){
                if(j.key==key){
                    var imagesPath:ArrayList<String> = ArrayList(j.path.split("|"))
                    imagesPath[position] = imagePaths
                    var str = ""
                    for (i in 0 until imagesPath.size-1){
                        str +=imagesPath[i]+"|"
                    }
                    str+=imagesPath[imagesPath.size-1]
                    j.path=str
                    isInList=true
                    break
                }
            }
            if(!isInList){
                var imagesPath = ""
                for (j in 0 until total){
                    if(j==position)
                        imagesPath+=imagePaths
                    if(j!=total-1)
                    imagesPath+="|"
                }

                val Paths = imagesPath.split("|")
                UnSerializeDataBase.imgList.add(BitmapMap(imagesPath,key))
            }
            mImagePaths[position] = imagePaths
        }
        glideLoader.loadImage(imageViewContent,imagePaths)
        glideLoader.clearMemoryCache()
    }
}