package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.aninterface.CheckBoxStyle
import kotlinx.android.synthetic.main.fragment_edit_emergency_contact.view.*

class EditEmergencyContactFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):EditEmergencyContactFragment{
            val editEmergencyContactFragment = EditEmergencyContactFragment()
            editEmergencyContactFragment.arguments = args
            return editEmergencyContactFragment
        }
    }
    lateinit var mView: View
    var adapter: RecyclerviewAdapter?=null
    var style = -1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_edit_emergency_contact,container,false)
        style = arguments!!.getInt("style")
        if(style==0)
            mView.btn_delete_emergency.visibility=View.GONE
        mView.tv_edit_emergency_contact_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.tv_edit_emergency_ok.setOnClickListener {
            val networkAdapter = NetworkAdapter(adapter!!.mData,context!!)
            if(networkAdapter.check()){
                mView.tv_edit_emergency_contact_back.callOnClick()
            }

        }
        mView.btn_delete_emergency.setOnClickListener {
            val toast = Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER,0,0)
            toast.show()
            mView.tv_edit_emergency_contact_back.callOnClick()
        }
        initFragment()
        return mView
    }
    private fun initFragment() {
        val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"姓名"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"关系"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"电话"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"身份证号码"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"地址"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_TEXTAREA,"备注","",false))
        if(style==1){
            mMultiStyleItemList[0].inputSingleContent = "张凌"
            mMultiStyleItemList[1].inputSingleContent = "张凌"
            mMultiStyleItemList[2].inputSingleContent = "张凌"
            mMultiStyleItemList[3].inputSingleContent = "张凌"
            mMultiStyleItemList[4].inputSingleContent = "张凌"
            mMultiStyleItemList[5].textAreaContent = "张凌"
        }
        adapter= RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_edit_emergency_contact_content.adapter = adapter
        mView.rv_edit_emergency_contact_content.layoutManager = LinearLayoutManager(context)
    }
}