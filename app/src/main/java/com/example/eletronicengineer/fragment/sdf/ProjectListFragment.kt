package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.DemandActivity
import com.example.eletronicengineer.activity.DemandDisplayActivity
import com.example.eletronicengineer.activity.SupplyDisplayActivity
import com.example.eletronicengineer.adapter.ProjectListAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.aninterface.ProjectList
import com.example.eletronicengineer.db.DisplayDemand.RequirementCarList
import com.example.eletronicengineer.db.DisplayDemand.RequirementMembersList
import com.example.eletronicengineer.distributionFileSave.*
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.FragmentHelper
import kotlinx.android.synthetic.main.fragemt_with_inventory.view.*
import kotlinx.android.synthetic.main.fragment_demand_display.view.*
import kotlinx.android.synthetic.main.fragment_project_display.view.*
import java.io.Serializable

class ProjectListFragment:Fragment() {
    companion object{
        fun newInstance(args: Bundle): ProjectListFragment
        {
            val fragment= ProjectListFragment()
            fragment.arguments=args
            return fragment
        }
    }

    lateinit var listData1:List<RequirementCarList>//车辆清册查看
    lateinit var listData2:List<RequirementMembersList>//成员清册查看
    lateinit var listData4:List<requirementLeaseProjectList>//租赁清单查看
    lateinit var listData5:List<thirdLists>//三方清册查看

    lateinit var listData3:List<requirementSecondProvideMaterialsList>
    lateinit var listData7:List<provideCrewLists>//供应人员清册查看
    lateinit var listData8:List<provideTransportMachines>//供应运输清册查看
    lateinit var listData9:List<constructionToolLists>//供应工器具清册查看
    lateinit var listData10:List<leaseList>//租赁清册

