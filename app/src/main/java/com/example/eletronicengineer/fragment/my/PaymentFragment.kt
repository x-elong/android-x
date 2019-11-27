package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.utils.PaymentHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import kotlinx.android.synthetic.main.fragment_payment.view.*

class PaymentFragment :Fragment(){
    companion object{
        fun newInstance(args: Bundle):PaymentFragment{
            val paymentFragment = PaymentFragment()
            paymentFragment.arguments = args
            return paymentFragment
        }
    }
    val networkAdapter = NetworkAdapter()
    private var paymentAmount = 0.00
    private var productId = ""
    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_payment,container,false)
        paymentAmount = arguments!!.getDouble("paymentAmount")
        productId = arguments!!.getString("productId")
        initFragment()
        return mView
    }

    override fun onStart() {
        super.onStart()
        if(UnSerializeDataBase.vipOpenState==1)
            activity!!.supportFragmentManager.popBackStack()
        UnSerializeDataBase.vipOpenState = -1
    }
    fun initFragment(){
        networkAdapter.context = activity!!
        mView.tv_payment_amount.text = "合计\n¥${paymentAmount}"
        mView.tv_payment_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.radio_btn_we_chat_payment.setOnClickListener {
            mView.radio_btn_ali_pay_payment.isChecked = false
            mView.radio_btn_num_payment.isChecked = false
        }
        mView.radio_btn_ali_pay_payment.setOnClickListener {
            mView.radio_btn_we_chat_payment.isChecked = false
            mView.radio_btn_num_payment.isChecked = false
        }
        mView.radio_btn_num_payment.setOnClickListener {
            mView.radio_btn_ali_pay_payment.isChecked = false
            mView.radio_btn_we_chat_payment.isChecked = false
        }
        mView.btn_confirmed_pay.setOnClickListener {
            UnSerializeDataBase.vipOpenState = 0
            if(mView.radio_btn_we_chat_payment.isChecked)
                networkAdapter.creatDataOrder(productId)
            else if(mView.radio_btn_ali_pay_payment.isChecked)
                networkAdapter.getAliPayOrder(productId)
            else
                networkAdapter.numPay(productId)
        }
    }
}