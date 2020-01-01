package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyCertificationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter.Companion.MESSAGE_SELECT_OK
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_enterprise_certification_show.view.*
import kotlinx.android.synthetic.main.fragment_my_certification.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class EnterpriseCertificationShowFragment :Fragment(){
    var mainType = ""
    var organizationName = ""
    val mHandler = Handler(Handler.Callback {
        when(it.what)
        {
            MESSAGE_SELECT_OK->
            {
                val selectContent=it.data.getString("selectContent")
                mView.tv_company_content.text=selectContent
                organizationName = selectContent!!
                getData()
//                mView.sp_demand_moder.
                false
            }
            else->
            {
                false
            }
        }
    })
    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_my_certification,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        initData()
        mView.tv_enterprise_certification_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }

//        mView.view_subject_category_sp.setOnClickListener{
//            val Option1Items = listOf("供应商","建设单位","设计单位","施工单位","监理单位","咨询单位","职能部门","其他")
//            val Option2Items:List<List<String>> = listOf(listOf(""),listOf("材料供应","机械租赁","技术服务","金融服务"), listOf("政府单位","事业单位","企业"),listOf(""),
//                listOf("施工企业","施工队","项目部"),
//                listOf(""),listOf("安监部","质检部"),listOf(""))
//            val selectDialog= CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,context!!,mHandler,Option1Items,Option2Items).multiDialog
//            selectDialog.show()
//        }

        mView.tv_enterprise_certification_add.setOnClickListener {
            val loadingDialog = LoadingDialog(mView.context,"正在查询个人认证情况...")
            loadingDialog.show()
            val result = Observable.create<RequestBody> {
                val json = JSONObject().put("mainType","个人").put("certificationStatus","1")
                val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
                it.onNext(requestBody)
            }.subscribe {
                loadingDialog.dismiss()
                val result = startSendMessage(it,UnSerializeDataBase.mineBasePath+ Constants.HttpUrlPath.My.certificationMore)
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                        val jsonObject = JSONObject(it.string())
                        val code = jsonObject.getInt("code")
                        val desc = jsonObject.getString("desc")
                        if(code==200 && desc=="OK"){
                            FragmentHelper.switchFragment(activity!!,EnterpriseCertificationFragment(),R.id.frame_my_certification,"Certification")
                        }
                        else if(code==500){
                            ToastHelper.mToast(context!!,"服务器异常")
                        }
                        else{
                            ToastHelper.mToast(context!!,"未进行过个人认证,请先进行个人认证!")
                            FragmentHelper.switchFragment(activity!!,PersonalCertificationFragment(),R.id.frame_my_certification,"Certification")
                        }
                    },{
                        loadingDialog.dismiss()
                        ToastHelper.mToast(context!!,"获取认证信息异常")
                        it.printStackTrace()
                    })
            }
        }

        mView.btn_enterprise_re_certification.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("organizationName",organizationName)
            FragmentHelper.switchFragment(activity!!,EnterpriseReCertificationFragment.newInstance(bundle),R.id.frame_my_certification,"MyCertification")
        }
    }

    private fun initData() {
        val result = getALLOrganizationCertificationDTOList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                val json = JSONObject(it.string())
                if(json.getInt("code")==200){
                    val jsonArray = json.getJSONArray("message")
                    val organizationNameList = ArrayList<String>()
                    for (j in 0 until jsonArray.length()){
                        val js = jsonArray.getJSONObject(j)
                        organizationNameList.add(js.getString("organizationName"))
                    }
                    mView.view_sp_company.setOnClickListener {
                        val selectDialog= CustomDialog(CustomDialog.Options.SELECT_DIALOG,context!!,organizationNameList,mHandler).dialog
                        selectDialog.show()
                    }
                }
            },{
                ToastHelper.mToast(mView.context,"网络异常")
                it.printStackTrace()
            })
    }

    private fun getData(){
        val result = Observable.create<RequestBody> {
            val json = JSONObject()
//                .put("mainType",mainType)
//                .put("certificationStatus","1")
                .put("organizationName",organizationName)
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
                            ImageLoadUtil.loadBackgound(mView.iv_business_license_show,mView.context,R.drawable.business_license)
                            ImageLoadUtil.loadBackgound(mView.iv_official_picture_show,mView.context,R.drawable.official_picture)
                            ImageLoadUtil.loadBackgound(mView.iv_legal_id_card_people_show,mView.context,R.drawable.id_card_people)
                            ImageLoadUtil.loadBackgound(mView.iv_legal_id_card_nation_show,mView.context,R.drawable.id_card_nation)
                            ImageLoadUtil.loadBackgound(mView.iv_administrator_id_card_people_show,mView.context,R.drawable.id_card_people)
                            ImageLoadUtil.loadBackgound(mView.iv_administrator_id_card_nation_show,mView.context,R.drawable.id_card_nation)

                        }
                        else{
                            val js = jsonObject.getJSONObject("message")
                            result = "获取数据成功"
                            val status = js.getInt("certificationStatus")
                            mView.tv_certification_status.text = "认证状态:" + when(status){
                                0-> "未处理"
                                1->"通过"
                                2->"驳回\n驳回理由:(${js.getString("reason")})"
                                3->"重新认证"
                                else->"未认证"
                            }
                            if(status==1 || status==2)
                                mView.btn_enterprise_re_certification.visibility = View.VISIBLE
                            mView.tv_institution_name_data.text = js.getString("organizationName")
                            mView.tv_social_credit_code_data.text = js.getString("socialCreditCode")
                            mView.tv_main_code_data.text = js.getString("mainBusiness")
                            GlideImageLoader().displayImage(mView.iv_business_license_show,js.getString("businessLicense"))
                            GlideImageLoader().displayImage(mView.iv_official_picture_show,js.getString("officialPicturePath"))
                            GlideImageLoader().displayImage(mView.iv_legal_id_card_people_show,js.getString("legalPersonPositivePath"))
                            GlideImageLoader().displayImage(mView.iv_legal_id_card_nation_show,js.getString("legalPersonNegativePath"))
                            mView.tv_administrator_name_data.text = js.getString("vipName")
                            mView.tv_administrator_number_data.text = js.getString("identifyCard")
                            GlideImageLoader().displayImage(mView.iv_administrator_id_card_people_show,js.getString("identifyCardPathFront"))
                            GlideImageLoader().displayImage(mView.iv_administrator_id_card_nation_show,js.getString("identifyCardPathContrary"))
                        }
                    }
                    ToastHelper.mToast(mView.context,result)
                },{
                    ImageLoadUtil.loadBackgound(mView.iv_business_license_show,mView.context,R.drawable.business_license)
                    ImageLoadUtil.loadBackgound(mView.iv_official_picture_show,mView.context,R.drawable.official_picture)
                    ImageLoadUtil.loadBackgound(mView.iv_legal_id_card_people_show,mView.context,R.drawable.id_card_people)
                    ImageLoadUtil.loadBackgound(mView.iv_legal_id_card_nation_show,mView.context,R.drawable.id_card_nation)
                    ImageLoadUtil.loadBackgound(mView.iv_administrator_id_card_people_show,mView.context,R.drawable.id_card_people)
                    ImageLoadUtil.loadBackgound(mView.iv_administrator_id_card_nation_show,mView.context,R.drawable.id_card_nation)
                    ToastHelper.mToast(mView.context,"获取信息异常")
                    it.printStackTrace()
                })
        }
    }
}