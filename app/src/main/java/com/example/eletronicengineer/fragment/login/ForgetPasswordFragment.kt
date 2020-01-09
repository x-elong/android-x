package com.example.eletronicengineer.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.FragmentHelper
import kotlinx.android.synthetic.main.fragment_forget_password.view.*

class ForgetPasswordFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_forget_password,container,false)
        mView.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.btn_forget_password_phone.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,ForgetPasswordPhoneFragment(),R.id.frame_login,"")
        }
        mView.btn_forget_password_email.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,ForgetPasswordEmailFragment(),R.id.frame_login,"")
        }
        return mView
    }
}