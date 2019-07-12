package com.example.eletronicengineer.utils

import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import java.io.Serializable
class SerializeHelper :Serializable
{
    var adapter:RecyclerviewAdapter?=null
    var multiStyleItemList:MutableList<MultiStyleItem>?=null
    var stringMap:Map<String,String>?=null
}