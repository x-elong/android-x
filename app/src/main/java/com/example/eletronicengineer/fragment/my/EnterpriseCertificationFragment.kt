package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyCertificationActivity
import kotlinx.android.synthetic.main.fragment_enterprise_certification.view.*

class EnterpriseCertificationFragment :Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_enterprise_certification,container,false)
        view.tv_business_license_show.setOnClickListener {
            (activity as MyCertificationActivity).switchFragment(BusinessLicenseFragment(),R.id.frame_my_certification,"Certification")
        }
        view.tv_business_license_legal_person_show.setOnClickListener {
            (activity as MyCertificationActivity).switchFragment(BusinessLicenseLegalPersonFragment(),R.id.frame_my_certification,"")
        }
        return view
    }
}