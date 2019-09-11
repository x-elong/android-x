package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.retailstore.RetailStoreFragment


class RetailStoreActivity : AppCompatActivity() {
    lateinit var title: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retail_store)
        initFragment()
    }

    private fun initFragment() {
//        initData()
        val data = Bundle()
//        data.putString("title", title)
        addFragment(RetailStoreFragment.newInstance(data))
    }

    //获取加载的界面类型
    private fun initData() {
        val intent = getIntent()
        title = intent.getStringExtra("title")
    }

    fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_retail_store, fragment)
        transaction.commit()
    }
    fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.frame_retail_store, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
