package com.example.eletronicengineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.CheckBoxStyle
import com.example.eletronicengineer.aninterface.Function
import kotlinx.android.synthetic.main.function.view.*
import kotlinx.android.synthetic.main.item_chekbox_style.view.*
import kotlinx.android.synthetic.main.item_image_check.view.*


class CheckBoxAdapter : RecyclerView.Adapter<CheckBoxAdapter.ViewHolder> {

    var mCheckBoxStyleList:List<CheckBoxStyle>

    constructor(mCheckBoxStyleList:List<CheckBoxStyle>){
        this.mCheckBoxStyleList=mCheckBoxStyleList
    }
    inner class ViewHolder : RecyclerView.ViewHolder {
        var chekboxSelectTitle: TextView
        var chekboxDetail: TextView
        var lookUp: ImageView
        var lookupMore: ImageView


        constructor(view:View):super (view){
            chekboxSelectTitle=view.tv_chekbox_select_title
            chekboxDetail=view.tv_chekbox_detail
            lookUp=view.tv_look_up
            lookupMore=view.tv_look_up_more
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_chekbox_style, viewGroup, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(@NonNull viewHolder: ViewHolder, i: Int) {
        val checkBoxStyle = mCheckBoxStyleList[i]
        viewHolder.chekboxSelectTitle.text = checkBoxStyle.itemName
        when {
            checkBoxStyle.selectStyle==0 -> {}
            checkBoxStyle.selectStyle==1 -> {
                viewHolder.chekboxDetail.visibility=View.VISIBLE
                viewHolder.chekboxDetail.text = checkBoxStyle.itemDetail
            }
            checkBoxStyle.selectStyle == 2 -> {
                viewHolder.lookUp.visibility=View.VISIBLE
                viewHolder.lookUp.setImageResource(R.drawable.ic_expand)
            }
            checkBoxStyle.selectStyle == 3 -> {
                viewHolder.lookupMore.visibility=View.VISIBLE
                viewHolder.lookupMore.setImageResource(R.drawable.more)
            }
        }
    }

    override fun getItemCount(): Int {
        return mCheckBoxStyleList.size
    }

}