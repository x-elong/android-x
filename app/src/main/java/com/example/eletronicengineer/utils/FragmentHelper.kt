package com.example.eletronicengineer.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R

class FragmentHelper {
    companion object{
        //在当前下增加碎片
        /**
         * @param activity 碎片处于的活动
         * @param fragment 新碎片
         * @param frameLayout Frame布局
         * @param tag 标签(可给空，看情况给)
         */
        fun addFragment(activity: FragmentActivity,fragment: Fragment, frameLayout:Int, tag:String) {
            val transaction = activity.supportFragmentManager.beginTransaction()
            transaction.add(frameLayout,fragment,tag)
            transaction.commit()
        }
        //Fragment切换
        fun switchFragment(activity: FragmentActivity,fragment: Fragment, frameLayout:Int, tag:String) {
            val transaction = activity.supportFragmentManager.beginTransaction()
            //隐藏上个Fragment
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.replace(frameLayout, fragment,tag)
            transaction.addToBackStack(tag)
            transaction.commit()
        }
    }
}