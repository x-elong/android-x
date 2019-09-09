package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.fragment_my_information.view.*


class MyInformationFragment : Fragment(){
    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_my_information,container,false)
        mView.tv_my_information_back.setOnClickListener {
            activity!!.finish()
        }
        return mView
    }
}