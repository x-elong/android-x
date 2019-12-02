package com.example.eletronicengineer.fragment.my

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import kotlinx.android.synthetic.main.fragment_vip_privileges_more.view.*

class VipPrivilegesMoreFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):VipPrivilegesMoreFragment{
            val vipPrivilegesMoreFragment = VipPrivilegesMoreFragment()
            vipPrivilegesMoreFragment.arguments = args
            return vipPrivilegesMoreFragment
        }
    }
    lateinit var mView: View
    var type = 0
    var price = 0.00
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_vip_privileges_more,container,false)
        type = arguments!!.getInt("type")
        return mView
    }

    override fun onStart() {
        super.onStart()
        initFragment()
    }
    private fun initFragment() {
        mView.tv_vip_privileges_more_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        Log.i("userVipLevel ",UnSerializeDataBase.userVipLevel.toString())
        if(UnSerializeDataBase.userVipLevel<type){
            mView.btn_subscribe.visibility = View.VISIBLE
            mView.vip_state.text = "未开通"
        }
        when(type){
            0->{

            }
            1->{
                price = 50.00
                mView.tv_vip_name.text = "精英"
                mView.view_vip_information.setBackgroundColor(ContextCompat.getColor(mView.context,R.color.vip2))
                mView.tv_vip_logo.setBackgroundResource(R.drawable.vip2_logo)
            }
            2->{
                price = 100.00
                mView.tv_vip_name.text = "黄金"
                mView.view_vip_information.setBackgroundColor(ContextCompat.getColor(mView.context,R.color.vip3))
                mView.tv_vip_logo.setBackgroundResource(R.drawable.vip3_logo)
            }
        }
        if(price!=0.toDouble())
            mView.tv_vip_price.text = "${price}元/年"
        val sp = SpannableStringBuilder(mView.tv_explain.text)
        sp.setSpan(ForegroundColorSpan(Color.GREEN)
            ,3
            ,9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        sp.setSpan(ForegroundColorSpan(Color.YELLOW)
            ,9
            ,21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        sp.setSpan(ForegroundColorSpan(Color.RED)
            ,21
            ,26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mView.tv_explain.text=sp
        mView.btn_subscribe.setOnClickListener {
            val productId = when(type){
                1->Constants.Goods.ELITE_VIP
                2->Constants.Goods.GOLD_VIP
                else->"0"
            }
            val bundle = Bundle()
            bundle.putDouble("paymentAmount",price)
            bundle.putString("productId",productId)
            FragmentHelper.switchFragment(activity!!,PaymentFragment.newInstance(bundle),R.id.frame_vip,"payment")
        }
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = mView.context
        adapterGenerate.activity = activity as AppCompatActivity
        mView.rv_vip_privileges_content.adapter = adapterGenerate.vipPrivilegesInformation(type)
        mView.rv_vip_privileges_content.layoutManager = LinearLayoutManager(context)

    }
}