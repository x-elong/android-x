package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyInformationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import kotlinx.android.synthetic.main.fragment_education_information.view.*

class EducationInformationFragment :Fragment(){
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_education_information,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        val mMultiStyleItemList=ArrayList<MultiStyleItem>()
        var item = MultiStyleItem(MultiStyleItem.Options.TITLE,"银行卡信息","2")
        mMultiStyleItemList.add(item)
        item.backListener = View.OnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        item.tvSelect = View.OnClickListener {
            val bundle = Bundle()
            (activity as MyInformationActivity).switchFragment(AddEducationInformationFragment.newInstance(bundle),"AddEducation")
        }
        item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"学历","毕业时间","毕业院校+专业","编辑")
        item.jumpListener = View.OnClickListener {
            val bundle = Bundle()
            (activity as MyInformationActivity).switchFragment(AddEducationInformationFragment.newInstance(bundle),"AddEducation")
        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"本科","2019-10-01","湖南人文科技学院物联网工程","编辑")
        item.jumpListener = View.OnClickListener {
            val bundle = Bundle()
            (activity as MyInformationActivity).switchFragment(AddEducationInformationFragment.newInstance(bundle), "AddEducation")
        }
        mMultiStyleItemList.add(item)
//        (mView.rv_my_release_content.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        mView.rv_education_information_content.adapter= RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_education_information_content.layoutManager= LinearLayoutManager(context)
    }
}