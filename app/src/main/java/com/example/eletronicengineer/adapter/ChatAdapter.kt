package com.example.eletronicengineer.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.eletronicengineer.activity.ChatActivity
import com.example.eletronicengineer.R
import com.example.eletronicengineer.model.ChatItem
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatAdapter:RecyclerView.Adapter<ChatAdapter.VH>
{
    companion object
    {
        val FRIEND_TYPE=0
        val CHAT_GROUP_TYPE=1
    }
    class VH:RecyclerView.ViewHolder
    {
        var tvChatIcon:TextView
        var tvChatUserName:TextView
        var tvChatUserMessage:TextView
        var tvChatTime:TextView
        constructor(v:View):super(v)
        {
            tvChatIcon=v.tv_chat_avatar
            tvChatUserName=v.tv_chat_user_name
            tvChatUserMessage=v.tv_chat_user_message
            tvChatTime=v.tv_chat_send_time
        }
    }
    var mData:List<ChatItem>
    constructor(data:List<ChatItem>)
    {
        mData=data
    }
    override fun getItemViewType(position: Int): Int {
        when(mData[position].type)
        {
            ChatItem.ChatType.FRIEND->
            {
                return FRIEND_TYPE
            }
            ChatItem.ChatType.CHAT_GROUP->
            {
                return CHAT_GROUP_TYPE
            }
        }
        return super.getItemViewType(position)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        when(holder.itemViewType)
        {
            FRIEND_TYPE->
            {
                holder.itemView.setOnClickListener {
                    val context=holder.itemView.context
                    val intent= Intent(context,ChatActivity::class.java)
                    intent.putExtra("username",mData[position].chatUserName)
                    intent.putExtra("type","friend")
                    context.startActivity(intent)
                }
                holder.tvChatIcon.background=mData[position].chatIcon
                holder.tvChatUserName.text=mData[position].chatUserName
                holder.tvChatUserMessage.text=mData[position].chatUserMessage
                holder.tvChatTime.text=mData[position].chatTime
            }
            CHAT_GROUP_TYPE->
            {
                holder.itemView.setOnClickListener {
                    val context=holder.itemView.context
                    val intent= Intent(context,ChatActivity::class.java)
                    intent.putExtra("groupId",mData[position].groupId)
                    intent.putExtra("type","chatGroup")
                    context.startActivity(intent)
                }
                holder.tvChatIcon.background=mData[position].chatIcon
                holder.tvChatUserName.text=mData[position].chatUserName
                holder.tvChatUserMessage.text=mData[position].chatUserMessage
                holder.tvChatTime.text=mData[position].chatTime
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.item_chat,parent,false)
        return VH(v)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

}