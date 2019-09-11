package com.example.eletronicengineer.model

import com.google.gson.annotations.SerializedName

class HttpResult<T>(val code:String, val desc:String, val message:T)
{
    override fun toString(): String {
        return "HttpResult(code='$code', desc='$desc', message='$message')"
    }

}
//okhttp

//retrofit okhttp rxjava GsonConverter