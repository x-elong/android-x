package com.example.eletronicengineer.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.codekidlabs.storagechooser.StorageChooser
import com.electric.engineering.model.MultiStyleItem
import com.electric.engineering.utils.ItemGenerate
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.fragment.InventoryFragment
import com.example.eletronicengineer.fragment.ShiftInputFragment
import com.example.eletronicengineer.fragment.UploadPhoneFragment
import com.example.eletronicengineer.model.Constants
import kotlinx.android.synthetic.main.item_supply.*
import java.util.zip.DataFormatException


class AdapterGenerate
{
    //转化成常量类型
    fun getType(content:String):Int{
        when(content){
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
            else ->return -1
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
                if (adapter.VHList.size==17)
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
                if (adapter.VHList.size==21)
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
        multiButtonListeners.add(cameraListener)//工程量清册
        multiButtonListeners.add(uploadProjectListListener)
        mData[5].buttonListener=multiButtonListeners
        mData[15].buttonListener=multiButtonListenersForSalary

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning()
            {
                if (adapter.VHList.size==21)
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
        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning()
            {
                if (adapter.VHList.size==18)
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

        val uploadProjectListListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
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
        multiButtonListeners.add(uploadProjectListListener)//工程清册
        multiButtonListeners.add(cameraListener)

        val mData=itemGenerate.getJsonFromAsset("Demand/DemandGroup(Pile foundation construction).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        mData[5].buttonListener=multiButtonListeners

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if (adapter.VHList.size==21)
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

        val uploadProjectListListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
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
        multiButtonListeners.add(uploadProjectListListener)//工程清册
        multiButtonListeners.add(cameraListener)

        val mData=itemGenerate.getJsonFromAsset("Demand/DemandGroup(Non-excavation).json")
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        mData[5].buttonListener=multiButtonListeners

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if (adapter.VHList.size==21)
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
                if (adapter.VHList.size==21)
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
                if (adapter.VHList.size==19)
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
        multiButtonListeners.add(uploadProjectListListener)
        multiButtonListeners.add(cameraListener)
        mData[5].buttonListener=multiButtonListeners
        mData[12].buttonListener=multiButtonListenersForSalary

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if (adapter.VHList.size==18)
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
                (activity.rv_main_content.findViewHolderForAdapterPosition(22) as RecyclerviewAdapter.VH).etInputUnitValue.hint="1-90"
            }
            override fun onBindRunning() {
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
                if (adapter.VHList.size==13)
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
                if(adapter.VHList.size==8){
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
            (activity as SupplyActivity).switchFragment(UploadPhoneFragment(),"Capture")
        }

        val adapter = RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if(adapter.VHList.size==11){
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
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        //上传
        mData[0].backListener=View.OnClickListener {
            activity.finish()
        }
        multiButtonListeners.add(View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[13].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        })
        mData[13].buttonListener=multiButtonListeners

        for (j in 3 until 11)
        {
            val jumpListener=View.OnClickListener{
                val data= Bundle()
                data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_PERSONAL_FRAGMENT.ordinal)
                data.putString("title",mData[j].shiftInputTitle)
                val fragment=InventoryFragment.newInstance(data)
                (activity as SupplyActivity).switchFragment(fragment,"Inventory")
            }
            mData[j].jumpListener=jumpListener
        }
        mData[8].jumpListener=View.OnClickListener {
            val data=Bundle()
            data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_VEHICLE_FRAGMENT.ordinal)
            data.putString("title",mData[8].shiftInputTitle)
            val fragment=InventoryFragment.newInstance(data)
            (activity as SupplyActivity).switchFragment(fragment,"Inventory")
        }
        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }

            override fun onBindRunning() {
                if(adapter.VHList.size==15){
                    adapter.VHList[14].etInputUnitValue.hint="1-90"
                }
            }
        }
        return adapter

    }
    //团队服务——测量设计
    fun ProviderGroupMeasurementDesign():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Measurement Design).json")
        mData[0].backListener= View.OnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
        for (j in 3 until 12)
        {
            val jumpListener=View.OnClickListener{
                val data= Bundle()
                data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_PERSONAL_FRAGMENT.ordinal)
                data.putString("title",mData[j].shiftInputTitle)
                val fragment=InventoryFragment.newInstance(data)
                (activity as SupplyActivity).switchFragment(fragment,"Inventory")
            }
            mData[j].jumpListener=jumpListener
        }
        mData[9].jumpListener=View.OnClickListener {
            val data=Bundle()
            data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_VEHICLE_FRAGMENT.ordinal)
            data.putString("title",mData[9].shiftInputTitle)
            val fragment=InventoryFragment.newInstance(data)
            (activity as SupplyActivity).switchFragment(fragment,"Inventory")
        }
        multiButtonListeners.add(View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[14].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        })
        mData[14].buttonListener=multiButtonListeners
        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
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

    //团队服务——马帮运输
    fun ProviderGroupCaravanTransportation():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Caravan transportation).json")
        mData[0].backListener= View.OnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
        //个人信息
        for (j in 3 until 8)
        {
            mData[j].jumpListener=View.OnClickListener{
                val data= Bundle()
                data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_PERSONAL_FRAGMENT.ordinal)
                data.putString("title",mData[j].shiftInputTitle)
                val fragment=InventoryFragment.newInstance(data)
                (activity as SupplyActivity).switchFragment(fragment,"Inventory")
            }
        }
        val adapter=RecyclerviewAdapter(mData)
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

    //团队服务——桩机服务
    fun ProviderGroupPileFoundationConstruction():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Pile foundation construction).json")
        mData[0].backListener= View.OnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        for (j in 3 until 11)
        {
            val jumpListener=View.OnClickListener{
                val data= Bundle()
                data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_PERSONAL_FRAGMENT.ordinal)
                data.putString("title",mData[j].shiftInputTitle)
                val fragment=InventoryFragment.newInstance(data)
                (activity as SupplyActivity).switchFragment(fragment,"Inventory")
            }
            mData[j].jumpListener=jumpListener
        }
        mData[8].jumpListener=View.OnClickListener {
            val data=Bundle()
            data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_VEHICLE_FRAGMENT.ordinal)
            data.putString("title",mData[8].shiftInputTitle)
            val fragment=InventoryFragment.newInstance(data)
            (activity as SupplyActivity).switchFragment(fragment,"Inventory")
        }
        multiButtonListeners.add(View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[14].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        })
        mData[14].buttonListener=multiButtonListeners
        val adapter = RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
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

    //团队服务——非开挖
    fun ProviderGroupNonExcavation():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Non-excavation).json")
        mData[0].backListener= View.OnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        for (j in 3 until 10)
        {
            val jumpListener=View.OnClickListener{
                val data= Bundle()
                data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_PERSONAL_FRAGMENT.ordinal)
                data.putString("title",mData[j].shiftInputTitle)
                val fragment=InventoryFragment.newInstance(data)
                (activity as SupplyActivity).switchFragment(fragment,"Inventory")
            }
            mData[j].jumpListener=jumpListener
        }
        mData[7].jumpListener=View.OnClickListener {
            val data=Bundle()
            data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_VEHICLE_FRAGMENT.ordinal)
            data.putString("title",mData[7].shiftInputTitle)
            val fragment=InventoryFragment.newInstance(data)
            (activity as SupplyActivity).switchFragment(fragment,"Inventory")
        }
        multiButtonListeners.add(View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[10].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        })
        mData[10].buttonListener=multiButtonListeners
        val adapter = RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
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
    //团队服务——试验调试
    fun ProviderGroupTestDebugging():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        lateinit var adapter:RecyclerviewAdapter
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Provider/TeamService/ProvicerGroup(Test debugging).json")
        mData[0].backListener= View.OnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
        for (j in 3 until 10)
        {
            val jumpListener=View.OnClickListener{
                val data= Bundle()
                data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_PERSONAL_FRAGMENT.ordinal)
                data.putString("title",mData[j].shiftInputTitle)
                val fragment=InventoryFragment.newInstance(data)
                (activity as SupplyActivity).switchFragment(fragment,"Inventory")
            }
            mData[j].jumpListener=jumpListener
        }
        mData[7].jumpListener=View.OnClickListener {
            val data=Bundle()
            data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_VEHICLE_FRAGMENT.ordinal)
            data.putString("title",mData[7].shiftInputTitle)
            val fragment=InventoryFragment.newInstance(data)
            (activity as SupplyActivity).switchFragment(fragment,"Inventory")
        }
        multiButtonListeners.add(View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[13].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        })
        mData[13].buttonListener=multiButtonListeners
        adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if(adapter.VHList.size==15){
                    adapter.VHList[14].etInputUnitValue.hint="1-90"
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
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Provider/TeamService/ProvicerGroup(Crossing frame).json")
        mData[0].backListener= View.OnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
        for (j in 3 until 10)
        {
            val jumpListener=View.OnClickListener{
                val data= Bundle()
                data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_PERSONAL_FRAGMENT.ordinal)
                data.putString("title",mData[j].shiftInputTitle)
                val fragment=InventoryFragment.newInstance(data)
                (activity as SupplyActivity).switchFragment(fragment,"Inventory")
            }
            mData[j].jumpListener=jumpListener
        }
        mData[7].jumpListener=View.OnClickListener {
            val data=Bundle()
            data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_VEHICLE_FRAGMENT.ordinal)
            data.putString("title",mData[7].shiftInputTitle)
            val fragment=InventoryFragment.newInstance(data)
            (activity as SupplyActivity).switchFragment(fragment,"Inventory")
        }
        multiButtonListeners.add(View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[10].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        })
        mData[10].buttonListener=multiButtonListeners
        adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
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
    //团队服务——运行维护
    fun OperationAndMaintenance():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        itemGenerate.context=context
        val mData = itemGenerate.getJsonFromAsset("Provider/TeamService/ProviderGroup(Operation and Maintenance).json")
        mData[0].backListener= View.OnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
        for (j in 3 until 8)
        {
            val jumpListener=View.OnClickListener{
                val data= Bundle()
                data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_PERSONAL_FRAGMENT.ordinal)
                data.putString("title",mData[j].shiftInputTitle)
                val fragment=InventoryFragment.newInstance(data)
                (activity as SupplyActivity).switchFragment(fragment,"Inventory")
            }
            mData[j].jumpListener=jumpListener
        }
        mData[5].jumpListener=View.OnClickListener {
            val data=Bundle()
            data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_VEHICLE_FRAGMENT.ordinal)
            data.putString("title",mData[5].shiftInputTitle)
            val fragment=InventoryFragment.newInstance(data)
            (activity as SupplyActivity).switchFragment(fragment,"Inventory")
        }
        multiButtonListeners.add(View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra("key",mData[9].key)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        })
        mData[9].buttonListener=multiButtonListeners
        val adapter = RecyclerviewAdapter(mData)
        adapter.adapterObserver = object :ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if(adapter.VHList.size==11){
                    adapter.VHList[10].etInputUnitValue.hint="1-90"
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
            (activity as SupplyActivity).switchFragment(UploadPhoneFragment(),"Capture")

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
            (activity as SupplyActivity).switchFragment(UploadPhoneFragment(),"Capture")
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
            (activity as SupplyActivity).switchFragment(UploadPhoneFragment(),"Capture")
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
            val data= Bundle()
            data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_PERSONAL_FRAGMENT.ordinal)
            (activity as SupplyActivity).switchFragment(UploadPhoneFragment(),"Capture")
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
            val data= Bundle()
            data.putInt("type",Constants.FragmentType.PROVIDER_GROUP_PERSONAL_FRAGMENT.ordinal)
            (activity as SupplyActivity).switchFragment(UploadPhoneFragment(),"Capture")
        }
        adapter=RecyclerviewAdapter(mData)
        return adapter
    }
}