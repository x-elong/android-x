package com.example.eletronicengineer.fragment.sdf

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
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.startSendMessage
import com.example.eletronicengineer.utils.uploadImage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_supply_publish.*
import kotlinx.android.synthetic.main.activity_supply_publish.view.*
import kotlinx.android.synthetic.main.activity_supply_publish.view.submit
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray

import org.json.JSONObject
import java.io.File

class SupplyFragment:Fragment(){
    companion object{
        fun newInstance(args:Bundle): SupplyFragment
        {
            val fragment= SupplyFragment()
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
    var vipId:String=""
    //车辆表
    var carType:String=""//车辆类型
    var maxPassengers:Long= Long.MIN_VALUE//
    var construction:Int=-1//车辆结构
    var isDriver:Boolean =false
    var isInsurance:Boolean =false
    var carPhotoPath:String=""//车辆照片
    var carNumber:String=""//拍照号码
    //负责人所在地
    var issuerBelongSite:String=""
    var remark:String=""
    //营业执照
    var businessLicensePath:String=""

    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i("onCreateView","running")
        mView= inflater.inflate(R.layout.activity_supply_publish,container,false)
        initFragment(mView)
        return mView
    }
    fun initFragment(mView:View){
        val data=arguments
        val adapterGenerate=AdapterGenerate()
        adapterGenerate.context=mView.context
        adapterGenerate.activity=activity as SupplyActivity
        //加载选择的界面
        if(mAdapter==null){
            val result = Observable.create<RecyclerviewAdapter>{
                val adapter = switchAdapter(adapterGenerate,data!!.getInt("type"))
                it.onNext(adapter)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mAdapter=it
                    mView.rv_supply_content.adapter=mAdapter
                    mView.rv_supply_content.layoutManager= LinearLayoutManager(context)
                }

        }
        else{
            mView.rv_supply_content.adapter=mAdapter
            mView.rv_supply_content.layoutManager= LinearLayoutManager(context)
        }
        //标题
        val  selectContent2=arguments!!.getString("selectContent2")
        mView.tv_title_title1.setText(selectContent2)
        //返回
        mView.tv_title_back.setOnClickListener{
            UnSerializeDataBase.imgList.clear()
            activity!!.finish()
        }
        //发布
        mView.submit.setOnClickListener{
            val networkAdapter= NetworkAdapter(mAdapter!!.mData, submit.context)
            val provider=NetworkAdapter.Provider(mAdapter!!.mData,submit.context)
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
                            //remark
                            MultiStyleItem.Options.INPUT_WITH_TEXTAREA->{
                                when(i.textAreaTitle){
                                    "附加信息"->{
                                        remark=i.textAreaContent
                                    }
                                }
                            }
//                            MultiStyleItem.Options.MULTI_RADIO_BUTTON -> {
//                                when(i.radioButtonTitle) {
//                                    "是否配驾驶员"->{
//
//                                     val   tmp = i.radioButtonValue
//                                       if(tmp=="1"){
//                                           isDriver=true
//                                       }else{
//                                           isDriver=false
//                                       }
//                                    }
//                                    "保险状态"->{
//                                        val   tmp = i.radioButtonValue
//                                        if(tmp=="1"){
//                                            isInsurance=true
//                                        }else{
//                                            isInsurance=false
//                                        }
//                                    }
//                                }
//                            }
                            MultiStyleItem.Options.SHIFT_INPUT -> {
                                when(i.shiftInputTitle) {
                                    "可服务地域"-> {
                                        issuerBelongSite=i.shiftInputContent
                                    }
                                    "营业执照"->{
                                        for(j in UnSerializeDataBase.imgList){
                                            if(j.key==i.key){
                                                businessLicensePath=j.path
                                            }
                                        }
                                    }

                                }
                            }
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
                                    "是否配驾驶员"->{
                                        isDriver = if((1-i.selectOption1Items.indexOf(i.selectContent))==1) true else false   //Int
                                    }
                                    "保险状态"->{
                                        isInsurance = if((1-i.selectOption1Items.indexOf(i.selectContent))==1) true else false    //Int
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
                    when(arguments!!.getInt("type")) {
                        Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal->{//主网
                            json.put("majorNetwork",
                                JSONObject().put("name",name)
                                    .put("validTime",validTime.toString())
                                    .put("issuerBelongSite",issuerBelongSite)
                                    .put("issuerName",UnSerializeDataBase.idCardName)
                                    .put("phone",UnSerializeDataBase.userPhone)
                                    .put("remark",remark)
                            )
                        }
                        Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal->{//配网
                            json.put("distribuionNetwork",
                                JSONObject().put("name",name)
                                    .put("validTime",validTime.toString())
                                    .put("issuerBelongSite",issuerBelongSite)
                                    .put("issuerName",UnSerializeDataBase.idCardName)
                                    .put("phone",UnSerializeDataBase.userPhone)
                                    .put("remark",remark)
                            )
                        }
                        Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal->{//变电
                            json.put("powerTransformation",
                                JSONObject().put("name",name)
                                    .put("validTime",validTime.toString())
                                    .put("issuerBelongSite",issuerBelongSite)
                                    .put("issuerName",UnSerializeDataBase.idCardName)
                                    .put("phone",UnSerializeDataBase.userPhone)
                                    .put("remark",remark)
                            )
                        }
                        Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal->{//测量设计
                            json.put("measureDesign",
                                JSONObject().put("name",name)
                                    .put("validTime",validTime.toString())
                                    .put("issuerBelongSite",issuerBelongSite)
                                    .put("issuerName",UnSerializeDataBase.idCardName)
                                    .put("phone",UnSerializeDataBase.userPhone)
                                    .put("remark",remark)
                            )
                        }
                        //团队服务——马帮运输
                        Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal->{
                            json.put("caravanTransport",
                                JSONObject().put("name",name)
                                .put("validTime",validTime.toString())
                                .put("horseNumber",horseNumber.toString())
                                    .put("issuerBelongSite",issuerBelongSite)
                                    .put("issuerName",UnSerializeDataBase.idCardName)
                                    .put("phone",UnSerializeDataBase.userPhone)
                                    .put("remark",remark)
                            )
                        }
                        //团队服务——桩基
                        Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal->{
                            json.put("pileFoundation",
                                JSONObject().put("name",name)
                                    .put("validTime",validTime.toString())
                                .put("workDia",workDia.toString())
                                .put("location",location)
                                    .put("issuerBelongSite",issuerBelongSite)
                                    .put("issuerName",UnSerializeDataBase.idCardName)
                                    .put("phone",UnSerializeDataBase.userPhone)
                                    .put("remark",remark)
                            )
                        }
                        //团队服务——非开挖
                        Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal-> {
                            json.put("validTime",validTime.toString())
                                .put("issuerBelongSite",issuerBelongSite)
                                .put("issuerName",UnSerializeDataBase.idCardName)
                                .put("phone",UnSerializeDataBase.userPhone)
                                .put("remark",remark)
                        }
                        Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal->{//实验调试
                            json.put("issuerBelongSite",issuerBelongSite)
                                .put("issuerName",UnSerializeDataBase.idCardName)
                                .put("phone",UnSerializeDataBase.userPhone)
                                .put("remark",remark)

                        }
                        Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal->{//跨越架
                            json.put("validTime",validTime.toString())
                                .put("issuerBelongSite",issuerBelongSite)
                                .put("issuerName",UnSerializeDataBase.idCardName)
                                .put("phone",UnSerializeDataBase.userPhone)
                                .put("remark",remark)
                        }
                        Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal->{//运维
                            json.put("issuerBelongSite",issuerBelongSite)
                                .put("issuerName",UnSerializeDataBase.idCardName)
                                .put("phone",UnSerializeDataBase.userPhone)
                                .put("remark",remark)

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
                                    .put("carPhotoPath",mAdapter!!.mData[9].shiftInputPicture)
                                    .put("carNumber",carNumber)
                            )
                                .put("issuerBelongSite",issuerBelongSite)
                                .put("issuerName",UnSerializeDataBase.idCardName)
                                .put("contactPhone",UnSerializeDataBase.userPhone)
                        }
                        //三方服务
                        Constants.FragmentType.TRIPARTITE_TRAINING_CERTIFICATE_TYPE.ordinal,
                        Constants.FragmentType.TRIPARTITE_FINANCIAL_ACCOUNTING_TYPE.ordinal,
                        Constants.FragmentType.TRIPARTITE_AGENCY_QUALIFICATION_TYPE.ordinal,
                        Constants.FragmentType.TRIPARTITE_TENDER_SERVICE_TYPE.ordinal,
                        Constants.FragmentType.TRIPARTITE_LEGAL_ADVICE_TYPE.ordinal,
                        Constants.FragmentType.TRIPARTITE_SOFTWARE_SERVICE_TYPE.ordinal,
                        Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal
                        ->{
                            json.put("companyCredential",
                                JSONObject().put("legalPersonName",mAdapter!!.mData[4].inputSingleContent)
                                    .put("legalPersonPhone",mAdapter!!.mData[5].inputSingleContent)
                                    .put("companyName",mAdapter!!.mData[1].inputSingleContent)
                                    .put("companyAbbreviation",mAdapter!!.mData[2].inputSingleContent)
                                    .put("businessLicensePath",businessLicensePath)
                                    .put("companyAddress",mAdapter!!.mData[3].inputSingleContent)
                            )
                        }
                    }
                    provider.generateJsonRequestBody(json).subscribe {
                        val loadingDialog = LoadingDialog(mView.context, "正在发布...", R.mipmap.ic_dialog_loading)
                        loadingDialog.show()
                        val result = startSendMessage(it, UnSerializeDataBase.dmsBasePath+mAdapter!!.urlPath).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        loadingDialog.dismiss()
                                        val json = JSONObject(it.string())
                                        if (json.getInt("code") == 200) {
                                                Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show()
                                                mView.tv_title_back.callOnClick()
                                        }else if(json.getInt("code") == 403){
                                            Toast.makeText(context, "${json.getString("desc")} 请升级为更高级会员", Toast.LENGTH_SHORT).show()
                                        } else if (json.getInt("code") == 400) {
                                            Toast.makeText(context, "发布失败", Toast.LENGTH_SHORT).show()
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
    fun switchAdapter(adapterGenerate: AdapterGenerate,Type: Int):RecyclerviewAdapter
    {
        lateinit var adapter:RecyclerviewAdapter
        when(Type){
            Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "普工"
                val selectOption1Items: List<String> = listOf("普工")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_LEADING_CADRE_TYPE.ordinal -> {
                adapter = adapterGenerate.PersonalService()
                var singleDisplayRightContent = "负责人"
                var selectOption1Items: List<String> =
                    listOf("配网项目负责人", "配网班组负责人", "主网项目负责人", "主网基础负责人", "主网组塔负责人","主网架线负责人","变电项目负责人","变电土建负责人","变电一次负责人","变电二次负责人","变电调试负责人")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_ENGINEER_TYPE.ordinal -> {
                adapter = adapterGenerate.PersonalService()
                var singleDisplayRightContent = "工程师"
                var selectOption1Items: List<String> =
                    listOf("助理工程师", "中级工程师", "高级工程师")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_DESIGNER_TYPE.ordinal -> {
                adapter = adapterGenerate.PersonalService()
                var singleDisplayRightContent = "设计员"
                var selectOption1Items: List<String> =
                    listOf("主网", "配网", "变电")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_SPECIAL_WORK_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "特种作业"
                val selectOption1Items: List<String> =
                    listOf("低压电工作业", "高压电工作业", "电力电缆作业", "继电保护作业", "电气试验作业", "融化焊接与热切割作业", "登高架设作业")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items

            }
            Constants.FragmentType.PERSONAL_PROFESSIONAL_OPERATION_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "专业操作"
                val selectOption1Items: List<String> =
                    listOf("压接操作", "机动绞磨操作", "牵张设备操作", "起重机械操作", "钢筋工", "混凝土工", "木工", "模板工", "油漆工", "砌筑工", "通风工", "打桩工", "架子工工")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_SURVEYOR_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "测量工"
                val selectOption1Items: List<String> = listOf("测量工")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_DRIVER_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "驾驶员"
                val selectOption1Items: List<String> =
                    listOf("驾驶证A1", "驾驶证A2", "驾驶证A3", "驾驶证B1", "驾驶证B2", "驾驶证C1", "驾驶证C2", "驾驶证C3", "驾驶证D", "驾驶证E")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_NINE_MEMBERS_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "九大员"
                val selectOption1Items: List<String> =
                    listOf("施工员", "安全员", "质量员", "材料员", "资料员", "预算员", "标准员", "机械员", "劳务员")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_REGISTRATION_CLASS_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                val singleDisplayRightContent = "注册类"
                val selectOption1Items: List<String> = listOf("造价工程师", "一级建造师","二级建造师", "安全工程师", "电气工程师")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_OTHER_TYPE.ordinal->{
                adapter=adapterGenerate.PersonalService()
                adapter.mData[0].singleDisplayRightContent = "其他"
                adapter.mData[1].selectOption1Items = listOf("其他")
//                adapter.mData[0].options=MultiStyleItem.Options.SINGLE_INPUT
//                adapter.mData[0].inputSingleTitle=adapter.mData[1].singleDisplayRightTitle
//                adapter.mData[1].options=MultiStyleItem.Options.SINGLE_INPUT
//                adapter.mData[1].inputSingleTitle=adapter.mData[2].selectTitle
            }
            //团队服务——变电施工
            Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupSubstationConstruction()
                val singleDisplayRightContent = "变电施工队"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                name =singleDisplayRightContent//名称
                adapter.urlPath=Constants.HttpUrlPath.Provider.PowerTransformation
            }
            //团队服务——主网施工
            Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupSubstationConstruction()
                val singleDisplayRightContent = "主网施工队"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath=Constants.HttpUrlPath.Provider.MajorNetwork

            }
            //团队服务——配网施工
            Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupSubstationConstruction()
                val singleDisplayRightContent = "配网施工队"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath=Constants.HttpUrlPath.Provider.DistribuionNetwork
            }
            //团队服务——测量设计
            Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupMeasurementDesign()
                val singleDisplayRightContent = "测量设计"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Provider.MeasureDesign
            }
            //团队服务——马帮运输
            Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupCaravanTransportation()
                val singleDisplayRightContent = "马帮运输"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Provider.CaravanTransport
            }
            //团队服务——桩基
            Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupPileFoundationConstruction()
                val singleDisplayRightContent = "桩基服务"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Provider.PileFoundation
            }
            //团队服务——非开挖
            Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal->{/////////////
                adapter=adapterGenerate.ProviderGroupNonExcavation()
                val singleDisplayRightContent = "非开挖"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Provider.Unexcavation

            }
            //团队服务——试验调试
            Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupTestDebugging()
                val singleDisplayRightContent = "实验调试"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Provider.TestTeam
            }
            //团队服务——跨越架
            Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal->{
                adapter=adapterGenerate.ProviderGroupCrossingFrame()
                adapter.urlPath = Constants.HttpUrlPath.Provider.SpanWoodenSupprt
                val singleDisplayRightContent = "跨越架"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
            }
            //团队服务——运维
            Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal->{
                adapter=adapterGenerate.OperationAndMaintenance()
                adapter.urlPath = Constants.HttpUrlPath.Provider.RunningMaintain
                val singleDisplayRightContent = "运行维护"
                name =singleDisplayRightContent//名称
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
            }
            //租赁服务——车辆
            Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal->{
                adapter=adapterGenerate.VehicleRental()
                adapter.urlPath = Constants.HttpUrlPath.Provider.LeaseCar
                val singleDisplayRightContent = "车辆租赁"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
            }
            //租赁服务——工器具
            Constants.FragmentType.TOOL_LEASING_TYPE.ordinal->{
                val bundle=Bundle()
                bundle.putInt("type",Type)
                adapter=adapterGenerate.EquipmentLeasing(bundle)
                //   adapter.mData[0].singleDisplayRightContent = "工器具租赁"
                var singleDisplayRightContent = "工器具租赁"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath=Constants.HttpUrlPath.Provider.LcTool
            }
            //租赁服务--机械
            Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal->{
                val bundle=Bundle()
                bundle.putInt("type",Type)
                adapter=adapterGenerate.EquipmentLeasing(bundle)
                adapter.mData[0].singleDisplayRightContent = "机械租赁"
                adapter.urlPath=Constants.HttpUrlPath.Provider.LeaseMachinery
            }
            //租赁服务--设备
            Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal->{
                val bundle=Bundle()
                bundle.putInt("type",Type)
                adapter=adapterGenerate.EquipmentLeasing(bundle)
                adapter.mData[0].singleDisplayRightContent = "设备租赁"
                adapter.urlPath=Constants.HttpUrlPath.Provider.LeaseFacility
            }
            //培训办证
            Constants.FragmentType.TRIPARTITE_TRAINING_CERTIFICATE_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[0].singleDisplayRightContent = "培训办证"
            }
            //三方财务记账
            Constants.FragmentType.TRIPARTITE_FINANCIAL_ACCOUNTING_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[0].singleDisplayRightContent = "财务记账"
            }
            //三方代办资格
            Constants.FragmentType.TRIPARTITE_AGENCY_QUALIFICATION_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[0].singleDisplayRightContent = "代办资格"
            }
            //三方标书服务
            Constants.FragmentType.TRIPARTITE_TENDER_SERVICE_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[0].singleDisplayRightContent = "标书服务"
            }
            //三方法律咨询
            Constants.FragmentType.TRIPARTITE_LEGAL_ADVICE_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[0].singleDisplayRightContent = "法律咨询"
            }
            //三方软件服务
            Constants.FragmentType.TRIPARTITE_SOFTWARE_SERVICE_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
                adapter.mData[0].singleDisplayRightContent = "软件服务"
            }
            //三方其他
            Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal->{
                adapter=adapterGenerate.ServiceInformationEntry()
            }
        }
        return adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
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
        if(check(itemMultiStyleItem))
        {
            mAdapter!!.mData[mAdapter!!.mData[0].selected].submitIsNecessary = true
        }
        Log.i("position is",mAdapter!!.mData[0].selected.toString())
        mAdapter!!.mData[mAdapter!!.mData[0].selected].itemMultiStyleItem = itemMultiStyleItem
        Log.i("item size",mAdapter!!.mData[mAdapter!!.mData[0].selected].itemMultiStyleItem.size.toString())
    }
    fun refresh(imagePath:String,position:Int){
        mAdapter!!.mData[position].shiftInputPicture = imagePath
        mAdapter!!.notifyDataSetChanged()
    }
    fun file(filePath:String){
        mAdapter!!.mData[9].filePath = filePath
        mAdapter!!.notifyDataSetChanged()
    }
}