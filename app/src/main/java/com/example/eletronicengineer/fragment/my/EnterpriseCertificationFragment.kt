package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.os.Handler
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
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.aninterface.Image
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.fragment.sdf.ImageFragment
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
import kotlinx.android.synthetic.main.fragment_enterprise_certification.*
import kotlinx.android.synthetic.main.fragment_enterprise_certification.view.*
import kotlinx.android.synthetic.main.fragment_information_certification.*
import kotlinx.android.synthetic.main.fragment_information_certification.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File

class EnterpriseCertificationFragment :Fragment(){
    var selectImage=-1
    lateinit var mView:View
    var mainType = ""
    val mHandler = Handler(Handler.Callback {
        when(it.what)
        {
            RecyclerviewAdapter.MESSAGE_SELECT_OK ->
            {
                val selectContent=it.data.getString("selectContent")
                mView.tv_subject_category_content.text=selectContent
                mainType = selectContent!!
//                mView.sp_demand_moder.
                false
            }
            else->
            {
                false
            }
        }
    })
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
            NetworkAdapter(mView.context).upImage(photoList[0],this@EnterpriseCertificationFragment)
//            photoAdapter.notifyDataSetChanged()
        }
    }
    var isFirst = true
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
        mView = inflater.inflate(R.layout.fragment_information_certification,container,false)
        Log.i("||| size is :","|||".split("|").size.toString())
        Log.i("|||| size is :","||||".split("|").size.toString())
        initFragment()
        return mView
    }
    private fun initFragment() {
        mView.tv_enterprise_certification_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }

        mView.view_subject_category_sp.setOnClickListener{
            val Option1Items = listOf("供应商","建设单位","设计单位","施工单位","监理单位","咨询单位","职能部门","其他")
            val Option2Items:List<List<String>> = listOf(listOf(""),listOf("材料供应","机械租赁","技术服务","金融服务"), listOf("政府单位","事业单位","企业"),listOf(""),
                listOf("施工企业","施工队","项目部"),
                listOf(""),listOf("安监部","质检部"),listOf(""))
            val selectDialog= CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,context!!,mHandler,Option1Items,Option2Items).multiDialog
            selectDialog.show()
        }

        mView.iv_business_license.viewTreeObserver.addOnGlobalLayoutListener{
            ImageLoadUtil.loadBackgound(mView.iv_business_license,mView.context,R.drawable.business_license)
        }
        mView.iv_official_picture.viewTreeObserver.addOnGlobalLayoutListener{
            ImageLoadUtil.loadBackgound(mView.iv_official_picture,mView.context,R.drawable.official_picture)
        }
        mView.iv_legal_id_card_people.viewTreeObserver.addOnGlobalLayoutListener{
            ImageLoadUtil.loadBackgound(mView.iv_legal_id_card_people,mView.context,R.drawable.id_card_people)
        }
        mView.iv_legal_id_card_nation.viewTreeObserver.addOnGlobalLayoutListener{
            ImageLoadUtil.loadBackgound(mView.iv_legal_id_card_nation,mView.context,R.drawable.id_card_nation)
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

        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("mainType","个人")
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,UnSerializeDataBase.mineBasePath+ Constants.HttpUrlPath.My.certificationMore)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        val js = jsonObject.getJSONObject("message")
                        result = "管理员已认证"
                        mView.tv_administrator_name_data.setText(js.getString("vipName"))
                        mView.tv_administrator_number_data.setText(js.getString("identifyCard"))
                        GlideImageLoader().displayImage(mView.iv_administrator_id_card_people,js.getString("identifyCardPathFront"))
                        GlideImageLoader().displayImage(mView.iv_administrator_id_card_nation,js.getString("identifyCardPathContrary"))
                        administratorIdCardPeopleMap.path = js.getString("identifyCardPathFront")
                        administratorIdCardNationMap.path = js.getString("identifyCardPathContrary")
                        isCertification = true
                    }
                    else if(code==500){
                        ImageLoadUtil.loadBackgound(mView.iv_administrator_id_card_people,mView.context,R.drawable.id_card_people)
                        ImageLoadUtil.loadBackgound(mView.iv_administrator_id_card_nation,mView.context,R.drawable.id_card_nation)
                        result = "服务器异常"
                    }
                    else{
                        result = "获取个人认证信息失败"
                    }
                    if(result != "管理员已认证")
                    ToastHelper.mToast(context!!,result)
                },{
                    ImageLoadUtil.loadBackgound(mView.iv_administrator_id_card_people,mView.context,R.drawable.id_card_people)
                    ImageLoadUtil.loadBackgound(mView.iv_administrator_id_card_nation,mView.context,R.drawable.id_card_nation)
                    ToastHelper.mToast(context!!,"获取认证信息异常")
                    it.printStackTrace()
                })
        }
        mView.btn_Enterprise_certification.setOnClickListener {
            if(mainType==""){
                ToastHelper.mToast(mView.context,"请选择主体类型")
            }else if(mView.et_institution_name.text.isBlank()){
                ToastHelper.mToast(mView.context,"机构名称不能为空!")
            }
            else if(mView.et_social_credit_code.text.length!=18){
                ToastHelper.mToast(mView.context,"社会信用代码必须为18位!")
            }
            else if(mView.et_main_code.text.isBlank()){
                ToastHelper.mToast(mView.context,"主营信息不能为空!")
            }
            else if(businessLicenseMap.path==""){
                ToastHelper.mToast(mView.context,"营业执照不能为空!")
            }
            else if(officialPictureMap.path==""){
                ToastHelper.mToast(mView.context,"公函图片不能为空!")
            }
            else if(legalIdCardPeopleMap.path=="" || legalIdCardNationMap.path==""){
                ToastHelper.mToast(mView.context,"法人身份证正反照不能为空!")
            }else
                certification()
        }
//        mView.btn_information_certification.setOnClickListener {
//            initImagePath()
//            uploadImg()
//        }
    }

    fun certification(){
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("mainType",mainType.trim().replace(" ","/"))
                .put("organizationName",mView.et_institution_name.text)
                .put("mainBusiness",mView.et_main_code.text)
                .put("socialCreditCode",mView.et_social_credit_code.text)
                .put("businessLicense",businessLicenseMap.path)
                .put("officialPicturePath",officialPictureMap.path)
                .put("legalPersonPositivePath",legalIdCardPeopleMap.path)
                .put("legalPersonNegativePath",legalIdCardNationMap.path)
                .put("identifyCardPathFront",administratorIdCardPeopleMap.path)
                .put("identifyCardPathContrary",administratorIdCardNationMap.path)
                .put("vipName",mView.tv_administrator_name_data.text)
                .put("identifyCard",mView.tv_administrator_number_data.text)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,UnSerializeDataBase.mineBasePath+ Constants.HttpUrlPath.My.enterpriseCertification)
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
        Log.i("imagePath",imagePath)
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