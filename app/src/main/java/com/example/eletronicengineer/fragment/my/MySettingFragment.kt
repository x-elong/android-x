package com.example.eletronicengineer.fragment.my

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
import com.example.eletronicengineer.utils.SysApplication
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_log_out.view.*
import kotlinx.android.synthetic.main.fragment_my_setting.view.*

class MySettingFragment :Fragment() {
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_my_setting,container,false)
        iniData()
        initOnClick()
        return mView
    }

    private fun initOnClick() {
        mView.tv_setting_back.setOnClickListener {
            activity!!.finish()
        }
        mView.btn_log_out.setOnClickListener {
            val dialog = BottomSheetDialog(context!!)
            val dialogView = LayoutInflater.from(context!!).inflate(R.layout.dialog_log_out,null)
            dialogView.btn_switch_login.setOnClickListener {
                val intent = Intent(context,LoginActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            dialogView.btn_sign_out.setOnClickListener {
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
            (activity as MySettingActivity).switchFragment(MessageManagementFragment())
        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"清除缓存","0.0KB")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"意见反馈",false)
        item.jumpListener = View.OnClickListener {
            (activity as MySettingActivity).switchFragment(FeedbackFragment())
        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"关于",false)
        item.jumpListener = View.OnClickListener {
            (activity as MySettingActivity).switchFragment(RelatedFragment())
        }
        mMultiStyleItemList.add(item)
        mView.rv_setting_content.adapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_setting_content.layoutManager=LinearLayoutManager(context)
    }

}