package com.example.eletronicengineer.fragment.shoppingmall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.my.MyOrderFragment
import com.example.eletronicengineer.utils.GlideLoader
import kotlinx.android.synthetic.main.fragment_person_information.view.*

/**
 * 项目名:    android
 * 包名:      com.example.eletronicengineer.fragment.shoppingmall
 * 文件名:    PersonInformationFragment
 * 创建者:    LLH
 * 创建时间:  2019/8/16 16:43
 * 描述:      TODO
 */
class PersonInformationFragment:Fragment() {
    lateinit var mTitle: ArrayList<String>
    lateinit var mFragment: ArrayList<Fragment>
    lateinit var mData:MutableList<MultiStyleItem>
    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_person_information, container, false)
        ininData()
        initView()
        return mView
    }

    private fun ininData() {
        mTitle = ArrayList()
        mTitle.add("购物订单")
        mTitle.add("收货地址")
        mFragment = ArrayList()
        mFragment.add(MyOrderFragment())
        mFragment.add(ReceiveAddrFragment())
        GlideLoader().loadImage(mView.tv_person_inform_header,"xx")
    }

    private fun initView() {
        mView.tv_person_inform_back.setOnClickListener {
            activity!!.finish()
        }
        //预加载
        mView.viewpager_person.offscreenPageLimit = mFragment.size
        //设置适配器
        mView.viewpager_person.adapter = viewPagerAdapter(childFragmentManager,mFragment,mTitle)
        //绑定
        mView.mTabLayout_person.setupWithViewPager(mView.viewpager_person)
    }
    class viewPagerAdapter(fm:FragmentManager?,var mList:ArrayList<Fragment>,var mTitle:ArrayList<String>):FragmentPagerAdapter(fm){
        override fun getItem(position: Int): Fragment {
            return mList.get(position)
        }
        override fun getCount(): Int {
            return mList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mTitle.get(position)
        }
    }
}