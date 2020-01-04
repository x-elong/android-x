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
import com.example.eletronicengineer.db.DisplaySupply.*
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
    var name:String=""//名称
    var type = 0
    var validTime = "0"
    lateinit var mView:View
    lateinit var id:String
    lateinit var companyCredentialId:String
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
        if(validTime.toInt()<=0)
            mView.btn_modify_job_information.text = "发布"

        mView.btn_modify_job_information.setOnClickListener{

            val networkAdapter= NetworkAdapter(mAdapter!!.mData, mView.context)
            val provider=NetworkAdapter.Provider(mAdapter!!.mData,mView.context)
            if(networkAdapter.check()){
                    val json=JSONObject()
                    json.put("id",id)
                    when(type) {
                        Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal->{//主网
                            json.put("majorNetwork",
                                JSONObject().put("name",name)
                                    .put("validTime",NetworkAdapter.Provider().parseToInt(mAdapter!!.mData[9]).toString())
                                    .put("issuerBelongSite",mAdapter!!.mData[3].shiftInputContent)
                                    .put("issuerName",UnSerializeDataBase.idCardName)
                                    .put("phone",UnSerializeDataBase.userPhone)
                                    .put("id",id)
                                    .put("teamServeId",UnSerializeDataBase.inventoryId)
                                    .put("isCar",mAdapter!!.mData[2].itemMultiStyleItem.isNotEmpty())
                                    .put("isConstructionTool",mAdapter!!.mData[6].itemMultiStyleItem.isNotEmpty())
                                    .put("remark",mAdapter!!.mData[11].textAreaContent)
                            )
                        }
                        Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal->{//配网
                            json.put("distribuionNetwork",
                                JSONObject().put("name",name)
                                    .put("validTime",NetworkAdapter.Provider().parseToInt(mAdapter!!.mData[9]).toString())
                                    .put("issuerBelongSite",mAdapter!!.mData[3].shiftInputContent)
                                    .put("issuerName",UnSerializeDataBase.idCardName)
                                    .put("phone",UnSerializeDataBase.userPhone)
                                    .put("id",id)
                                    .put("teamServeId",UnSerializeDataBase.inventoryId)
                                    .put("isCar",mAdapter!!.mData[2].itemMultiStyleItem.isNotEmpty())
                                    .put("isConstructionTool",mAdapter!!.mData[6].itemMultiStyleItem.isNotEmpty())
                                    .put("remark",mAdapter!!.mData[11].textAreaContent)
                            )
                        }
                        Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal->{//变电
                            json.put("powerTransformation",
                                JSONObject().put("name",name)
                                    .put("validTime",NetworkAdapter.Provider().parseToInt(mAdapter!!.mData[9]).toString())
                                    .put("issuerBelongSite",mAdapter!!.mData[3].shiftInputContent)
                                    .put("issuerName",UnSerializeDataBase.idCardName)
                                    .put("phone",UnSerializeDataBase.userPhone)
                                    .put("id",id)
                                    .put("teamServeId",UnSerializeDataBase.inventoryId)
                                    .put("isCar",mAdapter!!.mData[2].itemMultiStyleItem.isNotEmpty())
                                    .put("isConstructionTool",mAdapter!!.mData[6].itemMultiStyleItem.isNotEmpty())
                                    .put("remark",mAdapter!!.mData[11].textAreaContent)
                            )
                        }
                        Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal->{//测量设计
                            json.put("measureDesign",
                                JSONObject().put("name",name)
                                    .put("validTime",NetworkAdapter.Provider().parseToInt(mAdapter!!.mData[9]).toString())
                                    .put("issuerBelongSite",mAdapter!!.mData[3].shiftInputContent)
                                    .put("issuerName",UnSerializeDataBase.idCardName)
                                    .put("phone",UnSerializeDataBase.userPhone)
                                    .put("id",id)
                                    .put("teamServeId",UnSerializeDataBase.inventoryId)
                                    .put("isCar",mAdapter!!.mData[2].itemMultiStyleItem.isNotEmpty())
                                    .put("isConstructionTool",mAdapter!!.mData[6].itemMultiStyleItem.isNotEmpty())
                                    .put("remark",mAdapter!!.mData[11].textAreaContent)
                            )
                                .put("implementationRanges",NetworkAdapter().parseToString(mAdapter!!.mData[5]))
                                .put("voltages",NetworkAdapter.Provider().parseToString(mAdapter!!.mData[4]))
                        }
                        //团队服务——马帮运输
                        Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal->{
                            json.put("caravanTransport",
                                JSONObject().put("name",name)
                                .put("validTime",NetworkAdapter.Provider().parseToInt(mAdapter!!.mData[6]).toString())
                                .put("horseNumber",NetworkAdapter.Provider().parseToInt(mAdapter!!.mData[2]).toString())
                                    .put("issuerBelongSite",mAdapter!!.mData[5].shiftInputContent)
                                    .put("issuerName",UnSerializeDataBase.idCardName)
                                    .put("phone",UnSerializeDataBase.userPhone)
                                    .put("remark","")
                                    .put("id",id)
                                    .put("teamServeId",UnSerializeDataBase.inventoryId)
                                    .put("remark",mAdapter!!.mData[8].textAreaContent)
                            )
                        }
                        //团队服务——桩基
                        Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal->{
                            json.put("pileFoundation",
                                JSONObject().put("id",id)
                                    .put("name","桩基服务")
//                                    .put("isCar",if(mAdapter!!.mData[2].itemMultiStyleItem.isEmpty()) "fasle" else "true")
//                                    .put("isConstructionTool",if(mAdapter!!.mData[7].itemMultiStyleItem.isEmpty()) "fasle" else "true")
                                    .put("workDia",mAdapter!!.mData[5].selectOption1Items.indexOf(mAdapter!!.mData[5].selectContent)+1)
                                    .put("location",mAdapter!!.mData[6].selectContent.replace(" "," / "))
                                    .put("teamServeId",UnSerializeDataBase.inventoryId)
                                    .put("issuerName",mAdapter!!.mData[8].singleDisplayRightContent)
                                    .put("phone",mAdapter!!.mData[9].singleDisplayRightContent)
                                    .put("validTime",mAdapter!!.mData[10].inputUnitContent)
                                    .put("issuerBelongSite",mAdapter!!.mData[3].shiftInputContent)
                                    .put("isCar",mAdapter!!.mData[2].itemMultiStyleItem.isNotEmpty())
                                    .put("isConstructionTool",mAdapter!!.mData[7].itemMultiStyleItem.isNotEmpty())
                                    .put("remark",mAdapter!!.mData[12].textAreaContent)
                            )
                        }
                        //团队服务——非开挖
                        Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal-> {
                            json.put("id",id)
                                .put("issuerBelongSite",mAdapter!!.mData[4].shiftInputContent)
                                .put(UnSerializeDataBase.inventoryIdKey,UnSerializeDataBase.inventoryId)
                                .put("isCar",mAdapter!!.mData[2].itemMultiStyleItem.isNotEmpty())
                                .put("isConstructionTool",mAdapter!!.mData[3].itemMultiStyleItem.isNotEmpty())
                                .put("remark",mAdapter!!.mData[9].textAreaContent)
                        }
                        Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal->{//实验调试
                            json.put("id",id)
                                .put("issuerBelongSite",mAdapter!!.mData[3].shiftInputContent)
                                .put(UnSerializeDataBase.inventoryIdKey,UnSerializeDataBase.inventoryId)
                                .put("isCar",mAdapter!!.mData[2].itemMultiStyleItem.isNotEmpty())
                                .put("isConstructionTool",mAdapter!!.mData[7].itemMultiStyleItem.isNotEmpty())
                                .put("remark",mAdapter!!.mData[12].textAreaContent)
                        }
                        Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal->{//跨越架
                            json.put("id",id)
                                .put("issuerBelongSite",mAdapter!!.mData[3].shiftInputContent)
                                .put(UnSerializeDataBase.inventoryIdKey,UnSerializeDataBase.inventoryId)
                                .put("isCar",mAdapter!!.mData[2].itemMultiStyleItem.isNotEmpty())
                                .put("isConstructionTool",mAdapter!!.mData[3].itemMultiStyleItem.isNotEmpty())
                                .put("remark",mAdapter!!.mData[9].textAreaContent)
                        }
                        Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal->{//运维
                            val isCar= mAdapter!!.mData[1].itemMultiStyleItem.isNotEmpty()
                            json.put("id",id)
                                .put("issuerBelongSite",mAdapter!!.mData[3].shiftInputContent)
                                .put("isCar",mAdapter!!.mData[1].itemMultiStyleItem.isNotEmpty())
                                .put("isConstructionTool",mAdapter!!.mData[2].itemMultiStyleItem.isNotEmpty())
                                .put("remark",mAdapter!!.mData[12].textAreaContent)
//                                .put("name",name)
//                                .put("issuerName",UnSerializeDataBase.idCardName)
//                                .put("phone",UnSerializeDataBase.userPhone)
                        }
                        Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal->{//车辆租赁
                            json.put("carTable",
                                JSONObject().put("carType",NetworkAdapter.Provider().parseToString(mAdapter!!.mData[1]))
                                    .put("maxPassengers",mAdapter!!.mData[3].inputUnitContent.toLongOrNull().toString())
                                    .put("maxWeight",mAdapter!!.mData[4].inputUnitContent.toLongOrNull().toString())
                                    .put("construction",(1-mAdapter!!.mData[6].selectOption1Items.indexOf(mAdapter!!.mData[6].selectContent)).toString())
                                    .put("lenghtCar",mAdapter!!.mData[5].inputUnitContent.toLongOrNull().toString())
                                    .put("isDriver",NetworkAdapter.Provider().parseToBoolean(mAdapter!!.mData[7]))
                                    .put("isInsurance",NetworkAdapter.Provider().parseToBoolean(mAdapter!!.mData[8]))
                                    .put("carPhotoPath","")
                                    .put("carNumber",NetworkAdapter.Provider().parseToString(mAdapter!!.mData[2]))
                                    .put("comment",mAdapter!!.mData[17].textAreaContent)
                                    .put("id",UnSerializeDataBase.inventoryIdKey)
                                    .put("leaseCarId",UnSerializeDataBase.inventoryId)
                            )
                        }
                        Constants.FragmentType.TOOL_LEASING_TYPE.ordinal,
                        Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal,
                        Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal->{
//                            val key = when(type){
//                                Constants.FragmentType.TOOL_LEASING_TYPE.ordinal->"leaseConstructionTool"
//                                Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal->"leaseMachinery"
//                                Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal->"leaseFacility"
//                                else -> ""
//                            }
//                            json.put("companyCredential",
//                                JSONObject().put("id",companyCredentialId)
//                                    .put("legalPersonName",mAdapter!!.mData[4].inputSingleContent)
//                                    .put("legalPersonIdCardPath","")
//                                    .put("legalPersonPhone",mAdapter!!.mData[5].inputSingleContent)
//                                    .put("companyName",mAdapter!!.mData[1].inputSingleContent)
//                                    .put("companyAbbreviation",mAdapter!!.mData[2].inputSingleContent)
//                                    .put("businessLicensePath","")
//                                    .put("companyAddress",mAdapter!!.mData[3].inputSingleContent)
//                            )
//                                .put(key,JSONObject().put("id",id)
//                                    .put("leaseServeId",UnSerializeDataBase.inventoryId)
//                                    .put("conveyancePropertyInsurance",mAdapter!!.mData[11].selectContent)
//                                    .put("contact",mAdapter!!.mData[12].singleDisplayRightContent)
//                                    .put("contactPhone",mAdapter!!.mData[13].singleDisplayRightContent)
//                                    .put("issuerBelongSite",mAdapter!!.mData[14].shiftInputContent)
//                                    .put("isDistribution",NetworkAdapter.Provider().parseToBoolean(mAdapter!!.mData[10]))
//                                    .put("leaseConTractPath","")
//                                    .put("validTime",mAdapter!!.mData[15].inputUnitContent)
//                                    .put("variety",mAdapter!!.mData[0].singleDisplayRightContent)
//                                )
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
                                    .put("businessLicensePath",NetworkAdapter.Provider().parseToString(mAdapter!!.mData[6]))
                                    .put("companyAddress",mAdapter!!.mData[3].inputSingleContent)
                                    .put("businessScope",mAdapter!!.mData[13].textAreaContent)
                            )
                        }
                    }

                    provider.generateJsonRequestBody(json).subscribe {
                        var message = "修改"
                        if(validTime.toInt()<=0)
                            message = "发布"
                        val loadingDialog = LoadingDialog(mView.context, "正在请求...", R.mipmap.ic_dialog_loading)
                        loadingDialog.show()
                        val result = (if(validTime.toInt()<=0)
                            startSendMessage(it, UnSerializeDataBase.dmsBasePath+mAdapter!!.urlPath)
                        else
                            putSimpleMessage(it, UnSerializeDataBase.dmsBasePath+mAdapter!!.urlPath))
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        loadingDialog.dismiss()
                                        val json = JSONObject(it.string())
                                        Log.i("json",json.toString())
                                        if (json.getString("desc") == "OK") {
                                            ToastHelper.mToast(mView.context,"${message}成功")
                                                mView.tv_modify_job_information_back.callOnClick()
                                        }else if(json.getInt("code") == 403){
                                            ToastHelper.mToast(mView.context,"${json.getString("desc")} 请升级为更高级会员")
                                        } else if (json.getString("desc") == "FAIL") {
                                            ToastHelper.mToast(mView.context,"${message}失败")
                                        }
                                    },
                                    {
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(mView.context,"服务器异常")
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
            Constants.FragmentType.PERSONAL_LEADING_CADRE_TYPE.ordinal -> {
                adapter = adapterGenerate.PersonalService()
                val singleDisplayRightContent = "负责人"
                val selectOption1Items: List<String> =
                    listOf("配网项目负责人", "配网班组负责人", "主网项目负责人", "主网基础负责人", "主网组塔负责人","主网架线负责人","变电项目负责人","变电土建负责人","变电一次负责人","变电二次负责人","变电调试负责人")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                initPersonalService(adapter)
            }
            Constants.FragmentType.PERSONAL_ENGINEER_TYPE.ordinal -> {
                adapter = adapterGenerate.PersonalService()
                val singleDisplayRightContent = "工程师"
                val selectOption1Items: List<String> =
                    listOf("助理工程师", "中级工程师", "高级工程师")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                initPersonalService(adapter)
            }
            Constants.FragmentType.PERSONAL_DESIGNER_TYPE.ordinal -> {
                adapter = adapterGenerate.PersonalService()
                val singleDisplayRightContent = "设计员"
                val selectOption1Items: List<String> =
                    listOf("主网", "配网", "变电")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
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

        if(supplyPersonDetail.salaryUnit!="面议" || supplyPersonDetail.workMoney!="-1.0" ){
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
        validTime = supplyPersonDetail.validTime
        if(validTime.toInt()<=0)
            adapter.urlPath = Constants.HttpUrlPath.Provider.PersonalService

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
        UnSerializeDataBase.inventoryIdKey = "teamServeId"
        UnSerializeDataBase.inventoryId = network.powerTransformation.teamServeId
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
            adapter.mData[1].necessary = false
            adapter.mData[1].itemMultiStyleItem = itemMultiStyleItem
            if(adapter.mData[1].itemMultiStyleItem.isEmpty())
                adapter.mData[1].necessary = false
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
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()-1]
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
        if(network.powerTransformation.issuerBelongSite!=null){
            val issuerBelongSite = network.powerTransformation.issuerBelongSite.split("、")
            for (j in issuerBelongSite){
                val position = adapter.mData[3].placeCheckArray.indexOf(j)
                if(position>=0)
                    adapter.mData[3].placeCheckBoolenArray[position] = true
            }
            adapter.mData[3].shiftInputContent = network.powerTransformation.issuerBelongSite
        }
        val voltages = network.voltages
        if(voltages!=null){
            val voltageDegrees = ArrayList<String>()
            for (j in voltages)
                voltageDegrees.addAll(j.voltageDegree.split("、"))
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
        validTime = network.powerTransformation.validTime
        if(network.powerTransformation.remark!=null)
            adapter.mData[11].textAreaContent = network.powerTransformation.remark
        if(validTime.toInt()<=0)
            adapter.urlPath = Constants.HttpUrlPath.Provider.PowerTransformation

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
        UnSerializeDataBase.inventoryIdKey = "teamServeId"
        UnSerializeDataBase.inventoryId = network.majorNetwork.teamServeId
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
            if(adapter.mData[1].itemMultiStyleItem.isEmpty())
                adapter.mData[1].necessary = false
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
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()-1]
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
        if(network.majorNetwork.issuerBelongSite!=null){
            val issuerBelongSite = network.majorNetwork.issuerBelongSite.split("、")
            for (j in issuerBelongSite){
                val position = adapter.mData[3].placeCheckArray.indexOf(j)
                if(position>=0)
                    adapter.mData[3].placeCheckBoolenArray[position] = true
            }
            adapter.mData[3].shiftInputContent = network.majorNetwork.issuerBelongSite
        }
        val voltages = network.voltages
        if(voltages!=null){
            val voltageDegrees = ArrayList<String>()
            for (j in voltages)
                voltageDegrees.addAll(j.voltageDegree.split("、"))
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
        validTime = network.majorNetwork.validTime
        if(network.majorNetwork.remark!=null)
            adapter.mData[11].textAreaContent = network.majorNetwork.remark
        if(validTime.toInt()<=0)
            adapter.urlPath = Constants.HttpUrlPath.Provider.MajorNetwork

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
        UnSerializeDataBase.inventoryIdKey = "teamServeId"
        UnSerializeDataBase.inventoryId = network.distribuionNetwork.teamServeId
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
            if(adapter.mData[1].itemMultiStyleItem.isEmpty())
                adapter.mData[1].necessary = false
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
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()-1]
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
        if(network.distribuionNetwork.issuerBelongSite!=null){
            val issuerBelongSite = network.distribuionNetwork.issuerBelongSite.split("、")
            for (j in issuerBelongSite){
                val position = adapter.mData[3].placeCheckArray.indexOf(j)
                if(position>=0)
                    adapter.mData[3].placeCheckBoolenArray[position] = true
            }
            adapter.mData[3].shiftInputContent = network.distribuionNetwork.issuerBelongSite
        }
        val voltages = network.voltages
        if(voltages!=null){
            val voltageDegrees = ArrayList<String>()
            for (j in voltages)
                voltageDegrees.addAll(j.voltageDegree.split("、"))
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
        validTime = network.distribuionNetwork.validTime
        if(network.distribuionNetwork.remark!=null)
            adapter.mData[11].textAreaContent = network.distribuionNetwork.remark
        if(validTime.toInt()<=0)
            adapter.urlPath = Constants.HttpUrlPath.Provider.DistribuionNetwork

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
        UnSerializeDataBase.inventoryIdKey = "teamServeId"
        UnSerializeDataBase.inventoryId = network.measureDesign.teamServeId
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
            if(adapter.mData[1].itemMultiStyleItem.isEmpty())
                adapter.mData[1].necessary = false
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
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()-1]
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
        if(network.measureDesign.issuerBelongSite!=null){
            val issuerBelongSite = network.measureDesign.issuerBelongSite.split("、")
            for (j in issuerBelongSite){
                val position = adapter.mData[3].placeCheckArray.indexOf(j)
                if(position>=0)
                    adapter.mData[3].placeCheckBoolenArray[position] = true
            }
            adapter.mData[3].shiftInputContent = network.measureDesign.issuerBelongSite
        }
        val voltages = network.voltages
        if(voltages!=null){
            val voltageDegrees = ArrayList<String>()
            for (j in voltages)
                voltageDegrees.addAll(j.voltageDegree.split("、"))
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
        validTime = network.measureDesign.validTime
        if(network.measureDesign.remark!=null)
            adapter.mData[11].textAreaContent = network.measureDesign.remark
        if(validTime.toInt()<=0)
            adapter.urlPath = Constants.HttpUrlPath.Provider.MeasureDesign

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
        UnSerializeDataBase.inventoryIdKey = "teamServeId"
        UnSerializeDataBase.inventoryId = caravan.caravanTransport.teamServeId
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
            if(adapter.mData[1].itemMultiStyleItem.isEmpty())
                adapter.mData[1].necessary = false
        }
        adapter.mData[2].inputUnitContent = caravan.caravanTransport.horseNumber
        if(caravan.caravanTransport.issuerBelongSite!=null){
            val issuerBelongSite = caravan.caravanTransport.issuerBelongSite.split("、")
            for (j in issuerBelongSite){
                val position = adapter.mData[5].placeCheckArray.indexOf(j)
                if(position>=0)
                    adapter.mData[5].placeCheckBoolenArray[position] = true
            }
            adapter.mData[5].shiftInputContent = caravan.caravanTransport.issuerBelongSite
        }
        adapter.mData[6].inputUnitContent = caravan.caravanTransport.validTime
        validTime = caravan.caravanTransport.validTime
        if(caravan.caravanTransport.remark!=null)
            adapter.mData[8].textAreaContent = caravan.caravanTransport.remark
        if(validTime.toInt()<=0)
            adapter.urlPath = Constants.HttpUrlPath.Provider.CaravanTransport

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
        UnSerializeDataBase.inventoryIdKey = "teamServeId"
        UnSerializeDataBase.inventoryId = pile.pileFoundation.teamServeId
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
            if(adapter.mData[1].itemMultiStyleItem.isEmpty())
                adapter.mData[1].necessary = false
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
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()-1]
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
        if(pile.pileFoundation.issuerBelongSite!=null){
            val issuerBelongSite = pile.pileFoundation.issuerBelongSite.split("、")
            for (j in issuerBelongSite){
                val position = adapter.mData[3].placeCheckArray.indexOf(j)
                if(position>=0)
                    adapter.mData[3].placeCheckBoolenArray[position] = true
            }
            adapter.mData[3].shiftInputContent = pile.pileFoundation.issuerBelongSite
        }
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
        adapter.mData[6].selectContent = pile.pileFoundation.location.replace(" / "," ")
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
        validTime = pile.pileFoundation.validTime
        if(pile.pileFoundation.remark!=null)
            adapter.mData[12].textAreaContent = pile.pileFoundation.remark
        if(validTime.toInt()<=0)
            adapter.urlPath = Constants.HttpUrlPath.Provider.PileFoundation

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
        UnSerializeDataBase.inventoryIdKey = "teamServeId"
        UnSerializeDataBase.inventoryId = supplyUnexcavation.teamServeId
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
            if(adapter.mData[1].itemMultiStyleItem.isEmpty())
                adapter.mData[1].necessary = false
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
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()-1]
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
        if(supplyUnexcavation.issuerBelongSite!=null){
            val issuerBelongSite = supplyUnexcavation.issuerBelongSite.split("、")
            for (j in issuerBelongSite){
                val position = adapter.mData[4].placeCheckArray.indexOf(j)
                if(position>=0)
                    adapter.mData[4].placeCheckBoolenArray[position] = true
            }
            adapter.mData[4].shiftInputContent = supplyUnexcavation.issuerBelongSite
        }
        adapter.mData[7].inputUnitContent = supplyUnexcavation.validTime
        validTime = supplyUnexcavation.validTime
        if(supplyUnexcavation.remark!=null)
            adapter.mData[9].textAreaContent = supplyUnexcavation.remark
        if(validTime.toInt()<=0)
            adapter.urlPath = Constants.HttpUrlPath.Provider.Unexcavation

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
        UnSerializeDataBase.inventoryIdKey = "teamServeId"
        UnSerializeDataBase.inventoryId = supplyTest.teamServeId
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
            if(adapter.mData[1].itemMultiStyleItem.isEmpty())
                adapter.mData[1].necessary = false
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
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()-1]
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
        if(supplyTest.issuerBelongSite!=null){
            val issuerBelongSite = supplyTest.issuerBelongSite.split("、")
            for (j in issuerBelongSite){
                val position = adapter.mData[3].placeCheckArray.indexOf(j)
                if(position>=0)
                    adapter.mData[3].placeCheckBoolenArray[position] = true
            }
            adapter.mData[3].shiftInputContent = supplyTest.issuerBelongSite
        }
        val voltages = supplyTest.voltages
        if(voltages!=null){
            val voltageDegrees = ArrayList<String>()
            for (j in voltages)
                voltageDegrees.addAll(j.voltageDegree.split("、"))
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
        validTime = supplyTest.validTime
        if(supplyTest.remark!=null)
            adapter.mData[12].textAreaContent = supplyTest.remark
        if(validTime.toInt()<=0)
            adapter.urlPath = Constants.HttpUrlPath.Provider.TestTeam

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
        UnSerializeDataBase.inventoryIdKey = "teamServeId"
        UnSerializeDataBase.inventoryId = supplySpanWoodenSupprt.teamServeId
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
            if(adapter.mData[1].itemMultiStyleItem.isEmpty())
                adapter.mData[1].necessary = false
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
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()-1]
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
        if(supplySpanWoodenSupprt.issuerBelongSite!=null){
            val issuerBelongSite = supplySpanWoodenSupprt.issuerBelongSite.split("、")
            for (j in issuerBelongSite){
                val position = adapter.mData[4].placeCheckArray.indexOf(j)
                if(position>=0)
                    adapter.mData[4].placeCheckBoolenArray[position] = true
            }
            adapter.mData[4].shiftInputContent = supplySpanWoodenSupprt.issuerBelongSite
        }
        adapter.mData[7].inputUnitContent = supplySpanWoodenSupprt.validTime
        validTime = supplySpanWoodenSupprt.validTime
        if(supplySpanWoodenSupprt.remark!=null)
            adapter.mData[9].textAreaContent = supplySpanWoodenSupprt.remark
        if(validTime.toInt()<=0)
            adapter.urlPath = Constants.HttpUrlPath.Provider.SpanWoodenSupprt

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
        UnSerializeDataBase.inventoryIdKey = "teamServeId"
        UnSerializeDataBase.inventoryId = supplyRunningMaintain.teamServeId
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
            if(adapter.mData[1].itemMultiStyleItem.isEmpty())
                adapter.mData[1].necessary = false
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
                    mData[2].selectContent = mData[2].selectOption1Items[j.construction.toInt()-1]
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
        if(supplyRunningMaintain.issuerBelongSite!=null){
            val issuerBelongSite = supplyRunningMaintain.issuerBelongSite.split("、")
            for (j in issuerBelongSite){
                val position = adapter.mData[3].placeCheckArray.indexOf(j)
                if(position>=0)
                    adapter.mData[3].placeCheckBoolenArray[position] = true
            }
            adapter.mData[3].shiftInputContent = supplyRunningMaintain.issuerBelongSite
        }
        val voltages = supplyRunningMaintain.voltages
        if(voltages!=null){
            val voltageDegrees = ArrayList<String>()
                for (j in voltages)
                    voltageDegrees.addAll(j.voltageDegree.split("、"))
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
        validTime = supplyRunningMaintain.validTime
        if(supplyRunningMaintain.remark!=null)
            adapter.mData[12].textAreaContent = supplyRunningMaintain.remark
        if(validTime.toInt()<=0)
            adapter.urlPath = Constants.HttpUrlPath.Provider.RunningMaintain

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
        if(supplyLeaseCar.carTable.maxPassengers!=null)
            adapter.mData[3].inputUnitContent = supplyLeaseCar.carTable.maxPassengers
        if(supplyLeaseCar.carTable.maxWeight!=null)
            adapter.mData[4].inputUnitContent = supplyLeaseCar.carTable.maxWeight
        if(supplyLeaseCar.carTable.lenghtCar!=null)
            adapter.mData[5].inputUnitContent = supplyLeaseCar.carTable.lenghtCar
        adapter.mData[6].selectContent = if(supplyLeaseCar.carTable.construction=="1") "箱式" else "敞篷"
        adapter.mData[7].radioButtonValue = if(supplyLeaseCar.carTable.isDriver=="true") "1" else "0"
        adapter.mData[8].radioButtonValue = if(supplyLeaseCar.carTable.isInsurance=="true") "1" else "0"
        if(supplyLeaseCar.carTable.carPhotoPath!=null &&  supplyLeaseCar.carTable.carPhotoPath!="")
            UnSerializeDataBase.imgList.add(BitmapMap(supplyLeaseCar.carTable.carPhotoPath, adapter.mData[9].key))
        adapter.mData[14].inputSingleContent = supplyLeaseCar.site
        if(supplyLeaseCar.money=="-1.0" || supplyLeaseCar.salaryUnit=="面议"){
            adapter.mData[11].singleDisplayRightContent= "面议"
        }else {
            adapter.mData[11].singleDisplayRightContent= "${supplyLeaseCar.money} ${supplyLeaseCar.salaryUnit}"
        }
        if(supplyLeaseCar.issuerBelongSite!=null){
            val issuerBelongSite = supplyLeaseCar.issuerBelongSite.split("、")
            for (j in issuerBelongSite){
                val position = adapter.mData[10].placeCheckArray.indexOf(j)
                if(position>=0)
                    adapter.mData[10].placeCheckBoolenArray[position] = true
            }
            adapter.mData[10].shiftInputContent = supplyLeaseCar.issuerBelongSite
        }
        adapter.mData[15].inputUnitContent = supplyLeaseCar.validTime
        validTime = supplyLeaseCar.validTime
        if(validTime.toInt()<=0)
            adapter.urlPath = Constants.HttpUrlPath.Provider.LeaseCar

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
        UnSerializeDataBase.inventoryIdKey = "leaseServeId"
        when(adapter.mData[0].singleDisplayRightContent){
            "工器具租赁"-> {
                UnSerializeDataBase.inventoryId = otherLease.leaseConstructionTool.leaseServeId
                companyCredentialId = otherLease.leaseConstructionTool.companyCredentialId
                adapter.mData[10].radioButtonValue = if(otherLease.leaseConstructionTool.isDistribution=="true") "1" else "0"
                adapter.mData[11].selectContent = otherLease.leaseConstructionTool.conveyancePropertyInsurance
                adapter.mData[15].inputUnitContent = otherLease.leaseConstructionTool.validTime
                validTime = otherLease.leaseConstructionTool.validTime
                if(validTime.toInt()<=0)
                    adapter.urlPath = Constants.HttpUrlPath.Provider.LcTool

                if(otherLease.leaseConstructionTool.issuerBelongSite!=null){
                    val issuerBelongSite = otherLease.leaseConstructionTool.issuerBelongSite.split("、")
                    for (j in issuerBelongSite){
                        val position = adapter.mData[14].placeCheckArray.indexOf(j)
                        if(position>=0)
                            adapter.mData[14].placeCheckBoolenArray[position] = true
                    }
                    adapter.mData[14].shiftInputContent = otherLease.leaseConstructionTool.issuerBelongSite
                }
            }
            "机械租赁"-> {
                UnSerializeDataBase.inventoryId = otherLease.leaseMachinery.leaseServeId
                companyCredentialId = otherLease.leaseMachinery.companyCredentialId
                adapter.mData[10].radioButtonValue = if(otherLease.leaseMachinery.isDistribution=="true") "1" else "0"
                adapter.mData[11].selectContent = otherLease.leaseMachinery.conveyancePropertyInsurance
                adapter.mData[15].inputUnitContent = otherLease.leaseMachinery.validTime
                validTime = otherLease.leaseMachinery.validTime
                if(validTime.toInt()<=0)
                    adapter.urlPath = Constants.HttpUrlPath.Provider.LeaseMachinery

                if(otherLease.leaseMachinery.issuerBelongSite!=null){
                    val issuerBelongSite = otherLease.leaseMachinery.issuerBelongSite.split("、")
                    for (j in issuerBelongSite){
                        val position = adapter.mData[14].placeCheckArray.indexOf(j)
                        if(position>=0)
                            adapter.mData[14].placeCheckBoolenArray[position] = true
                    }
                    adapter.mData[14].shiftInputContent = otherLease.leaseMachinery.issuerBelongSite
                }
            }
            "设备租赁"-> {
                UnSerializeDataBase.inventoryId = otherLease.leaseFacility.leaseServeId
                companyCredentialId = otherLease.leaseFacility.companyCredentialId
                adapter.mData[10].radioButtonValue = if(otherLease.leaseFacility.isDistribution=="true") "1" else "0"
                adapter.mData[11].selectContent = otherLease.leaseFacility.conveyancePropertyInsurance
                adapter.mData[15].inputUnitContent = otherLease.leaseFacility.validTime
                validTime = otherLease.leaseFacility.validTime
                if(validTime.toInt()<=0)
                    adapter.urlPath = Constants.HttpUrlPath.Provider.LeaseFacility

                if(otherLease.leaseFacility.issuerBelongSite!=null){
                    val issuerBelongSite = otherLease.leaseFacility.issuerBelongSite.split("、")
                    for (j in issuerBelongSite){
                        val position = adapter.mData[14].placeCheckArray.indexOf(j)
                        if(position>=0)
                            adapter.mData[14].placeCheckBoolenArray[position] = true
                    }
                    adapter.mData[14].shiftInputContent = otherLease.leaseFacility.issuerBelongSite
                }
            }
        }

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
        if(supplyThirdParty.companyCredential.businessLicensePath!=null && supplyThirdParty.companyCredential.businessLicensePath!=""){
            UnSerializeDataBase.imgList.add(BitmapMap(supplyThirdParty.companyCredential.businessLicensePath, adapter.mData[6].key))
        }
        //9
        if(supplyThirdParty.issuerBelongSite!=null){
            val issuerBelongSite = supplyThirdParty.issuerBelongSite.split("、")
            for (j in issuerBelongSite){
                val position = adapter.mData[10].placeCheckArray.indexOf(j)
                if(position>=0)
                    adapter.mData[10].placeCheckBoolenArray[position] = true
            }
            adapter.mData[10].shiftInputContent = supplyThirdParty.issuerBelongSite
        }
        adapter.mData[11].inputUnitContent = supplyThirdParty.validTime
        validTime = supplyThirdParty.validTime
        if(validTime.toInt()<=0)
            adapter.urlPath = Constants.HttpUrlPath.Provider.ThirdServices
        if(supplyThirdParty.businessScope!=null)
        adapter.mData[13].textAreaContent = supplyThirdParty.businessScope
    }


    fun update(itemMultiStyleItem:List<MultiStyleItem>){
        Log.i("position is",mAdapter!!.mData[0].selected.toString())
        mAdapter!!.mData[mAdapter!!.mData[0].selected].itemMultiStyleItem = itemMultiStyleItem
        Log.i("item size",mAdapter!!.mData[mAdapter!!.mData[0].selected].itemMultiStyleItem.size.toString())
    }
}