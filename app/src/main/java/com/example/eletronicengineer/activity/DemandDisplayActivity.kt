package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.sdf.DemandDisplayFragment

class DemandDisplayActivity : AppCompatActivity() {
  var type:Int=0
  lateinit var id:String
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_demand_display)
    initFragment()
  }

  private fun initFragment() {
    initData()
    val data = Bundle()
    data.putInt("type",type)
    data.putString("id",id)
    addFragment(DemandDisplayFragment.newInstance(data))
  }

  //获取加载的界面类型
  private fun initData() {
    val intent = getIntent()
    type = intent.getIntExtra("type",0)
    id = intent.getStringExtra("id")
  }

  fun addFragment(fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.frame_display_demand, fragment)
    transaction.commit()
  }
  fun switchFragment(fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    transaction.replace(R.id.frame_display_demand, fragment)
    transaction.addToBackStack(null)
    transaction.commit()
  }
  //Fragment切换
  fun switchFragment(fragment: Fragment,frameLayout:Int,tag:String) {
    val transaction = supportFragmentManager.beginTransaction()
    //隐藏上个Fragment
    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    transaction.replace(frameLayout, fragment,tag)
    transaction.addToBackStack(tag)
    transaction.commit()
  }

}
