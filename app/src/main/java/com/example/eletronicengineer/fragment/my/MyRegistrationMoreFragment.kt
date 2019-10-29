package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.electric.engineering.model.MultiStyleItem
import com.electric.engineering.utils.ItemGenerate
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyRegistrationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_my_registration.*
import kotlinx.android.synthetic.main.fragment_my_registration_more.view.*
import org.json.JSONObject

class MyRegistrationMoreFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):MyRegistrationMoreFragment{
            val myRegistrationMoreFragment = MyRegistrationMoreFragment()
            myRegistrationMoreFragment.arguments = args
            return myRegistrationMoreFragment
        }
    }

    lateinit var mView: View
    val mMultiStyleItemList:MutableList<MultiStyleItem> =ArrayList()
    var type = -1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_my_registration_more,container,false)
        type = arguments!!.getInt("type")
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_my_registration_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        initData()
    }

    private fun initData() {
        lateinit var adapter: RecyclerviewAdapter
        mMultiStyleItemList.clear()
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = mView.context
        adapterGenerate.activity = activity as AppCompatActivity
        when(type){
            Constants.FragmentType.DEMAND_INDIVIDUAL_TYPE.ordinal-> {
                adapter = adapterGenerate.registrationDisplayDemandIndividual()
                val json = JSONObject(arguments!!.getString("demandIndividual"))
                val js = json.getJSONObject("personRequirementInformationCheck")
                adapter.mData[1].singleDisplayRightContent = json.getString("requirementMajor")
                adapter.mData[2].singleDisplayRightContent = json.getString("name")
                adapter.mData[3].singleDisplayRightContent = json.getString("phone")
                if(!js.isNull("comment"))
                    adapter.mData[5].textAreaContent = js.getString("comment")
            }
            Constants.FragmentType.DEMAND_GROUP_TYPE.ordinal-> {
                adapter = adapterGenerate.registrationDisplayDemandGroup()
                val json = JSONObject(arguments!!.getString("demandGroup"))
                val js = json.getJSONObject("requirementTeamLoggingCheck")
                adapter.mData[0].singleDisplayRightContent = js.getString("type")
                adapter.mData[1].singleDisplayRightContent = json.getString("name")
                adapter.mData[2].singleDisplayRightContent = json.getString("phone")
                if(!js.isNull("comment"))
                    adapter.mData[8].textAreaContent = js.getString("comment")
            }
            Constants.FragmentType.DEMAND_LEASE_TYPE.ordinal-> {
                adapter = adapterGenerate.registrationDisplayDemandLease()
                val json = JSONObject(arguments!!.getString("demandLease"))
                val js = json.getJSONObject("leaseLoggingCheck")
                adapter.mData[0].singleDisplayRightContent = json.getString("vehicleType")
                adapter.mData[1].singleDisplayRightContent = json.getString("name")
                adapter.mData[2].singleDisplayRightContent = json.getString("phone")
                if(!js.isNull("comment"))
                    adapter.mData[4].textAreaContent = js.getString("comment")
            }
            Constants.FragmentType.DEMAND_TRIPARTITE_TYPE.ordinal-> {
                adapter = adapterGenerate.registrationDisplayDemandTripartite()
                val json = JSONObject(arguments!!.getString("demandTripartite"))
                val js = json.getJSONObject("requirementThirdLoggingCheck")
                adapter.mData[0].singleDisplayRightContent = json.getString("requirementVariety")
                adapter.mData[1].singleDisplayRightContent = json.getString("name")
                adapter.mData[2].singleDisplayRightContent = json.getString("phone")
                if(!js.isNull("companyName"))
                adapter.mData[3].singleDisplayRightContent = json.getString("companyName")
                if(!js.isNull("comment"))
                    adapter.mData[4].textAreaContent = js.getString("comment")
            }
        }
        mView.rv_registration_more_content.adapter = adapter
        mView.rv_registration_more_content.layoutManager = LinearLayoutManager(context)
    }
}