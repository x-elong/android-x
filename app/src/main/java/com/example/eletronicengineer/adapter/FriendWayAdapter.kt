package com.example.eletronicengineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.eletronicengineer.model.FriendWayItem
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.item_friend_way.view.*

class FriendWayAdapter:RecyclerView.Adapter<FriendWayAdapter.VH>
{
    class VH:RecyclerView.ViewHolder
    {
        var tvFriendWayIcon:TextView
        var tvFriendWayName:TextView
        var tvFriendWayIntroduction:TextView
        constructor(v: View):super(v)
        {
            tvFriendWayIcon=v.tv_friend_way_avatar
            tvFriendWayName=v.tv_friend_way_user_name
            tvFriendWayIntroduction=v.tv_friend_way_user_message
        }
    }
    var mData:List<FriendWayItem> =ArrayList()
    constructor(data:List<FriendWayItem>)
    {
        mData=data
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.item_friend_way,parent,false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.tvFriendWayIcon.background=mData[position].icon
        holder.tvFriendWayName.text=mData[position].wayName
        holder.tvFriendWayIntroduction.text=mData[position].wayIntroduction
    }
}