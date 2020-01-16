package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyCertificationActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_personal_certification_show.view.*
import kotlinx.android.synthetic.main.personal_certification_show.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class PersonalCertificationShowFragment :Fragment(){
    var isCertification = false
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_personal_certification_show,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_personal_certification_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.tv_personal_certification_add.setOnClickListener {
            if (isCertification) {
                ToastHelper.mToast(mView.context, "已经认证过，无法再次认证。\n若想修改认证，请重新认证！")
            } else {
                FragmentHelper.switchFragment(
                    activity!!,
                    PersonalCertificationFragment(),
                    R.id.frame_my_certification,
                    "Certification"
                )
            }
        }

        mView.btn_personal_re_certification.setOnClickListener {
            FragmentHelper.switchFragment(
                activity!!,
                PersonalReCertificationFragment(),
                R.id.frame_my_certification,
                "MyCertification"
            )
        }
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("mainType", "个人")
            val requestBody =
                RequestBody.create(MediaType.parse("application/json"), json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(
                it,
                UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.certificationMore
            )
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if (code == 200) {
                        if (jsonObject.getString("desc") == "FAIL") {
//                            result = jsonObject.getString("message")
                            mView.sv.visibility = View.GONE
                            mView.tv_null.visibility = View.VISIBLE
                        } else {
                            mView.sv.visibility = View.VISIBLE
                            mView.tv_null.visibility = View.GONE
                            val js = jsonObject.getJSONObject("message")
//                            result = "获取数据成功"
                            isCertification = true
                            val status = js.getInt("certificationStatus")
                            mView.tv_certification_status.text = "认证状态:" + when(status){
                                0,3-> "审核中"
                                1->"成功"
                                2->"失败\n驳回理由:(${js.getString("reason")})"
                                else->"未认证"
                            }
                            if(status==1 || status==2)
                                mView.btn_personal_re_certification.visibility = View.VISIBLE
                            mView.tv_id_card_name_data.text = js.getString("vipName")
                            mView.tv_id_card_number_data.text = js.getString("identifyCard")
                            mView.tv_id_card_address_data.setText(js.getString("vipAddress"))
                            GlideImageLoader().displayImage(
                                mView.iv_id_card_people_show,
                                js.getString("identifyCardPathFront")
                            )
                            GlideImageLoader().displayImage(
                                mView.iv_id_card_nation_show,
                                js.getString("identifyCardPathContrary")
                            )
                        }
                    } else if (code == 500) {
                        result = "服务器异常"
                    }
                    if(result!="")
                        ToastHelper.mToast(mView.context, result)
                }, {
                    ToastHelper.mToast(mView.context, "网络异常")
                    it.printStackTrace()
                })
        }
        if(!UnSerializeDataBase.isCertificate){
            mView.tv_personal_certification_add.callOnClick()
            UnSerializeDataBase.isCertificate = true
        }
    }
}