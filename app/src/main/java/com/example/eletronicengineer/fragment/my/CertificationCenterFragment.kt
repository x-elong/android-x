package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import kotlinx.android.synthetic.main.fragment_certification_center.view.*

class CertificationCenterFragment :Fragment(){
    lateinit var mView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_certification_center,container,false)
        mView.tv_certification_center_back.setOnClickListener {
            activity!!.finish()
        }
        mView.btn_personal_certification.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,PersonalCertificationShowFragment(),R.id.frame_my_certification,"Certification")
        }
        mView.btn_enterprise_certification.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,EnterpriseCertificationShowFragment(),R.id.frame_my_certification,"Certification")
        }
        if(!UnSerializeDataBase.isCertificate){
            mView.btn_personal_certification.callOnClick()
        }
        return mView
    }
}