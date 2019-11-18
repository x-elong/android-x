package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.fragment_message_management.view.*

class MessageManagementFragment :Fragment(){
    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_message_management,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_message_management_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.tv_message_management_ok.setOnClickListener {
            Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show()
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
    }
}