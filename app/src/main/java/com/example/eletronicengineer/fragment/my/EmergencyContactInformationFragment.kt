package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyInformationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import kotlinx.android.synthetic.main.fragment_emergency_contact_information.view.*

class EmergencyContactInformationFragment :Fragment(){
    lateinit var mView: View
    var adapter:RecyclerviewAdapter?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_emergency_contact_information,container,false)
        mView.tv_emergency_contact_information_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.btn_edit_emergency_information.setOnClickListener {
            mView.tv_emergency_contact_information_back.callOnClick()
            val bundle = Bundle()
            bundle.putInt("style",1)
            (activity as MyInformationActivity).switchFragment(EditEmergencyContactFragment.newInstance(bundle),"")

        }
        initFragment()
        return mView
    }

    private fun initFragment() {
        val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"姓名","张凌"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"关系","张凌"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"电话","张凌"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"身份证号码","张凌"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"地址","张凌"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"备注","张凌"))
        adapter= RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_emergency_contact_information_content.adapter = adapter
        mView.rv_emergency_contact_information_content.layoutManager = LinearLayoutManager(context)
    }
}