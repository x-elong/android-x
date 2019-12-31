package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyOrderActivity
import com.example.eletronicengineer.adapter.StoreTypeAdapter
import com.example.eletronicengineer.aninterface.StoresName
import com.example.eletronicengineer.fragment.shoppingmall.OrderDetailsFragment
import com.example.eletronicengineer.utils.FragmentHelper
import kotlinx.android.synthetic.main.fragment_my_order.view.*

/**
 * 项目名:    android
 * 包名:      com.example.eletronicengineer.fragment.shoppingmall
 * 文件名:    MyOrderFragment
 * 创建者:    LLH
 * 创建时间:  2019/8/16 20:36
 * 描述:      TODO
 */
class MyOrderFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_order, container, false)
        initFragment(view)
        return view
    }
    var mShopList = ArrayList<StoresName>()
    private fun initFragment(view: View) {
        view.setOnClickListener {
            activity!!.finish()
        }
        var storesName: StoresName? = null
        //给链表赋值
        for (j in 0 until 6){
            storesName = StoresName(R.drawable.storename.toString(),"九朗{$j}KW屋顶太阳能发……","280w","2019-06-06 20:06:00",View.OnClickListener {
                FragmentHelper.switchFragment(activity!!,
                    OrderDetailsFragment(),R.id.frame_my_order,"")
            },1)
            mShopList.add(storesName)
        }
        //创建适配器
        val adapter = StoreTypeAdapter(mShopList)
        //设置布局管理器
        view.rv_my_order.layoutManager = LinearLayoutManager(context)
        //设置适配器
        view.rv_my_order.adapter = adapter
    }
}