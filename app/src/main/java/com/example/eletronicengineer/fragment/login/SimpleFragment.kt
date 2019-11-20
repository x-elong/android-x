package com.example.eletronicengineer.fragment.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.LoginActivity
import com.example.eletronicengineer.utils.FragmentHelper
import kotlinx.android.synthetic.main.fragment_simple.view.*

class SimpleFragment: Fragment()  {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_simple,container,false)
        initFragment(view)
        return view
    }

    private fun initFragment(v: View) {
        v.tv_simple_again.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,SetPasswordFragment(),R.id.frame_login,"")
        }

    }
}