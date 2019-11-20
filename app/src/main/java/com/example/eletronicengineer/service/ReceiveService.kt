package com.example.eletronicengineer.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.event.ContactNotifyEvent
import cn.jpush.im.android.api.event.ConversationRefreshEvent
import cn.jpush.im.android.api.event.MessageEvent
import cn.jpush.im.android.api.event.OfflineMessageEvent
import cn.jpush.im.android.api.model.UserInfo

import com.example.eletronicengineer.model.ChatMessageItem
import com.example.eletronicengineer.model.SelectFriendItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.UnSerializeDataBase
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import java.text.SimpleDateFormat
import java.util.*

class ReceiveService:Service()
{
    lateinit var mContext: Context
    lateinit var mBinder:CustomBinder
    override fun onBind(intent: Intent?): IBinder? {
        JMessageClient.registerEventReceiver(this)
        mContext=this
        mBinder=CustomBinder()
        mBinder.init()
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        JMessageClient.unRegisterEventReceiver(this)
        return super.onUnbind(intent)
    }
    class CustomBinder: Binder()
    {
        object ServiceDb{
            val friendRequestList:MutableList<SelectFriendItem> =ArrayList()

        }
        var messageEventEmit:ObservableEmitter<MessageEvent>?=null
        lateinit var observableMessageEvent:Observable<MessageEvent>
        var offlineMessageEventEmit:ObservableEmitter<OfflineMessageEvent>?=null
        lateinit var observableOfflineMessageEvent: Observable<OfflineMessageEvent>
        fun init()
        {
            observableMessageEvent=Observable.create<MessageEvent>{
                messageEventEmit=it
            }
            observableOfflineMessageEvent= Observable.create {
                offlineMessageEventEmit=it
            }
        }
        fun addFriendRequestItem(item: SelectFriendItem)
        {
            ServiceDb.friendRequestList.add(item)
        }
        fun getServiceDb():ServiceDb
        {
            return ServiceDb
        }
    }
    fun onEvent(event:MessageEvent)
    {
        if (event.message.targetInfo is UserInfo)
        {
            val targetUserName=(event.message.targetInfo as UserInfo).userName
            val conversation=JMessageClient.getSingleConversation(UnSerializeDataBase.userName)
            conversation.createSendMessage(event.message.content)
        }
        if (mBinder.messageEventEmit!=null)
        {
            mBinder.messageEventEmit!!.onNext(event)
        }
    }
    fun onEvent(event:ConversationRefreshEvent)
    {
        val conversation=JMessageClient.getSingleConversation(UnSerializeDataBase.userName)
        val allMessage=conversation.allMessage
        for (i in allMessage)
        {
            conversation.createSendMessage(i.content)
        }
    }
    fun onEvent(event: OfflineMessageEvent)
    {
        val currentConversation=event.conversation
        for (i in event.offlineMessageList)
        {
            currentConversation.createSendMessage(i.content)
        }
        if (mBinder.offlineMessageEventEmit!=null)
        {
            mBinder.offlineMessageEventEmit!!.onNext(event)
        }
    }
    fun onEvent(event: ContactNotifyEvent)
    {
        when(event.type)
        {
            ContactNotifyEvent.Type.invite_received->
            {
                val username=event.fromUsername
                val reason=event.reason
                sendNotification("来自${username}的好友请求", reason)
                JMessageClient.getUserInfo(username,object:GetUserInfoCallback(){
                    override fun gotResult(p0: Int, p1: String?, p2: UserInfo?) {
                        val avatar=if (p2?.avatarFile==null)
                            BitmapDrawable(mContext.resources,BitmapFactory.decodeFile(p2?.avatarFile?.absolutePath)) as Drawable
                        else
                            mContext.getDrawable(R.drawable.user_avatar_xml)!!
                        val selectFriendItem=SelectFriendItem(avatar,username,false)
                        mBinder.addFriendRequestItem(selectFriendItem)
                        UnSerializeDataBase.friendRequestList.add(selectFriendItem)
                    }
                })
            }
            ContactNotifyEvent.Type.invite_accepted->
            {
                val username=event.fromUsername
                val time=SimpleDateFormat("HH:mm:ss").format(Date(event.createTime))
                sendNotification("一条新的好友消息","${username}通过了你的请求",time)
                /*
                JMessageClient.getUserInfo(username,object:GetUserInfoCallback(){
                    override fun gotResult(p0: Int, p1: String?, p2: UserInfo?) {
                        val avatar=if (p2?.avatarFile==null)
                            BitmapDrawable(mContext.resources,BitmapFactory.decodeFile(p2?.avatarFile?.absolutePath)) as Drawable
                        else
                            mContext.getDrawable(R.drawable.user_avatar_xml)!!
                        val friendRequestItem=FriendRequestItem(username,avatar)
                        friendRequestItem.type=event.type
                        UnSerializeDataBase.friendRequestList.add(friendRequestItem)
                    }
                })*/
            }
        }
    }
    fun sendNotification(title:String,content:String,time:String)
    {
        val notifyManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification= NotificationCompat.Builder(mContext,"GlobalEvent.friendStatus.accpet")
            .setContentTitle(title)
            .setContentText(content)
            .setTicker(time)
            .build()
        notifyManager.notify(1,notification)
    }
    fun sendNotification(title: String,content: String)
    {

    }
}