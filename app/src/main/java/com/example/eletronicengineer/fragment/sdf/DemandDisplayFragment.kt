package com.example.eletronicengineer.fragment.sdf

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.eletronicengineer.fragment.distribution.IntegralFragment
import com.example.eletronicengineer.model.ApiConfig
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
        view.tv_display_demand_back.setOnClickListener {
            activity!!.finish()
        }

        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = view.context
        adapterGenerate.activity = activity as AppCompatActivity
        lateinit var adapter: RecyclerviewAdapter
        when (type) {
            //需求个人显示模板
            1 -> {
              adapter = adapterGenerate.demandIndividualDisplay()
                val result = getRequirementPersonDetail(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                                "无" } else{ data.requirementVariety }
                            adapter.mData[1].singleDisplayRightContent=if(data.requirementMajor==null) {
                                "无" } else{ data.requirementMajor }
                                adapter.mData[2].singleDisplayRightContent=if(data.projectName==null) {
                                    "无" } else{ data.projectName}
                                if(data.projectSite==null)
                                {
                                    adapter.mData[3].singleDisplayRightContent= "无"
                                }
                                else
                                {
                                        var str=data.projectSite.split(" / ")
                                        var projectSite=""
                                        for(temp in str) projectSite+= "$temp "
                                        adapter.mData[3].singleDisplayRightContent=projectSite
                                }
                                adapter.mData[4].singleDisplayRightContent=if(data.planTime==null) {
                                    "无" } else{ data.planTime}
                                adapter.mData[5].singleDisplayRightContent=if(data.workerExperience==null) {
                                    "无" } else{ data.workerExperience}
                                adapter.mData[6].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                                    "无" } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                                when {
                                    data.sexDemand=="0" -> adapter.mData[7].singleDisplayRightContent="男"
                                    data.sexDemand=="1" -> adapter.mData[7].singleDisplayRightContent="女"
                                    data.sexDemand=="-1" -> adapter.mData[7].singleDisplayRightContent="男女不限"
                                    else -> { }
                                }
                                when {
                                    data.roomBoardStandard=="0" -> adapter.mData[8].singleDisplayRightContent="全包"
                                    data.roomBoardStandard=="1" -> adapter.mData[8].singleDisplayRightContent="队部自理"
                                    else -> { }
                                }
                                adapter.mData[9].singleDisplayRightContent=if(data.journeySalary==null) {
                                    "无" } else{ data.journeySalary}
                                adapter.mData[10].singleDisplayRightContent=if(data.journeyCarFare==null) {
                                    "无" } else{ data.journeyCarFare}
                                adapter.mData[11].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                                    "无" } else{ data.needPeopleNumber}
                                adapter.mData[12].singleDisplayRightContent=if(data.salaryStandard==null||data.salaryUnit==null) {
                                    "无" } else{ "${data.salaryStandard} ${data.salaryUnit}" }
                                adapter.mData[14].singleDisplayRightContent=if(data.name==null) {
                                    "无" } else{ data.name}
                                adapter.mData[15].singleDisplayRightContent=if(data.phone==null) {
                                    "无" } else{ data.phone}
                                adapter.mData[16].singleDisplayRightContent=if(data.validTime==null) {
                                    "无" } else{ data.validTime}
                                adapter.mData[17].singleDisplayRightContent=if(data.additonalExplain==null) {
                                    "无" } else{ data.additonalExplain}
                                adapter.mData[18].submitListener = View.OnClickListener {

//                                    mdata.putString("projectName",data.projectName)
//                                    mdata.putString("requirementVariety",data.requirementVariety)
//                                    mdata.putString("requirementMajor",data.requirementMajor)
//                                    mdata.putString("name",data.name)
//                                    mdata.putString("phone",data.phone)
                                    mdata.putString("needPeopleNumber",data.needPeopleNumber)
                                    mdata.putSerializable("RequirementPersonDetail",data)
                                   // mdata.putInt("type",1)
                                    mdata.putInt("type", Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal)
                                    (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment.newInstance(mdata), R.id.frame_display_demand, "register")
                                }
                            view.tv_fragment_demand_display_content.adapter = adapter
                            view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //需求主网
            2->{
                adapter = adapterGenerate.demandTeamDisplay()
                val result = getRequirementMajorNetWork(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                            var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                                "无" } else{ data.requirementVariety }
                            adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                                "无" } else{ data.projectName }
                            if(data.projectSite==null)
                                adapter.mData[2].singleDisplayRightContent= "无"
                            else
                            {
                                var str=data.projectSite.split(" / ")
                                var projectSite=""
                                for(temp in str) projectSite+= "$temp "
                                adapter.mData[2].singleDisplayRightContent=projectSite
                            }
                            adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                                "无" } else{ data.projectTime}
                        if(data.requirementTeamProjectList == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementTeamProjectList!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    val listData = data.requirementTeamProjectList
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putInt("type",1)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }

                             adapter.mData[5].singleDisplayRightContent=if(data.requirementTeamVoltageClasses==null) {
                                "无" } else{ data.requirementTeamVoltageClasses}
                            adapter.mData[6].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                                "无" } else{ data.requirementConstructionWorkKind}
                            adapter.mData[7].singleDisplayRightContent=if(data.workerExperience==null) {
                                "无" } else{ data.workerExperience}
                            adapter.mData[8].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                                "无" } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                            when {
                                data.sexDemand=="0" -> adapter.mData[9].singleDisplayRightContent="男"
                                data.sexDemand=="1" -> adapter.mData[9].singleDisplayRightContent="女"
                                data.sexDemand=="-1" -> adapter.mData[9].singleDisplayRightContent="男女不限"
                            }
                            when {
                                data.roomBoardStandard=="0" -> adapter.mData[10].singleDisplayRightContent="队部自理"
                                data.roomBoardStandard=="1" -> adapter.mData[10].singleDisplayRightContent="全包"
                            }
                            adapter.mData[11].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            "无" } else{ data.journeyCarFare}
                            adapter.mData[12].singleDisplayRightContent=if(data.journeySalary==null) {
                                "无" } else{ data.journeySalary}
                            adapter.mData[13].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                                "无" } else{ data.needPeopleNumber}
                            adapter.mData[14].singleDisplayRightContent=if(data.vehicle==null) {
                                "无" } else{ data.vehicle}
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
                        }
                            when {
                                 data.constructionEquipment=="0" -> adapter.mData[16].singleDisplayRightContent="不提供"
                                 data.constructionEquipment=="1" -> adapter.mData[16].singleDisplayRightContent="提供"
                                 }
                        if(data.requirementSecondProvideMaterialsList == null)
                        {
                            adapter.mData[17].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })//乙方材料清册
                        }
                        else{
                            if(data.requirementSecondProvideMaterialsList!!.isEmpty())
                            {
                                adapter.mData[17].buttonListener = listOf(View.OnClickListener {
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })//乙方材料清册
                            }
                            else
                            {
                                adapter.mData[17].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementSecondProvideMaterialsList
                                    mdata.putSerializable("listData3", listData as Serializable)
                                    mdata.putInt("type",4)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))      })//乙方材料清册
                            }
                             }
                            adapter.mData[19].singleDisplayRightContent=if(data.name==null) {
                                "无" } else{ data.name}
                            adapter.mData[20].singleDisplayRightContent=if(data.phone==null) {
                                "无" } else{ data.phone}
                            adapter.mData[21].singleDisplayRightContent=if(data.validTime==null) {
                                "无" } else{ data.validTime}
                            adapter.mData[22].singleDisplayRightContent=if(data.additonalExplain==null) {
                                "无" } else{ data.additonalExplain}
                            adapter.mData[23].submitListener = View.OnClickListener {

//                                mdata.putInt("type",2)
//                                mdata.putString("projectName",data.projectName)
//                                mdata.putString("requirementVariety",data.requirementVariety)
//                                mdata.putString("name",data.name)
//                                mdata.putString("phone",data.phone)
//                                mdata.putString("vehicle",data.vehicle)
//                                mdata.putString("needPeopleNumber",data.needPeopleNumber)
//                                mdata.putString("constructionEquipment",data.constructionEquipment)
                                // mdata.putInt("type",1)
                                //                                (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                                mdata.putSerializable("listData2", data.requirementPowerTransformationSalary as Serializable) //清工薪资
                                mdata.putSerializable("listData1",data.requirementTeamProjectList as Serializable) //清单报价
                                mdata.putSerializable("RequirementMajorNetWork",data as Serializable)
                                mdata.putInt("type", Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal)
                                (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment.newInstance(mdata), R.id.frame_display_demand, "register")
                            }
                            view.tv_fragment_demand_display_content.adapter = adapter
                            view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //配网
            3->{
                adapter = adapterGenerate.demandTeamDisplay()
                val result = getRequirementDistributionNetWork(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            "无" } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            "无" } else{ data.projectName }
                        if(data.projectSite==null)
                            adapter.mData[2].singleDisplayRightContent= "无"
                        else
                        {
                            var str=data.projectSite.split(" / ")
                            var projectSite=""
                            for(temp in str) projectSite+= "$temp "
                            adapter.mData[2].singleDisplayRightContent=projectSite
                        }
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            "无" } else{ data.projectTime}
                        if(data.requirementTeamProjectList == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementTeamProjectList!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    val listData = data.requirementTeamProjectList
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putInt("type",1)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.requirementTeamVoltageClasses==null) {
                            "无" } else{ data.requirementTeamVoltageClasses}
                        adapter.mData[6].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                            "无" } else{ data.requirementConstructionWorkKind}
                        adapter.mData[7].singleDisplayRightContent=if(data.workerExperience==null) {
                            "无" } else{ data.workerExperience}
                        adapter.mData[8].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                            "无" } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                        when {
                            data.sexDemand=="0" -> adapter.mData[9].singleDisplayRightContent="女"
                            data.sexDemand=="1" -> adapter.mData[9].singleDisplayRightContent="男"
                            data.sexDemand=="-1" -> adapter.mData[9].singleDisplayRightContent="男女不限"
                            else -> { }
                        }
                        when {
                            data.roomBoardStandard=="0" -> adapter.mData[10].singleDisplayRightContent="队部自理"
                            data.roomBoardStandard=="1" -> adapter.mData[10].singleDisplayRightContent="全包"
                            else -> { }
                        }
                        adapter.mData[11].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            "无" } else{ data.journeyCarFare}
                        adapter.mData[12].singleDisplayRightContent=if(data.journeySalary==null) {
                            "无" } else{ data.journeySalary}
                        adapter.mData[13].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                            "无" } else{ data.needPeopleNumber}
                        adapter.mData[14].singleDisplayRightContent=if(data.vehicle==null) {
                            "无" } else{ data.vehicle}
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
                        }
                        when {
                            data.constructionEquipment=="0" -> adapter.mData[16].singleDisplayRightContent="不提供"
                            data.constructionEquipment=="1" -> adapter.mData[16].singleDisplayRightContent="提供"
                        }
                        if(data.requirementSecondProvideMaterialsList == null)
                        {
                            adapter.mData[17].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })//乙方材料清册
                        }
                        else{
                            if(data.requirementSecondProvideMaterialsList!!.isEmpty())
                            {
                                adapter.mData[17].buttonListener = listOf(View.OnClickListener {
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })//乙方材料清册
                            }
                            else
                            {
                                adapter.mData[17].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementSecondProvideMaterialsList
                                    mdata.putSerializable("listData3", listData as Serializable)
                                    mdata.putInt("type",4)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))      })//乙方材料清册
                            }
                        }
                        adapter.mData[19].singleDisplayRightContent=if(data.name==null) {
                            "无" } else{ data.name}
                        adapter.mData[20].singleDisplayRightContent=if(data.phone==null) {
                            "无" } else{ data.phone}
                        adapter.mData[21].singleDisplayRightContent=if(data.validTime==null) {
                            "无" } else{ data.validTime}
                        adapter.mData[22].singleDisplayRightContent=if(data.additonalExplain==null) {
                            "无" } else{ data.additonalExplain}
                        adapter.mData[23].submitListener = View.OnClickListener {

                            mdata.putInt("type",3)
                            mdata.putString("projectName",data.projectName)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            mdata.putString("vehicle",data.vehicle)
                            mdata.putString("needPeopleNumber",data.needPeopleNumber)
                            mdata.putString("constructionEquipment",data.constructionEquipment)
                            mdata.putSerializable("listData2", data.requirementPowerTransformationSalary as Serializable) //清工薪资
                            mdata.putSerializable("listData1",data.requirementTeamProjectList as Serializable) //清单报价
                            //(activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //变电
            4->{
                adapter = adapterGenerate.demandTeamDisplay()
                val result = getRequirementPowerTransformation(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            "无" } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            "无" } else{ data.projectName }
                        if(data.projectSite==null)
                        {
                            adapter.mData[2].singleDisplayRightContent= "无"
                        }
                        else
                        {
                            var str=data.projectSite.split(" / ")
                            var projectSite=""
                            for(temp in str) projectSite+= "$temp "
                            adapter.mData[2].singleDisplayRightContent=projectSite
                        }
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            "无" } else{ data.projectTime}
                        if(data.requirementTeamProjectList == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementTeamProjectList!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    val listData = data.requirementTeamProjectList
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putInt("type",1)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.requirementTeamVoltageClasses==null) {
                            "无" } else{ data.requirementTeamVoltageClasses}
                        adapter.mData[6].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                            "无" } else{ data.requirementConstructionWorkKind}
                        adapter.mData[7].singleDisplayRightContent=if(data.workerExperience==null) {
                            "无" } else{ data.workerExperience}
                        adapter.mData[8].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                            "无" } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                        when {
                            data.sexDemand=="0" -> adapter.mData[9].singleDisplayRightContent="女"
                            data.sexDemand=="1" -> adapter.mData[9].singleDisplayRightContent="男"
                            data.sexDemand=="-1" -> adapter.mData[9].singleDisplayRightContent="男女不限"
                            else -> { }
                        }
                        when {
                            data.roomBoardStandard=="0" -> adapter.mData[10].singleDisplayRightContent="队部自理"
                            data.roomBoardStandard=="1" -> adapter.mData[10].singleDisplayRightContent="全包"
                            else -> { }
                        }
                        adapter.mData[11].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            "无" } else{ data.journeyCarFare}
                        adapter.mData[12].singleDisplayRightContent=if(data.journeySalary==null) {
                            "无" } else{ data.journeySalary}
                        adapter.mData[13].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                            "无" } else{ data.needPeopleNumber}
                        adapter.mData[14].singleDisplayRightContent=if(data.vehicle==null) {
                            "无" } else{ data.vehicle}
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
                        }
                        when {
                            data.constructionEquipment=="0" -> adapter.mData[16].singleDisplayRightContent="不提供"
                            data.constructionEquipment=="1" -> adapter.mData[16].singleDisplayRightContent="提供"
                        }
                        if(data.requirementSecondProvideMaterialsList == null)
                        {
                            adapter.mData[17].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })//乙方材料清册
                        }
                        else{
                            if(data.requirementSecondProvideMaterialsList!!.isEmpty())
                            {
                                adapter.mData[17].buttonListener = listOf(View.OnClickListener {
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })//乙方材料清册
                            }
                            else
                            {
                                adapter.mData[17].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementSecondProvideMaterialsList
                                    mdata.putSerializable("listData3", listData as Serializable)
                                    mdata.putInt("type",4)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))      })//乙方材料清册
                            }
                        }
                        adapter.mData[19].singleDisplayRightContent=if(data.name==null) {
                            "无" } else{ data.name}
                        adapter.mData[20].singleDisplayRightContent=if(data.phone==null) {
                            "无" } else{ data.phone}
                        adapter.mData[21].singleDisplayRightContent=if(data.validTime==null) {
                            "无" } else{ data.validTime}
                        adapter.mData[22].singleDisplayRightContent=if(data.additonalExplain==null) {
                            "无" } else{ data.additonalExplain}
                        adapter.mData[23].submitListener = View.OnClickListener {

//                            mdata.putInt("type",4)
//                            mdata.putString("projectName",data.projectName)
//                            mdata.putString("requirementVariety",data.requirementVariety)
//                            mdata.putString("name",data.name)
//                            mdata.putString("phone",data.phone)
//                            mdata.putString("vehicle",data.vehicle)
//                            mdata.putString("needPeopleNumber",data.needPeopleNumber)
//                            mdata.putString("constructionEquipment",data.constructionEquipment)
//                            mdata.putSerializable("listData2", data.requirementPowerTransformationSalary as Serializable) //清工薪资
//                            mdata.putSerializable("listData1",data.requirementTeamProjectList as Serializable) //清单报价
                            //(activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                            mdata.putSerializable("listData2", data.requirementPowerTransformationSalary as Serializable) //清工薪资
                            mdata.putSerializable("listData1",data.requirementTeamProjectList as Serializable) //清单报价
                            mdata.putSerializable("RequirementPowerTransformation",data as Serializable)
                            mdata.putInt("type", Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal)
                            (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment.newInstance(mdata), R.id.frame_display_demand, "register")
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //测量设计
            5->{
                adapter = adapterGenerate.demandTeamDisplay()
                val result = getRequirementMeasureDesign(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                      var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                                "无" } else{ data.requirementVariety }
                            adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                                "无" } else{ data.projectName }
                            if(data.projectSite==null)
                            {
                                adapter.mData[2].singleDisplayRightContent= "无"
                            }
                            else
                            {
                                var str=data.projectSite.split(" / ")
                                var projectSite=""
                                for(temp in str) projectSite+= "$temp "
                                adapter.mData[2].singleDisplayRightContent=projectSite
                            }
                            adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                                "无" } else{ data.projectTime}
                        if(data.requirementTeamProjectList == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementTeamProjectList!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    val listData = data.requirementTeamProjectList
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putInt("type",1)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                            adapter.mData[5].singleDisplayRightContent=if(data.requirementTeamVoltageClasses==null) {
                                "无" } else{ data.requirementTeamVoltageClasses}
                            adapter.mData[6].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                                "无" } else{ data.requirementConstructionWorkKind}
                            adapter.mData[7].singleDisplayRightContent=if(data.workerExperience==null) {
                                "无" } else{ data.workerExperience}
                            adapter.mData[8].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                                "无" } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                            when {
                                data.sexDemand=="0" -> adapter.mData[9].singleDisplayRightContent="女"
                                data.sexDemand=="1" -> adapter.mData[9].singleDisplayRightContent="男"
                                data.sexDemand=="-1" -> adapter.mData[9].singleDisplayRightContent="男女不限"
                                else -> { }
                            }
                            when {
                                data.roomBoardStandard=="0" -> adapter.mData[10].singleDisplayRightContent="队部自理"
                                data.roomBoardStandard=="1" -> adapter.mData[10].singleDisplayRightContent="全包"
                                else -> { }
                            }
                            adapter.mData[11].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            "无" } else{ data.journeyCarFare}
                            adapter.mData[12].singleDisplayRightContent=if(data.journeySalary==null) {
                                "无" } else{ data.journeySalary}
                            adapter.mData[13].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                                "无" } else{ data.needPeopleNumber}
                            adapter.mData[14].singleDisplayRightContent=if(data.salaryStandard==null) {
                                "无" } else{ data.salaryStandard }
                            adapter.mData[15].singleDisplayRightContent=if(data.vehicle==null) {
                                "无" } else{ data.vehicle}
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
                        }
                        when {
                            data.equipment=="0" -> adapter.mData[16].singleDisplayRightContent="不提供"
                            data.equipment=="1" -> adapter.mData[16].singleDisplayRightContent="提供"
                        }
