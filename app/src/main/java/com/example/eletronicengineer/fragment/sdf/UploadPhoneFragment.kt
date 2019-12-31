package com.example.eletronicengineer.fragment.sdf

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ImageDisplayActivity
import com.example.eletronicengineer.activity.MyCertificationActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.BitmapMap
import com.example.eletronicengineer.utils.GlideImageLoader
import com.example.eletronicengineer.utils.GlideLoader
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.lcw.library.imagepicker.ImagePicker
import com.yancy.gallerypick.config.GalleryConfig
import com.yancy.gallerypick.config.GalleryPick
import com.yancy.gallerypick.inter.IHandlerCallBack
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
    val path = ArrayList<String>()
    val iHandlerCallBack = object : IHandlerCallBack {
        override fun onFinish() {
        }

        override fun onCancel() {
        }

        override fun onError() {
        }

        override fun onStart() {
        }

        override fun onSuccess(photoList: MutableList<String>) {
//            val fragment=activity!!.supportFragmentManager.findFragmentByTag("Capture")!!
            NetworkAdapter(mView.context).upImage(photoList[0],this@UploadPhoneFragment)
//            photoAdapter.notifyDataSetChanged()
        }
    }
    private var mImagePaths: ArrayList<String> = ArrayList()
    lateinit var imageViewContent:ImageView
    lateinit var mView: View
    lateinit var key:String
    var total = 3
    val glideImageLoader = GlideImageLoader()
    val galleryConfig = GalleryConfig.Builder()
        .imageLoader(glideImageLoader)    // ImageLoader 加载框架（必填）
        .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
        .provider("com.example.eletronicengineer.fileProvider")   // provider (必填)
        .pathList(mImagePaths)                         // 记录已选的图片
        .multiSelect(false, 1)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
        .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
        .crop(true, 1F, 1F, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
        .isShowCamera(true)                     // 是否现实相机按钮  默认：false
        .filePath("/Gallery/Pictures")          // 图片存放路径
        .build()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView =inflater.inflate(R.layout.activity_photo_upload,container,false)
        key = arguments!!.getString("key")
        //获取当前照片的所在位置 若只有一张照片则不必传值 默认为0
        position=arguments!!.getInt("position",0)
        total = arguments!!.getInt("total")
        if(activity is MyCertificationActivity){
            mView.tv_upload_photo_title.setText(arguments!!.getString("title")+"上传")
        }
        //refresh(mImagePaths!!)
        if(total==0)total=3
        mView.tv_upload_photo_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }

        imageViewContent = mView.iv_uplaod_photo_content

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
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity)
            }else{
                val intent = Intent(activity,ImageDisplayActivity::class.java)
                intent.putExtra("imagePath",mImagePaths[0])
                activity!!.startActivity(intent)
            }
            //activity!!.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            activity!!.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        return mView
    }
    fun refresh(imagePaths: String) {
        Log.i("",imagePaths)
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
        glideImageLoader.displayImage(imageViewContent,imagePaths)
        glideImageLoader.clearMemoryCache()
    }
}