package com.example.eletronicengineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.ExtensionStyle
import kotlinx.android.synthetic.main.item_extension_earn.view.*

class ExtensionAdapter: RecyclerView.Adapter<ExtensionAdapter.ViewHolder> {
    val mExtensionList:List<ExtensionStyle>
    lateinit var mView: View
    constructor(mExtensionList:List<ExtensionStyle>){
        this.mExtensionList = mExtensionList
    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        var extensionMonth : TextView
        var extensionMonthMark : TextView

        constructor(view: View):super (view){
            extensionMonth = view.tv_extension_month
            extensionMonthMark = view.tv_extension_month_mark
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, viewType: Int): ExtensionAdapter.ViewHolder {
        mView= LayoutInflater.from(viewGroup.context).inflate(R.layout.item_extension_earn, viewGroup, false)
        return ViewHolder(mView)
    }
    override fun onBindViewHolder(@NonNull viewHolder: ViewHolder, i: Int) {
        var extensionStyle = mExtensionList[i]
        viewHolder.extensionMonth.text = extensionStyle.extensionMonth
        viewHolder.extensionMonthMark.text = extensionStyle.extensionMonthEarn
    }
    override fun getItemCount(): Int {
        return mExtensionList.size
    }
}