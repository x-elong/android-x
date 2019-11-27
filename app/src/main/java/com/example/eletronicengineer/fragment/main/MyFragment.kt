package com.example.eletronicengineer.fragment.main

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.*
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.db.My.UserSubitemEntity
import com.example.eletronicengineer.utils.GlideLoader
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_recommended_application.view.*
import kotlinx.android.synthetic.main.my.view.*

class MyFragment : Fragment() {
    lateinit var mView:View
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    lateinit var user:UserSubitemEntity
    private var headerImg = ""
    var vipType = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.my, container, false)
        initFragment()
        initOnClick()
        return mView
    }
    override fun onStart() {
        super.onStart()
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        headerImg = pref.getString("headerImg","")
        initData()
        val result = NetworkAdapter().getDataUser().subscribe( {
            user = it.message.user
            if(headerImg!=it.message.user.headerImg!!){
                headerImg = it.message.user.headerImg!!
                pref.edit().putString("headerImg",it.message.user.headerImg).apply()
                initData()
            }
        },{
            it.printStackTrace()
        })
        vipType = pref.getString("vipType","")
        initVipType(vipType)
        NetworkAdapter().getDataUserOpenPower().subscribe( {
            val vipType = it.message
            if(vipType!=this.vipType){
                this.vipType = vipType
                pref.edit().putString("vipType",this.vipType).apply()
                initVipType(vipType)
                }
        },{
            it.printStackTrace()
        })
    }
    fun initVipType(vipType :String){
        when(vipType){
            "elite_vip"->{
                this.vipType = "精英会员"
                UnSerializeDataBase.userVipLevel = 1
            }
            "gold_vip"->{
                this.vipType = "黄金会员"
                UnSerializeDataBase.userVipLevel = 2
            }
            else->{
                this.vipType = "普通会员"
                UnSerializeDataBase.userVipLevel = 0
            }
        }
        mView.tv_my_vip_level.text = this.vipType
    }
    private fun initData() {
        GlideLoader().loadImage(mView.iv_my_header,headerImg)
        mView.tv_my_phone.text = UnSerializeDataBase.userPhone
    }

    private fun initOnClick() {
        mView.view_my.setOnClickListener {
            val intent =Intent(activity,MyInformationActivity::class.java)
            startActivity(intent)
        }
//        mView.tv_my_vip.setOnClickListener{
//            val intent = Intent(activity,MyVipActivity::class.java)
//            startActivity(intent)
//        }
        mView.tv_qr_code.setOnClickListener {
            val intent =Intent(activity,MyQRCodeActivity::class.java)
            intent.putExtra("headerImg",headerImg)
            startActivity(intent)
        }
        mView.my_setting.setOnClickListener {
            val intent =Intent(activity,MySettingActivity::class.java)

            startActivity(intent)
        }
        mView.btn_vip.setOnClickListener {
            val intent =Intent(activity,VipActivity::class.java)
            startActivity(intent)
        }
        mView.btn_my_vip_privileges.setOnClickListener {
            val intent = Intent(activity,MyVipActivity::class.java)
            startActivity(intent)

        }
    }

    private fun initFragment() {
//        getDataUser()
        mMultiStyleItemList.clear()
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"我的发布",false))
        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener=View.OnClickListener {
            val intent = Intent(activity,MyReleaseActivity::class.java)
            startActivity(intent)
        }
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"分销",false))
        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener=View.OnClickListener {
            val intent = Intent(activity,RetailStoreActivity::class.java)
            startActivity(intent)
        }
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
}