package com.example.eletronicengineer.fragment.main

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.im.android.api.ContactManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetGroupIDListCallback
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.callback.GetUserInfoListCallback
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback
import com.example.eletronicengineer.activity.AddChatGroupActivity
import com.example.eletronicengineer.activity.AddFriendActivity
import com.example.eletronicengineer.activity.FriendRequestActivity
import com.example.eletronicengineer.adapter.ChatAdapter
import com.example.eletronicengineer.adapter.ChatMenuAdapter
import com.example.eletronicengineer.model.ChatMenuItem
import com.example.eletronicengineer.service.ReceiveService
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ProjectDiskActivity
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.model.ChatItem
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.JiGuangMessageUtil
import com.example.eletronicengineer.utils.UnSerializeDataBase
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.layout_chat_title.*
import kotlinx.android.synthetic.main.news.view.*
import kotlinx.android.synthetic.main.popupwindow_chat.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NewsFragment : Fragment() {
    val chatData:MutableList<ChatItem> =ArrayList()
    var menuIsShow=false
    var appkey:String="0779610012f48cd81f43ca66"
    val serviceConn=object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            UnSerializeDataBase.receiveServiceBinder=service as ReceiveService.CustomBinder
        }
        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.news, container, false)
        initRegister()
        Thread{
            val inputName= EditText(context)
            inputName.hint="请输入要登录的账号"
            val dialog= AlertDialog.Builder(context!!).setView(inputName).setPositiveButton("确认",object: DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    initData(true,inputName.text.toString())
                }
            })
            activity!!.runOnUiThread {
                dialog.show()
                initMenu()
            }
        }.start()
        return view
    }

    override fun onStart() {
        super.onStart()
    }
    private fun initRegister()
    {
        val intent=Intent(activity,ReceiveService::class.java)
        activity!!.bindService(intent,serviceConn, Context.BIND_AUTO_CREATE)
    }
    private fun initData(isFirst:Boolean,loginName:String)
    {
        UnSerializeDataBase.username=loginName
        JMessageClient.login(loginName,"adminPassword",object: BasicCallback(){
            override fun gotResult(p0: Int, p1: String?) {
                Log.i("logInfo",p1)
                UnSerializeDataBase.isLogined=true
                val conversation=JMessageClient.getSingleConversation(UnSerializeDataBase.username)
                val myLatestMessage=conversation.latestMessage
                ContactManager.getFriendList(object: GetUserInfoListCallback(){
                    override fun gotResult(p0: Int, p1: String?, p2: MutableList<UserInfo>?) {
                        val userInfoList= p2 ?: ArrayList()
                        val userAvatar= ContextCompat.getDrawable(context!!,R.drawable.user_avatar_xml)
                        for (i in userInfoList)
                        {
                            val friendConversation=JMessageClient.getSingleConversation(i.userName,appkey)
                            val chatData=if (friendConversation.latestMessage!=null)
                            {
                                val simpleFormat= SimpleDateFormat("HH:mm")
                                if (myLatestMessage.createTime>friendConversation.latestMessage.createTime)
                                {
                                    val latestContent=JiGuangMessageUtil.toJsonObject(myLatestMessage.content).get("text").asString
                                    ChatItem(userAvatar!!,i.userName,latestContent,simpleFormat.format(Date(myLatestMessage.createTime)))
                                }
                                else
                                {
                                    val latestContent=JiGuangMessageUtil.toJsonObject(friendConversation.latestMessage.content).get("text").asString
                                    ChatItem(userAvatar!!,i.userName,latestContent,simpleFormat.format(Date(friendConversation.latestMessage.createTime)))
                                }
                            }
                            else if(myLatestMessage!=null)
                            {
                                val simpleFormat= SimpleDateFormat("HH:mm")
                                val latestContent=
                                    JiGuangMessageUtil.toJsonObject(myLatestMessage.content).get("text").asString
                                ChatItem(userAvatar!!,i.userName,latestContent,simpleFormat.format(Date(myLatestMessage.createTime)))
                            }
                            else
                                ChatItem(userAvatar!!,i.userName,"","14:50")
                            activity!!.runOnUiThread {
                                addFriend(chatData)
                            }
                        }
                    }
                })
            }
        })
//        val conversationList=JMessageClient.getChatRoomConversationList()
//        Log.i("message content",JMessageClient.createSingleTextMessage("username","appKey","text").content.toJson())
    }
    private fun addFriend(item:ChatItem)
    {
        val adapter=rv_fragment_message_content.adapter as ChatAdapter
        val source=adapter.mData.toMutableList()
        source.add(item)
        adapter.mData=source
        adapter.notifyDataSetChanged()
    }
    private fun initMenu()
    {
        val menu= PopupWindow(context!!)
        val menuBackground=ContextCompat.getDrawable(context!!,R.drawable.popupwindow_back)
        val menuList=LayoutInflater.from(context).inflate(R.layout.popupwindow_chat,null,false)
        menu.contentView=menuList
        menu.setBackgroundDrawable(menuBackground!!)
        val newGroup=ContextCompat.getDrawable(context!!,R.drawable.start_chat_group_xml)
        val addFriend=ContextCompat.getDrawable(context!!,R.drawable.add_friend_xml)
        val newFriend=ContextCompat.getDrawable(context!!,R.drawable.notification)
        var chatMenuItem= ChatMenuItem(newGroup!!,"新建群聊")
        chatMenuItem.listener= View.OnClickListener {
            menu.dismiss()
            menuIsShow=false
            val intent=Intent(context, AddChatGroupActivity::class.java)
            startActivity(intent)
        }
        val chatMenuData:MutableList<ChatMenuItem> =ArrayList()
        chatMenuData.add(chatMenuItem)
        chatMenuItem= ChatMenuItem(addFriend!!,"添加朋友")
        chatMenuItem.listener=View.OnClickListener {
            menu.dismiss()
            menuIsShow=false
            val intent=Intent(context, AddFriendActivity::class.java)
            startActivity(intent)
        }
        chatMenuData.add(chatMenuItem)
        chatMenuItem= ChatMenuItem(newFriend!!,"新的朋友")
        chatMenuItem.listener=View.OnClickListener {
            menu.dismiss()
            menuIsShow=false
            val intent=Intent(context, FriendRequestActivity::class.java)
            startActivity(intent)
        }
        chatMenuData.add(chatMenuItem)
        menuList.rv_chat_menu_content.adapter= ChatMenuAdapter(chatMenuData)
        menuList.rv_chat_menu_content.layoutManager= LinearLayoutManager(context!!)
        tv_chat_more.setOnClickListener {
            if (!menuIsShow)
            {
                menu.showAsDropDown(it)
                menuIsShow=true
            }
            else
            {
                menu.dismiss()
                menuIsShow=false
            }
        }
    }

}
