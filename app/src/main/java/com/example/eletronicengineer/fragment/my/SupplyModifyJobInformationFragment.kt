package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyReleaseActivity
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.distributionFileSave.*
import com.example.eletronicengineer.fragment.sdf.PublishInventoryItemMoreFragment
import com.example.eletronicengineer.fragment.sdf.SupplyPublishInventoryItemMoreFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.putSimpleMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_supply_publish.*
import kotlinx.android.synthetic.main.activity_supply_publish.view.*
import kotlinx.android.synthetic.main.activity_supply_publish.view.submit
import kotlinx.android.synthetic.main.fragment_modify_job_information.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray

import org.json.JSONObject
import java.io.File
import java.io.Serializable

class SupplyModifyJobInformationFragment:Fragment(){
    companion object{
        fun newInstance(args:Bundle): SupplyModifyJobInformationFragment
        {
            val fragment= SupplyModifyJobInformationFragment()
            fragment.arguments=args
            return fragment
        }
    }
    var mAdapter:RecyclerviewAdapter?=null
    var comment:String=""//备注
    var isCar:Boolean=true
    var isConstructionTool:Boolean=true
    var validTime:Long?= Long.MIN_VALUE//有效期
    var horseNumber:Long?= Long.MIN_VALUE//马匹数量
    var name:String=""//名称
    var voltages:ArrayList<String> = arrayListOf() //电压表等级
    var workDia:Int= Int.MIN_VALUE//作业直径
    var location:String=""//队部所在区域
    //车辆表
    var carType:String=""//车辆类型
    var maxPassengers:Long= Long.MIN_VALUE//
    var construction:Int=-1//车辆结构
    var isDriver:Boolean =false
    var isInsurance:Boolean =false
    var carPhotoPath:String=""//车辆照片
    var carNumber:String=""//拍照号码

