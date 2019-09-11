package com.example.eletronicengineer.adapter

import android.content.DialogInterface
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.ImageCheck
import com.example.eletronicengineer.custom.CustomDialog
import kotlinx.android.synthetic.main.item_image_check.view.*
import kotlinx.android.synthetic.main.shift_dialog.view.*

class ImageCheckAdapter: RecyclerView.Adapter<ImageCheckAdapter.ViewHolder> {
    var mImageCheckList: List<ImageCheck>
    lateinit var  mView:View
    constructor(imageCheckList: List<ImageCheck>) {
        this.mImageCheckList = imageCheckList
    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        var imageCheck_Image: TextView
        var imageCheck_name: TextView
        var imageCheck_more: TextView

        constructor(view: View) : super(view) {
            imageCheck_Image = view.tv_check_image
            imageCheck_name = view.tv_check_title
            imageCheck_more = view.tv_check_more
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, viewType: Int): ViewHolder {
        mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_image_check, viewGroup, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(@NonNull viewHolder: ViewHolder, i: Int) {
        val imageCheck = mImageCheckList[i]
        viewHolder.imageCheck_Image.setBackgroundResource(imageCheck.imageid)
        viewHolder.imageCheck_name.text = imageCheck.name
        viewHolder.imageCheck_more.setOnClickListener(initOnClick(mView,imageCheck))
    }

    override fun getItemCount(): Int {
        return mImageCheckList.size
    }
    fun initOnClick(v: View,imageCheck:ImageCheck):View.OnClickListener{
        return View.OnClickListener {
            val option1Items = listOf("下载", "重命名", "删除")
            val selectTitle = v.tv_check_title.text.toString()
            val handler = Handler(Handler.Callback {
                when (it.what) {
                    RecyclerviewAdapter.MESSAGE_SELECT_OK -> {
                        var type = it.data.getString("selectContent")
                        if (type == "重命名") {
                            val builder = AlertDialog.Builder(v.context)
                            val dialogView = View.inflate(mView.context, R.layout.shift_dialog, null)
                            dialogView.shift_dialog_content.setText(selectTitle)
                            builder.setTitle("重命名:")
                            builder.setNegativeButton("取消", null)
                            builder.setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                                run {
                                    v.tv_check_title.text = dialogView.shift_dialog_content.text.toString()
                                }
                            })
                            val dialog = builder.create()
                            dialog.setView(dialogView)
                            dialog.show()
                        }
                        else if(type=="删除"){
                            val builder = AlertDialog.Builder(v.context)
                            builder.setTitle("是否删除当前内容？")
                            builder.setNegativeButton("取消", null)
                            builder.setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                                run {
                                    val data = mImageCheckList.toMutableList()
                                    val position = data.indexOf(imageCheck)
                                    data.removeAt(position)
                                    mImageCheckList = data
                                    if(mImageCheckList.isEmpty())
                                        notifyDataSetChanged()
                                    else
                                    notifyItemRangeRemoved(position,itemCount)
                                }
                            })
                            builder.create().show()
                        }
                        false
                    }
                    else -> {
                        false
                    }
                }
            })
            val selectDialog =
                CustomDialog(CustomDialog.Options.SELECT_DIALOG, v.context, option1Items, handler).dialog
            selectDialog.show()
        }
    }
}