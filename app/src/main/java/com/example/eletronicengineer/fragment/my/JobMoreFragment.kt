package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyReleaseActivity
import kotlinx.android.synthetic.main.fragment_job_more.view.*

class JobMoreFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):JobMoreFragment{
            val jobMoreFragment = JobMoreFragment()
            jobMoreFragment.arguments = args
            return jobMoreFragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_job_more,container,false)
        view.tv_job_more_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        view.btn_edit_job_information.setOnClickListener {
            val bundle = Bundle()
            (activity as MyReleaseActivity).switchFragment(ModifyJobInformationFragment.newInstance(bundle))
        }
        return view
    }
}