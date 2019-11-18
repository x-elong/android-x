package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.DemandDisplayActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.distributionFileSave.*
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.UnSerializeDataBase
import kotlinx.android.synthetic.main.application_apply_fragment.view.*
import kotlinx.android.synthetic.main.fragment_demand_display.view.*

class ApplicationSubmitFragment:Fragment() {
    companion object{
        fun newInstance(args:Bundle):ApplicationSubmitFragment{
            val applicationSubmitFragment = ApplicationSubmitFragment()
            applicationSubmitFragment.arguments = args
            return applicationSubmitFragment
        }
    }
    var MemberDataSize:Int = 0
    var VehicleDataSize:Int = 0
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
            mView.tv_application_content.adapter=adapter
            mView.tv_application_content.layoutManager=LinearLayoutManager(context)
        }
        else{
            mView.tv_application_content.adapter=adapter
            mView.tv_application_content.layoutManager=LinearLayoutManager(context)
        }
        mView.button_ok.setOnClickListener {
            val networkAdapter= NetworkAdapter(adapter!!.mData,mView.context)
            if(networkAdapter.check()){
                networkAdapter.generateJsonRequestBody(adapter!!.baseUrl+adapter!!.urlPath)
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
                MemberDataSize=arguments!!.getString("needPeopleNumber").toInt()
                bundle.putInt("type",arguments!!.getInt("type"))
                bundle.putSerializable("RequirementPersonDetail",arguments!!.getSerializable("RequirementPersonDetail"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
            Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal->//主网
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                var ListData=arguments!!.getSerializable("RequirementMajorNetWork") as RequirementMajorNetWork
                MemberDataSize=ListData.needPeopleNumber.toInt()
                // VehicleDataSize=ListData.vehicle.toInt()
                bundle.putSerializable("RequirementMajorNetWork",arguments!!.getSerializable("RequirementMajorNetWork"))
//                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
//                bundle.putSerializable("listData2",arguments!!.getSerializable("listData2"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
            Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal->//配网
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                var ListData=arguments!!.getSerializable("RequirementDistributionNetwork") as RequirementDistributionNetwork
                MemberDataSize=ListData.needPeopleNumber.toInt()
                // VehicleDataSize=ListData.vehicle.toInt()
                bundle.putSerializable("RequirementDistributionNetwork",arguments!!.getSerializable("RequirementDistributionNetwork"))
//                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
//                bundle.putSerializable("listData2",arguments!!.getSerializable("listData2"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
            Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal->//变电
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                var ListData=arguments!!.getSerializable("RequirementPowerTransformation") as RequirementPowerTransformation
                MemberDataSize=ListData.needPeopleNumber.toInt()
                // VehicleDataSize=ListData.vehicle.toInt()
                bundle.putSerializable("RequirementPowerTransformation",arguments!!.getSerializable("RequirementPowerTransformation"))
//                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
//                bundle.putSerializable("listData2",arguments!!.getSerializable("listData2"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
            Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal->//测量设计
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                var ListData=arguments!!.getSerializable("RequirementMeasureDesign") as RequirementMeasureDesign
                MemberDataSize=ListData.needPeopleNumber.toInt()
                // VehicleDataSize=ListData.vehicle.toInt()
                bundle.putSerializable("RequirementMeasureDesign",arguments!!.getSerializable("RequirementMeasureDesign"))
//                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
//                bundle.putSerializable("listData2",arguments!!.getSerializable("listData2"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
            Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal->//实验调试
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                var ListData=arguments!!.getSerializable("RequirementTest") as RequirementTest
                MemberDataSize=ListData.needPeopleNumber.toInt()
                //VehicleDataSize=ListData.vehicle.toInt()
                bundle.putSerializable("RequirementTest",arguments!!.getSerializable("RequirementTest"))
//                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
//                bundle.putSerializable("listData2",arguments!!.getSerializable("listData2"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
            Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal->//跨越架
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                var ListData=arguments!!.getSerializable("RequirementSpanWoodenSupprt") as RequirementSpanWoodenSupprt
                MemberDataSize=ListData.needPeopleNumber.toInt()
                // VehicleDataSize=ListData.vehicle.toInt()
                bundle.putSerializable("RequirementSpanWoodenSupprt",arguments!!.getSerializable("RequirementSpanWoodenSupprt"))
//                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
//                bundle.putSerializable("listData2",arguments!!.getSerializable("listData2"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
            Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal->//运维
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                var ListData=arguments!!.getSerializable("RequirementRunningMaintain") as RequirementRunningMaintain
                MemberDataSize=ListData.needPeopleNumber.toInt()
                // VehicleDataSize=ListData.vehicle.toInt()
                bundle.putSerializable("RequirementRunningMaintain",arguments!!.getSerializable("RequirementRunningMaintain"))
//                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
//                bundle.putSerializable("listData2",arguments!!.getSerializable("listData2"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
            Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal-> //马帮运输
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                bundle.putSerializable("RequirementCaravanTransport",arguments!!.getSerializable("RequirementCaravanTransport"))
//                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
            Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal->//桩基
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                var ListData=arguments!!.getSerializable("RequirementPileFoundation") as RequirementPileFoundation
//                VehicleDataSize=ListData.vehicle.toInt()
                bundle.putSerializable("RequirementPileFoundation",arguments!!.getSerializable("RequirementPileFoundation"))
//                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
            Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal->//非开挖
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                var ListData=arguments!!.getSerializable("RequirementUnexcavation") as RequirementUnexcavation
//                VehicleDataSize=ListData.vehicle.toInt()
                bundle.putSerializable("RequirementUnexcavation",arguments!!.getSerializable("RequirementUnexcavation"))
//                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
            Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal->//车辆
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                var ListData=arguments!!.getSerializable("RequirementLeaseCar") as RequirementLeaseCar
                VehicleDataSize=ListData.vehicle.toInt()
                bundle.putSerializable("RequirementLeaseCar",arguments!!.getSerializable("RequirementLeaseCar"))
//                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
            Constants.FragmentType.TOOL_LEASING_TYPE.ordinal->//工器具
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                bundle.putSerializable("RequirementLeaseConstructionTool",arguments!!.getSerializable("RequirementLeaseConstructionTool"))
//                bundle.putSerializable("listData4",arguments!!.getSerializable("listData4"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
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
            adapter!!.mData[adapter!!.mData[0].selected].necessary = true
        }



        Log.i("position is",adapter!!.mData[0].selected.toString())
        adapter!!.mData[adapter!!.mData[0].selected].itemMultiStyleItem = itemMultiStyleItem
        Log.i("item size",adapter!!.mData[adapter!!.mData[0].selected].itemMultiStyleItem.size.toString())
    }
}