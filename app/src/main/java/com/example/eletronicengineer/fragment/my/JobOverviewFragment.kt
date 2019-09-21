package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyReleaseActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.adapter.StoreTypeAdapter
import com.example.eletronicengineer.aninterface.StoresName
import kotlinx.android.synthetic.main.fragment_job_overview.view.*

class JobOverviewFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):JobOverviewFragment{
            val jobOverviewFragment = JobOverviewFragment()
            jobOverviewFragment.arguments = args
            return jobOverviewFragment
        }
    }
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_job_overview,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_job_overview_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.btn_more.setOnClickListener {
            val bundle = Bundle()
            (activity as MyReleaseActivity).switchFragment(JobMoreFragment.newInstance(bundle))
        }
        initData()
    }

    private fun initData() {
        val mMultiStyleItemList = ArrayList<MultiStyleItem>()
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.STORE,"","张凌","44岁 | 湖南省 长沙市雨花区","普工 | 面议","0"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.STORE,"","张凌","44岁 | 湖南省 长沙市芙蓉广场","普工 | 5000-6000","0"))
        mView.rv_registration_list_content.adapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_registration_list_content.layoutManager = LinearLayoutManager(context)

    }
}