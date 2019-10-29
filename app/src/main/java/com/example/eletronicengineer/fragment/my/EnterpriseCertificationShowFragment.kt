package com.example.eletronicengineer.fragment.my

import android.os.Bundle
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
import com.example.eletronicengineer.utils.GlideLoader
import com.example.eletronicengineer.utils.ToastHelper
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_enterprise_certification_show.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class EnterpriseCertificationShowFragment :Fragment(){
    companion object{
        fun newInstace(args:Bundle):EnterpriseCertificationShowFragment{
            val enterpriseCertificationShowFragment = EnterpriseCertificationShowFragment()
            enterpriseCertificationShowFragment.arguments = args
            return enterpriseCertificationShowFragment
        }
    }
    lateinit var mView: View
    lateinit var mAdapter: ImageAdapter
    lateinit var mainType:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_enterprise_certification_show,container,false)
        mainType = arguments!!.getString("mainType").replace(" ","/")
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.btn_enterprise_re_certification.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("mainType",mainType)
            (activity as MyCertificationActivity).switchFragment(ReCertificationFragment.newInstance(bundle),R.id.frame_my_certification,"MyCertification")
        }
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("mainType",mainType)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,"http://192.168.1.132:8032"+ Constants.HttpUrlPath.My.certificationMore)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        val js = jsonObject.getJSONObject("message")
                        result = "获取数据成功"
                        mView.tv_institution_name_data.text = js.getString("organizationName")
                        mView.tv_social_credit_code_data.text = js.getString("socialCreditCode")
                        mView.tv_main_code_data.text = js.getString("mainBusiness")
                        GlideLoader().loadImage(mView.iv_legal_id_card_people_show,js.getString("legalPersonPositivePath"))
                        GlideLoader().loadImage(mView.iv_legal_id_card_nation_show,js.getString("legalPersonNegativePath"))
                        val blImagePath = js.getString("businessLicense").split("|")
                        val blImageList = ArrayList<Image>()
                        for (j in blImagePath){
                            blImageList.add(Image(j,null))
                            blImageList[blImageList.size-1].isX = false
                        }
                        blImageList[blImageList.size-1].imageListener = View.OnClickListener {  }
                        val opImageList = ArrayList<Image>()
                        val opImagePath = js.getString("officialPicturePath").split("|")
                        for (j in opImagePath){
                            opImageList.add(Image(j,null))
                            opImageList[opImageList.size-1].isX = false
                        }

                        opImageList[opImageList.size-1].imageListener = View.OnClickListener {  }
                        mView.rv_business_license_content_show.adapter = ImageAdapter(blImageList)
                        mView.rv_business_license_content_show.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
                        mView.rv_official_picture_content_show.adapter = ImageAdapter(opImageList)
                        mView.rv_official_picture_content_show.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
                        mView.tv_administrator_name_data.text = js.getString("vipName")
                        mView.tv_administrator_number_data.text = js.getString("identifyCard")
                        GlideLoader().loadImage(mView.iv_administrator_id_card_people_show,js.getString("identifyCardPathFront"))
                        GlideLoader().loadImage(mView.iv_administrator_id_card_nation_show,js.getString("identifyCardPathContrary"))
                    }
                    else
                        result = "获取数据失败"
                    ToastHelper.mToast(context!!,result)
                },{
                    it.printStackTrace()
                })
        }
    }
}