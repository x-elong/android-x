package com.example.eletronicengineer.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.main.MyFragment
import com.example.eletronicengineer.fragment.main.NewsFragment
import com.example.eletronicengineer.fragment.main.SdrFragment
import com.example.eletronicengineer.utils.PermissionHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // 初始化导航栏菜单
    fun initMenu() {
        data class MenuItem(val frag: Fragment, val layout: Int)

        val menu = listOf<MenuItem>(
            MenuItem(NewsFragment(), R.id.news),
            MenuItem(SdrFragment(), R.id.sdr),
            MenuItem(MyFragment(), R.id.my)
        )
        bnv.setOnNavigationItemSelectedListener {
            for ((i, m) in menu.withIndex()) {
                if (it.itemId == m.layout) {
                    switchFragment(m.frag, R.id.frame_main)
                    bnv.menu.getItem(i).isChecked = true
                    break
                }
            }
            true
        }

        // 默认选择第二个菜单
        switchFragment(menu[1].frag, R.id.frame_main)
        bnv.menu.getItem(1).isChecked = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val startTime = System.currentTimeMillis()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val endTime = System.currentTimeMillis()
        Log.i("main run time is", (endTime - startTime).toString())
        PermissionHelper.getPermission(this, 1)
        initMenu()
        supportActionBar?.hide()
    }

    //切换Fragment
    fun switchFragment(fragment: Fragment, frameLayout: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        //隐藏上个Fragment
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(frameLayout, fragment)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "必须同意才能继续使用", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }
        }
    }

}