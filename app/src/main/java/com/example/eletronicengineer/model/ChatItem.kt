package com.example.eletronicengineer.model

import android.graphics.drawable.Drawable

class ChatItem(var chatIcon:Drawable,var chatUserName:String,var chatUserMessage:String,var chatTime:String)
{
    var type:ChatType?=null
    enum class ChatType
    {
        FRIEND,CHAT_GROUP
    }
    var groupId:Long?=null
}