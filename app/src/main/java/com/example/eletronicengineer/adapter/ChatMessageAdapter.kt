package com.example.eletronicengineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.eletronicengineer.model.ChatMessageItem
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.item_left_message.view.*
import kotlinx.android.synthetic.main.item_right_message.view.*

class ChatMessageAdapter:RecyclerView.Adapter<ChatMessageAdapter.VH>
{
    companion object
    {
        val LEFT_MESSAGE_TYPE=0
        val RIGHT_MESSAGE_TYPE=1
    }
    class VH:RecyclerView.ViewHolder
    {
        lateinit var tvLeftMessageContent:TextView
        lateinit var tvRightMessageContent:TextView
        constructor(v: View,itemViewType:Int):super(v)
        {
            when(itemViewType)
            {
                LEFT_MESSAGE_TYPE->
                {
                    tvLeftMessageContent=v.tv_left_message_content
                }
                RIGHT_MESSAGE_TYPE->
                {
                    tvRightMessageContent=v.tv_right_message_content
                }
                else->
                {

                }
            }
        }
    }
    var mData:List<ChatMessageItem>
    constructor(data:List<ChatMessageItem>)
    {
        mData=data
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder:VH
        when(viewType)
        {
            LEFT_MESSAGE_TYPE->
            {
                val v=LayoutInflater.from(parent.context).inflate(R.layout.item_left_message,parent,false)
                holder=VH(v,viewType)
            }
            RIGHT_MESSAGE_TYPE->
            {
                val v=LayoutInflater.from(parent.context).inflate(R.layout.item_right_message,parent,false)
                holder=VH(v,viewType)
            }
            else->
            {
                val v=LayoutInflater.from(parent.context).inflate(R.layout.item_right_message,parent,false)
                holder=VH(v,viewType)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        when(holder.itemViewType)
        {
            LEFT_MESSAGE_TYPE->
            {
                holder.tvLeftMessageContent.text=mData[position].content
            }
            RIGHT_MESSAGE_TYPE->
            {
                holder.tvRightMessageContent.text=mData[position].content
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return  if (mData[position].isMe)
        {
            RIGHT_MESSAGE_TYPE
        }
        else
            LEFT_MESSAGE_TYPE
    }
}