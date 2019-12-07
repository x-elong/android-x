package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyRegistrationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.db.My.EnrollProvideCrewListEntity
import com.example.eletronicengineer.db.My.RequirementLeaseRegistrationEntity
import com.example.eletronicengineer.db.My.RequirementPersonalRegistrationEntity
import com.example.eletronicengineer.db.My.RequirementThirdRegistrationEntity
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.FragmentHelper
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
                val requirementPersonalRegistrationEntity = arguments!!.getSerializable("inventoryList") as RequirementPersonalRegistrationEntity
                val enrollProvideCrewList =  requirementPersonalRegistrationEntity.enrollProvideCrewList!!
                for (j in enrollProvideCrewList){
                    mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.name,false))
                    val itemMultiStyleItem = adapterGenerate.registrationProvideCrewList().mData
                    itemMultiStyleItem[0].singleDisplayRightContent = j.name
                    itemMultiStyleItem[1].singleDisplayRightContent = j.sex
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
            "车辆清册"->{
                val requirementLeaseRegistrationEntity = arguments!!.getSerializable("inventoryList") as RequirementLeaseRegistrationEntity
                val enrollCar =  requirementLeaseRegistrationEntity.enrollCar!!
                for (j in enrollCar){
                    mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.carType,false))
                    val itemMultiStyleItem = adapterGenerate.registrationThirdList().mData
                    itemMultiStyleItem[0].singleDisplayRightContent = j.carType
                    itemMultiStyleItem[0].singleDisplayRightContent = j.carNumber
                    itemMultiStyleItem[2].singleDisplayRightContent = if(j.sex.toInt()==0) "女" else "男"
                    itemMultiStyleItem[3].singleDisplayRightContent = j.age
                    itemMultiStyleItem[4].singleDisplayRightContent = j.maxPassengers
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
            "租赁清单"->{

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