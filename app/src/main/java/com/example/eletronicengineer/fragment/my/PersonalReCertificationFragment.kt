package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.util.Log
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
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.lcw.library.imagepicker.ImagePicker
import com.yancy.gallerypick.config.GalleryConfig
import com.yancy.gallerypick.config.GalleryPick
import com.yancy.gallerypick.inter.IHandlerCallBack
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_personal_re_certification.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import rx.Observer
import java.io.File

class PersonalReCertificationFragment : Fragment() {
    var selectImage = -1
    lateinit var mView: View
    var idCardPeopleMap = BitmapMap("", "identifyCardPathFront")
    var idCardNationMap = BitmapMap("", "identifyCardPathContrary")
    lateinit var mainType: String
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
            NetworkAdapter(mView.context).upImage(photoList[0],this@PersonalReCertificationFragment)
//            photoAdapter.notifyDataSetChanged()
        }
    }
    val glideImageLoader = GlideImageLoader()
    val galleryConfig = GalleryConfig.Builder()
        .imageLoader(glideImageLoader)    // ImageLoader 加载框架（必填）
        .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
        .provider("com.example.eletronicengineer.fileProvider")   // provider (必填)
//        .pathList(mImagePaths)                         // 记录已选的图片
        .multiSelect(false, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
        .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
        .crop(true, 1F, 1F, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
        .isShowCamera(true)                     // 是否现实相机按钮  默认：false
        .filePath("/Gallery/Pictures")          // 图片存放路径
        .build()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_personal_re_certification, container, false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        val result = NetworkAdapter().getDataUser()
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                val user = it.message.user
                if(user.isCredential){
                    mView.btn_personal_re_certification.visibility = View.VISIBLE
                    mView.tv_id_card_name_data.setText(user.name)
                    mView.tv_id_card_number_data.setText(user.identifyCard)
                    mView.et_id_card_address.setText(user.vipAddress)
                    GlideImageLoader().displayImage(mView.iv_id_card_people,user.identifyCardPathFront!!)
                    GlideImageLoader().displayImage(mView.iv_id_card_nation,user.identifyCardPathContrary!!)
                    idCardPeopleMap.path = user.identifyCardPathFront!!
                    idCardNationMap.path = user.identifyCardPathContrary!!
                }else{
//                        ToastHelper.mToast(mView.context,"")
                }
            },{
                ToastHelper.mToast(mView.context,"网络异常")
                it.printStackTrace()
            })
        mView.tv_personal_re_certification_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.iv_id_card_people.setOnClickListener {
            selectImage = 1
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity)
        }
        mView.iv_id_card_nation.setOnClickListener {
            selectImage = 2
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity)
        }
        mView.btn_personal_re_certification.setOnClickListener {
            val loadingDialog = LoadingDialog(mView.context,"正在验证...")
            loadingDialog.show()
            IDCardValidateUtil.mContext = mView.context
            if(mView.et_id_card_address.text.isBlank()){
                ToastHelper.mToast(mView.context,"身份证住址不能为空")
            }else if(idCardPeopleMap.path==""||idCardNationMap.path==""){
                    ToastHelper.mToast(mView.context,"身份证正反照不能为空")
                }
                else{
                    certification()
                }
            loadingDialog.dismiss()
        }
    }

    fun refresh(imagePath: String) {
        if(selectImage==1){
            idCardPeopleMap.path=imagePath
            glideImageLoader.displayImage(mView.iv_id_card_people,imagePath)
        }else if(selectImage==2){
            idCardNationMap.path=imagePath
            glideImageLoader.displayImage(mView.iv_id_card_nation,imagePath)
        }
    }


    fun certification() {
        val loadingDialog = LoadingDialog(mView.context,"正在提交...")
        loadingDialog.show()
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("mainType", "个人")
                .put("vipName", mView.tv_id_card_name_data.text)
                .put("identifyCard", mView.tv_id_card_number_data.text)
                .put("vipAddress",mView.et_id_card_address.text)
                .put("identifyCardPathFront", idCardPeopleMap.path)
                .put("identifyCardPathContrary", idCardNationMap.path)
            val requestBody = RequestBody.create(MediaType.parse("application/json"), json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result =
                putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.reCertification)
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                        loadingDialog.dismiss()
                        val jsonObject = JSONObject(it.string())
                        val code = jsonObject.getInt("code")
                        var result = ""
                        if(code==200 && jsonObject.getString("desc")=="OK"){
                            result = "提交成功"
                            activity!!.supportFragmentManager.popBackStackImmediate()
                        }
                        else
                            result = "提交失败"
                        ToastHelper.mToast(mView.context,result)
                    }, {
                        loadingDialog.dismiss()
                        ToastHelper.mToast(mView.context,"服务器异常")
                        it.printStackTrace()
                    })
        }
    }
}