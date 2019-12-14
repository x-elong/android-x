package com.example.eletronicengineer.fragment.my

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
import com.example.eletronicengineer.activity.DemandActivity
import com.example.eletronicengineer.activity.MyReleaseActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.distributionFileSave.*
import com.example.eletronicengineer.fragment.sdf.ProjectListFragment
import com.example.eletronicengineer.fragment.sdf.PublishInventoryFragment
import com.example.eletronicengineer.fragment.sdf.PublishInventoryItemMoreFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.putSimpleMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_modify_job_information.view.*
import org.json.JSONObject
import java.io.Serializable

class ModifyJobInformationFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):ModifyJobInformationFragment{
            val modifyJobInformationFragment = ModifyJobInformationFragment()
            modifyJobInformationFragment.arguments = args
            return modifyJobInformationFragment
        }
    }
    var type = 0
    lateinit var mView:View
    var  title:String=""
    lateinit var id:String
    lateinit var mData: List<MultiStyleItem>
    lateinit var requirmentTeamServeId:String
    lateinit var multiButtonListeners: MutableList<View.OnClickListener>
    var mAdapter: RecyclerviewAdapter?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_modify_job_information,container,false)
        type = arguments!!.getInt("type")
        id = arguments!!.getString("id")
        initFragment()
        return mView
    }
    private fun initFragment() {
        mView.tv_modify_job_information_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        multiButtonListeners = ArrayList()
        if(mAdapter==null){
            val result = Observable.create<RecyclerviewAdapter>{
                it.onNext(switchAdapter(type))
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mAdapter=it
                    mView.rv_job_information_content.adapter = mAdapter
                    mView.rv_job_information_content.layoutManager = LinearLayoutManager(mView.context)
                }

        }
        else{
            mView.rv_job_information_content.adapter = mAdapter
            mView.rv_job_information_content.layoutManager = LinearLayoutManager(mView.context)
        }
        //发布
        mView.btn_modify_job_information.setOnClickListener{

            val networkAdapter= NetworkAdapter(mAdapter!!.mData, mView.context)
            if(networkAdapter.check()){
                if (UnSerializeDataBase.fileList.size!=0||(UnSerializeDataBase.imgList.size!=0))
                    networkAdapter.generateMultiPartRequestBody(UnSerializeDataBase.dmsBasePath+mAdapter!!.urlPath)
                else
                {
                    val loadingDialog = LoadingDialog(mView.context, "正在修改中...", R.mipmap.ic_dialog_loading)
                    loadingDialog.show()
                    var json= JSONObject()
                    json.put("id",id)
                    json.put("requirementType", title)
                    json.put(UnSerializeDataBase.inventoryIdKey,UnSerializeDataBase.inventoryId)
                    networkAdapter.generateJsonRequestBody(json).subscribe {
                        val result = putSimpleMessage(it,UnSerializeDataBase.dmsBasePath+mAdapter!!.urlPath)
                            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                            .subscribe({
                                loadingDialog.dismiss()
                                val jsonObject = JSONObject(it.string())
                                if(jsonObject.getInt("code")==200){
                                    ToastHelper.mToast(mView.context,"修改成功")
                                    activity!!.supportFragmentManager.popBackStackImmediate()
                                }
                                else{
                                    ToastHelper.mToast(mView.context,"修改失败")
                                }

                            },{
                                loadingDialog.dismiss()
                                ToastHelper.mToast(mView.context,"修改信息异常")
                                it.printStackTrace()
                            })
                    }
                }
            }
        }
    }
    //选择加载的界面
    fun switchAdapter(Type: Int): RecyclerviewAdapter {
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = mView.context
        adapterGenerate.activity = activity as MyReleaseActivity
        lateinit var adapter: RecyclerviewAdapter
        when (Type) {
            Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                val singleDisplayRightContent = "普工"
                val selectOption1Items: List<String> = listOf("普工")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                initDemandIndividual(adapter)

            }
            Constants.FragmentType.PERSONAL_SPECIAL_WORK_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                val singleDisplayRightContent = "特种作业"
                val selectOption1Items: List<String> =
                    listOf("低压电工作业", "高压电工作业", "电力电缆作业", "继电保护作业", "电气试验作业", "融化焊接与热切割作业", "登高架设作业")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                initDemandIndividual(adapter)

            }
            Constants.FragmentType.PERSONAL_PROFESSIONAL_OPERATION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                val singleDisplayRightContent = "专业操作"
                val selectOption1Items: List<String> =
                    listOf("压接操作", "机动绞磨操作", "牵张设备操作", "起重机械操作", "钢筋工", "混凝土工", "木工", "模板工", "油漆工", "砌筑工", "通风工", "打桩工", "架子工工")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                initDemandIndividual(adapter)
            }
            Constants.FragmentType.PERSONAL_SURVEYOR_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                val singleDisplayRightContent = "测量工"
                val selectOption1Items: List<String> = listOf("测量工")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                initDemandIndividual(adapter)

            }
            Constants.FragmentType.PERSONAL_DRIVER_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                val singleDisplayRightContent = "驾驶员"
                val selectOption1Items: List<String> =
                    listOf("驾驶证A1", "驾驶证A2", "驾驶证A3", "驾驶证B1", "驾驶证B2", "驾驶证C1", "驾驶证C2", "驾驶证C3", "驾驶证D", "驾驶证E")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                initDemandIndividual(adapter)
            }
            Constants.FragmentType.PERSONAL_NINE_MEMBERS_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                val singleDisplayRightContent = "九大员"
                val selectOption1Items: List<String> =
                    listOf("施工员", "安全员", "质量员", "材料员", "资料员", "预算员", "标准员", "机械员", "劳务员")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                initDemandIndividual(adapter)
            }
            Constants.FragmentType.PERSONAL_REGISTRATION_CLASS_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                val singleDisplayRightContent = "注册类"
                val selectOption1Items: List<String> = listOf("造价工程师", "一级建造师", "安全工程师", "电气工程师")
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.mData[1].selectOption1Items = selectOption1Items
                initDemandIndividual(adapter)
            }
            Constants.FragmentType.PERSONAL_OTHER_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                initDemandIndividual(adapter)
                adapter.mData[1].options= MultiStyleItem.Options.SINGLE_INPUT
                adapter.mData[1].inputSingleTitle=adapter.mData[1].selectTitle
                adapter.mData[1].inputSingleContent = adapter.mData[1].selectContent
            }
            Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupSubstationConstruction()
                val singleDisplayRightContent = "变电施工队"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementPowerTransformation
                initDemandGroupPowerTransformation(adapter,arguments!!.getSerializable("data") as RequirementPowerTransformation)
            }
            Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupSubstationConstruction()
                val singleDisplayRightContent = "主网施工队"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementMajorNetwork
                initDemandGroupMajorNetwork(adapter,arguments!!.getSerializable("data") as RequirementMajorNetWork)
            }
            Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupSubstationConstruction()
                val singleDisplayRightContent = "配网施工队"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementDistribuionNetwork
                initDemandGroupDistribuionNetwork(adapter,arguments!!.getSerializable("data") as RequirementDistributionNetwork)
            }
            Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal -> {//测量设计
                adapter = adapterGenerate.DemandGroupMeasurementDesign()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementMeasureDesign
                initDemandGroupMeasurementDesign(adapter,arguments!!.getSerializable("data") as RequirementMeasureDesign)
            }
            Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal -> {//马帮运输
                adapter = adapterGenerate.DemandGroupCaravanTransportation()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementCaravanTransport
                initDemandGroupCaravanTransportation(adapter,arguments!!.getSerializable("data") as RequirementCaravanTransport)
            }
            Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal -> {//桩基服务
                adapter = adapterGenerate.DemandGroupPileFoundationConstruction()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementPileFoundation
                initDemandGroupPileFoundationConstruction(adapter,arguments!!.getSerializable("data") as RequirementPileFoundation)
            }
            Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal -> {//非开挖
                adapter = adapterGenerate.DemandGroupNonExcavation()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementUnexcavation
                initDemandGroupNonExcavation(adapter,arguments!!.getSerializable("data") as RequirementUnexcavation)
            }
            Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal -> {//试验调试
                adapter = adapterGenerate.DemandGroupTestDebugging()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementTest
                initDemandGroupTestDebugging(adapter,arguments!!.getSerializable("data") as RequirementTest)
            }
            Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal -> {//跨越架
                adapter = adapterGenerate.DemandGroupCrossingFrame()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementSpanWoodenSupprt
                initDemandGroupCrossingFrame(adapter,arguments!!.getSerializable("data") as RequirementSpanWoodenSupprt)
            }
            Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal -> {//运行维护
                adapter = adapterGenerate.DemandGroupOperationAndMaintenance()
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementRunningMaintain
                initDemandGroupOperationAndMaintenance(adapter,arguments!!.getSerializable("data") as RequirementRunningMaintain)
            }
            Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal -> {//车辆租赁
                adapter = adapterGenerate.DemandLeaseVehicleLeasing()
                adapter.mData[0].singleDisplayRightContent="车辆租赁"
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementLeaseCar
                initDemandLeaseVehicleLeasing(adapter,arguments!!.getSerializable("data") as RequirementLeaseCar)
            }
            Constants.FragmentType.TOOL_LEASING_TYPE.ordinal -> {//工器具租赁
                adapter = adapterGenerate.DemandEquipmentLeasing()
                adapter.mData[0].singleDisplayRightContent="工器具租赁"
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementLeaseConstructionTool
                initDemandEquipmentLeasing(adapter,arguments!!.getSerializable("data") as RequirementLeaseConstructionTool)
            }
            Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal -> {
                val singleDisplayRightContent = "机械租赁"
                adapter = adapterGenerate.DemandEquipmentLeasing()
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementLeaseMachinery
                initDemandMachineryLeasing(adapter,arguments!!.getSerializable("data") as RequirementLeaseMachinery)
            }
            Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal -> {
                val singleDisplayRightContent = "设备租赁"
                adapter = adapterGenerate.DemandEquipmentLeasing()
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementLeaseFacility
                initDemandEquipmentLeasing(adapter,arguments!!.getSerializable("data") as RequirementLeaseFacility)
            }
            Constants.FragmentType.TRIPARTITE_TRAINING_CERTIFICATE_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                val singleDisplayRightContent = "培训办证"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                initDemandTripartite(adapter)
            }
            Constants.FragmentType.TRIPARTITE_FINANCIAL_ACCOUNTING_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                val singleDisplayRightContent = "财务记账"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                initDemandTripartite(adapter)
            }
            Constants.FragmentType.TRIPARTITE_AGENCY_QUALIFICATION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                val singleDisplayRightContent = "代办资格"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                initDemandTripartite(adapter)
            }
            Constants.FragmentType.TRIPARTITE_TENDER_SERVICE_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                val singleDisplayRightContent = "标书服务"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                initDemandTripartite(adapter)
            }
            Constants.FragmentType.TRIPARTITE_LEGAL_ADVICE_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                val singleDisplayRightContent = "法律咨询"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                initDemandTripartite(adapter)
            }
            Constants.FragmentType.TRIPARTITE_SOFTWARE_SERVICE_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                val singleDisplayRightContent = "软件服务"
                adapter.mData[0].singleDisplayRightContent = singleDisplayRightContent
                initDemandTripartite(adapter)
            }
            Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                initDemandTripartite(adapter)
                adapter.mData[0].options= MultiStyleItem.Options.SINGLE_INPUT
                adapter.mData[0].inputSingleTitle=adapter.mData[0].singleDisplayRightTitle
                adapter.mData[0].inputSingleContent = adapter.mData[0].singleDisplayRightContent
            }
        }
        return adapter
    }


    /**
     * @需求个人
     */
    private fun initDemandIndividual(adapter: RecyclerviewAdapter) {
        adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementPerson
        val requirementPersonDetail = arguments!!.getSerializable("data") as RequirementPersonDetail
        title = requirementPersonDetail.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementPersonDetail.requirementType
        }
        adapter.mData[0].singleDisplayRightContent = requirementPersonDetail.requirementVariety
        adapter.mData[1].selectContent = requirementPersonDetail.requirementMajor
        adapter.mData[2].inputSingleContent = requirementPersonDetail.projectName
        adapter.mData[3].selectContent = requirementPersonDetail.projectSite.replace(" / "," ")
        adapter.mData[4].inputUnitContent = requirementPersonDetail.planTime
        adapter.mData[5].inputUnitContent = requirementPersonDetail.workerExperience
        adapter.mData[6].inputRangeValue1 = requirementPersonDetail.minAgeDemand
        adapter.mData[6].inputRangeValue2 = requirementPersonDetail.maxAgeDemand
        adapter.mData[7].radioButtonValue = requirementPersonDetail.sexDemand
        adapter.mData[8].radioButtonValue = requirementPersonDetail.roomBoardStandard
        adapter.mData[9].selectContent = requirementPersonDetail.journeySalary
        adapter.mData[10].selectContent = requirementPersonDetail.journeyCarFare
        adapter.mData[11].inputUnitContent = requirementPersonDetail.needPeopleNumber
        if(requirementPersonDetail.salaryStandard!="-1.0"){
            adapter.mData[12].inputMultiContent = requirementPersonDetail.salaryStandard
            adapter.mData[12].inputMultiSelectUnit = requirementPersonDetail.salaryUnit
        }else{
            adapter.mData[12].inputMultiSelectUnit = "面议"
        }
        adapter.mData[13].singleDisplayRightContent = requirementPersonDetail.name
        adapter.mData[14].singleDisplayRightContent = requirementPersonDetail.phone
        adapter.mData[15].inputUnitContent = requirementPersonDetail.validTime
        adapter.mData[17].textAreaContent = requirementPersonDetail.additonalExplain
    }

    /**
     * @需求团队变电
     */
    private fun initDemandGroupPowerTransformation(adapter: RecyclerviewAdapter, requirementPowerTransformation: RequirementPowerTransformation) {
        title = requirementPowerTransformation.requirementType
        activity?.runOnUiThread {
            mView.tv_modify_job_information_title.text = requirementPowerTransformation.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
        UnSerializeDataBase.inventoryId = requirementPowerTransformation.requirmentTeamServeId
        adapter.mData[0].singleDisplayRightContent = requirementPowerTransformation.requirementVariety
        adapter.mData[1].inputSingleContent = requirementPowerTransformation.projectName
        adapter.mData[2].selectContent = requirementPowerTransformation.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementPowerTransformation.projectTime
        val itemMultiStyleItem = ArrayList<MultiStyleItem>()
        val requirementCarLists = requirementPowerTransformation.requirementCarLists
        if(requirementCarLists!=null)
            for(j in requirementCarLists){
                itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","车辆清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.carType
                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needCarNumber
                mData[2].selectContent = if(j.construction.toInt()==1) "箱式" else "敞篷"
                mData[3].selectContent = if(j.isInsurance.toInt()==1) "有" else "无"
                mData[4].selectContent = if(j.isDriver.toInt()==1) "是" else "否"
                mData[5].inputUnitContent = j.haveDriver
                mData[6].inputUnitContent = j.noDriver
                if(j.remark!=null)
                mData[8].textAreaContent = j.remark
                itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","车辆清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[4].itemMultiStyleItem = itemMultiStyleItem
        adapter.mData[5].checkboxValueList = ArrayList(adapter.mData[5].checkboxNameList.size)
        val requirementTeamVoltageClasses = requirementPowerTransformation.requirementTeamVoltageClasses.split("、")
        for (j in adapter.mData[5].checkboxNameList){
            val i = adapter.mData[5].checkboxNameList.indexOf(j)
            val position = requirementTeamVoltageClasses.indexOf(j)
            if(position>=0)
                adapter.mData[5].checkboxValueList.add(true)
            else
                adapter.mData[5].checkboxValueList.add(false)
        }

        adapter.mData[6].checkboxValueList = ArrayList(adapter.mData[6].checkboxNameList.size)
        val requirementConstructionWorkKind = requirementPowerTransformation.requirementConstructionWorkKind.split("、")
        for (j in adapter.mData[6].checkboxNameList){
            val i = adapter.mData[6].checkboxNameList.indexOf(j)
            val position = requirementConstructionWorkKind.indexOf(j)
            if(position>=0)
                adapter.mData[6].checkboxValueList.add(true)
            else
                adapter.mData[6].checkboxValueList.add(false)
        }
        adapter.mData[7].inputUnitContent = requirementPowerTransformation.workerExperience

        adapter.mData[8].inputRangeValue1 = requirementPowerTransformation.minAgeDemand
        adapter.mData[8].inputRangeValue2 =  requirementPowerTransformation.maxAgeDemand
        adapter.mData[9].radioButtonValue = requirementPowerTransformation.sexDemand
        adapter.mData[10].radioButtonValue = requirementPowerTransformation.roomBoardStandard
        adapter.mData[11].selectContent = requirementPowerTransformation.journeyCarFare
        adapter.mData[12].selectContent = requirementPowerTransformation.journeySalary

        val itemMultiStyleItems = ArrayList<MultiStyleItem>()
        val requirementMembersLists = requirementPowerTransformation.requirementMembersLists
        if(requirementMembersLists!=null)
            for(j in requirementMembersLists){
                itemMultiStyleItems.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.positionType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","成员清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.positionType
                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needPeopleNumber
                mData[2].inputMultiSelectUnit = j.salaryStandard.substring(j.salaryStandard.indexOf("元"))
                mData[2].inputMultiContent = j.salaryStandard.replace(mData[2].inputMultiSelectUnit,"")
                mData[3].inputSingleContent = j.personCertificate
                mData[4].selectContent = if(j.haveCertificate.toInt()==1) "是" else "否"
                mData[6].textAreaContent = j.remark
                itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem = mData
                itemMultiStyleItems[itemMultiStyleItems.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItems[0].selected = itemMultiStyleItems.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","成员清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[13].itemMultiStyleItem = itemMultiStyleItems

        adapter.mData[14].singleDisplayRightContent = requirementPowerTransformation.needPeopleNumber
        adapter.mData[15].radioButtonValue = requirementPowerTransformation.constructionEquipment
        adapter.mData[16].singleDisplayRightContent = requirementPowerTransformation.name
        adapter.mData[17].singleDisplayRightContent = requirementPowerTransformation.phone
        adapter.mData[18].inputUnitContent = requirementPowerTransformation.validTime
        adapter.mData[20].textAreaContent = requirementPowerTransformation.additonalExplain
    }
    /**
     * @需求团队主网
     */
    private fun initDemandGroupMajorNetwork(adapter: RecyclerviewAdapter,requirementMajorNetWork: RequirementMajorNetWork) {
        title = requirementMajorNetWork.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementMajorNetWork.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
        UnSerializeDataBase.inventoryId = requirementMajorNetWork.requirmentTeamServeId
        adapter.mData[0].singleDisplayRightContent = requirementMajorNetWork.requirementVariety
        adapter.mData[1].inputSingleContent = requirementMajorNetWork.projectName
        adapter.mData[2].selectContent = requirementMajorNetWork.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementMajorNetWork.projectTime
        val itemMultiStyleItem = ArrayList<MultiStyleItem>()
        val requirementCarLists = requirementMajorNetWork.requirementCarLists
        if(requirementCarLists!=null)
            for(j in requirementCarLists){
                itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","车辆清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.carType
                                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needCarNumber
                mData[2].selectContent = if(j.construction.toInt()==1) "箱式" else "敞篷"
                mData[3].selectContent = if(j.isInsurance.toInt()==1) "有" else "无"
                mData[4].selectContent = if(j.isDriver.toInt()==1) "是" else "否"
                mData[5].inputUnitContent = j.haveDriver
                mData[6].inputUnitContent = j.noDriver
                if(j.remark!=null)
                mData[8].textAreaContent = j.remark
                itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","车辆清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[4].itemMultiStyleItem = itemMultiStyleItem
        adapter.mData[5].checkboxValueList = ArrayList(adapter.mData[5].checkboxNameList.size)
        val requirementTeamVoltageClasses = requirementMajorNetWork.requirementTeamVoltageClasses.split("、")
        for (j in adapter.mData[5].checkboxNameList){
            val i = adapter.mData[5].checkboxNameList.indexOf(j)
            val position = requirementTeamVoltageClasses.indexOf(j)
            if(position>=0)
                adapter.mData[5].checkboxValueList.add(true)
            else
                adapter.mData[5].checkboxValueList.add(false)
        }

        adapter.mData[6].checkboxValueList = ArrayList(adapter.mData[6].checkboxNameList.size)
        val requirementConstructionWorkKind = requirementMajorNetWork.requirementConstructionWorkKind.split("、")
        for (j in adapter.mData[6].checkboxNameList){
            val i = adapter.mData[6].checkboxNameList.indexOf(j)
            val position = requirementConstructionWorkKind.indexOf(j)
            if(position>=0)
                adapter.mData[6].checkboxValueList.add(true)
            else
                adapter.mData[6].checkboxValueList.add(false)
        }
        adapter.mData[7].inputUnitContent = requirementMajorNetWork.workerExperience

        adapter.mData[8].inputRangeValue1 = requirementMajorNetWork.minAgeDemand
        adapter.mData[8].inputRangeValue2 =  requirementMajorNetWork.maxAgeDemand
        adapter.mData[9].radioButtonValue = requirementMajorNetWork.sexDemand
        adapter.mData[10].radioButtonValue = requirementMajorNetWork.roomBoardStandard
        adapter.mData[11].selectContent = requirementMajorNetWork.journeyCarFare
        adapter.mData[12].selectContent = requirementMajorNetWork.journeySalary

        val itemMultiStyleItems = ArrayList<MultiStyleItem>()
        val requirementMembersLists = requirementMajorNetWork.requirementMembersLists
        if(requirementMembersLists!=null)
            for(j in requirementMembersLists){
                itemMultiStyleItems.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.positionType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","成员清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.positionType
                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needPeopleNumber
                mData[2].inputMultiSelectUnit = j.salaryStandard.substring(j.salaryStandard.indexOf("元"))
                mData[2].inputMultiContent = j.salaryStandard.replace(mData[2].inputMultiSelectUnit,"")
                mData[3].inputSingleContent = j.personCertificate
                mData[4].selectContent = if(j.haveCertificate.toInt()==1) "是" else "否"
                mData[6].textAreaContent = j.remark
                itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem = mData
                itemMultiStyleItems[itemMultiStyleItems.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItems[0].selected = itemMultiStyleItems.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","成员清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[13].itemMultiStyleItem = itemMultiStyleItems

        adapter.mData[14].singleDisplayRightContent = requirementMajorNetWork.needPeopleNumber
        adapter.mData[15].radioButtonValue = requirementMajorNetWork.constructionEquipment
        adapter.mData[16].singleDisplayRightContent = requirementMajorNetWork.name
        adapter.mData[17].singleDisplayRightContent = requirementMajorNetWork.phone
        adapter.mData[18].inputUnitContent = requirementMajorNetWork.validTime
        adapter.mData[20].textAreaContent = requirementMajorNetWork.additonalExplain
    }

    /**
     * @需求团队配网
     */
    private fun initDemandGroupDistribuionNetwork(adapter: RecyclerviewAdapter, requirementDistributionNetwork: RequirementDistributionNetwork) {
        title = requirementDistributionNetwork.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementDistributionNetwork.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity

        UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
        UnSerializeDataBase.inventoryId = requirementDistributionNetwork.requirmentTeamServeId
        adapter.mData[0].singleDisplayRightContent = requirementDistributionNetwork.requirementVariety
        adapter.mData[1].inputSingleContent = requirementDistributionNetwork.projectName
        adapter.mData[2].selectContent = requirementDistributionNetwork.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementDistributionNetwork.projectTime
        val itemMultiStyleItem = ArrayList<MultiStyleItem>()
        val requirementCarLists = requirementDistributionNetwork.requirementCarLists
        if(requirementCarLists!=null)
            for(j in requirementCarLists){
                itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","车辆清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.carType
                                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needCarNumber
                mData[2].selectContent = if(j.construction.toInt()==1) "箱式" else "敞篷"
                mData[3].selectContent = if(j.isInsurance.toInt()==1) "有" else "无"
                mData[4].selectContent = if(j.isDriver.toInt()==1) "是" else "否"
                mData[5].inputUnitContent = j.haveDriver
                mData[6].inputUnitContent = j.noDriver
                if(j.remark!=null)
                mData[8].textAreaContent = j.remark
                itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","车辆清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[4].itemMultiStyleItem = itemMultiStyleItem
        adapter.mData[5].checkboxValueList = ArrayList(adapter.mData[5].checkboxNameList.size)
        val requirementTeamVoltageClasses = requirementDistributionNetwork.requirementTeamVoltageClasses.split("、")
        for (j in adapter.mData[5].checkboxNameList){
            val i = adapter.mData[5].checkboxNameList.indexOf(j)
            val position = requirementTeamVoltageClasses.indexOf(j)
            if(position>=0)
                adapter.mData[5].checkboxValueList.add(true)
            else
                adapter.mData[5].checkboxValueList.add(false)
        }

        adapter.mData[6].checkboxValueList = ArrayList(adapter.mData[6].checkboxNameList.size)
        val requirementConstructionWorkKind = requirementDistributionNetwork.requirementConstructionWorkKind.split("、")
        for (j in adapter.mData[6].checkboxNameList){
            val i = adapter.mData[6].checkboxNameList.indexOf(j)
            val position = requirementConstructionWorkKind.indexOf(j)
            if(position>=0)
                adapter.mData[6].checkboxValueList.add(true)
            else
                adapter.mData[6].checkboxValueList.add(false)
        }
        adapter.mData[7].inputUnitContent = requirementDistributionNetwork.workerExperience

        adapter.mData[8].inputRangeValue1 = requirementDistributionNetwork.minAgeDemand
        adapter.mData[8].inputRangeValue2 =  requirementDistributionNetwork.maxAgeDemand
        adapter.mData[9].radioButtonValue = requirementDistributionNetwork.sexDemand
        adapter.mData[10].radioButtonValue = requirementDistributionNetwork.roomBoardStandard
        adapter.mData[11].selectContent = requirementDistributionNetwork.journeyCarFare
        adapter.mData[12].selectContent = requirementDistributionNetwork.journeySalary

        val itemMultiStyleItems = ArrayList<MultiStyleItem>()
        val requirementMembersLists = requirementDistributionNetwork.requirementMembersLists
        if(requirementMembersLists!=null)
            for(j in requirementMembersLists){
                itemMultiStyleItems.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.positionType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","成员清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.positionType
                                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needPeopleNumber
                mData[2].inputMultiSelectUnit = j.salaryStandard.substring(j.salaryStandard.indexOf("元"))
                mData[2].inputMultiContent = j.salaryStandard.replace(mData[2].inputMultiSelectUnit,"")
                mData[3].inputSingleContent = j.personCertificate
                mData[4].selectContent = if(j.haveCertificate.toInt()==1) "是" else "否"
                mData[6].textAreaContent = j.remark
                itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem = mData
                itemMultiStyleItems[itemMultiStyleItems.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItems[0].selected = itemMultiStyleItems.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","成员清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[13].itemMultiStyleItem = itemMultiStyleItems

        adapter.mData[14].singleDisplayRightContent = requirementDistributionNetwork.needPeopleNumber
        adapter.mData[15].radioButtonValue = requirementDistributionNetwork.constructionEquipment
        adapter.mData[16].singleDisplayRightContent = requirementDistributionNetwork.name
        adapter.mData[17].singleDisplayRightContent = requirementDistributionNetwork.phone
        adapter.mData[18].inputUnitContent = requirementDistributionNetwork.validTime
        adapter.mData[20].textAreaContent = requirementDistributionNetwork.additonalExplain
    }

    /**
     * @测量设计
     */
    private fun initDemandGroupMeasurementDesign(adapter: RecyclerviewAdapter, requirementMeasureDesign: RequirementMeasureDesign) {
        title = requirementMeasureDesign.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementMeasureDesign.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
        UnSerializeDataBase.inventoryId = requirementMeasureDesign.requirmentTeamServeId
        adapter.mData[0].singleDisplayRightContent = requirementMeasureDesign.requirementVariety
        adapter.mData[1].inputSingleContent = requirementMeasureDesign.projectName
        adapter.mData[2].selectContent = requirementMeasureDesign.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementMeasureDesign.projectTime
        val itemMultiStyleItem = ArrayList<MultiStyleItem>()
        val requirementCarLists = requirementMeasureDesign.requirementCarLists
        if(requirementCarLists!=null)
            for(j in requirementCarLists){
                itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","车辆清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.carType
                                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needCarNumber
                mData[2].selectContent = if(j.construction.toInt()==1) "箱式" else "敞篷"
                mData[3].selectContent = if(j.isInsurance.toInt()==1) "有" else "无"
                mData[4].selectContent = if(j.isDriver.toInt()==1) "是" else "否"
                mData[5].inputUnitContent = j.haveDriver
                mData[6].inputUnitContent = j.noDriver
                if(j.remark!=null)
                mData[8].textAreaContent = j.remark
                itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","车辆清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[4].itemMultiStyleItem = itemMultiStyleItem
        adapter.mData[5].checkboxValueList = ArrayList(adapter.mData[5].checkboxNameList.size)
        val requirementTeamVoltageClasses = requirementMeasureDesign.requirementTeamVoltageClasses.split("、")
        for (j in adapter.mData[5].checkboxNameList){
            val i = adapter.mData[5].checkboxNameList.indexOf(j)
            val position = requirementTeamVoltageClasses.indexOf(j)
            if(position>=0)
                adapter.mData[5].checkboxValueList.add(true)
            else
                adapter.mData[5].checkboxValueList.add(false)
        }

        adapter.mData[6].checkboxValueList = ArrayList(adapter.mData[6].checkboxNameList.size)
        val requirementConstructionWorkKind = requirementMeasureDesign.requirementConstructionWorkKind.split("、")
        for (j in adapter.mData[6].checkboxNameList){
            val i = adapter.mData[6].checkboxNameList.indexOf(j)
            val position = requirementConstructionWorkKind.indexOf(j)
            if(position>=0)
                adapter.mData[6].checkboxValueList.add(true)
            else
                adapter.mData[6].checkboxValueList.add(false)
        }
        adapter.mData[7].inputUnitContent = requirementMeasureDesign.workerExperience

        adapter.mData[8].inputRangeValue1 = requirementMeasureDesign.minAgeDemand
        adapter.mData[8].inputRangeValue2 =  requirementMeasureDesign.maxAgeDemand
        adapter.mData[9].radioButtonValue = requirementMeasureDesign.sexDemand
        adapter.mData[10].radioButtonValue = requirementMeasureDesign.roomBoardStandard
        adapter.mData[11].selectContent = requirementMeasureDesign.journeyCarFare
        adapter.mData[12].selectContent = requirementMeasureDesign.journeySalary

        val itemMultiStyleItems = ArrayList<MultiStyleItem>()
        val requirementMembersLists = requirementMeasureDesign.requirementMembersLists
        if(requirementMembersLists!=null)
            for(j in requirementMembersLists){
                itemMultiStyleItems.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.positionType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","成员清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.positionType
                                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needPeopleNumber
                mData[2].inputMultiSelectUnit = j.salaryStandard.substring(j.salaryStandard.indexOf("元"))
                mData[2].inputMultiContent = j.salaryStandard.replace(mData[2].inputMultiSelectUnit,"")
                mData[3].inputSingleContent = j.personCertificate
                mData[4].selectContent = if(j.haveCertificate.toInt()==1) "是" else "否"
                mData[6].textAreaContent = j.remark
                itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem = mData
                itemMultiStyleItems[itemMultiStyleItems.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItems[0].selected = itemMultiStyleItems.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","成员清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[13].itemMultiStyleItem = itemMultiStyleItems

        adapter.mData[14].singleDisplayRightContent = requirementMeasureDesign.needPeopleNumber
        adapter.mData[15].radioButtonValue = requirementMeasureDesign.equipment
        adapter.mData[16].singleDisplayRightContent = requirementMeasureDesign.name
        adapter.mData[17].singleDisplayRightContent = requirementMeasureDesign.phone
        adapter.mData[18].inputUnitContent = requirementMeasureDesign.validTime
        adapter.mData[20].textAreaContent = requirementMeasureDesign.additonalDxplain

    }

    /**
     * @马帮运输
     */
    private fun initDemandGroupCaravanTransportation(adapter: RecyclerviewAdapter, requirementCaravanTransport: RequirementCaravanTransport) {
        title = requirementCaravanTransport.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementCaravanTransport.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
        UnSerializeDataBase.inventoryId = requirementCaravanTransport.requirmentTeamServeId
        adapter.mData[0].singleDisplayRightContent = requirementCaravanTransport.requirementVariety
        adapter.mData[1].inputSingleContent = requirementCaravanTransport.projectName
        adapter.mData[2].selectContent = requirementCaravanTransport.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementCaravanTransport.projectTime

        adapter.mData[4].checkboxValueList = ArrayList(adapter.mData[4].checkboxNameList.size)
        if(requirementCaravanTransport.materialsType!=null){
            val materialsType = requirementCaravanTransport.materialsType.split("、")
            for (j in adapter.mData[4].checkboxNameList){
                val i = adapter.mData[4].checkboxNameList.indexOf(j)
                val position = materialsType.indexOf(j)
                if(position>=0)
                    adapter.mData[4].checkboxValueList.add(true)
                else
                    adapter.mData[4].checkboxValueList.add(false)
            }
        }
        adapter.mData[5].inputUnitContent = requirementCaravanTransport.workerExperience

        adapter.mData[6].inputRangeValue1 = requirementCaravanTransport.minAgeDemand
        adapter.mData[6].inputRangeValue2 =  requirementCaravanTransport.maxAgeDemand
        adapter.mData[7].radioButtonValue = requirementCaravanTransport.sexDemand
        adapter.mData[8].radioButtonValue = requirementCaravanTransport.roomBoardStandard
        adapter.mData[9].selectContent = requirementCaravanTransport.journeyCarFare
        adapter.mData[10].selectContent = requirementCaravanTransport.journeySalary

        adapter.mData[11].inputUnitContent = requirementCaravanTransport.needHorseNumber
        if(requirementCaravanTransport.salaryStandard!=null)
        adapter.mData[12].radioButtonValue = requirementCaravanTransport.salaryStandard
        adapter.mData[13].singleDisplayRightContent = requirementCaravanTransport.name
        adapter.mData[14].singleDisplayRightContent = requirementCaravanTransport.phone
        adapter.mData[15].inputUnitContent = requirementCaravanTransport.validTime
        adapter.mData[17].textAreaContent = requirementCaravanTransport.additonalExplain

    }

    /**
     * @桩基服务
     */
    private fun initDemandGroupPileFoundationConstruction(adapter: RecyclerviewAdapter, requirementPileFoundation: RequirementPileFoundation) {
        title = requirementPileFoundation.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementPileFoundation.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
        UnSerializeDataBase.inventoryId = requirementPileFoundation.requirmentTeamServeId
        adapter.mData[0].singleDisplayRightContent = requirementPileFoundation.requirementVariety
        adapter.mData[1].inputSingleContent = requirementPileFoundation.projectName
        adapter.mData[2].selectContent = requirementPileFoundation.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementPileFoundation.projectTime

        val itemMultiStyleItem = ArrayList<MultiStyleItem>()
        val requirementCarLists = requirementPileFoundation.requirementCarLists
        if(requirementCarLists!=null)
            for(j in requirementCarLists){
                itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","车辆清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.carType
                                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needCarNumber
                mData[2].selectContent = if(j.construction.toInt()==1) "箱式" else "敞篷"
                mData[3].selectContent = if(j.isInsurance.toInt()==1) "有" else "无"
                mData[4].selectContent = if(j.isDriver.toInt()==1) "是" else "否"
                mData[5].inputUnitContent = j.haveDriver
                mData[6].inputUnitContent = j.noDriver
                if(j.remark!=null)
                mData[8].textAreaContent = j.remark
                itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","车辆清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[4].itemMultiStyleItem = itemMultiStyleItem

        adapter.mData[5].inputUnitContent = requirementPileFoundation.workerExperience

        adapter.mData[6].inputRangeValue1 = requirementPileFoundation.minAgeDemand
        adapter.mData[6].inputRangeValue2 =  requirementPileFoundation.maxAgeDemand
        adapter.mData[7].radioButtonValue = requirementPileFoundation.sexDemand
        adapter.mData[8].checkboxValueList = ArrayList(adapter.mData[8].checkboxNameList.size)
        val requirementConstructionWorkKind = requirementPileFoundation.requirementConstructionWorkKind.split("、")
        for (j in adapter.mData[8].checkboxNameList){
            val i = adapter.mData[8].checkboxNameList.indexOf(j)
            val position = requirementConstructionWorkKind.indexOf(j)
            if(position>=0)
                adapter.mData[8].checkboxValueList.add(true)
            else
                adapter.mData[8].checkboxValueList.add(false)
        }

        adapter.mData[9].selectContent = requirementPileFoundation.roleMaxType
        adapter.mData[10].radioButtonValue = requirementPileFoundation.roomBoardStandard
        adapter.mData[11].selectContent = requirementPileFoundation.journeyCarFare
        adapter.mData[12].selectContent = requirementPileFoundation.journeySalary

        adapter.mData[13].inputUnitContent = requirementPileFoundation.needPileFoundationEquipment
        adapter.mData[14].radioButtonValue = requirementPileFoundation.salaryStandard
        adapter.mData[15].radioButtonValue = requirementPileFoundation.otherMachineEquipment
        adapter.mData[16].singleDisplayRightContent = requirementPileFoundation.name
        adapter.mData[17].singleDisplayRightContent = requirementPileFoundation.phone
        adapter.mData[18].inputUnitContent = requirementPileFoundation.validTime
        adapter.mData[20].textAreaContent = requirementPileFoundation.additonalExplain
    }

    /**
     * @非开挖
     */
    private fun initDemandGroupNonExcavation(adapter: RecyclerviewAdapter, requirementUnexcavation: RequirementUnexcavation) {
        title = requirementUnexcavation.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementUnexcavation.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
        UnSerializeDataBase.inventoryId = requirementUnexcavation.requirmentTeamServeId
        adapter.mData[0].singleDisplayRightContent = requirementUnexcavation.requirementVariety
        adapter.mData[1].inputSingleContent = requirementUnexcavation.projectName
        adapter.mData[2].selectContent = requirementUnexcavation.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementUnexcavation.projectTime

        val itemMultiStyleItem = ArrayList<MultiStyleItem>()
        val requirementCarLists = requirementUnexcavation.requirementCarLists
        if(requirementCarLists!=null)
            for(j in requirementCarLists){
                itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","车辆清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.carType
                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needCarNumber
                mData[2].selectContent = if(j.construction.toInt()==1) "箱式" else "敞篷"
                mData[3].selectContent = if(j.isInsurance.toInt()==1) "有" else "无"
                mData[4].selectContent = if(j.isDriver.toInt()==1) "是" else "否"
                mData[5].inputUnitContent = j.haveDriver
                mData[6].inputUnitContent = j.noDriver
                if(j.remark!=null)
                mData[8].textAreaContent = j.remark
                itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","车辆清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[4].itemMultiStyleItem = itemMultiStyleItem

        val requirementConstructionWorkKind = requirementUnexcavation.requirementConstructionWorkKind.split("、")
        for (j in adapter.mData[5].checkboxNameList){
            val i = adapter.mData[5].checkboxNameList.indexOf(j)
            val position = requirementConstructionWorkKind.indexOf(j)
            if(position>=0)
                adapter.mData[5].checkboxValueList.add(true)
            else
                adapter.mData[5].checkboxValueList.add(false)
        }

        adapter.mData[6].twoPairInputValue1 = requirementUnexcavation.diameterStandardBranchesNumber
        adapter.mData[6].twoPairInputValue2 =  requirementUnexcavation.holeStandardBranchesNumber
        adapter.mData[7].inputUnitContent = requirementUnexcavation.workerExperience
        adapter.mData[8].inputRangeValue1 = requirementUnexcavation.minAgeDemand
        adapter.mData[8].inputRangeValue2 =  requirementUnexcavation.maxAgeDemand
        adapter.mData[9].radioButtonValue = requirementUnexcavation.sexDemand

        adapter.mData[10].radioButtonValue = requirementUnexcavation.roomBoardStandard
        adapter.mData[11].selectContent = requirementUnexcavation.journeyCarFare
        adapter.mData[12].selectContent = requirementUnexcavation.journeySalary

        adapter.mData[13].inputUnitContent = requirementUnexcavation.needPileFoundation
        adapter.mData[14].radioButtonValue = requirementUnexcavation.salaryStandard
        adapter.mData[15].radioButtonValue = requirementUnexcavation.otherMachineEquipment
        adapter.mData[16].singleDisplayRightContent = requirementUnexcavation.name
        adapter.mData[17].singleDisplayRightContent = requirementUnexcavation.phone
        adapter.mData[18].inputUnitContent = requirementUnexcavation.validTime
        adapter.mData[20].textAreaContent = requirementUnexcavation.additonalExplain
    }

    /**
     * @试验调试
     */
    private fun initDemandGroupTestDebugging(adapter: RecyclerviewAdapter, requirementTest: RequirementTest) {
        title = requirementTest.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementTest.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
        UnSerializeDataBase.inventoryId = requirementTest.requirmentTeamServeId
        adapter.mData[0].singleDisplayRightContent = requirementTest.requirementVariety
        adapter.mData[1].inputSingleContent = requirementTest.projectName
        adapter.mData[2].selectContent = requirementTest.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementTest.projectTime
        val itemMultiStyleItem = ArrayList<MultiStyleItem>()
        val requirementCarLists = requirementTest.requirementCarLists
        if(requirementCarLists!=null)
            for(j in requirementCarLists){
                itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","车辆清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.carType
                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needCarNumber
                mData[2].selectContent = if(j.construction.toInt()==1) "箱式" else "敞篷"
                mData[3].selectContent = if(j.isInsurance.toInt()==1) "有" else "无"
                mData[4].selectContent = if(j.isDriver.toInt()==1) "是" else "否"
                mData[5].inputUnitContent = j.haveDriver
                mData[6].inputUnitContent = j.noDriver
                if(j.remark!=null)
                mData[8].textAreaContent = j.remark
                itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","车辆清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[4].itemMultiStyleItem = itemMultiStyleItem
        adapter.mData[5].checkboxValueList = ArrayList(adapter.mData[5].checkboxNameList.size)
        val requirementTeamVoltageClasses = requirementTest.requirementTeamVoltageClasses.split("、")
        for (j in adapter.mData[5].checkboxNameList){
            val i = adapter.mData[5].checkboxNameList.indexOf(j)
            val position = requirementTeamVoltageClasses.indexOf(j)
            if(position>=0)
                adapter.mData[5].checkboxValueList.add(true)
            else
                adapter.mData[5].checkboxValueList.add(false)
        }

        adapter.mData[6].checkboxValueList = ArrayList(adapter.mData[6].checkboxNameList.size)
        val requirementConstructionWorkKind = requirementTest.requirementConstructionWorkKind.split("、")
        for (j in adapter.mData[6].checkboxNameList){
            val i = adapter.mData[6].checkboxNameList.indexOf(j)
            val position = requirementConstructionWorkKind.indexOf(j)
            if(position>=0)
                adapter.mData[6].checkboxValueList.add(true)
            else
                adapter.mData[6].checkboxValueList.add(false)
        }
        adapter.mData[7].checkboxValueList = ArrayList(adapter.mData[7].checkboxNameList.size)
        val operationClasses = requirementTest.operationClasses.split("、")
        for (j in adapter.mData[7].checkboxNameList){
            val i = adapter.mData[7].checkboxNameList.indexOf(j)
            val position = operationClasses.indexOf(j)
            if(position>=0)
                adapter.mData[7].checkboxValueList.add(true)
            else
                adapter.mData[7].checkboxValueList.add(false)
        }
        adapter.mData[8].inputUnitContent = requirementTest.workerExperience

        adapter.mData[9].inputRangeValue1 = requirementTest.minAgeDemand
        adapter.mData[9].inputRangeValue2 =  requirementTest.maxAgeDemand
        adapter.mData[10].radioButtonValue = requirementTest.sexDemand
        adapter.mData[11].radioButtonValue = requirementTest.roomBoardStandard
        adapter.mData[12].selectContent = requirementTest.journeyCarFare
        adapter.mData[13].selectContent = requirementTest.journeySalary

        val itemMultiStyleItems = ArrayList<MultiStyleItem>()
        val requirementMembersLists = requirementTest.requirementMembersLists
        if(requirementMembersLists!=null)
            for(j in requirementMembersLists){
                itemMultiStyleItems.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.positionType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","成员清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.positionType
                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needPeopleNumber
                mData[2].inputMultiSelectUnit = j.salaryStandard.substring(j.salaryStandard.indexOf("元"))
                mData[2].inputMultiContent = j.salaryStandard.replace(mData[2].inputMultiSelectUnit,"")
                mData[3].inputSingleContent = j.personCertificate
                mData[4].selectContent = if(j.haveCertificate.toInt()==1) "是" else "否"
                mData[6].textAreaContent = j.remark
                itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem = mData
                itemMultiStyleItems[itemMultiStyleItems.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItems[0].selected = itemMultiStyleItems.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","成员清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[14].itemMultiStyleItem = itemMultiStyleItems

        adapter.mData[15].singleDisplayRightContent = requirementTest.needPeopleNumber
        adapter.mData[16].radioButtonValue = requirementTest.machineEquipment
        adapter.mData[17].singleDisplayRightContent = requirementTest.name
        adapter.mData[19].singleDisplayRightContent = requirementTest.phone
        adapter.mData[19].inputUnitContent = requirementTest.validTime
        adapter.mData[21].textAreaContent = requirementTest.additonalExplain

    }

    /**
     * @跨越架
     */
    private fun initDemandGroupCrossingFrame(adapter: RecyclerviewAdapter, requirementSpanWoodenSupprt: RequirementSpanWoodenSupprt) {
        title = requirementSpanWoodenSupprt.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementSpanWoodenSupprt.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
        UnSerializeDataBase.inventoryId = requirementSpanWoodenSupprt.requirmentTeamServeId
        adapter.mData[0].singleDisplayRightContent = requirementSpanWoodenSupprt.requirementVariety
        adapter.mData[1].inputSingleContent = requirementSpanWoodenSupprt.projectName
        adapter.mData[2].selectContent = requirementSpanWoodenSupprt.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementSpanWoodenSupprt.projectTime
        val itemMultiStyleItem = ArrayList<MultiStyleItem>()
        val requirementCarLists = requirementSpanWoodenSupprt.requirementCarLists
        if(requirementCarLists!=null)
            for(j in requirementCarLists){
                itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","车辆清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.carType
                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needCarNumber
                mData[2].selectContent = if(j.construction.toInt()==1) "箱式" else "敞篷"
                mData[3].selectContent = if(j.isInsurance.toInt()==1) "有" else "无"
                mData[4].selectContent = if(j.isDriver.toInt()==1) "是" else "否"
                mData[5].inputUnitContent = j.haveDriver
                mData[6].inputUnitContent = j.noDriver
                if(j.remark!=null)
                mData[8].textAreaContent = j.remark
                itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","车辆清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[4].itemMultiStyleItem = itemMultiStyleItem

        val spanShelfMaterial = requirementSpanWoodenSupprt.spanShelfMaterial.split("、")
        for (j in adapter.mData[5].checkboxNameList){
            val i = adapter.mData[5].checkboxNameList.indexOf(j)
            val position = spanShelfMaterial.indexOf(j)
            if(position>=0)
                adapter.mData[5].checkboxValueList.add(true)
            else
                adapter.mData[5].checkboxValueList.add(false)
        }
        adapter.mData[6].inputUnitContent = requirementSpanWoodenSupprt.workerExperience

        adapter.mData[7].inputRangeValue1 = requirementSpanWoodenSupprt.minAgeDemand
        adapter.mData[7].inputRangeValue2 =  requirementSpanWoodenSupprt.maxAgeDemand
        adapter.mData[8].radioButtonValue = requirementSpanWoodenSupprt.sexDemand
        adapter.mData[9].radioButtonValue = requirementSpanWoodenSupprt.roomBoardStandard
        adapter.mData[10].selectContent = requirementSpanWoodenSupprt.journeyCarFare
        adapter.mData[11].selectContent = requirementSpanWoodenSupprt.journeySalary

        val itemMultiStyleItems = ArrayList<MultiStyleItem>()
        val requirementMembersLists = requirementSpanWoodenSupprt.requirementMembersLists
        if(requirementMembersLists!=null)
            for(j in requirementMembersLists){
                itemMultiStyleItems.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.positionType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","成员清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.positionType
                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needPeopleNumber
                mData[2].inputMultiSelectUnit = j.salaryStandard.substring(j.salaryStandard.indexOf("元"))
                mData[2].inputMultiContent = j.salaryStandard.replace(mData[2].inputMultiSelectUnit,"")
                mData[3].inputSingleContent = j.personCertificate
                mData[4].selectContent = if(j.haveCertificate.toInt()==1) "是" else "否"
                mData[6].textAreaContent = j.remark
                itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem = mData
                itemMultiStyleItems[itemMultiStyleItems.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItems[0].selected = itemMultiStyleItems.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","成员清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[12].itemMultiStyleItem = itemMultiStyleItems
        adapter.mData[12].itemMultiStyleItem[0].PeopleNumber = requirementSpanWoodenSupprt.needPeopleNumber.toInt()
        adapter.mData[13].singleDisplayRightContent = requirementSpanWoodenSupprt.needPeopleNumber
        adapter.mData[14].radioButtonValue = requirementSpanWoodenSupprt.machineEquipment
        adapter.mData[15].singleDisplayRightContent = requirementSpanWoodenSupprt.name
        adapter.mData[16].singleDisplayRightContent = requirementSpanWoodenSupprt.phone
        adapter.mData[17].inputUnitContent = requirementSpanWoodenSupprt.validTime
        adapter.mData[19].textAreaContent = requirementSpanWoodenSupprt.additonalExplain
    }

    /**
     * @运行维护
     */
    private fun initDemandGroupOperationAndMaintenance(adapter: RecyclerviewAdapter, requirementRunningMaintain: RequirementRunningMaintain) {
        title = requirementRunningMaintain.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementRunningMaintain.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
        UnSerializeDataBase.inventoryId = requirementRunningMaintain.requirmentTeamServeId
        adapter.mData[0].singleDisplayRightContent = requirementRunningMaintain.requirementVariety
        adapter.mData[1].inputSingleContent = requirementRunningMaintain.projectName
        adapter.mData[2].selectContent = requirementRunningMaintain.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementRunningMaintain.projectTime
        val itemMultiStyleItem = ArrayList<MultiStyleItem>()
        val requirementCarLists = requirementRunningMaintain.requirementCarLists
        if(requirementCarLists!=null)
            for(j in requirementCarLists){
                itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","车辆清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.carType
                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needCarNumber
                mData[2].selectContent = if(j.construction.toInt()==1) "箱式" else "敞篷"
                mData[3].selectContent = if(j.isInsurance.toInt()==1) "有" else "无"
                mData[4].selectContent = if(j.isDriver.toInt()==1) "是" else "否"
                mData[5].inputUnitContent = j.haveDriver
                mData[6].inputUnitContent = j.noDriver
                if(j.remark!=null)
                mData[8].textAreaContent = j.remark
                itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","车辆清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[4].itemMultiStyleItem = itemMultiStyleItem
        adapter.mData[5].inputUnitContent = requirementRunningMaintain.workerExperience
        adapter.mData[6].inputRangeValue1 = requirementRunningMaintain.minAgeDemand
        adapter.mData[6].inputRangeValue2 =  requirementRunningMaintain.maxAgeDemand
        adapter.mData[7].radioButtonValue = requirementRunningMaintain.sexDemand

        val requirementConstructionWorkKind = requirementRunningMaintain.requirementConstructionWorkKind.split("、")
        for (j in adapter.mData[8].checkboxNameList){
            val i = adapter.mData[8].checkboxNameList.indexOf(j)
            val position = requirementConstructionWorkKind.indexOf(j)
            if(position>=0)
                adapter.mData[8].checkboxValueList.add(true)
            else
                adapter.mData[8].checkboxValueList.add(false)
        }
        adapter.mData[9].radioButtonValue = requirementRunningMaintain.roomBoardStandard
        adapter.mData[10].selectContent = requirementRunningMaintain.journeyCarFare
        adapter.mData[11].selectContent = requirementRunningMaintain.journeySalary

        val itemMultiStyleItems = ArrayList<MultiStyleItem>()
        val requirementMembersLists = requirementRunningMaintain.requirementMembersLists
        if(requirementMembersLists!=null)
            for(j in requirementMembersLists){
                itemMultiStyleItems.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.positionType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","成员清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.positionType
                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needPeopleNumber
                mData[2].inputMultiSelectUnit = j.salaryStandard.substring(j.salaryStandard.indexOf("元"))
                mData[2].inputMultiContent = j.salaryStandard.replace(mData[2].inputMultiSelectUnit,"")
                mData[3].inputSingleContent = j.personCertificate
                mData[4].selectContent = if(j.haveCertificate.toInt()==1) "是" else "否"
                mData[6].textAreaContent = j.remark
                itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem = mData
                itemMultiStyleItems[itemMultiStyleItems.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItems[0].selected = itemMultiStyleItems.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","成员清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[12].itemMultiStyleItem = itemMultiStyleItems

        adapter.mData[13].singleDisplayRightContent = requirementRunningMaintain.needPeopleNumber
        adapter.mData[14].radioButtonValue = requirementRunningMaintain.machineEquipment
        adapter.mData[15].singleDisplayRightContent = requirementRunningMaintain.name
        adapter.mData[16].singleDisplayRightContent = requirementRunningMaintain.phone
        adapter.mData[17].inputUnitContent = requirementRunningMaintain.validTime
        val requirementTeamVoltageClasses = requirementRunningMaintain.requirementTeamVoltageClasses.split("、")
        for (j in adapter.mData[18].checkboxNameList){
            val i = adapter.mData[18].checkboxNameList.indexOf(j)
            val position = requirementTeamVoltageClasses.indexOf(j)
            if(position>=0)
                adapter.mData[18].checkboxValueList.add(true)
            else
                adapter.mData[18].checkboxValueList.add(false)
        }
        adapter.mData[20].textAreaContent = requirementRunningMaintain.additonalExplain
    }

    /**
     * @车辆租赁
     */
    private fun initDemandLeaseVehicleLeasing(adapter: RecyclerviewAdapter, requirementLeaseCar: RequirementLeaseCar) {
        title = requirementLeaseCar.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementLeaseCar.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
        UnSerializeDataBase.inventoryId = requirementLeaseCar.requirementTeamServeId
        adapter.mData[0].singleDisplayRightContent = requirementLeaseCar.requirementVariety
        adapter.mData[1].inputSingleContent = requirementLeaseCar.projectName
        adapter.mData[2].selectContent = requirementLeaseCar.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementLeaseCar.projectTime
        val itemMultiStyleItem = ArrayList<MultiStyleItem>()
        val requirementCarLists = requirementLeaseCar.requirementCarLists
        if(requirementCarLists!=null)
            for(j in requirementCarLists){
                itemMultiStyleItem.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","车辆清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].selectContent = j.carType
                UnSerializeDataBase.inventoryIdKey = "requirementTeamServeId"
                UnSerializeDataBase.inventoryId = j.requirementTeamServeId
                mData[0].id = j.id
                mData[1].inputUnitContent = j.needCarNumber
                mData[2].selectContent = if(j.construction.toInt()==1) "箱式" else "敞篷"
                mData[3].selectContent = if(j.isInsurance.toInt()==1) "有" else "无"
                mData[4].selectContent = if(j.isDriver.toInt()==1) "是" else "否"
                mData[5].inputUnitContent = j.haveDriver
                mData[6].inputUnitContent = j.noDriver
                if(j.remark!=null)
                mData[8].textAreaContent = j.remark
                itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem = mData
                itemMultiStyleItem[itemMultiStyleItem.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItem[0].selected = itemMultiStyleItem.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItem[itemMultiStyleItem.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","车辆清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[4].itemMultiStyleItem = itemMultiStyleItem
        adapter.mData[5].inputUnitContent = requirementLeaseCar.workerExperience

        adapter.mData[6].radioButtonValue = requirementLeaseCar.roomBoardStandard
        adapter.mData[7].selectContent = requirementLeaseCar.journeyCarFare
        adapter.mData[8].selectContent = requirementLeaseCar.journeySalary

        if(requirementLeaseCar.salaryStandard!="-1.0"){
            adapter.mData[9].inputMultiContent = requirementLeaseCar.salaryStandard
            adapter.mData[9].inputMultiSelectUnit = requirementLeaseCar.salaryUnit
        }else{
            adapter.mData[9].inputMultiSelectUnit = "面议"
        }
        adapter.mData[10].singleDisplayRightContent = requirementLeaseCar.name
        adapter.mData[11].singleDisplayRightContent = requirementLeaseCar.phone
        adapter.mData[12].inputUnitContent = requirementLeaseCar.validTime
        if(requirementLeaseCar.additonalExplain!=null)
        adapter.mData[14].textAreaContent = requirementLeaseCar.additonalExplain
    }


    /**
     * @工器具租赁
     */
    private fun initDemandEquipmentLeasing(adapter: RecyclerviewAdapter, requirementLeaseConstructionTool: RequirementLeaseConstructionTool) {
        title = requirementLeaseConstructionTool.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementLeaseConstructionTool.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryIdKey = "requiremenLeaseServeId"
        UnSerializeDataBase.inventoryId = requirementLeaseConstructionTool.requiremenLeaseServeId
        adapter.mData[0].singleDisplayRightContent = requirementLeaseConstructionTool.requirementVariety
        adapter.mData[1].inputSingleContent = requirementLeaseConstructionTool.projectName
        adapter.mData[2].selectContent = requirementLeaseConstructionTool.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementLeaseConstructionTool.projectTime

        val itemMultiStyleItems = ArrayList<MultiStyleItem>()
        val requirementLeaseLists = requirementLeaseConstructionTool.requirementLeaseLists
        if(requirementLeaseLists!=null)
            for(j in requirementLeaseLists){
                itemMultiStyleItems.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.projectName,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","租赁清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].inputSingleContent = j.projectName
                mData[0].id = j.id
                UnSerializeDataBase.inventoryIdKey = "requiremenLeaseServeId"
                UnSerializeDataBase.inventoryId = j.requiremenLeaseServeId
                mData[1].inputSingleContent = j.specificationsModels
                mData[2].inputSingleContent = j.units
                mData[3].inputSingleContent = j.quantity
                if(j.detailsExplain!=null)
                mData[6].textAreaContent = j.detailsExplain
                itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem = mData
                itemMultiStyleItems[itemMultiStyleItems.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItems[0].selected = itemMultiStyleItems.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","租赁清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }

        adapter.mData[4].itemMultiStyleItem = itemMultiStyleItems
        adapter.mData[5].radioButtonValue = requirementLeaseConstructionTool.financeTransportationInsurance
        adapter.mData[6].radioButtonValue = requirementLeaseConstructionTool.distribution
        adapter.mData[7].radioButtonValue = requirementLeaseConstructionTool.partnerAttribute
        adapter.mData[8].radioButtonValue = requirementLeaseConstructionTool.hireFareStandard


        adapter.mData[9].singleDisplayRightContent = requirementLeaseConstructionTool.name
        adapter.mData[10].singleDisplayRightContent = requirementLeaseConstructionTool.phone
        adapter.mData[11].inputUnitContent = requirementLeaseConstructionTool.validTime
        if(requirementLeaseConstructionTool.additonalExplain!=null)
        adapter.mData[13].textAreaContent = requirementLeaseConstructionTool.additonalExplain
    }

    /**
     * @机械租赁
     */
    private fun initDemandMachineryLeasing(adapter: RecyclerviewAdapter, requirementLeaseMachinery: RequirementLeaseMachinery) {
        title = requirementLeaseMachinery.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementLeaseMachinery.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryIdKey = "requiremenLeaseServeId"
        UnSerializeDataBase.inventoryId = requirementLeaseMachinery.requiremenLeaseServeId
        adapter.mData[0].singleDisplayRightContent = requirementLeaseMachinery.requirementVariety
        adapter.mData[1].inputSingleContent = requirementLeaseMachinery.projectName
        adapter.mData[2].selectContent = requirementLeaseMachinery.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementLeaseMachinery.projectTime

        val itemMultiStyleItems = ArrayList<MultiStyleItem>()
        val requirementLeaseLists = requirementLeaseMachinery.requirementLeaseLists
        if(requirementLeaseLists!=null)
            for(j in requirementLeaseLists){
                itemMultiStyleItems.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.projectName,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","租赁清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].inputSingleContent = j.projectName
                mData[0].id = j.id
                UnSerializeDataBase.inventoryIdKey = "requiremenLeaseServeId"
                UnSerializeDataBase.inventoryId = j.requiremenLeaseServeId
                mData[1].inputSingleContent = j.specificationsModels
                mData[2].inputSingleContent = j.units
                mData[3].inputSingleContent = j.quantity
                if(j.detailsExplain!=null)
                mData[6].textAreaContent = j.detailsExplain
                itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem = mData
                itemMultiStyleItems[itemMultiStyleItems.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItems[0].selected = itemMultiStyleItems.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","租赁清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[4].itemMultiStyleItem = itemMultiStyleItems


        adapter.mData[5].radioButtonValue = requirementLeaseMachinery.financeTransportationInsurance
        adapter.mData[6].radioButtonValue = requirementLeaseMachinery.distribution
        adapter.mData[7].radioButtonValue = requirementLeaseMachinery.partnerAttribute
        adapter.mData[8].radioButtonValue = requirementLeaseMachinery.hireFareStandard


        adapter.mData[9].singleDisplayRightContent = requirementLeaseMachinery.name
        adapter.mData[10].singleDisplayRightContent = requirementLeaseMachinery.phone
        adapter.mData[11].inputUnitContent = requirementLeaseMachinery.validTime
        if(requirementLeaseMachinery.additonalExplain!=null)
        adapter.mData[13].textAreaContent = requirementLeaseMachinery.additonalExplain
    }

    /**
     * @设备租赁
     */
    private fun initDemandEquipmentLeasing(adapter: RecyclerviewAdapter, requirementLeaseFacility: RequirementLeaseFacility) {
        title = requirementLeaseFacility.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementLeaseFacility.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        UnSerializeDataBase.inventoryIdKey = "requiremenLeaseServeId"
        UnSerializeDataBase.inventoryId = requirementLeaseFacility.requiremenLeaseServeId
        adapter.mData[0].singleDisplayRightContent = requirementLeaseFacility.requirementVariety
        adapter.mData[1].inputSingleContent = requirementLeaseFacility.projectName
        adapter.mData[2].selectContent = requirementLeaseFacility.projectSite.replace(" / ", " ")
        adapter.mData[3].inputUnitContent = requirementLeaseFacility.projectTime

        val itemMultiStyleItems = ArrayList<MultiStyleItem>()
        val requirementLeaseLists = requirementLeaseFacility.requirementLeaseLists
        if(requirementLeaseLists!=null)
            for(j in requirementLeaseLists){
                itemMultiStyleItems.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.projectName,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","租赁清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].inputSingleContent = j.projectName
                mData[0].id = j.id
                UnSerializeDataBase.inventoryIdKey = "requiremenLeaseServeId"
                UnSerializeDataBase.inventoryId = j.requiremenLeaseServeId
                mData[1].inputSingleContent = j.specificationsModels
                mData[2].inputSingleContent = j.units
                mData[3].inputSingleContent = j.quantity
                if(j.detailsExplain!=null)
                mData[6].textAreaContent = j.detailsExplain
                itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem = mData
                itemMultiStyleItems[itemMultiStyleItems.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItems[0].selected = itemMultiStyleItems.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","租赁清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[4].itemMultiStyleItem = itemMultiStyleItems


        adapter.mData[5].radioButtonValue = requirementLeaseFacility.financeTransportationInsurance
        adapter.mData[6].radioButtonValue = requirementLeaseFacility.distribution
        adapter.mData[7].radioButtonValue = requirementLeaseFacility.partnerAttribute
        adapter.mData[8].radioButtonValue = requirementLeaseFacility.hireFareStandard


        adapter.mData[9].singleDisplayRightContent = requirementLeaseFacility.name
        adapter.mData[10].singleDisplayRightContent = requirementLeaseFacility.phone
        adapter.mData[11].inputUnitContent = requirementLeaseFacility.validTime
        if(requirementLeaseFacility.additonalExplain!=null)
        adapter.mData[13].textAreaContent = requirementLeaseFacility.additonalExplain
    }

    /**
     * @需求三方
     */
    private fun initDemandTripartite(adapter: RecyclerviewAdapter) {
        adapter.urlPath = Constants.HttpUrlPath.Requirement.updateRequirementThirdParty
        val requirementThirdPartyDetail = arguments!!.getSerializable("data") as RequirementThirdPartyDetail
        title = requirementThirdPartyDetail.requirementType
        activity?.runOnUiThread{
            mView.tv_modify_job_information_title.text = requirementThirdPartyDetail.requirementType
        }
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as MyReleaseActivity
        val itemMultiStyleItems = ArrayList<MultiStyleItem>()
        val thirdLists = requirementThirdPartyDetail.thirdLists
        if(thirdLists!=null)
            for(j in thirdLists){
                itemMultiStyleItems.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.projectName,true))
                val bundle = Bundle()
                val mData:List<MultiStyleItem>
                bundle.putString("type","三方服务清册发布")
                mData = adapterGenerate.PublishDetailList(bundle).mData
                mData[0].inputSingleContent = j.projectName
                UnSerializeDataBase.inventoryId=j.requirementThirdPartyId
                UnSerializeDataBase.inventoryIdKey = "requirementThirdPartyId"
                mData[0].id = j.id
                mData[1].inputSingleContent = j.specificationsModels
                mData[2].inputSingleContent = j.units
                mData[3].inputSingleContent = j.quantity
                mData[6].textAreaContent = j.detailsExplain
                itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem = mData
                itemMultiStyleItems[itemMultiStyleItems.size-1].jumpListener = View.OnClickListener {
                    itemMultiStyleItems[0].selected = itemMultiStyleItems.size-1
                    val bundle = Bundle()
                    val itemMultiStyleItem = itemMultiStyleItems[itemMultiStyleItems.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type","三方服务清册发布")
                    FragmentHelper.switchFragment(activity!!,
                        PublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_my_release,"")
                }
            }
        adapter.mData[1].itemMultiStyleItem = itemMultiStyleItems

        adapter.mData[2].radioButtonValue = requirementThirdPartyDetail.partnerAttribute
        adapter.mData[3].radioButtonValue = requirementThirdPartyDetail.fareStandard

        adapter.mData[4].singleDisplayRightContent = requirementThirdPartyDetail.name
        adapter.mData[5].singleDisplayRightContent = requirementThirdPartyDetail.phone
        adapter.mData[6].inputUnitContent = requirementThirdPartyDetail.validTime
        adapter.mData[8].textAreaContent = requirementThirdPartyDetail.additionalExplain
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