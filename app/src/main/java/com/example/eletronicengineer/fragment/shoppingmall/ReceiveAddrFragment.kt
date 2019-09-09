package com.example.eletronicengineer.fragment.shoppingmall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.PersonInformationActivity
import com.example.eletronicengineer.adapter.ShippingAddressAdapter
import com.example.eletronicengineer.aninterface.ShippingAddress
import com.example.eletronicengineer.fragment.shopping.AddressFragment
import kotlinx.android.synthetic.main.fragment_reseive_addr.view.*

/**
 * 项目名:    android
 * 包名:      com.example.eletronicengineer.fragment.shoppingmall
 * 文件名:    MyOrderFragment
 * 创建者:    LLH
 * 创建时间:  2019/8/16 20:36
 * 描述:      TODO
 */
class ReceiveAddrFragment:Fragment() {
    var mShippingAddressList:MutableList<ShippingAddress> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reseive_addr, container, false)
        if(mShippingAddressList.size==0)
        initData(view)
        //设置布局管理器
        view.rv_receive_addr.layoutManager = LinearLayoutManager(context)
        //设置适配器
        view.rv_receive_addr.adapter = ShippingAddressAdapter(mShippingAddressList)
        view.bt_new_address.setOnClickListener {
            val data = Bundle()
            data.putString("title", "添加收货地址")
            (activity as PersonInformationActivity).switchFragment(AddressFragment.newInstance(data))
        }
        return view
    }
    private fun initData(view: View) {
        //给链表赋值
        for (j in 0 until 6){
            val shippingAddress = ShippingAddress("Gavin","13574141625","湖南省娄底市娄星区大科街道湖南人文科技学院",View.OnClickListener {
                val data = Bundle()
                data.putString("title", "编辑收货地址")
                (activity as PersonInformationActivity).switchFragment(AddressFragment.newInstance(data))
            })
            mShippingAddressList.add(shippingAddress)
        }
    }
}