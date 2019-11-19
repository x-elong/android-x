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
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.FragmentHelper
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
        initOnClick()
        return mView
    }

    private fun initFragment() {
        val multiStyleItemList = ArrayList<MultiStyleItem>().toMutableList()
        if(type=="清单报价清册") {
            multiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.TITLE, type, "0"))
            adapter = RecyclerviewAdapter(multiStyleItemList)
            mView.rv_inventory_fragment_content.adapter = adapter
            mView.rv_inventory_fragment_content.layoutManager = LinearLayoutManager(context)
            switchAdapter()
        }
        else{
            multiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.TITLE, type, "2"))
            adapter = RecyclerviewAdapter(multiStyleItemList)
            mView.rv_inventory_fragment_content.adapter = adapter
            mView.rv_inventory_fragment_content.layoutManager = LinearLayoutManager(context)
        }

    }
    fun initOnClick(){
        adapter!!.mData[0].backListener = View.OnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        if(type!="清单报价清册") {
            adapter!!.mData[0].tvSelectListener = View.OnClickListener {
                adapter!!.mData[0].selected = -1
                bundle.putSerializable("inventoryItem", switchAdapter() as Serializable)
                FragmentHelper.switchFragment(
                    activity!!, SubmitInventoryItemMoreFragment.newInstance(bundle),
                    R.id.frame_display_demand, ""
                )
            }
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

            else->mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
        }
        return mData
    }
    fun update(itemMultiStyleItem:List<MultiStyleItem>){
        Log.i("adapter.mData size is",adapter!!.mData.toString())
        if(adapter!!.mData[0].selected==-1||type=="清单报价清册"){
            val mData = adapter!!.mData.toMutableList()
            mData.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"uuu",false))
            mData[mData.size-1].itemMultiStyleItem = itemMultiStyleItem
            mData[mData.size-1].jumpListener = View.OnClickListener {
                mData[0].selected = mData.size-1
                val bundle = Bundle()
                bundle.putSerializable("inventoryItem",mData[mData.size-1].itemMultiStyleItem as Serializable)
                FragmentHelper.switchFragment(mView.context as DemandDisplayActivity,SubmitInventoryItemMoreFragment.newInstance(bundle),
                    R.id.frame_display_demand,"")
            }
            adapter!!.mData = mData
            adapter!!.notifyItemInserted(mData.size-1)
        }
        else{
            adapter!!.mData[adapter!!.mData[0].selected].shiftInputTitle = itemMultiStyleItem[1].inputSingleContent
            adapter!!.mData[adapter!!.mData[0].selected].itemMultiStyleItem = itemMultiStyleItem
            adapter!!.notifyItemChanged(adapter!!.mData[0].selected)
        }
        val fragment = activity!!.supportFragmentManager.findFragmentByTag("register") as ApplicationSubmitFragment
        fragment.update(adapter!!.mData)
    }
}