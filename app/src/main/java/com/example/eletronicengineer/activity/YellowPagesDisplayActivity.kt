package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.retailstore.SupplyDisplayFragment
import com.example.eletronicengineer.fragment.retailstore.YellowPagesDisplayFragment
import com.example.eletronicengineer.utils.FragmentHelper

class YellowPagesDisplayActivity : AppCompatActivity() {
  lateinit var id:String
  var type:Int=0
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_yellow_pages_display)
    initFragment()
  }

  private fun initFragment() {
    initData()
    val data = Bundle()
    data.putInt("type",type)
    data.putString("id",id)
    FragmentHelper.addFragment(this,
      YellowPagesDisplayFragment.newInstance(data),R.id.frame_display_yellow_pages,"")
  }
  //获取加载的界面类型
  private fun initData() {
    val intent = getIntent()
    type = intent.getIntExtra("type",0)
    id = intent.getStringExtra("id")
  }
}
