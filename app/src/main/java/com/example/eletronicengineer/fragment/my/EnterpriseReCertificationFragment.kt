package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyCertificationActivity
import com.example.eletronicengineer.adapter.ImageAdapter
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.aninterface.Image
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.startSendMessage
import com.lcw.library.imagepicker.ImagePicker
import com.yancy.gallerypick.config.GalleryConfig
import com.yancy.gallerypick.config.GalleryPick
import com.yancy.gallerypick.inter.IHandlerCallBack
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_enterprise_re_certification.view.*
import kotlinx.android.synthetic.main.fragment_information_certification.*
import kotlinx.android.synthetic.main.fragment_re_certification.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File

class EnterpriseReCertificationFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):EnterpriseReCertificationFragment{
            val enterpriseReCertificationFragment = EnterpriseReCertificationFragment()
            enterpriseReCertificationFragment.arguments = args
            return enterpriseReCertificationFragment
        }
    }
    var selectImage=-1
    lateinit var mView:View
    lateinit var organizationName:String
    var mainType = ""
    var certificationStatus = ""
    var businessLicenseMap = BitmapMap("","businessLicense")
    var officialPictureMap = BitmapMap("","officialPicturePath")
    var legalIdCardPeopleMap = BitmapMap("","legalPersonPositivePath")
    var legalIdCardNationMap = BitmapMap("","legalPersonNegativePath")
    var administratorIdCardPeopleMap = BitmapMap("","identifyCardPathFront")
    var administratorIdCardNationMap = BitmapMap("","identifyCardPathContrary")

    var isCertification = false
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
            NetworkAdapter(mView.context).upImage(photoList[0],this@EnterpriseReCertificationFragment)
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
        mView = inflater.inflate(R.layout.fragment_re_certification,container,false)
        organizationName = arguments!!.getString("organizationName").replace("/"," ")
        certificationStatus = arguments!!.getString("certificationStatus")
        initFragment()
        return mView
    }
    private fun initFragment() {
        mView.tv_enterprise_re_certification_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.iv_legal_id_card_people.setOnClickListener{
            selectImage=1
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity)
        }
        mView.iv_legal_id_card_nation.setOnClickListener {
            selectImage=2
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity)
        }
//        mView.iv_administrator_id_card_people.setOnClickListener{
//            selectImage=3
//            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity)
//        }
//        mView.iv_administrator_id_card_nation.setOnClickListener {
//            selectImage=4
//            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity)
//        }
        mView.iv_business_license.setOnClickListener{
            selectImage=5
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity)
        }
        mView.iv_official_picture.setOnClickListener {
            selectImage=6
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity)
        }
        val results = Observable.create<RequestBody> {
            val json = JSONObject().put("organizationName",organizationName)
                .put("certificationStatus",certificationStatus)

            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.certificationMore)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if (code == 200) {
                        val js = jsonObject.getJSONObject("message")
                        result = "获取数据成功"
                        mainType = js.getString("mainType")
                        mView.tv_main_type.setText(mainType)
                        mView.tv_institution_name_data.setText (js.getString("organizationName"))

                        mView.tv_social_credit_code_data.setText (js.getString("socialCreditCode"))

                        mView.et_main_code.setText (js.getString("mainBusiness"))
                        businessLicenseMap.path = js.getString("businessLicense")
                        officialPictureMap.path = js.getString("officialPicturePath")

                        legalIdCardPeopleMap.path = js.getString("legalPersonPositivePath")
                        legalIdCardNationMap.path = js.getString("legalPersonNegativePath")

                        administratorIdCardPeopleMap.path = js.getString("identifyCardPathFront")
                        administratorIdCardNationMap.path = js.getString("identifyCardPathContrary")

                        GlideImageLoader().displayImage(mView.iv_business_license,businessLicenseMap.path)
                        GlideImageLoader().displayImage(mView.iv_official_picture,officialPictureMap.path)
                        GlideImageLoader().displayImage(mView.iv_legal_id_card_people,legalIdCardPeopleMap.path)
                        GlideImageLoader().displayImage(mView.iv_legal_id_card_nation,legalIdCardNationMap.path)
                        mView.tv_administrator_name_data.text = js.getString("vipName")
                        mView.tv_administrator_number_data.text = js.getString("identifyCard")
                        GlideImageLoader().displayImage(mView.iv_administrator_id_card_people,administratorIdCardPeopleMap.path)
                        GlideImageLoader().displayImage(mView.iv_administrator_id_card_nation,administratorIdCardNationMap.path)
                    } else{
                        result = "获取数据失败"
                        ImageLoadUtil.loadBackgound(mView.iv_business_license,mView.context,R.drawable.business_license)
                        ImageLoadUtil.loadBackgound(mView.iv_official_picture,mView.context,R.drawable.official_picture)
                        ImageLoadUtil.loadBackgound(mView.iv_legal_id_card_people,mView.context,R.drawable.id_card_people)
                        ImageLoadUtil.loadBackgound(mView.iv_legal_id_card_nation,mView.context,R.drawable.id_card_nation)
                        ImageLoadUtil.loadBackgound(mView.iv_administrator_id_card_people,mView.context,R.drawable.id_card_people)
                        ImageLoadUtil.loadBackgound(mView.iv_administrator_id_card_nation,mView.context,R.drawable.id_card_nation)
                    }
                    if(result!="获取数据成功")
                    ToastHelper.mToast(context!!, result)
                }, {
                    ImageLoadUtil.loadBackgound(mView.iv_business_license,mView.context,R.drawable.business_license)
                    ImageLoadUtil.loadBackgound(mView.iv_official_picture,mView.context,R.drawable.official_picture)
                    ImageLoadUtil.loadBackgound(mView.iv_legal_id_card_people,mView.context,R.drawable.id_card_people)
                    ImageLoadUtil.loadBackgound(mView.iv_legal_id_card_nation,mView.context,R.drawable.id_card_nation)
                    ImageLoadUtil.loadBackgound(mView.iv_administrator_id_card_people,mView.context,R.drawable.id_card_people)
                    ImageLoadUtil.loadBackgound(mView.iv_administrator_id_card_nation,mView.context,R.drawable.id_card_nation)
                    ToastHelper.mToast(context!!, "服务器异常")
                    it.printStackTrace()
                })
        }
