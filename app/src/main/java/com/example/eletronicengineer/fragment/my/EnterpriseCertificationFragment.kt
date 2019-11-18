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
import com.example.eletronicengineer.aninterface.Image
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.startSendMessage
import com.lcw.library.imagepicker.ImagePicker
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
    companion object{
        fun newInstance(args:Bundle):EnterpriseCertificationFragment{
            val enterpriseCertificationFragment = EnterpriseCertificationFragment()
            enterpriseCertificationFragment.arguments = args
            return enterpriseCertificationFragment
        }
    }
    val glideLoader = GlideLoader()
    var selectImage=-1
    lateinit var blAdapter:ImageAdapter
    lateinit var opAdapter:ImageAdapter
    lateinit var mView:View
    lateinit var mainType:String
    var legalIdCardPeopleMap = BitmapMap("","legalPersonPositivePath")
    var legalIdCardNationMap = BitmapMap("","legalPersonNegativePath")
    var administratorIdCardPeopleMap = BitmapMap("","identifyCardPathFront")
    var administratorIdCardNationMap = BitmapMap("","identifyCardPathContrary")
    val imagePaths = ArrayList<String>()
    var isCertification = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_enterprise_certification,container,false)
        mainType = arguments!!.getString("mainType")
        Log.i("||| size is :","|||".split("|").size.toString())
        Log.i("|||| size is :","||||".split("|").size.toString())
        initAdapter()
        initFragment()
        return mView
    }
    private fun initAdapter() {
        val imageList:MutableList<Image> = ArrayList()
        imageList.add(Image("",View.OnClickListener {
            selectImage = 5
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
        }))
        imageList[imageList.size-1].isX =false
        blAdapter = ImageAdapter(imageList)
        mView.rv_business_license_content.adapter = blAdapter
        mView.rv_business_license_content.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        val mImageList:MutableList<Image> = ArrayList()
        mImageList.add(Image("",View.OnClickListener {
            selectImage = 6
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
        }))
        mImageList[0].isX =false
        opAdapter = ImageAdapter(mImageList)
        mView.rv_official_picture_content.adapter = opAdapter
        mView.rv_official_picture_content.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
    }
    private fun initFragment() {
        mView.iv_legal_id_card_people.setOnClickListener{
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
        mView.iv_legal_id_card_nation.setOnClickListener {
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
                        mView.et_administrator_name.setText(js.getString("vipName"))
                        mView.et_administrator_number.setText(js.getString("identifyCard"))
                        GlideLoader().loadImage(mView.iv_administrator_id_card_people,js.getString("identifyCardPathFront"))
                        GlideLoader().loadImage(mView.iv_administrator_id_card_nation,js.getString("identifyCardPathContrary"))
                        mView.et_administrator_name.isEnabled=false
                        mView.et_administrator_number.isEnabled=false
                        administratorIdCardPeopleMap.path = js.getString("identifyCardPathFront")
                        administratorIdCardNationMap.path = js.getString("identifyCardPathContrary")
                        isCertification = true
                    }
                    else{
                        result = "管理员未认证"
                        mView.iv_administrator_id_card_people.setOnClickListener{
                            selectImage=3
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
                        mView.iv_administrator_id_card_nation.setOnClickListener {
                            selectImage=4
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
                    }

                    ToastHelper.mToast(context!!,result)
                },{
                    it.printStackTrace()
                })
        }
        activity!!.btn_information_certification.setOnClickListener {
            mView.btn_information_certification.callOnClick()
        }
        mView.btn_information_certification.setOnClickListener {
            initImagePath()
            uploadImg()
        }
    }

    private fun initImagePath() {
        imagePaths.clear()
        imagePaths.add(legalIdCardPeopleMap.path)
        imagePaths.add(legalIdCardNationMap.path)
        for(j in blAdapter.mImageList)
            imagePaths.add(j.imagePath)
        imagePaths.removeAt(imagePaths.size-1)
        for(j in opAdapter.mImageList)
            imagePaths.add(j.imagePath)
        imagePaths.removeAt(imagePaths.size-1)
        if(!isCertification){
            imagePaths.add(administratorIdCardPeopleMap.path)
            imagePaths.add(administratorIdCardNationMap.path)
        }
    }

    fun uploadImg(){
        var result = ""
        val results = try {
            for (j in 0 until imagePaths.size){
                val file = File(imagePaths[j])
                Log.i("File path is : ",file.path)
                val imagePart = MultipartBody.Part.createFormData("file",file.name,
                    RequestBody.create(MediaType.parse("image/*"),file))
                uploadImage(imagePart).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        imagePaths[j] = it.string()
                        result += "|"
                        Log.i("result url", result)
                        if(result.split("|").size-1==imagePaths.size){
                            certification()
                        }else{
//                            ToastHelper.mToast(context!!,"提交失败")
                        }
                    },
                    {
                        it.printStackTrace()
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun certification(){
        val result = Observable.create<RequestBody> {
            var businessLicenseImagePaths = ""
            for(j in 0 until blAdapter.mImageList.size-1){
                if(businessLicenseImagePaths!="")
                    businessLicenseImagePaths += "|"
                businessLicenseImagePaths += imagePaths[2+j]
            }
            var officialPicturePath = ""

            for(j in 0 until opAdapter.mImageList.size-1){
                if(officialPicturePath!="")
                    officialPicturePath += "|"
                officialPicturePath += imagePaths[2+j+blAdapter.mImageList.size-1]
            }
            var identifyCardPathFront = administratorIdCardPeopleMap.path
            var identifyCardPathContrary = administratorIdCardNationMap.path
            if(!isCertification){
                identifyCardPathFront = imagePaths[imagePaths.size-2]
                identifyCardPathContrary = imagePaths[imagePaths.size-1]
            }

            val json = JSONObject().put("mainType",mainType.replace(" ","/"))
                .put("organizationName",mView.et_institution_name.text)
                .put("mainBusiness",mView.et_main_code.text)
                .put("socialCreditCode",mView.et_social_credit_code.text)
                .put("businessLicense",businessLicenseImagePaths)
                .put("officialPicturePath",officialPicturePath)
                .put("legalPersonPositivePath",imagePaths[0])
                .put("legalPersonNegativePath",imagePaths[1])
                .put("identifyCardPathFront",identifyCardPathFront)
                .put("identifyCardPathContrary",identifyCardPathContrary)
                .put("vipName",mView.et_administrator_name.text)
                .put("identifyCard",mView.et_administrator_number.text)
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
        when(selectImage){
            1->{
                legalIdCardPeopleMap.path=imagePath
                glideLoader.loadImage(mView.iv_legal_id_card_people,imagePath)
            }
            2->{
                legalIdCardNationMap.path=imagePath
                glideLoader.loadImage(mView.iv_legal_id_card_nation,imagePath)
            }
            3->{
                administratorIdCardPeopleMap.path=imagePath
                glideLoader.loadImage(mView.iv_administrator_id_card_people,imagePath)
            }
            4->{
                administratorIdCardNationMap.path=imagePath
                glideLoader.loadImage(mView.iv_administrator_id_card_nation,imagePath)
            }
            5->{
                val imageList = blAdapter.mImageList.toMutableList()
                imageList.add(imageList.size-1,Image(imagePath,null))
                blAdapter.mImageList = imageList
                blAdapter.notifyDataSetChanged()
            }
            6->{
                val imageList = opAdapter.mImageList.toMutableList()
                imageList.add(imageList.size-1,Image(imagePath,null))
                opAdapter.mImageList = imageList
                opAdapter.notifyDataSetChanged()
            }
        }
    }
}