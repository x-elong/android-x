package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.SubscribeActivity
import kotlinx.android.synthetic.main.fragment_confirmed_subscribe.view.*

class ConfirmedSubscribeFragment :Fragment(){

    companion object{
        fun newInstance(args:Bundle):ConfirmedSubscribeFragment{
            val confirmedSubscribeFragment = ConfirmedSubscribeFragment()
            confirmedSubscribeFragment.arguments = args
            return confirmedSubscribeFragment
        }
    }
    var subscribeItems = ArrayList<Int>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_confirmed_subscribe,container,false)
        subscribeItems = arguments!!.getIntegerArrayList("SubscribeContent")
        view.tv_confirmed_subscribe_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        for (j in subscribeItems){
            when(j){
                1->view.tv_vip_member.visibility=View.VISIBLE
                2->view.tv_company_management.visibility=View.VISIBLE
                3->view.tv_project_management.visibility=View.VISIBLE
                4->view.tv_mall_system.visibility=View.VISIBLE
            }
        }
        view.btn_pay.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("paymentAmount", Int.MAX_VALUE)
            (activity as SubscribeActivity).switchFragment(PaymentFragment.newInstance(bundle),"")
        }
        return view
    }
}