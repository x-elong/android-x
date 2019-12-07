package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyInformationActivity
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import kotlinx.android.synthetic.main.fragment_bind_phone.view.*

class BindPhoneFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):BindPhoneFragment{
            val bindPhoneFragment = BindPhoneFragment()
            bindPhoneFragment.arguments = args
            return bindPhoneFragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bind_phone,container,false)
        view.tv_bind_phone_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        view.tv_bind_phone_number.text="当前绑定手机号:"+UnSerializeDataBase.userPhone
        view.btn_modify_phone.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,ModifyPhoneFragment(),R.id.frame_my_information,"")
        }
        return view
    }
}