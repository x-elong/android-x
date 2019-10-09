package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.GlideLoader

class PersonalCertificationShowFragment :Fragment(){
    val glideLoader = GlideLoader()
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_personal_certification_show,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {

    }
}