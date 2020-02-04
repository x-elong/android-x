package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.activity.*
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_demand_display.view.*
import java.io.Serializable


class DemandDisplayFragment:Fragment() {
    companion object{
        fun newInstance(args: Bundle): DemandDisplayFragment
        {
            val fragment= DemandDisplayFragment()
            fragment.arguments=args
            return fragment
        }
    }
    var type:Int=0
    lateinit var id:String
    val mdata = Bundle()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_demand_display, container, false)
        type = arguments!!.getInt("type")
        id = arguments!!.getString("id")
        initFragment(view)
        return view
    }

    private fun initFragment(view: View) {
        view.tv_demand_display_back.setOnClickListener {
            activity!!.finish()
        }

        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = view.context
        adapterGenerate.activity = activity as AppCompatActivity
        lateinit var adapter: RecyclerviewAdapter
        when (type) {
            //需求个人显示模板
            Constants.FragmentType.PERSONAL_TYPE.ordinal -> {
                adapter = adapterGenerate.demandIndividualDisplay()
                val result = getRequirementPersonDetail(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.requirementMajor==null) {
                            " " } else{ data.requirementMajor }
                        adapter.mData[2].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName}
                        if(data.projectSite==null)
                        {
                            adapter.mData[3].singleDisplayRightContent= " "
                        }
                        else
                        {
                            var str=data.projectSite.split(" / ")
                            var projectSite=""
                            for(temp in str) projectSite+= "$temp "
                            adapter.mData[3].singleDisplayRightContent=projectSite
                        }
                        adapter.mData[4].singleDisplayRightContent=if(data.planTime==null) {
                            " " } else{ data.planTime}
                        adapter.mData[5].singleDisplayRightContent=if(data.workerExperience==null) {
                            " " } else{ data.workerExperience}
                        if(data.minAgeDemand=="10"||data.maxAgeDemand=="10"){
                            adapter.mData[6].singleDisplayRightContent=""
                        }else{ adapter.mData[6].singleDisplayRightContent="${data.minAgeDemand}~${data.maxAgeDemand}" }
                        when {
                            data.sexDemand=="1" -> adapter.mData[7].singleDisplayRightContent="男"
                            data.sexDemand=="0" -> adapter.mData[7].singleDisplayRightContent="女"
                            data.sexDemand=="-1" -> adapter.mData[7].singleDisplayRightContent="男女不限"
                            else -> {adapter.mData[7].singleDisplayRightContent=" " }
                        }
                        when {
                            data.roomBoardStandard=="1" -> adapter.mData[8].singleDisplayRightContent="全包"
                            data.roomBoardStandard=="0" -> adapter.mData[8].singleDisplayRightContent="自理"
                            else -> { adapter.mData[8].singleDisplayRightContent=" "}
                        }
                        adapter.mData[9].singleDisplayRightContent=if(data.journeySalary==null) {
                            " " } else{ data.journeySalary}
                        adapter.mData[10].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            " " } else{ data.journeyCarFare}
                        adapter.mData[11].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                            " " } else{ data.needPeopleNumber}
                        if(data.salaryUnit=="面议"||data.salaryStandard=="-1.00"){
                            adapter.mData[12].singleDisplayRightContent= "面议"
                        }else {
                            adapter.mData[12].singleDisplayRightContent= "${data.salaryStandard} ${data.salaryUnit}"
                        }
                        adapter.mData[14].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[15].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[16].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[17].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener{
                            mdata.putSerializable("RequirementPersonDetail",data)
                            mdata.putInt("type", Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }
                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //需求主网
            Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplay()
                val result = getRequirementMajorNetWork(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            "" } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            "" } else{ data.projectName }
                        if(data.projectSite==null)
                            adapter.mData[2].singleDisplayRightContent= ""
                        else
                        {
                            var str=data.projectSite.split(" / ")
                            var projectSite=""
                            for(temp in str) projectSite+= "$temp "
                            adapter.mData[2].singleDisplayRightContent=projectSite
                        }
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            "" } else{ data.projectTime}
                        if(data.requirementCarLists == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementCarLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    val listData = data.requirementCarLists
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putString("type","车辆清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.requirementTeamVoltageClasses==null) {
                            " " } else{ data.requirementTeamVoltageClasses}
                        adapter.mData[6].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                            " " } else{ data.requirementConstructionWorkKind}
                        adapter.mData[7].singleDisplayRightContent=if(data.workerExperience==null) {
                            " " } else{ data.workerExperience}
                        adapter.mData[8].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                            " " } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                        when {
                            data.sexDemand=="0" -> adapter.mData[9].singleDisplayRightContent="女"
                            data.sexDemand=="1" -> adapter.mData[9].singleDisplayRightContent="男"
                            data.sexDemand=="-1" -> adapter.mData[9].singleDisplayRightContent="男女不限"
                            else -> {adapter.mData[9].singleDisplayRightContent=" " }
                        }
                        when {
                            data.roomBoardStandard=="0" -> adapter.mData[10].singleDisplayRightContent="自理"
                            data.roomBoardStandard=="1" -> adapter.mData[10].singleDisplayRightContent="全包"
                            else -> { adapter.mData[10].singleDisplayRightContent=" "}
                        }
                        adapter.mData[11].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            " " } else{ data.journeyCarFare}
                        adapter.mData[12].singleDisplayRightContent=if(data.journeySalary==null) {
                            " " } else{ data.journeySalary}
                        adapter.mData[13].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                            " " } else{ data.needPeopleNumber}
                        /*adapter.mData[14].singleDisplayRightContent=if(data.vehicle==null) {
                            " " } else{ data.vehicle}
                        when {
                            data.requirementPowerTransformationSalary == null && data.requirementListQuotations == null ->adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            },View.OnClickListener { Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show() })
                            data.requirementPowerTransformationSalary == null &&data.requirementListQuotations !=null -> adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            }, View.OnClickListener {
                                if(data.requirementListQuotations!!.isEmpty())
                                {
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    val listData = data.requirementListQuotations
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putInt("type",3)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                            data.requirementListQuotations == null &&data.requirementPowerTransformationSalary !=null -> adapter.mData[15].buttonListener = listOf(
                                View.OnClickListener {
                                    if(data.requirementPowerTransformationSalary!!.isEmpty())
                                    {
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                    }
                                    else {
                                        val listData = data.requirementPowerTransformationSalary
                                        mdata.putSerializable("listData2", listData as Serializable)
                                        mdata.putInt("type", 2)
                                        (activity as DemandDisplayActivity).switchFragment(
                                            ProjectListFragment.newInstance(mdata)
                                        )
                                    }
                                },
                                View.OnClickListener {
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            else -> adapter.mData[15].buttonListener = listOf(
                                View.OnClickListener {
                                    if(data.requirementPowerTransformationSalary!!.isEmpty())
                                    {
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                    }
                                    else {
                                        val listData = data.requirementPowerTransformationSalary
                                        mdata.putSerializable("listData2", listData as Serializable)
                                        mdata.putInt("type", 2)
                                        (activity as DemandDisplayActivity).switchFragment(
                                            ProjectListFragment.newInstance(mdata)
                                        )
                                    }},
                                View.OnClickListener {
                                    if(data.requirementListQuotations!!.isEmpty())
                                    {
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        val listData = data.requirementListQuotations
                                        mdata.putSerializable("listData1", listData as Serializable)
                                        mdata.putInt("type",3)
                                        (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                    } })
                            //薪资标准
                        }*/
                        when {
                            data.constructionEquipment=="0" -> adapter.mData[14].singleDisplayRightContent="不提供"
                            data.constructionEquipment=="1" -> adapter.mData[14].singleDisplayRightContent="提供"
                            else->{adapter.mData[14].singleDisplayRightContent=" " }
                        }
                        if(data.requirementMembersLists == null)
                        {
                            adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })//成员清册查看
                        }
                        else{
                            if(data.requirementMembersLists!!.isEmpty())
                            {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })//成员清册查看
                            }
                            else
                            {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    mdata.putSerializable("listData2", listData as Serializable)
                                    mdata.putString("type","成员清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))      })//乙方材料清册
                            }
                        }
                        adapter.mData[17].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[18].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[19].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[20].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("listData1",data.requirementCarLists as Serializable) //车辆清册查看
                            mdata.putSerializable("listData2", data.requirementMembersLists as Serializable) //成员清册查看
                            mdata.putSerializable("RequirementMajorNetWork",data as Serializable)
                            mdata.putInt("type", Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }

                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //配网
            Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplay()
                val result = getRequirementDistributionNetWork(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName }
                        if(data.projectSite==null)
                        {
                            adapter.mData[2].singleDisplayRightContent= " "
                        }
                        else
                        {
                            var str=data.projectSite.split(" / ")
                            var projectSite=""
                            for(temp in str) projectSite+= "$temp "
                            adapter.mData[2].singleDisplayRightContent=projectSite
                        }
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            " " } else{ data.projectTime}
                        if(data.requirementCarLists == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册查看
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementCarLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册查看
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册查看
                                    val listData = data.requirementCarLists
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putString("type","车辆清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.requirementTeamVoltageClasses==null) {
                            " " } else{ data.requirementTeamVoltageClasses}
                        adapter.mData[6].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                            " " } else{ data.requirementConstructionWorkKind}
                        adapter.mData[7].singleDisplayRightContent=if(data.workerExperience==null) {
                            " " } else{ data.workerExperience}
                        adapter.mData[8].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                            " " } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                        when {
                            data.sexDemand=="0" -> adapter.mData[9].singleDisplayRightContent="女"
                            data.sexDemand=="1" -> adapter.mData[9].singleDisplayRightContent="男"
                            data.sexDemand=="-1" -> adapter.mData[9].singleDisplayRightContent="男女不限"
                            else -> { adapter.mData[9].singleDisplayRightContent=" "}
                        }
                        when {
                            data.roomBoardStandard=="0" -> adapter.mData[10].singleDisplayRightContent="自理"
                            data.roomBoardStandard=="1" -> adapter.mData[10].singleDisplayRightContent="全包"
                            else -> {adapter.mData[10].singleDisplayRightContent=" " }
                        }
                        adapter.mData[11].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            " " } else{ data.journeyCarFare}
                        adapter.mData[12].singleDisplayRightContent=if(data.journeySalary==null) {
                            " " } else{ data.journeySalary}
                        adapter.mData[13].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                            " " } else{ data.needPeopleNumber}
                        when {
                            data.constructionEquipment=="0" -> adapter.mData[14].singleDisplayRightContent="不提供"
                            data.constructionEquipment=="1" -> adapter.mData[14].singleDisplayRightContent="提供"
                            else->{ adapter.mData[14].singleDisplayRightContent=" "}
                        }
                        if(data.requirementMembersLists == null)
                        {
                            adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })//成员清册查看
                        }
                        else{
                            if(data.requirementMembersLists!!.isEmpty())
                            {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })//成员清册查看
                            }
                            else
                            {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    mdata.putSerializable("listData2", listData as Serializable)
                                    mdata.putString("type","成员清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))      })//乙方材料清册
                            }
                        }
                        adapter.mData[17].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[18].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[19].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[20].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("RequirementDistributionNetwork",data as Serializable)
                            mdata.putSerializable("listData1",data.requirementCarLists as Serializable) //车辆清册查看
                            mdata.putSerializable("listData2", data.requirementMembersLists as Serializable) //成员清册查看
                            mdata.putInt("type", Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }
                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //变电
            Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplay()
                val result = getRequirementPowerTransformation(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName }
                        if(data.projectSite==null)
                        {
                            adapter.mData[2].singleDisplayRightContent= " "
                        }
                        else
                        {
                            var str=data.projectSite.split(" / ")
                            var projectSite=""
                            for(temp in str) projectSite+= "$temp "
                            adapter.mData[2].singleDisplayRightContent=projectSite
                        }
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            " " } else{ data.projectTime}
                        if(data.requirementCarLists == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementCarLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    val listData = data.requirementCarLists
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putString("type","车辆清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.requirementTeamVoltageClasses==null) {
                            " " } else{ data.requirementTeamVoltageClasses}
                        adapter.mData[6].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                            " " } else{ data.requirementConstructionWorkKind}
                        adapter.mData[7].singleDisplayRightContent=if(data.workerExperience==null) {
                            " " } else{ data.workerExperience}
                        adapter.mData[8].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                            " " } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                        when {
                            data.sexDemand=="0" -> adapter.mData[9].singleDisplayRightContent="女"
                            data.sexDemand=="1" -> adapter.mData[9].singleDisplayRightContent="男"
                            data.sexDemand=="-1" -> adapter.mData[9].singleDisplayRightContent="男女不限"
                            else -> { adapter.mData[9].singleDisplayRightContent=" "}
                        }
                        when {
                            data.roomBoardStandard=="0" -> adapter.mData[10].singleDisplayRightContent="自理"
                            data.roomBoardStandard=="1" -> adapter.mData[10].singleDisplayRightContent="全包"
                            else -> { adapter.mData[10].singleDisplayRightContent=" " }
                        }
                        adapter.mData[11].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            " " } else{ data.journeyCarFare}
                        adapter.mData[12].singleDisplayRightContent=if(data.journeySalary==null) {
                            " " } else{ data.journeySalary}
                        adapter.mData[13].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                            " " } else{ data.needPeopleNumber}
                        when {
                            data.constructionEquipment=="0" -> adapter.mData[14].singleDisplayRightContent="不提供"
                            data.constructionEquipment=="1" -> adapter.mData[14].singleDisplayRightContent="提供"
                            else->{adapter.mData[14].singleDisplayRightContent=" "}
                        }
                        if(data.requirementMembersLists == null)
                        {
                            adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })//成员清册查看
                        }
                        else{
                            if(data.requirementMembersLists!!.isEmpty())
                            {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })//成员清册查看
                            }
                            else
                            {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    mdata.putSerializable("listData2", listData as Serializable)
                                    mdata.putString("type","成员清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))      })//乙方材料清册
                            }
                        }
                        adapter.mData[17].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[18].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[19].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[20].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("listData1",data.requirementCarLists as Serializable) //车辆清册查看
                            mdata.putSerializable("listData2", data.requirementMembersLists as Serializable) //成员清册查看
                            mdata.putInt("type", Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal)
                            mdata.putSerializable("RequirementPowerTransformation",data as Serializable)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }
                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //测量设计
            Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplay()
                val result = getRequirementMeasureDesign(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName }
                        if(data.projectSite==null)
                        {
                            adapter.mData[2].singleDisplayRightContent= " "
                        }
                        else
                        {
                            var str=data.projectSite.split(" / ")
                            var projectSite=""
                            for(temp in str) projectSite+= "$temp "
                            adapter.mData[2].singleDisplayRightContent=projectSite
                        }
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            " " } else{ data.projectTime}
                        if(data.requirementCarLists == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementCarLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    val listData = data.requirementCarLists
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putString("type","车辆清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.requirementTeamVoltageClasses==null) {
                            " " } else{ data.requirementTeamVoltageClasses}
                        adapter.mData[6].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                            " " } else{ data.requirementConstructionWorkKind}
                        adapter.mData[7].singleDisplayRightContent=if(data.workerExperience==null) {
                            " " } else{ data.workerExperience}
                        adapter.mData[8].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                            " " } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                        when {
                            data.sexDemand=="0" -> adapter.mData[9].singleDisplayRightContent="女"
                            data.sexDemand=="1" -> adapter.mData[9].singleDisplayRightContent="男"
                            data.sexDemand=="-1" -> adapter.mData[9].singleDisplayRightContent="男女不限"
                            else -> { adapter.mData[9].singleDisplayRightContent=" "}
                        }
                        when {
                            data.roomBoardStandard=="0" -> adapter.mData[10].singleDisplayRightContent="自理"
                            data.roomBoardStandard=="1" -> adapter.mData[10].singleDisplayRightContent="全包"
                            else -> { adapter.mData[10].singleDisplayRightContent=" "}
                        }
                        adapter.mData[11].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            " " } else{ data.journeyCarFare}
                        adapter.mData[12].singleDisplayRightContent=if(data.journeySalary==null) {
                            " " } else{ data.journeySalary}
                        adapter.mData[13].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                            " " } else{ data.needPeopleNumber}
                        when {
                            data.equipment=="0" -> adapter.mData[14].singleDisplayRightContent="不提供"
                            data.equipment=="1" -> adapter.mData[14].singleDisplayRightContent="提供"
                            else->{adapter.mData[14].singleDisplayRightContent=" " }
                        }
                        if(data.requirementMembersLists == null)
                        {
                            adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })//成员清册查看
                        }
                        else{
                            if(data.requirementMembersLists!!.isEmpty())
                            {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })//成员清册查看
                            }
                            else
                            {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    mdata.putSerializable("listData2", listData as Serializable)
                                    mdata.putString("type","成员清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))      })//乙方材料清册
                            }
                        }
                        adapter.mData[17].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[18].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[19].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[20].textAreaContent=if(data.additonalDxplain==null) {
                            " " } else{ data.additonalDxplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("listData1",data.requirementCarLists as Serializable) //车辆清册查看
                            mdata.putSerializable("listData2", data.requirementMembersLists as Serializable) //成员清册查看
                            mdata.putSerializable("RequirementMeasureDesign",data as Serializable)
                            mdata.putInt("type", Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }
                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //马帮运输
            Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplayGongTrans()
                val result = getRequirementCaravanTransport(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName }
                        adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                            " " } else{ data.projectSite}
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            " " } else{ data.projectTime}
//                        if(data.requirementTeamProjectList == null )
//                        {
//                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
//                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
//                            })
//                        }
//                        else
//                        {
//                            if(data.requirementTeamProjectList!!.isEmpty())
//                            {
//                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
//                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
//                                })
//                            }
//                            else
//                            {
//                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
//                                    val listData = data.requirementTeamProjectList
//                                    mdata.putSerializable("listData1", listData as Serializable)
//                                    mdata.putInt("type",1)
//                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
//                                })
//                            }
//                        }
                        adapter.mData[4].singleDisplayRightContent=if(data.materialsType==null) {
                            " " } else{ data.materialsType}
                        adapter.mData[5].singleDisplayRightContent=if(data.workerExperience==null) {
                            " " } else{ data.workerExperience}
                        adapter.mData[6].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                            " " } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                        when {
                            data.sexDemand=="0" -> adapter.mData[7].singleDisplayRightContent="女"
                            data.sexDemand=="1" -> adapter.mData[7].singleDisplayRightContent="男"
                            data.sexDemand=="-1" -> adapter.mData[7].singleDisplayRightContent="男女不限"
                            else -> { adapter.mData[7].singleDisplayRightContent=" "}
                        }
                        when {
                            data.roomBoardStandard=="0" -> adapter.mData[8].singleDisplayRightContent="自理"
                            data.roomBoardStandard=="1" -> adapter.mData[8].singleDisplayRightContent="全包"
                            else -> {adapter.mData[8].singleDisplayRightContent=" " }
                        }
                        adapter.mData[9].singleDisplayRightContent=if(data.journeySalary==null) {
                            " " } else{ data.journeySalary}
                        adapter.mData[10].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            " " } else{ data.journeyCarFare}
                        adapter.mData[11].singleDisplayRightContent=if(data.needHorseNumber==null) {
                            " " } else{ data.needHorseNumber}
