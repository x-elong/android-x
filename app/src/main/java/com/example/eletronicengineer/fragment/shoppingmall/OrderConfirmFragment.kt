package com.example.eletronicengineer.fragment.shopping

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.SearchActivity
import com.example.eletronicengineer.activity.ShippingAddressActivity
import com.example.eletronicengineer.aninterface.ShippingAddress
import com.example.eletronicengineer.fragment.my.ShippingAddressFragment
import com.example.eletronicengineer.fragment.shoppingmall.ReceiveAddrFragment
import kotlinx.android.synthetic.main.fragment_order_confirm.view.*

class OrderConfirmFragment : Fragment() {

    var item:ShippingAddress ?=null
    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_order_confirm, container, false)
        initFragment()
        if(item!=null){
            mView.tv_order_confirm_name.text = item!!.shippingAddressPeople
            mView.tv_order_confirm_phone.text = item!!.shippingAddressPhone
            mView.tv_order_confirm_address.text = item!!.shippingAddress
        }
        return mView
    }
    private fun initFragment() {
        mView.tv_order_confirm_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.tv_order_confirm_subtotal_money.text="${mView.et_order_confirm_number.text.toString().toInt()*mView.tv_unit_price.text.toString().toInt()}"
        mView.tv_order_confirm_pay_money.text="${mView.et_order_confirm_number.text.toString().toInt()*mView.tv_unit_price.text.toString().toInt()}"
        mView.view_address_information.setOnClickListener {
                 (activity as SearchActivity).switchFragment(ShippingAddressFragment(),"search")
        }
        mView.btn_order_confirm_reduce.setOnClickListener{
            val number= mView.et_order_confirm_number.text.toString().toInt()
            if(number==1){
                val toast = Toast.makeText(context,"最少购买数量不得小于1",Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER,0,0)
                toast.show()
            }else{
                mView.et_order_confirm_number.setText("${number-1}")
                mView.tv_order_confirm_subtotal_money.text="${(number-1)*mView.tv_unit_price.text.toString().toInt()}"
                mView.tv_order_confirm_pay_money.text="${(number-1)*mView.tv_unit_price.text.toString().toInt()}"
            }
        }
        mView.btn_order_confirm_add.setOnClickListener {
            val number= mView.et_order_confirm_number.text.toString().toInt()
            mView.et_order_confirm_number.setText("${number+1}")
            mView.tv_order_confirm_subtotal_money.text="${(number+1)*mView.tv_unit_price.text.toString().toInt()}"
            mView.tv_order_confirm_pay_money.text="${(number+1)*mView.tv_unit_price.text.toString().toInt()}"
        }
        try {
            mView.et_order_confirm_number.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(p0!=""){
                        mView.tv_order_confirm_subtotal_money.text = "${p0.toString().toInt()*mView.tv_unit_price.text.toString().toInt()}"
                        mView.tv_order_confirm_pay_money.text="${p0.toString().toInt()*mView.tv_unit_price.text.toString().toInt()}"
                    }else{
                        mView.tv_order_confirm_subtotal_money.text = ""
                        mView.tv_order_confirm_pay_money.text = ""
                    }
                }

            })
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun motifySelectedAddress(item: ShippingAddress){
        this.item = item
    }
}