package com.example.eletronicengineer.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.*
import com.example.eletronicengineer.utils.PermissionHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_project_disk.*
import java.util.*
import kotlin.collections.ArrayList

class ProjectDiskActivity : AppCompatActivity() {
  lateinit var messageFragment: MessageFragment
  lateinit var projectDiskFragment: ProjectDiskFragment
  lateinit var applicationFragment: ApplicationFragment
  lateinit var phoneListFragment: PhoneListFragment
  lateinit var mineFragment: MineFragment
  var fragmentList: List<Fragment> = ArrayList()
  var lastfragment: Int = 2
  //判断选择的菜单
  private val changeFragment =
    BottomNavigationView.OnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.message -> {
          if (lastfragment != 0) {
            lastfragment=0
            switchFragment(fragmentList[lastfragment])
          }
          return@OnNavigationItemSelectedListener true
        }
        R.id.projectdisk -> {
          if (lastfragment != 1) {
            lastfragment = 1
            switchFragment(fragmentList[lastfragment])

          }
          return@OnNavigationItemSelectedListener true
        }
        R.id.application1 -> {
          if (lastfragment != 2) {
            lastfragment = 2
            switchFragment(fragmentList[lastfragment])

          }
          return@OnNavigationItemSelectedListener true
        }
        R.id.phonelist -> {
          if (lastfragment != 3) {
            lastfragment = 3
            switchFragment(fragmentList[lastfragment])
          }
          return@OnNavigationItemSelectedListener true
        }
        R.id.mine -> {
          if (lastfragment != 4) {
            lastfragment = 4
            switchFragment(fragmentList[lastfragment])
          }
          return@OnNavigationItemSelectedListener true
        }
      }

      false
    }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_project_disk)
    initFragment()
    supportActionBar?.hide()
    //返回
    PermissionHelper.getPermission(this@ProjectDiskActivity,1)
  }

  private fun initFragment() {
    messageFragment = MessageFragment()
    projectDiskFragment = ProjectDiskFragment()
    applicationFragment = ApplicationFragment()
    phoneListFragment = PhoneListFragment()
    mineFragment = MineFragment()
    fragmentList = ArrayList(Arrays.asList<Fragment>(messageFragment, projectDiskFragment, applicationFragment, phoneListFragment, mineFragment))
    lastfragment = 2
    //创建碎片管理者
    switchFragment(applicationFragment)
    bnv1.setOnNavigationItemSelectedListener(changeFragment)
    bnv1.menu.getItem(2).isChecked = true
  }
  //切换Fragment
  fun switchFragment(fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    //隐藏上个Fragment
    transaction.replace(R.id.frame_project_disk, fragment)
    transaction.commit()
    if (fragmentList.indexOf(fragment) > -1) {
      lastfragment = -1
    }
  }

}