package com.example.eletronicengineer.activity

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.example.eletronicengineer.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.fragment.*
import com.example.eletronicengineer.utils.PermissionHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sdf.*
import java.util.ArrayList
import java.util.Arrays

class MainActivity : AppCompatActivity() {
    var fragmentList: List<Fragment> = ArrayList()
    var lastfragment: Int = 2
    //判断选择的菜单
    private val changeFragment =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.news -> {
                    if (lastfragment != 0) {
                        lastfragment=0
                        switchFragment(fragmentList[lastfragment],R.id.frame_main)
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.sdr -> {
                    if (lastfragment != 1) {
                        lastfragment = 1
                        switchFragment(fragmentList[lastfragment],R.id.frame_main)
                    }
                    supportFragmentManager.popBackStackImmediate(null,1)
                    return_iv.visibility=View.INVISIBLE
                    title_tv.text="招投供需"
                    return@OnNavigationItemSelectedListener true
                }
                R.id.application -> {
                    if (lastfragment != 2) {
                        lastfragment = 2
                        switchFragment(fragmentList[lastfragment],R.id.frame_main)
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.shopping_mall->{
                    lastfragment=3
                    switchFragment(fragmentList[lastfragment],R.id.frame_main)
                }
                R.id.my -> {
                    if (lastfragment != 4) {
                        lastfragment = 4
                        switchFragment(fragmentList[lastfragment],R.id.frame_main)
                    }
                    return@OnNavigationItemSelectedListener true
                }
            }

            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PermissionHelper.getPermission(this, 1)
        initFragment()
        supportActionBar?.hide()
    }
    private fun initFragment() {
        val newsFragment = NewsFragment()
        val sdrFragment = sdrFragment()
        val applicationFragment = ApplicationFragment()
        val mallFragment = MallFragment()
        val myFragment = MyFragment()
        fragmentList = ArrayList(Arrays.asList<Fragment>(newsFragment, sdrFragment, applicationFragment, mallFragment, myFragment))
        lastfragment = 2
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_main, applicationFragment)
        transaction.commit()
        bnv.setOnNavigationItemSelectedListener(changeFragment)
        bnv.menu.getItem(2).isChecked = true
        //sdrFragment.initindex(lastfragment);
    }

    //切换Fragment
    fun switchFragment(fragment: Fragment,frameLayout:Int) {
        val transaction = supportFragmentManager.beginTransaction()
        //隐藏上个Fragment
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(frameLayout, fragment)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            1->
            {
                if (grantResults.size>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {

                }
                else
                {
                    Toast.makeText(this,"必须同意才能继续使用",Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }
        }
    }
}