package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.DemandDisplayActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.distributionFileSave.*
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.fragment_demand_display.view.*

class ProjectListDetailFragment:Fragment() {
    companion object {
        fun newInstance(args: Bundle): ProjectListDetailFragment {
            val fragment = ProjectListDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }


    var type: Int = 0
    lateinit var listData1: requirementTeamProjectList
    lateinit var listData2: requirementPowerTransformationSalary
    lateinit var listData3: requirementSecondProvideMaterialsList
    lateinit var listData4: requirementLeaseProjectList
    lateinit var listData5: thirdLists

    lateinit var listData7:provideCrewLists
    lateinit var listData8:provideTransportMachines
    lateinit var listData9:constructionToolLists
    lateinit var listData10:leaseList
    var needPeopleNumberNum:Int=0
    var mdata=Bundle()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_demand_display, container, false)
        view.tv_display_demand_title.text = "清册详情"
        type = arguments!!.getInt("type")
        when (type) {
            1, 3,24 -> listData1 =
                arguments!!.getSerializable("listData1") as requirementTeamProjectList
            2 -> listData2 =
                arguments!!.getSerializable("listData2") as requirementPowerTransformationSalary
            4 -> listData3 =
                arguments!!.getSerializable("listData3") as requirementSecondProvideMaterialsList
            5 ,25 -> listData4 = arguments!!.getSerializable("listData4") as requirementLeaseProjectList
            6 ,26-> listData5 = arguments!!.getSerializable("listData5") as thirdLists

            7 -> listData7 = arguments!!.getSerializable("listData7") as provideCrewLists
            8 -> listData8 = arguments!!.getSerializable("listData8") as provideTransportMachines
            9 -> listData9 = arguments!!.getSerializable("listData9") as constructionToolLists
            10 -> listData10 = arguments!!.getSerializable("listData10") as leaseList
            20-> needPeopleNumberNum=arguments!!.getInt("needPeopleNumberNum")

        }

