package com.example.eletronicengineer.fragment.shoppingmall

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.PersonInformationActivity
import com.example.eletronicengineer.adapter.StoreTypeAdapter
import com.example.eletronicengineer.aninterface.StoresName
import kotlinx.android.synthetic.main.fragment_person_information.view.*
import kotlinx.android.synthetic.main.fragment_shop_details.view.*
import kotlinx.android.synthetic.main.fragment_shop_list.view.*
import kotlinx.android.synthetic.main.mall.view.*

/**
 * 项目名:    android
 * 包名:      com.example.eletronicengineer.fragment.shoppingmall
 * 文件名:    MyOrderFragment
 * 创建者:    LLH
 * 创建时间:  2019/8/16 20:36
 * 描述:      TODO
 */
class ShopDetailsFragment:Fragment() {
    lateinit var mTitle: ArrayList<String>
    lateinit var mFragment: ArrayList<Fragment>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop_details, container, false)
        view.tv_shop_details_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        ininData()
        initView(view)
        return view
    }
    private fun ininData() {
        mTitle = ArrayList<String>()
        mTitle.add("宇畅简介")
        mTitle.add("资质证书")
        mFragment = ArrayList<Fragment>()
        mFragment.add(DescFragment())
        mFragment.add(CertifyFragment())
    }

    private fun initView(view: View) {
        //预加载
        view.viewpager_shop.offscreenPageLimit = mFragment.size
        //设置适配器
        view.viewpager_shop.adapter = viewPagerAdapter(childFragmentManager,mFragment,mTitle)
        //绑定
        view.mTabLayout_shop.setupWithViewPager(view.viewpager_shop)
    }
    class viewPagerAdapter(fm: FragmentManager?, var mList:ArrayList<Fragment>, var mTitle:ArrayList<String>):
        FragmentPagerAdapter(fm){
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