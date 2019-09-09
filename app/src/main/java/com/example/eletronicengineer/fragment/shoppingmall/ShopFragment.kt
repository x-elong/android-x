package com.example.eletronicengineer.fragment.shoppingmall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.SearchActivity
import kotlinx.android.synthetic.main.fragment_shop.view.*

/**
 * 项目名:    android
 * 包名:      com.example.eletronicengineer.fragment.shoppingmall
 * 文件名:    MyOrderFragment
 * 创建者:    LLH
 * 创建时间:  2019/8/16 20:36
 * 描述:      TODO
 */
class ShopFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)
        initFragment(view)
        return view
    }

    private fun initFragment(view: View) {
        view.tv_shop_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        view.tv_shift_shop_details.setOnClickListener {
            (activity as SearchActivity).switchFragment(ShopDetailsFragment(),"")
        }
    }
}