package com.example.eletronicengineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.eletronicengineer.model.SelectFriendItem
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.item_select_friend.view.*

class SelectFriendAdapter:RecyclerView.Adapter<SelectFriendAdapter.VH>
{
    class VH:RecyclerView.ViewHolder
    {
        var tvUserAvatar:TextView
        var tvUserName:TextView
        var cbSelectStatus:CheckBox
        constructor(v: View):super(v)
        {
            tvUserAvatar=v.tv_select_friend_avatar
            tvUserName=v.tv_select_friend_name
            cbSelectStatus=v.cb_select_friend_status
        }
    }
    var mData:List<SelectFriendItem>
    constructor(data:List<SelectFriendItem>)
    {
        mData=data
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.item_select_friend,parent,false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.tvUserName.text=mData[position].userName
        holder.tvUserAvatar.background=mData[position].userAvatar
        holder.cbSelectStatus.isChecked=mData[position].selectStatus
        holder.cbSelectStatus.setOnCheckedChangeListener { compoundButton, bool ->
            mData[position].selectStatus=bool
        }
    }
}