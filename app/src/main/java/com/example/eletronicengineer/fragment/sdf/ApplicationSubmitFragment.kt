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
import com.example.eletronicengineer.distributionFileSave.RequirementPersonDetail
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.application_apply_fragment.view.*

class ApplicationSubmitFragment:Fragment() {
    companion object{
        fun newInstance(args:Bundle):ApplicationSubmitFragment{
            val applicationSubmitFragment = ApplicationSubmitFragment()
            applicationSubmitFragment.arguments = args
            return applicationSubmitFragment
        }
    }
    var MemberDataSize:Int = 0
    var VehicleDataSize:Int = 0
    var MachineDataSize:Int = 0
    var ClearWorkSalaryDataSize:Int = 0
    var ClearSingleSalaryDataSize:Int = 0
    lateinit var mView: View
    var adapter: RecyclerviewAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView=inflater.inflate(R.layout.application_apply_fragment,container,false)
        if(adapter==null){
            switchAdapter()
            mView.tv_application_content.adapter=adapter
            mView.tv_application_content.layoutManager=LinearLayoutManager(context)
        }
        else{
            mView.tv_application_content.adapter=adapter
            mView.tv_application_content.layoutManager=LinearLayoutManager(context)
        }
        return mView
    }

    private fun switchAdapter() {
        val adapterGenerate=AdapterGenerate()
        adapterGenerate.context= context!!
        adapterGenerate.activity=activity as DemandDisplayActivity
        var bundle=Bundle()
        when(arguments!!.getInt("type")){
            Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal->
            {
                MemberDataSize=arguments!!.getString("needPeopleNumber").toInt()
                bundle.putInt("type",arguments!!.getInt("type"))
                bundle.putSerializable("RequirementPersonDetail",arguments!!.getSerializable("RequirementPersonDetail"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
//                val temp = arguments!!.getSerializable("RequirementPersonDetail") as RequirementPersonDetail
//                Log.i("jjj" ,temp.additonalExplain)
            }
            Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal->//主网
            {
                bundle.putInt("type",arguments!!.getInt("type"))
                bundle.putSerializable("RequirementMajorNetWork",arguments!!.getSerializable("RequirementMajorNetWork"))
                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
                bundle.putSerializable("listData2",arguments!!.getSerializable("listData2"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
            Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal->//变电
            {
                bundle.putInt("type",arguments!!.getInt("type"))

                MemberDataSize=arguments!!.getString("needPeopleNumber").toInt()
                bundle.putSerializable("RequirementPowerTransformation",arguments!!.getSerializable("RequirementPowerTransformation"))
                bundle.putSerializable("listData1",arguments!!.getSerializable("listData1"))
                bundle.putSerializable("listData2",arguments!!.getSerializable("listData2"))
                adapter=adapterGenerate.ApplicationSubmit(bundle)
            }
        }


    }

    fun update(itemMultiStyleItem:List<MultiStyleItem>){
        for (j in itemMultiStyleItem) {
            when (j.options) {
                MultiStyleItem.Options.TITLE -> {
                    if (j.title1 == "成员清册")
                    {
                        if (itemMultiStyleItem.size >= MemberDataSize) {
                            adapter!!.mData[0].necessary = true
                        }
                    }
                    if(j.title1== "车辆清册")
                    {

                    }
                }
            }
        }

        Log.i("position is",adapter!!.mData[0].selected.toString())
        adapter!!.mData[adapter!!.mData[0].selected].itemMultiStyleItem = itemMultiStyleItem
        Log.i("item size",adapter!!.mData[adapter!!.mData[0].selected].itemMultiStyleItem.size.toString())
    }
}