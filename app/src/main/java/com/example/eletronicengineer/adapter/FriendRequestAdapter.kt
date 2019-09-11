package com.example.eletronicengineer.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.jpush.im.android.api.ContactManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback

import com.example.eletronicengineer.model.FriendRequestItem
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.dialog_friend_request_reason.view.*
import kotlinx.android.synthetic.main.item_send_friend_request.view.*

class FriendRequestAdapter:RecyclerView.Adapter<FriendRequestAdapter.VH>
{
    class VH:RecyclerView.ViewHolder
    {
        val btnSubmit:Button
        var tvName:TextView
        var tvAvatar:TextView
        constructor(v: View):super(v)
        {
            btnSubmit=v.btn_send_request_submit
            tvName=v.tv_send_request_name
            tvAvatar=v.tv_send_request_avatar
        }
    }
    companion object
    {
        val DECLINE_STATUS=0
        val ACCEPT_STATUS=1
        val WAIT_STATUS=2
    }
    var mData:List<FriendRequestItem>
    constructor(data:List<FriendRequestItem>)
    {
        mData=data
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.item_send_friend_request,parent,false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.tvAvatar.background=mData[position].userAvatar
        holder.tvName.text=mData[position].username
        holder.btnSubmit.text="添加"
        holder.btnSubmit.setOnClickListener {
            val dialogView=LayoutInflater.from(it.context).inflate(R.layout.dialog_friend_request_reason,null,false)
            val dialog=AlertDialog.Builder(it.context).setView(dialogView).setPositiveButton("确认",object:DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    var content=dialogView.et_friend_request_reason_content.text.toString()
                    if (content=="")
                        content="你好"
                    ContactManager.sendInvitationRequest(mData[position].username,null,content,object:BasicCallback(){
                        override fun gotResult(p0: Int, p1: String?) {
                            if (p0==0)
                                Toast.makeText(holder.itemView.context,"发送成功",Toast.LENGTH_SHORT).show()
                            else
                                Toast.makeText(holder.itemView.context,"发送失败 $p1",Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            })
            dialog.show()
        }
    }
}