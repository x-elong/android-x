package com.example.eletronicengineer.fragment.my

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ImageDisplayActivity
import com.example.eletronicengineer.activity.MyReleaseActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.getPersonalIssueMore
import com.example.eletronicengineer.utils.getRequirementPersonMore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_job_more.view.*
import org.json.JSONObject

class JobMoreFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):JobMoreFragment{
            val jobMoreFragment = JobMoreFragment()
            jobMoreFragment.arguments = args
            return jobMoreFragment
        }
    }
    var adapter = RecyclerviewAdapter(ArrayList())
    lateinit var mView: View
    lateinit var type:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_job_more,container,false)
        type = arguments!!.getString("type")
        initFragment()
        return mView
    }

    private fun initFragment() {
        when(type){
            "需求个人"->getDataDemandIndividual()
            "个人劳务"->getDataPersonalServices()
        }
        mView.tv_job_more_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.btn_edit_job_information.setOnClickListener {
            val bundle = Bundle()
            (activity as MyReleaseActivity).switchFragment(ModifyJobInformationFragment.newInstance(bundle))
        }
    }

    private fun getDataDemandIndividual() {
        val result = getRequirementPersonMore(arguments!!.getString("id"))
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                val jsonObject = JSONObject(it.string())
                val code = jsonObject.getInt("code")
                var result = ""
                if(code==200){
                    val roomBoardStandard = arrayListOf("队部自理","全包")
                    val sexs = arrayListOf("女","男","男女不限")
                    result="当前数据获取成功"
                    val json = jsonObject.getJSONObject("message")
                    val sex = 1 - json.getString("sexDemand").toInt()
                    val adapterGenerate = AdapterGenerate()
                    adapterGenerate.context = mView.context
                    adapter = adapterGenerate.demandIndividualDisplay()
                    val data = adapter.mData.toMutableList()
                    data.removeAt(data.size-1)
                    data[0].singleDisplayRightContent = json.getString("requirementVariety")
                    data[1].singleDisplayRightContent = json.getString("requirementMajor")
                    data[2].singleDisplayRightContent = json.getString("projectName")
                    data[3].singleDisplayRightContent = json.getString("projectSite")
                    data[4].singleDisplayRightContent = json.getString("planTime")
                    data[5].singleDisplayRightContent = json.getString("workerExperience")
                    data[6].singleDisplayRightContent = "${json.getString("minAgeDemand")}-${json.getString("maxAgeDemand")}"
                    data[7].singleDisplayRightContent = sexs[sex]
                    data[8].singleDisplayRightContent = roomBoardStandard[json.getString("roomBoardStandard").toInt()]
                    data[9].singleDisplayRightContent = json.getString("journeySalary")
                    data[10].singleDisplayRightContent = json.getString("journeyCarFare")
                    data[11].singleDisplayRightContent = json.getString("needPeopleNumber")
                    data[12].singleDisplayRightContent = json.getString("salaryStandard")+json.getString("salaryUnit")
                    data[14].singleDisplayRightContent = json.getString("name")
                    data[15].singleDisplayRightContent = json.getString("phone")
                    data[16].singleDisplayRightContent = json.getString("validTime")
                    if(!json.isNull("additonalExplain"))
                        data[17].singleDisplayRightContent = json.getString("additonalExplain")
                    adapter.mData = data
                    mView.rv_job_more_content.adapter = adapter
                    mView.rv_job_more_content.layoutManager = LinearLayoutManager(context)
                }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
                    result="当前数据为空"
                }
                Toast.makeText(context,result, Toast.LENGTH_SHORT).show()
            },{
                it.printStackTrace()
            })
    }

    private fun getDataPersonalServices() {
        val result = getPersonalIssueMore(arguments!!.getString("id"))
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                val jsonObject = JSONObject(it.string())
                val code = jsonObject.getInt("code")
                var result = ""
                if(code==200){
                    val sexs = arrayListOf("女","男")
                    result="当前数据获取成功"
                    val json = jsonObject.getJSONObject("message")
                    val adapterGenerate = AdapterGenerate()
                    adapterGenerate.context = mView.context
                    adapter = adapterGenerate.supplyIndividualDisplay()
                    val data = adapter.mData.toMutableList()
                    data.removeAt(data.size-1)
                    data[0].singleDisplayRightContent = json.getString("issuerWorkType")
                    data[1].singleDisplayRightContent = json.getString("issuerWorkerKind")
//                    data[2].singleDisplayRightContent = json.getString("contact")
                    data[3].singleDisplayRightContent = sexs[json.getInt("sex")]
                    data[4].singleDisplayRightContent = json.getString("age")
                    data[5].singleDisplayRightContent = json.getString("workExperience")
                    data[6].singleDisplayRightContent = json.getString("workMoney")+json.getString("salaryUnit")
                    data[7].singleDisplayRightContent = json.getString("contactPhone")

                    data[9].singleDisplayRightContent = json.getString("validTime")
                    data[10].singleDisplayRightContent = json.getString("issuerBelongSite")
                    data[11].singleDisplayRightContent = json.getString("remark")
                    adapter.mData = data
                    mView.rv_job_more_content.adapter = adapter
                    mView.rv_job_more_content.layoutManager = LinearLayoutManager(context)
                }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
                    result="当前数据为空"
                }
                Toast.makeText(context,result, Toast.LENGTH_SHORT).show()
            },{
                it.printStackTrace()
            })
    }
}