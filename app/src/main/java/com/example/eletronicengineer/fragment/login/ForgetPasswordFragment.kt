package com.example.eletronicengineer.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.LoginActivity
import kotlinx.android.synthetic.main.fragment_forget_password.view.*

class ForgetPasswordFragment: Fragment()  {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_forget_password,container,false)
        initFragment(view)
        return view
    }

    private fun initFragment(v: View) {
        v.tv_forget_password_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        v.tv_forget_password_next.setOnClickListener {

        }
    }
}