//                        if(data.requirementListQuotations == null )
//                        {
//                            adapter.mData[12].buttonListener = listOf(View.OnClickListener {
//                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
//                            })
//                        }
//                        else
//                        {
//                            adapter.mData[12].buttonListener = listOf(View.OnClickListener {
//                                if(data.requirementListQuotations!!.isEmpty())
//                                {
//                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
//                                }
//                                else{
//                                    val listData = data.requirementListQuotations
//                                    mdata.putSerializable("listData1", listData as Serializable)
//                                    mdata.putInt("type",3)
//                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
//                                }
//                            })
//                        }
                        adapter.mData[13].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[14].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[15].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[16].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("RequirementCaravanTransport",data as Serializable)
                            mdata.putInt("type", Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }
                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //桩机服务
            Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplayPile()
                val result = getRequirementPileFoundation(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName }
                        adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                            " " } else{ data.projectSite}
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            " " } else{ data.projectTime}
                        if(data.requirementCarLists == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementCarLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    val listData = data.requirementCarLists
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putString("type","车辆清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.workerExperience==null) {
                            " " } else{ data.workerExperience}
                        adapter.mData[6].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                            " " } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                        when {
                            data.sexDemand=="0" -> adapter.mData[7].singleDisplayRightContent="女"
                            data.sexDemand=="1" -> adapter.mData[7].singleDisplayRightContent="男"
                            data.sexDemand=="-1" -> adapter.mData[7].singleDisplayRightContent="男女不限"
                            else -> { adapter.mData[7].singleDisplayRightContent=" "}
                        }
                        adapter.mData[8].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                            " " } else{ data.requirementConstructionWorkKind}
                        adapter.mData[9].singleDisplayRightContent=if(data.roleMaxType==null) {
                            " " } else{ data.roleMaxType}
                        when {
                            data.roomBoardStandard=="0" -> adapter.mData[10].singleDisplayRightContent="自理"
                            data.roomBoardStandard=="1" -> adapter.mData[10].singleDisplayRightContent="全包"
                            else -> {adapter.mData[10].singleDisplayRightContent=" " }
                        }
                        adapter.mData[11].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            " " } else{ data.journeyCarFare}
                        adapter.mData[12].singleDisplayRightContent=if(data.journeySalary==null) {
                            " " } else{ data.journeySalary}
                        when {
                            data.salaryStandard=="0" -> adapter.mData[13].singleDisplayRightContent=" "
                            data.salaryStandard=="1" -> adapter.mData[13].singleDisplayRightContent="面议"
                            else -> {adapter.mData[13].singleDisplayRightContent=" " }
                        }
                        adapter.mData[14].singleDisplayRightContent=if(data.needPileFoundationEquipment==null) {
                            " " } else{ data.needPileFoundationEquipment}
                        when {
                            data.otherMachineEquipment=="0" -> adapter.mData[15].singleDisplayRightContent="不提供"
                            data.otherMachineEquipment=="1" -> adapter.mData[15].singleDisplayRightContent="提供"
                            else->{adapter.mData[15].singleDisplayRightContent=" " }
                        }
                        adapter.mData[17].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[18].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[19].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[20].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("listData1",data.requirementCarLists as Serializable) //车辆清册查看
                            mdata.putSerializable("RequirementPileFoundation",data as Serializable)
                            mdata.putInt("type", Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }
                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //非开挖
            Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplayTrenchiless()
                val result = getRequirementUnexcavation(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName }
                        adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                            " " } else{ data.projectSite}
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            " " } else{ data.projectTime}
                        if(data.requirementCarLists == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementCarLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    val listData = data.requirementCarLists
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putString("type","车辆清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                            " " } else{ data.requirementConstructionWorkKind}
                        adapter.mData[6].singleDisplayRightContent=if(data.diameterStandardBranchesNumber==null || data.holeStandardBranchesNumber==null) {
                            " " } else{ "单管直径${data.diameterStandardBranchesNumber}毫米(mm)×${data.holeStandardBranchesNumber}孔"}
                        adapter.mData[7].singleDisplayRightContent=if(data.workerExperience==null) {
                            " " } else{ data.workerExperience}
                        adapter.mData[8].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                            " " } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                        when {
                            data.sexDemand=="0" -> adapter.mData[9].singleDisplayRightContent="女"
                            data.sexDemand=="1" -> adapter.mData[9].singleDisplayRightContent="男"
                            data.sexDemand=="-1" -> adapter.mData[9].singleDisplayRightContent="男女不限"
                            else -> {adapter.mData[9].singleDisplayRightContent=" " }
                        }
                        when {
                            data.roomBoardStandard=="0" -> adapter.mData[10].singleDisplayRightContent="自理"
                            data.roomBoardStandard=="1" -> adapter.mData[10].singleDisplayRightContent="全包"
                            else -> {adapter.mData[10].singleDisplayRightContent=" " }
                        }
                        adapter.mData[11].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            " " } else{ data.journeyCarFare}
                        adapter.mData[12].singleDisplayRightContent=if(data.journeySalary==null) {
                            " " } else{ data.journeySalary}
                        when {
                            data.salaryStandard=="0" -> adapter.mData[13].singleDisplayRightContent=" "
                            data.salaryStandard=="1" -> adapter.mData[13].singleDisplayRightContent="面议"
                            else -> {adapter.mData[13].singleDisplayRightContent=" " }
                        }
                        adapter.mData[14].singleDisplayRightContent=if(data.needPileFoundation==null) {
                            " " } else{ data.needPileFoundation}
                        when {
                            data.otherMachineEquipment=="0" -> adapter.mData[15].singleDisplayRightContent="不提供"
                            data.otherMachineEquipment=="1" -> adapter.mData[15].singleDisplayRightContent="提供"
                            else->{adapter.mData[15].singleDisplayRightContent=" " }
                        }
                        adapter.mData[17].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[18].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[19].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[20].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("listData1",data.requirementCarLists as Serializable) //车辆清册查看
                            mdata.putSerializable("RequirementUnexcavation",data as Serializable)
                            mdata.putInt("type", Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }
                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //试验调试
            Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplayTestAndDebugging()
                val result = getRequirementTest(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName }
                        adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                            " " } else{ data.projectSite}
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            " " } else{ data.projectTime}
                        if(data.requirementCarLists == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementCarLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    val listData = data.requirementCarLists
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putString("type","车辆清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.requirementTeamVoltageClasses==null) {
                            " " } else{ data.requirementTeamVoltageClasses}
                        adapter.mData[6].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                            " " } else{ data.requirementConstructionWorkKind}
                        adapter.mData[7].singleDisplayRightContent=if(data.operationClasses==null) {
                            " " } else{ data.operationClasses}
                        adapter.mData[8].singleDisplayRightContent=if(data.workerExperience==null) {
                            " " } else{ data.workerExperience}
                        adapter.mData[9].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                            " " } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                        when {
                            data.sexDemand=="0" -> adapter.mData[10].singleDisplayRightContent="女"
                            data.sexDemand=="1" -> adapter.mData[10].singleDisplayRightContent="男"
                            data.sexDemand=="-1" -> adapter.mData[10].singleDisplayRightContent="男女不限"
                            else -> {  adapter.mData[10].singleDisplayRightContent=" "}
                        }
                        when {
                            data.roomBoardStandard=="0" -> adapter.mData[11].singleDisplayRightContent="自理"
                            data.roomBoardStandard=="1" -> adapter.mData[11].singleDisplayRightContent="全包"
                            else -> { adapter.mData[11].singleDisplayRightContent=" "}
                        }
                        adapter.mData[12].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            " " } else{ data.journeyCarFare}
                        adapter.mData[13].singleDisplayRightContent=if(data.journeySalary==null) {
                            " " } else{ data.journeySalary}
                        adapter.mData[14].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                            " " } else{ data.needPeopleNumber}
                        if(data.requirementMembersLists == null)
                        {
                            adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })//成员清册查看
                        }
                        else{
                            if(data.requirementMembersLists!!.isEmpty())
                            {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })//成员清册查看
                            }
                            else
                            {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    mdata.putSerializable("listData2", listData as Serializable)
                                    mdata.putString("type","成员清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))      })//乙方材料清册
                            }
                        }
                        when {
                            data.machineEquipment=="0" -> adapter.mData[16].singleDisplayRightContent="不提供"
                            data.machineEquipment=="1" -> adapter.mData[16].singleDisplayRightContent="提供"
                            else->{adapter.mData[16].singleDisplayRightContent=" " }
                        }
                        adapter.mData[18].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[19].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[20].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[21].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("listData1",data.requirementCarLists as Serializable) //车辆清册查看
                            mdata.putSerializable("listData2", data.requirementMembersLists as Serializable) //成员清册查看
                            mdata.putSerializable("RequirementTest",data as Serializable)
                            mdata.putInt("type", Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }
                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //跨越架
            Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplayCrossFrame()
                val result = getRequirementSpanWoodenSupprt(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName }
                        adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                            " " } else{ data.projectSite}
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            " " } else{ data.projectTime}
                        if(data.requirementCarLists == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementCarLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    val listData = data.requirementCarLists
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putString("type","车辆清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.spanShelfMaterial==null) {
                            " " } else{ data.spanShelfMaterial}
                        adapter.mData[6].singleDisplayRightContent=if(data.workerExperience==null) {
                            " " } else{ data.workerExperience}
                        adapter.mData[7].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                            " " } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                        when {
                            data.sexDemand=="0" -> adapter.mData[8].singleDisplayRightContent="女"
                            data.sexDemand=="1" -> adapter.mData[8].singleDisplayRightContent="男"
                            data.sexDemand=="-1" -> adapter.mData[8].singleDisplayRightContent="男女不限"
                            else -> { adapter.mData[8].singleDisplayRightContent=" "}
                        }
                        when {
                            data.roomBoardStandard=="0" -> adapter.mData[9].singleDisplayRightContent="自理"
                            data.roomBoardStandard=="1" -> adapter.mData[9].singleDisplayRightContent="全包"
                            else -> {adapter.mData[9].singleDisplayRightContent=" " }
                        }
                        adapter.mData[10].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            " " } else{ data.journeyCarFare}
                        adapter.mData[11].singleDisplayRightContent=if(data.journeySalary==null) {
                            " " } else{ data.journeySalary}
                        adapter.mData[12].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                            " " } else{ data.needPeopleNumber}
                        if(data.requirementMembersLists == null)
                        {
                            adapter.mData[13].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })//成员清册查看
                        }
                        else{
                            if(data.requirementMembersLists!!.isEmpty())
                            {
                                adapter.mData[13].buttonListener = listOf(View.OnClickListener {
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })//成员清册查看
                            }
                            else
                            {
                                adapter.mData[13].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    mdata.putSerializable("listData2", listData as Serializable)
                                    mdata.putString("type","成员清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))      })//乙方材料清册
                            }
                        }
                        when {
                            data.machineEquipment=="0" -> adapter.mData[14].singleDisplayRightContent="不提供"
                            data.machineEquipment=="1" -> adapter.mData[14].singleDisplayRightContent="提供"
                            else->{ adapter.mData[14].singleDisplayRightContent=" "}
                        }
                        adapter.mData[16].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[17].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[18].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[19].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("listData1",data.requirementCarLists as Serializable) //车辆清册查看
                            mdata.putSerializable("listData2", data.requirementMembersLists as Serializable) //成员清册查看
                            mdata.putSerializable("RequirementSpanWoodenSupprt",data as Serializable)
                            mdata.putInt("type", Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }
                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //运维
            Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplayOperationAndMaintenance()
                val result = getRequirementRunningMaintain(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName }
                        adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                            " " } else{ data.projectSite}
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            " " } else{ data.projectTime}
                        if(data.requirementCarLists == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementCarLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    val listData = data.requirementCarLists
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putString("type","车辆清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.workerExperience==null) {
                            " " } else{ data.workerExperience}
                        adapter.mData[6].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                            " " } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                        when {
                            data.sexDemand=="0" -> adapter.mData[7].singleDisplayRightContent="女"
                            data.sexDemand=="1" -> adapter.mData[7].singleDisplayRightContent="男"
                            data.sexDemand=="-1" -> adapter.mData[7].singleDisplayRightContent="男女不限"
                            else -> { adapter.mData[7].singleDisplayRightContent=" "}
                        }
                        when {
                            data.roomBoardStandard=="0" -> adapter.mData[8].singleDisplayRightContent="自理"
                            data.roomBoardStandard=="1" -> adapter.mData[8].singleDisplayRightContent="全包"
                            else -> { adapter.mData[8].singleDisplayRightContent=" "}
                        }
                        adapter.mData[9].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            " " } else{ data.journeyCarFare}
                        adapter.mData[10].singleDisplayRightContent=if(data.journeySalary==null) {
                            " " } else{ data.journeySalary}
                        adapter.mData[11].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                            " " } else{ data.needPeopleNumber}
                        if(data.requirementMembersLists == null)
                        {
                            adapter.mData[12].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })//成员清册查看
                        }
                        else{
                            if(data.requirementMembersLists!!.isEmpty())
                            {
                                adapter.mData[12].buttonListener = listOf(View.OnClickListener {
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })//成员清册查看
                            }
                            else
                            {
                                adapter.mData[12].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    mdata.putSerializable("listData2", listData as Serializable)
                                    mdata.putString("type","成员清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))      })//乙方材料清册
                            }
                        }
                        adapter.mData[13].singleDisplayRightContent=if(data.vehicle==null) {
                            " " } else{ data.vehicle}
                        when {
                            data.machineEquipment=="0" -> adapter.mData[14].singleDisplayRightContent="不提供"
                            data.machineEquipment=="1" -> adapter.mData[14].singleDisplayRightContent="提供"
                            else->{ adapter.mData[14].singleDisplayRightContent=" "}
                        }
                        adapter.mData[15].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                            " " } else{ data.requirementConstructionWorkKind}
                        adapter.mData[16].singleDisplayRightContent=if(data.requirementTeamVoltageClasses==null) {
                            " " } else{ data.requirementTeamVoltageClasses}
                        adapter.mData[18].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[19].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[20].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[21].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("listData1",data.requirementCarLists as Serializable) //车辆清册查看
                            mdata.putSerializable("listData2", data.requirementMembersLists as Serializable) //成员清册查看
                            mdata.putSerializable("RequirementRunningMaintain",data as Serializable)
                            mdata.putInt("type", Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }
                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //车辆租赁
            Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplayVehicleLeasing()
                val result = getRequirementLeaseCar(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName }
                        adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                            " " } else{ data.projectSite}
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            " " } else{ data.projectTime}
                        if(data.requirementCarLists == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementCarLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    val listData = data.requirementCarLists
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putString("type","车辆清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.workerExperience==null) {
                            " " } else{ data.workerExperience}
                        when {
                            data.roomBoardStandard=="1" -> adapter.mData[6].singleDisplayRightContent="全包"
                            data.roomBoardStandard=="0" -> adapter.mData[6].singleDisplayRightContent="自理"
                            else -> { adapter.mData[6].singleDisplayRightContent=" " }
                        }
                        adapter.mData[7].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            " " } else{ data.journeyCarFare}
                        adapter.mData[8].singleDisplayRightContent=if(data.journeySalary==null) {
                            " " } else{ data.journeySalary}
                        if(data.salaryUnit=="面议"||data.salaryStandard=="-1.00"||data.salaryStandard==null){
                            adapter.mData[9].singleDisplayRightContent= "面议"
                        }else {
                            adapter.mData[9].singleDisplayRightContent= "${data.salaryStandard} ${data.salaryUnit}"
                        }
                        adapter.mData[11].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[12].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[13].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[14].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("listData1",data.requirementCarLists as Serializable) //车辆清册查看
                            mdata.putSerializable("RequirementLeaseCar",data as Serializable)
                            mdata.putInt("type", Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }

                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //工器具租赁
            Constants.FragmentType.TOOL_LEASING_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplayEquipmentLeasing()
                val result = getRequirementLeaseConstructionTool(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName }
                        adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                            " " } else{ data.projectSite}
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            " " } else{ data.projectTime}

                        if(data.requirementLeaseLists==null)
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //需求租赁清单
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementLeaseLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //需求租赁清单
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementLeaseLists
                                    mdata.putSerializable("listData4", listData as Serializable)
                                    mdata.putString("type","租赁清单查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        when {
                            data.financeTransportationInsurance==null -> adapter.mData[5].singleDisplayRightContent=" "
                            data.financeTransportationInsurance=="0" -> adapter.mData[5].singleDisplayRightContent="出租方承担"
                            data.financeTransportationInsurance=="1" -> adapter.mData[5].singleDisplayRightContent="租赁方承担"
                        }
                        when {
                            data.distribution==null -> adapter.mData[6].singleDisplayRightContent=" "
                            data.distribution=="0" -> adapter.mData[6].singleDisplayRightContent="出租方承担"
                            data.distribution=="1" -> adapter.mData[6].singleDisplayRightContent="租赁方承担"
                        }
                        when {
                            data.partnerAttribute==null -> adapter.mData[7].singleDisplayRightContent= " "
                            data.partnerAttribute=="0" -> adapter.mData[7].singleDisplayRightContent="单位"
                            data.partnerAttribute=="1" -> adapter.mData[7].singleDisplayRightContent="个人"
                        }
                        if(data.hireFareStandard==null) {
                            adapter.mData[8].singleDisplayRightContent=" " }
                        else if(data.hireFareStandard=="0"){ adapter.mData[8].singleDisplayRightContent="按清单报价"}
                        else if(data.hireFareStandard=="1"){ adapter.mData[8].singleDisplayRightContent="面议"}
                        adapter.mData[10].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[11].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[12].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[13].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("RequirementLeaseConstructionTool",data as Serializable)
                            if(data.requirementLeaseLists!=null)
                            {
                                mdata.putSerializable("listData4", data.requirementLeaseLists as Serializable)//租赁清单
                            }
                            mdata.putInt("type", Constants.FragmentType.TOOL_LEASING_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }

                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //设备租赁
            Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplayEquipmentLeasing()
                val result = getRequirementLeaseFacility(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName }
                        adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                            " " } else{ data.projectSite}
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            " " } else{ data.projectTime}
                        if(data.requirementLeaseLists==null)
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //需求租赁清单
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementLeaseLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //需求租赁清单
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementLeaseLists
                                    mdata.putSerializable("listData4", listData as Serializable)
                                    mdata.putString("type","租赁清单查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        when {
                            data.financeTransportationInsurance==null -> adapter.mData[5].singleDisplayRightContent=" "
                            data.financeTransportationInsurance=="0" -> adapter.mData[5].singleDisplayRightContent="出租方承担"
                            data.financeTransportationInsurance=="1" -> adapter.mData[5].singleDisplayRightContent="租赁方承担"
                        }
                        when {
                            data.distribution==null -> adapter.mData[6].singleDisplayRightContent=" "
                            data.distribution=="0" -> adapter.mData[6].singleDisplayRightContent="出租方承担"
                            data.distribution=="1" -> adapter.mData[6].singleDisplayRightContent="租赁方承担"
                        }
                        when {
                            data.partnerAttribute==null -> adapter.mData[7].singleDisplayRightContent= " "
                            data.partnerAttribute=="0" -> adapter.mData[7].singleDisplayRightContent="单位"
                            data.partnerAttribute=="1" -> adapter.mData[7].singleDisplayRightContent="个人"
                        }
                        if(data.hireFareStandard==null) {
                            adapter.mData[8].singleDisplayRightContent=" " }
                        else if(data.hireFareStandard=="0"){ adapter.mData[8].singleDisplayRightContent="按清单报价"}
                        else if(data.hireFareStandard=="1"){ adapter.mData[8].singleDisplayRightContent="面议"}
                        adapter.mData[10].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[11].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[12].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[13].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("RequirementLeaseFacility",data as Serializable)
                            if(data.requirementLeaseLists!=null)
                            {
                                mdata.putSerializable("listData4", data.requirementLeaseLists as Serializable)//租赁清单
                            }
                            mdata.putInt("type", Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }
                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //机械租赁
            Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplayEquipmentLeasing()
                val result = getRequirementLeaseMachinery(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            " " } else{ data.projectName }
                        adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                            " " } else{ data.projectSite}
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            " " } else{ data.projectTime}
                        if(data.requirementLeaseLists==null)
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //需求租赁清单
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementLeaseLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //需求租赁清单
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementLeaseLists
                                    mdata.putSerializable("listData4", listData as Serializable)
                                    mdata.putString("type","租赁清单查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        when {
                            data.financeTransportationInsurance==null -> adapter.mData[5].singleDisplayRightContent=" "
                            data.financeTransportationInsurance=="0" -> adapter.mData[5].singleDisplayRightContent="出租方承担"
                            data.financeTransportationInsurance=="1" -> adapter.mData[5].singleDisplayRightContent="租赁方承担"
                        }
                        when {
                            data.distribution==null -> adapter.mData[6].singleDisplayRightContent=" "
                            data.distribution=="0" -> adapter.mData[6].singleDisplayRightContent="出租方承担"
                            data.distribution=="1" -> adapter.mData[6].singleDisplayRightContent="租赁方承担"
                        }
                        when {
                            data.partnerAttribute==null -> adapter.mData[7].singleDisplayRightContent= " "
                            data.partnerAttribute=="0" -> adapter.mData[7].singleDisplayRightContent="单位"
                            data.partnerAttribute=="1" -> adapter.mData[7].singleDisplayRightContent="个人"
                        }
                        if(data.hireFareStandard==null) {
                            adapter.mData[8].singleDisplayRightContent=" " }
                        else if(data.hireFareStandard=="0"){ adapter.mData[8].singleDisplayRightContent="按清单报价"}
                        else if(data.hireFareStandard=="1"){ adapter.mData[8].singleDisplayRightContent="面议"}
                        adapter.mData[10].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[11].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[12].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[13].textAreaContent=if(data.additonalExplain==null) {
                            " " } else{ data.additonalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("RequirementLeaseMachinery",data as Serializable)

                            if(data.requirementLeaseLists!=null)
                            {
                                mdata.putSerializable("listData4", data.requirementLeaseLists as Serializable)//租赁清单
                            }
                            mdata.putInt("type", Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }
                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //需求三方 除资质合作
            Constants.FragmentType.TRIPARTITE_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplayDemandTripartite()
                val result = getRequirementThirdPartyDetail(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        if(data.thirdLists==null)
                        {
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener { //三方服务清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.thirdLists!!.isEmpty())
                            {
                                adapter.mData[1].buttonListener = listOf(View.OnClickListener { //三方服务清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[1].buttonListener = listOf(View.OnClickListener { //三方服务清册
                                    val listData = data.thirdLists
                                    mdata.putSerializable("listData5", listData as Serializable)
                                    mdata.putString("type","三方清册查看")
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        when {
                            data.partnerAttribute==null -> adapter.mData[2].singleDisplayRightContent=" "
                            data.partnerAttribute=="1" -> adapter.mData[2].singleDisplayRightContent="单位"
                            data.partnerAttribute=="0" -> adapter.mData[2].singleDisplayRightContent="个人"
                        }
                        when {
                            data.fareStandard==null -> adapter.mData[3].singleDisplayRightContent=" "
                            data.fareStandard=="1" -> adapter.mData[3].singleDisplayRightContent="面议"
                            data.fareStandard=="0" -> adapter.mData[3].singleDisplayRightContent="按清单报价"
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[6].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[7].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[8].textAreaContent=if(data.additionalExplain==null) {
                            " " } else{ data.additionalExplain}
                        view.button.setOnClickListener {
                            mdata.putSerializable("RequirementThirdPartyDetail",data as Serializable)
                            mdata.putSerializable("listData5", data.thirdLists as Serializable)//三方清册
                            mdata.putInt("type", Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal)
                            FragmentHelper.switchFragment(
                                activity as DemandDisplayActivity,
                                ApplicationSubmitFragment.newInstance(mdata),
                                R.id.frame_display_demand,
                                "register"
                            )
                        }

                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //需求三方 资质合作
            Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal->{
                adapter = adapterGenerate.demandTeamDisplayDemandTripartiteCooperation()
                val result = getRequirementThirdPartyDetail(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            " " } else{ data.requirementVariety }
                        when {
                            data.partnerAttribute==null -> adapter.mData[1].singleDisplayRightContent= " "
                            data.partnerAttribute=="1" -> adapter.mData[1].singleDisplayRightContent= "单位"
                            data.partnerAttribute=="1" -> adapter.mData[1].singleDisplayRightContent= "个人"
                        }
                        when {
                            data.qualificationUnitName==null -> adapter.mData[1].singleDisplayRightContent= " "
                            else -> adapter.mData[2].singleDisplayRightContent=  data.qualificationUnitName
                        }
                        adapter.mData[3].singleDisplayRightContent=if(data.cooperationRegion==null) {
                            " " } else{ data.cooperationRegion}
                        adapter.mData[5].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        adapter.mData[6].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[7].singleDisplayRightContent=if(data.qualificationRequirementt==null) {
                            " " } else{ data.qualificationRequirementt}
                        adapter.mData[8].submitListener = View.OnClickListener {
                            mdata.putInt("type",17)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            // (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.rv_demand_display_content.adapter = adapter
                        view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }

        }

    }


}