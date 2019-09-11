package com.example.eletronicengineer.model

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View

class ChatMenuItem(val chatMenuIcon:Drawable,val chatMenuName:String)
{
    var listener:View.OnClickListener?=null
}