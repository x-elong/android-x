package com.example.eletronicengineer.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.main.MallFragment
import com.example.eletronicengineer.fragment.shopping.GoodsClassifyFragment
import com.example.eletronicengineer.fragment.shoppingmall.ShopFragment

class ShoppingActivity: AppCompatActivity() {

    lateinit var title: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)
        supportActionBar?.hide()
        initFragment()
    }
    private fun initFragment() {
        val intent = getIntent()
        val type = intent.getIntExtra("type",0)
        var fragment = Fragment()
        when(type){
            1-> fragment = GoodsClassifyFragment()
            2-> fragment = ShopFragment()
        }
        addFragment(fragment)
    }
    fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_shopping, fragment)
        transaction.commit()
    }

    fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_shopping, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
