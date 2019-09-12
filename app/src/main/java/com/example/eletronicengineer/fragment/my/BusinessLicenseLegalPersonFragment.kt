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
import kotlinx.android.synthetic.main.fragment_business_license_legal_person.view.*

class BusinessLicenseLegalPersonFragment :Fragment(){

    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView=inflater.inflate(R.layout.fragment_business_license_legal_person,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_business_license_legal_person_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.btn_authorize.setOnClickListener {
            if(mView.radio_button_legal_person.isChecked){
                val networkAdapter = NetworkAdapter(mMultiStyleItemList,context!!)
                if(networkAdapter.check()){

                }
            }else{
                val toast = Toast.makeText(context,"请同意《电企通认证服务协议》",Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER,0,0)
                toast.show()
            }
        }
        mMultiStyleItemList.clear()
        var item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"企业名称：","请输入企业名称")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"企业注册号：","如无可填统一社会信用代码")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"法人姓名：","请输入法人姓名")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"法人身份证号：","请输入法人身份证号")
        mMultiStyleItemList.add(item)
        mView.rv_legal_person_content.adapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_legal_person_content.layoutManager= LinearLayoutManager(context)
    }
}