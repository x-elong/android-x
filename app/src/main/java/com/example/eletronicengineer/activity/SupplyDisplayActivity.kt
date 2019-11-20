package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.retailstore.SupplyDisplayFragment

class SupplyDisplayActivity : AppCompatActivity() {
  lateinit var id:String
  var type:Int=0
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
    addFragment(SupplyDisplayFragment.newInstance(data))
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
    transaction.replace(R.id.frame_display_demand, fragment)
    transaction.addToBackStack(null)
    transaction.commit()
  }
}
