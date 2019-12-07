package com.example.eletronicengineer.fragment.sdf

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bin.david.form.data.Column
import com.bin.david.form.data.table.TableData
import com.codekidlabs.storagechooser.StorageChooser
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.DemandActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.model.User
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.startSendMessage
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.downloadFile
import com.example.eletronicengineer.utils.startSendMessage
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_demand.*
import kotlinx.android.synthetic.main.activity_demand.view.*
import kotlinx.android.synthetic.main.fragemt_with_inventory.view.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class DemandFragment:Fragment() {

    companion object{
        fun newInstance(args: Bundle): DemandFragment
        {
            val fragment= DemandFragment()
            fragment.arguments=args
            return fragment
        }
    }
    var type:Int=0
    var  selectContent2:String=""
    lateinit var id:String
    val mdata = Bundle()
    lateinit var mData: List<MultiStyleItem>
    lateinit var multiButtonListeners: MutableList<View.OnClickListener>
    lateinit var mView: View
    //List<MultiStyleItem> mData=new ArrayList<>()
    var mAdapter: RecyclerviewAdapter?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.activity_demand, container, false)
        initFragment(mView)
        return mView
    }
    fun initFragment(mView:View) {

        multiButtonListeners = ArrayList()
        //获得界面类型的数值
//        val intent = getIntent()
//        val type = intent.getIntExtra("type", 0)
        type=arguments!!.getInt("type")
        if(mAdapter==null){
            val result = Observable.create<RecyclerviewAdapter>{
                it.onNext(switchAdapter(type))
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mAdapter=it
                    mView.rv_main_content.adapter = mAdapter
                    mView.rv_main_content.layoutManager = LinearLayoutManager(mView.context)
                }

        }
        else{
            mView.rv_main_content.adapter = mAdapter
            mView.rv_main_content.layoutManager = LinearLayoutManager(mView.context)
        }
        //标题
          selectContent2=arguments!!.getString("selectContent2")
        mView.tv_title_title1.setText(selectContent2)
        //返回
        mView.tv_title_back.setOnClickListener{
                UnSerializeDataBase.imgList.clear()
                activity!!.finish()
        }
        //发布
        mView.submit.setOnClickListener{

            val networkAdapter= NetworkAdapter(mAdapter!!.mData, submit.context)
            if(networkAdapter.check()){
                if (UnSerializeDataBase.fileList.size!=0||(UnSerializeDataBase.imgList.size!=0))
                    networkAdapter.generateMultiPartRequestBody(UnSerializeDataBase.dmsBasePath+mAdapter!!.urlPath)
                else
                {
                    val json= JSONObject()
                    json.put("requirementType", selectContent2)
                    val loadingDialog = LoadingDialog(mView.context, "正在发布中...", R.mipmap.ic_dialog_loading)
                    loadingDialog.show()
                    networkAdapter.generateJsonRequestBody(json)
                        .subscribe {
                            val result =
                                startSendMessage(it, UnSerializeDataBase.dmsBasePath+mAdapter!!.urlPath).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                    .subscribe(
                                        {
                                            loadingDialog.dismiss()
                                            val json = JSONObject(it.string())
                                            if (json.getInt("code") == 200) {
                                                ToastHelper.mToast(mView.context, "发布成功")
                                                mView.tv_title_back.callOnClick()
                                            } else if (json.getInt("code") == 400) {
                                                ToastHelper.mToast(mView.context, "发布失败")
                                            }
                                        },
                                        {
                                            loadingDialog.dismiss()
                                            ToastHelper.mToast(mView.context, "发布信息异常")
                                            it.printStackTrace()
                                        }
                                    )
                        }
                }
            }
        }
    }
    //选择加载的界面
    fun switchAdapter(Type: Int): RecyclerviewAdapter {
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = mView.context
        adapterGenerate.activity = activity as DemandActivity
        lateinit var adapter: RecyclerviewAdapter
        when (Type) {
            Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayRightContent = "普工"
                var selectOption1Items: List<String> = listOf("普工")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_SPECIAL_WORK_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayRightContent = "特种作业"
                var selectOption1Items: List<String> =
                    listOf("低压电工作业", "高压电工作业", "电力电缆作业", "继电保护作业", "电气试验作业", "融化焊接与热切割作业", "登高架设作业")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items

            }
            Constants.FragmentType.PERSONAL_PROFESSIONAL_OPERATION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayRightContent = "专业操作"
                var selectOption1Items: List<String> =
                    listOf("压接操作", "机动绞磨操作", "牵张设备操作", "起重机械操作", "钢筋工", "混凝土工", "木工", "模板工", "油漆工", "砌筑工", "通风工", "打桩工", "架子工工")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_SURVEYOR_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayRightContent = "测量工"
                var selectOption1Items: List<String> = listOf("测量工")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items

            }
            Constants.FragmentType.PERSONAL_DRIVER_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayRightContent = "驾驶员"
                var selectOption1Items: List<String> =
                    listOf("驾驶证A1", "驾驶证A2", "驾驶证A3", "驾驶证B1", "驾驶证B2", "驾驶证C1", "驾驶证C2", "驾驶证C3", "驾驶证D", "驾驶证E")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_NINE_MEMBERS_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayRightContent = "九大员"
                var selectOption1Items: List<String> =
                    listOf("施工员", "安全员", "质量员", "材料员", "资料员", "预算员", "标准员", "机械员", "劳务员")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_REGISTRATION_CLASS_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayRightContent = "注册类"
                var selectOption1Items: List<String> = listOf("造价工程师", "一级建造师", "安全工程师", "电气工程师")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_OTHER_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                adapter.mData[0].options= MultiStyleItem.Options.SINGLE_INPUT
                adapter.mData[0].inputSingleTitle=adapter.mData[0].singleDisplayRightTitle
                adapter.mData[1].options= MultiStyleItem.Options.SINGLE_INPUT
                adapter.mData[1].inputSingleTitle=adapter.mData[1].selectTitle
            }
            Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupSubstationConstruction()
                var singleDisplayRightContent = "变电施工队"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementPowerTransformation
            }
            Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupSubstationConstruction()
                var singleDisplayRightContent = "主网施工队"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementMajorNetwork
            }
            Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupSubstationConstruction()
                var singleDisplayRightContent = "配网施工队"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementDistribuionNetwork
            }
            Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal -> {//测量设计
                adapter = adapterGenerate.DemandGroupMeasurementDesign()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementMeasureDesign
            }
            Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal -> {//马帮运输
                adapter = adapterGenerate.DemandGroupCaravanTransportation()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementCaravanTransport
            }
            Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal -> {//桩基服务
                adapter = adapterGenerate.DemandGroupPileFoundationConstruction()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementPileFoundation
            }
            Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal -> {//非开挖
                adapter = adapterGenerate.DemandGroupNonExcavation()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementUnexcavation
            }
            Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal -> {//试验调试
                adapter = adapterGenerate.DemandGroupTestDebugging()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementTest
            }
            Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal -> {//跨越架
                adapter = adapterGenerate.DemandGroupCrossingFrame()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementSpanWoodenSupprt
            }
            Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal -> {//运行维护
                adapter = adapterGenerate.DemandGroupOperationAndMaintenance()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementRunningMaintain
            }
            Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal -> {//车辆租赁
                adapter = adapterGenerate.DemandLeaseVehicleLeasing()
                adapter.mData[0].singleDisplayRightContent="车辆租赁"
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementLeaseCar

            }
            Constants.FragmentType.TOOL_LEASING_TYPE.ordinal -> {//工器具租赁
                adapter = adapterGenerate.DemandEquipmentLeasing()
                adapter.mData[0].singleDisplayRightContent="工器具租赁"
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementLeaseConstructionTool
            }
            Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal -> {
                var singleDisplayRightContent = "机械租赁"
                adapter = adapterGenerate.DemandEquipmentLeasing()
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementLeaseMachinery
            }
            Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal -> {
                var singleDisplayRightContent = "设备租赁"
                adapter = adapterGenerate.DemandEquipmentLeasing()
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Requirement.requirementLeaseFacility
            }
            Constants.FragmentType.TRIPARTITE_TRAINING_CERTIFICATE_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                var singleDisplayRightContent = "培训办证"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
            }
            Constants.FragmentType.TRIPARTITE_FINANCIAL_ACCOUNTING_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                var singleDisplayRightContent = "财务记账"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
            }
            Constants.FragmentType.TRIPARTITE_AGENCY_QUALIFICATION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                var singleDisplayRightContent = "代办资格"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
            }
            Constants.FragmentType.TRIPARTITE_TENDER_SERVICE_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                var singleDisplayRightContent = "标书服务"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
            }
            Constants.FragmentType.TRIPARTITE_LEGAL_ADVICE_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                var singleDisplayRightContent = "法律咨询"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
            }
            Constants.FragmentType.TRIPARTITE_SOFTWARE_SERVICE_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                var singleDisplayRightContent = "软件服务"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
            }
            Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                adapter.mData[0].options= MultiStyleItem.Options.SINGLE_INPUT
                adapter.mData[0].inputSingleTitle=adapter.mData[0].singleDisplayRightTitle
                adapter.mData[0].inputSingleContent = adapter.mData[0].singleDisplayRightContent
            }
        }
        return adapter
    }

    fun initTable()
    {
        val column1 = Column<String>("姓名", "name")
        val column2 = Column<String>("年龄", "age")
        val column3 = Column<String>("更新时间", "time")
        val column4 = Column<String>("头像", "portrait")
        //组合列
        val totalColumn1 = Column<Any>("组合列名", column1, column2,column3,column4)
        val userList:MutableList<User> =ArrayList(5)

        val tableData: TableData<User> = TableData("表格名",userList,column1,column2,column3,column4)
        st_main_table.tableData=tableData
        st_main_table.setZoom(true)
    }
