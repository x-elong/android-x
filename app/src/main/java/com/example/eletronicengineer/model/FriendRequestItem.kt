package com.example.eletronicengineer.model

import android.graphics.drawable.Drawable
import cn.jpush.im.android.api.event.ContactNotifyEvent

class FriendRequestItem(var username:String,var userAvatar:Drawable)
{
    var type:ContactNotifyEvent.Type?=null
}