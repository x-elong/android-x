package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.retailstore.RetailStoreFragment
import com.example.eletronicengineer.utils.FragmentHelper


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
         FragmentHelper.addFragment(this,RetailStoreFragment.newInstance(data),R.id.frame_retail_store,"")
    }

    //获取加载的界面类型
    private fun initData() {
        val intent = getIntent()
        title = intent.getStringExtra("title")
    }
}
