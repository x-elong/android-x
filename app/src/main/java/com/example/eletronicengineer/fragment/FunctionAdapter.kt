package com.example.eletronicengineer.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.Function
import kotlinx.android.synthetic.main.function.view.*


class FunctionAdapter : RecyclerView.Adapter<FunctionAdapter.ViewHolder> {

    var mFunctionList:List<Function>

    constructor(functionList:List<Function>){
        this.mFunctionList=functionList
    }
    inner class ViewHolder : RecyclerView.ViewHolder {
        var function_Image: ImageView
        var function_name: TextView
        constructor(view:View):super (view){
            function_Image=view.function_iv
            function_name=view.function_tv
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.function, viewGroup, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(@NonNull viewHolder: ViewHolder, i: Int) {
        val function = mFunctionList[i]
        viewHolder.function_Image.setImageResource(function.imageid)
        viewHolder.function_name.text = function.name
    }

    override fun getItemCount(): Int {
        return mFunctionList.size
    }

}