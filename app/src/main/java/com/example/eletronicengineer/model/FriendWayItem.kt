package com.example.eletronicengineer.model

import android.graphics.drawable.Drawable
import android.view.View

class FriendWayItem(var icon:Drawable, var wayName:String, var wayIntroduction:String)
{
    var listener: View.OnClickListener?=null
}