//        val result = Observable.create<RequestBody> {
//            val json = JSONObject().put("mainType","个人")
//            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
//            it.onNext(requestBody)
//        }.subscribe {
//            val result = startSendMessage(it,UnSerializeDataBase.mineBasePath+ Constants.HttpUrlPath.My.certificationMore)
//                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
//                    val jsonObject = JSONObject(it.string())
//                    val code = jsonObject.getInt("code")
//                    var result = ""
//                    if(code==200){
//                        val js = jsonObject.getJSONObject("message")
//                        result = "管理员已认证"
//                        mView.tv_administrator_name_data.setText(js.getString("vipName"))
//                        mView.tv_administrator_number_data.setText(js.getString("identifyCard"))
//                        GlideLoader().loadImage(mView.iv_administrator_id_card_people,js.getString("identifyCardPathFront"))
//                        GlideLoader().loadImage(mView.iv_administrator_id_card_nation,js.getString("identifyCardPathContrary"))
//                        administratorIdCardPeopleMap.path = js.getString("identifyCardPathFront")
//                        administratorIdCardNationMap.path = js.getString("identifyCardPathContrary")
//                        isCertification = true
//                    }
//                    else{
//                        result = "管理员未认证"
//                        mView.iv_administrator_id_card_people.setOnClickListener{
//                            selectImage=3
//                            ImagePicker.getInstance()
//                                .setTitle("图片")//设置标题
//                                .showCamera(true)//设置是否显示拍照按钮
//                                .showImage(true)//设置是否展示图片
//                                .showVideo(true)//设置是否展示视频
//                                .showVideo(true)//设置是否展示视频
//                                .setSingleType(true)//设置图片视频不能同时选择
//                                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
//                                //.setImagePaths(mImagePaths)//保存上一次选择图片的状态，如果不需要可以忽略
//                                .setImageLoader(glideLoader)//设置自定义图片加载器
//                                .start(activity, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//                        }
//                        mView.iv_administrator_id_card_nation.setOnClickListener {
//                            selectImage=4
//                            ImagePicker.getInstance()
//                                .setTitle("图片")//设置标题
//                                .showCamera(true)//设置是否显示拍照按钮
//                                .showImage(true)//设置是否展示图片
//                                .showVideo(true)//设置是否展示视频
//                                .showVideo(true)//设置是否展示视频
//                                .setSingleType(true)//设置图片视频不能同时选择
//                                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
//                                //.setImagePaths(mImagePaths)//保存上一次选择图片的状态，如果不需要可以忽略
//                                .setImageLoader(glideLoader)//设置自定义图片加载器
//                                .start(activity, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//                        }
//                    }
//
//                    ToastHelper.mToast(context!!,result)
//                },{
//                    it.printStackTrace()
//                })
//        }
        mView.btn_re_enterprise_certification.setOnClickListener {
            certification()
        }
    }
    fun certification(){
        val result = Observable.create<RequestBody> {

            val json = JSONObject().put("mainType",mainType.replace(" ","/"))
                .put("organizationName",mView.tv_institution_name_data.text)
                .put("mainBusiness",mView.et_main_code.text)
                .put("socialCreditCode",mView.tv_social_credit_code_data.text)
                .put("businessLicense",businessLicenseMap.path)
                .put("officialPicturePath",officialPictureMap.path)
                .put("legalPersonPositivePath",legalIdCardPeopleMap.path)
                .put("legalPersonNegativePath",legalIdCardNationMap.path)
                .put("identifyCardPathFront",administratorIdCardPeopleMap.path)
                .put("identifyCardPathContrary",administratorIdCardNationMap.path)
                .put("vipName",mView.tv_administrator_name_data.text)
                .put("identifyCard",mView.tv_administrator_number_data.text)
                .put("certificationStatus",3)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = putSimpleMessage(it,UnSerializeDataBase.mineBasePath+ Constants.HttpUrlPath.My.reCertification)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        result = "提交成功"
                        activity!!.supportFragmentManager.popBackStackImmediate()
                    }
                    else
                        result = "提交失败"
                    ToastHelper.mToast(context!!,result)
                },{
                    it.printStackTrace()
                })
        }
    }
    fun refresh(imagePath:String){
        when(selectImage){
            1->{
                legalIdCardPeopleMap.path=imagePath
                glideImageLoader.displayImage(mView.iv_legal_id_card_people,imagePath)
            }
            2->{
                legalIdCardNationMap.path=imagePath
                glideImageLoader.displayImage(mView.iv_legal_id_card_nation,imagePath)
            }
            3->{
                administratorIdCardPeopleMap.path=imagePath
                glideImageLoader.displayImage(mView.iv_administrator_id_card_people,imagePath)
            }
            4->{
                administratorIdCardNationMap.path=imagePath
                glideImageLoader.displayImage(mView.iv_administrator_id_card_nation,imagePath)
            }
            5->{
                businessLicenseMap.path=imagePath
                glideImageLoader.displayImage(mView.iv_business_license,imagePath)
            }
            6->{
                officialPictureMap.path=imagePath
                glideImageLoader.displayImage(mView.iv_official_picture,imagePath)
            }
        }
    }
}