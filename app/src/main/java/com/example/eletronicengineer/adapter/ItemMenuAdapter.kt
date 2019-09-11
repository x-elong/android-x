package com.example.eletronicengineer.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.ItemMenu
import kotlinx.android.synthetic.main.item_goods_menu.view.*

class ItemMenuAdapter : RecyclerView.Adapter<ItemMenuAdapter.ViewHolder> {
    var mItemMenuList:List<ItemMenu>
    constructor(itemMenuList:List<ItemMenu>) {
        this.mItemMenuList = itemMenuList
    }
    inner class ViewHolder : RecyclerView.ViewHolder {
        var itemMenu : TextView
        constructor(view: View) : super(view) {
            itemMenu = view.tv_goods_menu
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, viewType: Int):ViewHolder {
        var view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_goods_menu, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull viewHolder:ViewHolder, i: Int) {
        val itemMenu= mItemMenuList[i]
        viewHolder.itemMenu.text = itemMenu.name
        viewHolder.itemMenu.setOnClickListener(itemMenu.viewOnClickListener)
        if(itemMenu.checked){
            viewHolder.itemMenu.setBackgroundColor(Color.parseColor("#ffffff"))
        }else{
            viewHolder.itemMenu.setBackgroundColor(Color.parseColor("#efefef"))
        }
//        viewHolder.itemMenu.setOnClickListener {
//            if(viewHolder.itemMenu.isSelected){
//               viewHolder.itemMenu.setBackgroundColor(Color.WHITE)
//            }else{
//            viewHolder.itemMenu.setBackgroundColor(Color.parseColor("#efefef"))
//            }
//        }
    }

    override fun getItemCount(): Int {
        return mItemMenuList.size
    }
}