package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyInformationActivity
import kotlinx.android.synthetic.main.fragment_bind_phone.view.*

class BindPhoneFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):BindPhoneFragment{
            val bindPhoneFragment = BindPhoneFragment()
            bindPhoneFragment.arguments = args
            return bindPhoneFragment
        }
    }
    var phone = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bind_phone,container,false)
        view.tv_bind_phone_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        view.btn_modify_phone.setOnClickListener {
            val bundle=Bundle()
            bundle.putString("phone",phone)
            (activity as MyInformationActivity).switchFragment( ModifyPhoneFragment.newInstance(bundle),"")
        }
        return view
    }
}