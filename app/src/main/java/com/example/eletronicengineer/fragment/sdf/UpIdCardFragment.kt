package com.example.eletronicengineer.fragment.sdf

import android.content.Intent
import android.os.Bundle
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
import com.lcw.library.imagepicker.ImagePicker
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
                ImagePicker.getInstance()
                    .setTitle("图片")//设置标题
                    .showCamera(true)//设置是否显示拍照按钮
                    .showImage(true)//设置是否展示图片
                    .showVideo(true)//设置是否展示视频
                    .showVideo(true)//设置是否展示视频
                    .setSingleType(true)//设置图片视频不能同时选择
                    .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                    .setImagePaths(mImagePaths1)//保存上一次选择图片的状态，如果不需要可以忽略
                    .setImageLoader(glideLoader)//设置自定义图片加载器
                    .start(activity, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
                //REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
            } else {
                val intent = Intent(activity, ImageDisplayActivity::class.java)
                intent.putExtra("mImagePaths", mImagePaths1)
                activity!!.startActivity(intent)
            }
        }

        mView.iv_id_nation_content.setOnClickListener {
            if (mImagePaths2.size == 0) {
                selectImage=1
                ImagePicker.getInstance()
                    .setTitle("图片")//设置标题
                    .showCamera(true)//设置是否显示拍照按钮
                    .showImage(true)//设置是否展示图片
                    .showVideo(true)//设置是否展示视频
                    .showVideo(true)//设置是否展示视频
                    .setSingleType(true)//设置图片视频不能同时选择
                    .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                    .setImagePaths(mImagePaths2)//保存上一次选择图片的状态，如果不需要可以忽略
                    .setImageLoader(glideLoader)//设置自定义图片加载器
                    .start(activity, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
                //REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
            } else {
                val intent = Intent(activity, ImageDisplayActivity::class.java)
                intent.putExtra("mImagePaths", mImagePaths2)
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