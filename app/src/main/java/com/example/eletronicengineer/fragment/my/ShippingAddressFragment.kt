package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.PersonInformationActivity
import com.example.eletronicengineer.activity.SearchActivity
import com.example.eletronicengineer.activity.ShippingAddressActivity
import com.example.eletronicengineer.adapter.ShippingAddressAdapter
import com.example.eletronicengineer.aninterface.ShippingAddress
import com.example.eletronicengineer.fragment.shopping.AddressFragment
import com.example.eletronicengineer.fragment.shopping.OrderConfirmFragment
import kotlinx.android.synthetic.main.fragment_reseive_addr.view.*
import kotlinx.android.synthetic.main.fragment_shipping_address.*
import kotlinx.android.synthetic.main.fragment_shipping_address.view.*

class ShippingAddressFragment :Fragment(){

    var adapter:ShippingAddressAdapter = ShippingAddressAdapter(ArrayList())

    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView=inflater.inflate(R.layout.fragment_shipping_address,container,false)
        initFragment()
        //设置布局管理器
        mView.rv_shipping_address_content.layoutManager = LinearLayoutManager(context)
        //设置适配器
        mView.rv_shipping_address_content.adapter = adapter
        return mView
    }

    private fun initData() {
        val mShippingAddressList:MutableList<ShippingAddress> = ArrayList()
        for (j in 0 until 6){
            val shippingAddress = ShippingAddress("Gavin","13574141625","湖南省娄底市娄星区|大科街道湖南人文科技学院",View.OnClickListener {
                val data = Bundle()
                data.putInt("position",j+1)
                data.putString("title", "编辑收货地址")
                if(activity is ShippingAddressActivity)
                    (activity as ShippingAddressActivity).switchFragment(AddressFragment.newInstance(data),"editAddress")
                else
                    (activity as SearchActivity).switchFragment(AddressFragment.newInstance(data),"editAddress")
            })
            if(activity is SearchActivity){
                shippingAddress.shippingAddressItemListener =View.OnClickListener {
                    (activity!!.supportFragmentManager.findFragmentByTag("orderConfirm") as OrderConfirmFragment).motifySelectedAddress(shippingAddress)
                    activity!!.supportFragmentManager.popBackStackImmediate()
                }
            }
            mShippingAddressList.add(shippingAddress)
        }
        adapter = ShippingAddressAdapter(mShippingAddressList)
    }

    private fun initFragment() {
        mView.tv_shipping_address_back.setOnClickListener {
            if(activity is SearchActivity)
                activity!!.supportFragmentManager.popBackStackImmediate()
            else
                activity!!.finish()
        }
        if(adapter.mShippingAddressList.size==0)
            initData()
        mView.btn_new_shipping_address.setOnClickListener {
            val data = Bundle()
            data.putString("title", "添加收货地址")
            if(activity is ShippingAddressActivity)
                (activity as ShippingAddressActivity).switchFragment(AddressFragment.newInstance(data),"addAddress")
            else
                (activity as SearchActivity).switchFragment(AddressFragment.newInstance(data),"editAddress")
        }
    }
    fun modify(position:Int,name:String,phone:String,address:String){
        var result = ""
        if(position==0){
            val mShippingAddressList = adapter.mShippingAddressList.toMutableList()
            val item = ShippingAddress(name,phone,address,View.OnClickListener {
                val data = Bundle()
                data.putInt("position",mShippingAddressList.size+1)
                data.putString("title", "编辑收货地址")
                if(activity is ShippingAddressActivity)
                    (activity as ShippingAddressActivity).switchFragment(AddressFragment.newInstance(data),"editAddress")
                else
                    (activity as SearchActivity).switchFragment(AddressFragment.newInstance(data),"editAddress")
            })
            if(activity is SearchActivity){
                item.shippingAddressItemListener =View.OnClickListener {
                    (activity!!.supportFragmentManager.findFragmentByTag("orderConfirm") as OrderConfirmFragment).motifySelectedAddress(item)
                    activity!!.supportFragmentManager.popBackStackImmediate()
                }
            }
            mShippingAddressList.add(item)
            adapter.mShippingAddressList = mShippingAddressList
            adapter.notifyItemInserted(mShippingAddressList.size-1)
            result="添加成功"
        }
        else{
            for ( j in 0 until adapter.mShippingAddressList.size){
                if(j==position-1){
                    adapter.mShippingAddressList[j].shippingAddressPeople=name
                    adapter.mShippingAddressList[j].shippingAddressPhone=phone
                    adapter.mShippingAddressList[j].shippingAddress=address
                    break
//                    val mShippingAddress = adapter.mShippingAddressList[j]
//                    mShippingAddress
                }
            }
            adapter.notifyItemChanged(position-1)
            result="修改成功"
        }
        val toast = Toast.makeText(context!!,result,Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }

}