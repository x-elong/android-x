package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.fragment_logout.view.*

class LogoutFragment :Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_logout,container,false)
        view.tv_logout_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        view.btn_logout.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        return view
    }
}