    var mProjectList = mutableListOf<ProjectList>()
    lateinit var listListener:View.OnClickListener
    var listAdapter=ProjectListAdapter()
    val bundle = Bundle()
    private lateinit var type:String
    lateinit var mView: View
    var adapter: RecyclerviewAdapter?=null
    val multiStyleItemList = ArrayList<MultiStyleItem>().toMutableList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_project_display, container, false)
        type = arguments!!.getString("type")
        mView.tv_project_demand_title.text=type+"列表"
        mView.tv_project_demand_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        if(adapter==null){
            initData()
            switchAdapter()
        }
        else{
            mView.rv_project_fragment_content.adapter = adapter
            mView.rv_project_fragment_content.layoutManager = LinearLayoutManager(context)
        }

        return mView
    }
    fun initData(){
        when (type) {
            "车辆清册查看"->
            {
                if(arguments!!.getSerializable("listData1")!= null)
                    listData1 = arguments!!.getSerializable("listData1") as List<RequirementCarList>
            }
            "成员清册查看"->{
                if(arguments!!.getSerializable("listData2")!= null)
                    listData2 = arguments!!.getSerializable("listData2") as List<RequirementMembersList>
            }
            "租赁清单查看"->{
                if(arguments!!.getSerializable("listData4")!= null)
                    listData4 = arguments!!.getSerializable("listData4") as List<requirementLeaseProjectList>
            }
            "三方清册查看"->{
                if(arguments!!.getSerializable("listData5")!= null)
                    listData5 = arguments!!.getSerializable("listData5") as List<thirdLists>
            }
            //供应人员清册查看
            "供应人员清册查看"->{
                if(arguments!!.getSerializable("listData7")!= null)
                    listData7 = arguments!!.getSerializable("listData7") as List<provideCrewLists>
            }
            //供应运输清册查看供应工器具清册查看
            "供应运输清册查看"->{
                if(arguments!!.getSerializable("listData8")!= null)
                    listData8 = arguments!!.getSerializable("listData8") as List<provideTransportMachines>
            }
            //供应工器具清册查看
            "供应工器具清册查看"->{
                if(arguments!!.getSerializable("listData9")!= null)
                    listData9 = arguments!!.getSerializable("listData9") as List<constructionToolLists>
            }
            //租赁清册
           "工器具租赁清册查看",
           "设备租赁清册查看",
           "机械租赁清册查看"->{
               if(arguments!!.getSerializable("listData10")!= null)
                   listData10 = arguments!!.getSerializable("listData10") as List<leaseList>
           }


//            4 -> listData3 = arguments!!.getSerializable("listData3") as List<requirementSecondProvideMaterialsList>
//            5 ,25->
//            {
//                if(arguments!!.getSerializable("listData4")==null)  requirementLease = 0
//                else{
//                    listData4 = arguments!!.getSerializable("listData4") as List<requirementLeaseProjectList>
//                    requirementLease = if(listData4!!.isEmpty()) 0
//                    else listData4.size
//                }
//
//            }
//            6 ,26 -> {
//                if(arguments!!.getSerializable("listData5")==null)  requirementThird = 0
//                else{
//                    listData5 = arguments!!.getSerializable("listData5") as List<thirdLists>
//                    requirementThird = if(listData5!!.isEmpty()) 0
//                    else listData5.size
//                }
//            }
//
//            7 -> listData7 = arguments!!.getSerializable("listData7") as List<provideCrewLists>
//            8 -> listData8 = arguments!!.getSerializable("listData8") as List<provideTransportMachines>
//            9 -> listData9 = arguments!!.getSerializable("listData9") as List<constructionToolLists>
//            10 -> listData10 = arguments!!.getSerializable("listData10") as List<leaseList>
//
//            //成员清册
//            20->needPeopleNumber=if(arguments!!.getString("needPeopleNumber")==null){"0"} else{arguments!!.getString("needPeopleNumber")}
//            //车辆清册
//            21->vehicle=if(arguments!!.getString("vehicle")==null){"0"} else{arguments!!.getString("vehicle")}
//            //机械清册  机械提供状态 0 不提供  1 提供
//            22->constructionEquipment=if(arguments!!.getString("constructionEquipment")==null){"-1"} else{arguments!!.getString("constructionEquipment")}

        }
    }
    private fun switchAdapter():List<MultiStyleItem>{
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as AppCompatActivity
        lateinit var mData:List<MultiStyleItem>
        when(type){
            "成员清册查看"->
            {
                for(i in listData2)
                {
                    bundle.putSerializable("listData2", i as Serializable)
                    mData = adapterGenerate.requirementMembersList(bundle).mData
                    initFragment(mData)
                }
                adapter = RecyclerviewAdapter(multiStyleItemList)
                mView.rv_project_fragment_content.adapter = adapter
                mView.rv_project_fragment_content.layoutManager = LinearLayoutManager(context)
            }
            "车辆清册查看"->
            {
                for(i in listData1)
                {
                    bundle.putSerializable("listData1", i as Serializable)
                    mData = adapterGenerate.requirementCarList(bundle).mData
                    initFragment(mData)
                }
                adapter = RecyclerviewAdapter(multiStyleItemList)
                mView.rv_project_fragment_content.adapter = adapter
                mView.rv_project_fragment_content.layoutManager = LinearLayoutManager(context)

            }
            "租赁清单查看"->
            {
                for(i in listData4)
                {
                    bundle.putSerializable("listData4", i as Serializable)
                    mData = adapterGenerate.requirmentLease(bundle).mData
                    initFragment(mData)
                }
                adapter = RecyclerviewAdapter(multiStyleItemList)
                mView.rv_project_fragment_content.adapter = adapter
                mView.rv_project_fragment_content.layoutManager = LinearLayoutManager(context)
            }
            "三方清册查看"->
            {
                for(i in listData5)
                {
                    bundle.putSerializable("listData5", i as Serializable)
                    mData = adapterGenerate.requirmentThird(bundle).mData
                    initFragment(mData)
                }
                adapter = RecyclerviewAdapter(multiStyleItemList)
                mView.rv_project_fragment_content.adapter = adapter
                mView.rv_project_fragment_content.layoutManager = LinearLayoutManager(context)

            }
            "供应人员清册查看"->
            {
                for(i in listData7)
                {
                    bundle.putSerializable("listData7", i as Serializable)
                    mData = adapterGenerate.provideCrewLists(bundle).mData
                    initFragment(mData)
                }
                adapter = RecyclerviewAdapter(multiStyleItemList)
                mView.rv_project_fragment_content.adapter = adapter
                mView.rv_project_fragment_content.layoutManager = LinearLayoutManager(context)

            }
            "供应运输清册查看"->{
            for(i in listData8)
            {
                bundle.putSerializable("listData8", i as Serializable)
                mData = adapterGenerate.provideTransportMachines(bundle).mData
                initFragment(mData)
            }
            adapter = RecyclerviewAdapter(multiStyleItemList)
            mView.rv_project_fragment_content.adapter = adapter
            mView.rv_project_fragment_content.layoutManager = LinearLayoutManager(context)
           }
            //供应工器具清册查看
            "供应工器具清册查看"->{
                for(i in listData9)
                {
                    bundle.putSerializable("listData9", i as Serializable)
                    mData = adapterGenerate.provideConstructionToolLists(bundle).mData
                    initFragment(mData)
                }
                adapter = RecyclerviewAdapter(multiStyleItemList)
                mView.rv_project_fragment_content.adapter = adapter
                mView.rv_project_fragment_content.layoutManager = LinearLayoutManager(context)
            }
            //租赁清册
            "工器具租赁清册查看",
            "设备租赁清册查看",
            "机械租赁清册查看"->{
                for(i in listData10)
                {
                    bundle.putSerializable("listData10", i as Serializable)
                    mData = adapterGenerate.provideHireList(bundle).mData
                    initFragment(mData)
                }
                adapter = RecyclerviewAdapter(multiStyleItemList)
                mView.rv_project_fragment_content.adapter = adapter
                mView.rv_project_fragment_content.layoutManager = LinearLayoutManager(context)
            }


            else->{
            }
        }
        return mData
    }


     fun initFragment(itemMultiStyleItem:List<MultiStyleItem>){

        when(type)
        {
            "车辆清册查看"->//车辆清册查看
            {
                multiStyleItemList.add(
                            MultiStyleItem(
                                MultiStyleItem.Options.SHIFT_INPUT,
                                itemMultiStyleItem[0].singleDisplayRightContent,
                                false
                            )
                        )
                var position=multiStyleItemList.size-1
                multiStyleItemList[position].itemMultiStyleItem = itemMultiStyleItem
                multiStyleItemList[position].jumpListener = View.OnClickListener {
                        val bundle = Bundle()
                        bundle.putSerializable("inventoryItem",multiStyleItemList[position].itemMultiStyleItem as Serializable)
                        bundle.putString("type",type)
                        FragmentHelper.switchFragment(mView.context as DemandDisplayActivity,ProjectListDetailFragment.newInstance(bundle),
                            R.id.frame_display_demand,"")
                    }
            }
            "成员清册查看"->//成员清册查看
            {
                multiStyleItemList.add(
                    MultiStyleItem(
                        MultiStyleItem.Options.SHIFT_INPUT,
                        itemMultiStyleItem[0].singleDisplayRightContent,
                        false
                    )
                )
                var position=multiStyleItemList.size-1
                multiStyleItemList[position].itemMultiStyleItem = itemMultiStyleItem
                multiStyleItemList[position].jumpListener = View.OnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable("inventoryItem",multiStyleItemList[position].itemMultiStyleItem as Serializable)
                    bundle.putString("type",type)
                    FragmentHelper.switchFragment(mView.context as DemandDisplayActivity,ProjectListDetailFragment.newInstance(bundle),
                        R.id.frame_display_demand,"")
                }
            }
            "租赁清单查看"->//租赁清单查看
            {
                multiStyleItemList.add(
                    MultiStyleItem(
                        MultiStyleItem.Options.SHIFT_INPUT,
                        itemMultiStyleItem[0].singleDisplayRightContent,
                        false
                    )
                )
                var position=multiStyleItemList.size-1
                multiStyleItemList[position].itemMultiStyleItem = itemMultiStyleItem
                multiStyleItemList[position].jumpListener = View.OnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable("inventoryItem",multiStyleItemList[position].itemMultiStyleItem as Serializable)
                    bundle.putString("type",type)
                    FragmentHelper.switchFragment(mView.context as DemandDisplayActivity,ProjectListDetailFragment.newInstance(bundle),
                        R.id.frame_display_demand,"")
                }
            }
            "三方清册查看"->//三方清册查看
            {
                multiStyleItemList.add(
                    MultiStyleItem(
                        MultiStyleItem.Options.SHIFT_INPUT,
                        itemMultiStyleItem[0].singleDisplayRightContent,
                        false
                    )
                )
                var position=multiStyleItemList.size-1
                multiStyleItemList[position].itemMultiStyleItem = itemMultiStyleItem
                multiStyleItemList[position].jumpListener = View.OnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable("inventoryItem",multiStyleItemList[position].itemMultiStyleItem as Serializable)
                    bundle.putString("type",type)
                    FragmentHelper.switchFragment(mView.context as DemandDisplayActivity,ProjectListDetailFragment.newInstance(bundle),
                        R.id.frame_display_demand,"")
                }
            }
            "供应人员清册查看"->//供应人员清册查看
            {
                multiStyleItemList.add(
                    MultiStyleItem(
                        MultiStyleItem.Options.SHIFT_INPUT,
                        itemMultiStyleItem[0].singleDisplayRightContent,
                        false
                    )
                )
                var position=multiStyleItemList.size-1
                multiStyleItemList[position].itemMultiStyleItem = itemMultiStyleItem
                multiStyleItemList[position].jumpListener = View.OnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable("inventoryItem",multiStyleItemList[position].itemMultiStyleItem as Serializable)
                    bundle.putString("type",type)
                    FragmentHelper.switchFragment(mView.context as SupplyDisplayActivity,ProjectListDetailFragment.newInstance(bundle),
                        R.id.frame_display_supply,"")
                }
            }
            "供应运输清册查看"->//供应运输清册查看
            {
                multiStyleItemList.add(
                    MultiStyleItem(
                        MultiStyleItem.Options.SHIFT_INPUT,
                        itemMultiStyleItem[0].singleDisplayRightContent,
                        false
                    )
                )
                var position=multiStyleItemList.size-1
                multiStyleItemList[position].itemMultiStyleItem = itemMultiStyleItem
                multiStyleItemList[position].jumpListener = View.OnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable("inventoryItem",multiStyleItemList[position].itemMultiStyleItem as Serializable)
                    bundle.putString("type",type)
                    FragmentHelper.switchFragment(mView.context as SupplyDisplayActivity,ProjectListDetailFragment.newInstance(bundle),
                        R.id.frame_display_supply,"")
                }
            }
            //供应工器具清册查看
            "供应工器具清册查看"->//供应工器具清册查看
            {
                multiStyleItemList.add(
                    MultiStyleItem(
                        MultiStyleItem.Options.SHIFT_INPUT,
                        itemMultiStyleItem[0].singleDisplayRightContent,
                        false
                    )
                )
                var position=multiStyleItemList.size-1
                multiStyleItemList[position].itemMultiStyleItem = itemMultiStyleItem
                multiStyleItemList[position].jumpListener = View.OnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable("inventoryItem",multiStyleItemList[position].itemMultiStyleItem as Serializable)
                    bundle.putString("type",type)
                    FragmentHelper.switchFragment(mView.context as SupplyDisplayActivity,ProjectListDetailFragment.newInstance(bundle),
                        R.id.frame_display_supply,"")
                }
            }
            //租赁清册
            "工器具租赁清册查看",
            "设备租赁清册查看",
            "机械租赁清册查看"->{
                multiStyleItemList.add(
                    MultiStyleItem(
                        MultiStyleItem.Options.SHIFT_INPUT,
                        itemMultiStyleItem[0].singleDisplayRightContent,
                        false
                    )
                )
                var position=multiStyleItemList.size-1
                multiStyleItemList[position].itemMultiStyleItem = itemMultiStyleItem
                multiStyleItemList[position].jumpListener = View.OnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable("inventoryItem",multiStyleItemList[position].itemMultiStyleItem as Serializable)
                    bundle.putString("type",type)
                    FragmentHelper.switchFragment(mView.context as SupplyDisplayActivity,ProjectListDetailFragment.newInstance(bundle),
                        R.id.frame_display_supply,"")
                }

            }
