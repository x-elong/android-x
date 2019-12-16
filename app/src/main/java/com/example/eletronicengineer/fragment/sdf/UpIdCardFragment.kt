package com.example.eletronicengineer.fragment.sdf

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ImageDisplayActivity
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
import kotlinx.android.synthetic.main.activity_id_card_upload.view.*

class UpIdCardFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):UpIdCardFragment{
            val upIdCardFragment = UpIdCardFragment()
            upIdCardFragment.arguments=args
            return upIdCardFragment
        }
    }
    lateinit var mView: View
    var key:String = ""
    val glideLoader = GlideLoader()
    var selectImage=-1
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
            NetworkAdapter(mView.context).upImage(photoList[0],this@UpIdCardFragment)
//            photoAdapter.notifyDataSetChanged()
        }
    }
    val glideImageLoader = GlideImageLoader()
    val galleryConfig = GalleryConfig.Builder()
        .imageLoader(glideImageLoader)    // ImageLoader 加载框架（必填）
        .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
        .provider("com.example.eletronicengineer.fileProvider")   // provider (必填)
//        .pathList(mImagePaths)                         // 记录已选的图片
        .multiSelect(false, 2)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
        .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
        .crop(true, 1F, 1F, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
        .isShowCamera(true)                     // 是否现实相机按钮  默认：false
        .filePath("/Gallery/Pictures")          // 图片存放路径
        .build()

    private var mImagePaths1: ArrayList<String> = ArrayList()
    private var mImagePaths2: ArrayList<String> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.activity_id_card_upload,container,false)
        key = arguments!!.getString("key")
        mView.tv_id_card_upload_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        for (i in UnSerializeDataBase.imgList){
            if(i.key==key){
                val mImagePath = i.path.split("|")
                if(mImagePath[0]!=""){
                    mImagePaths1.add(mImagePath[0])
                    refresh(mImagePaths1)
                }
                if(mImagePath[1]!=""){
                    mImagePaths2.add(mImagePath[1])
                    refresh(mImagePaths2)
                }
                break
            }
        }
        mView.iv_id_people_content.setOnClickListener {
            if (mImagePaths1.size == 0) {
                selectImage=0
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity)
                //REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
            } else {
                val intent = Intent(activity, ImageDisplayActivity::class.java)
                intent.putExtra("imagePath", mImagePaths1[0])
                activity!!.startActivity(intent)
            }
        }

        mView.iv_id_nation_content.setOnClickListener {
            if (mImagePaths2.size == 0) {
                selectImage=1
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity)
                //REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
            } else {
                val intent = Intent(activity, ImageDisplayActivity::class.java)
                intent.putExtra("imagePath", mImagePaths2[0])
                activity!!.startActivity(intent)
            }
        }
        return mView
    }
    fun refresh(imagePaths: ArrayList<String>) {
        if(selectImage==-1){
            if(mImagePaths1.size!=0){
                glideLoader.loadImage(mView.iv_id_people_content,mImagePaths1[0])
            }
            if(mImagePaths2.size!=0){
                glideLoader.loadImage(mView.iv_id_nation_content,mImagePaths2[0])
            }
        }else if(selectImage==0){
            if(mImagePaths1.size==0){
                var isInList = false
                for (j in UnSerializeDataBase.imgList){
                    if(j.key==key){
                        val ImagePath = j.path.split("|")
                        if(ImagePath.size>1)
                            j.path = imagePaths[0] + "|" + ImagePath[1]
                        else
                            j.path=imagePaths[0]+"|"
                        isInList = true
                    }
                }
                if(!isInList)
                UnSerializeDataBase.imgList.add(BitmapMap(imagePaths[0]+"|",key))
                mImagePaths1 = imagePaths
                glideLoader.loadImage(mView.iv_id_people_content,imagePaths[0])
            }
        }else{
            if(mImagePaths2.size==0){
                var isInList = false
                for (j in UnSerializeDataBase.imgList){
                    if(j.key==key){
                        val ImagePath = j.path.split("|")
                        if(ImagePath.size>1)
                            j.path = ImagePath[0] + "|" + imagePaths[0]
                        else
                            j.path="|"+imagePaths[0]
                        isInList = true
                    }
                }
                if(!isInList)
                UnSerializeDataBase.imgList.add(BitmapMap("|"+imagePaths[0],key))
                mImagePaths2 = imagePaths

                glideLoader.loadImage(mView.iv_id_nation_content,imagePaths[0])
            }
        }
        glideLoader.clearMemoryCache()
    }
}