//    fun initRetrofit(targetFileName:String,webFileName:String,baseUrl:String)
//    {
//        val chooser=
//            StorageChooser.Builder().withActivity(activity as DemandActivity).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(fragmentManager).withMemoryBar(true).build()
//        chooser.show()
//        chooser.setOnSelectListener {
//            downloadFile(it,targetFileName,webFileName,baseUrl)
//        }
//
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal -> {
//                    val bmp = data?.extras!!.get("data") as Bitmap
//                    val picName= UUID.randomUUID().toString()+".png"
//                    val file= File(this.filesDir.absolutePath+picName)
//                    val fos= FileOutputStream(file)
//                    bmp.compress(Bitmap.CompressFormat.PNG,100,fos)
//                    val imgMap= UnSerializeDataBase.imgList.get(UnSerializeDataBase.imgList.size-1)
//                    imgMap.path=file.absolutePath
//                    imgMap.needDelete=true
//                    UnSerializeDataBase.imgList.set(UnSerializeDataBase.imgList.size-1,imgMap)
//                }
//                Constants.RequestCode.REQUEST_PICK_FILE.ordinal -> {
//                    val uri = data!!.data
//                    var path:String?=null
//                    if (uri!!.toString().contains("content")) {
//                        path = getRealPathFromURI(uri)
//                        Log.i("path", path)
//                        val fileMap= UnSerializeDataBase.fileList.get(UnSerializeDataBase.fileList.size-1)
//                        fileMap.path=path!!
//                        UnSerializeDataBase.fileList.set(UnSerializeDataBase.fileList.size-1,fileMap)
//                    }
//                    else
//                    {
//                        val file = File(uri.toString())
//                        if (file.exists())
//                        {
//                            Log.i("file", file.name)
//                        }
//                        val fileMap= UnSerializeDataBase.fileList.get(UnSerializeDataBase.fileList.size-1)
//                        fileMap.path=uri.toString()
//                        UnSerializeDataBase.fileList.set(UnSerializeDataBase.fileList.size-1,fileMap)
//                    }
//
//                    //val resultFile= File()
//                }
//            }
//        }
//        else
//        {
//            if (UnSerializeDataBase.fileList.size!=0)
//            {
//                val fileMap= UnSerializeDataBase.fileList.get(UnSerializeDataBase.fileList.size-1)
//                if (fileMap.path=="")
//                {
//                    UnSerializeDataBase.fileList.removeAt(UnSerializeDataBase.fileList.size-1)
//                }
//            }
//            else if(UnSerializeDataBase.imgList.size!=0)
//            {
//                val imgMap= UnSerializeDataBase.imgList.get(UnSerializeDataBase.imgList.size-1)
//                if (imgMap.path=="")
//                {
//                    UnSerializeDataBase.imgList.removeAt(UnSerializeDataBase.imgList.size-1)
//                }
//            }
//
//        }
//    }
//
//    fun getRealPathFromURI(contentUri: Uri): String? {
//        var res: String? = null
//        val projection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
//        var cursor = contentResolver.query(contentUri, projection, null, null, null)
//        try {
//            if (cursor != null) {
//                val column = cursor.getColumnIndexOrThrow(projection[0])
//                if (cursor.moveToFirst()) {
//                    res = cursor.getString(column)
//                }
//                cursor.close()
//            }
//            if (res == null) {
//                cursor =
//                    contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)
//                if (cursor != null) {
//                    val column = cursor.getColumnIndexOrThrow(projection[0])
//                    if (cursor.moveToFirst()) {
//                        res = cursor.getString(column)
//                    }
//                    cursor.close()
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return res
//    }
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
            mAdapter!!.mData[mAdapter!!.mData[0].selected].necessary = true
        }
        Log.i("position is",mAdapter!!.mData[0].selected.toString())
        mAdapter!!.mData[mAdapter!!.mData[0].selected].itemMultiStyleItem = itemMultiStyleItem
        mAdapter!!.mData[mAdapter!!.mData[0].selected+1].singleDisplayRightContent=itemMultiStyleItem[0].PeopleNumber.toString()
        Log.i("item size",mAdapter!!.mData[mAdapter!!.mData[0].selected].itemMultiStyleItem.size.toString())
    }
}