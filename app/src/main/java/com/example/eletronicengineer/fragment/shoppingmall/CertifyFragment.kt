package com.example.eletronicengineer.fragment.shoppingmall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.StoreTypeAdapter
import com.example.eletronicengineer.aninterface.StoresName
import kotlinx.android.synthetic.main.fragment_shop_certify.view.*
import kotlinx.android.synthetic.main.fragment_shop_list.view.*

/**
 * 项目名:    android
 * 包名:      com.example.eletronicengineer.fragment.shoppingmall
 * 文件名:    DescFragment
 * 创建者:    LLH
 * 创建时间:  2019/8/17 10:55
 * 描述:      TODO
 */
class CertifyFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop_certify, container, false)
        initFragment(view)
        return view
    }
    var mCertifyList = ArrayList<Int>()
    private fun initFragment(view: View) {
        //给链表赋值
        for (j in 0 until 6){
            mCertifyList.add(R.drawable.image)
        }
//        //创建适配器
//        val adapter = CertifyAdapter(mCertifyList)
//        //设置布局管理器
//        view.rv_shop_certify.layoutManager = LinearLayoutManager(context)
//        //设置适配器
//        view.rv_shop_certify.adapter = adapter
    }
}