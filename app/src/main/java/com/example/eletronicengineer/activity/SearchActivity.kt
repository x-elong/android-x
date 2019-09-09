package com.example.eletronicengineer.activity

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.fragment.shoppingmall.SearchFragment
import com.library.OnChooseEditTextListener
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    //声明控件
    lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        //绑定控件
        addFragment(SearchFragment())
    }
    //在当前下增加碎片
    private fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_search,fragment,"1")
        transaction.commit()
    }
    //Fragment切换
    fun switchFragment(fragment: Fragment,  tag:String) {
        val transaction = supportFragmentManager.beginTransaction()
        //隐藏上个Fragment
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.frame_search, fragment,tag)
        transaction.addToBackStack(tag)
        transaction.commit()
    }
}
