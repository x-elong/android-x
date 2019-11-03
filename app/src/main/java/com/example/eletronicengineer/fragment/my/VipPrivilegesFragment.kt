package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.FragmentHelper
import kotlinx.android.synthetic.main.fragment_vip_privileges.view.*

class VipPrivilegesFragment :Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vip_privileges,container,false)
        view.tv_vip_privileges_back.setOnClickListener {
            activity!!.finish()
        }
        view.tv_vip1.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,VipPrivilegesMoreFragment(),R.id.frame_vip,"")
        }
        view.tv_vip2.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,VipPrivilegesMoreFragment(),R.id.frame_vip,"")
        }
        view.tv_vip3.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,VipPrivilegesMoreFragment(),R.id.frame_vip,"")
        }
        return view
    }
}