//                        if(data.requirementSecondProvideMaterialsList == null)
//                        {
                            adapter.mData[17].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })//乙方材料清册
//                        }
//                        else{
//                            if(data.requirementSecondProvideMaterialsList!!.isEmpty())
//                            {
//                                adapter.mData[17].buttonListener = listOf(View.OnClickListener {
//                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
//                                })//乙方材料清册
//                            }
//                            else
//                            {
//                                adapter.mData[17].buttonListener = listOf(View.OnClickListener {
//                                    val listData = data.requirementSecondProvideMaterialsList
//                                    mdata.putSerializable("listData3", listData as Serializable)
//                                    mdata.putInt("type",4)
//                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))      })//乙方材料清册
//                            }
//                        }
                        adapter.mData[19].singleDisplayRightContent=if(data.name==null) {
                            "无" } else{ data.name}
                        adapter.mData[20].singleDisplayRightContent=if(data.phone==null) {
                            "无" } else{ data.phone}
                        adapter.mData[21].singleDisplayRightContent=if(data.validTime==null) {
                            "无" } else{ data.validTime}
                        adapter.mData[22].singleDisplayRightContent=if(data.additonalDxplain==null) {
                            "无" } else{ data.additonalDxplain}
                        adapter.mData[23].submitListener = View.OnClickListener {

                            mdata.putInt("type",5)
                            mdata.putString("projectName",data.projectName)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            mdata.putString("vehicle",data.vehicle)
                            mdata.putString("needPeopleNumber",data.needPeopleNumber)
                            mdata.putSerializable("listData2", data.requirementPowerTransformationSalary as Serializable) //清工薪资
                            mdata.putSerializable("listData1",data.requirementTeamProjectList as Serializable) //清单报价
                           // (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //马帮运输
            6->{
                adapter = adapterGenerate.demandTeamDisplayGongTrans()
                val result = getRequirementCaravanTransport(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                      var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                                "无" } else{ data.requirementVariety }
                            adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                                "无" } else{ data.projectName }
                            adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                                "无" } else{ data.projectSite}
                            adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                                "无" } else{ data.projectTime}
                        if(data.requirementTeamProjectList == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementTeamProjectList!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    val listData = data.requirementTeamProjectList
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putInt("type",1)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                            adapter.mData[5].singleDisplayRightContent=if(data.materialsType==null) {
                                "无" } else{ data.materialsType}
                            adapter.mData[6].singleDisplayRightContent=if(data.workerExperience==null) {
                                "无" } else{ data.workerExperience}
                            adapter.mData[7].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                                "无" } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                            when {
                                data.sexDemand=="0" -> adapter.mData[8].singleDisplayRightContent="女"
                                data.sexDemand=="1" -> adapter.mData[8].singleDisplayRightContent="男"
                                data.sexDemand=="-1" -> adapter.mData[8].singleDisplayRightContent="男女不限"
                                else -> { }
                            }
                            when {
                                data.roomBoardStandard=="0" -> adapter.mData[9].singleDisplayRightContent="队部自理"
                                data.roomBoardStandard=="1" -> adapter.mData[9].singleDisplayRightContent="全包"
                                else -> { }
                            }
                            adapter.mData[10].singleDisplayRightContent=if(data.journeySalary==null) {
                                "无" } else{ data.journeySalary}
                            adapter.mData[11].singleDisplayRightContent=if(data.journeyCarFare==null) {
                                "无" } else{ data.journeyCarFare}
                            adapter.mData[12].singleDisplayRightContent=if(data.needHorseNumber==null) {
                                "无" } else{ data.needHorseNumber}
                        if(data.requirementListQuotations == null )
                        {
                            adapter.mData[13].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            adapter.mData[13].buttonListener = listOf(View.OnClickListener {
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
                        }
                            adapter.mData[15].singleDisplayRightContent=if(data.name==null) {
                                "无" } else{ data.name}
                            adapter.mData[16].singleDisplayRightContent=if(data.phone==null) {
                                "无" } else{ data.phone}
                            adapter.mData[17].singleDisplayRightContent=if(data.validTime==null) {
                                "无" } else{ data.validTime}
                            adapter.mData[18].singleDisplayRightContent=if(data.additonalExplain==null) {
                                "无" } else{ data.additonalExplain}
                        adapter.mData[19].submitListener = View.OnClickListener {

                            mdata.putInt("type",6)
                            mdata.putString("projectName",data.projectName)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            mdata.putSerializable("listData1",data.requirementTeamProjectList as Serializable)
                            //(activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //桩机服务
            7->{
                adapter = adapterGenerate.demandTeamDisplayPile()
                val result = getRequirementPileFoundation(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                      var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                                "无" } else{ data.requirementVariety }
                            adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                                "无" } else{ data.projectName }
                            adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                                "无" } else{ data.projectSite}
                            adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                                "无" } else{ data.projectTime}
                        if(data.requirementTeamProjectList == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementTeamProjectList!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    val listData = data.requirementTeamProjectList
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putInt("type",1)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                            adapter.mData[5].singleDisplayRightContent=if(data.workerExperience==null) {
                                "无" } else{ data.workerExperience}
                            adapter.mData[6].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                                "无" } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                            when {
                                data.sexDemand=="0" -> adapter.mData[7].singleDisplayRightContent="女"
                                data.sexDemand=="1" -> adapter.mData[7].singleDisplayRightContent="男"
                                data.sexDemand=="-1" -> adapter.mData[7].singleDisplayRightContent="男女不限"
                                else -> { }
                            }
                            adapter.mData[8].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                                "无" } else{ data.requirementConstructionWorkKind}
                           adapter.mData[9].singleDisplayRightContent=if(data.roleMaxType==null) {
                            "无" } else{ data.roleMaxType}
                            when {
                                data.roomBoardStandard=="0" -> adapter.mData[10].singleDisplayRightContent="队部自理"
                                data.roomBoardStandard=="1" -> adapter.mData[10].singleDisplayRightContent="全包"
                                else -> { }
                            }
                            adapter.mData[11].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            "无" } else{ data.journeyCarFare}
                            adapter.mData[12].singleDisplayRightContent=if(data.journeySalary==null) {
                                "无" } else{ data.journeySalary}
                            adapter.mData[13].singleDisplayRightContent=if(data.needPileFoundationEquipment==null) {
                                "无" } else{ data.needPileFoundationEquipment}
                            adapter.mData[14].singleDisplayRightContent=if(data.vehicle==null) {
                                "无" } else{ data.vehicle }
                        if(data.requirementListQuotations == null )
                        {
                            adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            adapter.mData[15].buttonListener = listOf(View.OnClickListener {
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
                        }
                        when {
                            data.otherMachineEquipment=="0" -> adapter.mData[16].singleDisplayRightContent="不提供"
                            data.otherMachineEquipment=="1" -> adapter.mData[16].singleDisplayRightContent="提供"
                        }
                            adapter.mData[18].singleDisplayRightContent=if(data.name==null) {
                                "无" } else{ data.name}
                            adapter.mData[19].singleDisplayRightContent=if(data.phone==null) {
                                "无" } else{ data.phone}
                            adapter.mData[20].singleDisplayRightContent=if(data.validTime==null) {
                                "无" } else{ data.validTime}
                            adapter.mData[21].singleDisplayRightContent=if(data.additonalExplain==null) {
                                "无" } else{ data.additonalExplain}
                        adapter.mData[22].submitListener = View.OnClickListener {

                            mdata.putInt("type",7)
                            mdata.putString("projectName",data.projectName)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            mdata.putString("vehicle",data.vehicle)
                            mdata.putSerializable("listData1",data.requirementTeamProjectList as Serializable) //清单报价
                           // (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //非开挖
            8->{
                adapter = adapterGenerate.demandTeamDisplayTrenchiless()
                val result = getRequirementUnexcavation(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                      var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                                "无" } else{ data.requirementVariety }
                            adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                                "无" } else{ data.projectName }
                            adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                                "无" } else{ data.projectSite}
                            adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                                "无" } else{ data.projectTime}
                        if(data.requirementTeamProjectList == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementTeamProjectList!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    val listData = data.requirementTeamProjectList
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putInt("type",1)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                            adapter.mData[5].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                                "无" } else{ data.requirementConstructionWorkKind}
                            adapter.mData[6].singleDisplayRightContent=if(data.diameterStandardBranchesNumber==null || data.holeStandardBranchesNumber==null) {
                                "无" } else{ "单管直径${data.diameterStandardBranchesNumber}毫米(mm)×${data.holeStandardBranchesNumber}孔"}
                            adapter.mData[7].singleDisplayRightContent=if(data.workerExperience==null) {
                                "无" } else{ data.workerExperience}
                            adapter.mData[8].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                                "无" } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                            when {
                                data.sexDemand=="0" -> adapter.mData[9].singleDisplayRightContent="女"
                                data.sexDemand=="1" -> adapter.mData[9].singleDisplayRightContent="男"
                                data.sexDemand=="-1" -> adapter.mData[9].singleDisplayRightContent="男女不限"
                                else -> { }
                            }
                            when {
                                data.roomBoardStandard=="0" -> adapter.mData[10].singleDisplayRightContent="队部自理"
                                data.roomBoardStandard=="1" -> adapter.mData[10].singleDisplayRightContent="全包"
                                else -> { }
                            }
                            adapter.mData[11].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            "无" } else{ data.journeyCarFare}
                            adapter.mData[12].singleDisplayRightContent=if(data.journeySalary==null) {
                                "无" } else{ data.journeySalary}
                            adapter.mData[13].singleDisplayRightContent=if(data.needPileFoundation==null) {
                                "无" } else{ data.needPileFoundation}
                        if(data.requirementListQuotations == null )
                        {
                            adapter.mData[14].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            adapter.mData[14].buttonListener = listOf(View.OnClickListener {
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
                        }
                            adapter.mData[15].singleDisplayRightContent=if(data.vehicle==null) {
                                "无" } else{ data.vehicle}
                             when {
                                   data.otherMachineEquipment=="0" -> adapter.mData[16].singleDisplayRightContent="不提供"
                                   data.otherMachineEquipment=="1" -> adapter.mData[16].singleDisplayRightContent="提供"
                                  }
                            adapter.mData[18].singleDisplayRightContent=if(data.name==null) {
                                "无" } else{ data.name}
                            adapter.mData[19].singleDisplayRightContent=if(data.phone==null) {
                                "无" } else{ data.phone}
                            adapter.mData[20].singleDisplayRightContent=if(data.validTime==null) {
                                "无" } else{ data.validTime}
                            adapter.mData[21].singleDisplayRightContent=if(data.additonalExplain==null) {
                                "无" } else{ data.additonalExplain}
                        adapter.mData[22].submitListener = View.OnClickListener {

                            mdata.putInt("type",8)
                            mdata.putString("projectName",data.projectName)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            mdata.putString("vehicle",data.vehicle)
                            mdata.putString("constructionEquipment",data.otherMachineEquipment)
                            mdata.putSerializable("listData1",data.requirementTeamProjectList as Serializable) //清单报价
                           // (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //试验调试
            9->{
                adapter = adapterGenerate.demandTeamDisplayTestAndDebugging()
                val result = getRequirementTest(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                      var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                                "无" } else{ data.requirementVariety }
                            adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                                "无" } else{ data.projectName }
                            adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                                "无" } else{ data.projectSite}
                            adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                                "无" } else{ data.projectTime}
                        if(data.requirementTeamProjectList == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            if(data.requirementTeamProjectList!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    val listData = data.requirementTeamProjectList
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putInt("type",1)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                            adapter.mData[5].singleDisplayRightContent=if(data.requirementTeamVoltageClasses==null) {
                                "无" } else{ data.requirementTeamVoltageClasses}
                            adapter.mData[6].singleDisplayRightContent=if(data.requirementConstructionWorkKind==null) {
                                "无" } else{ data.requirementConstructionWorkKind}
                            adapter.mData[7].singleDisplayRightContent=if(data.operationClasses==null) {
                                "无" } else{ data.operationClasses}
                            adapter.mData[8].singleDisplayRightContent=if(data.workerExperience==null) {
                                "无" } else{ data.workerExperience}
                            adapter.mData[9].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                                "无" } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                            when {
                                data.sexDemand=="0" -> adapter.mData[10].singleDisplayRightContent="女"
                                data.sexDemand=="1" -> adapter.mData[10].singleDisplayRightContent="男"
                                data.sexDemand=="-1" -> adapter.mData[10].singleDisplayRightContent="男女不限"
                                else -> { }
                            }
                            when {
                                data.roomBoardStandard=="0" -> adapter.mData[11].singleDisplayRightContent="队部自理"
                                data.roomBoardStandard=="1" -> adapter.mData[11].singleDisplayRightContent="全包"
                                else -> { }
                            }
                            adapter.mData[12].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            "无" } else{ data.journeyCarFare}
                            adapter.mData[13].singleDisplayRightContent=if(data.journeySalary==null) {
                                "无" } else{ data.journeySalary}
                            adapter.mData[14].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                                "无" } else{ data.needPeopleNumber}
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
                        }
                            adapter.mData[16].singleDisplayRightContent=if(data.vehicle==null) {
                                "无" } else{ data.vehicle}
                        when {
                            data.machineEquipment=="0" -> adapter.mData[17].singleDisplayRightContent="不提供"
                            data.machineEquipment=="1" -> adapter.mData[17].singleDisplayRightContent="提供"
                        }
                            adapter.mData[19].singleDisplayRightContent=if(data.name==null) {
                                "无" } else{ data.name}
                            adapter.mData[20].singleDisplayRightContent=if(data.phone==null) {
                                "无" } else{ data.phone}
                            adapter.mData[21].singleDisplayRightContent=if(data.validTime==null) {
                                "无" } else{ data.validTime}
                            adapter.mData[22].singleDisplayRightContent=if(data.additonalExplain==null) {
                                "无" } else{ data.additonalExplain}
                        adapter.mData[23].submitListener = View.OnClickListener {

                            mdata.putInt("type",9)
                            mdata.putString("projectName",data.projectName)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            mdata.putString("vehicle",data.vehicle)
                            mdata.putString("needPeopleNumber",data.needPeopleNumber)
                            mdata.putString("constructionEquipment",data.machineEquipment)
                            mdata.putSerializable("listData2", data.requirementTeamProjectList as Serializable)
                            mdata.putSerializable("listData1",data.requirementTeamProjectList as Serializable) //清单报价
                          //  (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //跨越架
            10->{
                adapter = adapterGenerate.demandTeamDisplayCrossFrame()
                val result = getRequirementSpanWoodenSupprt(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                      var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                                "无" } else{ data.requirementVariety }
                            adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                                "无" } else{ data.projectName }
                            adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                                "无" } else{ data.projectSite}
                            adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                                "无" } else{ data.projectTime}
                            if(data.requirementTeamProjectList == null )
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                if(data.requirementTeamProjectList!!.isEmpty())
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                        Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                        val listData = data.requirementTeamProjectList
                                        mdata.putSerializable("listData1", listData as Serializable)
                                        mdata.putInt("type",1)
                                        (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                    })
                                }
                            }
                            adapter.mData[5].singleDisplayRightContent=if(data.spanShelfMaterial==null) {
                                "无" } else{ data.spanShelfMaterial}
                            adapter.mData[6].singleDisplayRightContent=if(data.workerExperience==null) {
                                "无" } else{ data.workerExperience}
                            adapter.mData[7].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                                "无" } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                            when {
                                data.sexDemand=="0" -> adapter.mData[8].singleDisplayRightContent="女"
                                data.sexDemand=="1" -> adapter.mData[8].singleDisplayRightContent="男"
                                data.sexDemand=="-1" -> adapter.mData[8].singleDisplayRightContent="男女不限"
                                else -> { }
                            }
                            when {
                                data.roomBoardStandard=="0" -> adapter.mData[9].singleDisplayRightContent="队部自理"
                                data.roomBoardStandard=="1" -> adapter.mData[9].singleDisplayRightContent="全包"
                                else -> { }
                            }
                            adapter.mData[10].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            "无" } else{ data.journeyCarFare}
                            adapter.mData[11].singleDisplayRightContent=if(data.journeySalary==null) {
                                "无" } else{ data.journeySalary}
                            adapter.mData[12].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                                "无" } else{ data.needPeopleNumber}
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
                            else -> adapter.mData[13].buttonListener = listOf(
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
                        }
                            adapter.mData[14].singleDisplayRightContent=if(data.vehicle==null) {
                                "无" } else{ data.vehicle}
                        when {
                            data.machineEquipment=="0" -> adapter.mData[15].singleDisplayRightContent="不提供"
                            data.machineEquipment=="1" -> adapter.mData[15].singleDisplayRightContent="提供"
                        }
                            adapter.mData[17].singleDisplayRightContent=if(data.name==null) {
                                "无" } else{ data.name}
                            adapter.mData[18].singleDisplayRightContent=if(data.phone==null) {
                                "无" } else{ data.phone}
                            adapter.mData[19].singleDisplayRightContent=if(data.validTime==null) {
                                "无" } else{ data.validTime}
                            adapter.mData[20].singleDisplayRightContent=if(data.additonalExplain==null) {
                                "无" } else{ data.additonalExplain}
                            adapter.mData[21].submitListener = View.OnClickListener {

                            mdata.putInt("type",10)
                            mdata.putString("projectName",data.projectName)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            mdata.putString("vehicle",data.vehicle)
                            mdata.putString("needPeopleNumber",data.needPeopleNumber)
                            mdata.putString("constructionEquipment",data.machineEquipment)
                                mdata.putSerializable("listData2", data.requirementTeamProjectList as Serializable)
                                mdata.putSerializable("listData1",data.requirementTeamProjectList as Serializable) //清单报价
                            //(activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //运维
            11->{
                adapter = adapterGenerate.demandTeamDisplayOperationAndMaintenance()
                val result = getRequirementRunningMaintain(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                      var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                                "无" } else{ data.requirementVariety }
                            adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                                "无" } else{ data.projectName }
                            adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                                "无" } else{ data.projectSite}
                            adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                                "无" } else{ data.projectTime}
                            if(data.requirementTeamProjectList == null )
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                    Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })
                            }
                            else
                            {
                                if(data.requirementTeamProjectList!!.isEmpty())
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                        Toast.makeText(view.context, "没有数据", Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
                                        val listData = data.requirementTeamProjectList
                                        mdata.putSerializable("listData1", listData as Serializable)
                                        mdata.putInt("type",1)
                                        (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                    })
                                }
                            }
                            adapter.mData[5].singleDisplayRightContent=if(data.workerExperience==null) {
                                "无" } else{ data.workerExperience}
                            adapter.mData[6].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                                "无" } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                            when {
                                data.sexDemand=="0" -> adapter.mData[7].singleDisplayRightContent="女"
                                data.sexDemand=="1" -> adapter.mData[7].singleDisplayRightContent="男"
                                data.sexDemand=="-1" -> adapter.mData[7].singleDisplayRightContent="男女不限"
                                else -> { }
                            }
                            when {
                                data.roomBoardStandard=="0" -> adapter.mData[8].singleDisplayRightContent="队部自理"
                                data.roomBoardStandard=="1" -> adapter.mData[8].singleDisplayRightContent="全包"
                                else -> { }
                            }
                            adapter.mData[9].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            "无" } else{ data.journeyCarFare}
                            adapter.mData[10].singleDisplayRightContent=if(data.journeySalary==null) {
                                "无" } else{ data.journeySalary}
                            adapter.mData[11].singleDisplayRightContent=if(data.needPeopleNumber==null) {
                                "无" } else{ data.needPeopleNumber}
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
                            else -> adapter.mData[12].buttonListener = listOf(
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
                        }
                            adapter.mData[13].singleDisplayRightContent=if(data.vehicle==null) {
                                "无" } else{ data.vehicle}
                        when {
                            data.machineEquipment=="0" -> adapter.mData[14].singleDisplayRightContent="不提供"
                            data.machineEquipment=="1" -> adapter.mData[14].singleDisplayRightContent="提供"
                        }
                            adapter.mData[16].singleDisplayRightContent=if(data.name==null) {
                                "无" } else{ data.name}
                            adapter.mData[17].singleDisplayRightContent=if(data.phone==null) {
                                "无" } else{ data.phone}
                            adapter.mData[18].singleDisplayRightContent=if(data.validTime==null) {
                                "无" } else{ data.validTime}
                            adapter.mData[19].singleDisplayRightContent=if(data.additonalExplain==null) {
                                "无" } else{ data.additonalExplain}

                        adapter.mData[20].submitListener = View.OnClickListener {

                            mdata.putInt("type",11)
                            mdata.putString("projectName",data.projectName)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            mdata.putString("vehicle",data.vehicle)
                            mdata.putString("needPeopleNumber",data.needPeopleNumber)
                            mdata.putString("constructionEquipment",data.machineEquipment)
                            mdata.putSerializable("listData2", data.requirementTeamProjectList as Serializable)
                            mdata.putSerializable("listData1",data.requirementTeamProjectList as Serializable) //清单报价
                          //  (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //车辆租赁
            12->{
                adapter = adapterGenerate.demandTeamDisplayVehicleLeasing()
                val result = getRequirementLeaseCar(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                      var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                                "无" } else{ data.requirementVariety }
                            adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                                "无" } else{ data.projectName }
                            adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                                "无" } else{ data.projectSite}
                            adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                                "无" } else{ data.projectTime}
                            adapter.mData[4].singleDisplayRightContent=if(data.vehicleType==null) {
                                "无" } else{ data.vehicleType}
                            adapter.mData[5].singleDisplayRightContent=if(data.accurateLoadWeight==null) {
                                "无" } else{ data.accurateLoadWeight}
                            adapter.mData[6].singleDisplayRightContent=if(data.vehicleStructure==null) {
                                "无" } else{ data.vehicleStructure}
                            adapter.mData[7].singleDisplayRightContent=if(data.theCarriageLength==null) {
                                "无" } else{ data.theCarriageLength}
                        when {
                            data.insuranceCondition==null -> adapter.mData[8].singleDisplayRightContent= "无"
                            data.insuranceCondition=="1" -> adapter.mData[8].singleDisplayRightContent="脱保"
                            data.insuranceCondition=="0" -> adapter.mData[8].singleDisplayRightContent="在保"
                        }
                        when {
                            data.driverSex==null -> adapter.mData[9].singleDisplayRightContent= "无"
                            data.driverSex=="1" -> adapter.mData[9].singleDisplayRightContent="女"
                            data.driverSex=="0" -> adapter.mData[9].singleDisplayRightContent="男"
                            data.driverSex=="-1" -> adapter.mData[9].singleDisplayRightContent="男女不限"
                        }
                            adapter.mData[10].singleDisplayRightContent=if(data.workerExperience==null) {
                                "无" } else{ data.workerExperience}
                            adapter.mData[11].singleDisplayRightContent=if(data.minAgeDemand==null||data.maxAgeDemand==null) {
                                "无" } else{  "${data.minAgeDemand}~${data.maxAgeDemand}"}
                            when {
                                data.roomBoardStandard=="0" -> adapter.mData[12].singleDisplayRightContent="全包"
                                data.roomBoardStandard=="1" -> adapter.mData[12].singleDisplayRightContent="队部自理"
                                else -> { }
                            }
                            adapter.mData[14].singleDisplayRightContent=if(data.journeyCarFare==null) {
                            "无" } else{ data.journeyCarFare}
                            adapter.mData[13].singleDisplayRightContent=if(data.journeySalary==null) {
                                "无" } else{ data.journeySalary}
                            adapter.mData[15].singleDisplayRightContent=if(data.salaryStandard==null||data.salaryUnit==null) {
                                "无" } else{ "${data.salaryStandard} ${data.salaryUnit}" }
                            adapter.mData[16].singleDisplayRightContent=if(data.vehicle==null) {
                                "无" } else{ data.vehicle}
                            when {
                                data.machineEquipment=="0" -> adapter.mData[17].singleDisplayRightContent="不提供"
                                data.machineEquipment=="1" -> adapter.mData[17].singleDisplayRightContent="提供"
                            }

                            adapter.mData[19].singleDisplayRightContent=if(data.name==null) {
                                "无" } else{ data.name}
                            adapter.mData[20].singleDisplayRightContent=if(data.phone==null) {
                                "无" } else{ data.phone}
                            adapter.mData[21].singleDisplayRightContent=if(data.validTime==null) {
                                "无" } else{ data.validTime}
                            adapter.mData[22].singleDisplayRightContent=if(data.additonalExplain==null) {
                                "无" } else{ data.additonalExplain}
                        adapter.mData[23].submitListener = View.OnClickListener {

                            mdata.putInt("type",12)
                            mdata.putString("projectName",data.projectName)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            mdata.putString("vehicle",data.vehicle)
                            mdata.putString("vehicleType", data.vehicleType)
                           // (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //工器具租赁
            13->{
                adapter = adapterGenerate.demandTeamDisplayEquipmentLeasing()
                val result = getRequirementLeaseConstructionTool(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                      var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                                "无" } else{ data.requirementVariety }
                            adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                                "无" } else{ data.projectName }
                            adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                                "无" } else{ data.projectSite}
                            adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                                "无" } else{ data.projectTime}

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
                                    mdata.putInt("type",5)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        when {
                            data.financeTransportationInsurance==null -> adapter.mData[5].singleDisplayRightContent="无"
                            data.financeTransportationInsurance=="0" -> adapter.mData[5].singleDisplayRightContent="出租方承担"
                            data.financeTransportationInsurance=="1" -> adapter.mData[5].singleDisplayRightContent="租赁方承担"
                        }
                        when {
                            data.distribution==null -> adapter.mData[6].singleDisplayRightContent="无"
                            data.distribution=="0" -> adapter.mData[6].singleDisplayRightContent="出租方承担"
                            data.distribution=="1" -> adapter.mData[6].singleDisplayRightContent="租赁方承担"
                        }
                        when {
                            data.partnerAttribute==null -> adapter.mData[7].singleDisplayRightContent= "无"
                            data.partnerAttribute=="0" -> adapter.mData[7].singleDisplayRightContent="单位"
                            data.partnerAttribute=="1" -> adapter.mData[7].singleDisplayRightContent="个人"
                        }
                            if(data.hireFareStandard==null) {
                                adapter.mData[8].singleDisplayRightContent="无" }
                            else if(data.hireFareStandard=="0"){ adapter.mData[8].singleDisplayRightContent="按清单报价"}
                            else if(data.hireFareStandard=="1"){ adapter.mData[8].singleDisplayRightContent="面议"}
                            adapter.mData[10].singleDisplayRightContent=if(data.name==null) {
                            "无" } else{ data.name}
                            adapter.mData[11].singleDisplayRightContent=if(data.phone==null) {
                                "无" } else{ data.phone}
                            adapter.mData[12].singleDisplayRightContent=if(data.validTime==null) {
                                "无" } else{ data.validTime}
                            adapter.mData[13].singleDisplayRightContent=if(data.additonalExplain==null) {
                                "无" } else{ data.additonalExplain}
                        adapter.mData[14].submitListener = View.OnClickListener {
                            mdata.putInt("type",13)
                            mdata.putString("projectName",data.projectName)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            mdata.putSerializable("listData4", data.requirementLeaseLists as Serializable)//租赁清单
                           // (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //设备租赁
            14->{
                adapter = adapterGenerate.demandTeamDisplayEquipmentLeasing()
                val result = getRequirementLeaseFacility(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            "无" } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            "无" } else{ data.projectName }
                        adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                            "无" } else{ data.projectSite}
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            "无" } else{ data.projectTime}
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
                                    mdata.putInt("type",5)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        when {
                            data.financeTransportationInsurance==null -> adapter.mData[5].singleDisplayRightContent="无"
                            data.financeTransportationInsurance=="0" -> adapter.mData[5].singleDisplayRightContent="出租方承担"
                            data.financeTransportationInsurance=="1" -> adapter.mData[5].singleDisplayRightContent="租赁方承担"
                        }
                        when {
                            data.distribution==null -> adapter.mData[6].singleDisplayRightContent="无"
                            data.distribution=="0" -> adapter.mData[6].singleDisplayRightContent="出租方承担"
                            data.distribution=="1" -> adapter.mData[6].singleDisplayRightContent="租赁方承担"
                        }
                        when {
                            data.partnerAttribute==null -> adapter.mData[7].singleDisplayRightContent= "无"
                            data.partnerAttribute=="0" -> adapter.mData[7].singleDisplayRightContent="单位"
                            data.partnerAttribute=="1" -> adapter.mData[7].singleDisplayRightContent="个人"
                        }
                        if(data.hireFareStandard==null) {
                            adapter.mData[8].singleDisplayRightContent="无" }
                        else if(data.hireFareStandard=="0"){ adapter.mData[8].singleDisplayRightContent="按清单报价"}
                        else if(data.hireFareStandard=="1"){ adapter.mData[8].singleDisplayRightContent="面议"}
                        adapter.mData[10].singleDisplayRightContent=if(data.name==null) {
                            "无" } else{ data.name}
                        adapter.mData[11].singleDisplayRightContent=if(data.phone==null) {
                            "无" } else{ data.phone}
                        adapter.mData[12].singleDisplayRightContent=if(data.validTime==null) {
                            "无" } else{ data.validTime}
                        adapter.mData[13].singleDisplayRightContent=if(data.additonalExplain==null) {
                            "无" } else{ data.additonalExplain}
                        adapter.mData[14].submitListener = View.OnClickListener {

                            mdata.putInt("type",14)
                            mdata.putString("projectName",data.projectName)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            mdata.putSerializable("listData4", data.requirementLeaseLists as Serializable)//租赁清单
                           // (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //机械租赁
            15->{
                adapter = adapterGenerate.demandTeamDisplayEquipmentLeasing()
                val result = getRequirementLeaseMachinery(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            "无" } else{ data.requirementVariety }
                        adapter.mData[1].singleDisplayRightContent=if(data.projectName==null) {
                            "无" } else{ data.projectName }
                        adapter.mData[2].singleDisplayRightContent=if(data.projectSite==null) {
                            "无" } else{ data.projectSite}
                        adapter.mData[3].singleDisplayRightContent=if(data.projectTime==null) {
                            "无" } else{ data.projectTime}
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
                                    mdata.putInt("type",5)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        when {
                            data.financeTransportationInsurance==null -> adapter.mData[5].singleDisplayRightContent="无"
                            data.financeTransportationInsurance=="0" -> adapter.mData[5].singleDisplayRightContent="出租方承担"
                            data.financeTransportationInsurance=="1" -> adapter.mData[5].singleDisplayRightContent="租赁方承担"
                        }
                        when {
                            data.distribution==null -> adapter.mData[6].singleDisplayRightContent="无"
                            data.distribution=="0" -> adapter.mData[6].singleDisplayRightContent="出租方承担"
                            data.distribution=="1" -> adapter.mData[6].singleDisplayRightContent="租赁方承担"
                        }
                        when {
                            data.partnerAttribute==null -> adapter.mData[7].singleDisplayRightContent= "无"
                            data.partnerAttribute=="0" -> adapter.mData[7].singleDisplayRightContent="单位"
                            data.partnerAttribute=="1" -> adapter.mData[7].singleDisplayRightContent="个人"
                        }
                        if(data.hireFareStandard==null) {
                            adapter.mData[8].singleDisplayRightContent="无" }
                        else if(data.hireFareStandard=="0"){ adapter.mData[8].singleDisplayRightContent="按清单报价"}
                        else if(data.hireFareStandard=="1"){ adapter.mData[8].singleDisplayRightContent="面议"}
                        adapter.mData[10].singleDisplayRightContent=if(data.name==null) {
                            "无" } else{ data.name}
                        adapter.mData[11].singleDisplayRightContent=if(data.phone==null) {
                            "无" } else{ data.phone}
                        adapter.mData[12].singleDisplayRightContent=if(data.validTime==null) {
                            "无" } else{ data.validTime}
                        adapter.mData[13].singleDisplayRightContent=if(data.additonalExplain==null) {
                            "无" } else{ data.additonalExplain}
                        adapter.mData[14].submitListener = View.OnClickListener {
                            mdata.putInt("type",15)
                            mdata.putString("projectName",data.projectName)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            mdata.putSerializable("listData4", data.requirementLeaseLists as Serializable)//租赁清单
                            //(activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //需求三方 除资质合作
            16->{
                adapter = adapterGenerate.demandTeamDisplayDemandTripartite()
                val result = getRequirementThirdPartyDetail(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                      var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                                "无" } else{ data.requirementVariety }
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
                                    mdata.putInt("type",6)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                })
                            }
                        }
                        when {
                            data.partnerAttribute==null -> adapter.mData[2].singleDisplayRightContent="无"
                            data.partnerAttribute=="1" -> adapter.mData[2].singleDisplayRightContent="单位"
                            data.partnerAttribute=="0" -> adapter.mData[2].singleDisplayRightContent="个人"
                        }
                        when {
                            data.fareStandard==null -> adapter.mData[3].singleDisplayRightContent="无"
                            data.fareStandard=="1" -> adapter.mData[3].singleDisplayRightContent="面议"
                            data.fareStandard=="0" -> adapter.mData[3].singleDisplayRightContent="按清单报价"
                        }
                            adapter.mData[5].singleDisplayRightContent=if(data.name==null) {
                                "无" } else{ data.name}
                            adapter.mData[6].singleDisplayRightContent=if(data.phone==null) {
                                "无" } else{ data.phone}
                            adapter.mData[7].singleDisplayRightContent=if(data.validTime==null) {
                                "无" } else{ data.validTime}
                        adapter.mData[8].submitListener = View.OnClickListener {

                            mdata.putInt("type",16)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                            mdata.putSerializable("listData5", data.thirdLists as Serializable)//三方清册
                           // (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            //需求三方 资质合作
            17->{
                adapter = adapterGenerate.demandTeamDisplayDemandTripartiteCooperation()
                val result = getRequirementThirdPartyDetail(id,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.requirementVariety==null) {
                            "无" } else{ data.requirementVariety }
                        when {
                            data.partnerAttribute==null -> adapter.mData[1].singleDisplayRightContent= "无"
                            data.partnerAttribute=="1" -> adapter.mData[1].singleDisplayRightContent= "单位"
                            data.partnerAttribute=="1" -> adapter.mData[1].singleDisplayRightContent= "个人"
                        }
                        when {
                            data.qualificationUnitName==null -> adapter.mData[1].singleDisplayRightContent= "无"
                            else -> adapter.mData[2].singleDisplayRightContent=  data.qualificationUnitName
                        }
                        adapter.mData[3].singleDisplayRightContent=if(data.cooperationRegion==null) {
                            "无" } else{ data.cooperationRegion}
                        adapter.mData[5].singleDisplayRightContent=if(data.name==null) {
                            "无" } else{ data.name}
                        adapter.mData[6].singleDisplayRightContent=if(data.phone==null) {
                            "无" } else{ data.phone}
                        adapter.mData[7].singleDisplayRightContent=if(data.qualificationRequirementt==null) {
                            "无" } else{ data.qualificationRequirementt}
                        adapter.mData[8].submitListener = View.OnClickListener {
                            mdata.putInt("type",17)
                            mdata.putString("requirementVariety",data.requirementVariety)
                            mdata.putString("name",data.name)
                            mdata.putString("phone",data.phone)
                           // (activity as DemandDisplayActivity).switchFragment(ApplicationSubmitFragment(mdata))
                        }

                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }

        }

    }


}