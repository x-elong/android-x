package com.example.eletronicengineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.ShippingAddress
import kotlinx.android.synthetic.main.item_shipping_address.view.*

/**
 * 项目名:    android
 * 包名:      com.example.eletronicengineer.adapter
 * 文件名:    ShippingAddressAdapter
 * 创建者:    LLH
 * 创建时间:  2019/8/17 10:04
 * 描述:      TODO
 */
class ShippingAddressAdapter:RecyclerView.Adapter<ShippingAddressAdapter.ShippingAddressViewHolder> {
    //店铺名称的列表
    var mShippingAddressList:List<ShippingAddress>
    //构造函数
    constructor(mShippingAddressList:List<ShippingAddress>){
        this.mShippingAddressList = mShippingAddressList
    }
    //创建一个内部类
    inner class ShippingAddressViewHolder: RecyclerView.ViewHolder{
        var shippingAddressPeople: TextView
        var shippingAddressPhone: TextView
        var shippingAddress: TextView
        var shippingAddressEdit: TextView//未来的监听事件
        //构造函数
        constructor(view: View):super(view){
            //绑定控件
            shippingAddressPeople = view.tv_shipping_address_people
            shippingAddressPhone = view.tv_shipping_address_phone
            shippingAddress = view.tv_shipping_address
            shippingAddressEdit = view.tv_shipping_address_edit
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingAddressViewHolder {
        //填充视图，并返回StoreViewHolder的实例
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shipping_address,parent,false)
        //实例
        val shippingAddressViewHolder = ShippingAddressViewHolder(view)
        return shippingAddressViewHolder
    }

    override fun getItemCount(): Int {
        return mShippingAddressList.size
    }

    override fun onBindViewHolder(holder: ShippingAddressViewHolder, position: Int) {
        // holder.setIsRecyclable(false)
        //给相应项的内部控件赋值
        val shippingAddress = mShippingAddressList[position]
        holder.shippingAddressPeople.text =shippingAddress.shippingAddressPeople
        holder.shippingAddressPhone.text = shippingAddress.shippingAddressPhone
        holder.shippingAddress.text = shippingAddress.shippingAddress.replace("|","")
        holder.shippingAddressEdit.setOnClickListener(shippingAddress.shippingAddressListener)
        if(shippingAddress.shippingAddressItemListener!=null){
            holder.itemView.setOnClickListener(shippingAddress.shippingAddressItemListener)
        }

    }
}