package com.example.eletronicengineer.fragment.my

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.getIndustryYellowPagesDetail
import com.example.eletronicengineer.utils.getYellowPagesDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_job_more.view.*
import org.json.JSONObject

class YellowPagesJobMoreFragment :Fragment(){
    companion object{
        fun newInstance(args: Bundle):YellowPagesJobMoreFragment{
            val yellowPagesJobMoreFragment = YellowPagesJobMoreFragment()
            yellowPagesJobMoreFragment.arguments = args
            return yellowPagesJobMoreFragment
        }
    }
    var mdata=Bundle()
    var type:Int=0
    lateinit var id:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_job_more, container, false)
        id=arguments!!.getString("id")
        type = arguments!!.getInt("type")
        initFragment(view)
        return view
    }

    private fun initFragment(view: View) {
        view.btn_registration_more.visibility = View.GONE
        view.tv_job_more_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = view.context
        adapterGenerate.activity = activity as AppCompatActivity
        lateinit var adapter: RecyclerviewAdapter
        when (type) {
            Constants.FragmentType.INDUSTRYYELLOWPAGES_TYPE.ordinal -> {
                adapter = adapterGenerate.yellowPagesDisplay()
                val result = getYellowPagesDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(
                    Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        val data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.companyType==null) {
                            " " } else{ data.companyType }
                        val Qualification:List<String> = listOf(data.firstQualification,data.secondQualification,data.thirdQualification)
                        var temp=""
                        for(str in Qualification){
                            if(str!=null&&str!=""){
                                if(temp!="")
                                    temp+="|"
                                temp+=str
                            }
                        }
                        adapter.mData[1].singleDisplayRightContent= temp
                        adapter.mData[2].singleDisplayRightContent=if(data.companyName==null) {
                            " " } else{ data.companyName}
                        adapter.mData[3].singleDisplayRightContent=if(data.companyAddress==null) {
                            " " } else{ data.companyAddress}
                        adapter.mData[4].singleDisplayRightContent=if(data.businessScope==null) {
                            " " } else{ data.businessScope}
                        adapter.mData[5].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[6].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        view.btn_delete.setOnClickListener {
                            val loadingDialog = LoadingDialog(
                                view.context,
                                "正在删除中...",
                                R.mipmap.ic_dialog_loading
                            )
                            loadingDialog.show()
                            deleteIndustryYellowPages(id).observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe({
                                    loadingDialog.dismiss()
                                    val json = JSONObject(it.string())
                                    Log.i("json", json.toString())
                                    if (json.getString("desc") == "OK") {
                                        ToastHelper.mToast(view.context, "删除成功")
                                        activity!!.supportFragmentManager.popBackStackImmediate()
                                    } else
                                        ToastHelper.mToast(view.context, "删除失败")
                                }, {
                                    loadingDialog.dismiss()
                                    ToastHelper.mToast(view.context, "删除信息异常")
                                    it.printStackTrace()
                                })
                        }
                        view.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt("type", type)
                            bundle.putString("companyType",data.companyType)
                            bundle.putString("id", data.id)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                YellowPagesModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                        view.rv_job_more_content.adapter = adapter
                        view.rv_job_more_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }

        }

    }
}