package com.example.eletronicengineer.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.im.android.api.ContactManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.model.GroupInfo
import cn.jpush.im.android.api.model.Message
import cn.jpush.im.android.api.model.UserInfo

import com.example.eletronicengineer.adapter.ChatMessageAdapter
import com.example.eletronicengineer.model.ChatMessageItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.JiGuangMessageUtil
import com.example.eletronicengineer.utils.UnSerializeDataBase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.layout_chat_main_title.*

class ChatActivity:AppCompatActivity()
{
    var messageData:MutableList<ChatMessageItem> =ArrayList()
    lateinit var mConversation:Conversation
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar?.hide()
        when(intent.getStringExtra("type"))
        {
            "friend"->
            {
                val username=intent.getStringExtra("username")
                //JMessageClient.deleteSingleConversation(username)
                mConversation= Conversation.createSingleConversation(UnSerializeDataBase.username)
                tv_chat_main_title_name.text=username
                initData(username)
            }
            "chat_group"->
            {
                val groupId=intent.getLongExtra("groupId",0)
                mConversation= JMessageClient.getGroupConversation(groupId)
                tv_chat_main_title_name.text=mConversation.title
                if (UnSerializeDataBase.username!=null)
                    initData(UnSerializeDataBase.username!!)
                else
                    initData("admin1")
            }
        }
        tv_chat_main_title_back.setOnClickListener {
            finish()
        }
        rv_chat_message_list.adapter=ChatMessageAdapter(messageData)
        rv_chat_message_list.layoutManager=LinearLayoutManager(this)
    }
    private fun initData(username:String)
    {
        val conversationList=JMessageClient.getConversationList()
        val messageList:MutableList<Message> =ArrayList()
        for (i in conversationList)
        {
            if (i.targetInfo is UserInfo)
            {
                if (i.allMessage.size==0&&i.latestMessage==null)
                    continue
                val userInfo=i.targetInfo as UserInfo
                if (userInfo.userName==UnSerializeDataBase.username||userInfo.userName==username)
                {
                    if (i.allMessage.size==0)
                        messageList.add(i.latestMessage)
                    else
                        messageList.addAll(i.allMessage)
                }
            }
        }
        if (messageList.size>1)
        messageList.sortBy {
            it.createTime
        }
        for (i in messageList)
        {
            if (i.targetInfo is UserInfo)
            {
                val targetInfo=i.targetInfo as UserInfo
                if (targetInfo.userName==UnSerializeDataBase.username)
                {
                    val chatMessageItem=ChatMessageItem(true, JiGuangMessageUtil.toJsonObject(i.content).get("text").asString)
                    messageData.add(chatMessageItem)
                }
                else
                {
                    val chatMessageItem=ChatMessageItem(false,JiGuangMessageUtil.toJsonObject(i.content).get("text").asString)
                    messageData.add(chatMessageItem)
                }
            }
            else
            {
                val targetInfo=i.targetInfo as GroupInfo
            }
        }
        var result=UnSerializeDataBase.receiveServiceBinder.observableMessageEvent.subscribe {
            val chatMessageItem=ChatMessageItem(false,JiGuangMessageUtil.toJsonObject(it.message.content).get("text").asString)
            addData(chatMessageItem)
        }
        result=UnSerializeDataBase.receiveServiceBinder.observableOfflineMessageEvent.subscribe {
            for (i in it.offlineMessageList)
            {
                val chatMessageItem=ChatMessageItem(false,JiGuangMessageUtil.toJsonObject(i.content).get("text").asString)
                addData(chatMessageItem)
            }
        }
        tv_chat_send_message.setOnClickListener {
            val content=et_chat_input_content.text.toString()
            et_chat_input_content.setText("")
            val chatMessageItem=ChatMessageItem(true,content)
            addData(chatMessageItem)
            val currentMesage=mConversation.createSendTextMessage(content)
            JMessageClient.sendMessage(currentMesage)
        }
    }
    fun addData(item:ChatMessageItem)
    {
        val adapter=rv_chat_message_list.adapter as ChatMessageAdapter
        val source=adapter.mData.toMutableList()
        source.add(item)
        adapter.mData=source
        adapter.notifyDataSetChanged()
    }
}