package com.example.eletronicengineer.fragment.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.*
import com.example.eletronicengineer.adapter.FunctionAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.aninterface.Function
import com.example.eletronicengineer.model.ApiConfig
import com.example.eletronicengineer.utils.GlideLoader
import com.example.eletronicengineer.utils.getUser
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_recommended_application.view.*
import kotlinx.android.synthetic.main.my.view.*

class MyFragment : Fragment() {
    lateinit var mView:View
    val mFunctionList:MutableList<Function> = ArrayList()
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.my, container, false)
        initData()
        initOnClick()
        return mView
    }

    private fun initOnClick() {
        mView.view_my.setOnClickListener {
            val intent =Intent(activity,MyInformationActivity::class.java)
            startActivity(intent)
        }
        mView.tv_qr_code.setOnClickListener {
            val intent =Intent(activity,MyQRCodeActivity::class.java)
            startActivity(intent)
        }
        mView.my_setting.setOnClickListener {
            val intent =Intent(activity,MySettingActivity::class.java)
            startActivity(intent)
        }
        mView.btn_subscribe.setOnClickListener {
            val intent =Intent(activity,SubscribeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initData() {
        getDataUser()
        mFunctionList.clear()
        mMultiStyleItemList.clear()
        mFunctionList.add(Function("我的发布",R.drawable.my_release,View.OnClickListener {
            val intent = Intent(activity,MyReleaseActivity::class.java)
            startActivity(intent)
        }))
        mFunctionList.add(Function("我的订单",R.drawable.my_order,View.OnClickListener {
            val intent = Intent(activity,MyOrderActivity::class.java)
            startActivity(intent)
        }))
        mFunctionList.add(Function("收货地址",R.drawable.shipping_address,View.OnClickListener {
            val intent = Intent(activity,ShippingAddressActivity::class.java)
            startActivity(intent)
        }))
        mFunctionList.add(Function("分销",R.drawable.distribution,View.OnClickListener {
            val intent = Intent(activity,RetailStoreActivity::class.java)
            startActivity(intent)
        }))
        mView.rv_my_function_content.adapter=FunctionAdapter(mFunctionList)
        mView.rv_my_function_content.layoutManager = GridLayoutManager(context,4)

        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"我的报名",false))
        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener=View.OnClickListener {
            val intent = Intent(activity,MyRegistrationActivity::class.java)
            startActivity(intent)
        }
//        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"签名设置",false))
//        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener=View.OnClickListener {
//            val intent = Intent(activity,MySignatureActivity::class.java)
//            startActivity(intent)
//        }
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"认证管理",false))
        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener=View.OnClickListener {
            val intent = Intent(activity,MyCertificationActivity::class.java)
            startActivity(intent)
        }
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"帮助中心",false))

        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"推荐电企通",false))
        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener=View.OnClickListener {
            val dialog = BottomSheetDialog(context!!)
            val dialogView = LayoutInflater.from(context!!).inflate(R.layout.dialog_recommended_application,null)
            dialogView.view_sms.setOnClickListener {
                dialog.dismiss()
            }
            dialogView.view_we_chat.setOnClickListener {
                dialog.dismiss()
            }
            dialogView.view_qq.setOnClickListener {
                dialog.dismiss()
            }
            dialogView.tv_cancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(dialogView)
            dialog.show()
        }
        mView.rv_my_other_function_content.adapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_my_other_function_content.layoutManager = LinearLayoutManager(context)
    }
    fun getDataUser(){
        val result = getUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                GlideLoader().loadImage(mView.iv_my_header,it.message.user.headerImg)
                mView.tv_my_phone.text = it.message.user.phone
            },{
                it.printStackTrace()
            })
    }
}