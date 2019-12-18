package com.example.eletronicengineer.utils

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps2d.AMapUtils
import com.amap.api.maps2d.model.LatLng
import com.electric.engineering.model.MultiStyleItem
import com.electric.engineering.utils.ItemGenerate
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.*
import com.example.eletronicengineer.adapter.CheckBoxAdapter
import com.example.eletronicengineer.adapter.EngineeringAppLiancesAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.aninterface.CheckBoxStyle
import com.example.eletronicengineer.aninterface.EngineeringAppliances
import com.example.eletronicengineer.db.DisplayDemand.RequirementCarList
import com.example.eletronicengineer.db.DisplayDemand.RequirementMembersList
import com.example.eletronicengineer.distributionFileSave.*
import com.example.eletronicengineer.fragment.projectdisk.ProjectImageCheckFragment
import com.example.eletronicengineer.fragment.projectdisk.ProjectMoreFragment
import com.example.eletronicengineer.fragment.sdf.*
import com.example.eletronicengineer.model.Constants
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.dialog_input.view.*
import kotlinx.android.synthetic.main.dialog_soil_ratio.view.*
import kotlinx.android.synthetic.main.shift_dialog.view.*
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


class AdapterGenerate {
    //转化成常量类型
    fun getType(content: String): Int {
        when (content) {
            //供需
            "需求个人 普工", "个人劳务 普工" -> return Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal
            "需求个人 特种作业", "个人劳务 特种作业" -> return Constants.FragmentType.PERSONAL_SPECIAL_WORK_TYPE.ordinal
            "需求个人 专业操作", "个人劳务 专业操作" -> return Constants.FragmentType.PERSONAL_PROFESSIONAL_OPERATION_TYPE.ordinal
            "需求个人 测量工", "个人劳务 测量工" -> return Constants.FragmentType.PERSONAL_SURVEYOR_TYPE.ordinal
            "需求个人 驾驶员", "个人劳务 驾驶员" -> return Constants.FragmentType.PERSONAL_DRIVER_TYPE.ordinal
            "需求个人 九大员", "个人劳务 九大员" -> return Constants.FragmentType.PERSONAL_NINE_MEMBERS_TYPE.ordinal
            "需求个人 注册类", "个人劳务 注册类" -> return Constants.FragmentType.PERSONAL_REGISTRATION_CLASS_TYPE.ordinal
            "需求个人 其他", "个人劳务 其他" -> return Constants.FragmentType.PERSONAL_OTHER_TYPE.ordinal
            "需求团队 变电施工队", "团队服务 变电施工队" -> return Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal
            "需求团队 主网施工队", "团队服务 主网施工队" -> return Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal
            "需求团队 配网施工队", "团队服务 配网施工队" -> return Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal
            "需求团队 测量设计", "团队服务 测量设计" -> return Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal
            "需求团队 马帮运输", "团队服务 马帮运输" -> return Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal
            "需求团队 桩基服务", "团队服务 桩基服务" -> return Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal
            "需求团队 非开挖顶拉管作业", "团队服务 非开挖顶拉管作业" -> return Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal
            "需求团队 试验调试", "团队服务 试验调试" -> return Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal
            "需求团队 跨越架", "团队服务 跨越架" -> return Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal
            "需求团队 运行维护", "团队服务 运行维护" -> return Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal
            "需求租赁 车辆租赁", "租赁服务 车辆租赁" -> return Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal
            "需求租赁 工器具租赁", "租赁服务 工器具租赁" -> return Constants.FragmentType.TOOL_LEASING_TYPE.ordinal
            "需求租赁 机械租赁", "租赁服务 机械租赁" -> return Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal
            "需求租赁 设备租赁", "租赁服务 设备租赁" -> return Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal
            "需求三方 代办资格", "三方服务 代办资格" -> return Constants.FragmentType.TRIPARTITE_AGENCY_QUALIFICATION_TYPE.ordinal
            "需求三方 培训办证", "三方服务 培训办证" -> return Constants.FragmentType.TRIPARTITE_TRAINING_CERTIFICATE_TYPE.ordinal
            "需求三方 财务记账", "三方服务 财务记账" -> return Constants.FragmentType.TRIPARTITE_FINANCIAL_ACCOUNTING_TYPE.ordinal
            "需求三方 标书服务", "三方服务 标书服务" -> return Constants.FragmentType.TRIPARTITE_TENDER_SERVICE_TYPE.ordinal
            "需求三方 法律咨询", "三方服务 法律咨询" -> return Constants.FragmentType.TRIPARTITE_LEGAL_ADVICE_TYPE.ordinal
            "需求三方 软件服务", "三方服务 软件服务" -> return Constants.FragmentType.TRIPARTITE_SOFTWARE_SERVICE_TYPE.ordinal
            "需求三方 其他", "三方服务 其他" -> return Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal

            //专业

            "新建项目盘" -> return Constants.Subitem_TYPE.NEW_PROJECT_DISK.ordinal
            "架空 经济指标" -> return Constants.Subitem_TYPE.OVERHEAD_ECONOMIC_INDICATOR.ordinal
            //"架空 材料清册"->return Constants.Subitem_TYPE

            "架空 耐张段" -> return Constants.Subitem_TYPE.OVERHEAD_TENSILE_SEGMENT.ordinal
            "架空 杆塔参数" -> return Constants.Subitem_TYPE.OVERHEAD_TOWER_PARAMETERS.ordinal
            "架空 公共点定位" -> return Constants.Subitem_TYPE.OVERHEAD_PUBLIC_POINT_POSITION.ordinal
            "杆塔子项" -> return Constants.Subitem_TYPE.OVERHEAD_TOWER_SUBITEM.ordinal

            "杆塔子项 复测分坑" -> return Constants.Subitem_TYPE.OVERHEAD_RESURVEY_PITDIVIDING.ordinal
            "杆塔子项 基础开挖" -> return Constants.Subitem_TYPE.OVERHEAD_FOUNDATION_EXCAVATION.ordinal
            "杆塔子项 材料运输" -> return Constants.Subitem_TYPE.OVERHEAD_MATERIAL_TRANSPORTATION.ordinal
            "杆塔子项 预制件填埋" -> return Constants.Subitem_TYPE.OVERHEAD_PREFABRICATED_LANDFILL.ordinal
            "杆塔子项 基础浇筑" -> return Constants.Subitem_TYPE.OVERHEAD_FOUNDATION_POURING.ordinal
            "杆塔子项 杆塔组立" -> return Constants.Subitem_TYPE.OVERHEAD_TOWER_ASSEMBLY.ordinal
            "杆塔子项 拉线制作" -> return Constants.Subitem_TYPE.OVERHEAD_WIRE_DRAWING.ordinal
            "杆塔子项 横担安装" -> return Constants.Subitem_TYPE.OVERHEAD_CROSSBAR_INSTALLATION.ordinal
            "杆塔子项 绝缘子安装" -> return Constants.Subitem_TYPE.OVERHEAD_INSULATOR_MOUNTING.ordinal
            "杆塔子项 焊接制作" -> return Constants.Subitem_TYPE.OVERHEAD_WELDING_FABRICATION.ordinal
            "杆塔子项 导线架设" -> return Constants.Subitem_TYPE.OVERHEAD_WIRE_ERECTION.ordinal
            "杆塔子项 附件安装" -> return Constants.Subitem_TYPE.OVERHEAD_ACCESSORIES_INSTALLING.ordinal
            "杆塔子项 设备安装" -> return Constants.Subitem_TYPE.OVERHEAD_EQUIPMENT_INSTALLATION.ordinal
            "杆塔子项 接地敷设" -> return Constants.Subitem_TYPE.OVERHEAD_GROUND_LAYING.ordinal
            "杆塔子项 户表安装" -> return Constants.Subitem_TYPE.OVERHEAD_TABLE_INSTALLATION.ordinal
            "杆塔子项 带电作业" -> return Constants.Subitem_TYPE.OVERHEAD_LIVE_WORK.ordinal

            "节点 经济指标" -> return Constants.Subitem_TYPE.NODE_ECONOMIC_INDICATOR.ordinal
            "节点 节点参数" -> return Constants.Subitem_TYPE.NODE_PARAMETERS.ordinal
            "节点子项" -> return Constants.Subitem_TYPE.NODE_SUBITEMS.ordinal

            "节点子项 复测分坑" -> return Constants.Subitem_TYPE.NODE_RESURVEY_PITDIVIDING.ordinal
            "节点子项 基础开挖" -> return Constants.Subitem_TYPE.NODE_FOUNDATION_EXCAVATION.ordinal
            "节点子项 材料运输" -> return Constants.Subitem_TYPE.NODE_MATERIAL_TRANSPORTATION.ordinal
            "节点子项 预制构件制作及安装" -> return Constants.Subitem_TYPE.NODE_MANUFACTURE_INSTALLATION.ordinal
            "节点子项 基础浇筑" -> return Constants.Subitem_TYPE.NODE_FOUNDATION_POURING.ordinal
            "节点子项 设备安装" -> return Constants.Subitem_TYPE.NODE_EQUIPMENT_INSTALLATION.ordinal
            "节点子项 接地敷设" -> return Constants.Subitem_TYPE.NODE_GROUND_LAYING.ordinal
            "节点子项 户表安装" -> return Constants.Subitem_TYPE.NODE_TABLE_INSTALLATION.ordinal

            "通道 经济指标" -> return Constants.Subitem_TYPE.PASSAGEWAY_ECONOMIC_INDICATOR.ordinal
            "通道 通道参数" -> return Constants.Subitem_TYPE.PASSAGEWAY_CHANNEL_PARAMETERS.ordinal
            "通道子项" -> return Constants.Subitem_TYPE.PASSAGEWAY_CHANNEL_SUBITEMS.ordinal


            "通道子项 复测分坑" -> return Constants.Subitem_TYPE.PASSAGEWAY_RESURVEY_PITDIVIDING.ordinal
            "通道子项 基础开挖" -> return Constants.Subitem_TYPE.PASSAGEWAY_FOUNDATION_EXCAVATION.ordinal
            "通道子项 材料运输" -> return Constants.Subitem_TYPE.PASSAGEWAY_MATERIAL_TRANSPORTATION.ordinal
            "通道子项 预制构件制作及安装" -> return Constants.Subitem_TYPE.PASSAGEWAY_MANUFACTURE_INSTALLATION.ordinal
            "通道子项 基础浇筑" -> return Constants.Subitem_TYPE.PASSAGEWAY_FOUNDATION_POURING.ordinal
            "通道子项 电缆配管" -> return Constants.Subitem_TYPE.PASSAGEWAY_CABLE_PIPING.ordinal
            "通道子项 电缆桥架" -> return Constants.Subitem_TYPE.PASSAGEWAY_CABLE_TRAY.ordinal
            "通道子项 电缆敷设" -> return Constants.Subitem_TYPE.PASSAGEWAY_CABLE_LAYING.ordinal
            "通道子项 电缆头制作" -> return Constants.Subitem_TYPE.PASSAGEWAY_CABLE_HEAD_FABRICATION.ordinal
            "通道子项 电缆防火" -> return Constants.Subitem_TYPE.PASSAGEWAY_CABLE_FIRE_PROTECTION.ordinal
            "通道子项 电缆试验" -> return Constants.Subitem_TYPE.PASSAGEWEAY_CABLE_TEST.ordinal
            "通道子项 接地敷设" -> return Constants.Subitem_TYPE.PASSAGEWAY_GROUND_LAYING.ordinal

            "自查自检" -> return Constants.Subitem_TYPE.SELF_EXAMINATION.ordinal
            "监理验收" -> return Constants.Subitem_TYPE.SUPERVISION_ACCEPTANCE.ordinal
            "业主验收" -> return Constants.Subitem_TYPE.OWNER_ACCEPTANCE.ordinal
            "资料动态" -> return Constants.Subitem_TYPE.DATA_DYNAMIC.ordinal

            else -> return Constants.Subitem_TYPE.OVERHEAD_MORE.ordinal
        }
    }

    /*
        get json file with context
    */
    lateinit var context: Context
    /*
    *   upload file with activity
    * */
    lateinit var activity: AppCompatActivity

    //需求个人
    fun DemandIndividual(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Demand/DemandIndividual.json")
        mData[13].singleDisplayRightContent = UnSerializeDataBase.idCardName
        mData[14].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val adapter = RecyclerviewAdapter(mData)
        val styleTypeNum = arrayListOf(4,5,11,15)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(3,4,5,7,11,12,15)
        for(j in necessaryNum){
            adapter.mData[j].necessary= true//true为必选项
        }
        adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementPerson

        return adapter
    }

    //需求团队——变电施工
    fun DemandGroupSubstationConstruction(): RecyclerviewAdapter {

        val frame = if(activity is DemandActivity) R.id.frame_demand_publish else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Demand/DemandGroup(Substation Construction).json")
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[16].singleDisplayRightContent = UnSerializeDataBase.idCardName
        adapter.mData[17].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val mdata=Bundle()
        adapter.mData[4].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 4
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[4].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        adapter.mData[13].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 13
            mdata.putString("type","成员清册发布")
            mdata.putSerializable("inventory",mData[13].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        val styleTypeNum = arrayListOf(3,7,8,14,18)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(2,3,5,6,7,9,13,14,15,18)
        for(j in necessaryNum){
            adapter.mData[j].necessary= true//true为必选项
        }

        return adapter
//        val handler = Handler(Looper.getMainLooper(),Handler.Callback {
//            when (it.what) {
//                Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[15].additionalKey.split(" ")[0]))
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清工薪资清册模板", "清工薪资清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[15].additionalKey.split(" ")[1]))
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清单报价清册模板", "清单报价清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                else -> {
//                    false
//                }
//            }
//        })
//        val multiButtonListeners: MutableList<View.OnClickListener> = ArrayList()
//        val multiButtonListenersForSalary: MutableList<View.OnClickListener> = ArrayList()
//        val clearanceSalary = View.OnClickListener {
//            val content: List<String> = listOf("上传附件", "下载模板")
//            Toast.makeText(context, "模式:清工薪资", Toast.LENGTH_SHORT).show()
//            mData[15].buttonCheckList[0] = true
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        }
//        val listQuote = View.OnClickListener {
//            val content: List<String> = listOf("上传附件", "下载模板")
//            Toast.makeText(context, "模式:清单报价", Toast.LENGTH_SHORT).show()
//            mData[15].buttonCheckList[1] = true
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        }
//        multiButtonListenersForSalary.add(clearanceSalary)//薪资标准
//        multiButtonListenersForSalary.add(listQuote)
//
//
//        val uploadProjectListListener = View.OnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "*/*"
//            UnSerializeDataBase.fileList.add(FileMap("", mData[5].additionalKey))
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//        }
//        val cameraListener = View.OnClickListener {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            UnSerializeDataBase.imgList.add(BitmapMap("", mData[5].additionalKey))
//            //intent.putExtra("key",mData[5].key)
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//        }
//        val downloadProgressListener = View.OnClickListener {
//            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                .withMemoryBar(true).build()
//            chooser.show()
//            chooser.setOnSelectListener { selectContent ->
//                downloadFile(selectContent, "工程清册模板", "工程清册模板", "")
//            }
//        }
//        multiButtonListeners.add(downloadProgressListener)
//        multiButtonListeners.add(cameraListener)//工程清册
//        multiButtonListeners.add(uploadProjectListListener)
//        mData[5].buttonListener = multiButtonListeners
//        mData[15].buttonListener = multiButtonListenersForSalary

    }

    //需求团队——测量设计
    fun DemandGroupMeasurementDesign(): RecyclerviewAdapter {
        val frame = if(activity is DemandActivity) R.id.frame_demand_publish else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Demand/DemandGroup(Measurement Design).json")
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[16].singleDisplayRightContent = UnSerializeDataBase.idCardName
        adapter.mData[17].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val mdata=Bundle()
        adapter.mData[4].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 4
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[4].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        adapter.mData[13].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 13
            mdata.putString("type","成员清册发布")
            mdata.putSerializable("inventory",mData[13].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        val styleTypeNum = arrayListOf(3,7,8,14,18)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(2,3,5,6,7,9,13,14,15,18)
        for(j in necessaryNum){
            adapter.mData[j].necessary= true//true为必选项
        }

        return adapter
//        mData[0].backListener = View.OnClickListener {
//            activity.finish()
//            UnSerializeDataBase.imgList.clear()
//            UnSerializeDataBase.fileList.clear()
//        }
//        val handler = Handler(Looper.getMainLooper(),Handler.Callback {
//            when (it.what) {
//                Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[15].additionalKey.split(" ")[0]))
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清工薪资清册模板", "清工薪资清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[15].additionalKey.split(" ")[1]))
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清单报价清册模板", "清单报价清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                else -> {
//                    false
//                }
//            }
//        })
//        val multiButtonListeners: MutableList<View.OnClickListener> = ArrayList()
//        val multiButtonListenersForSalary: MutableList<View.OnClickListener> = ArrayList()
//        val clearanceSalary = View.OnClickListener {
//            val content: List<String> = listOf("上传附件", "下载模板")
//            mData[15].buttonCheckList[0] = true
//            Toast.makeText(context, "模式:清工薪资", Toast.LENGTH_SHORT).show()
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        }
//        val listQuote = View.OnClickListener {
//            val content: List<String> = listOf("上传附件", "下载模板")
//            Toast.makeText(context, "模式:清单报价", Toast.LENGTH_SHORT).show()
//            mData[15].buttonCheckList[1] = true
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        }
//        multiButtonListenersForSalary.add(clearanceSalary)//薪资标准
//        multiButtonListenersForSalary.add(listQuote)
//
//
//        val uploadProjectListListener = View.OnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "*/*"
//            UnSerializeDataBase.fileList.add(FileMap("", mData[5].additionalKey))
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//        }
//        val cameraListener = View.OnClickListener {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            UnSerializeDataBase.imgList.add(BitmapMap("", mData[5].additionalKey))
//            //intent.putExtra("key",mData[5].key)
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//        }
//        val downloadProgressListener = View.OnClickListener {
//            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                .withMemoryBar(true).build()
//            chooser.show()
//            chooser.setOnSelectListener { selectContent ->
//                downloadFile(selectContent, "工程清册模板", "工程清册模板", "")
//            }
//        }
//        multiButtonListeners.add(downloadProgressListener)
//        multiButtonListeners.add(cameraListener)//工程量清册
//        multiButtonListeners.add(uploadProjectListListener)
//        mData[5].buttonListener = multiButtonListeners
//        mData[15].buttonListener = multiButtonListenersForSalary
    }

    //需求团队——马帮运输
    fun DemandGroupCaravanTransportation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Demand/DemandGroup(Caravan transportation).json")
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[13].singleDisplayRightContent = UnSerializeDataBase.idCardName
        adapter.mData[14].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val styleTypeNum = arrayListOf(3,5,6,15)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(2,3,5,11,12,15)
        for(j in necessaryNum){
            adapter.mData[j].necessary= true//true为必选项
        }
        return adapter
