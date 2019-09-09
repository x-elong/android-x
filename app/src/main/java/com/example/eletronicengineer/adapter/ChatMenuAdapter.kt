package com.example.eletronicengineer.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.eletronicengineer.model.ChatMenuItem
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.item_chat_menu.view.*

class ChatMenuAdapter: RecyclerView.Adapter<ChatMenuAdapter.VH>
{
    class VH:RecyclerView.ViewHolder
    {
        var tvItemIcon:TextView
        var tvItemName:TextView
        constructor(v: View):super(v)
        {

            tvItemIcon=v.tv_chat_menu_icon
            tvItemName=v.tv_chat_menu_name
        }
    }
    var mData:List<ChatMenuItem>
    constructor(data:List<ChatMenuItem>)
    {
        mData=data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.item_chat_menu,parent,false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.tvItemName.text = mData[position].chatMenuName
        holder.tvItemIcon.background=mData[position].chatMenuIcon
        if (mData[position].listener!=null)
        {
            holder.itemView.setOnClickListener(mData[position].listener)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}