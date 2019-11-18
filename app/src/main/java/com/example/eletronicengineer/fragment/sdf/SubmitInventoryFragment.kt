package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.DemandDisplayActivity
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.distributionFileSave.requirementLeaseProjectList
import com.example.eletronicengineer.distributionFileSave.requirementTeamProjectList
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.fragemt_with_inventory.view.*
import java.io.Serializable

class SubmitInventoryFragment : Fragment() {
    companion object{
        fun newInstance(args: Bundle): SubmitInventoryFragment
        {
            val submitInventoryFragment= SubmitInventoryFragment()
            submitInventoryFragment.arguments=args
            return submitInventoryFragment
        }
    }
    val bundle = Bundle()
    private lateinit var type:String
    lateinit var mView: View
    var adapter: RecyclerviewAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragemt_with_inventory, container, false)
        type = arguments!!.getString("type")
        mView.tv_display_demand_title.setText(type)
        if(adapter==null){
            val multiStyleItemList = arguments!!.getSerializable("inventory") as List<MultiStyleItem>
            if(multiStyleItemList.isEmpty())
                initFragment()
            else{
                adapter = RecyclerviewAdapter(multiStyleItemList)
                mView.rv_inventory_fragment_content.adapter = adapter
                mView.rv_inventory_fragment_content.layoutManager = LinearLayoutManager(context)
            }
        }
        else{
            mView.rv_inventory_fragment_content.adapter = adapter
            mView.rv_inventory_fragment_content.layoutManager = LinearLayoutManager(context)
        }
        initOnClick(mView)
        return mView
    }

    private fun initFragment() {
        val multiStyleItemList = ArrayList<MultiStyleItem>().toMutableList()
        multiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.BLANK," "))
        adapter = RecyclerviewAdapter(multiStyleItemList)
        mView.rv_inventory_fragment_content.adapter = adapter
        mView.rv_inventory_fragment_content.layoutManager = LinearLayoutManager(context)

    }
    fun initOnClick(mView:View){
        mView.tv_display_demand_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.tv_select_add.setOnClickListener{
            adapter!!.mData[0].selected = -1
            bundle.putSerializable("inventoryItem", switchAdapter() as Serializable)
            bundle.putString("type",type)
            FragmentHelper.switchFragment(
                activity!!, SubmitInventoryItemMoreFragment.newInstance(bundle),
                R.id.frame_display_demand, ""
            )

        }
    }
    private fun switchAdapter():List<MultiStyleItem>{
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as DemandDisplayActivity
        val mData:List<MultiStyleItem>
        when(type){
            "成员清册"->
            {
                bundle.putString("type",type)
                mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
            }
            "车辆清册"->
            {
                bundle.putString("type",type)
                mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
            }
            "机械清册"->
            {
                bundle.putString("type",type)
                mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
            }
            "清工薪资清册"->
            {
                bundle.putString("type",type)
                mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
            }
            "清单报价清册"->
            {
                bundle.putString("type",type)
                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
                mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
            }
            "租赁清册"->
            {
                bundle.putString("type",type)
                bundle.putSerializable("listData4",arguments!!.getSerializable("listData4"))
                mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
            }

            else->mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
        }
        return mData
    }
    fun update(itemMultiStyleItem:List<MultiStyleItem>){
        Log.i("adapter.mData size is",adapter!!.mData.toString())
        if(adapter!!.mData[0].selected==-1){
            val mData = adapter!!.mData.toMutableList()
            if(type=="清单报价清册")
            {
                mData.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,itemMultiStyleItem[0].singleDisplayRightContent,false))
            }
            else{
                mData.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,itemMultiStyleItem[3].selectContent,false))
            }

            mData[mData.size-1].itemMultiStyleItem = itemMultiStyleItem
            mData[mData.size-1].jumpListener = View.OnClickListener {
                mData[0].selected = mData.size-1
                val bundle = Bundle()
                bundle.putSerializable("inventoryItem",mData[mData.size-1].itemMultiStyleItem as Serializable)
                bundle.putString("type",type)
                FragmentHelper.switchFragment(mView.context as DemandDisplayActivity,SubmitInventoryItemMoreFragment.newInstance(bundle),
                    R.id.frame_display_demand,"")
            }
            adapter!!.mData = mData
            adapter!!.notifyItemInserted(mData.size-1)
        }
        else{
            when(type)
            {
                "清单报价清册"->
                {
                    adapter!!.mData[adapter!!.mData[0].selected].shiftInputTitle = itemMultiStyleItem[0].singleDisplayRightContent
                }
                "成员清册","车辆清册"->
                {
                    adapter!!.mData[adapter!!.mData[0].selected].shiftInputTitle = itemMultiStyleItem[3].selectContent
                }
            }
            adapter!!.mData[adapter!!.mData[0].selected].itemMultiStyleItem = itemMultiStyleItem
            adapter!!.notifyItemChanged(adapter!!.mData[0].selected)
            if(itemMultiStyleItem[0].selected==0) {
                adapter!!.mData[adapter!!.mData[0].selected].necessary = false
            }
            else{
                adapter!!.mData[adapter!!.mData[0].selected].necessary = true
            }
        }

        val fragment = activity!!.supportFragmentManager.findFragmentByTag("register") as ApplicationSubmitFragment
        fragment.update(adapter!!.mData)
    }
}