package com.example.eletronicengineer.activity

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.im.android.api.ContactManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.event.ContactNotifyEvent
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback
import com.example.eletronicengineer.adapter.SelectFriendAdapter
import com.example.eletronicengineer.model.SelectFriendItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.UnSerializeDataBase
import kotlinx.android.synthetic.main.activity_friend_request.*
import kotlinx.android.synthetic.main.layout_add_chat_group_title.*

class FriendRequestActivity : AppCompatActivity() {
    lateinit var mFriendRequestList:MutableList<SelectFriendItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_request)
        supportActionBar?.hide()
        initData()
        rv_friend_request_content.adapter=SelectFriendAdapter(mFriendRequestList)
        rv_friend_request_content.layoutManager=LinearLayoutManager(this)
        tv_chat_group_name.text="好友请求"
        tv_chat_group_send.text="同意请求"
        tv_chat_group_send.setOnClickListener {
            val dataList=(rv_friend_request_content.adapter as SelectFriendAdapter).mData
            for (i in dataList)
            {
                if (i.selectStatus)
                {
                    ContactManager.acceptInvitation(i.userName,null,object:BasicCallback(){
                        override fun gotResult(p0: Int, p1: String?) {
                            Log.i("acceptInvitation",p1)
                        }
                    })
                }
            }
        }
    }
    fun initData()
    {
        mFriendRequestList= UnSerializeDataBase.receiveServiceBinder.getServiceDb().friendRequestList
    }
}
