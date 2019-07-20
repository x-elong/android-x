package com.example.eletronicengineer.model

import com.google.gson.annotations.SerializedName

class HttpResult
{
  @SerializedName("code")
  lateinit var code:String
  @SerializedName("desc")
  lateinit var desc:String
  @SerializedName("message")
  lateinit var message:String

  override fun toString(): String {
    return "HttpResult(code='$code', desc='$desc', message='$message')"
  }

}
//okhttp

//retrofit okhttp rxjava GsonConverter