package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyCertificationActivity
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_personal_certification_show.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class PersonalCertificationShowFragment :Fragment(){
    val glideLoader = GlideLoader()
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_personal_certification_show,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.btn_personal_re_certification.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("mainType","个人")
            FragmentHelper.switchFragment(activity!!,ReCertificationFragment.newInstance(bundle),R.id.frame_my_certification,"MyCertification")
        }
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("mainType","个人")
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it, UnSerializeDataBase.mineBasePath+ Constants.HttpUrlPath.My.certificationMore)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        if(jsonObject.getString("desc")=="FAIL"){
                            result = jsonObject.getString("message")
                            mView.btn_personal_re_certification.visibility = View.GONE
                        }
                        else{
                            val js = jsonObject.getJSONObject("message")
                            result = "获取数据成功"
                            mView.tv_id_card_name_data.text = js.getString("vipName")
                            mView.tv_id_card_number_data.text = js.getString("identifyCard")
                            GlideLoader().loadImage(mView.iv_id_card_people_show,js.getString("identifyCardPathFront"))
                            GlideLoader().loadImage(mView.iv_id_card_nation_show,js.getString("identifyCardPathContrary"))
                        }
                    }

                    ToastHelper.mToast(mView.context,result)
                },{
                    ToastHelper.mToast(mView.context,"获取信息异常")
                    it.printStackTrace()
                })
        }
    }
}