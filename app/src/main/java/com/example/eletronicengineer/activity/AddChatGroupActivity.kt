package com.example.eletronicengineer.activity

import android.content.DialogInterface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.im.android.api.ContactManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.CreateGroupCallback
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.callback.GetUserInfoListCallback
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback

import com.example.eletronicengineer.adapter.SelectFriendAdapter
import com.example.eletronicengineer.model.SelectFriendItem
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.activity_add_chat_group.*
import kotlinx.android.synthetic.main.dialog_write_group_info.view.*
import kotlinx.android.synthetic.main.layout_add_chat_group_title.*

class AddChatGroupActivity:AppCompatActivity()
{
    var selectFriendList:MutableList<SelectFriendItem> =ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chat_group)
        tv_chat_group_back.setOnClickListener {
            finish()
        }
        supportActionBar?.hide()
        initData()
        tv_chat_group_send.setOnClickListener {
            val adapter=rv_add_chat_group_content.adapter as SelectFriendAdapter
            val userList:MutableList<String> =ArrayList()
            for (i in adapter.mData)
            {
                if (i.selectStatus)
                {
                    userList.add(i.userName)
                }
            }
            val groupInfoView=LayoutInflater.from(it.context).inflate(R.layout.dialog_write_group_info,null,false)
            val dialog=AlertDialog.Builder(it.context).setView(groupInfoView).setPositiveButton("确认",object:DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val groupName=groupInfoView.et_write_group_info_content.text.toString()
                    JMessageClient.createGroup(groupName,"暂无描述",object:CreateGroupCallback(){
                        override fun gotResult(p0: Int, p1: String?, p2: Long) {
                            val groupId=p2
                            JMessageClient.addGroupMembers(groupId,userList,object:BasicCallback(){
                                override fun gotResult(p0: Int, p1: String?) {
                                    Log.i("addGroupMember",p1)
                                }
                            })
                        }
                    })
                }
            })
            dialog.show()
        }
    }
    private fun initData()
    {
        ContactManager.getFriendList(object:GetUserInfoListCallback(){
            override fun gotResult(p0: Int, p1: String?, p2: MutableList<UserInfo>) {
                for (i in p2)
                {
                    val userAvatar=if (i.avatarFile==null)
                        ContextCompat.getDrawable(this@AddChatGroupActivity,R.drawable.user_avatar_xml)
                    else
                        BitmapDrawable(this@AddChatGroupActivity.resources,i.avatarFile.absolutePath)
                    val username=i.userName
                    val selectFriendItem=SelectFriendItem(userAvatar!!,username,false)
                    selectFriendList.add(selectFriendItem)
                }
                rv_add_chat_group_content.adapter=SelectFriendAdapter(selectFriendList)
                rv_add_chat_group_content.layoutManager=LinearLayoutManager(this@AddChatGroupActivity)
            }
        })
        /*
        val userAvatar=ContextCompat.getDrawable(this, R.drawable.user_avatar_xml)
        var selectFriendItem=SelectFriendItem(userAvatar!!,"郑架构师",false)
        selectFriendList.add(selectFriendItem)
        selectFriendItem= SelectFriendItem(userAvatar,"钟正航",false)
        selectFriendList.add(selectFriendItem)*/
    }
}