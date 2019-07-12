package com.example.eletronicengineer.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.DisplayHelper
import kotlinx.android.synthetic.main.fragment_with_recyclerview.view.*
import kotlinx.android.synthetic.main.fragment_with_table.view.*

class ShiftInputFragment:Fragment()
{
    var mContext:Context?=null
    var mActivity:FragmentActivity?=null
    var inputSingleTitle:String?=null
    lateinit var mAdapter: RecyclerviewAdapter
    lateinit var mView: View
    companion object{
        fun newInstance(args:Bundle):ShiftInputFragment
        {
            val shiftInputFragment=ShiftInputFragment()
            shiftInputFragment.arguments=args
            return shiftInputFragment
        }
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext=context
        mActivity=activity
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val type=arguments?.getInt("type")
        activity!!.supportFragmentManager.findFragmentByTag("pre")
        when(type)
        {
            Constants.FragmentType.PROVIDER_GROUP_PERSONAL_FRAGMENT.ordinal->
            {
                mView=inflater.inflate(R.layout.fragment_with_recyclerview,container,false)
                val adapterGenerate=AdapterGenerate()
                adapterGenerate.context=mContext!!
                adapterGenerate.activity=mActivity as AppCompatActivity
                val adapter=adapterGenerate.ProviderGroupPersonAdd()
                adapter.mData[4].singleDisplayContent=arguments!!.getString("title")
                mAdapter=adapter
                mView.rv_fragment_content.adapter=adapter
                mView.rv_fragment_content.layoutManager= LinearLayoutManager(context)
            }
            Constants.FragmentType.PROVIDER_GROUP_VEHICLE_FRAGMENT.ordinal->
            {
                mView=inflater.inflate(R.layout.fragment_with_recyclerview,container,false)
                val adapterGenerate=AdapterGenerate()
                adapterGenerate.context=mContext!!
                adapterGenerate.activity=mActivity as AppCompatActivity
                val adapter=adapterGenerate.ProviderGroupVehicleAdd()
                mAdapter=adapter
                mView.rv_fragment_content.adapter=adapter
                mView.rv_fragment_content.layoutManager= LinearLayoutManager(context)
            }
            Constants.FragmentType.PROVIDER_GROUP_MEMBER_FRAGMENT.ordinal->
            {
                mView=inflater.inflate(R.layout.fragment_with_table,container,false)
                val tableData=DisplayHelper.excelGenerate(Constants.EXCEL_TYPE.EXCEL_MEMBER)
                mView.st_fragment_content.tableData=tableData
            }
        }
        return mView
    }
}