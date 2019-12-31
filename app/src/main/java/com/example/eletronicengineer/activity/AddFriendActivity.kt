package com.example.eletronicengineer.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.main.AddFriendMainFragment

class AddFriendActivity:AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        supportActionBar?.hide()
        val fragment= AddFriendMainFragment()
        replaceFragment(R.id.fl_add_friend_container,fragment,"mainFragment")
    }
    fun replaceFragment(layoutId:Int,fragment: Fragment)
    {
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(layoutId,fragment)
        transaction.commit()
    }
    fun replaceFragment(layoutId:Int,fragment: Fragment,fragmentId:String)
    {
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(layoutId,fragment,fragmentId)
        transaction.addToBackStack(fragmentId)
        transaction.commit()
    }
}