//        mData[0].backListener = View.OnClickListener {
//            activity.finish()
//            UnSerializeDataBase.imgList.clear()
//            UnSerializeDataBase.fileList.clear()
//        }
//        val multiButtonListenerList: MutableList<View.OnClickListener> = ArrayList()
//        val handler = Handler(Looper.getMainLooper(),Handler.Callback {
//            when (it.what) {
//                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[14].additionalKey.split(" ")[1]))
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清单报价清册模板", "清单报价清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                else -> {
//                    false
//                }
//            }
//        })
//        multiButtonListenerList.add(View.OnClickListener {
//            Toast.makeText(context, "模式:面议", Toast.LENGTH_SHORT).show()
//            mData[14].resetCheckList(mData[14].buttonCheckList)
//            for (i in UnSerializeDataBase.fileList) {
//                if (i.key == mData[14].additionalKey.split(" ")[1]) {
//                    UnSerializeDataBase.fileList.remove(i)
//                }
//            }
//            mData[14].buttonCheckList[0] = true
//        })
//        multiButtonListenerList.add(View.OnClickListener {
//            Toast.makeText(context, "模式:清单报价", Toast.LENGTH_SHORT).show()
//            mData[14].resetCheckList(mData[14].buttonCheckList)
//            mData[14].buttonCheckList[1] = true
//            val content: List<String> = listOf("上传附件", "下载模板")
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        })
//        mData[14].buttonListener = multiButtonListenerList
//        val multiButtonListeners: MutableList<View.OnClickListener> = ArrayList()
//        val uploadProjectListListener = View.OnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "*/*"
//            UnSerializeDataBase.fileList.add(FileMap("", mData[5].additionalKey))
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//        }
//        val cameraListener = View.OnClickListener {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            UnSerializeDataBase.imgList.add(BitmapMap("", mData[5].additionalKey))
//            //intent.putExtra("key",mData[5].key)
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//        }
//        val downloadProgressListener = View.OnClickListener {
//            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                .withMemoryBar(true).build()
//            chooser.show()
//            chooser.setOnSelectListener { selectContent ->
//                downloadFile(selectContent, "工程清册模板", "工程清册模板", "")
//            }
//        }
//        multiButtonListeners.add(downloadProgressListener)
//        multiButtonListeners.add(cameraListener)//工程清册
//        multiButtonListeners.add(uploadProjectListListener)
//        mData[5].buttonListener = multiButtonListeners

    }

    //需求团队——桩基服务
    fun DemandGroupPileFoundationConstruction(): RecyclerviewAdapter {
        val frame = if(activity is DemandActivity) R.id.frame_demand_publish else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Demand/DemandGroup(Pile foundation construction).json")
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[16].singleDisplayRightContent = UnSerializeDataBase.idCardName
        adapter.mData[17].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val mdata=Bundle()
        adapter.mData[4].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 4
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[4].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        val styleTypeNum = arrayListOf(3,5,6,13,18)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(2,3,5,8,9,13,14,15,18)
        for(j in necessaryNum){
            adapter.mData[j].necessary= true//true为必选项
        }
        return adapter
//        val multiButtonListeners: MutableList<View.OnClickListener> = ArrayList()
//        val multiButtonListenerList: MutableList<View.OnClickListener> = ArrayList()
//        val handler = Handler(Looper.getMainLooper(),Handler.Callback {
//            when (it.what) {
//                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[16].additionalKey.split(" ")[1]))
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清单报价清册模板", "清单报价清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                else -> {
//                    false
//                }
//            }
//        })
//        multiButtonListenerList.add(View.OnClickListener {
//            Toast.makeText(context, "模式:面议", Toast.LENGTH_SHORT).show()
//            mData[16].resetCheckList(mData[16].buttonCheckList)
//            mData[16].buttonCheckList[0] = true
//            for (i in UnSerializeDataBase.fileList) {
//                if (i.key == mData[16].additionalKey.split(" ")[1]) {
//                    UnSerializeDataBase.fileList.remove(i)
//                }
//            }
//        })
//        multiButtonListenerList.add(View.OnClickListener {
//            Toast.makeText(context, "模式:清单报价", Toast.LENGTH_SHORT).show()
//            mData[16].resetCheckList(mData[16].buttonCheckList)
//            mData[16].buttonCheckList[1] = true
//            val content: List<String> = listOf("上传附件", "下载模板")
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        })
//        mData[16].buttonListener = multiButtonListenerList
//
//        val uploadProjectListListener = View.OnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "*/*"
//            UnSerializeDataBase.fileList.add(FileMap("", mData[5].additionalKey))
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//        }
//        val cameraListener = View.OnClickListener {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            UnSerializeDataBase.imgList.add(BitmapMap("", mData[5].additionalKey))
//            //intent.putExtra("key",mData[5].key)
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//        }
//        val downloadProgressListener = View.OnClickListener {
//            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                .withMemoryBar(true).build()
//            chooser.show()
//            chooser.setOnSelectListener { selectContent ->
//                downloadFile(selectContent, "工程清册模板", "工程清册模板", "")
//            }
//        }
//        multiButtonListeners.add(downloadProgressListener)
//        multiButtonListeners.add(cameraListener)//工程清册
//        multiButtonListeners.add(uploadProjectListListener)
//
//
//        mData[0].backListener = View.OnClickListener {
//
//            activity.finish()
//            UnSerializeDataBase.imgList.clear()
//            UnSerializeDataBase.fileList.clear()
//        }
//        mData[5].buttonListener = multiButtonListeners
    }

    //需求团队——非开挖
    fun DemandGroupNonExcavation(): RecyclerviewAdapter {
        val frame = if(activity is DemandActivity) R.id.frame_demand_publish else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val multiButtonListeners: MutableList<View.OnClickListener> = ArrayList()
        val mData = itemGenerate.getJsonFromAsset("Demand/DemandGroup(Non-excavation).json")
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[16].singleDisplayRightContent = UnSerializeDataBase.idCardName
        adapter.mData[17].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val mdata=Bundle()
        mData[4].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 4
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[4].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        val styleTypeNum = arrayListOf(3,7,8,13,15,19)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(2,3,5,6,7,13,14,15,18)
        for(j in necessaryNum){
            adapter.mData[j].necessary= true//true为必选项
        }
        return adapter
//        val multiButtonListenerList: MutableList<View.OnClickListener> = ArrayList()
//        val handler = Handler(Looper.getMainLooper(),Handler.Callback {
//            when (it.what) {
//                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[15].additionalKey.split(" ")[1]))
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清单报价清册模板", "清单报价清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                else -> {
//                    false
//                }
//            }
//        })
//        multiButtonListenerList.add(View.OnClickListener {
//            Toast.makeText(context, "模式:面议", Toast.LENGTH_SHORT).show()
//            mData[15].resetCheckList(mData[15].buttonCheckList)
//            mData[15].buttonCheckList[0] = true
//            for (i in UnSerializeDataBase.fileList) {
//                if (i.key == mData[15].additionalKey.split(" ")[1]) {
//                    UnSerializeDataBase.fileList.remove(i)
//                }
//            }
//        })
//        multiButtonListenerList.add(View.OnClickListener {
//            Toast.makeText(context, "模式:清单报价", Toast.LENGTH_SHORT).show()
//            mData[15].resetCheckList(mData[15].buttonCheckList)
//            mData[15].buttonCheckList[1] = true
//            val content: List<String> = listOf("上传附件", "下载模板")
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        })
//        mData[15].buttonListener = multiButtonListenerList
//        val uploadProjectListListener = View.OnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "*/*"
//            UnSerializeDataBase.fileList.add(FileMap("", mData[5].additionalKey))
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//        }
//        val cameraListener = View.OnClickListener {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            UnSerializeDataBase.imgList.add(BitmapMap("", mData[5].additionalKey))
//            //intent.putExtra("key",mData[5].key)
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//        }
//        val downloadProgressListener = View.OnClickListener {
//            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                .withMemoryBar(true).build()
//            chooser.show()
//            chooser.setOnSelectListener { selectContent ->
//                downloadFile(selectContent, "工程清册模板", "工程清册模板", "")
//            }
//        }
//        multiButtonListeners.add(downloadProgressListener)
//        multiButtonListeners.add(cameraListener)//工程清册
//        multiButtonListeners.add(uploadProjectListListener)
//
//
//        mData[0].backListener = View.OnClickListener {
//            activity.finish()
//            UnSerializeDataBase.imgList.clear()
//            UnSerializeDataBase.fileList.clear()
//        }
//       mData[5].buttonListener = multiButtonListeners
    }

    //需求团队——试验调试
    fun DemandGroupTestDebugging(): RecyclerviewAdapter {
        val frame = if(activity is DemandActivity) R.id.frame_demand_publish else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Demand/DemandGroup(Test debugging).json")
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[17].singleDisplayRightContent = UnSerializeDataBase.idCardName
        adapter.mData[18].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val mdata=Bundle()
        adapter.mData[4].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 4
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[4].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        adapter.mData[14].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 14
            mdata.putString("type","成员清册发布")
            mdata.putSerializable("inventory",mData[14].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        val styleTypeNum = arrayListOf(3,8,9,15,19)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(2,3,5,6,7,8,14,15,16,19)
        for(j in necessaryNum){
            adapter.mData[j].necessary= true//true为必选项
        }

        return adapter
//        mData[0].backListener = View.OnClickListener {
//            activity.finish()
//            UnSerializeDataBase.imgList.clear()
//            UnSerializeDataBase.fileList.clear()
//        }
//        val handler = Handler(Looper.getMainLooper(),Handler.Callback {
//            when (it.what) {
//                Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[15].additionalKey.split(" ")[0]))
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清工薪资清册模板", "清工薪资清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[15].additionalKey.split(" ")[1]))
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清单报价清册模板", "清单报价清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                else -> {
//                    false
//                }
//            }
//        })
//        val multiButtonListeners: MutableList<View.OnClickListener> = ArrayList()
//        val multiButtonListenersForSalary: MutableList<View.OnClickListener> = ArrayList()
//        val clearanceSalary = View.OnClickListener {
//            val content: List<String> = listOf("上传附件", "下载模板")
//            mData[15].buttonCheckList[0] = true
//            Toast.makeText(context, "模式:清工薪资", Toast.LENGTH_SHORT).show()
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        }
//        val listQuote = View.OnClickListener {
//            val content: List<String> = listOf("上传附件", "下载模板")
//            Toast.makeText(context, "模式:清单报价", Toast.LENGTH_SHORT).show()
//            mData[15].buttonCheckList[1] = true
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        }
//        multiButtonListenersForSalary.add(clearanceSalary)//薪资标准
//        multiButtonListenersForSalary.add(listQuote)
//        val uploadProjectListListener = View.OnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "*/*"
//            UnSerializeDataBase.fileList.add(FileMap("", mData[5].additionalKey))
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//        }
//        val cameraListener = View.OnClickListener {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            UnSerializeDataBase.imgList.add(BitmapMap("", mData[5].additionalKey))
//            //intent.putExtra("key",mData[5].key)
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//        }
//        val downloadProgressListener = View.OnClickListener {
//            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                .withMemoryBar(true).build()
//            chooser.show()
//            chooser.setOnSelectListener { selectContent ->
//                downloadFile(selectContent, "工程清册模板", "工程清册模板", "")
//            }
//        }
//        multiButtonListeners.add(downloadProgressListener)
//        multiButtonListeners.add(cameraListener)
//        multiButtonListeners.add(uploadProjectListListener)//工程清册
//        mData[5].buttonListener = multiButtonListeners
//        mData[15].buttonListener = multiButtonListenersForSalary
    }

    //需求团队——跨越架
    fun DemandGroupCrossingFrame(): RecyclerviewAdapter {
        val frame = if(activity is DemandActivity) R.id.frame_demand_publish else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Demand/DemandGroup(Crossing frame).json")
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[15].singleDisplayRightContent = UnSerializeDataBase.idCardName
        adapter.mData[16].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val mdata=Bundle()
        adapter.mData[4].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 4
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[4].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata),frame, "publishInventory")
        })
        adapter.mData[12].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 12
            mdata.putString("type","成员清册发布")
            mdata.putSerializable("inventory",mData[12].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        val styleTypeNum = arrayListOf(3,6,7,13,17)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(2,3,5,6,12,13,14,17)
        for(j in necessaryNum){
            adapter.mData[j].necessary= true//true为必选项
        }
        return adapter
//        mData[0].backListener = View.OnClickListener {
//            activity.finish()
//            UnSerializeDataBase.imgList.clear()
//            UnSerializeDataBase.fileList.clear()
//        }
//        val handler = Handler(Looper.getMainLooper(),Handler.Callback {
//            when (it.what) {
//                Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[13].additionalKey.split(" ")[0]))
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清工薪资清册模板", "清工薪资清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[13].additionalKey.split(" ")[1]))
//
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清单报价清册模板", "清单报价清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                else -> {
//                    false
//                }
//            }
//        })
//        val multiButtonListeners: MutableList<View.OnClickListener> = ArrayList<View.OnClickListener>()
//        val multiButtonListenersForSalary: MutableList<View.OnClickListener> = ArrayList()
//        val clearanceSalary = View.OnClickListener {
//            val content: List<String> = listOf("上传附件", "下载模板")
//            mData[13].buttonCheckList[0] = true
//            Toast.makeText(context, "模式:清工薪资", Toast.LENGTH_SHORT).show()
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        }
//        val listQuote = View.OnClickListener {
//            val content: List<String> = listOf("上传附件", "下载模板")
//            Toast.makeText(context, "模式:清单报价", Toast.LENGTH_SHORT).show()
//            mData[13].buttonCheckList[1] = true
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        }
//        multiButtonListenersForSalary.add(clearanceSalary)//薪资标准
//        multiButtonListenersForSalary.add(listQuote)
//
//
//        val uploadProjectListListener = View.OnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "*/*"
//            UnSerializeDataBase.fileList.add(FileMap("", mData[5].additionalKey))
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//        }
//        val cameraListener = View.OnClickListener {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            UnSerializeDataBase.imgList.add(BitmapMap("", mData[5].additionalKey))
//            //intent.putExtra("key",mData[5].key)
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//        }
//        val downloadProgressListener = View.OnClickListener {
//            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                .withMemoryBar(true).build()
//            chooser.show()
//            chooser.setOnSelectListener { selectContent ->
//                downloadFile(selectContent, "工程清册模板", "工程清册模板", "")
//            }
//        }
//        multiButtonListeners.add(downloadProgressListener)
//        multiButtonListeners.add(cameraListener)
//        multiButtonListeners.add(uploadProjectListListener)//工程量清册
//        mData[5].buttonListener = multiButtonListeners
//        mData[13].buttonListener = multiButtonListenersForSalary
    }

    //需求团队——运行维护
    fun DemandGroupOperationAndMaintenance(): RecyclerviewAdapter {
        val frame = if(activity is DemandActivity) R.id.frame_demand_publish else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Demand/DemandGroup(Operation and Maintenance).json")
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[15].singleDisplayRightContent = UnSerializeDataBase.idCardName
        adapter.mData[16].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val mdata=Bundle()
        adapter.mData[4].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 4
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[4].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        adapter.mData[12].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 12
            mdata.putString("type","成员清册发布")
            mdata.putSerializable("inventory",mData[12].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        val styleTypeNum = arrayListOf(3,5,6,13,17)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(2,3,5,8,12,13,14,17)
        for(j in necessaryNum){
            adapter.mData[j].necessary= true//true为必选项
        }
        return adapter
//        mData[0].backListener = View.OnClickListener {
//            activity.finish()
//            UnSerializeDataBase.imgList.clear()
//            UnSerializeDataBase.fileList.clear()
//        }
//        val handler = Handler(Looper.getMainLooper(),Handler.Callback {
//            when (it.what) {
//                Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[12].additionalKey.split(" ")[0]))
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清工薪资清册模板", "清工薪资清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[12].additionalKey.split(" ")[1]))
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清单报价清册模板", "清单报价清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                else -> {
//                    false
//                }
//            }
//        })
//        val multiButtonListeners: MutableList<View.OnClickListener> = ArrayList()
//        val multiButtonListenersForSalary: MutableList<View.OnClickListener> = ArrayList()
//        val clearanceSalary = View.OnClickListener {
//            val content: List<String> = listOf("上传附件", "下载模板")
//            mData[12].buttonCheckList[0] = true
//            Toast.makeText(context, "模式:清工薪资", Toast.LENGTH_SHORT).show()
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_CLEARANCE_SALARY_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        }
//        val listQuote = View.OnClickListener {
//            val content: List<String> = listOf("上传附件", "下载模板")
//            Toast.makeText(context, "模式:清单报价", Toast.LENGTH_SHORT).show()
//            mData[12].buttonCheckList[1] = true
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        }
//        multiButtonListenersForSalary.add(clearanceSalary)//薪资标准
//        multiButtonListenersForSalary.add(listQuote)
//
//
//        val downloadProgressListener = View.OnClickListener {
//            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                .withMemoryBar(true).build()
//            chooser.show()
//            chooser.setOnSelectListener { selectContent ->
//                downloadFile(selectContent, "工程清册模板", "工程清册模板", "")
//            }
//        }
//        val uploadProjectListListener = View.OnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "*/*"
//            UnSerializeDataBase.fileList.add(FileMap("", mData[5].additionalKey))
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//        }
//        val cameraListener = View.OnClickListener {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            UnSerializeDataBase.imgList.add(BitmapMap("", mData[5].additionalKey))
//            //intent.putExtra("key",mData[5].key)
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//        }
//        //工程清册
//        multiButtonListeners.add(downloadProgressListener)
//        multiButtonListeners.add(cameraListener)
//        multiButtonListeners.add(uploadProjectListListener)
//        mData[5].buttonListener = multiButtonListeners
//        mData[12].buttonListener = multiButtonListenersForSalary
    }

    //需求租赁——车辆租赁
    fun DemandLeaseVehicleLeasing(): RecyclerviewAdapter {
        val frame = if(activity is DemandActivity) R.id.frame_demand_publish else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Demand/DemandLease(Vehicle Leasing).json")
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[10].singleDisplayRightContent = UnSerializeDataBase.idCardName
        adapter.mData[11].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val mdata=Bundle()
        adapter.mData[4].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 4
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[4].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        val styleTypeNum = arrayListOf(3,12)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(2,3,5,9,12)
        for(j in necessaryNum){
            adapter.mData[j].necessary= true//true为必选项
        }
        return adapter
    }

    //需求租赁——工器具租赁
    fun DemandEquipmentLeasing(): RecyclerviewAdapter {
        val frame = if(activity is DemandActivity) R.id.frame_demand_publish else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Demand/DemandLease(Equipment Leasing).json")
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[9].singleDisplayRightContent = UnSerializeDataBase.idCardName
        adapter.mData[10].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val mdata=Bundle()
        adapter.mData[4].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 4
            mdata.putString("type","租赁清册发布")
            mdata.putSerializable("inventory",mData[4].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        val styleTypeNum = arrayListOf(3,11)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(2,3,5,6,7,8,11)
        for(j in necessaryNum){
            adapter.mData[j].necessary= true//true为必选项
        }
        return adapter
//        mData[0].backListener = View.OnClickListener {
//            activity.finish()
//            UnSerializeDataBase.imgList.clear()
//            UnSerializeDataBase.fileList.clear()
//        }
//        val multiButtonListenerList: MutableList<View.OnClickListener> = ArrayList()
//        val handler = Handler(Looper.getMainLooper(),Handler.Callback {
//            when (it.what) {
//                Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal -> {
//                    when (it.data.getString("selectContent")) {
//                        "上传附件" -> {
//                            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                            intent.type = "*/*"
//                            UnSerializeDataBase.fileList.add(FileMap("", mData[14].additionalKey.split(" ")[1]))
//                            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//                        }
//                        "下载模板" -> {
//                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                                .withMemoryBar(true).build()
//                            chooser.show()
//                            chooser.setOnSelectListener { selectContent ->
//                                downloadFile(selectContent, "清单报价清册模板", "清单报价清册模板", "")
//                            }
//                        }
//                    }
//                    false
//                }
//                else -> {
//                    false
//                }
//            }
//        })
//
//        multiButtonListenerList.add(View.OnClickListener {
//            Toast.makeText(context, "模式:面议", Toast.LENGTH_SHORT).show()
//            mData[9].resetCheckList(mData[9].buttonCheckList)
//            for (i in UnSerializeDataBase.fileList) {
//                if (i.key == mData[9].additionalKey.split(" ")[1]) {
//                    UnSerializeDataBase.fileList.remove(i)
//                }
//            }
//            mData[9].buttonCheckList[0] = true
//        })
//        multiButtonListenerList.add(View.OnClickListener {
//            Toast.makeText(context, "模式:清单报价", Toast.LENGTH_SHORT).show()
//            mData[9].resetCheckList(mData[9].buttonCheckList)
//            mData[9].buttonCheckList[1] = true
//            val content: List<String> = listOf("上传附件", "下载模板")
//            val customDialog = CustomDialog(CustomDialog.Options.SELECT_DIALOG, context, content, handler)
//            customDialog.msgWhat = Constants.HandlerMsg.UPLOAD_LIST_QUOTE_SELECT_OK.ordinal
//            customDialog.dialog.show()
//        })
//        val multiButtonListeners: MutableList<View.OnClickListener> = ArrayList()
//        val downloadProgressListener = View.OnClickListener {
//            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                .withMemoryBar(true).build()
//            chooser.show()
//            chooser.setOnSelectListener { selectContent ->
//                downloadFile(selectContent, "需求租赁清单", "需求租赁清单", "")
//            }
//        }
//        val uploadProjectListListener = View.OnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "*/*"
//            UnSerializeDataBase.fileList.add(FileMap("", mData[5].additionalKey))
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//        }
//        val cameraListener = View.OnClickListener {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            UnSerializeDataBase.imgList.add(BitmapMap("", mData[5].additionalKey))
//            //intent.putExtra("key",mData[5].key)
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//        }
//        multiButtonListeners.add(downloadProgressListener)
//        multiButtonListeners.add(cameraListener)//拍照
//        multiButtonListeners.add(uploadProjectListListener)//上传附件
//
//        mData[5].buttonListener = multiButtonListeners
    }

    //需求三方
    fun DemandTripartite(): RecyclerviewAdapter {
        val frame = if(activity is DemandActivity) R.id.frame_demand_publish else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Demand/DemandTripartite.json")
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[4].singleDisplayRightContent = UnSerializeDataBase.idCardName
        adapter.mData[5].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val mdata=Bundle()
        adapter.mData[1].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 1
            mdata.putString("type","三方服务清册发布")
            mdata.putSerializable("inventory",mData[1].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,PublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        val styleTypeNum = arrayListOf(6)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(2,6)
        for(j in necessaryNum){
            adapter.mData[j].necessary= true//true为必选项
        }
        adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementThirdParty
        return adapter
//        mData[0].backListener = View.OnClickListener {
//            activity.finish()
//            UnSerializeDataBase.imgList.clear()
//            UnSerializeDataBase.fileList.clear()
//        }
//        val multiButtonListeners: MutableList<View.OnClickListener> = ArrayList()
//        val downloadProgressListener = View.OnClickListener {
//            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                .withMemoryBar(true).build()
//            chooser.show()
//            chooser.setOnSelectListener { selectContent ->
//                downloadFile(selectContent, "三方服务清单模板", "三方服务清单模板", "")
//            }
//        }
//        val uploadProjectListListener = View.OnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "*/*"
//            //intent.putExtra("key",mData[4].key)
//            UnSerializeDataBase.fileList.add(FileMap("", mData[2].additionalKey))
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//        }
//        val cameraListener = View.OnClickListener {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            UnSerializeDataBase.imgList.add(BitmapMap("", mData[2].additionalKey))
//            //intent.putExtra("key",mData[4].key)
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
//        }
//        multiButtonListeners.add(downloadProgressListener)
//        multiButtonListeners.add(cameraListener)//拍照
//        multiButtonListeners.add(uploadProjectListListener)//上传附件
//        mData[2].buttonListener = multiButtonListeners
    }

    /*
    *供应模块
    */
    //个人劳务
    fun PersonalService(): RecyclerviewAdapter {
        val frame = if(activity is SupplyActivity) R.id.frame_supply else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Provider/Personal service.json")
        val adapter = RecyclerviewAdapter(mData)
        mData[6].singleDisplayRightContent = UnSerializeDataBase.idCardName
        mData[7].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val styleTypeNum = arrayListOf(3,4,9)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(2,4,5,9)
        for(j in necessaryNum){
            adapter.mData[j].necessary= true//true为必选项
        }
        mData[8].jumpListener = View.OnClickListener {
             val data = Bundle()
            data.putString("key", mData[8].key)
            data.putString("title", mData[8].shiftInputTitle)
            FragmentHelper.switchFragment(activity,ImageFragment.newInstance(data),frame,"Capture")
        }

        adapter.urlPath = Constants.HttpUrlPath.Provider.PersonalService
        return adapter
    }


    //团队服务——变电施工
    fun ProviderGroupSubstationConstruction(): RecyclerviewAdapter {
        val frame = if(activity is SupplyActivity) R.id.frame_supply else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context  //
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Substation Construction).json")
        val adapter = RecyclerviewAdapter(mData)
        mData[7].singleDisplayRightContent = UnSerializeDataBase.idCardName
        mData[8].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val styleTypeNum = arrayListOf(9)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(1,2,6,4,5,9)//1,2,6 true为按钮清册不填可以发布 //其余true为必选项
        for(j in necessaryNum){
            adapter.mData[j].necessary= true
        }
        val mdata=Bundle()
        mData[1].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 1
            mdata.putString("type","成员清册发布")
            mdata.putSerializable("inventory",mData[1].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[2].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 2
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[2].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[6].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 6
            mdata.putString("type","工器具清册发布")
            mdata.putSerializable("inventory",mData[6].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
//        adapter.urlPath = Constants.HttpUrlPath.Provider.mainPath
        return adapter
        //上传
//        mData[0].backListener = View.OnClickListener {
//            activity.finish()
//            UnSerializeDataBase.imgList.clear()
//            UnSerializeDataBase.fileList.clear()
//        }
//        for (j in 3 until 9) {
//            if (j in arrayListOf(5, 6, 7)) continue
//            val multiButtonListeners: MutableList<View.OnClickListener> = ArrayList()
//            multiButtonListeners.add(View.OnClickListener {
//                val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                    .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                    .withMemoryBar(true).build()
//                chooser.show()
//                chooser.setOnSelectListener { selectContent ->
//                    downloadFile(selectContent, mData[j].buttonTitle + "模板", mData[j].buttonTitle + "模板", "")
//                }
//            })
//            multiButtonListeners.add(View.OnClickListener {
//                val intent = Intent(Intent.ACTION_GET_CONTENT)
//                intent.type = "*/*"
//                UnSerializeDataBase.fileList.add(FileMap("", mData[j].additionalKey))
//                activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//            })
//            mData[j].buttonListener = multiButtonListeners
//        }

    }

    //团队服务——测量设计
    fun ProviderGroupMeasurementDesign(): RecyclerviewAdapter {
        val frame = if(activity is SupplyActivity) R.id.frame_supply else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mdata=Bundle()
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Measurement Design).json")
        val adapter = RecyclerviewAdapter(mData)
        mData[7].singleDisplayRightContent = UnSerializeDataBase.idCardName
        mData[8].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val styleTypeNum = arrayListOf(9)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(1,2,6,4,5,9)//1,2,6 true为按钮清册不填可以发布 //其余true为必选项
        for(j in necessaryNum){
            adapter.mData[j].necessary= true
        }
        mData[1].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 1
            mdata.putString("type","成员清册发布")
            mdata.putSerializable("inventory",mData[1].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[2].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 2
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[2].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[6].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 6
            mdata.putString("type","工器具清册发布")
            mdata.putSerializable("inventory",mData[6].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        adapter.urlPath = Constants.HttpUrlPath.Provider.MeasureDesign
        return adapter
//        mData[0].backListener = View.OnClickListener {
//            activity.finish()
//            UnSerializeDataBase.imgList.clear()
//            UnSerializeDataBase.fileList.clear()
//        }
//        for (j in 3 until 9) {
//            if (j in arrayListOf(5, 6, 7)) continue
//            val multiButtonListeners: MutableList<View.OnClickListener> = ArrayList()
//            multiButtonListeners.add(View.OnClickListener {
//                val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                    .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                    .withMemoryBar(true).build()
//                chooser.show()
//                chooser.setOnSelectListener { selectContent ->
//                    downloadFile(selectContent, mData[j].buttonTitle + "模板", mData[j].buttonTitle + "模板", "")
//                }
//            })
//            multiButtonListeners.add(View.OnClickListener {
//                val intent = Intent(Intent.ACTION_GET_CONTENT)
//                intent.type = "*/*"
//                UnSerializeDataBase.fileList.add(FileMap("", mData[j].additionalKey))
//                activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//            })
//            mData[j].buttonListener = multiButtonListeners
//        }
//        mData[5].jumpListener = View.OnClickListener {
//            val data = Bundle()
//            data.putString("title", mData[5].shiftInputTitle)
//            data.putString("key", mData[5].key)
//            (activity as SupplyActivity).switchFragment(ImageFragment.newInstance(data), R.id.frame_supply, "Capture")
//        }
    }

    //团队服务——马帮运输
    fun ProviderGroupCaravanTransportation(): RecyclerviewAdapter {
        val frame = if(activity is SupplyActivity) R.id.frame_supply else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mdata=Bundle()
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Caravan transportation).json")
        val adapter = RecyclerviewAdapter(mData)
        mData[4].singleDisplayRightContent = UnSerializeDataBase.idCardName
        mData[5].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val styleTypeNum = arrayListOf(2,3)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(1,2,3)//1 true为按钮清册不填可以发布 //其余true为必选项
        for(j in necessaryNum){
            adapter.mData[j].necessary= true
        }
        mData[1].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 1
            mdata.putString("type","成员清册发布")
            mdata.putSerializable("inventory",mData[1].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        adapter.urlPath = Constants.HttpUrlPath.Provider.CaravanTransport
        return adapter
    }


    //团队服务——桩机服务
    fun ProviderGroupPileFoundationConstruction(): RecyclerviewAdapter {
        val frame = if(activity is SupplyActivity) R.id.frame_supply else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        val mdata=Bundle()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Pile foundation construction).json")
        val adapter = RecyclerviewAdapter(mData)
        mData[8].singleDisplayRightContent = UnSerializeDataBase.idCardName
        mData[9].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val styleTypeNum = arrayListOf(10)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(1,2,7,4,5,10)//1,2,7 true为按钮清册不填可以发布 //其余true为必选项
        for(j in necessaryNum){
            adapter.mData[j].necessary= true
        }
        mData[1].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 1
            mdata.putString("type","成员清册发布")
            mdata.putSerializable("inventory",mData[1].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[2].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 2
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[2].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[7].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 7
            mdata.putString("type","工器具清册发布")
            mdata.putSerializable("inventory",mData[7].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        adapter.urlPath = Constants.HttpUrlPath.Provider.PileFoundation
        return adapter
    }

    //团队服务——非开挖
    fun ProviderGroupNonExcavation(): RecyclerviewAdapter {
        val frame = if(activity is SupplyActivity) R.id.frame_supply else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        val mdata=Bundle()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Non-excavation).json")
        val adapter = RecyclerviewAdapter(mData)
        mData[5].singleDisplayRightContent = UnSerializeDataBase.idCardName
        mData[6].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val styleTypeNum = arrayListOf(7)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(1,2,3,7)//1,2,3 true为按钮清册不填可以发布 //其余true为必选项
        for(j in necessaryNum){
            adapter.mData[j].necessary= true
        }
        mData[1].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 1
            mdata.putString("type","成员清册发布")
            mdata.putSerializable("inventory",mData[1].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[2].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 2
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[2].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[3].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 3
            mdata.putString("type","工器具清册发布")
            mdata.putSerializable("inventory",mData[3].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        adapter.urlPath = Constants.HttpUrlPath.Provider.Unexcavation
        return adapter
    }

    //团队服务——试验调试
    fun ProviderGroupTestDebugging(): RecyclerviewAdapter {
        val frame = if(activity is SupplyActivity) R.id.frame_supply else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        val mdata = Bundle()
        lateinit var adapter: RecyclerviewAdapter
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProvicerGroup(Test debugging).json")
        adapter = RecyclerviewAdapter(mData)
        mData[8].singleDisplayRightContent = UnSerializeDataBase.idCardName
        mData[9].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val styleTypeNum = arrayListOf(10)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(1,2,7,4,5,6,10)//1,2,7 true为按钮清册不填可以发布 //其余true为必选项
        for(j in necessaryNum){
            adapter.mData[j].necessary= true
        }
        mData[1].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 1
            mdata.putString("type","成员清册发布")
            mdata.putSerializable("inventory",mData[1].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[2].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 2
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[2].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[7].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 7
            mdata.putString("type","工器具清册发布")
            mdata.putSerializable("inventory",mData[7].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        adapter.urlPath = Constants.HttpUrlPath.Provider.TestTeam
        return adapter
    }

    //团队服务——跨越架
    fun ProviderGroupCrossingFrame(): RecyclerviewAdapter {
        val frame = if(activity is SupplyActivity) R.id.frame_supply else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        val mdata = Bundle()
        lateinit var adapter: RecyclerviewAdapter
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProvicerGroup(Crossing frame).json")
        adapter = RecyclerviewAdapter(mData)
        mData[5].singleDisplayRightContent = UnSerializeDataBase.idCardName
        mData[6].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val styleTypeNum = arrayListOf(7)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(1,2,3,7)//1,2,3 true为按钮清册不填可以发布 //其余true为必选项
        for(j in necessaryNum){
            adapter.mData[j].necessary= true
        }
        mData[1].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 1
            mdata.putString("type","成员清册发布")
            mdata.putSerializable("inventory",mData[1].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[2].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 2
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[2].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[3].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 3
            mdata.putString("type","工器具清册发布")
            mdata.putSerializable("inventory",mData[3].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        adapter.urlPath = Constants.HttpUrlPath.Provider.SpanWoodenSupprt
        return adapter
    }

    //团队服务——运行维护
    fun OperationAndMaintenance(): RecyclerviewAdapter {
        val frame = if(activity is SupplyActivity) R.id.frame_supply else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        val mdata = Bundle()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Operation and Maintenance).json")
        val adapter = RecyclerviewAdapter(mData)
        mData[8].singleDisplayRightContent = UnSerializeDataBase.idCardName
        mData[9].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val styleTypeNum = arrayListOf(10)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(1,2,7,4,5,6,10)//1,2,7true为按钮清册不填可以发布 //其余true为必选项
        for(j in necessaryNum){
            adapter.mData[j].necessary= true
        }
        mData[1].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 1
            mdata.putString("type","成员清册发布")
            mdata.putSerializable("inventory",mData[1].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[2].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected = 2
            mdata.putString("type","车辆清册发布")
            mdata.putSerializable("inventory",mData[2].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        mData[7].buttonListener= arrayListOf( View.OnClickListener {
            mData[0].selected =7
            mdata.putString("type","工器具清册发布")
            mdata.putSerializable("inventory",mData[7].itemMultiStyleItem as Serializable)
            FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
        })
        adapter.urlPath = Constants.HttpUrlPath.Provider.RunningMaintain
        return adapter
    }

    //租赁服务——车辆租赁
    fun VehicleRental(): RecyclerviewAdapter {
        val frame = if(activity is SupplyActivity) R.id.frame_supply else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        val mdata = Bundle()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Provider/RentalService/VehicleRental/Information entry.json")
        val adapter = RecyclerviewAdapter(mData)
        mData[12].singleDisplayRightContent = UnSerializeDataBase.idCardName
        mData[13].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val styleTypeNum = arrayListOf(3,15)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(0,1,2,3,4,5,7,8,10,11,15)//true为必选项
        for(j in necessaryNum){
            adapter.mData[j].necessary= true
        }
        adapter.mData[9].jumpListener = View.OnClickListener {
            val data = Bundle()
            data.putString("key", mData[9].key)
            data.putString("title", mData[9].shiftInputTitle)
            FragmentHelper.switchFragment(activity,ImageFragment.newInstance(data), frame, "Capture")
        }
        return adapter
    }

    //租赁服务——工器具租赁
    fun EquipmentLeasing(bundle: Bundle): RecyclerviewAdapter {
        val frame = if(activity is SupplyActivity) R.id.frame_supply else R.id.frame_my_release
        val mdata = Bundle()
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Provider/RentalService/Equipment Leasing/Information entry.json")
        val adapter = RecyclerviewAdapter(mData)
        mData[12].singleDisplayRightContent = UnSerializeDataBase.idCardName
        mData[13].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val styleTypeNum = arrayListOf(15)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(8,1,3,10,11,15)//8 true为按钮清册不填可以发布 //其余true为必选项
        for(j in necessaryNum){
            adapter.mData[j].necessary= true
        }
        when(bundle.getInt("type"))
        {
            Constants.FragmentType.TOOL_LEASING_TYPE.ordinal->{
                mData[8].singleDisplayRightTitle="工器具租赁清单"
                mData[8].buttonListener= arrayListOf( View.OnClickListener {
                    mData[0].selected =8
                    mdata.putString("type","租赁清册发布")
                    mdata.putSerializable("inventory",mData[8].itemMultiStyleItem as Serializable)
                    FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
                })
            }
            Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal->{
                mData[8].singleDisplayRightTitle="机械租赁清单"
                mData[8].buttonListener= arrayListOf( View.OnClickListener {
                    mData[0].selected =8
                    mdata.putString("type","租赁清册发布")
                    mdata.putSerializable("inventory",mData[8].itemMultiStyleItem as Serializable)
                    FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
                })
            }
            Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal->{
                mData[8].singleDisplayRightTitle="设备租赁清单"
                mData[8].buttonListener= arrayListOf( View.OnClickListener {
                    mData[0].selected =8
                    mdata.putString("type","租赁清册发布")
                    mdata.putSerializable("inventory",mData[8].itemMultiStyleItem as Serializable)
                    FragmentHelper.switchFragment(activity,SupplyPublishInventoryFragment.newInstance(mdata), frame, "publishInventory")
                })
            }
        }
//        val uploadContractButtonListListeners: MutableList<View.OnClickListener> = ArrayList()
//        val uploadInventoryButtonListListeners: MutableList<View.OnClickListener> = ArrayList()
//        val downloadContractListener = View.OnClickListener {
//            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
//                .setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(activity.fragmentManager)
//                .withMemoryBar(true).build()
//            chooser.show()
//            chooser.setOnSelectListener { selectContent ->
//                downloadFile(selectContent, "工器具出租模板", "工器具出租模板", "")
//            }
//        }
//        val uploadContractListener = View.OnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "*/*"
//            UnSerializeDataBase.fileList.add(FileMap("", mData[9].additionalKey))
//            activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
//        }
//        //租赁合同
//
//          uploadContractButtonListListeners.add(downloadContractListener)
//        uploadContractButtonListListeners.add(uploadContractListener)

        mData[6].jumpListener = View.OnClickListener {
            val data = Bundle()
            data.putString("key", mData[6].key)
            FragmentHelper.switchFragment(activity,UpIdCardFragment.newInstance(data), frame, "Capture")
        }
        mData[7].jumpListener = View.OnClickListener {
            val data = Bundle()
            data.putString("key", mData[7].key)
            data.putInt("total", 1)
            FragmentHelper.switchFragment(activity,UploadPhoneFragment.newInstance(data), frame, "Capture")
        }
        mData[9].jumpListener = View.OnClickListener {
            val data = Bundle()
            data.putString("key", mData[9].key)
            data.putString("title", mData[9].shiftInputTitle)
            FragmentHelper.switchFragment(activity,ImageFragment.newInstance(data), frame, "Capture")
        }
//        mData[9].buttonListener = uploadContractButtonListListeners
        return adapter
    }

    //三方服务
    fun ServiceInformationEntry(): RecyclerviewAdapter {
        val frame = if(activity is SupplyActivity) R.id.frame_supply else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Provider/TripartiteServices/Information entry.json")
        val adapter = RecyclerviewAdapter(mData)
        mData[8].singleDisplayRightContent = UnSerializeDataBase.idCardName
        mData[9].singleDisplayRightContent = UnSerializeDataBase.userPhone
        val styleTypeNum = arrayListOf(12)
        for(i in styleTypeNum){
            adapter.mData[i].styleType="NUMBER"//限制为数字类型
        }
        val necessaryNum = arrayListOf(0,1,3,4,6,7,12)//余true为必选项
        for(j in necessaryNum){
            adapter.mData[j].necessary= true
        }

//        mData[0].backListener = View.OnClickListener {
//            activity.finish()
//            UnSerializeDataBase.imgList.clear()
//            UnSerializeDataBase.fileList.clear()
//        }
        mData[6].jumpListener = View.OnClickListener {
            val data = Bundle()
            data.putString("key", mData[6].key)
            FragmentHelper.switchFragment(activity,UpIdCardFragment.newInstance(data),frame,"Capture")
        }
        mData[7].jumpListener = View.OnClickListener {
            val data = Bundle()
            data.putString("key", mData[7].key)
            data.putString("title", mData[7].shiftInputTitle)
            FragmentHelper.switchFragment(activity,ImageFragment.newInstance(data),frame,"Capture")
        }
        mData[10].jumpListener = View.OnClickListener {
            val data = Bundle()
            data.putString("key", mData[10].key)
            data.putString("title", mData[10].shiftInputTitle)
            FragmentHelper.switchFragment(activity,ImageFragment.newInstance(data),frame,"Capture")
        }
        adapter.urlPath = Constants.HttpUrlPath.Provider.ThirdServices
        return adapter
    }


    fun ProviderGroupPersonAdd(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        lateinit var adapter: RecyclerviewAdapter
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Provider/ProviderGroupPersonAdd.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        mData[8].jumpListener = View.OnClickListener {
            val data = Bundle()
            data.putString("key", "CarPicture8")
            (activity as SupplyActivity).switchFragment(
                UploadPhoneFragment.newInstance(data),
                R.id.frame_supply,
                "Capture"
            )
        }
        adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    fun ProviderGroupVehicleAdd(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        val multiButtonListeners: MutableList<View.OnClickListener> = ArrayList()
        lateinit var adapter: RecyclerviewAdapter
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Provider/ProviderGroupVehicleAdd.json")
        activity.supportFragmentManager.popBackStackImmediate()
        mData[10].jumpListener = View.OnClickListener {
            val data = Bundle()
            data.putString("key", "CarPicture10")
            (activity as SupplyActivity).switchFragment(
                UploadPhoneFragment.newInstance(data),
                R.id.frame_supply,
                "Capture"
            )
        }
        adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    ////////
     fun SupplyPublishDetailList(bundle: Bundle):RecyclerviewAdapter{//供应清册发布
        val frame = if(activity is SupplyActivity) R.id.frame_supply else R.id.frame_my_release
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        lateinit var mData:List<MultiStyleItem>
        lateinit var adapter: RecyclerviewAdapter
        when(bundle.getString("type"))
        {
            //成员清册发布
            "成员清册发布"->{
                mData = itemGenerate.getJsonFromAsset("SupplyPublish/ApplicationSubmitMemberList.json")
                adapter = RecyclerviewAdapter(mData)
                val styleTypeNum = arrayListOf(2,4)
                for(i in styleTypeNum){
                    adapter.mData[i].styleType="NUMBER"//限制为数字类型
                }
                val necessaryNum = arrayListOf(0,1,2,3,4,5)
                for(j in necessaryNum){
                    adapter.mData[j].necessary= true//true为必选项
                }
            }
            "车辆清册发布"->{
                mData = itemGenerate.getJsonFromAsset("SupplyPublish/ApplicationSubmitVehicleList.json")
                adapter = RecyclerviewAdapter(mData)
                val styleTypeNum = arrayListOf(3,4,5)
                for(i in styleTypeNum){
                    adapter.mData[i].styleType="NUMBER"//限制为数字类型
                }
                val necessaryNum = arrayListOf(0,1,2,3,4,5,6,8)
                for(j in necessaryNum){
                    adapter.mData[j].necessary= true//true为必选项
                }
                adapter.mData[7].jumpListener = View.OnClickListener {
                    val data = Bundle()
                    data.putString("key", mData[7].key)
                    data.putString("title", mData[7].shiftInputTitle)
                    FragmentHelper.switchFragment(activity,ImageFragment.newInstance(data), frame, "Capture")
                }
            }
            "工器具清册发布"->{
                mData = itemGenerate.getJsonFromAsset("SupplyPublish/ApplicationSubmitToolList.json")
                adapter = RecyclerviewAdapter(mData)
                val styleTypeNum = arrayListOf(3)
                for(i in styleTypeNum){
                    adapter.mData[i].styleType="NUMBER"//限制为数字类型
                }
                val necessaryNum = arrayListOf(0,1,2,3)
                for(j in necessaryNum){
                    adapter.mData[j].necessary= true//true为必选项
                }
            }
            "租赁清册发布"->{
                mData = itemGenerate.getJsonFromAsset("SupplyPublish/ApplicationSubmitToolLeaseList.json")
                adapter = RecyclerviewAdapter(mData)
                val styleTypeNum = arrayListOf(3)
                for(i in styleTypeNum){
                    adapter.mData[i].styleType="NUMBER"//限制为数字类型
                }
                val necessaryNum = arrayListOf(0,1,2,3)
                for(j in necessaryNum){
                    adapter.mData[j].necessary= true//true为必选项
                }
            }

        }
        return adapter
//            "成员清册发布"->{
//                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitMemberList.json")
//                adapter = RecyclerviewAdapter(mData)
//                adapter.mData[0].backListener = View.OnClickListener {
//                    activity.supportFragmentManager.popBackStackImmediate()
//                }
//                adapter.mData[0].tvSelectListener = View.OnClickListener {
//                    // Log.i("fragment is ok?",(activity.supportFragmentManager.findFragmentByTag("inventory") is InventoryFragment).toString())
//                    val fragment = activity.supportFragmentManager.findFragmentByTag("inventory") as SupplyPublishInventoryFragment
//                    activity.supportFragmentManager.popBackStackImmediate()
//                    fragment.update(adapter.mData)
//                }
        //   }

//            //机械清册详情
//            "机械清册"->{
//                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitMachineList.json")
//                adapter = RecyclerviewAdapter(mData)
//                adapter.mData[0].backListener = View.OnClickListener {
//                    activity.supportFragmentManager.popBackStackImmediate()
//                }
//                adapter.mData[0].tvSelectListener = View.OnClickListener {
//                    // Log.i("fragment is ok?",(activity.supportFragmentManager.findFragmentByTag("inventory") is InventoryFragment).toString())
//                    val fragment = activity.supportFragmentManager.findFragmentByTag("inventory") as SubmitInventoryFragment
//                    activity.supportFragmentManager.popBackStackImmediate()
//                    fragment.update(adapter.mData)
//                }
//            }
//            //清工薪资清册详情
//            "清工薪资清册"->{
//                //lateinit var listData1:List<requirementTeamProjectList>
//                //lateinit var listData2:List<requirementPowerTransformationSalary>
//                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitCleanWorkList.json")
//                adapter = RecyclerviewAdapter(mData)
//                adapter.mData[0].backListener = View.OnClickListener {
//                    activity.supportFragmentManager.popBackStackImmediate()
//                }
//                adapter.mData[0].tvSelectListener = View.OnClickListener {
//                    // Log.i("fragment is ok?",(activity.supportFragmentManager.findFragmentByTag("inventory") is InventoryFragment).toString())
//                    val fragment = activity.supportFragmentManager.findFragmentByTag("inventory") as SubmitInventoryFragment
//                    activity.supportFragmentManager.popBackStackImmediate()
//                    fragment.update(adapter.mData)
//                }
//            }
//            //清单报价清册详情
//            "清单报价清册"->{
//                lateinit var list:requirementTeamProjectList
//                list=bundle.getSerializable("list") as requirementTeamProjectList
//                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitCleanSingleList.json")
//                adapter = RecyclerviewAdapter(mData)
//                adapter.mData[0].backListener = View.OnClickListener {
//                    activity.supportFragmentManager.popBackStackImmediate()
//                }
//                adapter.mData[0].tvSelectListener = View.OnClickListener {
//                    // Log.i("fragment is ok?",(activity.supportFragmentManager.findFragmentByTag("inventory") is InventoryFragment).toString())
//                    val fragment =
//                        activity.supportFragmentManager.findFragmentByTag("inventory") as SubmitInventoryFragment
//                    activity.supportFragmentManager.popBackStackImmediate()
//                    fragment.update(adapter.mData)
//                }
//                adapter.mData[1].singleDisplayRightContent = if (list.projectName == null) {
//                    "无数据"
//                } else {
//                    list.projectName
//                }
//                adapter.mData[2].singleDisplayRightContent =
//                    if (list.specificationsModels == null) {
//                        "无数据"
//                    } else {
//                        list.specificationsModels
//                    }
//                adapter.mData[3].singleDisplayRightContent = if (list.quantity == null) {
//                    "无数据"
//                } else {
//                    list.quantity
//                }
//                adapter.mData[4].singleDisplayRightContent = if (list.units == null) {
//                    "无数据"
//                } else {
//                    list.units
//                }
//                adapter.mData[6].singleDisplayRightContent = if (list.detailsExplain == null) {
//                    "无数据"
//                } else {
//                    list.detailsExplain
//                }
//            }

    }

    /*
    *   专业模块
    */
    // alterPosition time:2019/7/15
    // function:/* 架空更多 */
    fun ProfessionalOverheadMore(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverheadMore.json")
        mData[0].backListener = View.OnClickListener {
            activity.finish()
        }
        for (j in 15 until mData.size-1) {
            mData[j].jumpListener = View.OnClickListener {
                val data = Bundle()
                data.putString("title", mData[j].shiftInputTitle)
                (activity as ProfessionalActivity).switchFragment(ProjectImageCheckFragment.newInstance(data), "fileManager")
            }
        }
        mData[mData.size-1].jumpListener = View.OnClickListener {
            val data = Bundle()
            data.putInt("type", getType(mData[mData.size-1].shiftInputTitle))
            (activity as ProfessionalActivity).switchFragment(ProjectMoreFragment.newInstance(data), "")
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    /*
     * 架空
     */
    //经济指标
    fun OverHeadEconomicIndicator(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/economicIndicator.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        //土质比例
        mData[9].fourDisplayListener = View.OnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setNegativeButton("返回", null)
            builder.setCancelable(true)

            val view = View.inflate(context, R.layout.dialog_soil_ratio, null)

            view.rv_soil_ratio_content.adapter = SoilRatio()
            view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
        }
        //地形比例
        mData[10].fourDisplayListener = View.OnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setNegativeButton("返回", null)
            builder.setCancelable(true)

            val view = View.inflate(context, R.layout.dialog_soil_ratio, null)

            view.rv_soil_ratio_content.adapter = TerrainRatio()
            view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
        }
        //跨越
        mData[11].fourDisplayListener = View.OnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setNegativeButton("返回", null)
            builder.setCancelable(true)

            val view = View.inflate(context, R.layout.dialog_soil_ratio, null)

            view.rv_soil_ratio_content.adapter = TwentyCross()
            view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //土质比例
    fun SoilRatio(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Public/soilRatio.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //节点基础开挖 土质比例
    fun NodeSoilRatio(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Public/NodeSoilRatio.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //地形比例
    fun TerrainRatio(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        var mData = itemGenerate.getJsonFromAsset("Professional/Public/terrainRatio.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //跨越
    fun TwentyCross(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Public/cross.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //规格型号
    fun SpecificationsModels(shape: String): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val path = if (shape == "圆形") {
            "Professional/Public/specificationsModelscircular.json"
        } else {
            "Professional/Public/specificationsModelssquare.json"
        }
        val mData = itemGenerate.getJsonFromAsset(path)
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //回填物比例
    fun BackfillProportion(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Public/backfillProportion.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //材料清册

    //耐张段
    fun OverHeadTensileSegment(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData: MutableList<MultiStyleItem> = ArrayList()
        mData.add(MultiStyleItem(MultiStyleItem.Options.TITLE, "耐张段"))
        mData[0].styleType = "0"
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 0 until 2) {
            val expandList =
                itemGenerate.getJsonFromAsset("Professional/OverHead/tensileSegment/tensileSegmentSon.json")
            //从后台拿数据 只需更改此处逻辑
            for (k in 0 until expandList.size - 1) {
                //expandList[k].singleDisplayLeftContent = "x"
            }
            expandList[4].jumpListener = View.OnClickListener {
                val data = Bundle()
                data.putString("key", "CarPicture")
                (activity as ProfessionalActivity).switchFragment(UploadPhoneFragment.newInstance(data), "Capture")
            }
            //
            val expandListAdapter = RecyclerviewAdapter(expandList)
            mData.add(MultiStyleItem(MultiStyleItem.Options.EXPAND_LIST, "" + (j + 1) + "#耐张段", "0", expandListAdapter))
        }

        val adapter = RecyclerviewAdapter(mData.toList())
        return adapter
    }

    //杆塔参数
    fun OverHeadTowerParameters(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/towerParameters.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 0 until 2) {
            val multiStyleItem =
                MultiStyleItem(MultiStyleItem.Options.FIVE_DISPLAY, "A" + (j + 1), "150锥形电杆", "30米", "拉线门型塔", "···")
            multiStyleItem.fiveDisplayListener = View.OnClickListener {
                val data = Bundle()
                data.putInt("type", AdapterGenerate().getType("杆塔子项"))
                (activity as ProfessionalActivity).switchFragment(ProjectMoreFragment.newInstance(data), "")
            }
            mData.add(multiStyleItem)
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //公共点定位
    fun OverHeadPublicPointPosition(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/PublicPointPosition.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        mData[0].positionSubitemsRow = arrayListOf(1, 3, 5, 7, 9, mData.size)
        val adapter = RecyclerviewAdapter(mData)
        for (j in 0 until adapter.mData[0].positionSubitemsRow.size - 1) {
            val position = adapter.mData[0].positionSubitemsRow[j] + 1
            adapter.mData[position].positionListener = View.OnClickListener {
                LocationHelper(context, AMapLocationListener {
                    adapter.mData[position].inputPositionLatitude = it.latitude
                    adapter.mData[position].inputPositionLongitude = it.longitude
                    adapter.mData[position].inputPositionContent = it.address
                    adapter.notifyItemChanged(position)
                })
            }
        }
        adapter.mData[adapter.mData[0].positionSubitemsRow[0]].positionAdd = View.OnClickListener {
            val data = adapter.mData.toMutableList()
            val item = MultiStyleItem(MultiStyleItem.Options.POSITION_DELETE, "", true)
            val position = adapter.mData[0].positionSubitemsRow[1]
            data.add(position, item)
            for (i in 0 until data[0].positionSubitemsRow.size)
                data[0].positionSubitemsRow[i]++
            adapter.mData = data
            adapter.mData[position].positionListener = View.OnClickListener {
                LocationHelper(context, AMapLocationListener {
                    adapter.mData[position].inputPositionLatitude = it.latitude
                    adapter.mData[position].inputPositionLongitude = it.longitude
                    adapter.mData[position].inputPositionContent = it.address
                    adapter.notifyItemChanged(position)
                })
            }
            adapter.notifyItemRangeInserted(position, adapter.itemCount)
        }
        adapter.mData[adapter.mData[0].positionSubitemsRow[1]].positionAdd = View.OnClickListener {
            val data = adapter.mData.toMutableList()
            val item = MultiStyleItem(MultiStyleItem.Options.POSITION_DELETE, "", true)
            val position = adapter.mData[0].positionSubitemsRow[2]
            data.add(position, item)
            for (i in 1 until data[0].positionSubitemsRow.size)
                data[0].positionSubitemsRow[i]++
            adapter.mData = data
            adapter.mData[position].positionListener = View.OnClickListener {
                LocationHelper(context, AMapLocationListener {
                    adapter.mData[position].inputPositionLatitude = it.latitude
                    adapter.mData[position].inputPositionLongitude = it.longitude
                    adapter.mData[position].inputPositionContent = it.address
                    adapter.notifyItemChanged(position)
                })
            }
            adapter.notifyItemRangeInserted(position, adapter.itemCount)
        }
        for (j in 2 until adapter.mData[0].positionSubitemsRow.size-1){
            adapter.mData[adapter.mData[0].positionSubitemsRow[j]].positionAdd = View.OnClickListener {
                val data = adapter.mData.toMutableList()
                val item = MultiStyleItem(MultiStyleItem.Options.POSITION_START_END,"")
                val position = adapter.mData[0].positionSubitemsRow[j+1]
                data.add(position,item)
                for (i in j+1 until data[0].positionSubitemsRow.size)
                    data[0].positionSubitemsRow[i]++
                adapter.mData = data
                adapter.mData[position].positionListener = View.OnClickListener {
                    LocationHelper(context, AMapLocationListener {
                        adapter.mData[position].inputPositionLatitude = it.latitude
                        adapter.mData[position].inputPositionLongitude = it.longitude
                        adapter.mData[position].inputPositionContent = it.address
                        adapter.notifyItemChanged(position)
                    })
                }
                adapter.notifyItemRangeInserted(position,adapter.itemCount)
            }
        }
        return adapter
    }

    //杆塔子项
    fun OverHeadTowerSubitem(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/towerSubitem.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 4 until mData.size) {
            mData[j].jumpListener = View.OnClickListener {
                val data = Bundle()
                data.putInt("type", AdapterGenerate().getType(mData[0].title1 + " " + mData[j].shiftInputTitle))
                (activity as ProfessionalActivity).switchFragment(ProjectMoreFragment.newInstance(data), "")
            }
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    /*
     *杆塔子项--子项
     */

    //复测分坑
    fun OverHeadResurveyPitdividing(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/resurveyPitdividing.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[4].positionListener = View.OnClickListener {
            LocationHelper(context, AMapLocationListener {
                adapter.mData[4].inputPositionLatitude = it.latitude
                adapter.mData[4].inputPositionLongitude = it.longitude
                adapter.mData[4].inputPositionContent = it.address
                adapter.notifyItemChanged(4)
//                adapter.onBindViewHolder(adapter.VHList.get(4), 4)
            })
        }
        adapter.mData[7].positionListener = View.OnClickListener {
            LocationHelper(context, AMapLocationListener {
                adapter.mData[7].inputPositionLatitude = it.latitude
                adapter.mData[7].inputPositionLongitude = it.longitude
                val startLatlng =
                    LatLng(adapter.mData[4].inputPositionLatitude, adapter.mData[4].inputPositionLongitude)
                val endLatlng = LatLng(adapter.mData[7].inputPositionLatitude, adapter.mData[7].inputPositionLongitude)
                adapter.mData[7].inputDistancePositionContent =
                    (AMapUtils.calculateLineDistance(startLatlng, endLatlng) / 1000).toString()
                adapter.notifyItemChanged(7)
//                adapter.onBindViewHolder(adapter.VHList.get(4), 4)
            })
        }
        adapter.mData[8].positionListener = View.OnClickListener {
            LocationHelper(context, AMapLocationListener {
                adapter.mData[8].inputPositionLatitude = it.latitude
                adapter.mData[8].inputPositionLongitude = it.longitude
                val startLatlng =
                    LatLng(adapter.mData[4].inputPositionLatitude, adapter.mData[4].inputPositionLongitude)
                val endLatlng = LatLng(adapter.mData[8].inputPositionLatitude, adapter.mData[7].inputPositionLongitude)
                adapter.mData[8].inputDistancePositionContent =
                    (AMapUtils.calculateLineDistance(startLatlng, endLatlng) / 1000).toString()
                adapter.notifyItemChanged(8)
//                adapter.onBindViewHolder(adapter.VHList.get(4), 4)
            })
        }
        return adapter
    }

    //基础开挖
    fun OverHeadFoundationExcavation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/OverHead/foundationExcavation/foundationExcavation.json")
                .toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        /*for (j in 0 until 2){
            var path = ""
            if(){
                path="Professional/OverHead/foundationExcavation/foundationExcavationSon1.json",
  }else if{
                path="Professional/OverHead/foundationExcavation/foundationExcavationSon2.json"
            }else{
                path="Professional/OverHead/foundationExcavation/foundationExcavationSon3.json"
            }

            val expandList = itemGenerate.getJsonFromAsset("path")
            //从后台拿数据 只需更改此处逻辑
            for (k in 0 until expandList.size-1){
                //expandList[k].singleDisplayLeftContent = "x"
            }
            //
            val expandListAdapter=RecyclerviewAdapter(expandList)
            mData.add(MultiStyleItem(MultiStyleItem.Options.EXPAND_LIST,"塑料制品",expandListAdapter))
        }*/
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //材料运输
    fun OverHeadMaterialTransportation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/towerTransportation/towerTransportation.json")
            .toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //预制件填埋
    fun OverHeadPrefabricatedLandfill(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/OverHead/prefabricatedLandfill/prefabricatedLandfill.json")
                .toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //基础浇筑
    fun OverHeadFoundationPouring(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/foundationPouring/foundationPouring.json")
            .toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 0 until 2) {
            val expandList =
                itemGenerate.getJsonFromAsset("Professional/OverHead/foundationPouring/foundationPouringSon1.json")
            //从后台拿数据 只需更改此处逻辑
            for (k in 0 until expandList.size - 1) {
                //expandList[k].singleDisplayLeftContent = "x"
            }
            //
            val expandListAdapter = RecyclerviewAdapter(expandList)
            mData.add(MultiStyleItem(MultiStyleItem.Options.EXPAND_LIST, "现浇基础", "0", expandListAdapter))
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //杆塔组立
    fun OverHeadTowerErection(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/OverHead/towerErection/towerErection.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //拉线制作
    fun OverHeadWireDrawing(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/wireDrawing/wireDrawing.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //横担安装
    fun OverHeadCrossbarInstallation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/OverHead/crossbarInstallation/crossbarInstallation.json")
                .toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //绝缘子安装
    fun OverHeadInsulationInstallation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/OverHead/insulatorInstallation/insulatorInstallation.json")
                .toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //焊接制作
    fun OverHeadWeldingFabrication(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/weldingFabrication/weldingFabrication.json")
            .toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //导线架设
    fun OverHeadWireErection(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/OverHead/wireErection/wireErection.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 0 until 2) {
            val expandList = itemGenerate.getJsonFromAsset("Professional/OverHead/wireErection/wireErectionSon.json")
            //从后台拿数据 只需更改此处逻辑
            for (k in 0 until expandList.size - 1) {
                //expandList[k].singleDisplayLeftContent = "x"
            }
            //
            val expandListAdapter = RecyclerviewAdapter(expandList)
            mData.add(MultiStyleItem(MultiStyleItem.Options.EXPAND_LIST, "" + (j + 1) + "#耐张段", "0", expandListAdapter))
        }
        val adapter = RecyclerviewAdapter(mData.toList())
        return adapter
    }

    //附件安装
    fun OverHeadAccessoriesInstalling(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/OverHead/accessoriesInstalling/accessoriesInstalling.json")
                .toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //设备安装
    fun OverHeadEquipmentInstallation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/OverHead/equipmentInstallation/equipmentInstallation.json")
                .toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //接地敷设
    fun OverHeadGroundLaying(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/OverHead/groundLaying/groundLaying.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
            UnSerializeDataBase.imgList.clear()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //户表安装
    fun OverHeadTableInstallation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/tableInstallation/tableInstallation.json")
            .toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //带电作业
    fun OverHeadLiveWork(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/OverHead/liveWork/liveWork.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //项目盘
    fun ProfessionalProjectDisk(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/ProjectDisk.json").toMutableList()

        mData[2].tvExpandTitleListener = View.OnClickListener {

            val intent = Intent(activity, ProfessionalActivity::class.java)
            intent.putExtra("type", getType(mData[2].expandTitle))
            activity.startActivity(intent)
        }
        mData[0].backListener = View.OnClickListener {
            activity.finish()
        }
        mData[0].tvSelectListener = View.OnClickListener {
            XPopup.Builder(context)
                .atView(it)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                .asAttachList(
                    arrayOf("新建项目盘", "动态汇总表", "项目清册"),
                    null
                )
                { position, text ->
                    run {
                        val intent = Intent(activity, ProfessionalActivity::class.java)
                        intent.putExtra("type", text)
                        activity.startActivity(intent)
                    }
                }.show()
        }
        val adapter = RecyclerviewAdapter(mData)

        return adapter
    }

    /*
     * 节点
     */
    //经济指标
    fun NodeEconomicIndicator(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/economicIndicator.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //节点参数
    fun NodeParameters(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/nodeParameters.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        mData.add(MultiStyleItem(MultiStyleItem.Options.FOUR_DISPLAY, "A1", "工井", "直线井", "···"))
        mData[mData.size - 1].fourDisplayListener = View.OnClickListener {
            val data = Bundle()
            data.putInt("type", AdapterGenerate().getType("节点子项"))
            (activity as ProfessionalActivity).switchFragment(ProjectMoreFragment.newInstance(data), "")
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //节点子项
    fun NodeSubitems(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/nodeSubitems.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    /*
     *节点子项--子项
     */

    //复测分坑
    fun NodeResurveyPitdividing(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/resurveyPitdividing.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[3].positionListener = View.OnClickListener {
            LocationHelper(context, AMapLocationListener {
                adapter.mData[3].inputPositionLatitude = it.latitude
                adapter.mData[3].inputPositionLongitude = it.longitude
                adapter.mData[3].inputPositionContent = it.address
                adapter.notifyItemChanged(3)
//                adapter.onBindViewHolder(adapter.VHList.get(4), 4)
            })
        }
        adapter.mData[6].positionListener = View.OnClickListener {
            LocationHelper(context, AMapLocationListener {
                adapter.mData[6].inputPositionLatitude = it.latitude
                adapter.mData[6].inputPositionLongitude = it.longitude
                val startLatlng =
                    LatLng(adapter.mData[3].inputPositionLatitude, adapter.mData[3].inputPositionLongitude)
                val endLatlng = LatLng(adapter.mData[6].inputPositionLatitude, adapter.mData[6].inputPositionLongitude)
                adapter.mData[6].inputDistancePositionContent =
                    (AMapUtils.calculateLineDistance(startLatlng, endLatlng) / 1000).toString()
                adapter.notifyItemChanged(6)
//                adapter.onBindViewHolder(adapter.VHList.get(4), 4)
            })
        }
        adapter.mData[12].positionAdd = View.OnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setNegativeButton("返回", null)
            builder.setCancelable(true)
            val view = View.inflate(context, R.layout.dialog_input, null)
            var data: MutableList<MultiStyleItem> = ArrayList()
            data.clear()
            val selectOption1Items = arrayListOf("燃气管道", "自来水管", "通讯管道", "污水管道", "电力管道", "燃油管道", "暖气管道", "自定义")
            data.add(MultiStyleItem(MultiStyleItem.Options.SELECT_DIALOG, selectOption1Items, "添加内容"))
            data.add(MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_UNIT, "水平距离", "(m)"))
            data.add(MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_UNIT, "垂直距离", "(m)"))
            view.rv_input_content.adapter = RecyclerviewAdapter(data)
            view.rv_input_content.layoutManager = LinearLayoutManager(view.context)
            builder.setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                var mdata = adapter.mData.toMutableList()
                var item = MultiStyleItem(
                    MultiStyleItem.Options.FOUR_DISPLAY,
                    data[0].selectContent,
                    "${data[1].inputUnitContent}m",
                    "${data[2].inputUnitContent}m",
                    "···"
                )
                val oldSize = adapter.mData.size
                mdata.add(oldSize - 1, item)
                adapter.mData = mdata
                adapter.mData[adapter.mData.size - 2].fourDisplayListener = View.OnClickListener {
                    val values: Array<String> = arrayOf("修改", "删除")
                    val alertBuilder = AlertDialog.Builder(context)
                    alertBuilder.setItems(values, DialogInterface.OnClickListener { dialogInterface, i ->
                        if (values[i] == "删除") {
                            val position = adapter.mData.indexOf(item)
                            mdata = adapter.mData.toMutableList()
                            mdata.removeAt(position)
                            adapter.mData = mdata
                            adapter.notifyItemRangeRemoved(position, adapter.mData.size - position)
                        } else if (values[i] == "修改") {
                            val builder = AlertDialog.Builder(context)
                            builder.setNegativeButton("返回", null)
                            builder.setCancelable(true)
                            val view = View.inflate(context, R.layout.dialog_input, null)
                            var data: MutableList<MultiStyleItem> = ArrayList()
                            data.clear()
                            val selectContent = adapter.mData[adapter.mData.size - 2].fourDisplayTitle
                            val horizontalDistance = adapter.mData[adapter.mData.size - 2].fourDisplayContent1
                            val verticalDistance = adapter.mData[adapter.mData.size - 2].fourDisplayContent2
                            val selectOption1Items =
                                arrayListOf("燃气管道", "自来水管", "通讯管道", "污水管道", "电力管道", "燃油管道", "暖气管道", "自定义")
                            data.add(MultiStyleItem(MultiStyleItem.Options.SELECT_DIALOG, selectOption1Items, "添加内容"))
                            data[data.size - 1].selectContent = selectContent
                            data.add(MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_UNIT, "水平距离", "(m)"))
                            data[data.size - 1].inputUnitContent = horizontalDistance.replace("m", "")
                            data.add(MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_UNIT, "垂直距离", "(m)"))
                            data[data.size - 1].inputUnitContent = verticalDistance.replace("m", "")
                            var mAdapter = RecyclerviewAdapter(data)
                            view.rv_input_content.adapter = mAdapter
                            view.rv_input_content.layoutManager = LinearLayoutManager(view.context)
                            builder.setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface, i ->
                                adapter.mData[adapter.mData.indexOf(item)].fourDisplayTitle = data[0].selectContent
                                adapter.mData[adapter.mData.indexOf(item)].fourDisplayContent1 =
                                    "${data[1].inputUnitContent}m"
                                adapter.mData[adapter.mData.indexOf(item)].fourDisplayContent2 =
                                    "${data[2].inputUnitContent}m"
                                adapter.notifyItemChanged(adapter.mData.indexOf(item))
                            })
                            builder.setView(view)
                            val dialog = builder.create()
                            dialog.show()
                            dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                            dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                        }
                    }).show()
                }
                adapter.notifyItemInserted(mdata.size - 1)  //强制刷新
//                    adapter.onBindViewHolder(adapter.VHList.get(j), j)
                //adapter.notifyDataSetChanged()
                //adapter.notifyItemRangeChanged(j,2)
            })
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }
        return adapter
    }

    //基础开挖
    fun NodeFoundationExcavation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/foundationExcavation/foundationExcavation.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //材料运输
    fun NodeMaterialTransportation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/Node/materialTransportation/materialTransportation.json")
                .toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 0 until 2) {
            val expandList =
                itemGenerate.getJsonFromAsset("Professional/Node/materialTransportation/materialTransportationSon1.json")
            //从后台拿数据 只需更改此处逻辑
            for (k in 0 until expandList.size - 1) {
                //expandList[k].singleDisplayLeftContent = "x"
            }
            //
            expandList[5].jumpListener = View.OnClickListener {
                val data = Bundle()
                data.putString("key", "CarPicture")
                (activity as ProfessionalActivity).switchFragment(UploadPhoneFragment.newInstance(data), "Capture")
            }
            val expandListAdapter = RecyclerviewAdapter(expandList)
            mData.add(MultiStyleItem(MultiStyleItem.Options.EXPAND_LIST, "塑料制品", "0", expandListAdapter))
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //预制构件制作及安装
    fun NodeManufactureInstallation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/Node/manufactureInstallation/manufactureInstallation.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
            UnSerializeDataBase.imgList.clear()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //基础浇筑
    fun NodeFoundationPouring(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/foundationPouring/foundationPouring.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //设备安装
    fun NodeEquipmentInstallation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/equipmentInstallation.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //接地敷设
    fun NodeGroundLaying(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/Node/groundingLaying/groundingLaying.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //户表安装
    fun NodeTableInstallation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Node/tableInstallation.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
            UnSerializeDataBase.imgList.clear()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    /*
     * 通道
     */
    //经济指标
    fun PassagewayEconomicIndicator(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/economicIndicator.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        //土质比例
        mData[8].fourDisplayListener = View.OnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setNegativeButton("返回", null)
            builder.setCancelable(true)

            val view = View.inflate(context, R.layout.dialog_soil_ratio, null)

            view.rv_soil_ratio_content.adapter = SoilRatio()
            view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
        }
        //地形比例
        mData[9].fourDisplayListener = View.OnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setNegativeButton("返回", null)
            builder.setCancelable(true)

            val view = View.inflate(context, R.layout.dialog_soil_ratio, null)

            view.rv_soil_ratio_content.adapter = TerrainRatio()
            view.rv_soil_ratio_content.layoutManager = LinearLayoutManager(view.context)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //通道参数
    fun ChannelParameters(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/channelParameters.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        mData.add(MultiStyleItem(MultiStyleItem.Options.FIVE_DISPLAY, "A1", "22", "11", "直埋", "···"))
        mData[mData.size - 1].fiveDisplayListener = View.OnClickListener {
            val data = Bundle()
            data.putInt("type", AdapterGenerate().getType("通道子项"))
            (activity as ProfessionalActivity).switchFragment(ProjectMoreFragment.newInstance(data), "")
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //通道子项
    fun ChannelSubitems(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/channelSubitems.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 4 until mData.size) {
            mData[j].jumpListener = View.OnClickListener {
                val data = Bundle()
                data.putInt("type", getType(mData[0].title1 + " " + mData[j].shiftInputTitle))
                (activity as ProfessionalActivity).switchFragment(ProjectMoreFragment.newInstance(data), "")
            }
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    /*
     *通道子项--子项
     */
    //复测分坑
    fun PassagewayResurveyPitdividing(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/resurveyPitdividing.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[3].positionListener = View.OnClickListener {
            LocationHelper(context, AMapLocationListener {
                adapter.mData[3].inputPositionLatitude = it.latitude
                adapter.mData[3].inputPositionLongitude = it.longitude
                adapter.mData[3].inputPositionContent = it.address
                adapter.notifyItemChanged(3)
//                adapter.onBindViewHolder(adapter.VHList.get(4), 4)
            })
        }
        adapter.mData[6].positionListener = View.OnClickListener {
            LocationHelper(context, AMapLocationListener {
                adapter.mData[6].inputPositionLatitude = it.latitude
                adapter.mData[6].inputPositionLongitude = it.longitude
                val startLatlng =
                    LatLng(adapter.mData[3].inputPositionLatitude, adapter.mData[3].inputPositionLongitude)
                val endLatlng = LatLng(adapter.mData[6].inputPositionLatitude, adapter.mData[6].inputPositionLongitude)
                adapter.mData[6].inputDistancePositionContent =
                    (AMapUtils.calculateLineDistance(startLatlng, endLatlng) / 1000).toString()
                adapter.notifyItemChanged(6)
//                adapter.onBindViewHolder(adapter.VHList.get(4), 4)
            })
        }
        adapter.mData[12].positionAdd = View.OnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setNegativeButton("返回", null)
            builder.setCancelable(true)
            val view = View.inflate(context, R.layout.dialog_input, null)
            var data: MutableList<MultiStyleItem> = ArrayList()
            data.clear()
            val selectOption1Items = arrayListOf("燃气管道", "自来水管", "通讯管道", "污水管道", "电力管道", "燃油管道", "暖气管道", "自定义")
            data.add(MultiStyleItem(MultiStyleItem.Options.SELECT_DIALOG, selectOption1Items, "添加内容"))
            data.add(MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_UNIT, "水平距离", "(m)"))
            data.add(MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_UNIT, "垂直距离", "(m)"))
            view.rv_input_content.adapter = RecyclerviewAdapter(data)
            view.rv_input_content.layoutManager = LinearLayoutManager(view.context)
            builder.setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                var mdata = adapter.mData.toMutableList()
                var item = MultiStyleItem(
                    MultiStyleItem.Options.FOUR_DISPLAY,
                    data[0].selectContent,
                    "${data[1].inputUnitContent}m",
                    "${data[2].inputUnitContent}m",
                    "···"
                )
                val oldSize = adapter.mData.size
                mdata.add(oldSize - 1, item)
                adapter.mData = mdata
                adapter.mData[adapter.mData.size - 2].fourDisplayListener = View.OnClickListener {
                    val values: Array<String> = arrayOf("修改", "删除")
                    val alertBuilder = AlertDialog.Builder(context)
                    alertBuilder.setItems(values, DialogInterface.OnClickListener { dialogInterface, i ->
                        if (values[i] == "删除") {
                            val position = adapter.mData.indexOf(item)
                            mdata = adapter.mData.toMutableList()
                            mdata.removeAt(position)
                            adapter.mData = mdata
                            adapter.notifyItemRangeRemoved(position, adapter.mData.size - position)
                        } else if (values[i] == "修改") {
                            val builder = AlertDialog.Builder(context)
                            builder.setNegativeButton("返回", null)
                            builder.setCancelable(true)
                            val view = View.inflate(context, R.layout.dialog_input, null)
                            var data: MutableList<MultiStyleItem> = ArrayList()
                            data.clear()
                            val selectContent = adapter.mData[adapter.mData.size - 2].fourDisplayTitle
                            val horizontalDistance = adapter.mData[adapter.mData.size - 2].fourDisplayContent1
                            val verticalDistance = adapter.mData[adapter.mData.size - 2].fourDisplayContent2
                            val selectOption1Items =
                                arrayListOf("燃气管道", "自来水管", "通讯管道", "污水管道", "电力管道", "燃油管道", "暖气管道", "自定义")
                            data.add(MultiStyleItem(MultiStyleItem.Options.SELECT_DIALOG, selectOption1Items, "添加内容"))
                            data[data.size - 1].selectContent = selectContent
                            data.add(MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_UNIT, "水平距离", "(m)"))
                            data[data.size - 1].inputUnitContent = horizontalDistance.replace("m", "")
                            data.add(MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_UNIT, "垂直距离", "(m)"))
                            data[data.size - 1].inputUnitContent = verticalDistance.replace("m", "")
                            var mAdapter = RecyclerviewAdapter(data)
                            view.rv_input_content.adapter = mAdapter
                            view.rv_input_content.layoutManager = LinearLayoutManager(view.context)
                            builder.setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface, i ->
                                adapter.mData[adapter.mData.indexOf(item)].fourDisplayTitle = data[0].selectContent
                                adapter.mData[adapter.mData.indexOf(item)].fourDisplayContent1 =
                                    "${data[1].inputUnitContent}m"
                                adapter.mData[adapter.mData.indexOf(item)].fourDisplayContent2 =
                                    "${data[2].inputUnitContent}m"
                                adapter.notifyItemChanged(adapter.mData.indexOf(item))
                            })
                            builder.setView(view)
                            val dialog = builder.create()
                            dialog.show()
                            dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                            dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                        }
                    }).show()
                }
                adapter.notifyItemInserted(mdata.size - 1)  //强制刷新
//                    adapter.onBindViewHolder(adapter.VHList.get(j), j)
                //adapter.notifyDataSetChanged()
                //adapter.notifyItemRangeChanged(j,2)
            })
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }
        return adapter
    }

    //基础开挖
    fun PassagewayFoundationExcavation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/Passageway/foundationExcavation/foundationExcavation.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //材料运输
    fun PassagewayMaterialTransportation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/Passageway/materialTransportation/materialTransportation.json")
                .toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 0 until 2) {
            val expandList =
                itemGenerate.getJsonFromAsset("Professional/Passageway/materialTransportation/materialTransportationSon1.json")
            //从后台拿数据 只需更改此处逻辑
            for (k in 0 until expandList.size - 1) {
                //expandList[k].singleDisplayLeftContent = "x"
            }
            //
            expandList[5].jumpListener = View.OnClickListener {
                val data = Bundle()
                data.putString("key", "CarPicture")
                (activity as ProfessionalActivity).switchFragment(UploadPhoneFragment.newInstance(data), "Capture")
            }
            val expandListAdapter = RecyclerviewAdapter(expandList)
            mData.add(MultiStyleItem(MultiStyleItem.Options.EXPAND_LIST, "塑料制品", "0", expandListAdapter))
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //预制构件制作及安装
    fun PassagewayManufactureInstallation(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/Passageway/manufactureInstallation/manufactureInstallation.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //基础浇筑
    fun PassagewayFoundationPouring(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/foundationPouring/foundationPouring.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //电缆配管
    fun PassagewayCablePiping(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/Passageway/cablePiping/cablePiping.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 0 until 2) {
            val expandList = itemGenerate.getJsonFromAsset("Professional/Passageway/cablePiping/cablePipingSon1.json")
            //从后台拿数据 只需更改此处逻辑
            for (k in 0 until expandList.size - 1) {
                //expandList[k].singleDisplayLeftContent = "x"
            }
            //
            expandList[7].jumpListener = View.OnClickListener {
                val data = Bundle()
                data.putString("key", "CarPicture")
                (activity as ProfessionalActivity).switchFragment(UploadPhoneFragment.newInstance(data), "Capture")
            }
            val expandListAdapter = RecyclerviewAdapter(expandList)
            mData.add(MultiStyleItem(MultiStyleItem.Options.EXPAND_LIST, "排管", "0", expandListAdapter))
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //电缆桥架
    fun PassagewayCableTray(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/cableTray/cableTray.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //电缆敷设
    fun PassagewayCableLaying(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/Passageway/cableLaying/cableLaying.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 0 until 2) {
            val expandList = itemGenerate.getJsonFromAsset("Professional/Passageway/cableLaying/cableLayingSon1.json")
            //从后台拿数据 只需更改此处逻辑
            for (k in 0 until expandList.size - 1) {
                //expandList[k].singleDisplayLeftContent = "x"
            }
            //
            expandList[4].jumpListener = View.OnClickListener {
                val data = Bundle()
                data.putString("key", "CarPicture")
                (activity as ProfessionalActivity).switchFragment(UploadPhoneFragment.newInstance(data), "Capture")
            }
            val expandListAdapter = RecyclerviewAdapter(expandList)
            mData.add(MultiStyleItem(MultiStyleItem.Options.EXPAND_LIST, "直埋式敷设", "0", expandListAdapter))
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //电缆头制作
    fun PassagewayCableHeadFabrication(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/Passageway/cableHeadFabrication/cableHeadFabrication.json")
                .toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        for (j in 0 until 2) {
            val expandList =
                itemGenerate.getJsonFromAsset("Professional/Passageway/cableHeadFabrication/cableHeadFabricationSon2.json")
            //从后台拿数据 只需更改此处逻辑
            for (k in 0 until expandList.size - 1) {
                //expandList[k].singleDisplayLeftContent = "x"
            }
            //
            expandList[2].jumpListener = View.OnClickListener {
                val data = Bundle()
                data.putString("key", "CarPicture")
                (activity as ProfessionalActivity).switchFragment(UploadPhoneFragment.newInstance(data), "Capture")
            }
            val expandListAdapter = RecyclerviewAdapter(expandList)
            mData.add(MultiStyleItem(MultiStyleItem.Options.EXPAND_LIST, "塑料线槽", "0", expandListAdapter))
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //电缆防火
    fun PassagewayCableFireProtection(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/cableFireProtection.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
            UnSerializeDataBase.imgList.clear()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //电缆试验
    fun PassagewayCableTest(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Passageway/cableTest.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
            UnSerializeDataBase.imgList.clear()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //接地敷设
    fun PassagewayGroundLaying(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData =
            itemGenerate.getJsonFromAsset("Professional/Passageway/groundLaying/groundLaying.json").toMutableList()
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //
    /*
     *  公共
     */


    //资料动态
    fun DataDynamic(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Public/dataDynamics.json")
        mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //专业
    fun Professional_ProjectMore(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/ProjectMore.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    // alterPosition time:2019/7/15
    // function:/* 节点更多 */
    fun Professional_NodeMore(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/NodeMore.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    // alterPosition time:2019/7/15
    // function:/* 通道更多 */
    fun Professional_PassageWayMore(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/PassageWayMore.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    // alterPosition time:2019/7/18
    // function: //新建项目盘
    fun ProfessionalNewProjectDisk(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/NewProjectDisk.json")
        val adapter = RecyclerviewAdapter(mData)
        mData[0].backListener = View.OnClickListener {
            activity.finish()
        }
        adapter.setHasStableIds(true)
        for (j in 1 until 12) {
            if (j in arrayListOf(4, 5, 6, 7))
                continue
            mData[j].jumpListener = View.OnClickListener {
                val builder = AlertDialog.Builder(context)
                val dialogView = View.inflate(context, R.layout.shift_dialog, null)
                builder.setTitle(mData[j].shiftInputTitle)
                builder.setNegativeButton("取消", null)
                builder.setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                    mData[j].shiftInputContent = dialogView.shift_dialog_content.text.toString()
                    adapter.notifyItemChanged(j)  //强制刷新
//                    adapter.onBindViewHolder(adapter.VHList.get(j), j)
                    //adapter.notifyDataSetChanged()
                    //adapter.notifyItemRangeChanged(j,2)
                })
                val dialog = builder.create()
                dialog.setView(dialogView)
                dialog.show()
            }
        }
        for (j in 12 until 14) {
            mData[j].jumpListener = View.OnClickListener {
                var result = ""
                val cale1 = Calendar.getInstance()
                DatePickerDialog(
                    context,
                    DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        result += "${year}年${month + 1}月${dayOfMonth}日"
                        mData[j].shiftInputContent = result
                        adapter.notifyItemChanged(j)
//                        adapter.onBindViewHolder(adapter.VHList.get(j), j)
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

    //项目盘应用
    fun ProfessionalApplication(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Application.json")
        val expandMenuListListener: MutableList<View.OnClickListener?> = ArrayList()
        for (j in 0 until mData[2].expandMenuList.size) {
            expandMenuListListener.add(null)
        }
        expandMenuListListener[3] = View.OnClickListener {
            //            activity.startActivity(Intent(activity,))
        }
        mData[0].backListener = View.OnClickListener {
            activity.finish()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    // alterPosition time:2019/7/25
    // function:派工单Professional/Dispatch/DispatchList.json
    fun ProfessionalDispatchList(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Professional/Dispatch/DispatchList.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    // alterPosition time:2019/7/25
    // function:选择项目Professional/Dispatch/ProjectName.json
    fun ProfessionalSelectProject(): CheckBoxAdapter {
        val mCheckBoxList = ArrayList<CheckBoxStyle>()
        for (j in 0 until 10) {
            var checkBox: CheckBoxStyle = CheckBoxStyle("张三", "普工", 2)
            mCheckBoxList.add(checkBox)
        }
        val adapter = CheckBoxAdapter(mCheckBoxList)
        return adapter
    }

    // alterPosition time:2019/7/25
    // function:班组人员Professional/Dispatch/GroupCrew.json
    fun ProfessionalGroupCrew(): CheckBoxAdapter {
        val mCheckBoxList = ArrayList<CheckBoxStyle>()
        for (j in 0 until 10) {
            var checkBox: CheckBoxStyle = CheckBoxStyle("张三", "普工", 3)
            mCheckBoxList.add(checkBox)
        }
        val adapter = CheckBoxAdapter(mCheckBoxList)
        return adapter
    }

    // alterPosition time:2019/7/25
    // function:机械Professional/Dispatch/Machine.json
    fun ProfessionalMachine(): CheckBoxAdapter {
        val mCheckBoxList = ArrayList<CheckBoxStyle>()
        for (j in 0 until 10) {
            var checkBox: CheckBoxStyle = CheckBoxStyle("挖掘机", "规格型号:C15", 1)
            mCheckBoxList.add(checkBox)
        }
        val adapter = CheckBoxAdapter(mCheckBoxList)
        return adapter
    }

    // alterPosition time:2019/7/25
    // function:车辆Professional/Dispatch/Vehicle.json
    fun ProfessionalVehicle(): CheckBoxAdapter {
        val mCheckBoxList = ArrayList<CheckBoxStyle>()
        for (j in 0 until 10) {
            var checkBox: CheckBoxStyle = CheckBoxStyle("车牌号码:湘A 8888\n规格型号:2019款 Cayenne E-Hybrid", "普工", 0)
            mCheckBoxList.add(checkBox)
        }
        val adapter = CheckBoxAdapter(mCheckBoxList)
        return adapter
    }

    // alterPosition time:2019/7/26
    // function:标准
    fun ProfessionalStandard(): CheckBoxAdapter {
        val mCheckBoxList = ArrayList<CheckBoxStyle>()
        for (j in 0 until 10) {
            var checkBox: CheckBoxStyle = CheckBoxStyle("1、施工单位一名领导(副所长)兼管……", "普工", 2)
            mCheckBoxList.add(checkBox)
        }
        val adapter = CheckBoxAdapter(mCheckBoxList)
        return adapter
    }

    // alterPosition time:2019/7/26
    // function:安全措施
    fun ProfessionalSafetyMeasures(): CheckBoxAdapter {
        val mCheckBoxList = ArrayList<CheckBoxStyle>()
        for (j in 0 until 10) {
            var checkBox: CheckBoxStyle = CheckBoxStyle("1、施工单位一名领导(副所长)兼管……", "普工", 2)
            mCheckBoxList.add(checkBox)
        }
        val adapter = CheckBoxAdapter(mCheckBoxList)
        return adapter
    }

    // alterPosition time:2019/7/26
    // function:技术措施
    fun ProfessionalTechnicalMeasures(): CheckBoxAdapter {
        val mCheckBoxList = ArrayList<CheckBoxStyle>()
        for (j in 0 until 10) {
            var checkBox: CheckBoxStyle = CheckBoxStyle("1、施工单位一名领导(副所长)兼管……", "普工", 2)
            mCheckBoxList.add(checkBox)
        }
        val adapter = CheckBoxAdapter(mCheckBoxList)
        return adapter
    }

    // alterPosition time:2019/7/26
    // function:施工方案
    fun ProfessionalWorkingPlan(): CheckBoxAdapter {
        val mCheckBoxList = ArrayList<CheckBoxStyle>()
        for (j in 0 until 10) {
            var checkBox: CheckBoxStyle = CheckBoxStyle("管道焊接施工方案范本", "普工", 3)
            mCheckBoxList.add(checkBox)
        }
        val adapter = CheckBoxAdapter(mCheckBoxList)
        return adapter
    }

    // alterPosition time:2019/7/26
    // function:作业指导书
    fun ProfessionalWorkInstruction(): CheckBoxAdapter {
        val mCheckBoxList = ArrayList<CheckBoxStyle>()
        for (j in 0 until 30) {
            var checkBox: CheckBoxStyle = CheckBoxStyle("电力工程电气作业指导书", "普工", 3)
            mCheckBoxList.add(checkBox)
        }
        val adapter = CheckBoxAdapter(mCheckBoxList)
        return adapter
    }

    // alterPosition time:2019/7/26
    // function:材料
    fun ProfessionalMaterial(): EngineeringAppLiancesAdapter {
        val mEngineeringAppliancesList = ArrayList<EngineeringAppliances>()
        for (j in 0 until 10) {
            var engineeringAppliances: EngineeringAppliances = EngineeringAppliances("基础保护帽", "C15", "m3", "20")
            mEngineeringAppliancesList.add(engineeringAppliances)
        }
        val adapter = EngineeringAppLiancesAdapter(mEngineeringAppliancesList)
        return adapter
    }

    // alterPosition time:2019/7/26
    // function:工器具
    fun ProfessionalEngineeringAppliances(): EngineeringAppLiancesAdapter {
        val mEngineeringAppliancesList = ArrayList<EngineeringAppliances>()
        for (j in 0 until 10) {
            var engineeringAppliances: EngineeringAppliances = EngineeringAppliances("铲子", "C15", "m3", "20")
            mEngineeringAppliancesList.add(engineeringAppliances)
        }
        val adapter = EngineeringAppLiancesAdapter(mEngineeringAppliancesList)
        return adapter
    }

    /*
     * 办公OA
     */

    //通讯录
    fun OfficeAddressBook(): RecyclerviewAdapter {
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("Office/AddressBook.json")
        mData[0].backListener = View.OnClickListener {
            activity.finish()
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    /**
     * @VIP
     */
    fun vipPrivilegesInformation(type:Int):RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = when(type){
            0->itemGenerate.getJsonFromAsset("Vip/vip1.json")
            1->itemGenerate.getJsonFromAsset("Vip/vip2.json")
            else->itemGenerate.getJsonFromAsset("Vip/vip3.json")
        }
        return RecyclerviewAdapter(mData)
    }
    /**
     * @我的报名
     */
    fun registrationDisplayDemandIndividual():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("MyRegistration/DemandIndividual.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    fun registrationDisplayDemandGroup():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("MyRegistration/DemandGroup.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    fun registrationDisplayDemandLease():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("MyRegistration/DemandLease.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    fun registrationDisplayDemandTripartite():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("MyRegistration/DemandTripartite.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }


    fun inventoryItemType():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitMemberList.json")
        val adapter = RecyclerviewAdapter(mData)
        adapter.mData[0].backListener = View.OnClickListener {
            activity.supportFragmentManager.popBackStackImmediate()
        }
        adapter.mData[0].tvSelectListener = View.OnClickListener {
            Log.i("fragment is ok?",(activity.supportFragmentManager.findFragmentByTag("inventory") is InventoryFragment).toString())
            val fragment = activity.supportFragmentManager.findFragmentByTag("inventory") as InventoryFragment
            activity.supportFragmentManager.popBackStackImmediate()
            fragment.update(adapter.mData)
        }
        return adapter
    }

    ////alterPosition:2019/8/26 0026
    //found:Sanget
    //需求 供应 显示

    //需求个人显示模板
    fun demandIndividualDisplay():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/demandIndividual.json")
        return RecyclerviewAdapter(mData)
    }
    //个人劳务显示模板
    fun supplyIndividualDisplay():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/supplyIndividual.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //需求团队（变电主网配网测量显示模板）
    fun demandTeamDisplay():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/demandTeam.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //团队服务（变电主网配网测量显示模板）
    fun supplyTeamDisplay():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/supplyTeam.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //需求团队（马帮运输）
    fun demandTeamDisplayGongTrans():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/gangTransportation.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //团队服务（马帮运输）
    fun supplyTeamDisplayGongTrans():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/gangTransportation.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //需求团队（桩机服务）
    fun demandTeamDisplayPile():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/pileFoundationService.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //团队服务（桩机服务）
    fun supplyTeamDisplayPile():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/pileFoundationService.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //需求团队（非开挖）
    fun demandTeamDisplayTrenchiless():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/trenchlessJackingConstructionTeam.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //团队服务（非开挖）
    fun supplyTeamDisplayTrenchiless():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/trenchlessJackingConstructionTeam.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //需求团队（试验调试）
    fun demandTeamDisplayTestAndDebugging():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/testAndDebugging.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //团队服务（试验调试）
    fun supplyTeamDisplayTestAndDebugging():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/testAndDebugging.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //需求团队（跨越架）
    fun demandTeamDisplayCrossFrame():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/crossFrame.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //团队服务（跨越架）
    fun supplyTeamDisplayCrossFrame():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/crossFrame.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //需求团队（运行维护）
    fun demandTeamDisplayOperationAndMaintenance():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/operationAndMaintenance.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //团队服务（运行维护）
    fun supplyTeamDisplayOperationAndMaintenance():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/operationAndMaintenance.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //需求租赁（车辆租赁）
    fun demandTeamDisplayVehicleLeasing():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/vehicleLeasing.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //租赁服务（租赁）
    fun supplyTeamDisplayVehicleLeasing():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/vehicleLeasing.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //需求租赁（工器具设备机械租赁）
    fun demandTeamDisplayEquipmentLeasing():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/equipmentLeasing.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //租赁服务（工器具设备机械租赁）
    fun supplyTeamDisplayEquipmentLeasing(type:String):RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/equipmentLeasing.json")
        when(type){
            "工器具租赁"->{
                mData[8].singleDisplayRightTitle="工器具租赁清册"
            }
            "设备租赁"->{
                mData[8].singleDisplayRightTitle="设备租赁清册"
            }
            "机械租赁"->{
                mData[8].singleDisplayRightTitle="机械租赁清册"
            }
        }
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //需求三方
    fun demandTeamDisplayDemandTripartite():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/demandTripartite.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //三方服务
    fun supplyTeamDisplayDemandTripartite():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/demandTripartite.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //三方 资质合作
    fun demandTeamDisplayDemandTripartiteCooperation():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/demandTripartiteCooperation.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //三方 资质合作
    fun supplyTeamDisplayDemandTripartiteCooperation():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/supplyTripartiteCooperation.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }


    /**
     * @报名清册查看
     */
    fun  registrationProvideCrewList():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("RegistrationInventoryDisplay/ProvideCrewList.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //团队成员清册
    fun  registrationCrewList():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("RegistrationInventoryDisplay/CrewList.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //机械设备
    fun registrationMechanicalEquipment():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("RegistrationInventoryDisplay/MechanicalEquipment.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //团队车辆清册
    fun registrationTeamCarList():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("RegistrationInventoryDisplay/TeamCarList.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //租赁车辆清册
    fun registrationLeaseCarList():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("RegistrationInventoryDisplay/LeaseCarList.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //租赁清单
    fun registrationLeaseList():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("RegistrationInventoryDisplay/LeaseCarList.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //三方服务清册
    fun  registrationThirdList():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("RegistrationInventoryDisplay/ThirdList.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }

    //////需求查看
    //需求车辆清册查看
    fun requirementCarList(bundle: Bundle):RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/requirementCarList.json")
        val adapter = RecyclerviewAdapter(mData)
        var listData1 = bundle!!.getSerializable("listData1") as RequirementCarList
        adapter.mData[0].singleDisplayRightContent=if(listData1.carType==null){" "}else{ listData1.carType}
        adapter.mData[1].singleDisplayRightContent=if(listData1.needCarNumber==null){" "}else{ listData1.needCarNumber}
        when{
            listData1.construction=="1"->adapter.mData[2].singleDisplayRightContent="箱式"
            listData1.construction=="0"->adapter.mData[2].singleDisplayRightContent="敞篷"
            else->adapter.mData[2].singleDisplayRightContent=" "
        }
        when{
            listData1.isInsurance=="0"->adapter.mData[3].singleDisplayRightContent="无保险"
            listData1.isInsurance=="1"->adapter.mData[3].singleDisplayRightContent="有保险"
            else->adapter.mData[3].singleDisplayRightContent=" "
        }
        when{
            listData1.isDriver=="0"->adapter.mData[4].singleDisplayRightContent="不配备"
            listData1.isDriver=="1"->adapter.mData[4].singleDisplayRightContent="配备"
            else->adapter.mData[4].singleDisplayRightContent=" "
        }
        adapter.mData[5].singleDisplayRightContent=if(listData1.haveDriver==null){" "}else{ listData1.haveDriver}
        adapter.mData[6].singleDisplayRightContent=if(listData1.noDriver==null){" "}else{ listData1.noDriver}
        adapter.mData[7].singleDisplayRightContent=if(listData1.remark==null){" "}else{ listData1.remark}
        return adapter
    }
    //需求成员清册查看
    fun requirementMembersList(bundle: Bundle):RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/requirementMembersList.json")
        val adapter = RecyclerviewAdapter(mData)
        var listData2 = bundle.getSerializable("listData2") as RequirementMembersList
        adapter.mData[0].singleDisplayRightContent=if(listData2.positionType==null){" "}else{ listData2.positionType}
        adapter.mData[1].singleDisplayRightContent=if(listData2.needPeopleNumber==null){" "}else{ listData2.needPeopleNumber}
        adapter.mData[2].singleDisplayRightContent=if(listData2.salaryStandard==null){" "}else{ listData2.salaryStandard}
        adapter.mData[3].singleDisplayRightContent=if(listData2.personCertificate==null){" "}else{ listData2.personCertificate}
        when{
            listData2.haveCertificate=="0"->adapter.mData[4].singleDisplayRightContent="否"
            listData2.haveCertificate=="1"->adapter.mData[4].singleDisplayRightContent="是"
            else->adapter.mData[4].singleDisplayRightContent=" "
        }
        adapter.mData[5].singleDisplayRightContent=if(listData2.remark==null){" "}else{ listData2.remark}
        return adapter
    }

    //需求租赁清单查看
    fun requirmentLease(bundle: Bundle):RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/requirmentLease.json")
        val adapter = RecyclerviewAdapter(mData)
        var listData4 = bundle.getSerializable("listData4") as requirementLeaseProjectList
                adapter.mData[0].singleDisplayRightContent = if(listData4.projectName==null){" "}else{ listData4.projectName }
                adapter.mData[1].singleDisplayRightContent = if(listData4.specificationsModels==null){" "}else{ listData4.specificationsModels }
                adapter.mData[2].singleDisplayRightContent = if(listData4.units==null){" "}else{ listData4.units }
                adapter.mData[3].singleDisplayRightContent = if(listData4.quantity==null){" "}else{ listData4.quantity }
                adapter.mData[5].singleDisplayRightContent = if(listData4.hireTime==null){" "}else{ listData4.hireTime }
                adapter.mData[6].singleDisplayRightContent = if(listData4.detailsExplain==null){" "}else{ listData4.detailsExplain }
        return adapter
    }
    //需求三方清单查看
    fun requirmentThird(bundle: Bundle):RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/requirmentThird.json")
        val adapter = RecyclerviewAdapter(mData)
        var listData5 = bundle.getSerializable("listData5") as thirdLists
        adapter.mData[0].singleDisplayRightContent = if(listData5.projectName==null){" "}else{ listData5.projectName }
        adapter.mData[1].singleDisplayRightContent = if(listData5.specificationsModels==null){" "}else{ listData5.specificationsModels }
        adapter.mData[2].singleDisplayRightContent = if(listData5.units==null){" "}else{ listData5.units }
        adapter.mData[3].singleDisplayRightContent = if(listData5.quantity==null){" "}else{ listData5.quantity }
        adapter.mData[5].singleDisplayRightContent = if(listData5.detailsExplain==null){" "}else{ listData5.detailsExplain }
        return adapter
    }
/////供应查看
    //供应人员清册查看
    fun provideCrewLists(bundle: Bundle):RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/provideCrewLists.json")
        val adapter = RecyclerviewAdapter(mData)
        var listData7 = bundle.getSerializable("listData7") as ProvideCrewLists
        adapter.mData[0].singleDisplayRightContent = if(listData7.name==null){" "}else{ listData7.name }
           when(listData7.sex){
               "true"->{ adapter.mData[1].singleDisplayRightContent = "男" }
               "false"->{adapter.mData[1].singleDisplayRightContent = "女"}
               else->{adapter.mData[1].singleDisplayRightContent = ""}
           }
        adapter.mData[2].singleDisplayRightContent = if(listData7.age==null){" "}else{ listData7.age }
        adapter.mData[3].singleDisplayRightContent = if(listData7.wokerType==null){" "}else{ listData7.wokerType }
        adapter.mData[4].singleDisplayRightContent = if(listData7.workExperience==null){" "}else{ listData7.workExperience }
        adapter.mData[5].singleDisplayRightContent = if(listData7.money==null){" "}else{ listData7.money }
        adapter.mData[6].singleDisplayRightContent = if(listData7.salaryUnit==null){" "}else{ listData7.salaryUnit }
        return adapter
    }
    //供应运输清册查看
    fun provideTransportMachines(bundle: Bundle):RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/provideTransportMachines.json")
        val adapter = RecyclerviewAdapter(mData)
        var listData8 = bundle.getSerializable("listData8") as ProvideTransportMachines
        adapter.mData[0].singleDisplayRightContent = if(listData8.carType==null){" "}else{ listData8.carType }
        adapter.mData[1].singleDisplayRightContent = if(listData8.carNumber==null){" "}else{ listData8.carNumber }
        adapter.mData[2].singleDisplayRightContent = if(listData8.maxPassengers==null){" "}else{ listData8.maxPassengers }
        adapter.mData[3].singleDisplayRightContent = if(listData8.maxWeight==null){" "}else{ listData8.maxWeight }
        when(listData8.construction){
            "1"->{ adapter.mData[4].singleDisplayRightContent = "箱式" }
            "2"->{adapter.mData[4].singleDisplayRightContent = "敞篷"}
            else->{adapter.mData[4].singleDisplayRightContent = ""}
        }
        adapter.mData[5].singleDisplayRightContent = if(listData8.lenghtCar==null){" "}else{ listData8.lenghtCar }
        when(listData8.isDriver){
            "true"->{ adapter.mData[6].singleDisplayRightContent = "配备 " }
            "false"->{adapter.mData[6].singleDisplayRightContent = "不配备 "}
            else->{adapter.mData[6].singleDisplayRightContent = ""}
        }
        when(listData8.isInsurance){
            "true"->{ adapter.mData[7].singleDisplayRightContent = "有保险 " }
            "false"->{adapter.mData[7].singleDisplayRightContent = "无保险 "}
            else->{adapter.mData[7].singleDisplayRightContent = ""}
        }
        adapter.mData[8].singleDisplayRightContent = if(listData8.carPhotoPath==null){" "}else{ listData8.carPhotoPath }

        return adapter
    }
    //供应工器具清册查看
    fun provideConstructionToolLists(bundle: Bundle):RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplaySupply/constructionToolLists.json")
        val adapter = RecyclerviewAdapter(mData)
        var listData9 = bundle.getSerializable("listData9") as ConstructionToolLists
        adapter.mData[0].singleDisplayRightContent = if(listData9.type==null){" "}else{ listData9.type }
        adapter.mData[1].singleDisplayRightContent = if(listData9.specificationsModel==null){" "}else{ listData9.specificationsModel }
        adapter.mData[2].singleDisplayRightContent = if(listData9.quantity==null){" "}else{ listData9.quantity }
        adapter.mData[3].singleDisplayRightContent = if(listData9.unit==null){" "}else{ listData9.unit }
        adapter.mData[4].singleDisplayRightContent = if(listData9.remark==null){" "}else{ listData9.remark }
        return adapter
    }
    //租赁清册
    fun provideHireList(bundle: Bundle):RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/requirementPowerTransformationSalary.json")
        val adapter = RecyclerviewAdapter(mData)
        var listData10 = bundle.getSerializable("listData10") as LeaseList
        adapter.mData[0].singleDisplayRightContent = if(listData10.type==null){" "}else{ listData10.type }
        adapter.mData[1].singleDisplayRightContent = if(listData10.specificationsModels==null){" "}else{ listData10.specificationsModels }
        adapter.mData[2].singleDisplayRightContent = if(listData10.quantity==null){" "}else{ listData10.quantity }
        adapter.mData[3].singleDisplayRightContent = if(listData10.unit==null){" "}else{ listData10.unit }
        adapter.mData[4].singleDisplayRightContent = if(listData10.remark==null){" "}else{ listData10.remark }
        return adapter
    }
    //清工薪资
    fun requirementPowerTransformationSalary():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/requirementPowerTransformationSalary.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //清单报价
    fun requirementListQuotations():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/requirementListQuotations.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }
    //乙供清单
    fun requirementSecondProvideMaterialsList():RecyclerviewAdapter{
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        val mData = itemGenerate.getJsonFromAsset("DisplayDemand/requirementSecondProvideMaterialsList.json")
        val adapter = RecyclerviewAdapter(mData)
        return adapter
    }



    fun ApplicationSubmit(args:Bundle): RecyclerviewAdapter {
        val mdata=Bundle()
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        lateinit var mData:List<MultiStyleItem>
        lateinit var adapter: RecyclerviewAdapter
        when (args.getInt("type")) {
            //个人
            Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal -> {
//                var MutableListenerMember:MutableList<OnClickListener> =ArrayList()
                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitPersonnal.json")
                adapter = RecyclerviewAdapter(mData)
                val memberListData: MutableList<MultiStyleItem> = ArrayList()
                val Listdata = args.getSerializable("RequirementPersonDetail") as RequirementPersonDetail
                adapter.mData[0].singleDisplayRightContent = if (Listdata.projectName == null) {" "} else { Listdata.projectName }
                adapter.mData[1].singleDisplayRightContent = if (Listdata.requirementVariety == null) {" "} else { Listdata.requirementVariety }
                adapter.mData[2].singleDisplayRightContent = if (Listdata.requirementMajor == null) {" "} else { Listdata.requirementMajor }
                adapter.mData[3].singleDisplayRightContent = UnSerializeDataBase.idCardName
                adapter.mData[4].singleDisplayRightContent =UnSerializeDataBase.userPhone
                if(Listdata.salaryUnit=="面议"){
                    adapter.mData[5].singleDisplayRightContent= Listdata.salaryUnit
                }else {
                    adapter.mData[5].singleDisplayRightContent= "${Listdata.salaryStandard} ${Listdata.salaryUnit}"
                }
                //////成员清册查看
                //
                if(Listdata.needPeopleNumber.isEmpty()||Listdata.needPeopleNumber=="0")
                {
                    mData[6].buttonListener= arrayListOf( View.OnClickListener {
                        ToastHelper.mToast(context,"成员清册人数为空 不能填写")})
                    mData[6].necessary =true
                }
                else{
                    memberListData.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,
                            "${Listdata.requirementVariety}/${Listdata.requirementMajor}",
                            "${Listdata.needPeopleNumber}人"
                        )
                    )
                       val needpople = Listdata.needPeopleNumber.toInt()
                    for (i in 0 until needpople) {
                        val position = memberListData.size
                        val mData1: List<MultiStyleItem>
                        mdata.putString("type", "成员清册")
                        mData1 = ApplicationSubmitDetailList(mdata).mData
                        memberListData.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SHIFT_INPUT,
                                "",
                                false
                            )
                        )
                        memberListData[memberListData.size - 1].itemMultiStyleItem = mData1
                        memberListData[memberListData.size - 1].necessary = false
                        memberListData[memberListData.size - 1].jumpListener =
                            View.OnClickListener {
                                memberListData[0].selected = position
                                val bundle = Bundle()
                                bundle.putSerializable(
                                    "inventoryItem",
                                    memberListData[position].itemMultiStyleItem as Serializable
                                )
                                bundle.putString("type","成员清册")
                                FragmentHelper.switchFragment(
                                    context as DemandDisplayActivity,
                                    SubmitInventoryItemMoreFragment.newInstance(bundle),
                                    R.id.frame_display_demand,
                                    ""
                                )
                            }
                        if (needpople%10 ==0) {
                            memberListData.add(MultiStyleItem(MultiStyleItem.Options.BLANK, " "))
                        }
                    }
                    mData[6].buttonListener = arrayListOf(View.OnClickListener {
                        mData[0].selected = 6
                        mdata.putString("type", "成员清册")
                        mData[6].itemMultiStyleItem = memberListData
                        mdata.putSerializable(
                            "inventory",
                            mData[6].itemMultiStyleItem as Serializable
                        )
                        FragmentHelper.switchFragment(
                            activity as DemandDisplayActivity,
                            SubmitInventoryFragment.newInstance(mdata),
                            R.id.frame_display_demand,
                            "inventory"
                        )
                    })
                }

            }
            //主配 网 变电 测量 实验调试 跨 运行维护
            Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal,
            Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal,
            Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal,
            Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal,
            Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal,
            Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal,
            Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal->
            {
                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitTeam.json")
                lateinit var listData1: List<RequirementCarList>
                lateinit var listData2: List<RequirementMembersList>
                val carListData: MutableList<MultiStyleItem> = ArrayList()
                val memberListData: MutableList<MultiStyleItem> = ArrayList()
                adapter = RecyclerviewAdapter(mData)
                adapter.mData[6].necessary=true
                when (args.getInt("type"))
                {
                    Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal->//主网
                    {
                        val Listdata =args.getSerializable("RequirementMajorNetWork") as RequirementMajorNetWork
                        adapter.mData[0].singleDisplayRightContent = if(Listdata.projectName ==null){" "} else {Listdata.projectName}
                        adapter.mData[1].singleDisplayRightContent = if(Listdata.requirementVariety ==null){" "} else {Listdata.requirementVariety}
                        adapter.mData[2].singleDisplayRightContent = UnSerializeDataBase.idCardName
                        adapter.mData[3].singleDisplayRightContent =UnSerializeDataBase.userPhone
                        adapter.mData[6].buttonListener = arrayListOf(View.OnClickListener {
                            if(Listdata.constructionEquipment =="0") {
                                mData[0].selected = 6
                                mdata.putString("type", "机械清册")
                                mdata.putSerializable(
                                    "inventory",
                                    mData[6].itemMultiStyleItem as Serializable
                                )
                                FragmentHelper.switchFragment(
                                    activity as DemandDisplayActivity,
                                    SubmitInventoryFragment.newInstance(mdata),
                                    R.id.frame_display_demand,
                                    "inventory"
                                )
                            }
                            else{
                                adapter.mData[6].necessary =true
                                Toast.makeText(context,"已全部提供不需要填写",Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal->////配网
                    {
                        val Listdata =args.getSerializable("RequirementDistributionNetwork") as RequirementDistributionNetwork
                        adapter.mData[0].singleDisplayRightContent = if(Listdata.projectName ==null){" "} else {Listdata.projectName}
                        adapter.mData[1].singleDisplayRightContent = if(Listdata.requirementVariety ==null){" "} else {Listdata.requirementVariety}
                        adapter.mData[2].singleDisplayRightContent = UnSerializeDataBase.idCardName
                        adapter.mData[3].singleDisplayRightContent =UnSerializeDataBase.userPhone
                        adapter.mData[6].buttonListener = arrayListOf(View.OnClickListener {
                            if(Listdata.constructionEquipment =="0") {
                                mData[0].selected = 6
                                mdata.putString("type", "机械清册")
                                mdata.putSerializable(
                                    "inventory",
                                    mData[6].itemMultiStyleItem as Serializable
                                )
                                FragmentHelper.switchFragment(
                                    activity as DemandDisplayActivity,
                                    SubmitInventoryFragment.newInstance(mdata),
                                    R.id.frame_display_demand,
                                    "inventory"
                                )
                            }
                            else{
                                mData[6].necessary =true
                                Toast.makeText(context,"已全部提供不需要填写",Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal->//变电
                    {
                        val Listdata =args.getSerializable("RequirementPowerTransformation") as RequirementPowerTransformation
                        adapter.mData[0].singleDisplayRightContent = if(Listdata.projectName ==null){" "} else {Listdata.projectName}
                        adapter.mData[1].singleDisplayRightContent = if(Listdata.requirementVariety ==null){" "} else {Listdata.requirementVariety}
                        adapter.mData[2].singleDisplayRightContent = UnSerializeDataBase.idCardName
                        adapter.mData[3].singleDisplayRightContent =UnSerializeDataBase.userPhone
                        adapter.mData[6].buttonListener = arrayListOf(View.OnClickListener {
                            if(Listdata.constructionEquipment =="0") {
                                mData[0].selected = 6
                                mdata.putString("type", "机械清册")
                                mdata.putSerializable(
                                    "inventory",
                                    mData[6].itemMultiStyleItem as Serializable
                                )
                                FragmentHelper.switchFragment(
                                    activity as DemandDisplayActivity,
                                    SubmitInventoryFragment.newInstance(mdata),
                                    R.id.frame_display_demand,
                                    "inventory"
                                )
                            }
                            else{
                                mData[6].necessary =true
                                Toast.makeText(context,"已全部提供不需要填写",Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal->//测量设计
                    {
                        val Listdata =args.getSerializable("RequirementMeasureDesign") as RequirementMeasureDesign
                        adapter.mData[0].singleDisplayRightContent = if(Listdata.projectName ==null){" "} else {Listdata.projectName}
                        adapter.mData[1].singleDisplayRightContent = if(Listdata.requirementVariety ==null){" "} else {Listdata.requirementVariety}
                        adapter.mData[2].singleDisplayRightContent = UnSerializeDataBase.idCardName
                        adapter.mData[3].singleDisplayRightContent =UnSerializeDataBase.userPhone
                        adapter.mData[6].buttonListener = arrayListOf(View.OnClickListener {
                            if(Listdata.equipment =="0") {
                                mData[0].selected = 6
                                mdata.putString("type", "机械清册")
                                mdata.putSerializable(
                                    "inventory",
                                    mData[6].itemMultiStyleItem as Serializable
                                )
                                FragmentHelper.switchFragment(
                                    activity as DemandDisplayActivity,
                                    SubmitInventoryFragment.newInstance(mdata),
                                    R.id.frame_display_demand,
                                    "inventory"
                                )
                            }
                            else{
                                mData[6].necessary =true
                                Toast.makeText(context,"已全部提供不需要填写",Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal->//实验调试
                    {
                        val Listdata =args.getSerializable("RequirementTest") as RequirementTest
                        adapter.mData[0].singleDisplayRightContent = if(Listdata.projectName ==null){" "} else {Listdata.projectName}
                        adapter.mData[1].singleDisplayRightContent = if(Listdata.requirementVariety ==null){" "} else {Listdata.requirementVariety}
                        adapter.mData[2].singleDisplayRightContent = UnSerializeDataBase.idCardName
                        adapter.mData[3].singleDisplayRightContent =UnSerializeDataBase.userPhone
                        adapter.mData[6].buttonListener = arrayListOf(View.OnClickListener {
                            if(Listdata.machineEquipment =="0") {
                                mData[0].selected = 6
                                mdata.putString("type", "机械清册")
                                mdata.putSerializable(
                                    "inventory",
                                    mData[6].itemMultiStyleItem as Serializable
                                )
                                FragmentHelper.switchFragment(
                                    activity as DemandDisplayActivity,
                                    SubmitInventoryFragment.newInstance(mdata),
                                    R.id.frame_display_demand,
                                    "inventory"
                                )
                            }
                            else{
                                Toast.makeText(context,"已全部提供不需要填写",Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal->//跨越架
                    {
                        val Listdata =args.getSerializable("RequirementSpanWoodenSupprt") as RequirementSpanWoodenSupprt
                        adapter.mData[0].singleDisplayRightContent = if(Listdata.projectName ==null){" "} else {Listdata.projectName}
                        adapter.mData[1].singleDisplayRightContent = if(Listdata.requirementVariety ==null){" "} else {Listdata.requirementVariety}
                        adapter.mData[2].singleDisplayRightContent = UnSerializeDataBase.idCardName
                        adapter.mData[3].singleDisplayRightContent =UnSerializeDataBase.userPhone
                        adapter.mData[6].buttonListener = arrayListOf(View.OnClickListener {
                            if(Listdata.machineEquipment =="0") {
                                mData[0].selected = 6
                                mdata.putString("type", "机械清册")
                                mdata.putSerializable(
                                    "inventory",
                                    mData[6].itemMultiStyleItem as Serializable
                                )
                                FragmentHelper.switchFragment(
                                    activity as DemandDisplayActivity,
                                    SubmitInventoryFragment.newInstance(mdata),
                                    R.id.frame_display_demand,
                                    "inventory"
                                )
                            }
                            else{
                                Toast.makeText(context,"已全部提供不需要填写",Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal->//运维
                    {
                        val Listdata =args.getSerializable("RequirementRunningMaintain") as RequirementRunningMaintain
                        adapter.mData[0].singleDisplayRightContent = if(Listdata.projectName ==null){" "} else {Listdata.projectName}
                        adapter.mData[1].singleDisplayRightContent = if(Listdata.requirementVariety ==null){" "} else {Listdata.requirementVariety}
                        adapter.mData[2].singleDisplayRightContent = UnSerializeDataBase.idCardName
                        adapter.mData[3].singleDisplayRightContent =UnSerializeDataBase.userPhone
                        adapter.mData[6].buttonListener = arrayListOf(View.OnClickListener {
                            if(Listdata.machineEquipment =="0") {
                                mData[0].selected = 6
                                mdata.putString("type", "机械清册")
                                mdata.putSerializable(
                                    "inventory",
                                    mData[6].itemMultiStyleItem as Serializable
                                )
                                FragmentHelper.switchFragment(
                                    activity as DemandDisplayActivity,
                                    SubmitInventoryFragment.newInstance(mdata),
                                    R.id.frame_display_demand,
                                    "inventory"
                                )
                            }
                            else{
                                Toast.makeText(context,"已全部提供不需要填写",Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
                /////车辆清册报名
                //
                if(args.getSerializable("listData1")!=null) {
                    listData1 = args!!.getSerializable("listData1") as List<RequirementCarList>
                    if (listData1!!.isEmpty()) {
                        mData[4].necessary =true
                        adapter.mData[4].buttonListener = arrayListOf(View.OnClickListener {
                        ToastHelper.mToast(context, "车辆清册为空 不能填写")})
                    } else {
                        val sumSize=listData1.size
                        var Temp=0
                        for (j in listData1) {
                            Temp++
                            val type = j.carType
                            val needcar = j.needCarNumber.toInt()
                            carListData.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,
                                    type,
                                    "${needcar.toString()}辆"
                                )
                            )
                            for (i in 0 until needcar) {
                                val position = carListData.size
                                val mData1: List<MultiStyleItem>
                                mdata.putString("type", "车辆清册")
                                mdata.putString("name", type)
                                mData1 = ApplicationSubmitDetailList(mdata).mData
                                carListData.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SHIFT_INPUT,
                                        type,
                                        false
                                    )
                                )
                                carListData[carListData.size - 1].itemMultiStyleItem = mData1
                                carListData[carListData.size - 1].necessary = false
                                carListData[carListData.size - 1].jumpListener =
                                    View.OnClickListener {
                                        carListData[0].selected = position
                                        val bundle = Bundle()
                                        bundle.putSerializable(
                                            "inventoryItem",
                                            carListData[position].itemMultiStyleItem as Serializable
                                        )
                                        bundle.putString("type", "车辆清册")
                                        FragmentHelper.switchFragment(
                                            context as DemandDisplayActivity,
                                            SubmitInventoryItemMoreFragment.newInstance(bundle),
                                            R.id.frame_display_demand,
                                            ""
                                        )
                                    }
                            }
                            if(Temp<sumSize) {
                                carListData.add(MultiStyleItem(MultiStyleItem.Options.BLANK, " "))
                            }
                        }
                        adapter.mData[4].buttonListener = arrayListOf(View.OnClickListener {
                            mData[0].selected = 4
                            mdata.putString("type", "车辆清册")
                            mData[4].itemMultiStyleItem = carListData
                            mdata.putSerializable(
                                "inventory",
                                mData[4].itemMultiStyleItem as Serializable
                            )
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                SubmitInventoryFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "inventory"
                            )
                        })
                    }
                }
                else{
                    mData[4].necessary =true
                    adapter.mData[4].buttonListener = arrayListOf(View.OnClickListener {
                        ToastHelper.mToast(context, "车辆清册为空 不能填写")})
                }
                //////成员清册查看
                //
                if(args.getSerializable("listData2")!=null) {
                    listData2 = args.getSerializable("listData2") as List<RequirementMembersList>
                    if (listData2.isEmpty()) {
                        mData[5].necessary =true
                        adapter.mData[5].buttonListener= arrayListOf( View.OnClickListener {
                            ToastHelper.mToast(context,"成员清册为空 不能填写")})
                    } else {
                        val sumSize=listData2.size
                        var Temp=0
                                for (j in listData2) {
                                    Temp++
                                    val type = j.positionType
                                    val needpople = j.needPeopleNumber.toInt()
                                    memberListData.add(
                                        MultiStyleItem(
                                            MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,
                                            type,
                                            "${needpople.toString()}人"
                                        )
                                    )
                                    for (i in 0 until needpople) {
                                        val position = memberListData.size
                                        val mData1: List<MultiStyleItem>
                                        mdata.putString("type", "成员清册")
                                        mdata.putString("name", type)
                                        mData1 = ApplicationSubmitDetailList(mdata).mData
                                        memberListData.add(
                                            MultiStyleItem(
                                                MultiStyleItem.Options.SHIFT_INPUT,
                                                type,
                                                false
                                            )
                                        )
                                        memberListData[memberListData.size - 1].itemMultiStyleItem = mData1
                                        memberListData[memberListData.size - 1].necessary = false
                                        memberListData[memberListData.size - 1].jumpListener =
                                            View.OnClickListener {
                                                memberListData[0].selected = position
                                                val bundle = Bundle()
                                                bundle.putSerializable(
                                                    "inventoryItem",
                                                    memberListData[position].itemMultiStyleItem as Serializable
                                                )
                                                bundle.putString("type", "成员清册")
                                                FragmentHelper.switchFragment(
                                                    context as DemandDisplayActivity,
                                                    SubmitInventoryItemMoreFragment.newInstance(bundle),
                                                    R.id.frame_display_demand,
                                                    ""
                                                )
                                            }
                                    }
                                    if(Temp<sumSize)
                                    {
                                        memberListData.add(MultiStyleItem(MultiStyleItem.Options.BLANK, " "))
                                    }
                                }
                            mData[5].buttonListener = arrayListOf(View.OnClickListener {
                                mData[0].selected = 5
                                mdata.putString("type", "成员清册")
                                mData[5].itemMultiStyleItem = memberListData
                                mdata.putSerializable(
                                    "inventory",
                                    mData[5].itemMultiStyleItem as Serializable
                                )
                                FragmentHelper.switchFragment(
                                    activity as DemandDisplayActivity,
                                    SubmitInventoryFragment.newInstance(mdata),
                                    R.id.frame_display_demand,
                                    "inventory"
                                )
                            })
                    }
                }
                else{
                    mData[5].necessary =true
                    mData[5].buttonListener= arrayListOf( View.OnClickListener {
                    ToastHelper.mToast(context,"成员清册为空 不能填写")})
                }
            }
            //马帮
            Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal-> {//马帮运输
                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitHorseHelp.json")
                adapter = RecyclerviewAdapter(mData)
                val Listdata =args.getSerializable("RequirementCaravanTransport") as RequirementCaravanTransport
                adapter.mData[0].singleDisplayRightContent = if(Listdata.projectName ==null){" "} else {Listdata.projectName}
                adapter.mData[1].singleDisplayRightContent = if(Listdata.requirementVariety ==null){" "} else {Listdata.requirementVariety}
                adapter.mData[2].singleDisplayRightContent = UnSerializeDataBase.idCardName
                adapter.mData[3].singleDisplayRightContent =UnSerializeDataBase.userPhone
            }
            //非开挖 桩基
            Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal, //桩基
            Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal->{//非开挖
                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitPileFoundation.json")
                adapter = RecyclerviewAdapter(mData)
                mData[5].necessary=true
                lateinit var listData1: List<RequirementCarList>
                val carListData: MutableList<MultiStyleItem> = ArrayList()
                when (args.getInt("type")) {
                    Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal->{//桩基
                        val Listdata = args.getSerializable("RequirementPileFoundation") as RequirementPileFoundation
                        adapter.mData[0].singleDisplayRightContent = if (Listdata.projectName == null) { " " } else { Listdata.projectName }
                        adapter . mData [1].singleDisplayRightContent = if (Listdata.requirementVariety == null) { " " } else { Listdata.requirementVariety }
                        adapter.mData[2].singleDisplayRightContent = UnSerializeDataBase.idCardName
                        adapter.mData[3].singleDisplayRightContent =UnSerializeDataBase.userPhone
                        adapter.mData[5].buttonListener = arrayListOf(View.OnClickListener {
                            if(Listdata.otherMachineEquipment =="0") {
                                mData[0].selected = 5
                                mdata.putString("type", "机械清册")
                                mdata.putSerializable(
                                    "inventory",
                                    mData[5].itemMultiStyleItem as Serializable
                                )
                                FragmentHelper.switchFragment(
                                    activity as DemandDisplayActivity,
                                    SubmitInventoryFragment.newInstance(mdata),
                                    R.id.frame_display_demand,
                                    "inventory"
                                )
                            }
                            else{
                                Toast.makeText(context,"已全部提供不需要填写",Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal->{
                        val Listdata = args.getSerializable("RequirementUnexcavation") as RequirementUnexcavation
                        adapter.mData[0].singleDisplayRightContent = if (Listdata.projectName == null) { " " } else { Listdata.projectName }
                        adapter . mData [1].singleDisplayRightContent = if (Listdata.requirementVariety == null) { " " } else { Listdata.requirementVariety }
                        adapter.mData[2].singleDisplayRightContent = UnSerializeDataBase.idCardName
                        adapter.mData[3].singleDisplayRightContent =UnSerializeDataBase.userPhone
                        adapter.mData[5].buttonListener = arrayListOf(View.OnClickListener {
                            if(Listdata.otherMachineEquipment =="0") {
                                mData[0].selected = 5
                                mdata.putString("type", "机械清册")
                                mdata.putSerializable(
                                    "inventory",
                                    mData[5].itemMultiStyleItem as Serializable
                                )
                                FragmentHelper.switchFragment(
                                    activity as DemandDisplayActivity,
                                    SubmitInventoryFragment.newInstance(mdata),
                                    R.id.frame_display_demand,
                                    "inventory"
                                )
                            }
                            else{
                                Toast.makeText(context,"已全部提供不需要填写",Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
                /////车辆清册报名
                //
                if(args.getSerializable("listData1")!=null) {
                    listData1 = args.getSerializable("listData1") as List<RequirementCarList>
                    if (listData1.isEmpty()) {
                        mData[4].necessary =true
                        adapter.mData[4].buttonListener = arrayListOf(View.OnClickListener {
                            ToastHelper.mToast(context, "车辆清册为空 不能填写")})
                    } else {
                        val sumSize=listData1.size
                        var Temp=0
                        for (j in listData1) {
                            Temp++
                            val type = j.carType
                            val needcar = j.needCarNumber.toInt()
                            carListData.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,
                                    type,
                                    "${needcar.toString()}辆"
                                )
                            )
                            for (i in 0 until needcar) {
                                val position = carListData.size
                                val mData1: List<MultiStyleItem>
                                mdata.putString("type", "车辆清册")
                                mdata.putString("name", type)
                                mData1 = ApplicationSubmitDetailList(mdata).mData
                                carListData.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SHIFT_INPUT,
                                        type,
                                        false
                                    )
                                )
                                carListData[carListData.size - 1].itemMultiStyleItem = mData1
                                carListData[carListData.size - 1].necessary = false
                                carListData[carListData.size - 1].jumpListener =
                                    View.OnClickListener {
                                        carListData[0].selected = position
                                        val bundle = Bundle()
                                        bundle.putSerializable(
                                            "inventoryItem",
                                            carListData[position].itemMultiStyleItem as Serializable
                                        )
                                        bundle.putString("type", "车辆清册")
                                        FragmentHelper.switchFragment(
                                            context as DemandDisplayActivity,
                                            SubmitInventoryItemMoreFragment.newInstance(bundle),
                                            R.id.frame_display_demand,
                                            ""
                                        )
                                    }
                            }
                            if(Temp<sumSize) {
                                carListData.add(MultiStyleItem(MultiStyleItem.Options.BLANK, " "))
                            }
                        }
                        adapter.mData[4].buttonListener = arrayListOf(View.OnClickListener {
                            mData[0].selected = 4
                            mdata.putString("type", "车辆清册")
                            mData[4].itemMultiStyleItem = carListData
                            mdata.putSerializable(
                                "inventory",
                                mData[4].itemMultiStyleItem as Serializable
                            )
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                SubmitInventoryFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "inventory"
                            )
                        })
                    }
                }
                else{
                    mData[4].necessary =true
                    adapter.mData[4].buttonListener = arrayListOf(View.OnClickListener {
                        ToastHelper.mToast(context, "车辆清册为空 不能填写")})
                }
            }
            //车辆租赁
            Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal-> {//车辆
                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitVeichleHire.json")
                adapter = RecyclerviewAdapter(mData)
                lateinit var listData1: List<RequirementCarList>
                val carListData: MutableList<MultiStyleItem> = ArrayList()
                val Listdata = args.getSerializable("RequirementLeaseCar") as RequirementLeaseCar
                adapter.mData[0].singleDisplayRightContent = if (Listdata.projectName == null) {
                    " "
                } else {
                    Listdata.projectName
                }
                adapter.mData[1].singleDisplayRightContent =
                    if (Listdata.requirementVariety == null) {
                        " "
                    } else {
                        Listdata.requirementVariety
                    }
                adapter.mData[2].singleDisplayRightContent = if (Listdata.vehicleType == null) {
                    " "
                } else {
                    Listdata.vehicleType
                }
                adapter.mData[3].singleDisplayRightContent = UnSerializeDataBase.idCardName
                adapter.mData[4].singleDisplayRightContent =UnSerializeDataBase.userPhone
                /////车辆租赁车辆清册报名
                //
                if(args.getSerializable("listData1")!=null) {
                    listData1 = args.getSerializable("listData1") as List<RequirementCarList>
                    if (listData1.isEmpty()) {
                        mData[5].necessary =true
                        adapter.mData[5].buttonListener = arrayListOf(View.OnClickListener {
                            ToastHelper.mToast(context, "车辆清册为空 不能填写")})
                    } else {
                        val sumSize=listData1.size
                        var Temp=0
                        for (j in listData1) {
                            Temp++
                            val type = j.carType
                            val needcar = j.needCarNumber.toInt()
                            carListData.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,
                                    type,
                                    "${needcar.toString()}辆"
                                )
                            )
                            for (i in 0 until needcar) {
                                val position = carListData.size
                                val mData1: List<MultiStyleItem>
                                mdata.putString("type", "车辆租赁车辆清册")
                                mdata.putString("name", type)
                                mData1 = ApplicationSubmitDetailList(mdata).mData
                                carListData.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SHIFT_INPUT,
                                        type,
                                        false
                                    )
                                )
                                carListData[carListData.size - 1].itemMultiStyleItem = mData1
                                carListData[carListData.size - 1].necessary = false
                                carListData[carListData.size - 1].jumpListener =
                                    View.OnClickListener {
                                        carListData[0].selected = position
                                        val bundle = Bundle()
                                        bundle.putSerializable(
                                            "inventoryItem",
                                            carListData[position].itemMultiStyleItem as Serializable
                                        )
                                        bundle.putString("type", "车辆租赁车辆清册")
                                        FragmentHelper.switchFragment(
                                            context as DemandDisplayActivity,
                                            SubmitInventoryItemMoreFragment.newInstance(bundle),
                                            R.id.frame_display_demand,
                                            ""
                                        )
                                    }
                            }
                            if(Temp<sumSize) {
                                carListData.add(MultiStyleItem(MultiStyleItem.Options.BLANK, " "))
                            }
                        }
                        adapter.mData[5].buttonListener = arrayListOf(View.OnClickListener {
                            mData[0].selected = 5
                            mdata.putString("type", "车辆租赁车辆清册")
                            mData[5].itemMultiStyleItem = carListData
                            mdata.putSerializable(
                                "inventory",
                                mData[5].itemMultiStyleItem as Serializable
                            )

                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                SubmitInventoryFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "inventory"
                            )
                        })
                    }
                }
                else{
                    mData[5].necessary =true
                    adapter.mData[5].buttonListener = arrayListOf(View.OnClickListener {
                        ToastHelper.mToast(context, "车辆清册为空 不能填写")})
                }
            }
            //除车辆所有租赁
            Constants.FragmentType.TOOL_LEASING_TYPE.ordinal,//工器具
            Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal,//设备
            Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal//机械
            ->{
                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitBesidesVeichleHire.json")
                adapter = RecyclerviewAdapter(mData)
                lateinit var listData4: List<requirementLeaseProjectList>
                val besidesVeichleListData: MutableList<MultiStyleItem> = ArrayList()
                var typeName:String=""//项目名称
                when (args.getInt("type")) {
                    Constants.FragmentType.TOOL_LEASING_TYPE.ordinal -> {//工器具
                        val Listdata = args.getSerializable("RequirementLeaseConstructionTool") as RequirementLeaseConstructionTool
                        adapter.mData[0].singleDisplayRightContent = if (Listdata.projectName == null) { " " } else { Listdata.projectName }
                        adapter.mData[1].singleDisplayRightContent = if (Listdata.requirementVariety == null) { " " } else { Listdata.requirementVariety }
                        adapter.mData[2].singleDisplayRightContent = UnSerializeDataBase.idCardName
                        adapter.mData[3].singleDisplayRightContent =UnSerializeDataBase.userPhone
                        typeName = if (Listdata.projectName == null) { " " } else { Listdata.projectName }
                    }
                    Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal-> {//设备
                        val Listdata = args.getSerializable("RequirementLeaseFacility") as RequirementLeaseFacility
                        adapter.mData[0].singleDisplayRightContent = if (Listdata.projectName == null) { " " } else { Listdata.projectName }
                        adapter.mData[1].singleDisplayRightContent = if (Listdata.requirementVariety == null) { " " } else { Listdata.requirementVariety }
                        adapter.mData[2].singleDisplayRightContent = UnSerializeDataBase.idCardName
                        adapter.mData[3].singleDisplayRightContent =UnSerializeDataBase.userPhone
                        typeName = if (Listdata.projectName == null) { " " } else { Listdata.projectName }
                    }
                    Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal->{//机械
                        val Listdata = args.getSerializable("RequirementLeaseMachinery") as RequirementLeaseMachinery
                        adapter.mData[0].singleDisplayRightContent = if (Listdata.projectName == null) { " " } else { Listdata.projectName }
                        adapter.mData[1].singleDisplayRightContent = if (Listdata.requirementVariety == null) { " " } else { Listdata.requirementVariety }
                        adapter.mData[2].singleDisplayRightContent = UnSerializeDataBase.idCardName
                        adapter.mData[3].singleDisplayRightContent =UnSerializeDataBase.userPhone
                        typeName = if (Listdata.projectName == null) { " " } else { Listdata.projectName }
                    }

                }
                //////除车辆租赁所有租赁清册查看
                //
                if (args.getSerializable("listData4") != null) {
                    listData4 = args.getSerializable("listData4") as List<requirementLeaseProjectList>
                    if (listData4.isEmpty()) {
                        adapter.mData[4].necessary=true
                        adapter.mData[4].buttonListener = arrayListOf(View.OnClickListener {
                            ToastHelper.mToast(context, "租赁清册为空 不能填写")
                        })
                    } else {
                        val sumSize = listData4.size//清册大小
                        besidesVeichleListData.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,
                                typeName,
                                "${sumSize.toString()}条"
                            )
                        )
                        var temp=0
                        for (j in listData4) {
                            temp++//记录条数
                            val type = j.projectName
                                val position = besidesVeichleListData.size
                                val mData1: List<MultiStyleItem>
                                mdata.putString("type", "租赁清册")
                                mdata.putSerializable("requirementLeaseProjectList",j)
                                mData1 = ApplicationSubmitDetailList(mdata).mData
                                besidesVeichleListData.add(
                                    MultiStyleItem(
                                        MultiStyleItem.Options.SHIFT_INPUT,
                                        type,
                                        false
                                    )
                                )
                                besidesVeichleListData[besidesVeichleListData.size - 1].itemMultiStyleItem =
                                    mData1
                                besidesVeichleListData[besidesVeichleListData.size - 1].necessary =
                                    false
                                besidesVeichleListData[besidesVeichleListData.size - 1].jumpListener =
                                    View.OnClickListener {
                                        besidesVeichleListData[0].selected = position
                                        val bundle = Bundle()
                                        bundle.putSerializable(
                                            "inventoryItem",
                                            besidesVeichleListData[position].itemMultiStyleItem as Serializable
                                        )
                                        bundle.putString("type", "租赁清册")
                                        FragmentHelper.switchFragment(
                                            context as DemandDisplayActivity,
                                            SubmitInventoryItemMoreFragment.newInstance(bundle),
                                            R.id.frame_display_demand,
                                            ""
                                        )
                                    }
                                        if(temp%10==0){//十条加分割线
                                            besidesVeichleListData.add(MultiStyleItem(MultiStyleItem.Options.BLANK, " "))
                                        }
                        }
                        adapter.mData[4].buttonListener = arrayListOf(View.OnClickListener {
                            mData[0].selected = 4
                            mdata.putString("type", "租赁清册")
                            mData[4].itemMultiStyleItem = besidesVeichleListData
                            mdata.putSerializable(
                                "inventory",
                                mData[4].itemMultiStyleItem as Serializable
                            )
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                SubmitInventoryFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "inventory"
                            )
                        })
                    }
                } else {
                    adapter.mData[4].necessary=true
                    adapter.mData[4].buttonListener = arrayListOf(View.OnClickListener {
                        ToastHelper.mToast(context, "租赁清册为空 不能填写")
                    })
                }
            }
            //除资质合作 所有三方
            Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal->{//三方
                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitTripartite.json")
                adapter = RecyclerviewAdapter(mData)
                lateinit var listData5: List<thirdLists>
                val tripartiteListData: MutableList<MultiStyleItem> = ArrayList()
                var requirementType:String=""//项目类型
                val Listdata = args.getSerializable("RequirementThirdPartyDetail") as RequirementThirdPartyDetail
                adapter.mData[0].singleDisplayRightContent = if (Listdata.requirementVariety == null) { " " } else { Listdata.requirementVariety }
                adapter.mData[1].singleDisplayRightContent = UnSerializeDataBase.idCardName
                adapter.mData[2].singleDisplayRightContent =UnSerializeDataBase.userPhone
                requirementType = if (Listdata.requirementVariety == null) { " " } else { Listdata.requirementVariety }
                //////除资质合作 所有三方
                //
                if (args.getSerializable("listData5") != null) {
                    listData5 = args.getSerializable("listData5") as List<thirdLists>
                    if (listData5.isEmpty()) {
                        adapter.mData[3].necessary=true
                        adapter.mData[3].buttonListener = arrayListOf(View.OnClickListener {
                            ToastHelper.mToast(context, "三方清册为空 不能填写")
                        })
                    } else {
                        val sumSize = listData5.size//清册大小
                        tripartiteListData.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,
                                requirementType,
                                "${sumSize.toString()}条"
                            )
                        )
                        var temp=0
                        for (j in listData5) {
                            temp++//记录条数
                            val type = j.projectName
                            val position = tripartiteListData.size
                            val mData1: List<MultiStyleItem>
                            mdata.putString("type", "三方清册")
                            mdata.putSerializable("thirdLists",j)
                            mData1 = ApplicationSubmitDetailList(mdata).mData
                            tripartiteListData.add(
                                MultiStyleItem(
                                    MultiStyleItem.Options.SHIFT_INPUT,
                                    type,
                                    false
                                )
                            )
                            tripartiteListData[tripartiteListData.size - 1].itemMultiStyleItem =
                                mData1
                            tripartiteListData[tripartiteListData.size - 1].necessary =
                                false
                            tripartiteListData[tripartiteListData.size - 1].jumpListener =
                                View.OnClickListener {
                                    tripartiteListData[0].selected = position
                                    val bundle = Bundle()
                                    bundle.putSerializable(
                                        "inventoryItem",
                                        tripartiteListData[position].itemMultiStyleItem as Serializable
                                    )
                                    bundle.putString("type", "三方清册")
                                    FragmentHelper.switchFragment(
                                        context as DemandDisplayActivity,
                                        SubmitInventoryItemMoreFragment.newInstance(bundle),
                                        R.id.frame_display_demand,
                                        ""
                                    )
                                }
                            if(temp%10==0){//十条加分割线
                                tripartiteListData.add(MultiStyleItem(MultiStyleItem.Options.BLANK, " "))
                            }
                        }
                        adapter.mData[3].buttonListener = arrayListOf(View.OnClickListener {
                            mData[0].selected = 3
                            mdata.putString("type", "三方清册")
                            mData[3].itemMultiStyleItem = tripartiteListData
                            mdata.putSerializable(
                                "inventory",
                                mData[3].itemMultiStyleItem as Serializable
                            )
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                SubmitInventoryFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "inventory"
                            )
                        })
                    }
                } else {
                    adapter.mData[3].necessary=true
                    adapter.mData[3].buttonListener = arrayListOf(View.OnClickListener {
                        ToastHelper.mToast(context, "三方清册为空 不能填写")
                    })
                }
            }
        }
        return adapter
    }
    fun ApplicationSubmitDetailList(bundle: Bundle):RecyclerviewAdapter{//需求清册报名
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        lateinit var mData:List<MultiStyleItem>
        lateinit var adapter: RecyclerviewAdapter
        when(bundle.getString("type")) {
            //成员清册详情
            "成员清册" -> {
                mData =
                    itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitMemberList.json")
                adapter = RecyclerviewAdapter(mData)
                val styleTypeNum = arrayListOf(1)
                for (i in styleTypeNum) {
                    adapter.mData[i].styleType = "NUMBER"//限制为数字类型
                }
                val necessaryNum = arrayListOf(0, 1, 2, 4)
                for (j in necessaryNum) {
                    adapter.mData[j].necessary = true//true为必选项
                }
            }
            //车辆清册详情
            "车辆清册" -> {
                mData =
                    itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitVehicleList.json")
                adapter = RecyclerviewAdapter(mData)
                val styleTypeNum = arrayListOf(2,7)
                for (i in styleTypeNum) {
                    adapter.mData[i].styleType = "NUMBER"//限制为数字类型
                }
                val necessaryNum = arrayListOf(0, 1, 2, 4, 5,6,7)
                for (j in necessaryNum) {
                    adapter.mData[j].necessary = true//true为必选项
                }
                adapter.mData[3].jumpListener = View.OnClickListener {
                    val data = Bundle()
                    data.putString("key", mData[3].key)
                    data.putString("title", mData[3].shiftInputTitle)
                    (activity as DemandDisplayActivity).switchFragment(
                        ImageFragment.newInstance(
                            data
                        ), R.id.frame_display_demand, "Capture"
                    )
                }
            }
            //车辆租赁车辆清册详情
            "车辆租赁车辆清册" -> {
                mData =
                    itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitVehicleHireList.json")
                adapter = RecyclerviewAdapter(mData)
                val styleTypeNum = arrayListOf(4,3)
                for (i in styleTypeNum) {
                    adapter.mData[i].styleType = "NUMBER"//限制为数字类型
                }
                val necessaryNum = arrayListOf(0, 1, 2, 3, 4, 6, 7)
                for (j in necessaryNum) {
                    adapter.mData[j].necessary = true//true为必选项
                }
                adapter.mData[5].jumpListener = View.OnClickListener {
                    val data = Bundle()
                    data.putString("key", mData[5].key)
                    data.putString("title", mData[5].shiftInputTitle)
                    (activity as DemandDisplayActivity).switchFragment(
                        ImageFragment.newInstance(
                            data
                        ), R.id.frame_display_demand, "Capture"
                    )
                }
            }
            //机械清册详情
            "机械清册" -> {
                mData =
                    itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitMachineList.json")
                adapter = RecyclerviewAdapter(mData)
                val styleTypeNum = arrayListOf(2)
                for (i in styleTypeNum) {
                    adapter.mData[i].styleType = "NUMBER"//限制为数字类型
                }
                val necessaryNum = arrayListOf(0, 1, 2, 3, 4)
                for (j in necessaryNum) {
                    adapter.mData[j].necessary = true//true为必选项
                }
            }

            //需求租赁清单
            "租赁清册" -> {
                if (bundle.getSerializable("requirementLeaseProjectList") != null) {
                    lateinit var listData4: requirementLeaseProjectList
                    listData4 =
                        bundle.getSerializable("requirementLeaseProjectList") as requirementLeaseProjectList
                    mData =
                        itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitHireList.json")
                    adapter = RecyclerviewAdapter(mData)
                    adapter.mData[0].singleDisplayRightContent =
                        if (listData4.projectName == null) {
                            " "
                        } else {
                            listData4.projectName
                        }
                    adapter.mData[1].singleDisplayRightContent =
                        if (listData4.specificationsModels == null) {
                            " "
                        } else {
                            listData4.specificationsModels
                        }
                    adapter.mData[2].singleDisplayRightContent = if (listData4.quantity == null) {
                        " "
                    } else {
                        listData4.quantity
                    }
                    adapter.mData[3].singleDisplayRightContent = if (listData4.units == null) {
                        " "
                    } else {
                        listData4.units
                    }
                    adapter.mData[5].singleDisplayRightContent = if (listData4.hireTime == null) {
                        " "
                    } else {
                        listData4.hireTime
                    }
                    adapter.mData[6].textAreaContent = if (listData4.detailsExplain == null) {
                        " "
                    } else {
                        listData4.detailsExplain
                    }
                    val necessaryNum = arrayListOf(4)
                    for (j in necessaryNum) {
                        adapter.mData[j].necessary = true//true为必选项
                    }
                } else {
                    mData =
                        itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitHireList.json")
                    adapter = RecyclerviewAdapter(mData)
                    val necessaryNum = arrayListOf(4)
                    for (j in necessaryNum) {
                        adapter.mData[j].necessary = true//true为必选项
                    }
                }

            }
            //需求三方清册
            "三方清册" -> {
                if (bundle.getSerializable("thirdLists") != null) {
                    lateinit var listData5: thirdLists
                    listData5 = bundle.getSerializable("thirdLists") as thirdLists
                    mData =
                        itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitTripartiteList.json")
                    adapter = RecyclerviewAdapter(mData)
                    adapter.mData[0].singleDisplayRightContent =
                        if (listData5.projectName == null) {
                            " "
                        } else {
                            listData5.projectName
                        }
                    adapter.mData[1].singleDisplayRightContent =
                        if (listData5.specificationsModels == null) {
                            " "
                        } else {
                            listData5.specificationsModels
                        }
                    adapter.mData[2].singleDisplayRightContent = if (listData5.units == null) {
                        " "
                    } else {
                        listData5.units
                    }
                    adapter.mData[3].singleDisplayRightContent = if (listData5.quantity == null) {
                        " "
                    } else {
                        listData5.quantity
                    }
                    adapter.mData[5].textAreaContent = if (listData5.detailsExplain == null) {
                        " "
                    } else {
                        listData5.detailsExplain
                    }
                    val styleTypeNum = arrayListOf(4)
                    for (i in styleTypeNum) {
                        adapter.mData[i].styleType = "NUMBER"//限制为数字类型
                    }
                    val necessaryNum = arrayListOf(4)
                    for (j in necessaryNum) {
                        adapter.mData[j].necessary = true//true为必选项
                    }
                } else {
                    mData =
                        itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitTripartiteList.json")
                    adapter = RecyclerviewAdapter(mData)
                    val styleTypeNum = arrayListOf(4)
                    for (i in styleTypeNum) {
                        adapter.mData[i].styleType = "NUMBER"//限制为数字类型
                    }
                    val necessaryNum = arrayListOf(4)
                    for (j in necessaryNum) {
                        adapter.mData[j].necessary = true//true为必选项
                    }

                }

            }
        }
            return adapter
    }
    fun PublishDetailList(bundle: Bundle):RecyclerviewAdapter{//需求清册发布
        val itemGenerate = ItemGenerate()
        itemGenerate.context = context
        lateinit var mData:List<MultiStyleItem>
        lateinit var adapter: RecyclerviewAdapter
        when(bundle.getString("type"))
        {
            //成员清册发布
            "成员清册发布"->{
                mData = itemGenerate.getJsonFromAsset("DemandPublish/ApplicationPublishMemberList.json")
                adapter = RecyclerviewAdapter(mData)
                val styleTypeNum = arrayListOf(1)
                for(i in styleTypeNum){
                    adapter.mData[i].styleType="NUMBER"//限制为数字类型
                }
                val necessaryNum = arrayListOf(0,1,2,4)
                for(j in necessaryNum){
                    adapter.mData[j].necessary= true//true为必选项
                }
            }
            "车辆清册发布"->{
                mData = itemGenerate.getJsonFromAsset("DemandPublish/ApplicationPublishVehicleList.json")
                adapter = RecyclerviewAdapter(mData)
                val styleTypeNum = arrayListOf(1)
                for(i in styleTypeNum){
                    adapter.mData[i].styleType="NUMBER"//限制为数字类型
                }
                mData[6].styleType="NUMBER_FLOAT"
                val necessaryNum = arrayListOf(0,1,2,3,4)
                for(j in necessaryNum){
                    adapter.mData[j].necessary= true//true为必选项
                }
            }
            //租赁清册发布
            "租赁清册发布"->{
                lateinit var listData1:List<requirementLeaseProjectList>
                //listData1=bundle.getSerializable("listData4") as List<requirementLeaseProjectList>
                mData = itemGenerate.getJsonFromAsset("DemandPublish/ApplicationPublishHireList.json")
                adapter = RecyclerviewAdapter(mData)
                val styleTypeNum = arrayListOf(3)
                for(i in styleTypeNum){
                    adapter.mData[i].styleType="NUMBER"//限制为数字类型
                }
                mData[6].styleType="NUMBER_FLOAT"
                val necessaryNum = arrayListOf(0,1,2,3,5)
                for(j in necessaryNum){
                    adapter.mData[j].necessary= true//true为必选项
                }
//                    adapter.mData[4].singleDisplayRightContent = if (i.units == null) {
//                        "无数据"
//                    } else {
//                        i.units
//                    }
            }
            //三方服务清册发布
            "三方服务清册发布"->{
                lateinit var listData1:List<requirementLeaseProjectList>
                //listData1=bundle.getSerializable("listData4") as List<requirementLeaseProjectList>
                mData = itemGenerate.getJsonFromAsset("DemandPublish/ApplicationPubilshTripartiteList.json")
                adapter = RecyclerviewAdapter(mData)
                val necessaryNum = arrayListOf(0,1,2,3)
                for(j in necessaryNum){
                    adapter.mData[j].necessary= true//true为必选项
                }
//                    adapter.mData[4].singleDisplayRightContent = if (i.units == null) {
//                        "无数据"
//                    } else {
//                        i.units
//                    }
            }
        }
        return adapter
//            "成员清册发布"->{
//                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitMemberList.json")
//                adapter = RecyclerviewAdapter(mData)
//                adapter.mData[0].backListener = View.OnClickListener {
//                    activity.supportFragmentManager.popBackStackImmediate()
//                }
//                adapter.mData[0].tvSelectListener = View.OnClickListener {
//                    // Log.i("fragment is ok?",(activity.supportFragmentManager.findFragmentByTag("inventory") is InventoryFragment).toString())
//                    val fragment = activity.supportFragmentManager.findFragmentByTag("inventory") as SupplyPublishInventoryFragment
//                    activity.supportFragmentManager.popBackStackImmediate()
//                    fragment.update(adapter.mData)
//                }
            //   }

//            //机械清册详情
//            "机械清册"->{
//                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitMachineList.json")
//                adapter = RecyclerviewAdapter(mData)
//                adapter.mData[0].backListener = View.OnClickListener {
//                    activity.supportFragmentManager.popBackStackImmediate()
//                }
//                adapter.mData[0].tvSelectListener = View.OnClickListener {
//                    // Log.i("fragment is ok?",(activity.supportFragmentManager.findFragmentByTag("inventory") is InventoryFragment).toString())
//                    val fragment = activity.supportFragmentManager.findFragmentByTag("inventory") as SubmitInventoryFragment
//                    activity.supportFragmentManager.popBackStackImmediate()
//                    fragment.update(adapter.mData)
//                }
//            }
//            //清工薪资清册详情
//            "清工薪资清册"->{
//                //lateinit var listData1:List<requirementTeamProjectList>
//                //lateinit var listData2:List<requirementPowerTransformationSalary>
//                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitCleanWorkList.json")
//                adapter = RecyclerviewAdapter(mData)
//                adapter.mData[0].backListener = View.OnClickListener {
//                    activity.supportFragmentManager.popBackStackImmediate()
//                }
//                adapter.mData[0].tvSelectListener = View.OnClickListener {
//                    // Log.i("fragment is ok?",(activity.supportFragmentManager.findFragmentByTag("inventory") is InventoryFragment).toString())
//                    val fragment = activity.supportFragmentManager.findFragmentByTag("inventory") as SubmitInventoryFragment
//                    activity.supportFragmentManager.popBackStackImmediate()
//                    fragment.update(adapter.mData)
//                }
//            }
//            //清单报价清册详情
//            "清单报价清册"->{
//                lateinit var list:requirementTeamProjectList
//                list=bundle.getSerializable("list") as requirementTeamProjectList
//                mData = itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitCleanSingleList.json")
//                adapter = RecyclerviewAdapter(mData)
//                adapter.mData[0].backListener = View.OnClickListener {
//                    activity.supportFragmentManager.popBackStackImmediate()
//                }
//                adapter.mData[0].tvSelectListener = View.OnClickListener {
//                    // Log.i("fragment is ok?",(activity.supportFragmentManager.findFragmentByTag("inventory") is InventoryFragment).toString())
//                    val fragment =
//                        activity.supportFragmentManager.findFragmentByTag("inventory") as SubmitInventoryFragment
//                    activity.supportFragmentManager.popBackStackImmediate()
//                    fragment.update(adapter.mData)
//                }
//                adapter.mData[1].singleDisplayRightContent = if (list.projectName == null) {
//                    "无数据"
//                } else {
//                    list.projectName
//                }
//                adapter.mData[2].singleDisplayRightContent =
//                    if (list.specificationsModels == null) {
//                        "无数据"
//                    } else {
//                        list.specificationsModels
//                    }
//                adapter.mData[3].singleDisplayRightContent = if (list.quantity == null) {
//                    "无数据"
//                } else {
//                    list.quantity
//                }
//                adapter.mData[4].singleDisplayRightContent = if (list.units == null) {
//                    "无数据"
//                } else {
//                    list.units
//                }
//                adapter.mData[6].singleDisplayRightContent = if (list.detailsExplain == null) {
//                    "无数据"
//                } else {
//                    list.detailsExplain
//                }
//            }

    }
}