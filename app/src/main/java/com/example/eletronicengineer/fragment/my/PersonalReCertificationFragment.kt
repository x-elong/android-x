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
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.lcw.library.imagepicker.ImagePicker
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_personal_certification.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import rx.Observer
import java.io.File

class PersonalReCertificationFragment : Fragment() {
    companion object {
        fun newInstance(args: Bundle): PersonalReCertificationFragment {
            val personalReCertificationFragment = PersonalReCertificationFragment()
            personalReCertificationFragment.arguments = args
            return personalReCertificationFragment
        }
    }

    val glideLoader = GlideLoader()
    var selectImage = -1
    lateinit var mView: View
    var idCardPeopleMap = BitmapMap("", "identifyCardPathFront")
    var idCardNationMap = BitmapMap("", "identifyCardPathContrary")
    lateinit var mainType: String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_personal_certification, container, false)
        mainType = arguments!!.getString("mainType")
        initFragment()
        return mView
    }

    private fun initFragment() {
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("mainType", "个人")
            val requestBody = RequestBody.create(MediaType.parse("application/json"), json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.certificationMore)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if (code == 200) {
                        val js = jsonObject.getJSONObject("message")
                        val identifyCardPathFront = js.getString("identifyCardPathFront")
                        val identifyCardPathContrary = js.getString("identifyCardPathContrary")
                        result = "获取数据成功"
                        mView.et_id_card_name.setText(js.getString("vipName"))
                        mView.et_id_card_number.setText(js.getString("identifyCard"))
                        GlideLoader().loadImage(mView.iv_id_card_people, identifyCardPathFront)
                        GlideLoader().loadImage(mView.iv_id_card_nation, identifyCardPathContrary)
                        idCardPeopleMap.path = identifyCardPathFront
                        idCardNationMap.path = identifyCardPathContrary
                    } else
                        result = "获取数据失败"
                    ToastHelper.mToast(context!!, result)
                }, {
                    it.printStackTrace()
                })
        }

        mView.iv_id_card_people.setOnClickListener {
            selectImage = 1
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
            selectImage = 2
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
            if (mView.et_id_card_name.text.isBlank()) {
                val toast = Toast.makeText(context, "身份证姓名不能为空", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            } else if (mView.et_id_card_number.text.isBlank() || mView.et_id_card_number.text.length != 18) {
                val toast = Toast.makeText(context, "请输入正确的18位身份证号码", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            } else if (idCardPeopleMap.path == "" || idCardNationMap.path == "") {
                val toast = Toast.makeText(context, "身份证正反照不能为空", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            } else {
                uploadImg()
            }
        }
    }

    fun refresh(imagePath: String) {
        if (selectImage == 1) {
            idCardPeopleMap.path = imagePath
            glideLoader.loadImage(mView.iv_id_card_people, imagePath)
        } else if (selectImage == 2) {
            idCardNationMap.path = imagePath
            glideLoader.loadImage(mView.iv_id_card_nation, imagePath)
        }
    }

    fun uploadImg() {
        var result = ""
        val fileList = arrayListOf(File(idCardPeopleMap.path), File(idCardNationMap.path))
        val results = try {
            for (j in fileList) {
                if(j.path.contains("https://i.bmp.ovh/imgs")){
                    result+="|"
                    continue
                }
                Log.i("File path is : ", j.path)
                val imagePart = MultipartBody.Part.createFormData(
                    "file", j.name,
                    RequestBody.create(MediaType.parse("image/*"), j)
                )
                uploadImage(imagePart).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        //Log.i("responseBody",it.string())
                        if (result != "")
                            result += "|"
                        if (j.path == idCardPeopleMap.path) {
                            idCardPeopleMap.path = it.string()
                            result += idCardPeopleMap.path
                        } else {
                            idCardNationMap.path = it.string()
                            result += idCardPeopleMap.path
                        }
                        Log.i("result url", result)
                        if (result.split("|").size == 2)
                            certification(result)
                    },
                    {
                        it.printStackTrace()
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun certification(result: String) {
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("mainType", "个人")
                .put("vipName", mView.et_id_card_name.text)
                .put("identifyCard", mView.et_id_card_number.text)
                .put("identifyCardPathFront", idCardPeopleMap.path)
                .put("identifyCardPathContrary", idCardNationMap.path)
            val requestBody = RequestBody.create(MediaType.parse("application/json"), json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result =
                putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.enterpriseReCertification)
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                        val jsonObject = JSONObject(it.string())
                        val code = jsonObject.getInt("code")
                        var result = ""
                        if (code == 200) {
                            result = "提交成功"
                            activity!!.supportFragmentManager.popBackStackImmediate()
                        } else
                            result = "提交失败"
                        ToastHelper.mToast(context!!, result)
                    }, {
                        it.printStackTrace()
                    })
        }
    }
}