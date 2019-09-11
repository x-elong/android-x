package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.BitmapMap
import com.example.eletronicengineer.utils.GlideLoader
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.lcw.library.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.fragment_personal_certification.view.*

class PersonalCertificationFragment :Fragment(){
    val glideLoader = GlideLoader()
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    var selectImage=-1
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_personal_certification,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        UnSerializeDataBase.imgList.clear()
        UnSerializeDataBase.imgList.add(BitmapMap("","people"))
        UnSerializeDataBase.imgList.add(BitmapMap("","nation"))
        mView.iv_id_card_people.setOnClickListener {
            selectImage=1
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
        }
        mView.iv_id_card_nation.setOnClickListener {
            selectImage=2
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
        }
        mView.btn_personal_certification.setOnClickListener {
            val networkAdapter = NetworkAdapter(mMultiStyleItemList,context!!)
            if(networkAdapter.check()){
                if(UnSerializeDataBase.imgList[0].path==""||UnSerializeDataBase.imgList[1].path==""){
                    val toast = Toast.makeText(context,"身份证正反照不能为空",Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER,0,0)
                    toast.show()
                }else{

                }
            }
        }
        mMultiStyleItemList.clear()
        var item = MultiStyleItem(MultiStyleItem.Options.SELECT_DIALOG,arrayListOf("中国大陆","港澳台地区"),"国籍(地区):")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"真实姓名:","请填写真实姓名")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"身份证号码:","请输入身份证号码")
        mMultiStyleItemList.add(item)
        mView.rv_personal_certification_content.adapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_personal_certification_content.layoutManager= LinearLayoutManager(context)
    }
    fun refresh(imagePath:String){
        if(selectImage==1){
            UnSerializeDataBase.imgList[0].path=imagePath
            glideLoader.loadImage(mView.iv_id_card_people,imagePath)
        }else if(selectImage==2){
            UnSerializeDataBase.imgList[1].path=imagePath
            glideLoader.loadImage(mView.iv_id_card_nation,imagePath)
        }
    }
}