        initFragment(view)
        return view
    }

    private fun initFragment(view: View) {
        view.tv_display_demand_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
//        view.tv_display_demand_add.visibility=View.VISIBLE
//        view.tv_display_demand_add.setOnClickListener {
//            activity!!.supportFragmentManager.popBackStackImmediate()
//           needPeopleNumberNum++
//            mdata.putInt("type",20)
//            mdata.putString("needPeopleNumber",needPeopleNumberNum.toString())
//            (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
//        }

        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = view.context
        adapterGenerate.activity = activity as AppCompatActivity
        lateinit var adapter: RecyclerviewAdapter
        when (type) {
            1 ->//工程量清册
            {
                adapter = adapterGenerate.requirementTeamProjectList()
                adapter.mData[0].singleDisplayRightContent=if(listData1.projectName==null){"无数据"}else{ listData1.projectName}
                adapter.mData[1].singleDisplayRightContent=if(listData1.specificationsModels==null){"无数据"}else{ listData1.specificationsModels}
                adapter.mData[2].singleDisplayRightContent=if(listData1.quantity==null){"无数据"}else{ listData1.quantity}
                adapter.mData[3].singleDisplayRightContent=if(listData1.units==null){"无数据"}else{ listData1.units}
                adapter.mData[4].singleDisplayRightContent="乙方填写"
                adapter.mData[5].singleDisplayRightContent=if(listData1.detailsExplain==null){"无数据"}else{ listData1.detailsExplain}
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            2 ->//清工薪资
            {
                adapter = adapterGenerate.requirementPowerTransformationSalary()
                adapter.mData[0].singleDisplayRightContent = if(listData2.positionType==null){"无数据"}else{ listData2.positionType}
                adapter.mData[1].singleDisplayRightContent = if(listData2.needPeopleNumber==null){"无数据"}else{ listData2.needPeopleNumber}
                adapter.mData[2].singleDisplayRightContent = if(listData2.salaryStandard==null){"无数据"}else{ listData2.salaryStandard}
                adapter.mData[3].singleDisplayRightContent = if(listData2.personCertificate==null){"无数据"}else{ listData2.personCertificate}
                adapter.mData[4].singleDisplayRightContent = if(listData2.remark==null){"无数据"}else{ listData2.remark}
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager =
                    LinearLayoutManager(view.context)
            }
            3 ->//清单报价
            {
                adapter = adapterGenerate.requirementListQuotations()
                adapter.mData[0].singleDisplayRightContent = if(listData1.projectName==null){"无数据"}else{ listData1.projectName}
                adapter.mData[1].singleDisplayRightContent = if(listData1.specificationsModels==null){"无数据"}else{ listData1.specificationsModels}
                adapter.mData[2].singleDisplayRightContent = if(listData1.quantity==null){"无数据"}else{ listData1.quantity}
                adapter.mData[3].singleDisplayRightContent = if(listData1.units==null){"无数据"}else{ listData1.units}
                adapter.mData[4].singleDisplayRightContent = "乙方填写"
                adapter.mData[5].singleDisplayRightContent = if(listData1.detailsExplain==null){"无数据"}else{ listData1.detailsExplain}
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager =
                    LinearLayoutManager(view.context)
            }
            4 ->//乙供清单
            {
                adapter = adapterGenerate.requirementSecondProvideMaterialsList()
                adapter.mData[0].singleDisplayRightContent = if(listData3.projectName==null){"无数据"}else{ listData3.projectName}
                adapter.mData[1].singleDisplayRightContent = if(listData3.specification==null){"无数据"}else{ listData3.specification}
                adapter.mData[2].singleDisplayRightContent = if(listData3.quantity==null){"无数据"}else{ listData3.quantity}
                adapter.mData[3].singleDisplayRightContent = if(listData3.units==null){"无数据"}else{ listData3.units}
                adapter.mData[4].singleDisplayRightContent = if(listData3.details==null){"无数据"}else{ listData3.details}
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager =
                    LinearLayoutManager(view.context)
            }
            5 ->//需求租赁清单
            {
                adapter = adapterGenerate.requirmentLease()
                adapter.mData[0].singleDisplayRightContent = if(listData4.projectName==null){"无数据"}else{ listData4.projectName }
                adapter.mData[1].singleDisplayRightContent = if(listData4.specificationsModels==null){"无数据"}else{ listData4.specificationsModels }
                adapter.mData[2].singleDisplayRightContent = if(listData4.units==null){"无数据"}else{ listData4.units }
                adapter.mData[3].singleDisplayRightContent = if(listData4.quantity==null){"无数据"}else{ listData4.quantity }
                adapter.mData[4].singleDisplayRightContent = if(listData4.quotationList==null){"由乙方填写"}else{ listData4.quotationList }
                adapter.mData[5].singleDisplayRightContent = if(listData4.hireTime==null){"无数据"}else{ listData4.hireTime }
                adapter.mData[6].singleDisplayRightContent = "乙方填写"
                adapter.mData[7].singleDisplayRightContent = if(listData4.detailsExplain==null){"无数据"}else{ listData4.detailsExplain }
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager =
                    LinearLayoutManager(view.context)
            }
            6 ->//需求三方
            {
                adapter = adapterGenerate.requirmentThird()
                adapter.mData[0].singleDisplayRightContent = if(listData5.listId==null){"无数据"}else{ listData5.listId }
                adapter.mData[1].singleDisplayRightContent = if(listData5.projectName==null){"无数据"}else{ listData5.projectName }
                adapter.mData[2].singleDisplayRightContent = if(listData5.specificationsModels==null){"无数据"}else{ listData5.specificationsModels }
                adapter.mData[3].singleDisplayRightContent = if(listData5.quantity==null){"无数据"}else{ listData5.quantity }
                adapter.mData[4].singleDisplayRightContent = if(listData5.units==null){"无数据"}else{ listData5.units }
                adapter.mData[5].singleDisplayRightContent = "乙方填写"
                adapter.mData[6].singleDisplayRightContent = if(listData5.detailsExplain==null){"无数据"}else{ listData5.detailsExplain }
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager =
                    LinearLayoutManager(view.context)
            }
            7->//成员清册
            {
                adapter = adapterGenerate.provideCrewLists()
                adapter.mData[0].singleDisplayRightContent = if(listData7.name==null){"无数据"}else{ listData7.name}
                adapter.mData[1].singleDisplayRightContent = if(listData7.sex==null){"无数据"}else{ listData7.sex }
                adapter.mData[2].singleDisplayRightContent = if(listData7.age==null){"无数据"}else{ listData7.age }
                adapter.mData[3].singleDisplayRightContent = if(listData7.wokerType==null){"无数据"}else{ listData7.wokerType }
                adapter.mData[4].singleDisplayRightContent = if(listData7.workExperience==null){"无数据"}else{ listData7.workExperience }
                adapter.mData[5].singleDisplayRightContent = if(listData7.money==null && listData7.salaryUnit==null){"无数据"} else{"${listData7.money} ${listData7.salaryUnit}"}
                adapter.mData[6].singleDisplayRightContent = if(listData7.remark==null){"无数据"}else{ listData7.remark }
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            8->//车辆清册
            {
                adapter = adapterGenerate.provideTransportMachines()
                adapter.mData[0].singleDisplayRightContent = if(listData8.carNumber==null){"无数据"}else{ listData8.carNumber}
                adapter.mData[1].singleDisplayRightContent = if(listData8.carType==null){"无数据"}else{ listData8.carType }
                adapter.mData[2].singleDisplayRightContent = if(listData8.maxPassengers==null){"无数据"}else{ listData8.maxPassengers }
                adapter.mData[3].singleDisplayRightContent = if(listData8.maxWeight==null){"无数据"}else{ listData8.maxWeight }
                adapter.mData[4].singleDisplayRightContent = if(listData8.construction==null){"无数据"}else{ listData8.construction }
                adapter.mData[5].singleDisplayRightContent = if(listData8.lenghtCar==null){"无数据"} else{listData8.lenghtCar}
                adapter.mData[6].singleDisplayRightContent = if(listData8.isDriver==null){"无数据"}else{ listData8.isDriver}
                adapter.mData[7].singleDisplayRightContent = if(listData8.isInsurance==null){"无数据"}else{ listData8.isInsurance }
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            9->//机械清册
            {
                adapter = adapterGenerate.constructionToolLists()
                adapter.mData[0].singleDisplayRightContent = if(listData9.serialNumber==null){"无数据"}else{ listData9.serialNumber }
                adapter.mData[1].singleDisplayRightContent = if(listData9.type==null){"无数据"}else{ listData9.type }
                adapter.mData[2].singleDisplayRightContent = if(listData9.specificationsModel==null){"无数据"}else{ listData9.specificationsModel }
                adapter.mData[3].singleDisplayRightContent = if(listData9.quantity==null){"无数据"}else{ listData9.quantity }
                adapter.mData[4].singleDisplayRightContent = if(listData9.unit==null){"无数据"}else{ listData9.unit }
                adapter.mData[5].singleDisplayRightContent = if(listData9.remark==null){"无数据"} else{listData9.remark}
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            10->//工器具
            {
                adapter = adapterGenerate.constructionToolLists()
                adapter.mData[0].singleDisplayRightContent = if(listData10.serialNumber==null){"无数据"}else{ listData10.serialNumber }
                adapter.mData[1].singleDisplayRightContent = if(listData10.type==null){"无数据"}else{ listData10.type }
                adapter.mData[2].singleDisplayRightContent = if(listData10.specificationsModels==null){"无数据"}else{ listData10.specificationsModels }
                adapter.mData[3].singleDisplayRightContent = if(listData10.quantity==null){"无数据"}else{ listData10.quantity }
                adapter.mData[4].singleDisplayRightContent = if(listData10.unit==null){"无数据"}else{ listData10.unit }
                adapter.mData[5].singleDisplayRightContent = if(listData10.remark==null){"无数据"} else{listData10.remark}
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            /* 20 ->//成员清册
             {
                 adapter = adapterGenerate.ApplicationSubmitDetailList(type)
                 view.tv_fragment_demand_display_content.adapter = adapter
                 view.tv_fragment_demand_display_content.layoutManager =
                     LinearLayoutManager(view.context)
             }
             21 ->//车辆清册
             {
                 adapter = adapterGenerate.ApplicationSubmitDetailList(type)
                 view.tv_fragment_demand_display_content.adapter = adapter
                 view.tv_fragment_demand_display_content.layoutManager =
                     LinearLayoutManager(view.context)
             }
             22 ->//机械清册
             {
                 adapter = adapterGenerate.ApplicationSubmitDetailList(type)
                 view.tv_fragment_demand_display_content.adapter = adapter
                 view.tv_fragment_demand_display_content.layoutManager =
                     LinearLayoutManager(view.context)
             }
             23 ->//清工薪资清册 listData2
             {
                 adapter = adapterGenerate.ApplicationSubmitDetailList(type)
                 view.tv_fragment_demand_display_content.adapter = adapter
                 view.tv_fragment_demand_display_content.layoutManager =
                     LinearLayoutManager(view.context)
             }
             24 ->//清单报价清册 listData1
             {
                 adapter = adapterGenerate.ApplicationSubmitDetailList(type)
                 adapter.mData[0].singleDisplayRightContent =if(listData1.projectName==null){"无数据"}else{ listData1.projectName }
                 adapter.mData[1].singleDisplayRightContent = if(listData1.specificationsModels==null){"无数据"}else{ listData1.specificationsModels }
                 adapter.mData[2].singleDisplayRightContent = if( listData1.quantity ==null){"无数据"}else{  listData1.quantity  }
                 adapter.mData[3].singleDisplayRightContent =   if( listData1.units ==null){"无数据"}else{  listData1.units  }
                 adapter.mData[5].singleDisplayRightContent =  if( listData1.detailsExplain ==null){"无数据"}else{ listData1.detailsExplain  }
                 view.tv_fragment_demand_display_content.adapter = adapter
                 view.tv_fragment_demand_display_content.layoutManager =
                     LinearLayoutManager(view.context)
             }
             25 ->//需求租赁清册 listData4
             {
                 adapter = adapterGenerate.ApplicationSubmitDetailList(type)
 //                adapter.mData[0].singleDisplayRightContent = listData1.projectName
 //                adapter.mData[1].singleDisplayRightContent = listData1.specificationsModels
 //                adapter.mData[2].singleDisplayRightContent = listData1.quantity
 //                adapter.mData[3].singleDisplayRightContent = listData1.units
 //                adapter.mData[5].singleDisplayRightContent = listData1.detailsExplain
                 adapter.mData[4].singleDisplayRightContent=  if( listData4.quotationList ==null){"无数据"}else{ listData4.quotationList }
                 view.tv_fragment_demand_display_content.adapter = adapter
                 view.tv_fragment_demand_display_content.layoutManager =
                     LinearLayoutManager(view.context)
             }
             26 ->//需求三方清册 listData5
             {
                 adapter = adapterGenerate.ApplicationSubmitDetailList(type)
 //                adapter.mData[0].singleDisplayRightContent = listData1.projectName
 //                adapter.mData[1].singleDisplayRightContent = listData1.specificationsModels
 //                adapter.mData[2].singleDisplayRightContent = listData1.quantity
 //                adapter.mData[3].singleDisplayRightContent = listData1.units
 //                adapter.mData[5].singleDisplayRightContent = listData1.detailsExplain
                 adapter.mData[4].singleDisplayRightContent= if( listData5.quotationList ==null){"无数据"}else{ listData5.quotationList }
                 view.tv_fragment_demand_display_content.adapter = adapter
                 view.tv_fragment_demand_display_content.layoutManager =
                     LinearLayoutManager(view.context)
             }*/
        }

    }
}