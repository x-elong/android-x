package com.example.eletronicengineer.fragment.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.SearchActivity
import kotlinx.android.synthetic.main.fragment_goods_details.view.*

class GoodsDetailsFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_goods_details, container, false)
        initFragment(view)
        return view
    }
    private fun initFragment(v: View) {
        v.tv_goods_details_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        v.tv_goods_buy.setOnClickListener {
            (activity as SearchActivity).switchFragment(OrderConfirmFragment(),"orderConfirm")
        }
    }
}