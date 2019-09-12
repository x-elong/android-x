package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyCertificationActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_my_certification.view.*

class MyCertificationFragment :Fragment(){
    lateinit var mView:View
    var selectedPosition = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_my_certification,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_my_certification_back.setOnClickListener {
            activity!!.finish()
        }
        val mTitle = arrayListOf("个人认证","企业认证")
        for(j in mTitle){
            mView.mTabLayout_my_certification.addTab(mView.mTabLayout_my_certification.newTab().setText(j))
        }
        if(selectedPosition==0)
            (activity as MyCertificationActivity).switchFragment(PersonalCertificationFragment(),R.id.frame_certification_category,"Certification")
        else {
            mView.mTabLayout_my_certification.getTabAt(selectedPosition)!!.select()
            (activity as MyCertificationActivity).switchFragment(EnterpriseCertificationFragment(), R.id.frame_certification_category, "EnterpriseCertification")
        }
        mView.mTabLayout_my_certification.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab) {
                Log.i("select is",p0.text.toString())
                if(p0.text=="个人认证"){
                    selectedPosition=0
                    (activity as MyCertificationActivity).switchFragment(PersonalCertificationFragment(),R.id.frame_certification_category,"Certification")
                }else{
                    selectedPosition=1
                    (activity as MyCertificationActivity).switchFragment(EnterpriseCertificationFragment(),R.id.frame_certification_category,"EnterpriseCertification")
                }
            }

        })
    }

}