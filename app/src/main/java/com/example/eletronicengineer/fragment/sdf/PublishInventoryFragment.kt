package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.DemandActivity
import com.example.eletronicengineer.activity.MyRegistrationActivity
import com.example.eletronicengineer.activity.MyReleaseActivity
import com.example.eletronicengineer.activity.SupplyDisplayActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.fragment.my.ModifyJobInformationFragment
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.FragmentHelper
import kotlinx.android.synthetic.main.fragment_with_inventory.view.*
import java.io.Serializable

class PublishInventoryFragment:Fragment() {
        companion object{
            fun newInstance(args: Bundle): PublishInventoryFragment
            {
                val submitInventoryFragment= PublishInventoryFragment()
                submitInventoryFragment.arguments=args
                return submitInventoryFragment
            }
        }
        val bundle = Bundle()
        private lateinit var type:String
        lateinit var mView: View
        var adapter: RecyclerviewAdapter?=null
        var frame = R.id.frame_demand_publish
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            mView = inflater.inflate(R.layout.fragment_with_inventory, container, false)
            type = arguments!!.getString("type")
            mView.tv_inventory_title.setText(type)
        if(activity is MyReleaseActivity)
            frame = R.id.frame_my_release
        else if(activity is MyRegistrationActivity)
            frame = R.id.frame_my_registration
        else if(activity is SupplyDisplayActivity)
            frame = R.id.frame_display_supply
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
                //增加
                adapter!!.mData[0].selected = -1
                bundle.putSerializable("inventoryItem", switchAdapter() as Serializable)
                bundle.putString("type",type)
                FragmentHelper.switchFragment(activity!!, PublishInventoryItemMoreFragment.newInstance(bundle), frame, "")
            }

            for (j in adapter!!.mData){
                j.jumpListener = View.OnClickListener {
                    //修改
                    adapter!!.mData[0].selected = adapter!!.mData.indexOf(j)
                    val bundle = Bundle()
                    var itemMultiStyleItem = j.itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type",type)
                    FragmentHelper.switchFragment(activity!!,PublishInventoryItemMoreFragment.newInstance(bundle), frame,"")
                }
            }
        }
        private fun switchAdapter():List<MultiStyleItem>{
            val adapterGenerate= AdapterGenerate()
            adapterGenerate.context= context!!
            adapterGenerate.activity= if(activity is DemandActivity) activity as DemandActivity else activity as MyReleaseActivity
            val mData:List<MultiStyleItem>
            when(type){
                "成员清册发布"->
                {
                    bundle.putString("type",type)
                    mData = adapterGenerate.PublishDetailList(bundle).mData
                }
                "车辆清册发布"->
                {
                    bundle.putString("type",type)
                    mData = adapterGenerate.PublishDetailList(bundle).mData
                }
                "租赁清册发布"->
                {
                    bundle.putString("type",type)
                    //bundle.putSerializable("listData4",arguments!!.getSerializable("listData4"))
                    mData = adapterGenerate.PublishDetailList(bundle).mData
                }
                "三方服务清册发布"->
                {
                    bundle.putString("type",type)
                   // bundle.putSerializable("listData4",arguments!!.getSerializable("listData4"))
                    mData = adapterGenerate.PublishDetailList(bundle).mData
                }

                else->{
                    bundle.putString("type",type)
                    mData = adapterGenerate.PublishDetailList(bundle).mData
                }
            }
            return mData
        }
        fun update(itemMultiStyleItem:List<MultiStyleItem>){
            if(adapter!!.mData[0].selected==-1){
                val mData = adapter!!.mData.toMutableList()
                if(type== "成员清册发布"|| type=="车辆清册发布") {
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
                    if(itemMultiStyleItem[1].inputUnitContent!="")
                    {
                        mData[0].PeopleNumber+=itemMultiStyleItem[1].inputUnitContent.toInt()
                    }
                    mData[mData[0].selected].necessary = true
                }
                mData[mData.size-1].itemMultiStyleItem = itemMultiStyleItem
                mData[mData.size-1].jumpListener = View.OnClickListener {
                    //修改
                    mData[0].selected = mData.size-1
                    if(mData[0].PeopleNumber>0 && mData[mData[0].selected].necessary == true&&itemMultiStyleItem[1].inputUnitContent!="")
                    {
                        mData[0].PeopleNumber-=if(mData[mData[0].selected].itemMultiStyleItem[1].inputUnitContent.isEmpty()){ 0 }
                        else{
                            mData[mData[0].selected].itemMultiStyleItem[1].inputUnitContent.toInt()
                        }
                    }
                    val bundle = Bundle()
                    var itemMultiStyleItem = mData[mData.size-1].itemMultiStyleItem
                    bundle.putSerializable("inventoryItem",itemMultiStyleItem as Serializable)
                    bundle.putString("type",type)
                    FragmentHelper.switchFragment(activity!!,PublishInventoryItemMoreFragment.newInstance(bundle), frame,"")
                }
                adapter!!.mData = mData
                adapter!!.notifyItemInserted(mData.size-1)
            }
            else {
                if(type== "成员清册发布"|| type=="车辆清册发布") {
                    adapter!!.mData[adapter!!.mData[0].selected].shiftInputTitle =
                        itemMultiStyleItem[0].selectContent
                }
                else
                {
                    adapter!!.mData[adapter!!.mData[0].selected].shiftInputTitle =
                        itemMultiStyleItem[0].inputSingleContent
                }

                if (itemMultiStyleItem[0].selected == 0) {
                    adapter!!.mData[adapter!!.mData[0].selected].necessary = false
                } else {
                    if(itemMultiStyleItem[1].inputUnitContent!="")
                    {
                        adapter!!.mData[0].PeopleNumber=adapter!!.mData[0].PeopleNumber - adapter!!.mData[adapter!!.mData[0].selected].itemMultiStyleItem[1].inputUnitContent.toInt() + itemMultiStyleItem[1].inputUnitContent.toInt()
                    }
                    adapter!!.mData[adapter!!.mData[0].selected].necessary = true
                }
                adapter!!.mData[adapter!!.mData[0].selected].itemMultiStyleItem = itemMultiStyleItem
                adapter!!.notifyItemChanged(adapter!!.mData[0].selected)
            }
            val fragment = activity!!.supportFragmentManager.findFragmentByTag("register")
            if(fragment is DemandFragment)
                fragment.update(adapter!!.mData)
            else if(fragment is ModifyJobInformationFragment)
                fragment.update(adapter!!.mData)
        }
}