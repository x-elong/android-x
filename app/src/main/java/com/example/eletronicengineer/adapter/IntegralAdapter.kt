package com.example.eletronicengineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.IntegralStyle
import kotlinx.android.synthetic.main.item_integral.view.*

class IntegralAdapter  : RecyclerView.Adapter<IntegralAdapter.ViewHolder> {

    val mIntegralList:List<IntegralStyle>
    lateinit var mView:View
    constructor(mIntegralList:List<IntegralStyle>){
        this.mIntegralList = mIntegralList
    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        var integralImage: TextView
        var integralTitle: TextView
        var integralContent: TextView
        var integralMark: TextView

        constructor(view: View):super (view){
            integralImage = view.tv_integral_image
            integralTitle = view.tv_item_integral_title
            integralContent = view.tv_integral_content
            integralMark = view.tv_integral_mark
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, viewType: Int): IntegralAdapter.ViewHolder {
        mView= LayoutInflater.from(viewGroup.context).inflate(R.layout.item_integral, viewGroup, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(@NonNull viewHolder: IntegralAdapter.ViewHolder, i: Int) {
        val integralStyle = mIntegralList[i]
        viewHolder.integralTitle.text = integralStyle.integralTitle
        viewHolder.integralContent.text = integralStyle.integralcontent
        when{
            integralStyle.integralstyleType == 1 ->{
                viewHolder.integralMark.visibility=View.VISIBLE
                viewHolder.integralMark.text = integralStyle.integralMark
            }
            integralStyle.integralstyleType == 2 ->{
                viewHolder.integralImage.visibility=View.GONE
                //viewHolder.integralImage.setBackgroundResource(integralStyle.integralImage.toInt())
            }
            integralStyle.integralstyleType == 3 ->{
                viewHolder.integralContent.visibility = View.GONE
                viewHolder.integralMark.visibility=View.VISIBLE
                viewHolder.integralMark.text = integralStyle.integralMark
            }
        }
    }

    override fun getItemCount(): Int {
        return mIntegralList.size
    }
}