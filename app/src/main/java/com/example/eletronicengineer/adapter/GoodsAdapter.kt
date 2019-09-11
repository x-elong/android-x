package com.example.eletronicengineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.Goods
import com.example.eletronicengineer.utils.GlideLoader
import kotlinx.android.synthetic.main.goods.view.*

class GoodsAdapter :RecyclerView.Adapter<GoodsAdapter.VH>{

    val mGoodsList:List<Goods>

    constructor(goodsList:List<Goods>){
        this.mGoodsList = goodsList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goods,parent,false)
        return VH(view)
    }


    override fun getItemCount(): Int {
        return mGoodsList.size
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        val goods = mGoodsList[position]
        holder.goodsName.text = goods.goodsName
        holder.goodsPrice.text = "￥${goods.goodsprice}"
        GlideLoader().loadImage(holder.goodsImage,goods.goodsPicture)
    }

    inner class VH:RecyclerView.ViewHolder{
        var goodsImage:ImageView
        var goodsName:TextView
        var goodsPrice:TextView
        constructor(v: View):super(v){
            goodsImage = v.iv_goods_picture
            goodsName = v.tv_goods_name
            goodsPrice = v.tv_goods_price
        }
    }
}