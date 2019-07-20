package com.example.eletronicengineer.fragment.projectdisk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.fragment_project_more.view.*


class ProjectMoreFragment : Fragment(){
    companion object{
        fun newInstance(args:Bundle): ProjectMoreFragment
        {
            val fragment= ProjectMoreFragment()
            fragment.arguments=args
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_project_more,container,false)
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context=view.context
        adapterGenerate.activity=activity as AppCompatActivity
        val adapter = switchAdapter(adapterGenerate,arguments!!.getInt("type"))
        view.rv_project_more_content.adapter = adapter
        view.rv_project_more_content.layoutManager= LinearLayoutManager(context)
        return view
    }
    fun switchAdapter(adapterGenerate: AdapterGenerate,type: Int): RecyclerviewAdapter{
        lateinit var adapter: RecyclerviewAdapter
        when(type){
            //新建项目盘
            Constants.Subitem_TYPE.NEW_PROJECT_DISK.ordinal->{
                adapter = adapterGenerate.Professional_NewProjectDisk()
            }
            //架空 经济指标
            Constants.Subitem_TYPE.OVERHEAD_ECONOMIC_INDICATOR.ordinal->{
                 adapter = adapterGenerate.OverHeadEconomicIndicator()
            }
            //架空 杆塔参数
            Constants.Subitem_TYPE.OVERHEAD_TOWER_PARAMETERS.ordinal->{
                adapter = adapterGenerate.OverHeadTowerParameters()
            }
            //耐张段
            Constants.Subitem_TYPE.OVERHEAD_TENSILE_SEGMENT.ordinal->{
                adapter = adapterGenerate.OverHeadTensileSegment()
            }
            //公共点定位
            Constants.Subitem_TYPE.OVERHEAD_PUBLIC_POINT_POSITION.ordinal->{
                adapter = adapterGenerate.OverHeadPublicPointPosition()
            }
            //自查自检
            Constants.Subitem_TYPE.PASSAGEWAY_SELF_EXAMINATION.ordinal,
            Constants.Subitem_TYPE.NODE_SELF_EXAMINATION.ordinal,
            Constants.Subitem_TYPE.OVERHEAD_SELF_EXAMINATION.ordinal->{
                adapter = adapterGenerate.SelfExamination()
            }
            //监理验收
            Constants.Subitem_TYPE.PASSAGEWAY_SUPERVISION_ACCEPTANCE.ordinal,
            Constants.Subitem_TYPE.NODE_SUPERVISION_ACCEPTANCE.ordinal,
            Constants.Subitem_TYPE.OVERHEAD_SUPERVISION_ACCEPTANCE.ordinal->{
                adapter = adapterGenerate.SupervisionAcceptance()
            }
            //业主验收
            Constants.Subitem_TYPE.PASSAGEWAY_OWNER_ACCEPTANCE.ordinal,
            Constants.Subitem_TYPE.NODE_OWNER_ACCEPTANCE.ordinal,
            Constants.Subitem_TYPE.OVERHEAD_OWNER_ACCEPTANCE.ordinal->{
                adapter = adapterGenerate.OwnerAcceptance()
            }
            //资料动态
            Constants.Subitem_TYPE.PASSAGEWAY_DATA_DYNAMIC.ordinal,
            Constants.Subitem_TYPE.NODE_DATA_DYNAMIC.ordinal,
            Constants.Subitem_TYPE.OVERHEAD_DATA_DYNAMIC.ordinal->{
                adapter = adapterGenerate.DataDynamic()
            }
            //杆塔子项
            Constants.Subitem_TYPE.OVERHEAD_TOWER_SUBITEM.ordinal->{
                adapter = adapterGenerate.OverHeadTowerSubitem()
            }
            //杆塔子项 复测分坑
            Constants.Subitem_TYPE.OVERHEAD_RESURVEY_PITDIVIDING.ordinal->{
                adapter = adapterGenerate.OverHeadResurveyPitdividing()
            }
            //杆塔子项 基础开挖
            Constants.Subitem_TYPE.OVERHEAD_FOUNDATION_EXCAVATION.ordinal->{
                adapter = adapterGenerate.OverHeadFoundationExcavation()
            }
            //杆塔子项 材料运输
            Constants.Subitem_TYPE.OVERHEAD_MATERIAL_TRANSPORTATION.ordinal->{
                adapter = adapterGenerate.OverHeadMaterialTransportation()
            }
            //杆塔子项 预制件填埋
            Constants.Subitem_TYPE.OVERHEAD_PREFABRICATED_LANDFILL.ordinal->{
                adapter = adapterGenerate.OverHeadPrefabricatedLandfill()
            }
            //杆塔子项 基础浇筑
            Constants.Subitem_TYPE.OVERHEAD_FOUNDATION_POURING.ordinal->{
                adapter = adapterGenerate.OverHeadFoundationPouring()
            }
            //杆塔子项 杆塔组立
            Constants.Subitem_TYPE.OVERHEAD_TOWER_ASSEMBLY.ordinal->{
                adapter = adapterGenerate.OverHeadTowerErection()
            }
            //杆塔子项 拉线制作
            Constants.Subitem_TYPE.OVERHEAD_WIRE_DRAWING.ordinal->{
                adapter = adapterGenerate.OverHeadWireDrawing()
            }
            //杆塔子项 横担安装
            Constants.Subitem_TYPE.OVERHEAD_CROSSBAR_INSTALLATION.ordinal->{
                adapter = adapterGenerate.OverHeadCrossbarInstallation()
            }
            //杆塔子项 绝缘子安装
            Constants.Subitem_TYPE.OVERHEAD_INSULATOR_MOUNTING.ordinal->{
                adapter = adapterGenerate.OverHeadInsulationInstallation()
            }
            //杆塔子项 焊接制作
            Constants.Subitem_TYPE.OVERHEAD_WELDING_FABRICATION.ordinal->{
                adapter = adapterGenerate.OverHeadWeldingFabrication()
            }
            //杆塔子项 导线架设
            Constants.Subitem_TYPE.OVERHEAD_WIRE_ERECTION.ordinal->{
                adapter = adapterGenerate.OverHeadWireErection()
            }
            //杆塔子项 附件安装
            Constants.Subitem_TYPE.OVERHEAD_ACCESSORIES_INSTALLING.ordinal->{
                adapter = adapterGenerate.OverHeadAccessoriesInstalling()
            }
            //杆塔子项 设备安装
            Constants.Subitem_TYPE.OVERHEAD_EQUIPMENT_INSTALLATION.ordinal->{
                adapter = adapterGenerate.OverHeadEquipmentInstallation()
            }
            //杆塔子项 接地敷设
            Constants.Subitem_TYPE.OVERHEAD_GROUND_LAYING.ordinal->{
                adapter = adapterGenerate.OverHeadGroundLaying()
            }
            //杆塔子项 户表安装
            Constants.Subitem_TYPE.OVERHEAD_TABLE_INSTALLATION.ordinal->{
                adapter = adapterGenerate.OverHeadTableInstallation()
            }
            //杆塔子项 带电作业
            Constants.Subitem_TYPE.OVERHEAD_LIVE_WORK.ordinal->{
                adapter = adapterGenerate.OverHeadLiveWork()
            }

            //节点 经济指标
            Constants.Subitem_TYPE.NODE_ECONOMIC_INDICATOR.ordinal->{
                adapter = adapterGenerate.NodeEconomicIndicator()
            }
            //节点 节点参数
             Constants.Subitem_TYPE.NODE_PARAMETERS.ordinal->{
                 adapter = adapterGenerate.NodeParameters()
             }
            //节点子项
            Constants.Subitem_TYPE.NODE_SUBITEMS.ordinal->{
                adapter = adapterGenerate.NodeSubitems()
            }
            //节点子项 复测分坑
            Constants.Subitem_TYPE.NODE_RESURVEY_PITDIVIDING.ordinal->{
                adapter = adapterGenerate.NodeResurveyPitdividing()
            }
            //节点子项 基础开挖
            Constants.Subitem_TYPE.NODE_FOUNDATION_EXCAVATION.ordinal->{
                adapter = adapterGenerate.NodeFoundationExcavation()
            }
            //节点子项 材料运输
            Constants.Subitem_TYPE.NODE_MATERIAL_TRANSPORTATION.ordinal->{
                adapter = adapterGenerate.NodeMaterialTransportation()
            }
            //节点子项 预制构件制作及安装
            Constants.Subitem_TYPE.NODE_MANUFACTURE_INSTALLATION.ordinal->{
                adapter = adapterGenerate.NodeManufactureInstallation()
            }
            //节点子项 基础浇筑
            Constants.Subitem_TYPE.NODE_FOUNDATION_POURING.ordinal->{
                adapter = adapterGenerate.NodeFoundationPouring()
            }
            //节点子项 设备安装
            Constants.Subitem_TYPE.NODE_EQUIPMENT_INSTALLATION.ordinal->{
                adapter = adapterGenerate.NodeEquipmentInstallation()
            }
            //节点子项 接地敷设
            Constants.Subitem_TYPE.NODE_GROUND_LAYING.ordinal->{
                adapter = adapterGenerate.NodeGroundLaying()
            }
            //节点子项 户表安装
            Constants.Subitem_TYPE.NODE_TABLE_INSTALLATION.ordinal->{
                adapter = adapterGenerate.NodeTableInstallation()
            }
            //

            //通道 经济指标
            Constants.Subitem_TYPE.PASSAGEWAY_ECONOMIC_INDICATOR.ordinal->{
                adapter = adapterGenerate.PassagewayEconomicIndicator()
            }
            //通道 通道参数
            Constants.Subitem_TYPE.PASSAGEWAY_CHANNEL_PARAMETERS.ordinal->{
                adapter = adapterGenerate.ChannelParameters()
            }
            //通道子项
            Constants.Subitem_TYPE.PASSAGEWAY_CHANNEL_SUBITEMS.ordinal->{
                adapter = adapterGenerate.ChannelSubitems()
            }
            //复测分坑
            Constants.Subitem_TYPE.PASSAGEWAY_RESURVEY_PITDIVIDING.ordinal->{
                adapter = adapterGenerate.PassagewayResurveyPitdividing()
            }
            //基础开挖
            Constants.Subitem_TYPE.PASSAGEWAY_FOUNDATION_EXCAVATION.ordinal->{
                adapter = adapterGenerate.PassagewayFoundationExcavation()
            }
            //材料运输
            Constants.Subitem_TYPE.PASSAGEWAY_MATERIAL_TRANSPORTATION.ordinal->{
                adapter = adapterGenerate.PassagewayMaterialTransportation()
            }
            //预制构件制作及安装
            Constants.Subitem_TYPE.PASSAGEWAY_MANUFACTURE_INSTALLATION.ordinal->{
                adapter = adapterGenerate.PassagewayManufactureInstallation()
            }
            //基础浇筑
            Constants.Subitem_TYPE.PASSAGEWAY_FOUNDATION_POURING.ordinal->{
                adapter = adapterGenerate.PassagewayFoundationPouring()
            }
            //电缆配管
            Constants.Subitem_TYPE.PASSAGEWAY_CABLE_PIPING.ordinal->{
                adapter = adapterGenerate.PassagewayCablePiping()
            }
            //电缆桥架
            Constants.Subitem_TYPE.PASSAGEWAY_CABLE_TRAY.ordinal->{
                adapter = adapterGenerate.PassagewayCableTray()
            }
            //电缆敷设
            Constants.Subitem_TYPE.PASSAGEWAY_CABLE_LAYING.ordinal->{
                adapter = adapterGenerate.NodeGroundLaying()
            }
            //电缆头制作
            Constants.Subitem_TYPE.PASSAGEWAY_CABLE_HEAD_FABRICATION.ordinal->{
                adapter = adapterGenerate.PassagewayCableHeadFabrication()
            }
            //电缆防火
            Constants.Subitem_TYPE.PASSAGEWAY_CABLE_FIRE_PROTECTION.ordinal->{
                adapter = adapterGenerate.PassagewayCableFireProtection()
            }
            //电缆试验
            Constants.Subitem_TYPE.PASSAGEWEAY_CABLE_TEST.ordinal->{
                adapter = adapterGenerate.PassagewayCableTest()
            }
            //接地敷设
            Constants.Subitem_TYPE.PASSAGEWAY_GROUND_LAYING.ordinal->{
                adapter = adapterGenerate.OverHeadGroundLaying()
            }
            //架空 更多
            Constants.Subitem_TYPE.OVERHEAD_MORE.ordinal->{
                adapter = adapterGenerate.Professional_OverheadMore()
                adapter.mData[0].title1 = arguments!!.getString("title")
            }
        }
        return adapter
    }

}