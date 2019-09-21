package com.example.eletronicengineer.fragment.my

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import kotlinx.android.synthetic.main.dialog_datepicker.view.*
import kotlinx.android.synthetic.main.dialog_recyclerview.view.*
import kotlinx.android.synthetic.main.fragment_family_information.view.*

class FamilyInformationFragment :Fragment(){

    lateinit var mView: View
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    var adapter = RecyclerviewAdapter(ArrayList())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_family_information,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mMultiStyleItemList.clear()
        var item = MultiStyleItem(MultiStyleItem.Options.TITLE,"家庭情况","2")
        mMultiStyleItemList.add(item)
        item.backListener = View.OnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        item = MultiStyleItem(MultiStyleItem.Options.FOUR_DISPLAY,"子女姓名","年龄","出生日期","详情")
        mMultiStyleItemList.add(item)
        adapter = RecyclerviewAdapter(mMultiStyleItemList)
        adapter.mData[0].tvSelect = View.OnClickListener {
            val dialog = AlertDialog.Builder(context!!)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_recyclerview,null)
            val rvList:MutableList<MultiStyleItem> = ArrayList()
            rvList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"子女姓名"))
            rvList.add(MultiStyleItem(MultiStyleItem.Options.SELECT_DIALOG, arrayListOf("男","女"),"性别"))
            rvList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"出生日期",false))
            rvList[rvList.size-1].jumpListener = View.OnClickListener {
                val dialog = AlertDialog.Builder(dialogView.context)
                val mDialogView = layoutInflater.inflate(R.layout.dialog_datepicker,null)
                if(rvList[rvList.size-1].shiftInputContent!="") {
                    val s = rvList[rvList.size-1].shiftInputContent.split("-")
                    mDialogView.date_picker.init(s[0].toInt(),s[1].toInt()-1,s[2].toInt(),null)
                }
                dialog.setTitle("出生日期")
                    .setView(mDialogView)
                    .setNegativeButton("取消",null)
                    .setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                        rvList[rvList.size-1].shiftInputContent="${mDialogView.date_picker.year}-${mDialogView.date_picker.month+1}-${mDialogView.date_picker.dayOfMonth}"
                        dialogView.rv_dialog.adapter = RecyclerviewAdapter(rvList)
                    })
                    .create()
                    .show()
            }
            dialogView.rv_dialog.adapter = RecyclerviewAdapter(rvList)
            dialogView.rv_dialog.layoutManager=LinearLayoutManager(dialogView.context)
            dialog.setTitle("子女信息")
                .setView(dialogView)
                .setNegativeButton("取消",null)
                .setPositiveButton("确定",object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        var mList = adapter.mData.toMutableList()
                        val mItem = MultiStyleItem(MultiStyleItem.Options.FOUR_DISPLAY,rvList[0].inputSingleContent,rvList[1].selectContent,rvList[2].shiftInputContent,"···")
                        mList.add(mItem)
                        adapter.mData = mList
                        adapter.mData[adapter.mData.size-1].fourDisplayListener = View.OnClickListener {
                            val values: Array<String> = arrayOf("修改", "删除")
                            val alertBuilder = AlertDialog.Builder(context!!)
                            alertBuilder.setItems(values, DialogInterface.OnClickListener { dialogInterface, i ->
                                if (values[i] == "删除") {
                                    val position = adapter.mData.indexOf(mItem)
                                    Log.i("position is",position.toString())
                                    mList = adapter.mData.toMutableList()
                                    mList.removeAt(position)
                                    adapter.mData = mList
                                    Log.i("mList size is",mList.size.toString())
                                    adapter.notifyDataSetChanged()
                                    Log.i("adapter size is",adapter.mData.size.toString())
                                } else if (values[i] == "修改") {
                                    val builder = AlertDialog.Builder(context!!)
                                    val v = View.inflate(context, R.layout.dialog_recyclerview, null)
                                    val data: MutableList<MultiStyleItem> = ArrayList()
                                    data.clear()
                                    data.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"子女姓名"))
                                    data[data.size-1].inputSingleContent=adapter.mData[adapter.mData.indexOf(mItem)].fourDisplayTitle
                                    data.add(MultiStyleItem(MultiStyleItem.Options.SELECT_DIALOG, arrayListOf("男","女"),"性别"))
                                    data[data.size-1].selectContent=adapter.mData[adapter.mData.indexOf(mItem)].fourDisplayContent1
                                    data.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"出生日期",false))
                                    data[data.size-1].shiftInputContent=adapter.mData[adapter.mData.indexOf(mItem)].fourDisplayContent2
                                    data[data.size-1].jumpListener = View.OnClickListener {
                                        val dialog = AlertDialog.Builder(dialogView.context)
                                        val mDialogView = layoutInflater.inflate(R.layout.dialog_datepicker,null)
                                        if(data[data.size-1].shiftInputContent!="") {
                                            val s = data[data.size-1].shiftInputContent.split("-")
                                            mDialogView.date_picker.init(s[0].toInt(),s[1].toInt()-1,s[2].toInt(),null)
                                        }
                                        dialog.setTitle("出生日期")
                                            .setView(mDialogView)
                                            .setNegativeButton("取消",null)
                                            .setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                                                data[data.size-1].shiftInputContent="${mDialogView.date_picker.year}-${mDialogView.date_picker.month+1}-${mDialogView.date_picker.dayOfMonth}"
                                                dialogView.rv_dialog.adapter = RecyclerviewAdapter(data)
                                            })
                                            .create()
                                            .show()
                                    }
                                    val mAdapter = RecyclerviewAdapter(data)
                                    v.rv_dialog.adapter = mAdapter
                                    v.rv_dialog.layoutManager = LinearLayoutManager(v.context)
                                    builder.setTitle("子女信息")
                                        .setCancelable(true)
                                        .setNegativeButton("取消", null)
                                        .setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface, i ->
                                            adapter.mData[adapter.mData.indexOf(mItem)].fourDisplayTitle = data[0].inputSingleContent
                                            adapter.mData[adapter.mData.indexOf(mItem)].fourDisplayContent1 = data[1].selectContent
                                            adapter.notifyItemChanged(adapter.mData.indexOf(mItem))
                                        })
                                        .setView(v)
                                        .create()
                                        .show()
//                                    .window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
//                                    .window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                                }
                            }).show()
                        }
                        adapter.notifyItemInserted(mList.size-1)
                    }
                })
                .create()
                .show()
        }
        mView.rv_family_content.adapter = adapter
        mView.rv_family_content.layoutManager = LinearLayoutManager(context)
        (mView.rv_family_content.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
    }
    fun update(){
        mView.rv_family_content.adapter = adapter
        mView.rv_family_content.layoutManager = LinearLayoutManager(context)
    }
}