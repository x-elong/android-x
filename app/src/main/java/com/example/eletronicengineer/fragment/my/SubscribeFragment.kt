package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.SubscribeActivity
import kotlinx.android.synthetic.main.fragment_subscribe.view.*

class SubscribeFragment :Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_subscribe,container,false)
        view.tv_subscribe_back.setOnClickListener {
            activity!!.finish()
        }
        view.btn_confirmed_subscribe.setOnClickListener {
            val subscribeItems = ArrayList<Int>()
            if(view.checkbox_vip_member.isChecked){
                subscribeItems.add(1)
            }
            if(view.checkbox_company_management.isChecked){
                subscribeItems.add(2)
            }
            if(view.checkbox_project_management.isChecked){
                subscribeItems.add(3)
            }
            if(view.checkbox_mall_system.isChecked){
                subscribeItems.add(4)
            }
            if(subscribeItems.size==0){
                val toast = Toast.makeText(context,"请选择开通的服务",Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER,0,0)
                toast.show()
            }
            else{
                val bundle = Bundle()
                bundle.putIntegerArrayList("SubscribeContent",subscribeItems)
                (activity as SubscribeActivity).switchFragment(ConfirmedSubscribeFragment.newInstance(bundle),"")
            }

        }
        return view
    }
}