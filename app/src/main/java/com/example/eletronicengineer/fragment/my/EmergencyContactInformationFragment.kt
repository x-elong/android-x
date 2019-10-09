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
import org.json.JSONObject

class EmergencyContactInformationFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):EmergencyContactInformationFragment{
            val emergencyContactInformationFragment = EmergencyContactInformationFragment()
            emergencyContactInformationFragment.arguments = args
            return emergencyContactInformationFragment
        }
    }
    lateinit var json: JSONObject
    lateinit var mView: View
    var adapter = RecyclerviewAdapter(ArrayList())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_emergency_contact_information,container,false)
        json = JSONObject(arguments!!.getString("urgentPeople"))
        mView.tv_emergency_contact_information_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.btn_edit_emergency_information.setOnClickListener {
            mView.tv_emergency_contact_information_back.callOnClick()
            val bundle = Bundle()
            bundle.putInt("style",1)
            bundle.putString("urgentPeople",json.toString())
            (activity as MyInformationActivity).switchFragment(EditEmergencyContactFragment.newInstance(bundle),"")
        }
        initFragment()
        return mView
    }

    private fun initFragment() {
        val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"姓名",json.getString("urgentPeopleName")))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"关系",json.getString("relation")))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"电话",json.getString("phone")))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"身份证号码",json.getString("urgentPeopleId")))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"地址",json.getString("address")))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"备注",""))
        if(!json.isNull("additonalExplain"))
            mMultiStyleItemList[mMultiStyleItemList.size-1].singleDisplayRightContent = json.getString("additonalExplain")
        adapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_emergency_contact_information_content.adapter = adapter
        mView.rv_emergency_contact_information_content.layoutManager = LinearLayoutManager(context)
    }
}