package com.example.eletronicengineer.fragment.my

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ImageDisplayActivity
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.BitmapMap
import com.example.eletronicengineer.utils.GlideLoader
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lcw.library.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.dialog_upload.view.*
import kotlinx.android.synthetic.main.fragment_business_license.view.*

class BusinessLicenseFragment :Fragment(){
    val glideLoader = GlideLoader()
    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_business_license,container,false)
        initFragment()
        //glideLoader.loadImage(mView.iv_business_license,"")
        return mView
    }

    private fun initFragment() {
        UnSerializeDataBase.imgList.clear()
        UnSerializeDataBase.imgList.add(BitmapMap("",""))
        mView.tv_business_license_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.iv_business_license.setOnClickListener{
            if(UnSerializeDataBase.imgList.last().path!=""){
                val intent = Intent(activity!!,ImageDisplayActivity::class.java)
                intent.putExtra("imagePath",UnSerializeDataBase.imgList.last().path)
                startActivity(intent)
            }
        }
        mView.btn_upload.setOnClickListener {
            val dialog = BottomSheetDialog(context!!)
            val dialogView = LayoutInflater.from(context!!).inflate(R.layout.dialog_upload,null)
            dialogView.btn_photograph.setOnClickListener {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                // UnSerializeDataBase.imgList.add(BitmapMap("", mData[5].additionalKey))
                activity!!.startActivityForResult(intent, Constants.RequestCode.REQUEST_PHOTOGRAPHY.ordinal)
                dialog.dismiss()
            }
            dialogView.btn_album.setOnClickListener {
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
                dialog.dismiss()
            }
            dialogView.btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(dialogView)
            dialog.show()
        }
    }
    fun refresh(mImagePath:String){
        UnSerializeDataBase.imgList.last().path = mImagePath
        glideLoader.loadImage(mView.iv_business_license,mImagePath)
    }
}