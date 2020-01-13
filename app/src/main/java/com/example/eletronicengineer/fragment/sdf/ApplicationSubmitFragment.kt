package com.example.eletronicengineer.fragment.sdf

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
import com.example.eletronicengineer.activity.DemandDisplayActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.db.DisplayDemand.*
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.ToastHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.application_apply_fragment.view.*
import kotlinx.android.synthetic.main.fragment_demand_display.view.*
import org.json.JSONObject

class ApplicationSubmitFragment:Fragment() {
    companion object{
        fun newInstance(args:Bundle):ApplicationSubmitFragment{
            val applicationSubmitFragment = ApplicationSubmitFragment()
            applicationSubmitFragment.arguments = args
            return applicationSubmitFragment
        }
    }
    var comment:String=""//备注
    var requirementPersonId:String=""//id
    var typeVariety:String=""//类型
    var vipId:String=""
    lateinit var mView: View
    var adapter: RecyclerviewAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView=inflater.inflate(R.layout.application_apply_fragment,container,false)
        if(adapter==null){
            switchAdapter()
            mView.rv_registration_more_content.adapter=adapter
            mView.rv_registration_more_content.layoutManager=LinearLayoutManager(context)
        }
        else{
            mView.rv_registration_more_content.adapter=adapter
            mView.rv_registration_more_content.layoutManager=LinearLayoutManager(context)
        }
        mView.tv_registration_more_back.setOnClickListener {
            UnSerializeDataBase.imgList.clear()
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.button_ok.setOnClickListener {
            val networkAdapter= NetworkAdapter(adapter!!.mData,mView.context)
            if(networkAdapter.check()){
                for(i in adapter!!.mData ) {
                    when (i.options){
                        MultiStyleItem.Options.INPUT_WITH_TEXTAREA->{
                            comment=i.textAreaContent
                        }
                    }
                }
                var json=JSONObject()
                when(arguments!!.getInt("type")) {
                    Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal -> {//个人
                        json.put(
                            "personRequirementInformationCheck", JSONObject().put(
                                "requirementPersonId", requirementPersonId
                            ).put("comment", comment)
                        )
                    }
                    Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal,//主网
                    Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal,//配网
                    Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal,//变电
                    Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal,//测量设计
                    Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal,//实验调试
                    Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal,//跨越架
                    Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal//运维
                    -> {
                        json.put(
                            "requirementTeamLoggingCheck", JSONObject().put(
                                "requirementCaravanTransportId", requirementPersonId
                            ).put("type",typeVariety).put("comment", comment)).
                            put("name",UnSerializeDataBase.idCardName).put("phone",UnSerializeDataBase.userPhone)

                    }
                    Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal//马帮运输
                    ->{
                        json.put(
                            "requirementTeamLoggingCheck", JSONObject().put(
                                "requirementCaravanTransportId", requirementPersonId
                            ).put("type",typeVariety).put("comment", comment)).
                            put("name",UnSerializeDataBase.idCardName).put("phone",UnSerializeDataBase.userPhone)
                    }
                    Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal,//桩基
                    Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal//非开挖
                    ->{
                        json.put(
                            "requirementTeamLoggingCheck", JSONObject().put(
                                "requirementCaravanTransportId", requirementPersonId
                            ).put("type",typeVariety).put("comment", comment)).
                            put("name",UnSerializeDataBase.idCardName).put("phone",UnSerializeDataBase.userPhone)
                    }
                    Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal,//车辆
                    Constants.FragmentType.TOOL_LEASING_TYPE.ordinal,//工器具
                    Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal,//设备
                    Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal//机械
                    ->{
                        json.put(
                            "leaseLoggingCheck", JSONObject().put(
                                "requirementLeaseMachineryId", requirementPersonId
                            ).put("vipId",vipId).put("type",typeVariety).put("comment", comment)
                        ).put("name",UnSerializeDataBase.idCardName).put("phone",UnSerializeDataBase.userPhone)
                    }
                    Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal->//三方
                    {
                        json.put(
                            "requirementThirdLoggingCheck", JSONObject().put(
                                "requirementThirdPartyId", requirementPersonId
                            ).put("comment", comment)
                        ).put("name",UnSerializeDataBase.idCardName).put("phone",UnSerializeDataBase.userPhone)
                    }
                }
                val loadingDialog = LoadingDialog(mView.context,"正在提交...")
                loadingDialog.show()
                networkAdapter.generateJsonRequestBody(json).subscribe {
                    val result = startSendMessage(it,UnSerializeDataBase.dmsBasePath+adapter!!.urlPath).subscribeOn(
                        Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                loadingDialog.dismiss()
                                var json = JSONObject(it.string())
                                if (json.getInt("code") == 200) {
                                    if(json.getString("desc")=="FAIL"){
                                        mView.tv_registration_more_back.callOnClick()
                                        Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show()
                                    } else{
                                        mView.tv_registration_more_back.callOnClick()
                                        Toast.makeText(context, "报名成功", Toast.LENGTH_SHORT).show()
                                    }
                                } else if (json.getInt("code") == 400) {
                                    Toast.makeText(context, "报名失败", Toast.LENGTH_SHORT).show()
                                }
                            },
                            {
                                loadingDialog.dismiss()
                                val toast = Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                toast.show()
                                it.printStackTrace()
                            }
                        )
                }
            }
        }
        return mView
    }

    private fun switchAdapter() {
        val adapterGenerate=AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as DemandDisplayActivity
        var bundle=Bundle()
        when(arguments!!.getInt("type")){
            Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal->//个人
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                var listdata=arguments!!.getSerializable("RequirementPersonDetail") as RequirementPersonDetail
                bundle.putSerializable("RequirementPersonDetail",arguments!!.getSerializable("RequirementPersonDetail"))
                requirementPersonId= listdata.id
                adapter=adapterGenerate.ApplicationSubmit(bundle)
                adapter!!.urlPath = Constants.HttpUrlPath.Requirement.insertPersonRequirementInformationCheck
            }
            Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal,//主网
            Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal,//配网
            Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal,//变电
            Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal,//测量设计
            Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal,//实验调试
            Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal,//跨越架
            Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal//运维
            ->
            {
                when(arguments!!.getInt("type")){
                    Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal->{//主网
                        var listdata=arguments!!.getSerializable("RequirementMajorNetWork") as RequirementMajorNetWork
                        bundle.putSerializable("RequirementMajorNetWork",arguments!!.getSerializable("RequirementMajorNetWork"))
                        typeVariety=listdata.requirementVariety
                        requirementPersonId= listdata.id

                    }
                    Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal->//配网
                    {
                        var listdata=arguments!!.getSerializable("RequirementDistributionNetwork") as RequirementDistributionNetwork
                        bundle.putSerializable("RequirementDistributionNetwork",arguments!!.getSerializable("RequirementDistributionNetwork"))
                        typeVariety=listdata.requirementVariety
                        requirementPersonId= listdata.id
                    }
                    Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal->//变电
                    {
                        var listdata=arguments!!.getSerializable("RequirementPowerTransformation") as RequirementPowerTransformation
                        bundle.putSerializable("RequirementPowerTransformation",arguments!!.getSerializable("RequirementPowerTransformation"))
                        typeVariety=listdata.requirementVariety
                        requirementPersonId= listdata.id
                    }
                    Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal->//测量设计
                    {
                        var listdata=arguments!!.getSerializable("RequirementMeasureDesign") as RequirementMeasureDesign
                        bundle.putSerializable("RequirementMeasureDesign",arguments!!.getSerializable("RequirementMeasureDesign"))
                        typeVariety=listdata.requirementVariety
                        requirementPersonId= listdata.id
                    }
                    Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal->//实验调试
                    {
                        var listdata=arguments!!.getSerializable("RequirementTest") as RequirementTest
                        bundle.putSerializable("RequirementTest",arguments!!.getSerializable("RequirementTest"))
                        typeVariety=listdata.requirementVariety
                        requirementPersonId= listdata.id
                    }
                    Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal->//跨越架
                    {
                        var listdata=arguments!!.getSerializable("RequirementSpanWoodenSupprt") as RequirementSpanWoodenSupprt
                        bundle.putSerializable("RequirementSpanWoodenSupprt",arguments!!.getSerializable("RequirementSpanWoodenSupprt"))
                        typeVariety=listdata.requirementVariety
                        requirementPersonId= listdata.id
                    }
                    Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal->//运维
                    {
                        var listdata=arguments!!.getSerializable("RequirementRunningMaintain") as RequirementRunningMaintain
                        bundle.putSerializable("RequirementRunningMaintain",arguments!!.getSerializable("RequirementRunningMaintain"))
                        typeVariety=listdata.requirementVariety
                        requirementPersonId= listdata.id
                    }
                }
                bundle.putInt("type",arguments!!.getInt("type"))
                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))//车辆清册查看
                bundle.putSerializable("listData2",arguments!!.getSerializable("listData2"))//成员清册查看
                adapter=adapterGenerate.ApplicationSubmit(bundle)
                adapter!!.urlPath = Constants.HttpUrlPath.Requirement.insertRequirementTeamLoggingCheck
            }
            Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal-> //马帮运输
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                var listdata=arguments!!.getSerializable("RequirementCaravanTransport") as RequirementCaravanTransport
                bundle.putSerializable("RequirementCaravanTransport",arguments!!.getSerializable("RequirementCaravanTransport"))
                typeVariety=listdata.requirementVariety
                requirementPersonId= listdata.id
                adapter=adapterGenerate.ApplicationSubmit(bundle)
                adapter!!.urlPath = Constants.HttpUrlPath.Requirement.insertRequirementTeamLoggingCheck
            }
            Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal,//桩基
            Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal->//非开挖
            {
                when(arguments!!.getInt("type")){
                    Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal->{//桩基
                        var listdata=arguments!!.getSerializable("RequirementPileFoundation") as RequirementPileFoundation
                        bundle.putSerializable("RequirementPileFoundation",arguments!!.getSerializable("RequirementPileFoundation"))
                        typeVariety=listdata.requirementVariety
                        requirementPersonId= listdata.id
                    }
                    Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal-> {//非开挖
                        var listdata=arguments!!.getSerializable("RequirementUnexcavation") as RequirementUnexcavation
                        bundle.putSerializable("RequirementUnexcavation",arguments!!.getSerializable("RequirementUnexcavation"))
                        typeVariety=listdata.requirementVariety
                        requirementPersonId= listdata.id
                    }
                }
                bundle.putInt("type",arguments!!.getInt("type"))
                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))//车辆清册查看
                adapter=adapterGenerate.ApplicationSubmit(bundle)
                adapter!!.urlPath = Constants.HttpUrlPath.Requirement.insertRequirementTeamLoggingCheck
            }
            Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal->//车辆
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                bundle.putSerializable("RequirementLeaseCar",arguments!!.getSerializable("RequirementLeaseCar"))
                val listdata=arguments!!.getSerializable("RequirementLeaseCar") as RequirementLeaseCar
                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
                typeVariety=listdata.requirementVariety
                vipId=listdata.vipId
                requirementPersonId= listdata.id
                adapter=adapterGenerate.ApplicationSubmit(bundle)
                adapter!!.urlPath = Constants.HttpUrlPath.Requirement.insertLeaseLoggingCheckController
            }
            Constants.FragmentType.TOOL_LEASING_TYPE.ordinal,//工器具
            Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal,//设备
            Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal//机械
            ->{
                when(arguments!!.getInt("type")){
                    Constants.FragmentType.TOOL_LEASING_TYPE.ordinal->{//工器具
                        var listdata=arguments!!.getSerializable("RequirementLeaseConstructionTool") as RequirementLeaseConstructionTool
                        bundle.putSerializable("RequirementLeaseConstructionTool",arguments!!.getSerializable("RequirementLeaseConstructionTool"))
                        typeVariety=listdata.requirementVariety
                        vipId=listdata.vipId
                        requirementPersonId= listdata.id
                    }
                    Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal-> {//设备
                        bundle.putSerializable("RequirementLeaseFacility",arguments!!.getSerializable("RequirementLeaseFacility"))
                        var listdata=arguments!!.getSerializable("RequirementLeaseFacility") as RequirementLeaseFacility
                        typeVariety=listdata.requirementVariety
                        vipId=listdata.vipId
                        requirementPersonId= listdata.id

                    }
                    Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal->{//机械
                        bundle.putSerializable("RequirementLeaseMachinery",arguments!!.getSerializable("RequirementLeaseMachinery"))
                        var listdata=arguments!!.getSerializable("RequirementLeaseMachinery") as RequirementLeaseMachinery
                        typeVariety=listdata.requirementVariety
                        vipId=listdata.vipId
                        requirementPersonId= listdata.id
                    }
                }
                bundle.putInt("type",arguments!!.getInt("type"))
                bundle.putSerializable("listData4",arguments!!.getSerializable("listData4"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
                adapter!!.urlPath = Constants.HttpUrlPath.Requirement.insertLeaseLoggingCheckController
            }
            Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal->//三方
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                bundle.putSerializable("RequirementThirdPartyDetail",arguments!!.getSerializable("RequirementThirdPartyDetail"))
                bundle.putSerializable("listData5",arguments!!.getSerializable("listData5"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
                var listdata=arguments!!.getSerializable("RequirementThirdPartyDetail") as RequirementThirdPartyDetail
                requirementPersonId= listdata.id
                adapter!!.urlPath = Constants.HttpUrlPath.Requirement.insertRequirementThirdLoggingCheck
            }
        }
    }
    fun check(itemMultiStyleItem:List<MultiStyleItem>):Boolean{

        for(j in itemMultiStyleItem)
        {
            when (j.options) {
                MultiStyleItem.Options.SHIFT_INPUT -> {
                    if (j.necessary == false) {
                        return false
                    }
                }
            }
        }

        return true
    }

    fun update(itemMultiStyleItem:List<MultiStyleItem>){
//        val a = when(adapter!!.mData[adapter!!.mData[0].selected].buttonTitle){
//            "成员清册" ->MemberDataSize
//            "车辆清册"->VehicleDataSize
//            else ->0
//        }
        if(check(itemMultiStyleItem))
        {
            adapter!!.mData[adapter!!.mData[0].selected].SubmitIsNecessary = true
        }



        Log.i("position is",adapter!!.mData[0].selected.toString())
        adapter!!.mData[adapter!!.mData[0].selected].itemMultiStyleItem = itemMultiStyleItem
        Log.i("item size",adapter!!.mData[adapter!!.mData[0].selected].itemMultiStyleItem.size.toString())
    }
}