package com.example.eletronicengineer.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ImageDisplayActivity
import com.example.eletronicengineer.aninterface.Image
import com.example.eletronicengineer.utils.GlideLoader
import kotlinx.android.synthetic.main.image.view.*

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    var mImageList:List<Image>
    constructor(ImageList:List<Image>){
        this.mImageList = ImageList
    }
    inner class ViewHolder : RecyclerView.ViewHolder {
        var ivImage : ImageView
        constructor(view: View):super (view){
            ivImage=view.iv_image
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = mImageList[position]
        if(position==mImageList.size-1)
            holder.ivImage.setImageResource(R.drawable.photo)
        else
        GlideLoader().loadImage(holder.ivImage,image.imagePath)
        if(image.imageListener==null){
            holder.ivImage.setOnClickListener {
                val intent = Intent(holder.ivImage.context, ImageDisplayActivity::class.java)
                intent.putExtra("imagePath",image.imagePath)
                val data = Bundle()
                holder.ivImage.context!!.startActivity(intent)
            }
        }
        else
            holder.ivImage.setOnClickListener(image.imageListener)
    }

    override fun getItemCount(): Int {
        return mImageList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image,parent,false)
        return ViewHolder(view)
    }



}
