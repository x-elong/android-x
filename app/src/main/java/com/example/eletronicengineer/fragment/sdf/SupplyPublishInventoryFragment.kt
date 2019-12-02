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
import com.example.eletronicengineer.activity.DemandActivity
import com.example.eletronicengineer.activity.DemandDisplayActivity
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.distributionFileSave.Supply
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.FragmentHelper
import kotlinx.android.synthetic.main.fragemt_with_inventory.view.*
import java.io.Serializable

class SupplyPublishInventoryFragment:Fragment() {
        companion object{
            fun newInstance(args: Bundle): SupplyPublishInventoryFragment
            {
                val submitInventoryFragment= SupplyPublishInventoryFragment()
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
            mView.tv_inventory_title.setText(type+"列表")
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
        fun initOnClick(mView: View){
            mView.tv_inventory_back.setOnClickListener {
                activity!!.supportFragmentManager.popBackStackImmediate()
            }
            mView.tv_select_add.setOnClickListener{
                adapter!!.mData[0].selected = -1
                bundle.putSerializable("inventoryItem", switchAdapter() as Serializable)
                bundle.putString("type",type)
                FragmentHelper.switchFragment(
                    activity!!, SupplyPublishInventoryItemMoreFragment.newInstance(bundle),
                    R.id.frame_supply, ""
                )

            }
        }
        private fun switchAdapter():List<MultiStyleItem>{
            val adapterGenerate= AdapterGenerate()
            adapterGenerate.context= context!!
            adapterGenerate.activity=activity as SupplyActivity
            val mData:List<MultiStyleItem>
            when(type){
                "成员清册发布"->
                {
                    bundle.putString("type",type)
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                }
                "车辆清册发布"->
                {
                    bundle.putString("type",type)
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                }
                "工器具清册发布"->
                {
                    bundle.putString("type",type)
                    //bundle.putSerializable("listData4",arguments!!.getSerializable("listData4"))
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                }
                "租赁清册发布"->
                {
                    bundle.putString("type",type)
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                }
                "车辆表发布"->{
                    bundle.putString("type",type)
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                }
                else->{
                    bundle.putString("type","成员清册发布")
                    mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
                }
            }
            return mData
        }
        fun update(itemMultiStyleItem:List<MultiStyleItem>){
            if(adapter!!.mData[0].selected==-1){
                val mData = adapter!!.mData.toMutableList()
                if(type=="车辆清册发布"||type=="车辆表发布") {
                    mData.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SHIFT_INPUT,
                            itemMultiStyleItem[0].selectContent,
                            false
                        )
                    )
                }
                else
                {
                    mData.add(
                        MultiStyleItem(
                            MultiStyleItem.Options.SHIFT_INPUT,
                            itemMultiStyleItem[0].inputSingleContent,
                            false
                        )
                    )
                }
                mData[0].selected = mData.size-1
                if (itemMultiStyleItem[0].selected == 0) {
                    mData[mData[0].selected].necessary = false
                } else {
                        mData[mData[0].selected].necessary = true
                    }
                mData[mData.size-1].itemMultiStyleItem = itemMultiStyleItem
                mData[mData.size-1].jumpListener = View.OnClickListener {
                    mData[0].selected = mData.size-1
                    val bundle = Bundle()
                    bundle.putSerializable("inventoryItem",mData[mData.size-1].itemMultiStyleItem as Serializable)
                    bundle.putString("type",type)
                    FragmentHelper.switchFragment(mView.context as SupplyActivity,SupplyPublishInventoryItemMoreFragment.newInstance(bundle),
                        R.id.frame_supply,"")
                }
                adapter!!.mData = mData
                adapter!!.notifyItemInserted(mData.size-1)
            }
            else {
                if(type=="车辆清册发布"||type=="车辆表发布") {
                    adapter!!.mData[adapter!!.mData[0].selected].shiftInputTitle =
                        itemMultiStyleItem[0].selectContent
                }
                else
                {
                    adapter!!.mData[adapter!!.mData[0].selected].shiftInputTitle =
                        itemMultiStyleItem[0].inputSingleContent
                }
                adapter!!.mData[adapter!!.mData[0].selected].itemMultiStyleItem = itemMultiStyleItem
                adapter!!.notifyItemChanged(adapter!!.mData[0].selected)
                if (itemMultiStyleItem[0].selected == 0) {
                    adapter!!.mData[adapter!!.mData[0].selected].necessary = false
                } else {
                    adapter!!.mData[adapter!!.mData[0].selected].necessary = true
                }
            }
            val fragment = activity!!.supportFragmentManager.findFragmentByTag("register") as SupplyFragment
            fragment.update(adapter!!.mData)
        }
}