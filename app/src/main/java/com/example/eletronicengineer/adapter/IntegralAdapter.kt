package com.example.eletronicengineer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.IntegralStyle
import kotlinx.android.synthetic.main.item_integral.view.*

class IntegralAdapter  : RecyclerView.Adapter<IntegralAdapter.ViewHolder> {

    lateinit var mContext:Context
    val mIntegralList:List<IntegralStyle>
    lateinit var mView:View
    constructor(mIntegralList:List<IntegralStyle>){
        this.mIntegralList = mIntegralList
    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        var integralImage: ImageView
        var integralTitle: TextView
        var integralContent: TextView
        var integralMark: TextView

        constructor(view: View):super (view){
            integralImage = view.iv_integral_image
            integralTitle = view.tv_item_integral_title
            integralContent = view.tv_integral_content
            integralMark = view.tv_integral_mark
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, viewType: Int): ViewHolder {
        mView= LayoutInflater.from(viewGroup.context).inflate(R.layout.item_integral, viewGroup, false)
        mContext = viewGroup.context
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(@NonNull viewHolder: ViewHolder, i: Int) {
        val integralStyle = mIntegralList[i]
        if(integralStyle.integralTitle == "无")
            viewHolder.integralTitle.hint = "无名称"
        else
            viewHolder.integralTitle.text = integralStyle.integralTitle
        viewHolder.integralContent.text = integralStyle.integralcontent
        when{
            integralStyle.integralstyleType == 1 ->{//积分详情
                viewHolder.integralMark.visibility=View.VISIBLE
                viewHolder.integralMark.text = integralStyle.integralMark
            }
            integralStyle.integralstyleType == 2 ->{//下级会员
                viewHolder.integralImage.visibility=View.VISIBLE
                if(integralStyle.integralImage == "无")
                    Glide.with(mContext).load(R.drawable.ic_insert_emoticon_black_24dp).override(50,50).fitCenter().into(viewHolder.integralImage)
                else
                    Glide.with(mContext).load(integralStyle.integralImage).override(50,50).fitCenter().into(viewHolder.integralImage)
            }
        }
    }

    override fun getItemCount(): Int {
        return mIntegralList.size
    }
}