    var type = 0
    lateinit var mView:View
    lateinit var id:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i("onCreateView","running")
        mView= inflater.inflate(R.layout.fragment_modify_job_information,container,false)
        type = arguments!!.getInt("type")
        id = arguments!!.getString("id")
        initFragment()
        return mView
    }
    fun initFragment(){
        mView.tv_modify_job_information_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
            UnSerializeDataBase.imgList.clear()
        }
        //加载选择的界面
        if(mAdapter==null){
            val result = Observable.create<RecyclerviewAdapter>{
                val adapter = switchAdapter(type)
                it.onNext(adapter)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mAdapter=it
                    mView.rv_job_information_content.adapter=mAdapter
                    mView.rv_job_information_content.layoutManager= LinearLayoutManager(context)
                }

        }
        else{
            mView.rv_job_information_content.adapter=mAdapter
            mView.rv_job_information_content.layoutManager= LinearLayoutManager(context)
        }
        //发布
        mView.btn_modify_job_information.setOnClickListener{

            val networkAdapter= NetworkAdapter(mAdapter!!.mData, mView.context)
            val provider=NetworkAdapter.Provider(mAdapter!!.mData,mView.context)
            if(networkAdapter.check()){
                    for(i in mAdapter!!.mData ) {
                        when (i.options){
                            MultiStyleItem.Options.INPUT_WITH_UNIT->{

                                when(i.inputUnitTitle){
                                        "有效期"->{
                                            val tmp = i.inputUnitContent.toLongOrNull()
                                            validTime = if (tmp != null) { tmp } else {10}
                                        }
                                    "马匹数量"->{
                                        val tmp = i.inputUnitContent.toLongOrNull()
                                        horseNumber = if (tmp != null) { tmp } else {10}
                                    }
                                    "核载乘客"->{
                                        val tmp = i.inputUnitContent.toLongOrNull()
                                        maxPassengers =if (tmp != null) { tmp } else {10}
                                    }
                                }
                            }
                            MultiStyleItem.Options.MULTI_RADIO_BUTTON -> {
                                when(i.radioButtonTitle) {
                                    "是否配驾驶员"->{

                                     val   tmp = i.radioButtonValue
                                       if(tmp=="1"){
                                           isDriver=true
                                       }else{
                                           isDriver=false
                                       }
                                    }
                                    "保险状态"->{
                                        val   tmp = i.radioButtonValue
                                        if(tmp=="1"){
                                            isInsurance=true
                                        }else{
                                            isInsurance=false
                                        }
                                    }
                                }
                            }
//                            MultiStyleItem.Options.SHIFT_INPUT -> {
//                                when(i.shiftInputTitle) {
//                                    "车辆照片"->{
//                                        val results = try {
//                                            for (j in UnSerializeDataBase.imgList) {
//                                                if (j.key == i.key) {
//                                                    val imagePath = j.path.split("|")
//                                                    for (k in imagePath) {
//                                                        val file = File(k)
//                                                        val imagePart = MultipartBody.Part.createFormData(
//                                                            "file",
//                                                            file.name,
//                                                            RequestBody.create(MediaType.parse("image/*"), file)
//                                                        )
//                                                        uploadImage(imagePart).observeOn(AndroidSchedulers.mainThread()).subscribe(
//                                                            {
//                                                                if (carPhotoPath != "")
//                                                                    carPhotoPath += "|"
//                                                                val json = JSONObject(it.string())
//                                                                if(json.getBoolean("success")){
//                                                                    carPhotoPath += json.getString("httpUrl")
//                                                                }else{
//                                                                    carPhotoPath += ""
//                                                                }
//                                                            },
//                                                            {
//                                                                it.printStackTrace()
//                                                            })
//                                                    }
//                                                }
//                                            }
//                                        } catch (e: Exception) {
//                                            e.printStackTrace()
//                                        }
//                                    }
//                                }
//                            }
                            MultiStyleItem.Options.SINGLE_INPUT -> {
                                when(i.inputSingleTitle) {
                                    "牌照号码"->{
                                        carNumber = i.inputSingleContent
                                    }
                                }

                            }
                            MultiStyleItem.Options.SELECT_DIALOG ->{
                                when(i.selectTitle) {
                                    "作业最大直径"->{
                                        workDia = 1-i.selectOption1Items.indexOf(i.selectContent)
                                    }//Int
                                    "车辆类型"->{
                                        carType =if (i.selectContent != "") {i.selectContent} else { "" }//String
                                    }
                                    "车厢结构"->{
                                        construction = 1-i.selectOption1Items.indexOf(i.selectContent)    //Int
                                    }
                                }
                            }
                            MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG->{//String
                                when(i.selectTitle) {
                                    "队部所在区域"->{
                                        location = if (i.selectContent != "") {i.selectContent} else { "" }
                                    }

                                }
                            }

                        }
                    }
                    val json=JSONObject()
                    json.put("id",id)
                    when(arguments!!.getInt("type")) {
                        Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal->{//主网
                            json.put("MajorNetwork",
                                JSONObject().put("name",name)
                                    .put("validTime",validTime.toString()))
                        }
                        Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal->{//配网
                            json.put("DistribuionNetwork",
                                JSONObject().put("name",name)
                                    .put("validTime",validTime.toString()))
                        }
                        Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal->{//变电
                            json.put("PowerTransformation",
                                JSONObject().put("name",name)
                                    .put("validTime",validTime.toString()))
                        }
                        Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal->{//测量设计
                            json.put("MeasureDesign",
                                JSONObject().put("name",name)
                                    .put("validTime",validTime.toString()))
                        }
                        //团队服务——马帮运输
                        Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal->{
                            json.put("caravanTransport",
                                JSONObject().put("name",name)
                                .put("validTime",validTime.toString())
                                .put("horseNumber",horseNumber.toString()))
                        }
                        //团队服务——桩基
                        Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal->{
                            json.put("PileFoundation",
                                JSONObject().put("id",id)
                                    .put("name","桩基服务")
                                    .put("isCar",if(mAdapter!!.mData[2].itemMultiStyleItem.isEmpty()) "fasle" else "true")
                                    .put("isConstructionTool",if(mAdapter!!.mData[7].itemMultiStyleItem.isEmpty()) "fasle" else "true")
                                    .put("workDia",mAdapter!!.mData[5].selectOption1Items.indexOf(mAdapter!!.mData[5].selectContent)+1)
                                    .put("location",mAdapter!!.mData[6].selectContent.replace(" "," / "))
                                    .put("teamServeId",UnSerializeDataBase.inventoryId)
                                    .put("issuerName",mAdapter!!.mData[8].singleDisplayRightContent)
                                    .put("phone",mAdapter!!.mData[9].singleDisplayRightContent)
                                    .put("validTime",mAdapter!!.mData[10].inputUnitContent)
                                    .put("issuerBelongSite","")
                            )
                        }
                        //团队服务——非开挖
                        Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal-> {
                            json.put("validTime",validTime.toString())
                        }
                        Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal->{//实验调试

                        }
                        Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal->{//跨越架
                            json.put("validTime",validTime.toString())
                        }
                        Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal->{//运维

                        }
                        Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal->{//车辆租赁
                            json.put("carTable",
                                JSONObject().put("carType",carType)
                                    .put("maxPassengers",maxPassengers.toString())
                                    .put("maxWeight",mAdapter!!.mData[4].inputUnitContent.toLongOrNull().toString())
                                    .put("construction",construction.toString())
                                    .put("lenghtCar",mAdapter!!.mData[5].inputUnitContent.toLongOrNull().toString())
                                    .put("isDriver",isDriver)
                                    .put("isInsurance",isInsurance)
                                    .put("carPhotoPath",carPhotoPath)
                                    .put("carNumber",carNumber)
                                    .put("id",UnSerializeDataBase.inventoryIdKey)
                                    .put("leaseCarId",UnSerializeDataBase.inventoryId)
                            )
                        }
                    }
                    provider.generateJsonRequestBody(json).subscribe {
                        val loadingDialog = LoadingDialog(mView.context, "正在请求...", R.mipmap.ic_dialog_loading)
                        loadingDialog.show()
                        val result = putSimpleMessage(it, UnSerializeDataBase.dmsBasePath+mAdapter!!.urlPath).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        loadingDialog.dismiss()
                                        val json = JSONObject(it.string())
                                        Log.i("json",json.toString())
                                        if (json.getString("desc") == "OK") {
                                                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show()
                                                mView.tv_modify_job_information_back.callOnClick()
                                        }else if(json.getInt("code") == 403){
                                            Toast.makeText(context, "${json.getString("desc")} 请升级为更高级会员", Toast.LENGTH_SHORT).show()
                                        } else if (json.getString("desc") == "FAIL") {
                                            Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show()
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
    }
    fun switchAdapter(Type: Int):RecyclerviewAdapter
    {
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = mView.context
        adapterGenerate.activity = activity as MyReleaseActivity
        lateinit var adapter: RecyclerviewAdapter
        when(Type){
            Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "普工"
                val selectOption1Items: List<String> = listOf("普工")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                adapter.urlPath = Constants.HttpUrlPath.Provider.updatePersonalIssue
                initPersonalService(adapter)
            }
            Constants.FragmentType.PERSONAL_SPECIAL_WORK_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "特种作业"
                val selectOption1Items: List<String> =
                    listOf("低压电工作业", "高压电工作业", "电力电缆作业", "继电保护作业", "电气试验作业", "融化焊接与热切割作业", "登高架设作业")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                adapter.urlPath = Constants.HttpUrlPath.Provider.updatePersonalIssue
                initPersonalService(adapter)
            }
            Constants.FragmentType.PERSONAL_PROFESSIONAL_OPERATION_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "专业操作"
                val selectOption1Items: List<String> =
                    listOf("压接操作", "机动绞磨操作", "牵张设备操作", "起重机械操作", "钢筋工", "混凝土工", "木工", "模板工", "油漆工", "砌筑工", "通风工", "打桩工", "架子工工")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                adapter.urlPath = Constants.HttpUrlPath.Provider.updatePersonalIssue
                initPersonalService(adapter)
            }
            Constants.FragmentType.PERSONAL_SURVEYOR_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "测量工"
                val selectOption1Items: List<String> = listOf("测量工")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                adapter.urlPath = Constants.HttpUrlPath.Provider.updatePersonalIssue
                initPersonalService(adapter)
            }
            Constants.FragmentType.PERSONAL_DRIVER_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "驾驶员"
                val selectOption1Items: List<String> =
                    listOf("驾驶证A1", "驾驶证A2", "驾驶证A3", "驾驶证B1", "驾驶证B2", "驾驶证C1", "驾驶证C2", "驾驶证C3", "驾驶证D", "驾驶证E")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                adapter.urlPath = Constants.HttpUrlPath.Provider.updatePersonalIssue
                initPersonalService(adapter)
            }
            Constants.FragmentType.PERSONAL_NINE_MEMBERS_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "九大员"
                val selectOption1Items: List<String> =
                    listOf("施工员", "安全员", "质量员", "材料员", "资料员", "预算员", "标准员", "机械员", "劳务员")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                adapter.urlPath = Constants.HttpUrlPath.Provider.updatePersonalIssue
                initPersonalService(adapter)
            }
            Constants.FragmentType.PERSONAL_REGISTRATION_CLASS_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "注册类"
                val selectOption1Items: List<String> = listOf("造价工程师", "一级建造师", "安全工程师", "电气工程师")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                adapter.urlPath = Constants.HttpUrlPath.Provider.updatePersonalIssue
                initPersonalService(adapter)
            }
            Constants.FragmentType.PERSONAL_OTHER_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                adapter.urlPath = Constants.HttpUrlPath.Provider.updatePersonalIssue
                initPersonalService(adapter)
                adapter.mData[1].options= MultiStyleItem.Options.SINGLE_INPUT
                adapter.mData[1].inputSingleTitle=adapter.mData[1].selectTitle
                adapter.mData[1].inputSingleContent = adapter.mData[1].selectContent
            }
            //团队服务——变电施工
            Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupSubstationConstruction()
                val singleDisplayRightContent = "变电施工队"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                name =singleDisplayRightContent//名称
                adapter.urlPath=Constants.HttpUrlPath.Provider.updatePowerTransformation
                initProviderGroupPowerTransformation(adapter)
            }
            //团队服务——主网施工
            Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupSubstationConstruction()
                val singleDisplayRightContent = "主网施工队"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath=Constants.HttpUrlPath.Provider.updateMajorNetwork
                initProviderGroupMajorNetwork(adapter)

            }
            //团队服务——配网施工
            Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupSubstationConstruction()
                val singleDisplayRightContent = "配网施工队"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath=Constants.HttpUrlPath.Provider.updateDistribuionNetworkDTO
                initProviderGroupDistribuionNetwork(adapter)
            }
            //团队服务——测量设计
            Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupMeasurementDesign()
                val singleDisplayRightContent = "测量设计"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Provider.updateMeasureDesign
                initProviderGroupMeasurementDesign(adapter)
            }
            //团队服务——马帮运输
            Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupCaravanTransportation()
                val singleDisplayRightContent = "马帮运输"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Provider.updateCaravanTransport
                initProviderGroupCaravanTransportation(adapter)
            }
            //团队服务——桩基
            Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupPileFoundationConstruction()
                val singleDisplayRightContent = "桩基服务"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Provider.updatePileFoundation
                initProviderGroupPileFoundationConstruction(adapter)
            }
            //团队服务——非开挖
            Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal->{/////////////
                adapter=adapterGenerate.ProviderGroupNonExcavation()
                val singleDisplayRightContent = "非开挖顶拉管作业"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Provider.updateUnexcavation
                initProviderGroupNonExcavation(adapter)

            }
            //团队服务——试验调试
            Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupTestDebugging()
                val singleDisplayRightContent = "实验调试"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Provider.updateTestTeam
                initProviderGroupTestDebugging(adapter)
            }
            //团队服务——跨越架
            Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupCrossingFrame()
                adapter.urlPath = Constants.HttpUrlPath.Provider.updateSpanWoodenSupprt
                val singleDisplayRightContent = "跨越架"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                initProviderGroupCrossingFrame(adapter)
            }
            //团队服务——运维
            Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal->{
                adapter=adapterGenerate.OperationAndMaintenance()
                adapter.urlPath = Constants.HttpUrlPath.Provider.updateRunningMaintain
                val singleDisplayRightContent = "运行维护"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                initOperationAndMaintenance(adapter)
            }
            //租赁服务——车辆
            Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal->{
                adapter=adapterGenerate.VehicleRental()
                adapter.urlPath = Constants.HttpUrlPath.Provider.updateLeaseCar
                val singleDisplayRightContent = "车辆租赁"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                initVehicleRental(adapter)
            }
            //租赁服务——工器具
            Constants.FragmentType.TOOL_LEASING_TYPE.ordinal->{
                val bundle=Bundle()
                bundle.putInt("type",Type)
                adapter=adapterGenerate.EquipmentLeasing(bundle)
                //   adapter.mData[0].singleDisplayRightContent = "工器具租赁"
                val singleDisplayRightContent = "工器具租赁"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Provider.updateLeaseConstructionTool
                adapter.mData[13].key="validTime"
                initEquipmentLeasing(adapter)
            }
            //租赁服务--机械
            Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal->{
                val bundle=Bundle()
                bundle.putInt("type",Type)
                adapter=adapterGenerate.EquipmentLeasing(bundle)
                adapter.mData[0].singleDisplayRightContent = "机械租赁"
                adapter.urlPath = Constants.HttpUrlPath.Provider.updateLeaseMachinery
                initEquipmentLeasing(adapter)
            }
            //租赁服务--设备
            Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal->{
                val bundle=Bundle()
                bundle.putInt("type",Type)
                adapter=adapterGenerate.EquipmentLeasing(bundle)
                adapter.mData[0].singleDisplayRightContent = "设备租赁"
                adapter.urlPath = Constants.HttpUrlPath.Provider.updateLeaseFacility
                initEquipmentLeasing(adapter)
            }
            //培训办证
            Constants.FragmentType.TRIPARTITE_TRAINING_CERTIFICATE_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[0].singleDisplayRightContent = "培训办证"
                initTripartiteServices(adapter)
            }
            //三方财务记账
            Constants.FragmentType.TRIPARTITE_FINANCIAL_ACCOUNTING_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[0].singleDisplayRightContent = "财务记账"
                initTripartiteServices(adapter)
            }
            //三方代办资格
            Constants.FragmentType.TRIPARTITE_AGENCY_QUALIFICATION_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[0].singleDisplayRightContent = "代办资格"
                initTripartiteServices(adapter)
            }
            //三方标书服务
            Constants.FragmentType.TRIPARTITE_TENDER_SERVICE_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[0].singleDisplayRightContent = "标书服务"
                initTripartiteServices(adapter)
            }
            //三方法律咨询
            Constants.FragmentType.TRIPARTITE_LEGAL_ADVICE_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[0].singleDisplayRightContent = "法律咨询"
                initTripartiteServices(adapter)
            }
            //三方软件服务
            Constants.FragmentType.TRIPARTITE_SOFTWARE_SERVICE_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[0].singleDisplayRightContent = "软件服务"
                initTripartiteServices(adapter)
            }
            //三方其他
            Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                initTripartiteServices(adapter)
                adapter.mData[0].options=MultiStyleItem.Options.SINGLE_INPUT
                adapter.mData[0].inputSingleTitle=adapter.mData[0].singleDisplayRightTitle
            }
        }
        return adapter
    }



    /**
     * @个人劳务
     */
    private fun initPersonalService(adapter: RecyclerviewAdapter) {
        adapter.urlPath = Constants.HttpUrlPath.Provider.updatePersonalIssue
        val supplyPersonDetail = arguments!!.getSerializable("data") as SupplyPersonDetail
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "个人劳务"
        }
        adapter.mData[0].singleDisplayRightContent = supplyPersonDetail.issuerWorkType
        adapter.mData[1].selectContent = supplyPersonDetail.issuerWorkerKind
        adapter.mData[2].radioButtonValue = supplyPersonDetail.sex
        adapter.mData[3].inputUnitContent = supplyPersonDetail.age
        adapter.mData[4].inputUnitContent = supplyPersonDetail.workExperience

        if(supplyPersonDetail.salaryUnit!="-1.0"){
            adapter.mData[5].inputMultiContent = supplyPersonDetail.workMoney
            adapter.mData[5].inputMultiSelectUnit = supplyPersonDetail.salaryUnit
        }else{
            adapter.mData[5].inputMultiSelectUnit = "面议"
        }

        adapter.mData[6].singleDisplayRightContent = UnSerializeDataBase.idCardName
        adapter.mData[7].singleDisplayRightContent = UnSerializeDataBase.userPhone
        //专业证书附件上传 8
        adapter.mData[9].inputUnitContent = supplyPersonDetail.validTime
        // 10
        if(supplyPersonDetail.remark!=null)
            adapter.mData[11].textAreaContent = supplyPersonDetail.remark
    }


    /**
     * @变电
     */
    private fun initProviderGroupPowerTransformation(adapter: RecyclerviewAdapter) {
        val network = arguments!!.getSerializable("data") as Network
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "团队服务"
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        adapter.mData[0].singleDisplayRightContent = network.powerTransformation.name
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideCrewLists = network.provideCrewLists
            if(provideCrewLists!=null)
                for(j in provideCrewLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","成员清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.name
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].selectContent = if(j.sex=="true") "女" else "男"
                    mData[2].inputSingleContent = j.age
                    mData[3].selectContent = j.wokerType
                    mData[4].inputSingleContent = j.workExperience
                    mData[5].inputSingleContent = j.money
                    if(j.remark!=null)
                        mData[7].textAreaContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","成员清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[1].itemMultiStyleItem = itemMultiStyleItem
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideTransportMachines = network.provideTransportMachines
            if(provideTransportMachines!=null)
                for(j in provideTransportMachines){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","车辆清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].selectContent = j.carType
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.carNumber
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()]
                    mData[3].inputUnitContent = j.maxPassengers
                    mData[4].inputUnitContent = j.maxWeight
                    mData[5].inputUnitContent = j.lenghtCar
                    mData[6].selectContent = if(j.isDriver=="true") "是" else "否"
                    if(j.carPhotoPath!=null && j.carPhotoPath!="")
                        UnSerializeDataBase.imgList.add(BitmapMap(j.carPhotoPath,mData[7].key))
                    mData[8].selectContent = if(j.isInsurance=="true") "有" else "无"
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","车辆清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[2].itemMultiStyleItem = itemMultiStyleItem
        }
        adapter.mData[3].singleDisplayRightContent = network.powerTransformation.issuerBelongSite
        val voltages = network.voltages
        if(voltages!=null){
            val voltageDegrees = ArrayList<String>()
            for (j in voltages)
                voltageDegrees.add(j.voltageDegree)
            adapter.mData[4].checkboxValueList = ArrayList(adapter.mData[4].checkboxNameList.size)
            for (j in adapter.mData[4].checkboxNameList){
                val position = voltageDegrees.indexOf(j)
                if(position>=0)
                    adapter.mData[4].checkboxValueList.add(true)
                else
                    adapter.mData[4].checkboxValueList.add(false)
            }
        }
        adapter.mData[5].checkboxValueList = ArrayList(adapter.mData[5].checkboxNameList.size)
        val implementationRanges = network.implementationRanges.name.split("、")
        for (j in adapter.mData[5].checkboxNameList){
            val position = implementationRanges.indexOf(j)
            if(position>=0)
                adapter.mData[5].checkboxValueList.add(true)
            else
                adapter.mData[5].checkboxValueList.add(false)
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val constructionToolLists = network.constructionToolLists
            if(constructionToolLists!=null)
                for(j in constructionToolLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.type,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","工器具清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.type
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.specificationsModel
                    mData[2].inputSingleContent = j.unit
                    mData[3].inputSingleContent = j.quantity
                    if(j.remark!=null)
                        mData[5].inputSingleContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","工器具清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[6].itemMultiStyleItem = itemMultiStyleItem
        }
        adapter.mData[9].inputUnitContent = network.powerTransformation.validTime
    }

    /**
     * @主网
     */
    private fun initProviderGroupMajorNetwork(adapter: RecyclerviewAdapter) {
        val network = arguments!!.getSerializable("data") as Network
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "团队服务"
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        adapter.mData[0].singleDisplayRightContent = network.majorNetwork.name
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideCrewLists = network.provideCrewLists
            if(provideCrewLists!=null)
                for(j in provideCrewLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","成员清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.name
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].selectContent = if(j.sex=="true") "女" else "男"
                    mData[2].inputSingleContent = j.age
                    mData[3].selectContent = j.wokerType
                    mData[4].inputSingleContent = j.workExperience
                    mData[5].inputSingleContent = j.money
                    if(j.remark!=null)
                        mData[7].textAreaContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","成员清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[1].itemMultiStyleItem = itemMultiStyleItem
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideTransportMachines = network.provideTransportMachines
            if(provideTransportMachines!=null)
                for(j in provideTransportMachines){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","车辆清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].selectContent = j.carType
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.carNumber
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()]
                    mData[3].inputUnitContent = j.maxPassengers
                    mData[4].inputUnitContent = j.maxWeight
                    mData[5].inputUnitContent = j.lenghtCar
                    mData[6].selectContent = if(j.isDriver=="true") "是" else "否"
                    if(j.carPhotoPath!=null && j.carPhotoPath!="")
                        UnSerializeDataBase.imgList.add(BitmapMap(j.carPhotoPath,mData[7].key))
                    mData[8].selectContent = if(j.isInsurance=="true") "有" else "无"
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","车辆清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[2].itemMultiStyleItem = itemMultiStyleItem
        }
        adapter.mData[3].singleDisplayRightContent = network.majorNetwork.issuerBelongSite
        val voltages = network.voltages
        if(voltages!=null){
            val voltageDegrees = ArrayList<String>()
            for (j in voltages)
                voltageDegrees.add(j.voltageDegree)
            adapter.mData[4].checkboxValueList = ArrayList(adapter.mData[4].checkboxNameList.size)
            for (j in adapter.mData[4].checkboxNameList){
                val position = voltageDegrees.indexOf(j)
                if(position>=0)
                    adapter.mData[4].checkboxValueList.add(true)
                else
                    adapter.mData[4].checkboxValueList.add(false)
            }
        }
        adapter.mData[5].checkboxValueList = ArrayList(adapter.mData[5].checkboxNameList.size)
        val implementationRanges = network.implementationRanges.name.split("、")
        for (j in adapter.mData[5].checkboxNameList){
            val position = implementationRanges.indexOf(j)
            if(position>=0)
                adapter.mData[5].checkboxValueList.add(true)
            else
                adapter.mData[5].checkboxValueList.add(false)
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val constructionToolLists = network.constructionToolLists
            if(constructionToolLists!=null)
                for(j in constructionToolLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.type,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","工器具清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.type
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.specificationsModel
                    mData[2].inputSingleContent = j.unit
                    mData[3].inputSingleContent = j.quantity
                    if(j.remark!=null)
                        mData[5].inputSingleContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","工器具清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[6].itemMultiStyleItem = itemMultiStyleItem
        }
        adapter.mData[9].inputUnitContent = network.majorNetwork.validTime
    }

    /**
     * @配网
     */
    private fun initProviderGroupDistribuionNetwork(adapter: RecyclerviewAdapter) {
        val network = arguments!!.getSerializable("data") as Network
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "团队服务"
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        adapter.mData[0].singleDisplayRightContent = network.distribuionNetwork.name
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideCrewLists = network.provideCrewLists
            if(provideCrewLists!=null)
                for(j in provideCrewLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","成员清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.name
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].selectContent = if(j.sex=="true") "女" else "男"
                    mData[2].inputSingleContent = j.age
                    mData[3].selectContent = j.wokerType
                    mData[4].inputSingleContent = j.workExperience
                    mData[5].inputSingleContent = j.money
                    if(j.remark!=null)
                        mData[7].textAreaContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","成员清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[1].itemMultiStyleItem = itemMultiStyleItem
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideTransportMachines = network.provideTransportMachines
            if(provideTransportMachines!=null)
                for(j in provideTransportMachines){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","车辆清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].selectContent = j.carType
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.carNumber
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()]
                    mData[3].inputUnitContent = j.maxPassengers
                    mData[4].inputUnitContent = j.maxWeight
                    mData[5].inputUnitContent = j.lenghtCar
                    mData[6].selectContent = if(j.isDriver=="true") "是" else "否"
                    if(j.carPhotoPath!=null && j.carPhotoPath!="")
                        UnSerializeDataBase.imgList.add(BitmapMap(j.carPhotoPath,mData[7].key))
                    mData[8].selectContent = if(j.isInsurance=="true") "有" else "无"
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","车辆清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[2].itemMultiStyleItem = itemMultiStyleItem
        }
        adapter.mData[3].singleDisplayRightContent = network.distribuionNetwork.issuerBelongSite
        val voltages = network.voltages
        if(voltages!=null){
            val voltageDegrees = ArrayList<String>()
            for (j in voltages)
                voltageDegrees.add(j.voltageDegree)
            adapter.mData[4].checkboxValueList = ArrayList(adapter.mData[4].checkboxNameList.size)
            for (j in adapter.mData[4].checkboxNameList){
                val position = voltageDegrees.indexOf(j)
                if(position>=0)
                    adapter.mData[4].checkboxValueList.add(true)
                else
                    adapter.mData[4].checkboxValueList.add(false)
            }
        }
        adapter.mData[5].checkboxValueList = ArrayList(adapter.mData[5].checkboxNameList.size)
        val implementationRanges = network.implementationRanges.name.split("、")
        for (j in adapter.mData[5].checkboxNameList){
            val position = implementationRanges.indexOf(j)
            if(position>=0)
                adapter.mData[5].checkboxValueList.add(true)
            else
                adapter.mData[5].checkboxValueList.add(false)
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val constructionToolLists = network.constructionToolLists
            if(constructionToolLists!=null)
                for(j in constructionToolLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.type,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","工器具清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.type
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.specificationsModel
                    mData[2].inputSingleContent = j.unit
                    mData[3].inputSingleContent = j.quantity
                    if(j.remark!=null)
                        mData[5].inputSingleContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","工器具清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[6].itemMultiStyleItem = itemMultiStyleItem
        }
        adapter.mData[9].inputUnitContent = network.distribuionNetwork.validTime
    }

    /**
     * @测量设计
     */
    private fun initProviderGroupMeasurementDesign(adapter: RecyclerviewAdapter) {
        val network = arguments!!.getSerializable("data") as Network
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "团队服务"
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        adapter.mData[0].singleDisplayRightContent = network.measureDesign.name
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideCrewLists = network.provideCrewLists
            if(provideCrewLists!=null)
                for(j in provideCrewLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","成员清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.name
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].selectContent = if(j.sex=="true") "女" else "男"
                    mData[2].inputSingleContent = j.age
                    mData[3].selectContent = j.wokerType
                    mData[4].inputSingleContent = j.workExperience
                    mData[5].inputSingleContent = j.money
                    if(j.remark!=null)
                        mData[7].textAreaContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","成员清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[1].itemMultiStyleItem = itemMultiStyleItem
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideTransportMachines = network.provideTransportMachines
            if(provideTransportMachines!=null)
                for(j in provideTransportMachines){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","车辆清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].selectContent = j.carType
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.carNumber
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()]
                    mData[3].inputUnitContent = j.maxPassengers
                    mData[4].inputUnitContent = j.maxWeight
                    mData[5].inputUnitContent = j.lenghtCar
                    mData[6].selectContent = if(j.isDriver=="true") "是" else "否"
                    if(j.carPhotoPath!=null && j.carPhotoPath!="")
                        UnSerializeDataBase.imgList.add(BitmapMap(j.carPhotoPath,mData[7].key))
                    mData[8].selectContent = if(j.isInsurance=="true") "有" else "无"
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","车辆清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[2].itemMultiStyleItem = itemMultiStyleItem
        }
        adapter.mData[3].singleDisplayRightContent = network.measureDesign.issuerBelongSite
        val voltages = network.voltages
        if(voltages!=null){
            val voltageDegrees = ArrayList<String>()
            for (j in voltages)
                voltageDegrees.add(j.voltageDegree)
            adapter.mData[4].checkboxValueList = ArrayList(adapter.mData[4].checkboxNameList.size)
            for (j in adapter.mData[4].checkboxNameList){
                val position = voltageDegrees.indexOf(j)
                if(position>=0)
                    adapter.mData[4].checkboxValueList.add(true)
                else
                    adapter.mData[4].checkboxValueList.add(false)
            }
        }
        adapter.mData[5].checkboxValueList = ArrayList(adapter.mData[5].checkboxNameList.size)
        val implementationRanges = network.implementationRanges.name.split("、")
        for (j in adapter.mData[5].checkboxNameList){
            val position = implementationRanges.indexOf(j)
            if(position>=0)
                adapter.mData[5].checkboxValueList.add(true)
            else
                adapter.mData[5].checkboxValueList.add(false)
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val constructionToolLists = network.constructionToolLists
            if(constructionToolLists!=null)
                for(j in constructionToolLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.type,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","工器具清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.type
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.specificationsModel
                    mData[2].inputSingleContent = j.unit
                    mData[3].inputSingleContent = j.quantity
                    if(j.remark!=null)
                        mData[5].inputSingleContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","工器具清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[6].itemMultiStyleItem = itemMultiStyleItem
        }
        adapter.mData[9].inputUnitContent = network.measureDesign.validTime
    }

    /**
     * @马帮运输
     */
    private fun initProviderGroupCaravanTransportation(adapter: RecyclerviewAdapter) {
        val caravan = arguments!!.getSerializable("data") as Caravan
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "团队服务"
        }

        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        adapter.mData[0].singleDisplayRightContent = caravan.caravanTransport.name
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideCrewLists = caravan.provideCrewLists
            if(provideCrewLists!=null)
                for(j in provideCrewLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","成员清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.name
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].selectContent = if(j.sex=="true") "女" else "男"
                    mData[2].inputSingleContent = j.age
                    mData[3].selectContent = j.wokerType
                    mData[4].inputSingleContent = j.workExperience
                    mData[5].inputSingleContent = j.money
                    if(j.remark!=null)
                        mData[7].textAreaContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","成员清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[1].itemMultiStyleItem = itemMultiStyleItem
        }
        adapter.mData[2].inputUnitContent = caravan.caravanTransport.horseNumber
        //3
        adapter.mData[6].inputUnitContent = caravan.caravanTransport.validTime
    }

    /**
     * @桩基服务
     */
    private fun initProviderGroupPileFoundationConstruction(adapter: RecyclerviewAdapter) {
        val pile = arguments!!.getSerializable("data") as Pile
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "团队服务"
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        adapter.mData[0].singleDisplayRightContent = pile.pileFoundation.name
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideCrewLists = pile.provideCrewLists
            if(provideCrewLists!=null)
                for(j in provideCrewLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","成员清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.name
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].selectContent = if(j.sex=="true") "女" else "男"
                    mData[2].inputSingleContent = j.age
                    mData[3].selectContent = j.wokerType
                    mData[4].inputSingleContent = j.workExperience
                    mData[5].inputSingleContent = j.money
                    if(j.remark!=null)
                        mData[7].textAreaContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","成员清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[1].itemMultiStyleItem = itemMultiStyleItem
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideTransportMachines = pile.provideTransportMachines
            if(provideTransportMachines!=null)
                for(j in provideTransportMachines){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","车辆清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].selectContent = j.carType
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.carNumber
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()]
                    mData[3].inputUnitContent = j.maxPassengers
                    mData[4].inputUnitContent = j.maxWeight
                    mData[5].inputUnitContent = j.lenghtCar
                    mData[6].selectContent = if(j.isDriver=="true") "是" else "否"
                    if(j.carPhotoPath!=null && j.carPhotoPath!="")
                        UnSerializeDataBase.imgList.add(BitmapMap(j.carPhotoPath,mData[7].key))
                    mData[8].selectContent = if(j.isInsurance=="true") "有" else "无"
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","车辆清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[2].itemMultiStyleItem = itemMultiStyleItem
        }
        //adapter.mData[3].singleDisplayRightContent = supplyTest.issuerBelongSite
        val implementationRanges = pile.implementationRanges.name.split("、")
        adapter.mData[4].checkboxValueList = ArrayList(adapter.mData[4].checkboxNameList.size)
        for (j in adapter.mData[4].checkboxNameList){
            val position = implementationRanges.indexOf(j)
            if(position>=0)
                adapter.mData[4].checkboxValueList.add(true)
            else
                adapter.mData[4].checkboxValueList.add(false)
        }

        adapter.mData[5].selectContent = adapter.mData[5].selectOption1Items[pile.pileFoundation.workDia.toInt()-1]
        adapter.mData[6].selectContent = pile.pileFoundation.location
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val constructionToolLists = pile.constructionToolLists
            if(constructionToolLists!=null)
                for(j in constructionToolLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.type,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","工器具清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.type
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.specificationsModel
                    mData[2].inputSingleContent = j.unit
                    mData[3].inputSingleContent = j.quantity
                    if(j.remark!=null)
                        mData[5].inputSingleContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","工器具清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[7].itemMultiStyleItem = itemMultiStyleItem
        }
        adapter.mData[10].inputUnitContent = pile.pileFoundation.validTime
    }

    /**
     * @非开挖
     */
    private fun initProviderGroupNonExcavation(adapter: RecyclerviewAdapter) {
        val supplyUnexcavation = arguments!!.getSerializable("data") as SupplyUnexcavation
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "团队服务"
        }

        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        adapter.mData[0].singleDisplayRightContent = supplyUnexcavation.name
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideCrewLists = supplyUnexcavation.provideCrewLists
            if(provideCrewLists!=null)
                for(j in provideCrewLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","成员清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.name
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].selectContent = if(j.sex=="true") "女" else "男"
                    mData[2].inputSingleContent = j.age
                    mData[3].selectContent = j.wokerType
                    mData[4].inputSingleContent = j.workExperience
                    mData[5].inputSingleContent = j.money
                    if(j.remark!=null)
                        mData[7].textAreaContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","成员清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[1].itemMultiStyleItem = itemMultiStyleItem
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideTransportMachines = supplyUnexcavation.provideTransportMachines
            if(provideTransportMachines!=null)
                for(j in provideTransportMachines){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","车辆清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].selectContent = j.carType
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.carNumber
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()]
                    mData[3].inputUnitContent = j.maxPassengers
                    mData[4].inputUnitContent = j.maxWeight
                    mData[5].inputUnitContent = j.lenghtCar
                    mData[6].selectContent = if(j.isDriver=="true") "是" else "否"
                    if(j.carPhotoPath!=null && j.carPhotoPath!="")
                        UnSerializeDataBase.imgList.add(BitmapMap(j.carPhotoPath,mData[7].key))
                    mData[8].selectContent = if(j.isInsurance=="true") "有" else "无"
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","车辆清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[2].itemMultiStyleItem = itemMultiStyleItem
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val constructionToolLists = supplyUnexcavation.constructionToolLists
            if(constructionToolLists!=null)
                for(j in constructionToolLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.type,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","工器具清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.type
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.specificationsModel
                    mData[2].inputSingleContent = j.unit
                    mData[3].inputSingleContent = j.quantity
                    if(j.remark!=null)
                        mData[5].inputSingleContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","工器具清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[3].itemMultiStyleItem = itemMultiStyleItem
        }
        //4
        adapter.mData[7].inputUnitContent = supplyUnexcavation.validTime
    }

    /**
     * @实验调试
     */
    private fun initProviderGroupTestDebugging(adapter: RecyclerviewAdapter) {
        val supplyTest = arguments!!.getSerializable("data") as SupplyTest
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "团队服务"
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        adapter.mData[0].singleDisplayRightContent = supplyTest.name
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideCrewLists = supplyTest.provideCrewLists
            if(provideCrewLists!=null)
                for(j in provideCrewLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","成员清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.name
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].selectContent = if(j.sex=="true") "女" else "男"
                    mData[2].inputSingleContent = j.age
                    mData[3].selectContent = j.wokerType
                    mData[4].inputSingleContent = j.workExperience
                    mData[5].inputSingleContent = j.money
                    if(j.remark!=null)
                        mData[7].textAreaContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","成员清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[1].itemMultiStyleItem = itemMultiStyleItem
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideTransportMachines = supplyTest.provideTransportMachines
            if(provideTransportMachines!=null)
                for(j in provideTransportMachines){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","车辆清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].selectContent = j.carType
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.carNumber
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()]
                    mData[3].inputUnitContent = j.maxPassengers
                    mData[4].inputUnitContent = j.maxWeight
                    mData[5].inputUnitContent = j.lenghtCar
                    mData[6].selectContent = if(j.isDriver=="true") "是" else "否"
                    if(j.carPhotoPath!=null && j.carPhotoPath!="")
                        UnSerializeDataBase.imgList.add(BitmapMap(j.carPhotoPath,mData[7].key))
                    mData[8].selectContent = if(j.isInsurance=="true") "有" else "无"
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","车辆清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[2].itemMultiStyleItem = itemMultiStyleItem
        }
        //adapter.mData[3].singleDisplayRightContent = supplyTest.issuerBelongSite
        val voltages = supplyTest.voltages
        if(voltages!=null){
            val voltageDegrees = ArrayList<String>()
            for (j in voltages)
                voltageDegrees.add(j.voltageDegree)
            adapter.mData[4].checkboxValueList = ArrayList(adapter.mData[4].checkboxNameList.size)
            for (j in adapter.mData[4].checkboxNameList){
                val position = voltageDegrees.indexOf(j)
                if(position>=0)
                    adapter.mData[4].checkboxValueList.add(true)
                else
                    adapter.mData[4].checkboxValueList.add(false)
            }
        }
        adapter.mData[5].checkboxValueList = ArrayList(adapter.mData[5].checkboxNameList.size)
        val testWorkTypes =supplyTest.testWorkTypes.split("、")
        for (j in adapter.mData[5].checkboxNameList){
            val position = testWorkTypes.indexOf(j)
            if(position>=0)
                adapter.mData[5].checkboxValueList.add(true)
            else
                adapter.mData[5].checkboxValueList.add(false)
        }
        adapter.mData[6].selectContent = supplyTest.operateDegree
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val constructionToolLists = supplyTest.constructionToolLists
            if(constructionToolLists!=null)
                for(j in constructionToolLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.type,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","工器具清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.type
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.specificationsModel
                    mData[2].inputSingleContent = j.unit
                    mData[3].inputSingleContent = j.quantity
                    if(j.remark!=null)
                        mData[5].inputSingleContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","工器具清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[7].itemMultiStyleItem = itemMultiStyleItem
        }
        adapter.mData[10].inputUnitContent = supplyTest.validTime
    }

    /**
     * @跨越架
     */
    private fun initProviderGroupCrossingFrame(adapter: RecyclerviewAdapter) {
        val supplySpanWoodenSupprt = arguments!!.getSerializable("data") as SupplySpanWoodenSupprt
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "团队服务"
        }

        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        adapter.mData[0].singleDisplayRightContent = supplySpanWoodenSupprt.name
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideCrewLists = supplySpanWoodenSupprt.provideCrewLists
            if(provideCrewLists!=null)
                for(j in provideCrewLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","成员清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.name
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].selectContent = if(j.sex=="true") "女" else "男"
                    mData[2].inputSingleContent = j.age
                    mData[3].selectContent = j.wokerType
                    mData[4].inputSingleContent = j.workExperience
                    mData[5].inputSingleContent = j.money
                    if(j.remark!=null)
                        mData[7].textAreaContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","成员清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[1].itemMultiStyleItem = itemMultiStyleItem
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideTransportMachines = supplySpanWoodenSupprt.provideTransportMachines
            if(provideTransportMachines!=null)
                for(j in provideTransportMachines){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","车辆清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].selectContent = j.carType
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.carNumber
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()]
                    mData[3].inputUnitContent = j.maxPassengers
                    mData[4].inputUnitContent = j.maxWeight
                    mData[5].inputUnitContent = j.lenghtCar
                    mData[6].selectContent = if(j.isDriver=="true") "是" else "否"
                    if(j.carPhotoPath!=null && j.carPhotoPath!="")
                        UnSerializeDataBase.imgList.add(BitmapMap(j.carPhotoPath,mData[7].key))
                    mData[8].selectContent = if(j.isInsurance=="true") "有" else "无"
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","车辆清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[2].itemMultiStyleItem = itemMultiStyleItem
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val constructionToolLists = supplySpanWoodenSupprt.constructionToolLists
            if(constructionToolLists!=null)
                for(j in constructionToolLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.type,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","工器具清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.type
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.specificationsModel
                    mData[2].inputSingleContent = j.unit
                    mData[3].inputSingleContent = j.quantity
                    if(j.remark!=null)
                        mData[5].inputSingleContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","工器具清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[3].itemMultiStyleItem = itemMultiStyleItem
        }
        //4
        adapter.mData[7].inputUnitContent = supplySpanWoodenSupprt.validTime
    }

    /**
     * @运行维护
     */
    private fun initOperationAndMaintenance(adapter: RecyclerviewAdapter) {
        val supplyRunningMaintain = arguments!!.getSerializable("data") as SupplyRunningMaintain
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "团队服务"
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        adapter.mData[0].singleDisplayRightContent = supplyRunningMaintain.name
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideCrewLists = supplyRunningMaintain.provideCrewLists
            if(provideCrewLists!=null)
                for(j in provideCrewLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","成员清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.name
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].selectContent = if(j.sex=="true") "女" else "男"
                    mData[2].inputSingleContent = j.age
                    mData[3].selectContent = j.wokerType
                    mData[4].inputSingleContent = j.workExperience
                    mData[5].inputSingleContent = j.money
                    if(j.remark!=null)
                        mData[7].textAreaContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","成员清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[1].itemMultiStyleItem = itemMultiStyleItem
        }
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val provideTransportMachines = supplyRunningMaintain.provideTransportMachines
            if(provideTransportMachines!=null)
                for(j in provideTransportMachines){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","车辆清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].selectContent = j.carType
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.carNumber
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()]
                    mData[3].inputUnitContent = j.maxPassengers
                    mData[4].inputUnitContent = j.maxWeight
                    mData[5].inputUnitContent = j.lenghtCar
                    mData[6].selectContent = if(j.isDriver=="true") "是" else "否"
                    if(j.carPhotoPath!=null && j.carPhotoPath!="")
                        UnSerializeDataBase.imgList.add(BitmapMap(j.carPhotoPath,mData[7].key))
                    mData[8].selectContent = if(j.isInsurance=="true") "有" else "无"
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","车辆清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[2].itemMultiStyleItem = itemMultiStyleItem
        }
        adapter.mData[3].singleDisplayRightContent = supplyRunningMaintain.issuerBelongSite
        val voltages = supplyRunningMaintain.voltages
        if(voltages!=null){
            val voltageDegrees = ArrayList<String>()
                for (j in voltages)
                    voltageDegrees.add(j.voltageDegree)
            adapter.mData[4].checkboxValueList = ArrayList(adapter.mData[4].checkboxNameList.size)
            for (j in adapter.mData[4].checkboxNameList){
                val position = voltageDegrees.indexOf(j)
                if(position>=0)
                    adapter.mData[4].checkboxValueList.add(true)
                else
                    adapter.mData[4].checkboxValueList.add(false)
            }
        }
        adapter.mData[5].checkboxValueList = ArrayList(adapter.mData[5].checkboxNameList.size)
        val implementationRanges = supplyRunningMaintain.implementationRanges.split("、")
        for (j in adapter.mData[5].checkboxNameList){
            val position = implementationRanges.indexOf(j)
            if(position>=0)
                adapter.mData[5].checkboxValueList.add(true)
            else
                adapter.mData[5].checkboxValueList.add(false)
        }
        adapter.mData[6].selectContent = supplyRunningMaintain.workTerritory.replace(" / "," ")
        if(true){
            val itemMultiStyleItem = ArrayList<MultiStyleItem>()
            val constructionToolLists = supplyRunningMaintain.constructionToolLists
            if(constructionToolLists!=null)
                for(j in constructionToolLists){
                    itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.type,true))
                    val bundle = Bundle()
                    val mData:List<MultiStyleItem>
                    bundle.putString("type","工器具清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                    mData[0].inputSingleContent = j.type
                    UnSerializeDataBase.inventoryIdKey = "teamServeId"
                    UnSerializeDataBase.inventoryId = j.teamServeId
                    mData[0].id = j.id
                    mData[1].inputSingleContent = j.specificationsModel
                    mData[2].inputSingleContent = j.unit
                    mData[3].inputSingleContent = j.quantity
                    if(j.remark!=null)
                        mData[5].inputSingleContent = j.remark
                    itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                    itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                        itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                        val bundle = Bundle()
                        val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                        bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                        bundle.putString("type","工器具清册发布")
                        FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                    }
                }
            adapter.mData[7].itemMultiStyleItem = itemMultiStyleItem
        }
        adapter.mData[10].inputUnitContent = supplyRunningMaintain.validTime
    }



    /**
     * @车辆租赁
     */
    private fun initVehicleRental(adapter: RecyclerviewAdapter) {
        val supplyLeaseCar = arguments!!.getSerializable("data") as SupplyLeaseCar
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "租赁服务"
        }
        UnSerializeDataBase.inventoryId = supplyLeaseCar.carTable.leaseCarId
        UnSerializeDataBase.inventoryIdKey = supplyLeaseCar.carTable.id
        adapter.mData[0].id = supplyLeaseCar.id
        adapter.mData[0].singleDisplayRightContent = supplyLeaseCar.variety
        adapter.mData[1].selectContent = supplyLeaseCar.carTable.carType
        adapter.mData[2].inputSingleContent = supplyLeaseCar.carTable.carNumber
        adapter.mData[3].inputUnitContent = supplyLeaseCar.carTable.maxPassengers
        adapter.mData[4].inputUnitContent = supplyLeaseCar.carTable.maxWeight
        adapter.mData[5].inputUnitContent = supplyLeaseCar.carTable.lenghtCar
        adapter.mData[6].selectContent = if(supplyLeaseCar.carTable.construction=="1") "箱式" else "敞篷"
        adapter.mData[7].radioButtonValue = if(supplyLeaseCar.carTable.isDriver=="true") "1" else "0"
        adapter.mData[8].radioButtonValue = if(supplyLeaseCar.carTable.isInsurance=="true") "1" else "0"
        if(supplyLeaseCar.carTable.carPhotoPath!=null &&  supplyLeaseCar.carTable.carPhotoPath!="")
            UnSerializeDataBase.imgList.add(BitmapMap(supplyLeaseCar.carTable.carPhotoPath, adapter.mData[9].key))
        adapter.mData[10].inputSingleContent = supplyLeaseCar.site
        if(supplyLeaseCar.money=="-1.0"){
            adapter.mData[11].singleDisplayRightContent= "面议"
        }else {
            adapter.mData[11].singleDisplayRightContent= "${supplyLeaseCar.money} ${supplyLeaseCar.salaryUnit}"
        }
        // 14
        adapter.mData[15].inputUnitContent = supplyLeaseCar.validTime
        if(supplyLeaseCar.comment!=null)
            adapter.mData[17].textAreaContent = supplyLeaseCar.comment
    }

    /**
     * @工器具租赁
     */
    private fun initEquipmentLeasing(adapter: RecyclerviewAdapter) {
        val otherLease = arguments!!.getSerializable("data") as OtherLease
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "租赁服务"
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryId
        UnSerializeDataBase.inventoryIdKey
        adapter.mData[0].id = otherLease.leaseConstructionTool.id
        adapter.mData[0].singleDisplayRightContent = otherLease.leaseConstructionTool.variety
        adapter.mData[1].inputSingleContent = otherLease.companyCredential.companyName
        adapter.mData[2].inputSingleContent = otherLease.companyCredential.companyAbbreviation
        adapter.mData[3].inputSingleContent = otherLease.companyCredential.companyAddress
        adapter.mData[4].inputSingleContent = otherLease.companyCredential.legalPersonName
        adapter.mData[5].inputSingleContent = otherLease.companyCredential.legalPersonPhone
        if(otherLease.companyCredential.legalPersonIdCardPath!=null && otherLease.companyCredential.legalPersonIdCardPath!=""){
            UnSerializeDataBase.imgList.add(BitmapMap(otherLease.companyCredential.legalPersonIdCardPath, adapter.mData[6].key))
        }
        if(otherLease.companyCredential.businessLicensePath!=null && otherLease.companyCredential.businessLicensePath!=""){
            UnSerializeDataBase.imgList.add(BitmapMap(otherLease.companyCredential.businessLicensePath, adapter.mData[7].key))
        }
//        adapter.mData[7].radioButtonValue = if(supplyLeaseCar.carTable.isDriver=="true") "1" else "0"
//        adapter.mData[8].radioButtonValue = if(supplyLeaseCar.carTable.isInsurance=="true") "1" else "0"
        //9
        val itemMultiStyleItem = ArrayList<MultiStyleItem>()
        val leaseList = otherLease.leaseList
        if(leaseList!=null)
            for(j in leaseList){
                itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.type,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","工器具清册发布")
                mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                mData[0].inputSingleContent = j.type
                    UnSerializeDataBase.inventoryIdKey = "leaseServeId"
                    UnSerializeDataBase.inventoryId = j.leaseServeId
                    mData[0].id = j.id
                mData[1].inputSingleContent = j.specificationsModels
                mData[2].inputSingleContent = j.unit
                mData[3].inputSingleContent = j.quantity
                if(j.remark!=null)
                    mData[5].textAreaContent = j.remark
                itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","工器具清册发布")
                    FragmentHelper.switchFragment(activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle), R.id.frame_my_release,"")
                }
            }
        adapter.mData[8].itemMultiStyleItem = itemMultiStyleItem
        adapter.mData[10].radioButtonValue = if(otherLease.leaseConstructionTool.isDistribution=="true") "1" else "0"
        adapter.mData[11].selectContent = otherLease.leaseConstructionTool.conveyancePropertyInsurance
        // 14
        adapter.mData[15].inputUnitContent = otherLease.leaseConstructionTool.validTime
    }

    /**
     * @三方服务
     */
    private fun initTripartiteServices(adapter: RecyclerviewAdapter) {
        adapter.urlPath = Constants.HttpUrlPath.Provider.updateThirdServices
        val supplyThirdParty = arguments!!.getSerializable("data") as SupplyThirdParty
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = "三方服务"
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryId
        UnSerializeDataBase.inventoryIdKey
        adapter.mData[0].id = supplyThirdParty.id
        adapter.mData[0].singleDisplayRightContent = supplyThirdParty.serveType
        adapter.mData[1].inputSingleContent = supplyThirdParty.companyCredential.companyName
        if(supplyThirdParty.companyCredential.companyAbbreviation!=null)
            adapter.mData[2].inputSingleContent = supplyThirdParty.companyCredential.companyAbbreviation
        adapter.mData[3].inputSingleContent = supplyThirdParty.companyCredential.companyAddress
        adapter.mData[4].inputSingleContent = supplyThirdParty.companyCredential.legalPersonName
        adapter.mData[5].inputSingleContent = supplyThirdParty.companyCredential.legalPersonPhone
        if(supplyThirdParty.companyCredential.legalPersonIdCardPath!=null && supplyThirdParty.companyCredential.legalPersonIdCardPath!=""){
            UnSerializeDataBase.imgList.add(BitmapMap(supplyThirdParty.companyCredential.legalPersonIdCardPath, adapter.mData[6].key))
        }
        if(supplyThirdParty.companyCredential.businessLicensePath!=null && supplyThirdParty.companyCredential.businessLicensePath!=""){
            UnSerializeDataBase.imgList.add(BitmapMap(supplyThirdParty.companyCredential.businessLicensePath, adapter.mData[7].key))
        }
        //10
        //11
        adapter.mData[12].inputUnitContent = supplyThirdParty.validTime
        if(supplyThirdParty.businessScope!=null)
        adapter.mData[13].inputUnitContent = supplyThirdParty.businessScope
    }


    fun update(itemMultiStyleItem:List<MultiStyleItem>){
        Log.i("position is",mAdapter!!.mData[0].selected.toString())
        mAdapter!!.mData[mAdapter!!.mData[0].selected].itemMultiStyleItem = itemMultiStyleItem
        Log.i("item size",mAdapter!!.mData[mAdapter!!.mData[0].selected].itemMultiStyleItem.size.toString())
    }
}