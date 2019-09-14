package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.fragment_payment.view.*

class PaymentFragment :Fragment(){
    companion object{
        fun newInstance(args: Bundle):PaymentFragment{
            val paymentFragment = PaymentFragment()
            paymentFragment.arguments = args
            return paymentFragment
        }
    }
    private var paymentAmount = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_payment,container,false)
        paymentAmount = arguments!!.getInt("paymentAmount")
        view.tv_payment_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        view.radio_btn_we_chat_payment.setOnClickListener {
            view.radio_btn_ali_pay_payment.isChecked = false
        }
        view.radio_btn_ali_pay_payment.setOnClickListener {
            view.radio_btn_we_chat_payment.isChecked = false
        }
        view.btn_confirmed_pay.setOnClickListener {

        }
        return view
    }
}