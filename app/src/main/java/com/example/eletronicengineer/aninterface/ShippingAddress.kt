package com.example.eletronicengineer.aninterface

import android.view.View
import com.electric.engineering.model.MultiStyleItem

/**
 * 项目名:    android
 * 包名:      com.example.eletronicengineer.aninterface
 * 文件名:    ShippingAddress
 * 创建者:    LLH
 * 创建时间:  2019/8/17 10:05
 * 描述:      TODO
 */
class ShippingAddress(var shippingAddressPeople:String, var shippingAddressPhone:String, var shippingAddress:String,var shippingAddressListener: View.OnClickListener?){
    var mData:MutableList<MultiStyleItem> = ArrayList()
    var shippingAddressItemListener:View.OnClickListener? = null
}