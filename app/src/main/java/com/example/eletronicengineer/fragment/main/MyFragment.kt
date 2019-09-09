package com.example.eletronicengineer.fragment.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.*
import com.example.eletronicengineer.adapter.FunctionAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.aninterface.Function
import kotlinx.android.synthetic.main.my.view.*

class MyFragment : Fragment() {
    lateinit var mView:View
    val mFunctionList:MutableList<Function> = ArrayList()
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.my, container, false)
        initData()
        initOnClick()
        return mView
    }

    private fun initOnClick() {
        mView.view_my.setOnClickListener {
            val intent =Intent(activity,MyInformationActivity::class.java)
            startActivity(intent)
        }
        mView.tv_my_phone.setOnClickListener {
            Toast.makeText(context,mView.tv_my_phone.text,Toast.LENGTH_SHORT).show()
        }
        mView.tv_qr_code.setOnClickListener {
            val intent =Intent(activity,MyQRCodeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initData() {
        mFunctionList.clear()
        mMultiStyleItemList.clear()
        mFunctionList.add(Function("我的发布",R.drawable.my_release,null))
        mFunctionList.add(Function("我的订单",R.drawable.my_order,View.OnClickListener {
            val intent = Intent(activity,MyOrderActivity::class.java)
            startActivity(intent)
        }))
        mFunctionList.add(Function("收货地址",R.drawable.shipping_address,View.OnClickListener {
            val intent = Intent(activity,ShippingAddressActivity::class.java)
            startActivity(intent)
        }))
        mFunctionList.add(Function("分销",R.drawable.distribution,View.OnClickListener {
            val intent = Intent(activity,RetailStoreActivity::class.java)
            startActivity(intent)
        }))
        mView.rv_my_function_content.adapter=FunctionAdapter(mFunctionList)
        mView.rv_my_function_content.layoutManager = GridLayoutManager(context,4)

        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"签名设置",false))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"认证管理",false))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"帮助中心",false))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"推荐电企通",false))
        mView.rv_my_other_function_content.adapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_my_other_function_content.layoutManager = LinearLayoutManager(context)
    }
}