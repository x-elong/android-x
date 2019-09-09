package com.example.eletronicengineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.ExpandMenuItem
import kotlinx.android.synthetic.main.expand_menu_item.view.*


class ExpandMenuAdapter : RecyclerView.Adapter<ExpandMenuAdapter.VH>{
    var mExpandMenuList:List<ExpandMenuItem>
    constructor(expandMenuList:List<ExpandMenuItem>){
        this.mExpandMenuList=expandMenuList
    }
    inner class VH:RecyclerView.ViewHolder{
        val tvname:TextView
        constructor(view: View):super(view){
            this.tvname = view.tv_name

        }
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        val ExpandMenu = mExpandMenuList[position]
        holder.tvname.text = ExpandMenu.itemName
        holder.tvname.setOnClickListener(ExpandMenu.viewOnClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.expand_menu_item,parent,false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return mExpandMenuList.size
    }
}