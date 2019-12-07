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
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.FragmentHelper
import kotlinx.android.synthetic.main.fragment_with_inventory.view.*
import java.io.Serializable

class InventoryFragment : Fragment() {
    companion object{
        fun newInstance(args:Bundle): InventoryFragment
        {
            val inventoryFragment= InventoryFragment()
            inventoryFragment.arguments=args
            return inventoryFragment
        }
    }
    private lateinit var type:String
    lateinit var mView: View
    var adapter:RecyclerviewAdapter?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_with_inventory, container, false)
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
        multiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.TITLE,type,"2"))
        adapter = RecyclerviewAdapter(multiStyleItemList)
        mView.rv_inventory_fragment_content.adapter = adapter
        mView.rv_inventory_fragment_content.layoutManager = LinearLayoutManager(context)
    }
    fun initOnClick(){
        adapter!!.mData[0].backListener = View.OnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        adapter!!.mData[0].tvSelectListener = View.OnClickListener {
            adapter!!.mData[0].selected = -1

            val bundle = Bundle()
            bundle.putSerializable("inventoryItem", switchAdapter() as Serializable)
            FragmentHelper.switchFragment(activity!!,InventoryItemMoreFragment.newInstance(bundle),R.id.frame_supply,"")
        }
    }
    private fun switchAdapter():List<MultiStyleItem>{
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as SupplyActivity
        val mData:List<MultiStyleItem>
        when(type){
            "成员清册"->mData = adapterGenerate.inventoryItemType().mData
            else->mData = adapterGenerate.inventoryItemType().mData
        }
        return mData
    }
    fun update(itemMultiStyleItem:List<MultiStyleItem>){
        Log.i("adapter.mData size is",adapter!!.mData.size.toString())
        if(adapter!!.mData[0].selected==-1){
            val mData = adapter!!.mData.toMutableList()
            mData.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,itemMultiStyleItem[1].inputSingleContent,false))
            mData[mData.size-1].itemMultiStyleItem = itemMultiStyleItem
            mData[mData.size-1].jumpListener = View.OnClickListener {
                mData[0].selected = mData.size-1
                val bundle = Bundle()
                bundle.putSerializable("inventoryItem",mData[mData.size-1].itemMultiStyleItem as Serializable)
                FragmentHelper.switchFragment(mView.context as SupplyActivity,InventoryItemMoreFragment.newInstance(bundle),R.id.frame_supply,"")
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