//            2->//清工薪资
//            {
//                for(i in listData2)
//                {
//                    var temp=""
//                    listListener = View.OnClickListener {
//                        //                        val intent = Intent(context, ProjectListDetailActivity::class.java)
////                        intent.putExtra("type",2)//清工薪资
////                        val bundle = Bundle()
////                        bundle.putSerializable("listData2", i as Serializable)//清册序列化传输
////                        intent.putExtras(bundle)
////                        startActivity(intent)
//
//                        mdata.putInt("type",2)
//                        mdata.putSerializable("listData2", i as Serializable)
//                        (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
//                    }
//                    temp = if(i.positionType==null) {
//                        "无数据"
//                    } else{
//                        i.positionType
//                    }
//                    var projectList = ProjectList(temp,listListener)
//                    mProjectList.add(projectList)
//                }
//                view.rv_demand_display_content.adapter = ProjectListAdapter(mProjectList)
//                view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
//            }
//            3->//清单报价
//            {
//                for(i in listData1)
//                {
//                    var temp=""
//                    listListener = View.OnClickListener {
//                        //                        val intent = Intent(context, ProjectListDetailActivity::class.java)
////                        intent.putExtra("type",3)//清单报价
////                        val bundle = Bundle()
////                        bundle.putSerializable("listData1", i as Serializable)//清册序列化传输
////                        intent.putExtras(bundle)
////                        startActivity(intent)
//                        mdata.putInt("type",3)
//                        mdata.putSerializable("listData1", i as Serializable)
//                        (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
//                    }
//                    temp = if(i.carType==null) {
//                        "无数据"
//                    } else{
//                        i.carType
//                    }
//                    var projectList = ProjectList(temp,listListener)
//                    mProjectList.add(projectList)
//                }
//                view.rv_demand_display_content.adapter = ProjectListAdapter(mProjectList)
//                view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
//            }
//            4->//乙供清单
//            {
//                for(i in listData3)
//                {
//                    var temp=""
//                    listListener = View.OnClickListener {
//                        //                        val intent = Intent(context, ProjectListDetailActivity::class.java)
//////                        intent.putExtra("type",4)//乙供清单
//////                        val bundle = Bundle()
//////                        bundle.putSerializable("listData3", i as Serializable)//清册序列化传输
//////                        intent.putExtras(bundle)
//////                        startActivity(intent)
//                        mdata.putInt("type",4)
//                        mdata.putSerializable("listData3", i as Serializable)
//                        (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
//                    }
//                    temp = if(i.projectName==null) {
//                        "无数据"
//                    } else{
//                        i.projectName
//                    }
//                    var projectList = ProjectList(temp,listListener)
//                    mProjectList.add(projectList)
//                }
//                view.rv_demand_display_content.adapter = ProjectListAdapter(mProjectList)
//                view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
//            }
//            5->{//需求租赁清单
//                for(i in listData4)
//                {
//                    var temp=""
//                    listListener = View.OnClickListener {
//                        //                        val intent = Intent(context, ProjectListDetailActivity::class.java)
////                        intent.putExtra("type",5)
////                        val bundle = Bundle()
////                        bundle.putSerializable("listData4", i as Serializable)//清册序列化传输
////                        intent.putExtras(bundle)
////                        startActivity(intent)
//                        mdata.putInt("type",5)
//                        mdata.putSerializable("listData4", i as Serializable)
//                        (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
//                    }
//                    temp = if(i.projectName==null) {
//                        "无数据"
//                    } else{
//                        i.projectName
//
//                    }
//                    var projectList = ProjectList(temp,listListener)
//                    mProjectList.add(projectList)
//                }
//                view.rv_demand_display_content.adapter = ProjectListAdapter(mProjectList)
//                view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
//            }
//            6->{//需求三方
//                for(i in listData5)
//                {
//                    var temp=""
//                    listListener = View.OnClickListener {
//                        //                        val intent = Intent(context, ProjectListDetailActivity::class.java)
////                        intent.putExtra("type",6)
////                        val bundle = Bundle()
////                        bundle.putSerializable("listData5", i as Serializable)//清册序列化传输
////                        intent.putExtras(bundle)
////                        startActivity(intent)
//                        mdata.putInt("type",6)
//                        mdata.putSerializable("listData5", i as Serializable)
//                        (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
//                    }
//                    temp = if(i.projectName==null) {
//                        "无数据"
//                    } else{
//                        i.projectName
//
//                    }
//                    var projectList = ProjectList(temp,listListener)
//                    mProjectList.add(projectList)
//                }
//                view.rv_demand_display_content.adapter = ProjectListAdapter(mProjectList)
//                view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
//            }
//            7->{//人员清册显示
//                for(i in listData7)
//                {
//                    var temp=""
//                    listListener = View.OnClickListener {
//                        //                        val intent = Intent(context, ProjectListDetailActivity::class.java)
////                        intent.putExtra("type",7)
////                        val bundle = Bundle()
////                        bundle.putSerializable("listData7", i as Serializable)//清册序列化传输
////                        intent.putExtras(bundle)
////                        startActivity(intent)
//                        mdata.putInt("type",7)
//                        mdata.putSerializable("listData7", i as Serializable)
//                        (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
//                    }
//                    temp = if(i.name==null) {
//                        "无数据"
//                    } else{
//                        i.name
//
//                    }
//                    var projectList = ProjectList(temp,listListener)
//                    mProjectList.add(projectList)
//                }
//                view.rv_demand_display_content.adapter = ProjectListAdapter(mProjectList)
//                view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
//            }
//            8->{//车辆清册显示
//                for(i in listData8)
//                {
//                    var temp=""
//                    listListener = View.OnClickListener {
//                        //                        val intent = Intent(context, ProjectListDetailActivity::class.java)
////                        intent.putExtra("type",8)
////                        val bundle = Bundle()
////                        bundle.putSerializable("listData8", i as Serializable)//清册序列化传输
////                        intent.putExtras(bundle)
////                        startActivity(intent)
//                        mdata.putInt("type",8)
//                        mdata.putSerializable("listData8", i as Serializable)
//                        (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
//                    }
//                    temp = if(i.carNumber==null) {
//                        "无数据"
//                    } else{
//                        i.carNumber
//
//                    }
//                    var projectList = ProjectList(temp,listListener)
//                    mProjectList.add(projectList)
//                }
//                view.rv_demand_display_content.adapter = ProjectListAdapter(mProjectList)
//                view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
//
//            }
//            9->{//机械清册显示
//                for(i in listData9)
//                {
//                    var temp=""
//                    listListener = View.OnClickListener {
//                        //                        val intent = Intent(context, ProjectListDetailActivity::class.java)
////                        intent.putExtra("type",9)
////                        val bundle = Bundle()
////                        bundle.putSerializable("listData9", i as Serializable)//清册序列化传输
////                        intent.putExtras(bundle)
////                        startActivity(intent)
//                        mdata.putInt("type",9)
//                        mdata.putSerializable("listData9", i as Serializable)
//                        (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
//                    }
//                    temp = if(i.category==null) {
//                        "无数据"
//                    } else{
//                        i.category
//
//                    }
//                    var projectList = ProjectList(temp,listListener)
//                    mProjectList.add(projectList)
//                }
//                view.rv_demand_display_content.adapter = ProjectListAdapter(mProjectList)
//                view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
//            }
//            10->{//工程量清册显示
//                for(i in listData10)
//                {
//                    var temp=""
//                    listListener = View.OnClickListener {
//                        mdata.putInt("type",10)
//                        mdata.putSerializable("listData10", i as Serializable)
//                        (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
//                    }
//                    temp = if(i.serialNumber==null) {
//                        "无数据"
//                    } else{
//                        i.serialNumber
//
//                    }
//                    var projectList = ProjectList(temp,listListener)
//                    mProjectList.add(projectList)
//                }
//                view.rv_demand_display_content.adapter = ProjectListAdapter(mProjectList)
//                view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
//            }

            /*  //成员清册
              20->{
                  view.tv_display_demand_add.visibility=View.VISIBLE

                  for(i in 0 until  needPeopleNumberNum)
                  {
                      listListener = View.OnClickListener {
  //                        val intent = Intent(context, ProjectListDetailActivity::class.java)
  //                        intent.putExtra("type",20)//20为成员清册
  //                        startActivity(intent)
                          mdata.putInt("type",20)
                          (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
                      }
                      val itemGenerate=ItemGenerate()
                      itemGenerate.context=view.context
                      var data=itemGenerate.getJsonFromAsset("DemandSubmit/ApplicationSubmitMemberList.json")
                      var projectList = ProjectList((i+1).toString(),listListener,data)
                      mProjectList.add(projectList)

                      listAdapter = ProjectListAdapter(mProjectList)
                  }

                  view.rv_demand_display_content.adapter =listAdapter
                  view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
              }
              21->{
                  view.tv_display_demand_add.visibility=View.VISIBLE

                  for(i in 0 until vehicleNum)
                  {
                      listListener = View.OnClickListener {
                          mdata.putInt("type",21)
                          (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
                      }
                      var projectList = ProjectList((i+1).toString(),listListener)
                      mProjectList.add(projectList)
                      listAdapter = ProjectListAdapter(mProjectList)
                  }
                  view.rv_demand_display_content.adapter = listAdapter
                  view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
              }
              22->{
                  view.tv_display_demand_add.visibility=View.VISIBLE

                  if(constructionEquipment.equals("1")) {
                      listListener = View.OnClickListener {
                          mdata.putInt("type",22)
                          (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
                      }
                      var projectList = ProjectList(constructionEquipment, listListener)
                      mProjectList.add(projectList)
                      listAdapter = ProjectListAdapter(mProjectList)
                  }
                  else if(constructionEquipment.equals("0")){
                      listListener = View.OnClickListener {
                          Toast.makeText(context,"您没有选择提供按钮，不能查看", Toast.LENGTH_SHORT).show()
                      }
                      var projectList = ProjectList(constructionEquipment, listListener)
                      mProjectList.add(projectList)
                      listAdapter = ProjectListAdapter(mProjectList)
                  }
                  view.rv_demand_display_content.adapter =
                      listAdapter
                  view.rv_demand_display_content.layoutManager =
                      LinearLayoutManager(view.context)

              }
              //清工薪资
              23->{
                  view.tv_display_demand_add.visibility=View.VISIBLE
                  //view.tv_display_demand_add.visibility=View.GONE
                  for(i in 0 until  requirementSalary1)
                  {
                      listListener = View.OnClickListener {
                          mdata.putInt("type",23)
                          (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
                      }
                      var projectList = ProjectList((i+1).toString(),listListener)
                      listAdapter = ProjectListAdapter(mProjectList)
                  }
                  view.rv_demand_display_content.adapter = listAdapter
                  view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
              }
              //清单报价
              24->{
                  //view.tv_display_demand_add.visibility=View.GONE
                  if(requirementSalary2==0)//清册为空
                  {
                      listListener = View.OnClickListener {
                          Toast.makeText(context,"没有清单报价清册",Toast.LENGTH_SHORT).show()
                      }
                      var projectList = ProjectList("无数据",listListener)
                      mProjectList.add(projectList)
                      listAdapter = ProjectListAdapter(mProjectList)
                  }
                  else
                  {
                      for(i in  listData1)
                      {
                          var temp=""
                          listListener = View.OnClickListener {
                                  mdata.putInt("type",24)
                                  mdata.putSerializable("listData1", i as Serializable)
                                  (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
                          }
                          temp = if(i.projectName==null)  " "
                          else  i.projectName
                          var projectList = ProjectList(temp,listListener)
                          mProjectList.add(projectList)
                          listAdapter = ProjectListAdapter(mProjectList)
                      }
                  }
                  view.rv_demand_display_content.adapter = listAdapter
                  view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
              }
              25->{//需求租赁清单

                  if(requirementLease==0)//清册为空
                  {
                      listListener = View.OnClickListener {
                          Toast.makeText(context,"没有需求租赁清单清册",Toast.LENGTH_SHORT).show()
                      }
                      var projectList = ProjectList("无数据",listListener)
                      mProjectList.add(projectList)
                      listAdapter = ProjectListAdapter(mProjectList)
                  }
                  else
                  {
                      view.tv_display_demand_add.visibility=View.VISIBLE
                      for(i in  listData4)
                      {
                          var temp=""
                          listListener = View.OnClickListener {
                              mdata.putInt("type",25)
                              mdata.putSerializable("listData4", i as Serializable)
                              (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
                          }
                          temp = if(i.projectName==null)  " "
                          else  i.projectName

                          var projectList = ProjectList(temp,listListener)
                          mProjectList.add(projectList)
                          listAdapter = ProjectListAdapter(mProjectList)
                      }
                  }
                  view.rv_demand_display_content.adapter = listAdapter
                  view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)
              }
              26->{//需求三方清单

                  if(requirementThird==0)//清册为空
                  {
                      listListener = View.OnClickListener {
                          Toast.makeText(context,"没有需求三方清单",Toast.LENGTH_SHORT).show()
                      }
                      var projectList = ProjectList("无数据",listListener)
                      mProjectList.add(projectList)
                      listAdapter = ProjectListAdapter(mProjectList)
                  }
                  else
                  {
                      view.tv_display_demand_add.visibility=View.VISIBLE
                      for(i in  listData5)
                      {
                          var temp=""
                          listListener = View.OnClickListener {
                              mdata.putInt("type",26)
                              mdata.putSerializable("listData5", i as Serializable)
                              (activity as DemandDisplayActivity).switchFragment(ProjectListDetailFragment.newInstance(mdata))
                          }
                          temp = if(i.projectName==null)  " "
                          else  i.projectName

                          var projectList = ProjectList(temp,listListener)
                          mProjectList.add(projectList)
                          listAdapter = ProjectListAdapter(mProjectList)
                      }
                  }
                  view.rv_demand_display_content.adapter =listAdapter
                  view.rv_demand_display_content.layoutManager = LinearLayoutManager(view.context)


              }
  */
        }

    }
}
