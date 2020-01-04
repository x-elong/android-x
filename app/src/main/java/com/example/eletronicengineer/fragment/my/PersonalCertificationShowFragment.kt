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
            if(isCertification){
                ToastHelper.mToast(mView.context,"已经认证过，无法再次认证。\n若想修改认证，请重新认证！")
            }else{
                FragmentHelper.switchFragment(activity!!,PersonalCertificationFragment(),R.id.frame_my_certification,"Certification")
            }
        }

        mView.btn_personal_re_certification.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,PersonalReCertificationFragment(),R.id.frame_my_certification,"MyCertification")
        }
            val result = NetworkAdapter().getDataUser()
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val user = it.message.user
                    if(user.isCredential){
                        isCertification = true
                        mView.btn_personal_re_certification.visibility = View.VISIBLE
                        mView.tv_id_card_name_data.text = user.name
                        mView.tv_id_card_number_data.text = user.identifyCard
                        mView.tv_id_card_address_data.text = user.vipAddress
                        GlideImageLoader().displayImage(mView.iv_id_card_people_show,user.identifyCardPathFront!!)
                        GlideImageLoader().displayImage(mView.iv_id_card_nation_show,user.identifyCardPathContrary!!)
                    }else{
//                        ToastHelper.mToast(mView.context,"")
                    }
                },{
                    ToastHelper.mToast(mView.context,"网络异常")
                    it.printStackTrace()
                })
        }
}