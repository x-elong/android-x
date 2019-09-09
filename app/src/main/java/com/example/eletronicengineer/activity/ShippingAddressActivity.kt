package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.my.ShippingAddressFragment

class ShippingAddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping_address)
        initData()
    }

    private fun initData() {
        addFragment(ShippingAddressFragment())
    }

    fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_shipping_address, fragment,"my")
        transaction.commit()
    }

    fun switchFragment(fragment: Fragment, tag:String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.frame_shipping_address, fragment,tag)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
