package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.FragmentHelper
import kotlinx.android.synthetic.main.fragment_vip_privileges_more.view.*

class VipPrivilegesMoreFragment :Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vip_privileges_more,container,false)
        view.tv_vip_privileges_more_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        view.btn_subscribe.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("PaymentFragment",50)
            FragmentHelper.switchFragment(activity!!,PaymentFragment.newInstance(bundle),R.id.frame_vip,"")
        }
        return view
    }
}