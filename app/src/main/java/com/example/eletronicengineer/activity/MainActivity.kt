package com.example.eletronicengineer.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import com.example.eletronicengineer.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.fragment.main.*
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sdf.*
import com.lcw.library.imagepicker.ImagePicker
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private var exitTime:Long = 0

    var fragmentList: List<Fragment> = ArrayList()
    var lastfragment: Int = 2
    //判断选择的菜单
    val changeFragment =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
//                R.id.news -> {
//                    if (lastfragment != 0) {
//                        lastfragment=0
//                        //ToastHelper.mToast(this,"正在开发中")
//                        switchFragment(fragmentList[lastfragment],R.id.frame_main)
//                    }
//                    return@OnNavigationItemSelectedListener true
//                }
                R.id.sdr -> {
                    if (lastfragment != 1) {
//                        lastfragment = 1
                        switchFragment(fragmentList[1],R.id.frame_main)
                    }
                    supportFragmentManager.popBackStackImmediate(null,1)
                    view14.visibility=View.GONE
//                    tv_title.text="招投供需"
                    return@OnNavigationItemSelectedListener true
                }

                R.id.application -> {
                    if (lastfragment != 2) {
//                        lastfragment = 2
                        switchFragment(fragmentList[2],R.id.frame_main)
                    }
                    return@OnNavigationItemSelectedListener true
                }
                    /*
                R.id.shopping_mall->{
                    if(lastfragment != 3) {
                        lastfragment = 3
                        switchFragment(fragmentList[lastfragment], R.id.frame_main)
                    }
                    return@OnNavigationItemSelectedListener true
                }*/
                R.id.my -> {
                    if (lastfragment != 4) {
//                        lastfragment = 4
                        switchFragment(fragmentList[4],R.id.frame_main)
                    }
                    return@OnNavigationItemSelectedListener true
                }
            }

            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        val startTime = System.currentTimeMillis()
        super.onCreate(savedInstanceState)
        SysApplication.getInstance().addActivity(this)
        setContentView(R.layout.activity_main)
        val endTime = System.currentTimeMillis()
        Log.i("main run time is",(endTime-startTime).toString())
        initFragment()
        getData()
        supportActionBar?.hide()
    }

    private fun getData() {
        val result = NetworkAdapter().getDataUser().subscribe( {
            UnSerializeDataBase.userName = it.message.user.userName
            UnSerializeDataBase.userPhone = it.message.user.phone
            if(it.message.user.name!=null)
                UnSerializeDataBase.idCardName = it.message.user.name!!
            if(!it.message.user.isCredential){
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("个人认证")
                    .setMessage("当前未进行个人认证，是否立即前往认证？")
                    .setNegativeButton("暂不认证", null)
                    .setPositiveButton("立即前往", DialogInterface.OnClickListener() { _, _ ->
                        UnSerializeDataBase.isCertificate = false
                        val intent = Intent(this,MyCertificationActivity::class.java)
                        startActivity(intent)
                    })
                    .create().show()
            }
        },{
            ToastHelper.mToast(this,"网络异常")
            it.printStackTrace()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    //初始化界面 将第三个界面作为最初界面
    private fun initFragment() {
        val newsFragment = NewsFragment()
        val sdrFragment = SdrFragment()
        val applicationFragment = ApplicationFragment()
//        applicationFragment.setClickListener(View.OnClickListener{
//            if (lastfragment != 1) {
//                lastfragment = 1
//                switchFragment(fragmentList[lastfragment],R.id.frame_main)
//            }
//            supportFragmentManager.popBackStackImmediate(null,1)
//            return_iv.visibility=View.INVISIBLE
//            tv_title.text="招投供需"
//            bnv.menu.getItem(1).isChecked=true
//        })
        val mallFragment = MallFragment()
        val myFragment = MyFragment()
        fragmentList = ArrayList(Arrays.asList<Fragment>(newsFragment, sdrFragment, applicationFragment, mallFragment, myFragment))
        lastfragment = 0
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_main, sdrFragment)
        transaction.commit()
        bnv.setOnNavigationItemSelectedListener(changeFragment)
        bnv.menu.getItem(0).isChecked = true
        //sdrFragment.initindex(lastfragment);
    }

    //切换Fragment
    fun switchFragment(fragment: Fragment,frameLayout:Int) {
        val transaction = supportFragmentManager.beginTransaction()
        //隐藏上个Fragment
        if(!transaction.isEmpty){
            transaction.hide(fragmentList[lastfragment])
            lastfragment = fragmentList.indexOf(fragment)
        }
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exit()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if((System.currentTimeMillis()-exitTime)>2000) {
            ToastHelper.mToast(this, "再按一次退出程序")
            exitTime=System.currentTimeMillis()
        }else{
            SysApplication.getInstance().exit()
        }
    }
}