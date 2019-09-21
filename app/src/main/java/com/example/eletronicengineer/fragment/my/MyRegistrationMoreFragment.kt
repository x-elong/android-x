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
            }
            Constants.FragmentType.DEMAND_GROUP_TYPE.ordinal-> {
                adapter = adapterGenerate.registrationDisplayDemandGroup()
            }
            Constants.FragmentType.DEMAND_LEASE_TYPE.ordinal-> {
                adapter = adapterGenerate.registrationDisplayDemandLease()
            }
            Constants.FragmentType.DEMAND_TRIPARTITE_TYPE.ordinal-> {
                adapter = adapterGenerate.registrationDisplayDemandTripartite()
            }
        }
        mView.rv_registration_more_content.adapter = adapter
        mView.rv_registration_more_content.layoutManager = LinearLayoutManager(context)
    }
}