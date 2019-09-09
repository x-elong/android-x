package com.example.eletronicengineer.fragment

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.model.UserInfo

import com.example.eletronicengineer.activity.AddFriendActivity
import com.example.eletronicengineer.adapter.FriendRequestAdapter
import com.example.eletronicengineer.adapter.FriendWayAdapter
import com.example.eletronicengineer.model.FriendRequestItem
import com.example.eletronicengineer.model.FriendWayItem
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.layout_add_friend.*
import kotlinx.android.synthetic.main.layout_chat_main_title.*
import kotlinx.android.synthetic.main.layout_search_user.*

class AddFriendMainFragment:Fragment()
{
    var mFriendWayList:MutableList<FriendWayItem> =ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root=inflater.inflate(R.layout.layout_add_friend,container,false)
        root.run {
            startInit()
        }
        return root
    }
    private fun startInit()
    {
        val initThread=Thread {
            tv_chat_main_title_back.setOnClickListener {
                activity?.finish()
            }
            editText.setOnClickListener {
                val fragment = AddFriendSearchUserFragment()
                replaceFragment(fragment,"searchUserFragment")
            }
            activity?.runOnUiThread {
                initData()
                rv_add_friend_content.adapter = FriendWayAdapter(mFriendWayList)
                rv_add_friend_content.layoutManager = LinearLayoutManager(activity!!)
                tv_chat_main_title_name.text = "添加朋友"
            }
        }
        initThread.start()
    }
    private fun initData()
    {
        if (mFriendWayList.size==0)
        {
            val userAvatar= ContextCompat.getDrawable(activity!!,R.drawable.user_avatar_xml)
            var friendWayItem= FriendWayItem(userAvatar!!,"手机联系人","添加或邀请通讯录中的朋友")
            mFriendWayList.add(friendWayItem)
        }
    }
    fun replaceFragment(fragment: Fragment)
    {
        val activity=activity as AddFriendActivity
        activity.replaceFragment(R.id.fl_add_friend_container,fragment)
    }
    fun replaceFragment(fragment: Fragment,fragmentId:String)
    {
        val activity=activity as AddFriendActivity
        activity.replaceFragment(R.id.fl_add_friend_container,fragment,fragmentId)
    }
}