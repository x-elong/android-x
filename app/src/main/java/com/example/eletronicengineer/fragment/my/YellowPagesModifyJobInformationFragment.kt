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
import com.example.eletronicengineer.DisplayYellowPages.YellowPagesDetail
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyReleaseActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.putSimpleMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_modify_job_information.view.*
import org.json.JSONObject

class YellowPagesModifyJobInformationFragment :Fragment(){
    companion object{
        fun newInstance(args: Bundle): YellowPagesModifyJobInformationFragment
        {
            val fragment= YellowPagesModifyJobInformationFragment()
            fragment.arguments=args
            return fragment
        }
    }
    var mAdapter: RecyclerviewAdapter?=null
    var companyType:String=""//名称
    var type = 0
    var validTime = "0"
    lateinit var mView: View
    lateinit var id:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i("onCreateView","running")
        mView= inflater.inflate(R.layout.fragment_modify_job_information,container,false)
        type = arguments!!.getInt("type")
        companyType = arguments!!.getString("companyType")
        id = arguments!!.getString("id")
        initFragment()
        return mView
    }
    fun initFragment(){
        val data=arguments
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context=mView.context
        adapterGenerate.activity=activity as MyReleaseActivity
        //加载选择的界面
        if(mAdapter==null){
            val result = Observable.create<RecyclerviewAdapter>{
                val adapter = switchAdapter(adapterGenerate,type,companyType)
                it.onNext(adapter)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mAdapter=it
                    mView.rv_job_information_content.adapter=mAdapter
                    mView.rv_job_information_content.layoutManager= LinearLayoutManager(context)
                }

        }
        else{
            mView.rv_job_information_content.adapter=mAdapter
            mView.rv_job_information_content.layoutManager= LinearLayoutManager(context)
        }
        //返回
        mView.tv_modify_job_information_back.setOnClickListener{
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        //发布
        mView.btn_modify_job_information.setOnClickListener{

            val networkAdapter= NetworkAdapter(mAdapter!!.mData, mView.context)
            val provider= NetworkAdapter.Provider(mAdapter!!.mData,mView.context)
            if(networkAdapter.check()){
                val json= JSONObject().put("id",id)
                provider.generateJsonRequestBody(json).subscribe {
                    val loadingDialog = LoadingDialog(mView.context, "正在请求...", R.mipmap.ic_dialog_loading)
                    loadingDialog.show()
                    val result = putSimpleMessage(it, UnSerializeDataBase.dmsBasePath+mAdapter!!.urlPath).subscribeOn(
                        Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                loadingDialog.dismiss()
                                val json = JSONObject(it.string())
                                if (json.getInt("code") == 200) {
                                    ToastHelper.mToast(mView.context,"修改成功")
                                    mView.tv_modify_job_information_back.callOnClick()
                                }else if(json.getInt("code") == 403){
                                    ToastHelper.mToast(mView.context,"${json.getString("desc")} 请升级为更高级会员")
                                } else if (json.getInt("code") == 400) {
                                    ToastHelper.mToast(mView.context,"修改失败")
                                }
                            },
                            {
                                loadingDialog.dismiss()
                                ToastHelper.mToast(mView.context,"网络异常")
                                it.printStackTrace()
                            }
                        )
                }
            }
        }
    }
    fun switchAdapter(adapterGenerate: AdapterGenerate, Type: Int, selectContent:String):RecyclerviewAdapter {
        lateinit var adapter:RecyclerviewAdapter
        when(Type){
            Constants.FragmentType.INDUSTRYYELLOWPAGES_TYPE.ordinal->{
                adapter=adapterGenerate.yellowPagesThreePublish(selectContent)
                adapter.mData[0].singleDisplayRightContent = selectContent
                adapter.urlPath = Constants.HttpUrlPath.YellowPages.updateIndustryYellowPages
                initYellowPages(adapter)
            }
        }
        return adapter
    }
    fun initYellowPages(adapter: RecyclerviewAdapter){
        val yellowPagesDetail = arguments!!.getSerializable("data") as YellowPagesDetail
        adapter.mData[1].inputSingleContent = yellowPagesDetail.companyName
        adapter.mData[2].inputSingleContent = yellowPagesDetail.companyAddress
        adapter.mData[3].inputSingleContent = yellowPagesDetail.businessScope
        var qualification = ""
        if(yellowPagesDetail.firstQualification!=null){
            qualification+=yellowPagesDetail.firstQualification
        }
        if(yellowPagesDetail.secondQualification!=null){
            if(qualification!="")
                qualification+="|"
            qualification+=yellowPagesDetail.secondQualification
        }
        if(yellowPagesDetail.thirdQualification!=null){
            if(qualification!="")
                qualification+="|"
            qualification+=yellowPagesDetail.thirdQualification
        }
        adapter.mData[4].selectContent = qualification
    }
}