package com.example.eletronicengineer.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.fragment.my.VipPrivilegesFragment
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import interfaces.heweather.com.interfacesmodule.view.HeContext.context
import kotlinx.android.synthetic.main.activity_my_vip.*

class MyVipActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vip)
        when(UnSerializeDataBase.userVipLevel){
            1->{
                tv_vip_name.text = "精英"
                tv_vip_price.text = "50.00元/年"

                view_vip_information.setBackgroundColor(ContextCompat.getColor(this,R.color.vip2))
                tv_vip_logo.setBackgroundResource(R.drawable.vip2_logo)
            }
            2->{
                tv_vip_name.text = "黄金"
                tv_vip_price.text = "100.00元/年"
                view_vip_information.setBackgroundColor(ContextCompat.getColor(this,R.color.vip3))
                tv_vip_logo.setBackgroundResource(R.drawable.vip3_logo)
            }
        }
        val sp = SpannableStringBuilder(tv_explain.text)
        sp.setSpan(ForegroundColorSpan(Color.GREEN)
            ,3
            ,9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        sp.setSpan(ForegroundColorSpan(Color.YELLOW)
            ,9
            ,21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        sp.setSpan(ForegroundColorSpan(Color.RED)
            ,21
            ,26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_explain.text=sp
        tv_my_vip_privileges_information_back.setOnClickListener {
            finish()
        }
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = this
        adapterGenerate.activity = this
        rv_vip_privileges_content.adapter = adapterGenerate.vipPrivilegesInformation(UnSerializeDataBase.userVipLevel)
        rv_vip_privileges_content.layoutManager = LinearLayoutManager(context)
    }
}