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
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.application_apply_fragment.view.*
class ApplicationSubmitFragment:Fragment() {
    lateinit var mView: View
    var adapter: RecyclerviewAdapter?=null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mView=inflater.inflate(R.layout.application_apply_fragment,container,false)
        if(adapter==null)
            initData()
        else{
            mView.tv_application_content.adapter=adapter
            mView.tv_application_content.layoutManager=LinearLayoutManager(context)
        }
        return mView
    }
    private fun initData()
    {
        // 加载哪个清册
        switchAdapter()
        mView.tv_application_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.tv_application_ok.setOnClickListener {

        }
        mView.tv_application_content.adapter=adapter
        mView.tv_application_content.layoutManager=LinearLayoutManager(context)
    }

    private fun switchAdapter() {
        val adapterGenerate=AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as SupplyActivity
        when(arguments!!.getInt("type")){
            Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal-> adapter=adapterGenerate.inventoryType()
        }
    }

    fun update(itemMultiStyleItem:List<MultiStyleItem>){
        Log.i("position is",adapter!!.mData[0].selected.toString())
        adapter!!.mData[adapter!!.mData[0].selected].itemMultiStyleItem = itemMultiStyleItem
        Log.i("item size",adapter!!.mData[adapter!!.mData[0].selected].itemMultiStyleItem.size.toString())
    }
}