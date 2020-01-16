package com.example.eletronicengineer.fragment.my

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.LoginActivity
import com.example.eletronicengineer.activity.MySettingActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_log_out.view.*
import kotlinx.android.synthetic.main.fragment_my_setting.view.*

class MySettingFragment :Fragment() {
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    lateinit var mView:View
    lateinit var mAdapter: RecyclerviewAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_my_setting,container,false)
        iniData()
        initOnClick()
        return mView
    }

    private fun initOnClick() {
        val result = Observable.create<String> {

            it.onNext(CacheUtil.getCacheSize(mView.context))
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mAdapter.mData[1].singleDisplayRightContent = it
                mAdapter.notifyDataSetChanged()
        }
        mView.tv_setting_back.setOnClickListener {
            activity!!.finish()
        }
        mView.btn_log_out.setOnClickListener {
            val dialog = BottomSheetDialog(context!!)
            val dialogView = LayoutInflater.from(context!!).inflate(R.layout.dialog_log_out,null)
            dialogView.btn_switch_login.setOnClickListener {
                dialog.dismiss()
                UnSerializeDataBase.isLogined=true
                val intent = Intent(context,LoginActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            dialogView.btn_sign_out.setOnClickListener {
                dialog.dismiss()
                SysApplication.getInstance().exit()
            }
            dialogView.btn_cancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(dialogView)
            dialog.show()
        }
    }

    private fun iniData() {
        mMultiStyleItemList.clear()
        var item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"消息管理",false)
        item.jumpListener = View.OnClickListener {
            FragmentHelper.switchFragment(activity!!,MessageManagementFragment(),R.id.frame_my_setting,"")
        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"清除缓存","0.0KB")
        item.jumpListener = View.OnClickListener {
            if(item.singleDisplayRightContent=="0.0KB"){
                ToastHelper.mToast(mView.context,"当前缓存为空!")
            }
            else{
                val dialog = AlertDialog.Builder(mView.context)
                dialog.setTitle("清除缓存")
                    .setMessage("                   确定清除缓存？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                        CacheUtil.cleanCache(mView.context)
                        mAdapter.mData[1].singleDisplayRightContent = "0.0KB"
                        mAdapter.notifyDataSetChanged()
                    })
                    .create().show()
            }
        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"意见反馈",false)
        item.jumpListener = View.OnClickListener {
            FragmentHelper.switchFragment(activity!!,FeedbackFragment(),R.id.frame_my_setting,"")
        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"关于",false)
        item.jumpListener = View.OnClickListener {
            FragmentHelper.switchFragment(activity!!,RelatedFragment(),R.id.frame_my_setting,"")
        }
        mMultiStyleItemList.add(item)
        mAdapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_setting_content.adapter = mAdapter
        mView.rv_setting_content.layoutManager=LinearLayoutManager(context)

    }

}