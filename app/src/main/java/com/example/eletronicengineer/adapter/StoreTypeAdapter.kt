package com.example.eletronicengineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.StoresName
import kotlinx.android.synthetic.main.item_mall_goods_type.view.*

/**
 * 项目名:    android
 * 包名:      com.example.eletronicengineer.adapter
 * 文件名:    StoreTypeAdapter
 * 创建者:    LLH
 * 创建时间:  2019/8/16 15:00
 * 描述:      TODO
 */
class StoreTypeAdapter:RecyclerView.Adapter<StoreTypeAdapter.StoreViewHolder> {
    //店铺名称的列表
    var storesList:ArrayList<StoresName>
    //构造函数
    constructor(storesList:ArrayList<StoresName>){
        this.storesList = storesList
    }
    //创建一个内部类
    inner class StoreViewHolder:RecyclerView.ViewHolder{
        //一个图片，一个店名，一个地址，一个主营
        var storeImage:ImageView
        var storeName:TextView
        var storeAdd:TextView
        var storeMajor:TextView
        var storeShift:ImageView
        //构造函数
        constructor(view:View):super(view){
            //绑定控件
            storeImage = view.iv_mall_store_name
            storeName = view.tv_mall_store_name
            storeAdd = view.tv_mall_add_name
            storeMajor = view.tv_mall_major_name
            storeShift = view.iv_person_inform_shift
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        //填充视图，并返回StoreViewHolder的实例
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mall_goods_type,parent,false)
        //实例
        val storeViewHolder = StoreViewHolder(view)
        return storeViewHolder
    }

    override fun getItemCount(): Int {
        return storesList.size
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        // holder.setIsRecyclable(false)
        //给相应项的内部控件赋值
        val storesName = storesList[position]
        holder.storeImage.setImageResource(storesName.storeImage)
        holder.storeName.text = storesName.storeName
        holder.storeAdd.text = storesName.storeAdd
        holder.storeMajor.text = storesName.storeMajor
        holder.storeShift.setOnClickListener(storesName.storeListener)
        when {
            storesName.style==0 -> { }
            storesName.style==1 -> {
                holder.storeShift.visibility = View.VISIBLE
            }
        }
    }
}