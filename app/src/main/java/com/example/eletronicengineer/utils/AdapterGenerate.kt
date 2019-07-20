package com.example.eletronicengineer.utils

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.codekidlabs.storagechooser.StorageChooser
import com.electric.engineering.model.MultiStyleItem
import com.electric.engineering.utils.ItemGenerate
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ProfessionalActivity
import com.example.eletronicengineer.activity.ProjectDiskActivity
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.fragment.projectdisk.ProjectMoreFragment
import com.example.eletronicengineer.fragment.sdf.InventoryFragment
import com.example.eletronicengineer.fragment.sdf.UploadPhoneFragment
import com.example.eletronicengineer.model.Constants
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.dialog_soil_ratio.*
import kotlinx.android.synthetic.main.dialog_soil_ratio.view.*
import kotlinx.android.synthetic.main.item_input_with_multi_unit.view.*
import kotlinx.android.synthetic.main.shift_dialog.view.*
import java.util.*
import kotlin.collections.ArrayList


class AdapterGenerate
{
    //转化成常量类型
    fun getType(content:String):Int{
        when(content){

            //供需
            "需求个人 普工","个人劳务 普工"-> return Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal
            "需求个人 特种作业","个人劳务 特种作业"-> return Constants.FragmentType.PERSONAL_SPECIAL_WORK_TYPE.ordinal
            "需求个人 专业操作","个人劳务 专业操作"-> return Constants.FragmentType.PERSONAL_PROFESSIONAL_OPERATION_TYPE.ordinal
            "需求个人 测量工","个人劳务 测量工"-> return Constants.FragmentType.PERSONAL_SURVEYOR_TYPE.ordinal
            "需求个人 驾驶员","个人劳务 驾驶员"-> return Constants.FragmentType.PERSONAL_DRIVER_TYPE.ordinal
            "需求个人 九大员","个人劳务 九大员"-> return Constants.FragmentType.PERSONAL_NINE_MEMBERS_TYPE.ordinal
            "需求个人 注册类","个人劳务 注册类"-> return Constants.FragmentType.PERSONAL_REGISTRATION_CLASS_TYPE.ordinal
            "需求个人 其他","个人劳务 其他"-> return Constants.FragmentType.PERSONAL_OTHER_TYPE.ordinal
            "需求团队 变电施工队","团队服务 变电施工队"->return Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal
            "需求团队 主网施工队","团队服务 主网施工队"->return Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal
            "需求团队 配网施工队","团队服务 配网施工队"->return Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal
            "需求团队 测量设计","团队服务 测量设计"->return Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal
            "需求团队 马帮运输","团队服务 马帮运输"->return Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal
            "需求团队 桩基服务","团队服务 桩基服务"->return Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal
            "需求团队 非开挖顶拉管作业","团队服务 非开挖顶拉管作业"->return Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal
            "需求团队 试验调试","团队服务 试验调试"->return Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal
            "需求团队 跨越架","团队服务 跨越架"->return Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal
            "需求团队 运行维护","团队服务 运行维护"->return Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal
            "需求租赁 车辆租赁","租赁服务 车辆租赁"->return Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal
            "需求租赁 工器具租赁","租赁服务 工器具租赁"->return Constants.FragmentType.TOOL_LEASING_TYPE.ordinal
            "需求租赁 机械租赁","租赁服务 机械租赁"->return Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal
            "需求租赁 设备租赁","租赁服务 设备租赁"->return Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal
            "需求三方 代办资格","三方服务 代办资格"->return Constants.FragmentType.TRIPARTITE_AGENCY_QUALIFICATION_TYPE.ordinal
            "需求三方 培训办证","三方服务 培训办证"->return Constants.FragmentType.TRIPARTITE_TRAINING_CERTIFICATE_TYPE.ordinal
            "需求三方 财务记账","三方服务 财务记账"->return Constants.FragmentType.TRIPARTITE_FINANCIAL_ACCOUNTING_TYPE.ordinal
            "需求三方 标书服务","三方服务 标书服务"->return Constants.FragmentType.TRIPARTITE_TENDER_SERVICE_TYPE.ordinal
            "需求三方 法律咨询","三方服务 法律咨询"->return Constants.FragmentType.TRIPARTITE_LEGAL_ADVICE_TYPE.ordinal
            "需求三方 软件服务","三方服务 软件服务"->return Constants.FragmentType.TRIPARTITE_SOFTWARE_SERVICE_TYPE.ordinal
            "需求三方 其他","三方服务 其他"->return Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal

            //专业

            "新建项目盘"->return Constants.Subitem_TYPE.NEW_PROJECT_DISK.ordinal
            "架空 经济指标"->return Constants.Subitem_TYPE.OVERHEAD_ECONOMIC_INDICATOR.ordinal
            //"架空 材料清册"->return Constants.Subitem_TYPE

            "架空 耐张段"->return Constants.Subitem_TYPE.OVERHEAD_TENSILE_SEGMENT.ordinal
            "架空 杆塔参数"->return Constants.Subitem_TYPE.OVERHEAD_TOWER_PARAMETERS.ordinal
            "架空 公共点定位"->return Constants.Subitem_TYPE.OVERHEAD_PUBLIC_POINT_POSITION.ordinal
            "架空 自查自检"->return Constants.Subitem_TYPE.OVERHEAD_SELF_EXAMINATION.ordinal
            "架空 监理验收"->return Constants.Subitem_TYPE.OVERHEAD_SUPERVISION_ACCEPTANCE.ordinal
            "架空 业主验收"->return Constants.Subitem_TYPE.OVERHEAD_OWNER_ACCEPTANCE.ordinal
            "架空 资料动态"->return Constants.Subitem_TYPE.OVERHEAD_DATA_DYNAMIC.ordinal
            "杆塔子项"->return Constants.Subitem_TYPE.OVERHEAD_TOWER_SUBITEM.ordinal

            "杆塔子项 复测分坑"->return Constants.Subitem_TYPE.OVERHEAD_RESURVEY_PITDIVIDING.ordinal
            "杆塔子项 基础开挖"->return Constants.Subitem_TYPE.OVERHEAD_FOUNDATION_EXCAVATION.ordinal
            "杆塔子项 材料运输"->return Constants.Subitem_TYPE.OVERHEAD_MATERIAL_TRANSPORTATION.ordinal
            "杆塔子项 预制件填埋"->return Constants.Subitem_TYPE.OVERHEAD_PREFABRICATED_LANDFILL.ordinal
            "杆塔子项 基础浇筑"->return Constants.Subitem_TYPE.OVERHEAD_FOUNDATION_POURING.ordinal
            "杆塔子项 杆塔组立"->return Constants.Subitem_TYPE.OVERHEAD_TOWER_ASSEMBLY.ordinal
            "杆塔子项 拉线制作"->return Constants.Subitem_TYPE.OVERHEAD_WIRE_DRAWING.ordinal
            "杆塔子项 横担安装"->return Constants.Subitem_TYPE.OVERHEAD_CROSSBAR_INSTALLATION.ordinal
            "杆塔子项 绝缘子安装"->return Constants.Subitem_TYPE.OVERHEAD_INSULATOR_MOUNTING.ordinal
            "杆塔子项 焊接制作"->return Constants.Subitem_TYPE.OVERHEAD_WELDING_FABRICATION.ordinal
            "杆塔子项 导线架设"->return Constants.Subitem_TYPE.OVERHEAD_WIRE_ERECTION.ordinal
            "杆塔子项 附件安装"->return Constants.Subitem_TYPE.OVERHEAD_ACCESSORIES_INSTALLING.ordinal
            "杆塔子项 设备安装"->return Constants.Subitem_TYPE.OVERHEAD_EQUIPMENT_INSTALLATION.ordinal
            "杆塔子项 接地敷设"->return Constants.Subitem_TYPE.OVERHEAD_GROUND_LAYING.ordinal
            "杆塔子项 户表安装"->return Constants.Subitem_TYPE.OVERHEAD_TABLE_INSTALLATION.ordinal
            "杆塔子项 带电作业"->return Constants.Subitem_TYPE.OVERHEAD_LIVE_WORK.ordinal

            "节点 经济指标"->return Constants.Subitem_TYPE.NODE_ECONOMIC_INDICATOR.ordinal
            "节点 节点参数"->return Constants.Subitem_TYPE.NODE_PARAMETERS.ordinal
            "节点子项"->return Constants.Subitem_TYPE.NODE_SUBITEMS.ordinal

            "节点子项 复测分坑"->return Constants.Subitem_TYPE.NODE_RESURVEY_PITDIVIDING.ordinal
            "节点子项 基础开挖"->return Constants.Subitem_TYPE.NODE_FOUNDATION_EXCAVATION.ordinal
            "节点子项 材料运输"->return Constants.Subitem_TYPE.NODE_MATERIAL_TRANSPORTATION.ordinal
            "节点子项 预制构件制作及安装"->return Constants.Subitem_TYPE.NODE_MANUFACTURE_INSTALLATION.ordinal
            "节点子项 基础浇筑"->return Constants.Subitem_TYPE.NODE_FOUNDATION_POURING.ordinal
            "节点子项 设备安装"->return Constants.Subitem_TYPE.NODE_EQUIPMENT_INSTALLATION.ordinal
            "节点子项 接地敷设"->return Constants.Subitem_TYPE.NODE_GROUND_LAYING.ordinal
            "节点子项 户表安装"->return Constants.Subitem_TYPE.NODE_TABLE_INSTALLATION.ordinal

            "节点 自查自检"->return Constants.Subitem_TYPE.NODE_SELF_EXAMINATION.ordinal
            "节点 监理验收"->return Constants.Subitem_TYPE.NODE_SUPERVISION_ACCEPTANCE.ordinal
            "节点 业主验收"->return Constants.Subitem_TYPE.NODE_OWNER_ACCEPTANCE.ordinal
            "节点 资料动态"->return Constants.Subitem_TYPE.NODE_DATA_DYNAMIC.ordinal

            "通道 经济指标"->return Constants.Subitem_TYPE.PASSAGEWAY_ECONOMIC_INDICATOR.ordinal
            "通道 通道参数"->return Constants.Subitem_TYPE.PASSAGEWAY_CHANNEL_PARAMETERS.ordinal
            "通道子项"->return Constants.Subitem_TYPE.PASSAGEWAY_CHANNEL_SUBITEMS.ordinal


            "通道子项 复测分坑"->return Constants.Subitem_TYPE.PASSAGEWAY_RESURVEY_PITDIVIDING.ordinal
            "通道子项 基础开挖"->return Constants.Subitem_TYPE.PASSAGEWAY_FOUNDATION_EXCAVATION.ordinal
            "通道子项 材料运输"->return Constants.Subitem_TYPE.PASSAGEWAY_MATERIAL_TRANSPORTATION.ordinal
            "通道子项 预制构件制作及安装"->return Constants.Subitem_TYPE.PASSAGEWAY_MANUFACTURE_INSTALLATION.ordinal
            "通道子项 基础浇筑"->return Constants.Subitem_TYPE.PASSAGEWAY_FOUNDATION_POURING.ordinal
            "通道子项 电缆配管"->return Constants.Subitem_TYPE.PASSAGEWAY_CABLE_PIPING.ordinal
            "通道子项 电缆桥架"->return Constants.Subitem_TYPE.PASSAGEWAY_CABLE_TRAY.ordinal
            "通道子项 电缆敷设"->return Constants.Subitem_TYPE.PASSAGEWAY_CABLE_LAYING.ordinal
            "通道子项 电缆头制作"->return Constants.Subitem_TYPE.PASSAGEWAY_CABLE_HEAD_FABRICATION.ordinal
            "通道子项 电缆防火"->return Constants.Subitem_TYPE.PASSAGEWAY_CABLE_FIRE_PROTECTION.ordinal
            "通道子项 电缆试验"->return Constants.Subitem_TYPE.PASSAGEWEAY_CABLE_TEST.ordinal
            "通道子项 接地敷设"->return Constants.Subitem_TYPE.PASSAGEWAY_GROUND_LAYING.ordinal

            "通道 自查自检"->return Constants.Subitem_TYPE.PASSAGEWAY_SELF_EXAMINATION.ordinal
            "通道 监理验收"->return Constants.Subitem_TYPE.PASSAGEWAY_SUPERVISION_ACCEPTANCE.ordinal
            "通道 业主验收"->return Constants.Subitem_TYPE.PASSAGEWAY_OWNER_ACCEPTANCE.ordinal
            "通道 资料动态"->return Constants.Subitem_TYPE.PASSAGEWAY_DATA_DYNAMIC.ordinal
            else ->return Constants.Subitem_TYPE.OVERHEAD_MORE.ordinal
        }
    }
    /*
        get json file with context
    */
    lateinit var context:Context
    /*
    *   upload file with activity
    * */
    lateinit var activity:AppCompatActivity
    //需求个人
    fun DemandIndividual():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Demand/DemandIndividual.json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning()
            {
                if (adapter.VHList.size>=17)
                {
                    adapter.VHList[16].etInputUnitValue.hint = "1-90"
                }
            }
        }
        return adapter
    }
    //需求团队——变电施工
    fun DemandGroupSubstationConstruction():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Demand/DemandGroup(Substation Construction).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        val handler=Handler(Handler.Callback {
            when(it.what)
            {
                Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal->
                {
                    when(it.data.getString("selectContent"))
                    {
                        "上传附件"->
                        {
                            val intent=Intent(Intent.ACTION_GET_CONTENT)
                            intent.type="*/*"
                            intent.putExtra("key",mData[15].key.split(" ")[0])
                            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                        "下载模板"->
                        {
                            val chooser=StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
                            chooser.show()
                            chooser.setOnSelectListener { selectContent ->
                                downloadFile(selectContent,"清工薪资清册模板","清工薪资清册模板","")
                            }
                        }
                    }
                    false
                }
                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal->
                {
                    when(it.data.getString("selectContent"))
                    {
                        "上传附件"->
                        {
                            val intent=Intent(Intent.ACTION_GET_CONTENT)
                            intent.type="*/*"
                            intent.putExtra("key",mData[15].key.split(" ")[1])
                            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                        "下载模板"->
                        {
                            val chooser=StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
                            chooser.show()
                            chooser.setOnSelectListener { selectContent ->
                                downloadFile(selectContent,"清单报价清册模板","清单报价清册模板","")
                            }
                        }
                    }
                    false
                }
                else ->
                {
                    false
                }
            }
        })
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        val multiButtonListenersForSalary:MutableList<View.OnClickListener> =ArrayList()

        val clearanceSalary=View.OnClickListener {
            val content:List<String> = listOf("上传附件","下载模板")
            val customDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,context,content,handler)
            customDialog.msgWhat=Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal
            customDialog.dialog.show()
        }
        val listQuote=View.OnClickListener {
            val content:List<String> = listOf("上传附件","下载模板")
            val customDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,context,content,handler)
            customDialog.msgWhat=Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
            customDialog.dialog.show()
        }
        multiButtonListenersForSalary.add(clearanceSalary)//薪资标准
        multiButtonListenersForSalary.add(listQuote)


        val uploadProjectListListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        val cameraListener=View.OnClickListener {
            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        val downloadProgressListener=View.OnClickListener {
            val chooser= StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
            chooser.show()
            chooser.setOnSelectListener { selectContent ->
                downloadFile(selectContent,"工程清册模板","工程清册模板","")
            }
        }
        multiButtonListeners.add(downloadProgressListener)
        multiButtonListeners.add(cameraListener)//工程清册
        multiButtonListeners.add(uploadProjectListListener)
        mData[5].buttonListener=multiButtonListeners
        mData[15].buttonListener=multiButtonListenersForSalary
        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning()
            {
                if (adapter.VHList.size>=21)
                {
                    adapter.VHList[20].etInputUnitValue.hint = "1-90"
                }
            }
        }
        return adapter
    }
    //需求团队——测量设计
    fun DemandGroupMeasurementDesign():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Demand/DemandGroup(Measurement Design).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        val handler=Handler(Handler.Callback {
            when(it.what)
            {
                Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal->
                {
                    when(it.data.getString("selectContent"))
                    {
                        "上传附件"->
                        {
                            val intent=Intent(Intent.ACTION_GET_CONTENT)
                            intent.type="*/*"
                            intent.putExtra("key",mData[15].key.split(" ")[0])
                            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                        "下载模板"->
                        {
                            val chooser=StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
                            chooser.show()
                            chooser.setOnSelectListener { selectContent ->
                                downloadFile(selectContent,"清工薪资清册模板","清工薪资清册模板","")
                            }
                        }
                    }
                    false
                }
                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal->
                {
                    when(it.data.getString("selectContent"))
                    {
                        "上传附件"->
                        {
                            val intent=Intent(Intent.ACTION_GET_CONTENT)
                            intent.type="*/*"
                            intent.putExtra("key",mData[15].key.split(" ")[1])
                            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                        "下载模板"->
                        {
                            val chooser=StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
                            chooser.show()
                            chooser.setOnSelectListener { selectContent ->
                                downloadFile(selectContent,"清单报价清册模板","清单报价清册模板","")
                            }
                        }
                    }
                    false
                }
                else ->
                {
                    false
                }
            }
        })
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        val multiButtonListenersForSalary:MutableList<View.OnClickListener> =ArrayList()

        val clearanceSalary=View.OnClickListener {
            val content:List<String> = listOf("上传附件","下载模板")
            val customDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,context,content,handler)
            customDialog.msgWhat=Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal
            customDialog.dialog.show()
        }
        val listQuote=View.OnClickListener {
            val content:List<String> = listOf("上传附件","下载模板")
            val customDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,context,content,handler)
            customDialog.msgWhat=Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
            customDialog.dialog.show()
        }
        multiButtonListenersForSalary.add(clearanceSalary)//薪资标准
        multiButtonListenersForSalary.add(listQuote)


        val uploadProjectListListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        val cameraListener=View.OnClickListener {
            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        val downloadProgressListener=View.OnClickListener {
            val chooser= StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
            chooser.show()
            chooser.setOnSelectListener { selectContent ->
                downloadFile(selectContent,"工程清册模板","工程清册模板","")
            }
        }
        multiButtonListeners.add(downloadProgressListener)
        multiButtonListeners.add(cameraListener)//工程清册
        multiButtonListeners.add(uploadProjectListListener)
        mData[5].buttonListener=multiButtonListeners
        mData[15].buttonListener=multiButtonListenersForSalary

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning()
            {
                if (adapter.VHList.size>=21)
                {
                    adapter.VHList[20].etInputUnitValue.hint = "1-90"
                }
            }
        }
        return adapter
    }
    //需求团队——马帮运输
    fun DemandGroupCaravanTransportation():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Demand/DemandGroup(Caravan transportation).json")
        val multiButtonListenerList:MutableList<View.OnClickListener> =ArrayList()
        val handler=Handler(Handler.Callback {
            when(it.what)
            {
                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal->
                {
                    when(it.data.getString("selectContent"))
                    {
                        "上传附件"->
                        {
                            val intent=Intent(Intent.ACTION_GET_CONTENT)
                            intent.type="*/*"
                            intent.putExtra("key",mData[14].key.split(" ")[1])
                            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                        "下载模板"->
                        {
                            val chooser=StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
                            chooser.show()
                            chooser.setOnSelectListener { selectContent ->
                                downloadFile(selectContent,"清单报价清册模板","清单报价清册模板","")
                            }
                        }
                    }
                    false
                }
                else ->
                {
                    false
                }
            }
        })
        multiButtonListenerList.add(View.OnClickListener {
            Toast.makeText(context,"模式:面议",Toast.LENGTH_SHORT).show()
        })
        multiButtonListenerList.add(View.OnClickListener {
            Toast.makeText(context,"模式:清单报价",Toast.LENGTH_SHORT).show()
            val content:List<String> = listOf("上传附件","下载模板")
            val customDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,context,content,handler)
            customDialog.msgWhat=Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
            customDialog.dialog.show()
        })
        mData[14].buttonListener=multiButtonListenerList
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        val uploadProjectListListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        val cameraListener=View.OnClickListener {
            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        val downloadProgressListener=View.OnClickListener {
            val chooser= StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
            chooser.show()
            chooser.setOnSelectListener { selectContent ->
                downloadFile(selectContent,"工程清册模板","工程清册模板","")
            }
        }
        multiButtonListeners.add(downloadProgressListener)
        multiButtonListeners.add(cameraListener)//工程清册
        multiButtonListeners.add(uploadProjectListListener)
        mData[5].buttonListener=multiButtonListeners
        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning()
            {
                if (adapter.VHList.size>=18)
                {
                    adapter.VHList[17].etInputUnitValue.hint = "1-90"
                }
            }
        }
        return adapter
    }
    //需求团队——桩基施工
    fun DemandGroupPileFoundationConstruction():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        val mData=itemGenerate.getJsonFromAsset("Demand/DemandGroup(Pile foundation construction).json")
        val multiButtonListenerList:MutableList<View.OnClickListener> =ArrayList()
        val handler=Handler(Handler.Callback {
            when(it.what)
            {
                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal->
                {
                    when(it.data.getString("selectContent"))
                    {
                        "上传附件"->
                        {
                            val intent=Intent(Intent.ACTION_GET_CONTENT)
                            intent.type="*/*"
                            intent.putExtra("key",mData[16].key.split(" ")[1])
                            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                        "下载模板"->
                        {
                            val chooser=StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
                            chooser.show()
                            chooser.setOnSelectListener { selectContent ->
                                downloadFile(selectContent,"清单报价清册模板","清单报价清册模板","")
                            }
                        }
                    }
                    false
                }
                else ->
                {
                    false
                }
            }
        })
        multiButtonListenerList.add(View.OnClickListener {
            Toast.makeText(context,"模式:面议",Toast.LENGTH_SHORT).show()
        })
        multiButtonListenerList.add(View.OnClickListener {
            Toast.makeText(context,"模式:清单报价",Toast.LENGTH_SHORT).show()
            val content:List<String> = listOf("上传附件","下载模板")
            val customDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,context,content,handler)
            customDialog.msgWhat=Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
            customDialog.dialog.show()
        })
        mData[16].buttonListener=multiButtonListenerList
        val uploadProjectListListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        val cameraListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_PICK)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        val downloadProgressListener=View.OnClickListener {
            val chooser= StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
            chooser.show()
            chooser.setOnSelectListener { selectContent ->
                downloadFile(selectContent,"工程清册模板","工程清册模板","")
            }
        }
        multiButtonListeners.add(downloadProgressListener)
        multiButtonListeners.add(cameraListener)
        multiButtonListeners.add(uploadProjectListListener)//工程清册



        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        mData[5].buttonListener=multiButtonListeners

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if (adapter.VHList.size>=21)
                {
                    adapter.VHList[20].etInputUnitValue.hint = "1-90"
                }
            }
        }
        return adapter
    }
    //需求团队——非开挖
    fun DemandGroupNonExcavation():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()

        val mData=itemGenerate.getJsonFromAsset("Demand/DemandGroup(Non-excavation).json")
        val multiButtonListenerList:MutableList<View.OnClickListener> =ArrayList()
        val handler=Handler(Handler.Callback {
            when(it.what)
            {
                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal->
                {
                    when(it.data.getString("selectContent"))
                    {
                        "上传附件"->
                        {
                            val intent=Intent(Intent.ACTION_GET_CONTENT)
                            intent.type="*/*"
                            intent.putExtra("key",mData[15].key.split(" ")[1])
                            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                        "下载模板"->
                        {
                            val chooser=StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
                            chooser.show()
                            chooser.setOnSelectListener { selectContent ->
                                downloadFile(selectContent,"清单报价清册模板","清单报价清册模板","")
                            }
                        }
                    }
                    false
                }
                else ->
                {
                    false
                }
            }
        })
        multiButtonListenerList.add(View.OnClickListener {
            Toast.makeText(context,"模式:面议",Toast.LENGTH_SHORT).show()
        })
        multiButtonListenerList.add(View.OnClickListener {
            Toast.makeText(context,"模式:清单报价",Toast.LENGTH_SHORT).show()
            val content:List<String> = listOf("上传附件","下载模板")
            val customDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,context,content,handler)
            customDialog.msgWhat=Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
            customDialog.dialog.show()
        })
        mData[15].buttonListener=multiButtonListenerList
        val uploadProjectListListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        val cameraListener=View.OnClickListener {
            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        val downloadProgressListener=View.OnClickListener {
            val chooser= StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
            chooser.show()
            chooser.setOnSelectListener { selectContent ->
                downloadFile(selectContent,"工程清册模板","工程清册模板","")
            }
        }
        multiButtonListeners.add(downloadProgressListener)
        multiButtonListeners.add(cameraListener)
        multiButtonListeners.add(uploadProjectListListener)//工程清册
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        mData[5].buttonListener=multiButtonListeners

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if (adapter.VHList.size>=21)
                {
                    adapter.VHList[20].etInputUnitValue.hint = "1-90"
                }
            }
        }
        return adapter
    }
    //需求团队——试验调试
    fun DemandGroupTestDebugging():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Demand/DemandGroup(Test debugging).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        val handler=Handler(Handler.Callback {
            when(it.what)
            {
                Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal->
                {
                    when(it.data.getString("selectContent"))
                    {
                        "上传附件"->
                        {
                            val intent=Intent(Intent.ACTION_GET_CONTENT)
                            intent.type="*/*"
                            intent.putExtra("key",mData[15].key.split(" ")[0])
                            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                        "下载模板"->
                        {
                            val chooser=StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
                            chooser.show()
                            chooser.setOnSelectListener { selectContent ->
                                downloadFile(selectContent,"清工薪资清册模板","清工薪资清册模板","")
                            }
                        }
                    }
                    false
                }
                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal->
                {
                    when(it.data.getString("selectContent"))
                    {
                        "上传附件"->
                        {
                            val intent=Intent(Intent.ACTION_GET_CONTENT)
                            intent.type="*/*"
                            intent.putExtra("key",mData[15].key.split(" ")[1])
                            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                        "下载模板"->
                        {
                            val chooser=StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
                            chooser.show()
                            chooser.setOnSelectListener { selectContent ->
                                downloadFile(selectContent,"清单报价清册模板","清单报价清册模板","")
                            }
                        }
                    }
                    false
                }
                else ->
                {
                    false
                }
            }
        })
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        val multiButtonListenersForSalary:MutableList<View.OnClickListener> =ArrayList()
        val clearanceSalary=View.OnClickListener {
            val content:List<String> = listOf("上传附件","下载模板")
            val customDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,context,content,handler)
            customDialog.msgWhat=Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal
            customDialog.dialog.show()
        }
        val listQuote=View.OnClickListener {
            val content:List<String> = listOf("上传附件","下载模板")
            val customDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,context,content,handler)
            customDialog.msgWhat=Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
            customDialog.dialog.show()
        }
        multiButtonListenersForSalary.add(clearanceSalary)//薪资标准
        multiButtonListenersForSalary.add(listQuote)
        val uploadProjectListListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        val cameraListener=View.OnClickListener {
            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        val downloadProgressListener=View.OnClickListener {
            val chooser= StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
            chooser.show()
            chooser.setOnSelectListener { selectContent ->
                downloadFile(selectContent,"工程清册模板","工程清册模板","")
            }
        }
        multiButtonListeners.add(downloadProgressListener)
        multiButtonListeners.add(cameraListener)
        multiButtonListeners.add(uploadProjectListListener)//工程清册
        mData[5].buttonListener=multiButtonListeners
        mData[15].buttonListener=multiButtonListenersForSalary

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if (adapter.VHList.size>=21)
                {
                    adapter.VHList[20].etInputUnitValue.hint = "1-90"
                }
            }
        }
        return adapter
    }
    //需求团队——跨越架
    fun DemandGroupCrossingFrame():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Demand/DemandGroup(Crossing frame).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        val handler=Handler(Handler.Callback {
            when(it.what)
            {
                Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal->
                {
                    when(it.data.getString("selectContent"))
                    {
                        "上传附件"->
                        {
                            val intent=Intent(Intent.ACTION_GET_CONTENT)
                            intent.type="*/*"
                            intent.putExtra("key",mData[13].key.split(" ")[0])
                            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                        "下载模板"->
                        {
                            val chooser=StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
                            chooser.show()
                            chooser.setOnSelectListener { selectContent ->
                                downloadFile(selectContent,"清工薪资清册模板","清工薪资清册模板","")
                            }
                        }
                    }
                    false
                }
                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal->
                {
                    when(it.data.getString("selectContent"))
                    {
                        "上传附件"->
                        {
                            val intent=Intent(Intent.ACTION_GET_CONTENT)
                            intent.type="*/*"
                            intent.putExtra("key",mData[13].key.split(" ")[1])
                            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                        "下载模板"->
                        {
                            val chooser=StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
                            chooser.show()
                            chooser.setOnSelectListener { selectContent ->
                                downloadFile(selectContent,"清单报价清册模板","清单报价清册模板","")
                            }
                        }
                    }
                    false
                }
                else ->
                {
                    false
                }
            }
        })
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList<View.OnClickListener>()
        val multiButtonListenersForSalary:MutableList<View.OnClickListener> =ArrayList()
        val clearanceSalary=View.OnClickListener {
            val content:List<String> = listOf("上传附件","下载模板")
            val customDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,context,content,handler)
            customDialog.msgWhat=Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal
            customDialog.dialog.show()
        }
        val listQuote=View.OnClickListener {
            val content:List<String> = listOf("上传附件","下载模板")
            val customDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,context,content,handler)
            customDialog.msgWhat=Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
            customDialog.dialog.show()
        }
        multiButtonListenersForSalary.add(clearanceSalary)//薪资标准
        multiButtonListenersForSalary.add(listQuote)


        val uploadProjectListListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        val cameraListener=View.OnClickListener {
            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        val downloadProgressListener=View.OnClickListener {
            val chooser= StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
            chooser.show()
            chooser.setOnSelectListener { selectContent ->
                downloadFile(selectContent,"工程清册模板","工程清册模板","")
            }
        }
        multiButtonListeners.add(downloadProgressListener)
        multiButtonListeners.add(cameraListener)
        multiButtonListeners.add(uploadProjectListListener)//工程量清册
        mData[5].buttonListener=multiButtonListeners
        mData[13].buttonListener=multiButtonListenersForSalary

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if (adapter.VHList.size>=19)
                {
                    adapter.VHList[18].etInputUnitValue.hint = "1-90"
                }
            }
        }
        return adapter
    }
    //需求团队——运行维护
    fun DemandGroupOperationAndMaintenance():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Demand/DemandGroup(Operation and Maintenance).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        val handler= Handler(Handler.Callback {
            when(it.what)
            {
                Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal->
                {
                    when(it.data.getString("selectContent"))
                    {
                        "上传附件"->
                        {
                            val intent=Intent(Intent.ACTION_GET_CONTENT)
                            intent.type="*/*"
                            intent.putExtra("key",mData[12].key.split(" ")[0])
                            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                        "下载模板"->
                        {
                            val chooser=StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
                            chooser.show()
                            chooser.setOnSelectListener { selectContent ->
                                downloadFile(selectContent,"清工薪资清册模板","清工薪资清册模板","")
                            }
                        }
                    }
                    false
                }
                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal->
                {
                    when(it.data.getString("selectContent"))
                    {
                        "上传附件"->
                        {
                            val intent=Intent(Intent.ACTION_GET_CONTENT)
                            intent.type="*/*"
                            intent.putExtra("key",mData[12].key.split(" ")[1])
                            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                        "下载模板"->
                        {
                            val chooser= StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
                            chooser.show()
                            chooser.setOnSelectListener { selectContent ->
                                downloadFile(selectContent,"清单报价清册模板","清单报价清册模板","")
                            }
                        }
                    }
                    false
                }
                else ->
                {
                    false
                }
            }
        })
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        val multiButtonListenersForSalary:MutableList<View.OnClickListener> =ArrayList()
        val clearanceSalary=View.OnClickListener {
            val content:List<String> = listOf("上传附件","下载模板")
            val customDialog= CustomDialog(CustomDialog.Options.SELECT_DIALOG,context,content,handler)
            customDialog.msgWhat=Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal
            customDialog.dialog.show()
        }
        val listQuote=View.OnClickListener {
            val content:List<String> = listOf("上传附件","下载模板")
            val customDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,context,content,handler)
            customDialog.msgWhat=Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
            customDialog.dialog.show()
        }
        multiButtonListenersForSalary.add(clearanceSalary)//薪资标准
        multiButtonListenersForSalary.add(listQuote)


        val downloadProgressListener=View.OnClickListener {
            val chooser= StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
            chooser.show()
            chooser.setOnSelectListener { selectContent ->
                downloadFile(selectContent,"工程清册模板","工程清册模板","")
            }
        }
        val uploadProjectListListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        val cameraListener=View.OnClickListener {
            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        //工程清册
        multiButtonListeners.add(downloadProgressListener)

        multiButtonListeners.add(cameraListener)
        multiButtonListeners.add(uploadProjectListListener)
        mData[5].buttonListener=multiButtonListeners
        mData[12].buttonListener=multiButtonListenersForSalary

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if (adapter.VHList.size>=18)
                {
                    adapter.VHList[17].etInputUnitValue.hint = "1-90"
                }
            }
        }
        return adapter
    }
    //需求租赁——车辆租赁
    fun DemandLeaseVehicleLeasing():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Demand/DemandLease(Vehicle Leasing).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if(adapter.VHList.size>=22){
                    adapter.VHList[21].etInputUnitValue.hint="1-90"
                }
            }
        }
        return adapter
    }
    //需求租赁——工器具租赁
    fun DemandEquipmentLeasing():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Demand/DemandLease(Equipment Leasing).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()

        val downloadProgressListener=View.OnClickListener {
            val chooser= StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
            chooser.show()
            chooser.setOnSelectListener { selectContent ->
                downloadFile(selectContent,"需求租赁清单","需求租赁清单","")
            }
        }
        val uploadProjectListListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        val cameraListener=View.OnClickListener {
            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra("key",mData[5].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        multiButtonListeners.add(downloadProgressListener)
        multiButtonListeners.add(cameraListener)//拍照
        multiButtonListeners.add(uploadProjectListListener)//上传附件

        mData[5].buttonListener=multiButtonListeners
        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning()
            {
                if (adapter.VHList.size>=13)
                {
                    adapter.VHList[12].etInputUnitValue.hint = "1-90"
                }
            }
        }
        return adapter
    }
    //需求三方
    fun DemandTripartite():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Demand/DemandTripartite.json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        val downloadProgressListener=View.OnClickListener {
            val chooser= StorageChooser.Builder().withActivity(activity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager).withMemoryBar(true).build()
            chooser.show()
            chooser.setOnSelectListener { selectContent ->
                downloadFile(selectContent,"三方服务清单模板","三方服务清单模板","")
            }
        }
        val uploadProjectListListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            //intent.putExtra("key",mData[4].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        val cameraListener=View.OnClickListener {
            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //intent.putExtra("key",mData[4].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        multiButtonListeners.add(downloadProgressListener)
        multiButtonListeners.add(cameraListener)//拍照
        multiButtonListeners.add(uploadProjectListListener)//上传附件
        mData[2].buttonListener=multiButtonListeners

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object :ObserverFactory.RecyclerviewAdapterObserver{

            override fun onBindComplete() {

            }
            override fun onBindRunning() {
                if(adapter.VHList.size>=8){
                    adapter.VHList[7].etInputUnitValue.hint="1-90"
                }
            }
        }
        return adapter
    }

    /*
    *供应模块
    */
    //个人劳务
    fun PersonalService():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Provider/Personal service.json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        mData[9].jumpListener = View.OnClickListener {
            (activity as SupplyActivity).switchFragment(UploadPhoneFragment(),R.id.frame_supply,"Capture")
        }

        val adapter = RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if(adapter.VHList.size>=11){
                    adapter.VHList[10].etInputUnitValue.hint="1-90"
                }
            }
        }
        return adapter
    }


    //团队服务——变电施工
    fun ProviderGroupSubstationConstruction():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context  //
        val mData=itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Substation Construction).json")
        //上传
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        for(j in 3 until 8){
            if(j in arrayListOf(5,6))continue
            val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
            multiButtonListeners.add(View.OnClickListener {
                val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
                    .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
                    .withMemoryBar(true).build()
                chooser.show()
                chooser.setOnSelectListener { selectContent ->
                    downloadFile(selectContent, mData[j].buttonTitle+"模板", mData[j].buttonTitle+"模板", "")
                }
            })
            multiButtonListeners.add(View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                intent.putExtra("key", mData[j].key)
                activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
            })
            mData[j].buttonListener = multiButtonListeners
        }
        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }

            override fun onBindRunning() {
                if(adapter.VHList.size==9){
                    adapter.VHList[8].etInputUnitValue.hint="1-90"
                }
            }
        }
        return adapter

    }
    //团队服务——测量设计
    fun ProviderGroupMeasurementDesign():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Measurement Design).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        for(j in 3 until 8){
            if(j in arrayListOf(5,6))continue
            val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
            multiButtonListeners.add(View.OnClickListener {
                val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
                    .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
                    .withMemoryBar(true).build()
                chooser.show()
                chooser.setOnSelectListener { selectContent ->
                    downloadFile(selectContent, mData[j].buttonTitle+"模板", mData[j].buttonTitle+"模板", "")
                }
            })
            multiButtonListeners.add(View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                intent.putExtra("key", mData[j].key)
                activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
            })
            mData[j].buttonListener = multiButtonListeners
        }
        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }

            override fun onBindRunning() {
                if(adapter.VHList.size==9){
                    adapter.VHList[8].etInputUnitValue.hint="1-90"
                }
            }
        }
        return adapter
    }

    //团队服务——马帮运输
    fun ProviderGroupCaravanTransportation():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Caravan transportation).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        multiButtonListeners.add(View.OnClickListener {
            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
                .withMemoryBar(true).build()
            chooser.show()
            chooser.setOnSelectListener { selectContent ->
                downloadFile(selectContent, mData[3].buttonTitle+"模板", mData[3].buttonTitle+"模板", "")
            }
        })
        multiButtonListeners.add(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key", mData[3].key)
            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        })
        mData[3].buttonListener = multiButtonListeners

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }

            override fun onBindRunning() {
                if(adapter.VHList.size==6){
                    adapter.VHList[5].etInputUnitValue.hint="1-90"
                }
            }
        }
        return adapter
    }


    //团队服务——桩机服务
    fun ProviderGroupPileFoundationConstruction():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Pile foundation construction).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        for(j in 3 until 9){
            if(j in arrayListOf(5,6,7))continue
            val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
            multiButtonListeners.add(View.OnClickListener {
                val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
                    .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
                    .withMemoryBar(true).build()
                chooser.show()
                chooser.setOnSelectListener { selectContent ->
                    downloadFile(selectContent, mData[j].buttonTitle+"模板", mData[j].buttonTitle+"模板", "")
                }
            })
            multiButtonListeners.add(View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                intent.putExtra("key", mData[j].key)
                activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
            })
            mData[j].buttonListener = multiButtonListeners
        }

        val adapter = RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }

            override fun onBindRunning() {
                if(adapter.VHList.size==10){
                    adapter.VHList[9].etInputUnitValue.hint="1-90"
                }
            }
        }
        return adapter
    }

    //团队服务——非开挖
    fun ProviderGroupNonExcavation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Non-excavation).json")
        mData[0].backListener = View.OnClickListener {
            activity.finish()
        }
        for(j in 3 until 6){
            val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
            multiButtonListeners.add(View.OnClickListener {
                val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
                    .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
                    .withMemoryBar(true).build()
                chooser.show()
                chooser.setOnSelectListener { selectContent ->
                    downloadFile(selectContent, mData[j].buttonTitle+"模板", mData[j].buttonTitle+"模板", "")
                }
            })
            multiButtonListeners.add(View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                intent.putExtra("key", mData[j].key)
                activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
            })
            mData[j].buttonListener = multiButtonListeners
        }

        val adapter = RecyclerviewAdapter(mData)
        adapter.adapterObserver = object : ObserverFactory.RecyclerviewAdapterObserver {
            override fun onBindComplete() {
            }

            override fun onBindRunning() {
                if (adapter.VHList.size == 7) {
                    adapter.VHList[6].etInputUnitValue.hint = "1-90"
                }
            }
        }
        return adapter
    }
    //团队服务——试验调试
    fun ProviderGroupTestDebugging(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        lateinit var adapter: RecyclerviewAdapter
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProvicerGroup(Test debugging).json")
        mData[0].backListener = View.OnClickListener {
            activity.finish()
        }
        for(j in 3 until 9){
            if(j in arrayListOf(5,6,7)) continue
            val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
            multiButtonListeners.add(View.OnClickListener {
                val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
                    .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
                    .withMemoryBar(true).build()
                chooser.show()
                chooser.setOnSelectListener { selectContent ->
                    downloadFile(selectContent, mData[j].buttonTitle+"模板", mData[j].buttonTitle+"模板", "")
                }
            })
            multiButtonListeners.add(View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                intent.putExtra("key", mData[j].key)
                activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
            })
            mData[j].buttonListener = multiButtonListeners
        }
        adapter = RecyclerviewAdapter(mData)
        adapter.adapterObserver = object : ObserverFactory.RecyclerviewAdapterObserver {
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if (adapter.VHList.size == 10) {
                    adapter.VHList[9].etInputUnitValue.hint = "1-90"
                }
            }
        }
        return adapter
    }
    //团队服务——跨越架
    fun ProviderGroupCrossingFrame():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        lateinit var adapter:RecyclerviewAdapter
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Provider/TeamService/ProvicerGroup(Crossing frame).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        for(j in 3 until 6){
            val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
            multiButtonListeners.add(View.OnClickListener {
                val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
                    .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
                    .withMemoryBar(true).build()
                chooser.show()
                chooser.setOnSelectListener { selectContent ->
                    downloadFile(selectContent, mData[j].buttonTitle+"模板", mData[j].buttonTitle+"模板", "")
                }
            })
            multiButtonListeners.add(View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                intent.putExtra("key", mData[j].key)
                activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
            })
            mData[j].buttonListener = multiButtonListeners
        }
        adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if(adapter.VHList.size==7){
                    adapter.VHList[6].etInputUnitValue.hint="1-90"
                }
            }
        }
        return adapter
    }
    //团队服务——运行维护
    fun OperationAndMaintenance():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Operation and Maintenance).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        for(j in 3 until 7){
            if(j==5)
                continue
            val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
            multiButtonListeners.add(View.OnClickListener {
                val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
                    .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
                    .withMemoryBar(true).build()
                chooser.show()
                chooser.setOnSelectListener { selectContent ->
                    downloadFile(selectContent, mData[j].buttonTitle+"模板", mData[j].buttonTitle+"模板", "")
                }
            })
            multiButtonListeners.add(View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                intent.putExtra("key", mData[j].key)
                activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
            })
            mData[j].buttonListener = multiButtonListeners
        }
        val adapter = RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if(adapter.VHList.size==8){
                    adapter.VHList[7].etInputUnitValue.hint="1-90"
                }
            }
        }
        return adapter
    }
    //租赁服务——车辆租赁
    fun VehicleRental():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val multiButtonListeners:MutableList<View.OnClickListener> = ArrayList()
        val mData = itemGenerate.getJsonFromAsset("Provider/RentalService/VehicleRental/Information entry.json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        mData[14].jumpListener=View.OnClickListener {
            (activity as SupplyActivity).switchFragment(UploadPhoneFragment(),R.id.frame_supply,"Capture")

        }
        val adapter = RecyclerviewAdapter(mData)
        adapter.adapterObserver=object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {

            }
            override fun onBindRunning() {
                if(adapter.VHList.size==16){
                    adapter.VHList[15].etInputUnitValue.hint="1-90"
                }
            }
        }
        return adapter
    }
    //租赁服务——工器具租赁
    fun EquipmentLeasing():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Provider/RentalService/Equipment Leasing/Information entry.json")
        val uploadContractButtonListListeners:MutableList<View.OnClickListener> = ArrayList()
        val uploadInventoryButtonListListeners:MutableList<View.OnClickListener> = ArrayList()
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        val downloadContractListener = View.OnClickListener {
            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
                .withMemoryBar(true).build()
            chooser.show()
            chooser.setOnSelectListener { selectContent ->
                downloadFile(selectContent, "工器具出租模板", "工器具出租模板", "")
            }
        }
        val uploadContractListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type="*/*"
            //intent.putExtra("key",mData[9].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        val uploadInventoryListener = View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type="*/*"
            //intent.putExtra("key",mData[8].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        //租赁合同
        uploadInventoryButtonListListeners.add(uploadInventoryListener)
        //
        uploadContractButtonListListeners.add(downloadContractListener)
        uploadContractButtonListListeners.add(uploadContractListener)

        mData[7].jumpListener = View.OnClickListener {
            (activity as SupplyActivity).switchFragment(UploadPhoneFragment(),R.id.frame_supply,"Capture")
        }
        mData[8].buttonListener= uploadInventoryButtonListListeners
        mData[9].buttonListener=uploadContractButtonListListeners

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if(adapter.VHList.size==13){
                    adapter.VHList[12].etInputUnitValue.hint="1-90"
                }
            }
        }
        return adapter
    }
    //三方服务
    fun ServiceInformationEntry():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val jumpListener = View.OnClickListener {
            (activity as SupplyActivity).switchFragment(UploadPhoneFragment(),R.id.frame_supply,"Capture")
        }
        val mData=itemGenerate.getJsonFromAsset("Provider/TripartiteServices/Information entry.json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        mData[7].jumpListener=jumpListener
        mData[10].jumpListener=jumpListener
        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if(adapter.VHList.size==12){
                    adapter.VHList[11].etInputUnitValue.hint="1-90"
                }
            }
        }
        return adapter
    }
    fun ProviderGroupPersonAdd():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        lateinit var adapter:RecyclerviewAdapter
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Provider/ProviderGroupPersonAdd.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        mData[8].jumpListener=View.OnClickListener {
            (activity as SupplyActivity).switchFragment(UploadPhoneFragment(),R.id.frame_supply,"Capture")
        }
        adapter=RecyclerviewAdapter(mData)
        return adapter
    }
    fun ProviderGroupVehicleAdd():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        lateinit var adapter: RecyclerviewAdapter
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Provider/ProviderGroupVehicleAdd.json")
        activity.supportFragmentManager.popBackStackImmediate()
        mData[10].jumpListener=View.OnClickListener {
            (activity as SupplyActivity).switchFragment(UploadPhoneFragment(),R.id.frame_supply,"Capture")
        }
        adapter=RecyclerviewAdapter(mData)
        return adapter
    }

    /*
    *   专业模块
    */

    // alterPosition time:2019/7/15
    // function:/* 架空更多 */
    fun Professional_OverheadMore():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Professional/OverheadMore.json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        mData[0].tvSelect =View.OnClickListener {
            val option1Items= listOf("架空","节点","通道")
            val option2Items= listOf(listOf("经济指标","材料清册","杆塔参数","耐张段","公共点定位","自查自检","监理验收","业主验收","资料动态"), listOf("经济指标","材料清册","节点参数","自查自检","监理验收","业主验收","资料动态"), listOf("经济指标","材料清册","通道参数","自查自检","监理验收","业主验收","资料动态"))
            val handler=Handler(Handler.Callback {
                when(it.what) {
                        RecyclerviewAdapter.MESSAGE_SELECT_OK -> {
                            val data = Bundle()
                            val adapterGenerate = AdapterGenerate()
                            val type = adapterGenerate.getType(it.data.getString("selectContent"))
                            data.putInt("type",type)
                            (activity as ProfessionalActivity).switchFragment(ProjectMoreFragment.newInstance(data))
                            false
                        }
                        else -> {
                            false
                        }
                    }
                })

            val selectDialog = CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,context,handler,option1Items,option2Items).multiDialog
            selectDialog.dialog.show()
        }

        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }
    /*
     * 架空
     */
    //经济指标
    fun OverHeadEconomicIndicator():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/economicIndicator.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        //土质比例
        mData[9].fiveDisplayListener=View.OnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setNegativeButton("返回",null)
            builder.setCancelable(true)

            val view = View.inflate(context, R.layout.dialog_soil_ratio, null)

            view.rv_soil_ratio_content.adapter = OverHeadSoilRatio()
            view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
        }
        //地形比例
        mData[10].fiveDisplayListener=View.OnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setNegativeButton("返回",null)
            builder.setCancelable(true)

            val view = View.inflate(context, R.layout.dialog_soil_ratio, null)

            view.rv_soil_ratio_content.adapter = OverHeadTerrainRatio()
            view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
        }
        //跨越
        mData[11].fiveDisplayListener=View.OnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setNegativeButton("返回",null)
            builder.setCancelable(true)

            val view = View.inflate(context, R.layout.dialog_soil_ratio, null)

            view.rv_soil_ratio_content.adapter = OverHeadCross ()
            view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
        }
        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }
    //土质比例
    fun OverHeadSoilRatio():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/soilRatio.json")
        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }
    //地形比例
    fun OverHeadTerrainRatio():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        var mData = itemGenerate.getJsonFromAsset("Professional/Public/terrainRatio.json")

        //项目属性为 主网 才有
        val data = mData.toMutableList()
        for(j in 0 until mData.size){
            if(mData[j].twoDisplayTitle in listOf("沙漠","河流"))
                data.removeAt(j)
        }
        mData = data.toList()

        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }
    //跨越
    fun OverHeadCross():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Public/cross.json")
        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }
    //材料清册

    //耐张段
    fun OverHeadTensileSegment():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/tensileSegment/tensileSegment.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }

    //杆塔参数
    fun OverHeadTowerParameters():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/towerParameters.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 2 until mData.size){
            mData[j].fiveDisplayListener=View.OnClickListener {
                val data = Bundle()
                data.putInt("type",AdapterGenerate().getType("杆塔子项"))
                (activity as ProfessionalActivity).switchFragment(ProjectMoreFragment.newInstance(data))
            }
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //公共点定位
    fun OverHeadPublicPointPosition():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/PublicPointPosition.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //杆塔子项
    fun OverHeadTowerSubitem():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/towerSubitem.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 4 until mData.size){
            mData[j].jumpListener=View.OnClickListener {
                val data = Bundle()
                data.putInt("type",AdapterGenerate().getType(mData[0].title1+" "+mData[j].shiftInputTitle))
                (activity as ProfessionalActivity).switchFragment(ProjectMoreFragment.newInstance(data))
            }
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    /*
     *杆塔子项--子项
     */

    //复测分坑
    fun OverHeadResurveyPitdividing():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/resurveyPitdividing.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //基础开挖
    fun OverHeadFoundationExcavation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/foundationExcavation/foundationExcavation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //材料运输
    fun OverHeadMaterialTransportation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/towerTransportation/towerTransportation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //预制件填埋
    fun OverHeadPrefabricatedLandfill():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/prefabricatedLandfill/prefabricatedLandfill.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //基础浇筑
    fun OverHeadFoundationPouring():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/foundationPouring/foundationPouring.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //杆塔组立
    fun OverHeadTowerErection():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/towerErection/towerErection.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //拉线制作
    fun OverHeadWireDrawing():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/wireDrawing/wireDrawing.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //横担安装
    fun OverHeadCrossbarInstallation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/crossbarInstallation/crossbarInstallation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //绝缘子安装
    fun OverHeadInsulationInstallation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/insulatorInstallation/insulatorInstallation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //焊接制作
    fun OverHeadWeldingFabrication():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/weldingFabrication/weldingFabrication.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //导线架设
    fun OverHeadWireErection():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/wireErection/wireErection.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //附件安装
    fun OverHeadAccessoriesInstalling():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/accessoriesInstalling/accessoriesInstalling.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //设备安装
    fun OverHeadEquipmentInstallation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/equipmentInstallation/equipmentInstallation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //接地敷设
    fun OverHeadGroundLaying():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/groundLaying/groundLaying.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //户表安装
    fun OverHeadTableInstallation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/tableInstallation/tableInstallation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //带电作业
    fun OverHeadLiveWork():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/liveWork/liveWork.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //项目盘
    fun ProfessionalProjectDisk(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/ProjectDisk.json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        mData[0].tvSelect = View.OnClickListener {
            XPopup.Builder(context)
                .atView(it)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                .asAttachList(
                    arrayOf("新建项目盘", "动态汇总表", "项目清册"),
                    null
                )
                { position, text ->
                    run {
                        val data = Bundle()
                        data.putInt("type", AdapterGenerate().getType(text))
                        val intent = Intent(activity, ProfessionalActivity::class.java)
                        intent.putExtra("title", text)
                        activity.startActivity(intent)
                    }

                }.show()
        }
        for (j in 2 until mData.size) {
            mData[j].tvExpandTitleListener = View.OnClickListener {
                val intent = Intent(activity, ProfessionalActivity::class.java)
                intent.putExtra("title", mData[j].expandTitle)
                activity.startActivity(intent)
            }
            mData[j].tvExpandMoreListener = View.OnClickListener {
                val option1Items = listOf("删除", "更多", "详情")
                val handler = Handler(Handler.Callback {
                    when (it.what) {
                        RecyclerviewAdapter.MESSAGE_SELECT_OK -> {
//                            val data = Bundle()
//                            val adapterGenerate = AdapterGenerate()
//                            val type = adapterGenerate.getType(it.data.getString("selectContent"))
//                            data.putInt("type", type)
//                            (activity as ProjectDiskActivity).switchFragment(ProjectMoreFragment.newInstance(data))
                            false
                        }
                        else -> {
                            false
                        }
                    }
                })
                val selectDialog =
                    CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, option1Items, handler).dialog
                selectDialog.show()
            }
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    /*
     * 节点
     */
    //经济指标
    fun NodeEconomicIndicator():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/economicIndicator.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }
    //节点参数
    fun NodeParameters():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/nodeParameters.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for(j in 7 until mData.size){
            mData[j].fourDisplayListener=View.OnClickListener {
                val data = Bundle()
                data.putInt("type", AdapterGenerate().getType("节点子项"))
                (activity as ProfessionalActivity).switchFragment(ProjectMoreFragment.newInstance(data))
            }
        }
        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }

    //节点子项
    fun NodeSubitems():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/nodeSubitems.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 4 until mData.size){
            mData[j].jumpListener=View.OnClickListener {
                val data = Bundle()
                data.putInt("type",AdapterGenerate().getType(mData[0].title1+" "+mData[j].shiftInputTitle))
                (activity as ProfessionalActivity).switchFragment(ProjectMoreFragment.newInstance(data))
            }
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    /*
     *节点子项--子项
     */

    //复测分坑
    fun NodeResurveyPitdividing():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/resurveyPitdividing.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //基础开挖
    fun NodeFoundationExcavation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/foundationExcavation/foundationExcavation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //材料运输
    fun NodeMaterialTransportation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/materialTransportation/materialTransportation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //预制构件制作及安装
    fun NodeManufactureInstallation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/manufactureInstallation/manufactureInstallation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //基础浇筑
    fun NodeFoundationPouring():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/foundationPouring/foundationPouring.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //设备安装
    fun NodeEquipmentInstallation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/equipmentInstallation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //接地敷设
    fun NodeGroundLaying():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/groundingLaying/groundingLaying.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //户表安装
    fun NodeTableInstallation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/tableInstallation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    /*
     * 通道
     */
    //经济指标
    fun PassagewayEconomicIndicator():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/economicIndicator.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }
    //通道参数
    fun ChannelParameters():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/channelParameters.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 7 until mData.size){
            mData[j].fiveDisplayListener=View.OnClickListener {
                val data = Bundle()
                data.putInt("type",AdapterGenerate().getType("通道子项"))
                (activity as ProfessionalActivity).switchFragment(ProjectMoreFragment.newInstance(data))
            }
        }
        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }
    //通道子项
    fun ChannelSubitems():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/channelSubitems.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 4 until mData.size){
            mData[j].jumpListener=View.OnClickListener {
                val data = Bundle()
                data.putInt("type",AdapterGenerate().getType(mData[0].title1+" "+mData[j].shiftInputTitle))
                (activity as ProfessionalActivity).switchFragment(ProjectMoreFragment.newInstance(data))
            }
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    /*
     *通道子项--子项
     */
    //复测分坑
    fun PassagewayResurveyPitdividing():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/resurveyPitdividing.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //基础开挖
    fun PassagewayFoundationExcavation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/foundationExcavation/foundationExcavation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //材料运输
    fun PassagewayMaterialTransportation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/materialTransportation/materialTransportation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //预制构件制作及安装
    fun PassagewayManufactureInstallation():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/manufactureInstallation/manufactureInstallation.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //基础浇筑
    fun PassagewayFoundationPouring():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/foundationPouring/foundationPouring.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //电缆配管
    fun PassagewayCablePiping():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/cablePiping/cablePiping.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //电缆桥架
    fun PassagewayCableTray():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/cableTray/cableTray.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //电缆敷设
    fun PassagewayCableLaying():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/cableLaying/cableLaying.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //电缆头制作
    fun PassagewayCableHeadFabrication():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/cableHeadFabrication/cableHeadFabrication.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //电缆防火
    fun PassagewayCableFireProtection():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/cableFireProtection.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //电缆试验
    fun PassagewayCableTest():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/cableTest.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //接地敷设
    fun PassagewayGroundLaying():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/groundLaying/groundLaying.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //
    /*
     *  公共
     */
    //自查自检
    fun SelfExamination(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Public/selfExamination.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        mData[0].tvSelect = View.OnClickListener {
            XPopup.Builder(context)
                .atView(it)
                .asAttachList(arrayOf("下载模板", "上传文件", "上传照片","拍照"), null)
                { position, text ->
                    run {
                        if(text=="拍照") {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
                        } else if(text=="上传照片"){
                            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
                        } else if(text=="下载模板"){
                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
                                .withMemoryBar(true).build()
                            chooser.show()
                        } else if(text=="上传文件") {
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "*/*"
                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                    }
                }.show()
        }
        for(j in 2 until mData.size){
            mData[j].checkMoreListener = View.OnClickListener {
                val option1Items = listOf("下载", "重命名", "转发","删除")
                val handler = Handler(Handler.Callback {
                    when (it.what) {
                        RecyclerviewAdapter.MESSAGE_SELECT_OK -> {

                            false
                        }
                        else -> {
                            false
                        }
                    }
                })
                val selectDialog =
                    CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, option1Items, handler).dialog
                selectDialog.show()
            }
        }

        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //监理验收
    fun SupervisionAcceptance(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Public/supervisionAcceptance.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        mData[0].tvSelect = View.OnClickListener {
            XPopup.Builder(context)
                .atView(it)
                .asAttachList(arrayOf("下载模板", "上传文件", "上传照片","拍照"), null)
                { position, text ->
                    run {
                        if(text=="拍照") {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
                        } else if(text=="上传照片"){
                            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
                        } else if(text=="下载模板"){
                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
                                .withMemoryBar(true).build()
                            chooser.show()
                        } else if(text=="上传文件") {
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "*/*"
                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                    }
                }.show()
        }
        for(j in 2 until mData.size){
            mData[j].checkMoreListener = View.OnClickListener {
                val option1Items = listOf("下载", "重命名", "转发","删除")
                val handler = Handler(Handler.Callback {
                    when (it.what) {
                        RecyclerviewAdapter.MESSAGE_SELECT_OK -> {
                            false
                        }
                        else -> {
                            false }
                    }
                })
                val selectDialog =
                    CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, option1Items, handler).dialog
                selectDialog.show()
            }
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //业主验收
    fun OwnerAcceptance(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Public/ownerAcceptance.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        mData[0].tvSelect = View.OnClickListener {
            XPopup.Builder(context)
                .atView(it)
                .asAttachList(arrayOf("下载模板", "上传文件", "上传照片","拍照"), null)
                { position, text ->
                    run {
                        if(text=="拍照") {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
                        } else if(text=="上传照片"){
                            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
                        } else if(text=="下载模板"){
                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
                                .withMemoryBar(true).build()
                            chooser.show()
                        } else if(text=="上传文件") {
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "*/*"
                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                    }
                }.show()
        }
        for(j in 2 until mData.size){
            mData[j].checkMoreListener = View.OnClickListener {
                val option1Items = listOf("下载", "重命名", "转发","删除")
                val handler = Handler(Handler.Callback {
                    when (it.what) {
                        RecyclerviewAdapter.MESSAGE_SELECT_OK -> {
                            false
                        }
                        else -> {
                            false
                        }
                    }
                })
                val selectDialog =
                    CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, option1Items, handler).dialog
                selectDialog.show()
            }
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //资料动态
    fun DataDynamic():RecyclerviewAdapter{
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Professional/Public/dataDynamics.json")
        mData[0].backListener=View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //专业
    fun Professional_ProjectMore():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Professional/ProjectMore.json")
        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }
    // alterPosition time:2019/7/15
    // function:/* 节点更多 */
    fun Professional_NodeMore():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Professional/NodeMore.json")
        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }
    // alterPosition time:2019/7/15
    // function:/* 通道更多 */
    fun Professional_PassageWayMore():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Professional/PassageWayMore.json")
        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }

    // alterPosition time:2019/7/18
    // function: //新建项目盘
    fun Professional_NewProjectDisk(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/NewProjectDisk.json")
        val adapter = RecyclerviewAdapter(mData)
        mData[0].backListener = View.OnClickListener {
            activity.finish()
        }

        for (j in 1 until 17) {
            if (j in arrayListOf(5, 6, 7, 12, 13))
                continue
            mData[j].jumpListener = View.OnClickListener {
                val builder = AlertDialog.Builder(context)
                val dialogView = View.inflate(context, R.layout.shift_dialog, null)
                builder.setTitle(mData[j].shiftInputTitle)
                builder.setNegativeButton("取消", null)
                builder.setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                    mData[j].shiftinputContent = dialogView.shift_dialog_content.text.toString()
                    //adapter.notifyItemChanged(j)  //强制刷新
                    adapter.onBindViewHolder(adapter.VHList.get(j), j)
                    //adapter.notifyDataSetChanged()
                    //adapter.notifyItemRangeChanged(j,2)
                })
                val dialog = builder.create()
                dialog.setView(dialogView)
                dialog.show()
            }
        }
        for(j in 12 until 14) {
            mData[j].jumpListener = View.OnClickListener {
                var result = ""
                val cale1 = Calendar.getInstance()
                DatePickerDialog(
                    context,
                    DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        result += "${year}年${month + 1}月${dayOfMonth}日"
                        mData[j].shiftinputContent = result
                        adapter.onBindViewHolder(adapter.VHList.get(j), j)
                    },
                    cale1.get(Calendar.YEAR),
                    cale1.get(Calendar.MONTH),
                    cale1.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
        val uploadProjectListListener = View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        mData[14].jumpListener = uploadProjectListListener

        return adapter
    }
}