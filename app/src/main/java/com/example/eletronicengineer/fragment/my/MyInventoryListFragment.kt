package com.example.eletronicengineer.fragment.my

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ImageDisplayActivity
import com.example.eletronicengineer.activity.MyRegistrationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.db.My.*
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.ToastHelper
import kotlinx.android.synthetic.main.fragment_with_inventory.view.*

class MyInventoryListFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):MyInventoryListFragment{
            val myInventoryListFragment = MyInventoryListFragment()
            myInventoryListFragment.arguments = args
            return myInventoryListFragment
        }
    }
    lateinit var mView: View
    var adapter: RecyclerviewAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_with_inventory,container,false)
        if(adapter==null){
            initFragment()
        }
        else{
            mView.rv_inventory_fragment_content.adapter = adapter
            mView.rv_inventory_fragment_content.layoutManager = LinearLayoutManager(context)
        }
        return mView
    }

    private fun initFragment() {
        mView.tv_select_add.visibility = View.GONE
        val type = arguments!!.getString("type")
        mView.tv_inventory_title.text=type+"列表"
        mView.tv_inventory_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = mView.context
        adapterGenerate.activity = activity as MyRegistrationActivity
        val mMultiStyleItemList = ArrayList<MultiStyleItem>()
        when(type){
            "成员清册"->{
                val requirementType = arguments!!.getInt("requirementType",0)
                val requirementPersonalRegistrationEntity = arguments!!.getSerializable("inventoryList")
                if(requirementType==0){
                    requirementPersonalRegistrationEntity as RequirementPersonalRegistrationEntity
                    val enrollProvideCrewList =  requirementPersonalRegistrationEntity.enrollProvideCrewList!!
                    for (j in enrollProvideCrewList){
                        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,false))
                        val itemMultiStyleItem = adapterGenerate.registrationProvideCrewList().mData
                        itemMultiStyleItem[0].singleDisplayRightContent = j.name
                        itemMultiStyleItem[1].singleDisplayRightContent = if(j.sex.toInt()==0) "女" else "男"
                        itemMultiStyleItem[2].singleDisplayRightContent = j.age
                        itemMultiStyleItem[3].singleDisplayRightContent = requirementPersonalRegistrationEntity.requirementVariety

                        itemMultiStyleItem[4].singleDisplayRightContent = j.workExperience

                        itemMultiStyleItem[5].singleDisplayRightContent =
                            if(requirementPersonalRegistrationEntity.salaryStandard=="-1")
                                "面议"
                            else
                                "${requirementPersonalRegistrationEntity.salaryStandard}${requirementPersonalRegistrationEntity.salaryUnit}"
                        if(j.remark!=null)
                            itemMultiStyleItem[6].textAreaContent = j.remark
                        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener = View.OnClickListener {
                            val bundle = Bundle()
                            bundle.putString("title","成员清册")
                            val fragment = MyInventoryItemMoreFragment.newInstance(bundle)
                            fragment.item = itemMultiStyleItem
                            FragmentHelper.switchFragment(activity!!,fragment,R.id.frame_my_registration,"")
                        }
                    }
                }
                else  {
                    requirementPersonalRegistrationEntity as RequirementTeamRegistrationEntity
                    val enrollProvideCrewList =  requirementPersonalRegistrationEntity.enrollProvideCrewLists!!
                    for (j in enrollProvideCrewList){
                        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,false))
                        val itemMultiStyleItem = adapterGenerate.registrationCrewList().mData
                        itemMultiStyleItem[0].singleDisplayRightContent = j.positionType
                        itemMultiStyleItem[1].singleDisplayRightContent = j.name
                        itemMultiStyleItem[2].singleDisplayRightContent = if(j.sex.toInt()==0) "女" else "男"
                        itemMultiStyleItem[3].singleDisplayRightContent = j.age
                        itemMultiStyleItem[4].singleDisplayRightContent = j.workExperience
                        if(j.remark!=null)
                            itemMultiStyleItem[5].textAreaContent = j.remark
                        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener = View.OnClickListener {
                            val bundle = Bundle()
                            bundle.putString("title","成员清册")
                            val fragment = MyInventoryItemMoreFragment.newInstance(bundle)
                            fragment.item = itemMultiStyleItem
                            FragmentHelper.switchFragment(activity!!,fragment,R.id.frame_my_registration,"")
                        }
                    }
                }

            }
            "机械设备"->{
                val requirementTeamRegistrationEntity =arguments!!.getSerializable("inventoryList") as RequirementTeamRegistrationEntity
                val enrollMachineries = requirementTeamRegistrationEntity.enrollMachineries!!
                for (j in enrollMachineries){
                    mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,false))
                    val itemMultiStyleItem = adapterGenerate.registrationMechanicalEquipment().mData
                    itemMultiStyleItem[0].singleDisplayRightContent = j.name
                    itemMultiStyleItem[1].singleDisplayRightContent = j.specification
                    itemMultiStyleItem[2].singleDisplayRightContent = j.quantity
                    itemMultiStyleItem[3].singleDisplayRightContent = j.units
                    if(j.details!=null)
                        itemMultiStyleItem[4].singleDisplayRightContent = j.details
                    mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener = View.OnClickListener {
                        val bundle = Bundle()
                        bundle.putString("title","机械设备")
                        val fragment = MyInventoryItemMoreFragment.newInstance(bundle)
                        fragment.item = itemMultiStyleItem
                        FragmentHelper.switchFragment(activity!!,fragment,R.id.frame_my_registration,"")
                    }
                }
            }
            "车辆清册"->{
                val requirementType = arguments!!.getInt("requirementType",0)
                val requirementLeaseRegistrationEntity = arguments!!.getSerializable("inventoryList")

                if(requirementType == 0){
                    requirementLeaseRegistrationEntity  as RequirementLeaseRegistrationEntity
                    val enrollCar = requirementLeaseRegistrationEntity.enrollCar!!
                    for (j in enrollCar){
                        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,false))
                        val itemMultiStyleItem = adapterGenerate.registrationLeaseCarList().mData
                        itemMultiStyleItem[0].singleDisplayRightContent = j.carType
                        itemMultiStyleItem[1].singleDisplayRightContent = j.carNumber
                        itemMultiStyleItem[2].singleDisplayRightContent = if(j.sex.toInt()==0) "女" else "男"
                        itemMultiStyleItem[3].singleDisplayRightContent = j.age
                        itemMultiStyleItem[4].singleDisplayRightContent = j.maxPassengers
                        itemMultiStyleItem[5].jumpListener = View.OnClickListener {
                            if(j.carPhotoPath!=null && j.carPhotoPath!=""){
                                val intent = Intent(activity,ImageDisplayActivity::class.java)
                                intent.putExtra("imagePath",j.carPhotoPath)
                                activity!!.startActivity(intent)
                            }else{
                                ToastHelper.mToast(mView.context,"照片为空")
                            }
                        }
                        if(j.maxWeight!=null)
                            itemMultiStyleItem[6].singleDisplayRightContent = j.maxWeight
                        if(j.lenghtCar!=null)
                            itemMultiStyleItem[7].singleDisplayRightContent = j.lenghtCar
                        if(j.remark!=null)
                            itemMultiStyleItem[7].singleDisplayRightContent = j.remark
                        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener = View.OnClickListener {
                            val bundle = Bundle()
                            bundle.putString("title","车辆清册")
                            val fragment = MyInventoryItemMoreFragment.newInstance(bundle)
                            fragment.item = itemMultiStyleItem
                            FragmentHelper.switchFragment(activity!!,fragment,R.id.frame_my_registration,"")
                        }
                    }
                }
                else {
                    requirementLeaseRegistrationEntity as RequirementTeamRegistrationEntity
                    val enrollCars = requirementLeaseRegistrationEntity.enrollCars!!

                    for (j in enrollCars){
                        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,false))
                        val itemMultiStyleItem = adapterGenerate.registrationTeamCarList().mData
                        itemMultiStyleItem[0].singleDisplayRightContent = j.carType
                        itemMultiStyleItem[1].singleDisplayRightContent = j.carNumber
                        itemMultiStyleItem[2].singleDisplayRightContent = j.maxPassengers
                        //3
                        if(j.maxWeight!=null)
                            itemMultiStyleItem[4].singleDisplayRightContent = j.maxWeight
                        if(j.lenghtCar!=null)
                            itemMultiStyleItem[5].singleDisplayRightContent = j.lenghtCar
                        if(j.remark!=null)
                            itemMultiStyleItem[6].singleDisplayRightContent = j.remark
                        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener = View.OnClickListener {
                            val bundle = Bundle()
                            bundle.putString("title","车辆清册")
                            val fragment = MyInventoryItemMoreFragment.newInstance(bundle)
                            fragment.item = itemMultiStyleItem
                            FragmentHelper.switchFragment(activity!!,fragment,R.id.frame_my_registration,"")
                        }
                    }
                }

            }
            "租赁清单"->{
                val requirementLeaseRegistrationEntity = arguments!!.getSerializable("inventoryList") as RequirementLeaseRegistrationEntity
                val enrollRequirementLeaseList = requirementLeaseRegistrationEntity.enrollRequirementLeaseList!!
                for (j in enrollRequirementLeaseList){
                    mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,false))
                    val itemMultiStyleItem = adapterGenerate.registrationLeaseList().mData
                    itemMultiStyleItem[0].singleDisplayRightContent = j.name
                    itemMultiStyleItem[1].singleDisplayRightContent = j.specificationsModels
                    itemMultiStyleItem[2].singleDisplayRightContent = j.units
                    itemMultiStyleItem[3].singleDisplayRightContent = j.quantity
                    if(j.quotationList!=null)
                    itemMultiStyleItem[4].singleDisplayRightContent = j.quotationList
                    itemMultiStyleItem[5].singleDisplayRightContent = j.hireTime
                    if(j.detailsExplain!=null)
                        itemMultiStyleItem[6].singleDisplayRightContent = j.detailsExplain
                    mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener = View.OnClickListener {
                        val bundle = Bundle()
                        bundle.putString("title","租赁清单")
                        val fragment = MyInventoryItemMoreFragment.newInstance(bundle)
                        fragment.item = itemMultiStyleItem
                        FragmentHelper.switchFragment(activity!!,fragment,R.id.frame_my_registration,"")
                    }
                }
            }
            "三方服务清单"->{
                val requirementThirdRegistrationEntity = arguments!!.getSerializable("inventoryList") as RequirementThirdRegistrationEntity
                val enrollThirdLists =  requirementThirdRegistrationEntity.enrollThirdLists!!
                for (j in enrollThirdLists){
                    mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.projectName,false))
                    val itemMultiStyleItem = adapterGenerate.registrationThirdList().mData
                    itemMultiStyleItem[0].singleDisplayRightContent = j.projectName
                    itemMultiStyleItem[1].singleDisplayRightContent = j.specificationsModels
                    itemMultiStyleItem[2].singleDisplayRightContent = j.units
                    itemMultiStyleItem[3].singleDisplayRightContent = j.quantity
                    if(j.quotationList!=null)
                    itemMultiStyleItem[4].singleDisplayRightContent = j.quotationList
                    if(j.detailsExplain!=null)
                    itemMultiStyleItem[5].singleDisplayRightContent = j.detailsExplain
                    mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener = View.OnClickListener {
                        val bundle = Bundle()
                        bundle.putString("title","三方服务清单")
                        val fragment = MyInventoryItemMoreFragment.newInstance(bundle)
                        fragment.item = itemMultiStyleItem
                        FragmentHelper.switchFragment(activity!!,fragment,R.id.frame_my_registration,"")
                    }
                }
            }
        }
        mView.rv_inventory_fragment_content.adapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_inventory_fragment_content.layoutManager = LinearLayoutManager(mView.context)
    }
}