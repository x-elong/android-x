package com.example.eletronicengineer.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.item_supply.view.*

class SupplyFragment:Fragment(){
    companion object{
        fun newInstance(args:Bundle):SupplyFragment
        {
            val fragment=SupplyFragment()
            fragment.arguments=args
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i("onCreateView","running")
        val view= inflater.inflate(R.layout.item_supply,container,false)
        val data=arguments
        val adapterGenerate=AdapterGenerate()
        adapterGenerate.context=view.context
        adapterGenerate.activity=activity as AppCompatActivity
        val adapter = switchAdapter(adapterGenerate,data!!.getInt("type"))
        view.rv_main_content.adapter=adapter
        view.rv_main_content.layoutManager= LinearLayoutManager(context)
        return view
    }
    fun switchAdapter(adapterGenerate: AdapterGenerate,Type: Int):RecyclerviewAdapter
    {
        lateinit var adapter:RecyclerviewAdapter
        when(Type){
            Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                var singleDisplayContent = "普工"
                var selectOption1Items: List<String> = listOf("普工")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_SPECIAL_WORK_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                var singleDisplayContent = "特种作业"
                var selectOption1Items: List<String> =
                    listOf("低压电工作业", "高压电工作业", "电力电缆作业", "继电保护作业", "电气试验作业", "融化焊接与热切割作业", "登高架设作业")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items

            }
            Constants.FragmentType.PERSONAL_PROFESSIONAL_OPERATION_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                var singleDisplayContent = "专业操作"
                var selectOption1Items: List<String> =
                    listOf("压接操作", "机动绞磨操作", "牵张设备操作", "起重机械操作", "钢筋工", "混凝土工", "木工", "模板工", "油漆工", "砌筑工", "通风工", "打桩工", "架子工工")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_SURVEYOR_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                var singleDisplayContent = "测量工"
                var selectOption1Items: List<String> = listOf("测量工")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_DRIVER_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                var singleDisplayContent = "驾驶员"
                var selectOption1Items: List<String> =
                    listOf("驾驶证A1", "驾驶证A2", "驾驶证A3", "驾驶证B1", "驾驶证B2", "驾驶证C1", "驾驶证C2", "驾驶证C3", "驾驶证D", "驾驶证E")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_NINE_MEMBERS_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                var singleDisplayContent = "九大员"
                var selectOption1Items: List<String> =
                    listOf("施工员", "安全员", "质量员", "材料员", "资料员", "预算员", "标准员", "机械员", "劳务员")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_REGISTRATION_CLASS_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                var singleDisplayContent = "注册类"
                var selectOption1Items: List<String> = listOf("造价工程师", "一级建造师", "安全工程师", "电气工程师")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_OTHER_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                adapter.mData[1].options=MultiStyleItem.Options.SINGLE_INPUT
                adapter.mData[1].inputSingleTitle=adapter.mData[1].singleDisplayTitle
                adapter.mData[2].options=MultiStyleItem.Options.SINGLE_INPUT
                adapter.mData[2].inputSingleTitle=adapter.mData[2].selectTitle
            }
            //团队服务——变电施工
            Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupSubstationConstruction()
                var singleDisplayContent = "变电施工队"
                adapter.mData[1].singleDisplayContent = singleDisplayContent
            }
            //团队服务——主网施工
            Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupSubstationConstruction()
                var singleDisplayContent = "主网施工队"
                adapter.mData[1].singleDisplayContent = singleDisplayContent

            }
            //团队服务——配网施工
            Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupSubstationConstruction()
                var singleDisplayContent = "配网施工队"
                adapter.mData[1].singleDisplayContent = singleDisplayContent
            }
            //团队服务——测量设计
            Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupMeasurementDesign()
            }
            //团队服务——马帮运输
            Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupCaravanTransportation()
            }
            //团队服务——桩基
            Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupPileFoundationConstruction()
            }
            //团队服务——非开挖
            Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupNonExcavation()
            }
            //团队服务——试验调试
            Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupTestDebugging()
            }
            //团队服务——跨越架
            Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupCrossingFrame()
            }
            //团队服务——运维
            Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal->{
                adapter=adapterGenerate.OperationAndMaintenance()
            }
            //租赁服务——车辆
            Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal->{
                adapter=adapterGenerate.VehicleRental()
            }
            //祖灵服务——工器具
            Constants.FragmentType.TOOL_LEASING_TYPE.ordinal->{
                adapter=adapterGenerate.EquipmentLeasing()
                adapter.mData[1].singleDisplayContent = "工器具租赁"
            }
            //租赁服务--机械
            Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal->{
                adapter=adapterGenerate.EquipmentLeasing()
                adapter.mData[1].singleDisplayContent = "机械租赁"
            }
            //租赁服务--设备
            Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal->{
                adapter=adapterGenerate.EquipmentLeasing()
                adapter.mData[1].singleDisplayContent = "设备租赁"
            }
            //培训办证
            Constants.FragmentType.TRIPARTITE_TRAINING_CERTIFICATE_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[1].singleDisplayContent = "培训办证"
            }
            //三方财务记账
            Constants.FragmentType.TRIPARTITE_FINANCIAL_ACCOUNTING_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[1].singleDisplayContent = "财务记账"
            }
            //三方代办资格
            Constants.FragmentType.TRIPARTITE_AGENCY_QUALIFICATION_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[1].singleDisplayContent = "代办资格"
            }
            //三方标书服务
            Constants.FragmentType.TRIPARTITE_TENDER_SERVICE_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[1].singleDisplayContent = "标书服务"
            }
            //三方法律咨询
            Constants.FragmentType.TRIPARTITE_LEGAL_ADVICE_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[1].singleDisplayContent = "法律咨询"
            }
            //三方软件服务
            Constants.FragmentType.TRIPARTITE_SOFTWARE_SERVICE_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[1].singleDisplayContent = "软件服务"
            }
            //三方其他
            Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[1].options=MultiStyleItem.Options.SINGLE_INPUT
                adapter.mData[1].inputSingleTitle=adapter.mData[1].singleDisplayTitle
            }
        }
        return adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}