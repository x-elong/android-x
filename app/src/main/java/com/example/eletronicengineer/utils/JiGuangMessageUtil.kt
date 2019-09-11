package com.example.eletronicengineer.utils

import cn.jmessage.support.google.gson.JsonObject
import cn.jpush.im.android.api.content.MessageContent

class JiGuangMessageUtil
{
    companion object
    {
        fun toJsonObject(content:MessageContent):JsonObject
        {
            return content.toJsonElement().asJsonObject
        }
    }
}