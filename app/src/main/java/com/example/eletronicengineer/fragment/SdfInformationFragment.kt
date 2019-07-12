package com.example.eletronicengineer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MainActivity
import kotlinx.android.synthetic.main.sdf.*
import kotlinx.android.synthetic.main.sdf.view.*
import kotlinx.android.synthetic.main.sdf_information.view.*

class SdfInformationFragment:Fragment(){
    lateinit var titleReturn:View
    lateinit var titleContent:TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sdf_information,container,false)
        titleReturn=activity!!.return_iv
        titleContent=activity!!.title_tv
        initonClick(view)
        return view
    }
    fun initonClick(view: View){
        view.demand_release_tv.setOnClickListener {
            switchFragment(DemandInformationFragment(),R.id.fragment_sdf)
            titleContent.text="需求发布"
            titleReturn.visibility=View.VISIBLE
            titleReturn.setOnClickListener {
                activity!!.supportFragmentManager.popBackStackImmediate()
                titleReturn.visibility=View.INVISIBLE
                titleContent.text="招投供需"
            }
        }
        view.supply_announcement_tv.setOnClickListener {
            switchFragment(SupplyInformationFragment(),R.id.fragment_sdf)
            titleContent.text="供应发布"
            titleReturn.visibility=View.VISIBLE
            titleReturn.setOnClickListener {
                activity!!.supportFragmentManager.popBackStackImmediate()
                titleReturn.visibility=View.INVISIBLE
                titleContent.text="招投供需"
            }
        }
    }
    fun switchFragment(fragment: Fragment,frameLayout:Int) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        //隐藏上个Fragment
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}