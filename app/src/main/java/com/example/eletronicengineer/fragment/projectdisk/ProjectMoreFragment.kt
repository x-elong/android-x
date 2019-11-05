package com.example.eletronicengineer.fragment.projectdisk

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ProfessionalActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_project_disk.view.*
import kotlinx.android.synthetic.main.fragment_project_more.view.*
import org.json.JSONObject


class ProjectMoreFragment : Fragment(){
    companion object{
        fun newInstance(args:Bundle): ProjectMoreFragment
        {
            val fragment= ProjectMoreFragment()
            fragment.arguments=args
            return fragment
        }
    }
    var json:JSONObject = JSONObject()
    lateinit var mView: View
    var pageCount = 1
    var page = 1
    var adapter:RecyclerviewAdapter = RecyclerviewAdapter(ArrayList())
    var towerSubitemId=""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_project_more,container,false)
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context=mView.context
        adapterGenerate.activity=activity as AppCompatActivity
        val type=arguments!!.getInt("type")
        if (adapter.mData.size==0)
            adapter  = switchAdapter(adapterGenerate,type)
        mView.rv_project_more_content.adapter = adapter
        mView.rv_project_more_content.itemAnimator=null
        mView.rv_project_more_content.layoutManager= LinearLayoutManager(context)
        //if(type in arrayListOf(Constants.Subitem_TYPE.OVERHEAD_TOWER_PARAMETERS.ordinal,Constants.Subitem_TYPE.NODE_PARAMETERS.ordinal,Constants.Subitem_TYPE.PASSAGEWAY_CHANNEL_PARAMETERS.ordinal)) initOnScrollListener(type)
        return mView
    }

    private fun initOnScrollListener(type: Int) {
        mView.rv_project_more_content.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                if(lastCompletelyVisibleItemPosition == layoutManager.itemCount-1 && page<=pageCount){
                    Log.i("page","$page")
                    Toast.makeText(mView.context,"滑动到底了",Toast.LENGTH_SHORT).show()
                    val networkAdapter = NetworkAdapter(adapter.mData,context!!)
                    val baseUrl = "http://192.168.1.133:8014"
                    when(type){
                        Constants.Subitem_TYPE.OVERHEAD_TOWER_PARAMETERS.ordinal->{
                            networkAdapter.getDataPowerTowerParameters(arguments!!.getLong("majorDistributionProjectId"),page,baseUrl,adapter,this@ProjectMoreFragment)
                        }
                        Constants.Subitem_TYPE.PASSAGEWAY_CHANNEL_PARAMETERS.ordinal->{
                            networkAdapter.getDataGalleryParameter(arguments!!.getLong("majorDistributionProjectId"),page,baseUrl,adapter,this@ProjectMoreFragment)
                        }

                        Constants.Subitem_TYPE.NODE_PARAMETERS.ordinal->{
                            networkAdapter.getDataNodeParameter(arguments!!.getLong("majorDistributionProjectId"),page,baseUrl,adapter,this@ProjectMoreFragment)
                        }

                    }
                    page++
                }
            }
        })
    }

    fun switchAdapter(adapterGenerate: AdapterGenerate,type: Int): RecyclerviewAdapter{
        var adapter: RecyclerviewAdapter = RecyclerviewAdapter(ArrayList())
        when(type){
            //新建项目盘
            Constants.Subitem_TYPE.NEW_PROJECT_DISK.ordinal->{
                adapter = adapterGenerate.ProfessionalNewProjectDisk()
            }
            //架空 经济指标
            Constants.Subitem_TYPE.OVERHEAD_ECONOMIC_INDICATOR.ordinal->{
                adapter = adapterGenerate.OverHeadEconomicIndicator()
                NetworkAdapter(adapter.mData,context!!).getDataAerialEconomy("1","http://192.168.1.133:8016",adapter,this,adapterGenerate,arguments!!.getString("ProjectAttributes"))
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
            Constants.Subitem_TYPE.SELF_EXAMINATION.ordinal->{
                //adapter = adapterGenerate.SelfExamination()
            }
            //监理验收
            Constants.Subitem_TYPE.SUPERVISION_ACCEPTANCE.ordinal->{
                //adapter = adapterGenerate.SupervisionAcceptance()
            }
            //业主验收
            Constants.Subitem_TYPE.OWNER_ACCEPTANCE.ordinal->{
                //adapter = adapterGenerate.OwnerAcceptance()
            }
            //资料动态
            Constants.Subitem_TYPE.DATA_DYNAMIC.ordinal->{
                adapter = adapterGenerate.DataDynamic()
            }
            //杆塔子项
            Constants.Subitem_TYPE.OVERHEAD_TOWER_SUBITEM.ordinal->{
                adapter = adapterGenerate.OverHeadTowerSubitem()
//                json = JSONObject(Gson().toJson(arguments!!.getSerializable("overheadTower")))
//                towerSubitemId = json.getString("towerSubitemId")
//                adapter.mData[1].twoColumnDisplayContent1=json.getString("poleNumber")
//                adapter.mData[1].twoColumnDisplayContent2=json.getString("powerTowerAttribute")
//                adapter.mData[2].twoColumnDisplayContent1=json.getString("powerTowerHeight")
//                adapter.mData[2].twoColumnDisplayContent2=json.getString("powerTowerType")
            }
            //杆塔子项 复测分坑
            Constants.Subitem_TYPE.OVERHEAD_RESURVEY_PITDIVIDING.ordinal->{
                adapter = adapterGenerate.OverHeadResurveyPitdividing()
            }
            //杆塔子项 基础开挖
            Constants.Subitem_TYPE.OVERHEAD_FOUNDATION_EXCAVATION.ordinal->{
                adapter = adapterGenerate.OverHeadFoundationExcavation()
                NetworkAdapter(adapter.mData,context!!).getDataAerialFoundationExcavation("1111","http://192.168.1.133:8016",adapter,this,adapterGenerate)
            }
            //杆塔子项 材料运输
            Constants.Subitem_TYPE.OVERHEAD_MATERIAL_TRANSPORTATION.ordinal->{
                adapter = adapterGenerate.OverHeadMaterialTransportation()
                NetworkAdapter(adapter.mData,context!!).getDataMaterialsTransport("ttt","http://192.168.1.133:8016",adapter,this)
            }
            //杆塔子项 预制件填埋
            Constants.Subitem_TYPE.OVERHEAD_PREFABRICATED_LANDFILL.ordinal->{
                adapter = adapterGenerate.OverHeadPrefabricatedLandfill()
                NetworkAdapter(adapter.mData,context!!).getDataPreformedUnitLandFill("2","http://192.168.1.133:8016",adapter,this)
            }
            //杆塔子项 基础浇筑
            Constants.Subitem_TYPE.OVERHEAD_FOUNDATION_POURING.ordinal->{
                adapter = adapterGenerate.OverHeadFoundationPouring()
                NetworkAdapter(adapter.mData,context!!).getDataAerialFoundationCasting("ttt","http://192.168.1.133:8016",adapter,this)
            }
            //杆塔子项 杆塔组立
            Constants.Subitem_TYPE.OVERHEAD_TOWER_ASSEMBLY.ordinal->{
                adapter = adapterGenerate.OverHeadTowerErection()
                NetworkAdapter(adapter.mData,context!!).getDataAerialPoleTowerAssemblage("5","http://192.168.1.133:8016",adapter,this)
            }
            //杆塔子项 拉线制作
            Constants.Subitem_TYPE.OVERHEAD_WIRE_DRAWING.ordinal->{
                adapter = adapterGenerate.OverHeadWireDrawing()
                NetworkAdapter(adapter.mData,context!!).getDataAerialsStayguyMakeList(towerSubitemId,"http://192.168.1.144:8016",adapter)
            }
            //杆塔子项 横担安装
            Constants.Subitem_TYPE.OVERHEAD_CROSSBAR_INSTALLATION.ordinal->{
                adapter = adapterGenerate.OverHeadCrossbarInstallation()
                NetworkAdapter(adapter.mData,context!!).getDataAerialPoleArmInstall("1","http://192.168.1.133:8016",adapter,this)
            }
            //杆塔子项 绝缘子安装
            Constants.Subitem_TYPE.OVERHEAD_INSULATOR_MOUNTING.ordinal->{
                adapter = adapterGenerate.OverHeadInsulationInstallation()
                NetworkAdapter(adapter.mData,context!!).getDataAerialInsulatorInstall(towerSubitemId,"http://192.168.1.144:8016",adapter)
            }
            //杆塔子项 焊接制作
            Constants.Subitem_TYPE.OVERHEAD_WELDING_FABRICATION.ordinal->{
                adapter = adapterGenerate.OverHeadWeldingFabrication()
                NetworkAdapter(adapter.mData,context!!).getDataAerialWeldMake("51faa51ffbaa4b16be30e4b08b692a64","http://192.168.1.144:8016",adapter)
            }
            //杆塔子项 导线架设
            Constants.Subitem_TYPE.OVERHEAD_WIRE_ERECTION.ordinal->{
                adapter = adapterGenerate.OverHeadWireErection()
            }
            //杆塔子项 附件安装
            Constants.Subitem_TYPE.OVERHEAD_ACCESSORIES_INSTALLING.ordinal->{
                adapter = adapterGenerate.OverHeadAccessoriesInstalling()
                NetworkAdapter(adapter.mData,context!!).getDataAerialEnclosureInstall(towerSubitemId,"http://192.168.1.144:8016",adapter)
            }
            //杆塔子项 设备安装
            Constants.Subitem_TYPE.OVERHEAD_EQUIPMENT_INSTALLATION.ordinal->{
                adapter = adapterGenerate.OverHeadEquipmentInstallation()
                NetworkAdapter(adapter.mData,context!!).getDataALLAerialFacilityInstall(towerSubitemId,"http://192.168.1.144:8016",adapter,this)
            }
            //杆塔子项 接地敷设
            Constants.Subitem_TYPE.OVERHEAD_GROUND_LAYING.ordinal->{
                adapter = adapterGenerate.OverHeadGroundLaying()
                NetworkAdapter(adapter.mData,context!!).getDataAerialGroundConnectionLay("22","http://192.168.1.133:8016",adapter,this,adapterGenerate)
            }
            //杆塔子项 户表安装
            Constants.Subitem_TYPE.OVERHEAD_TABLE_INSTALLATION.ordinal->{
                adapter = adapterGenerate.OverHeadTableInstallation()
                NetworkAdapter(adapter.mData,context!!).getDataAerialHouseholdInstall("kfc","http://192.168.1.133:8016",adapter,this)
            }
            //杆塔子项 带电作业
            Constants.Subitem_TYPE.OVERHEAD_LIVE_WORK.ordinal->{
                adapter = adapterGenerate.OverHeadLiveWork()
                NetworkAdapter(adapter.mData,context!!).getDataAerialElectrificationJob("5","http://192.168.1.133:8016",adapter,this)
            }

            //节点 经济指标
            Constants.Subitem_TYPE.NODE_ECONOMIC_INDICATOR.ordinal->{
                adapter = adapterGenerate.NodeEconomicIndicator()
                NetworkAdapter(adapter.mData,context!!).getDataNodeEconomy("Node-1","http://192.168.1.133:8014",adapter,this,adapterGenerate,arguments!!.getString("ProjectAttributes"))
            }
            //节点 节点参数
            Constants.Subitem_TYPE.NODE_PARAMETERS.ordinal->{
                adapter = adapterGenerate.NodeParameters()
            }
            //节点子项
            Constants.Subitem_TYPE.NODE_SUBITEMS.ordinal->{
                adapter = adapterGenerate.NodeSubitems()
//                json = JSONObject(Gson().toJson(arguments!!.getSerializable("Node")))
//                adapter.mData[1].twoColumnDisplayContent1 = json.getString("nodeIndex")
//                adapter.mData[1].twoColumnDisplayContent2 = json.getString("kind")
//                adapter.mData[2].singleDisplayLeftContent = json.getString("entail")
                for (j in 4 until adapter.mData.size){
                    adapter.mData[j].jumpListener=View.OnClickListener {
                        val data = Bundle()
                        //data.putString("nodeSubitemId",json.getString("nodeSubitemId"))
                        data.putInt("type",AdapterGenerate().getType(adapter.mData[0].title1+" "+adapter.mData[j].shiftInputTitle))
                        (activity as ProfessionalActivity).switchFragment(newInstance(data),"")
                    }
                }
            }
            //节点子项 复测分坑
            Constants.Subitem_TYPE.NODE_RESURVEY_PITDIVIDING.ordinal->{
                adapter = adapterGenerate.NodeResurveyPitdividing()
            }
            //节点子项 基础开挖
            Constants.Subitem_TYPE.NODE_FOUNDATION_EXCAVATION.ordinal->{
                adapter = adapterGenerate.NodeFoundationExcavation()
                NetworkAdapter(adapter.mData,context!!).getDataNodeBasisExcavation(arguments!!.getString("nodeSubitemId"),"http://192.168.1.133:8014",adapter,this,adapterGenerate)
            }
            //节点子项 材料运输
            Constants.Subitem_TYPE.NODE_MATERIAL_TRANSPORTATION.ordinal->{
                adapter = adapterGenerate.NodeMaterialTransportation()
                NetworkAdapter(adapter.mData,context!!).getDataNodeMaterial(arguments!!.getString("nodeSubitemId"),"http://192.168.1.133:8014",adapter,this)
            }
            //节点子项 预制构件制作及安装
            Constants.Subitem_TYPE.NODE_MANUFACTURE_INSTALLATION.ordinal->{
                adapter = adapterGenerate.NodeManufactureInstallation()
                NetworkAdapter(adapter.mData,context!!).getDataNodePreformedUnitMakeInstallation(arguments!!.getString("nodeSubitemId"),"http://192.168.1.133:8014",adapter,this)
            }
            //节点子项 基础浇筑
            Constants.Subitem_TYPE.NODE_FOUNDATION_POURING.ordinal->{
                adapter = adapterGenerate.NodeFoundationPouring()
                NetworkAdapter(adapter.mData,context!!).getDataNodeBasisPouring(arguments!!.getString("nodeSubitemId"),"http://192.168.1.133:8014",adapter,this)
            }
            //节点子项 设备安装
            Constants.Subitem_TYPE.NODE_EQUIPMENT_INSTALLATION.ordinal->{
                adapter = adapterGenerate.NodeEquipmentInstallation()
                NetworkAdapter(adapter.mData,context!!).getDataListFacility(arguments!!.getString("nodeSubitemId"),"http://192.168.1.133:8014",adapter)
            }
            //节点子项 接地敷设
            Constants.Subitem_TYPE.NODE_GROUND_LAYING.ordinal->{
                adapter = adapterGenerate.NodeGroundLaying()
                NetworkAdapter(adapter.mData,context!!).getDataNodeGroundingInstallation(arguments!!.getString("nodeSubitemId"),"http://192.168.1.133:8014",adapter,this,adapterGenerate)
            }
            //节点子项 户表安装
            Constants.Subitem_TYPE.NODE_TABLE_INSTALLATION.ordinal->{
                adapter = adapterGenerate.NodeTableInstallation()
                NetworkAdapter(adapter.mData,context!!).getDataNodeHouseholdTableInstallation(arguments!!.getString("nodeSubitemId"),"http://192.168.1.133:8014",adapter,this)
            }
            //

            //通道 经济指标
            Constants.Subitem_TYPE.PASSAGEWAY_ECONOMIC_INDICATOR.ordinal->{
                adapter = adapterGenerate.PassagewayEconomicIndicator()
                NetworkAdapter(adapter.mData,context!!).getDataNodeEconomy("Gallery-1","http://192.168.1.133:8014",adapter,this,adapterGenerate,arguments!!.getString("ProjectAttributes"))
            }
            //通道 通道参数
            Constants.Subitem_TYPE.PASSAGEWAY_CHANNEL_PARAMETERS.ordinal->{
                adapter = adapterGenerate.ChannelParameters()
            }
            //通道子项
            Constants.Subitem_TYPE.PASSAGEWAY_CHANNEL_SUBITEMS.ordinal->{
                adapter = adapterGenerate.ChannelSubitems()
//                json = JSONObject(Gson().toJson(arguments!!.getSerializable("galleryNode")))
//                adapter.mData[1].twoColumnDisplayContent1 = json.getString("galleryIndex")
//                adapter.mData[1].twoColumnDisplayContent2 = json.getString("kind")
//                adapter.mData[2].twoColumnDisplayContent1 = json.getString("snodeIndex")
//                adapter.mData[2].twoColumnDisplayContent2 = json.getString("bnodeIndex")
                for (j in 4 until adapter.mData.size){
                    adapter.mData[j].jumpListener=View.OnClickListener {
                        val data = Bundle()
                        //data.putString("nodeSubitemId",json.getString("nodeSubitemId"))
                        data.putInt("type",AdapterGenerate().getType(adapter.mData[0].title1+" "+adapter.mData[j].shiftInputTitle))
                        (activity as ProfessionalActivity).switchFragment(newInstance(data),"")
                    }
                }
            }
            //复测分坑
            Constants.Subitem_TYPE.PASSAGEWAY_RESURVEY_PITDIVIDING.ordinal->{
                adapter = adapterGenerate.PassagewayResurveyPitdividing()
            }
            //基础开挖
            Constants.Subitem_TYPE.PASSAGEWAY_FOUNDATION_EXCAVATION.ordinal->{
                adapter = adapterGenerate.PassagewayFoundationExcavation()
                NetworkAdapter(adapter.mData,context!!).getDataNodeBasisExcavation(arguments!!.getString("nodeSubitemId"),"http://192.168.1.133:8014",adapter,this,adapterGenerate)
            }
            //材料运输
            Constants.Subitem_TYPE.PASSAGEWAY_MATERIAL_TRANSPORTATION.ordinal->{
                adapter = adapterGenerate.PassagewayMaterialTransportation()
                NetworkAdapter(adapter.mData,context!!).getDataNodeMaterial(arguments!!.getString("nodeSubitemId"),"http://192.168.1.133:8014",adapter,this)
            }
            //预制构件制作及安装
            Constants.Subitem_TYPE.PASSAGEWAY_MANUFACTURE_INSTALLATION.ordinal->{
                adapter = adapterGenerate.PassagewayManufactureInstallation()
                NetworkAdapter(adapter.mData,context!!).getDataNodePreformedUnitMakeInstallation(arguments!!.getString("nodeSubitemId"),"http://192.168.1.133:8014",adapter,this)
            }
            //基础浇筑
            Constants.Subitem_TYPE.PASSAGEWAY_FOUNDATION_POURING.ordinal->{
                adapter = adapterGenerate.PassagewayFoundationPouring()
                NetworkAdapter(adapter.mData,context!!).getDataNodeBasisPouring(arguments!!.getString("nodeSubitemId"),"http://192.168.1.133:8014",adapter,this)
            }
            //电缆配管
            Constants.Subitem_TYPE.PASSAGEWAY_CABLE_PIPING.ordinal->{
                adapter = adapterGenerate.PassagewayCablePiping()
                NetworkAdapter(adapter.mData,context!!).getDataListCablePipe("GalleryParameter-1","http://192.168.1.133:8014",adapter,this)
            }
            //电缆桥架
            Constants.Subitem_TYPE.PASSAGEWAY_CABLE_TRAY.ordinal->{
                adapter = adapterGenerate.PassagewayCableTray()
                NetworkAdapter(adapter.mData,context!!).getDataCableBridge("GalleryParameter-1","http://192.168.1.133:8014",adapter,this)
            }
            //电缆敷设
            Constants.Subitem_TYPE.PASSAGEWAY_CABLE_LAYING.ordinal->{
                adapter = adapterGenerate.PassagewayCableLaying()
                NetworkAdapter(adapter.mData,context!!).getDataCableLaying("GalleryParameter-1","http://192.168.1.133:8014",adapter,this)
            }
            //电缆头制作
            Constants.Subitem_TYPE.PASSAGEWAY_CABLE_HEAD_FABRICATION.ordinal->{
                adapter = adapterGenerate.PassagewayCableHeadFabrication()
                NetworkAdapter(adapter.mData,context!!).getDataCableHeadMake(arguments!!.getString("nodeSubitemId"),"http://192.168.1.133:8014",adapter,this)
            }
            //电缆防火
            Constants.Subitem_TYPE.PASSAGEWAY_CABLE_FIRE_PROTECTION.ordinal->{
                adapter = adapterGenerate.PassagewayCableFireProtection()
                NetworkAdapter(adapter.mData,context!!).getDataCableFireroofing("GalleryParameter-1","http://192.168.1.133:8014",adapter,this)
            }
            //电缆试验
            Constants.Subitem_TYPE.PASSAGEWEAY_CABLE_TEST.ordinal->{
                adapter = adapterGenerate.PassagewayCableTest()
                NetworkAdapter(adapter.mData,context!!).getDataCableTest("GalleryParameter-1","http://192.168.1.133:8014",adapter,this)
            }
            //接地敷设
            Constants.Subitem_TYPE.PASSAGEWAY_GROUND_LAYING.ordinal->{
                adapter = adapterGenerate.PassagewayGroundLaying()
                NetworkAdapter(adapter.mData,context!!).getDataNodeGroundingInstallation(arguments!!.getString("nodeSubitemId"),"http://192.168.1.133:8014",adapter,this,adapterGenerate)
            }
            //架空 更多
            Constants.Subitem_TYPE.OVERHEAD_MORE.ordinal-> {
                //json = JSONObject(Gson().toJson(arguments!!.getSerializable("MajorDistributionProjectEntity")))
                adapter = adapterGenerate.ProfessionalOverheadMore()
//                for(i in adapter.mData)
//                {
//                    when(i.options)
//                    {
//                        MultiStyleItem.Options.TITLE ->{
//                            i.title1 = json.getString(i.key)
//                        }
//                        MultiStyleItem.Options.SINGLE_DISPLAY_LEFT->
//                        {
//                            val str:String = json.optString(i.key)
//                            i.singleDisplayLeftContent = if(str==null)"" else str
//                        }
//                        MultiStyleItem.Options.SHIFT_INPUT ->{
//                            //i.shiftInputContent = json.getString(i.key)
//                        }
//                    }
//                }
                adapter.mData[0].tvSelectListener = View.OnClickListener {
                    val option1Items = listOf("架空", "节点", "通道")
                    val option2Items = listOf(
                        listOf("经济指标", "材料清册", "杆塔参数", "耐张段", "公共点定位"),
                        listOf("经济指标", "节点参数"),
                        listOf("经济指标", "通道参数")
                    )
                    val handler = Handler(Handler.Callback {
                        when (it.what) {
                            RecyclerviewAdapter.MESSAGE_SELECT_OK -> {
                                val data = Bundle()
                                val title = it.data.getString("selectContent")
                                val adapterGenerate = AdapterGenerate()
                                val type = adapterGenerate.getType(title)
                                if (title.contains("经济指标"))
                                    data.putString("ProjectAttributes", adapter.mData[4].singleDisplayLeftContent)
                                data.putInt("type", type)
                                data.putLong("majorDistributionProjectId", json.getLong("id"))
                                (activity as ProfessionalActivity).switchFragment(newInstance(data), "")
                                false
                            }
                            else -> {
                                false
                            }
                        }
                    })

                    val selectDialog = CustomDialog(
                        CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,
                        mView.context,
                        handler,
                        option1Items,
                        option2Items
                    ).multiDialog
                    selectDialog.dialog.show()
                }
            }
        }
        return adapter
    }
}