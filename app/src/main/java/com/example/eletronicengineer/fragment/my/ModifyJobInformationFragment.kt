package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R

class ModifyJobInformationFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):ModifyJobInformationFragment{
            val modifyJobInformationFragment = ModifyJobInformationFragment()
            modifyJobInformationFragment.arguments = args
            return modifyJobInformationFragment
        }
    }
    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_modify_job_information,container,false)
        return mView
    }
}