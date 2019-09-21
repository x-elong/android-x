package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyInformationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import kotlinx.android.synthetic.main.fragment_bank_card_information.view.*

class BankCardInformationFragment :Fragment(){
    lateinit var mView: View
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    var adapter = RecyclerviewAdapter(ArrayList())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_bank_card_information,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mMultiStyleItemList.clear()
        var item = MultiStyleItem(MultiStyleItem.Options.TITLE,"银行卡信息","2")
        mMultiStyleItemList.add(item)
        item.backListener = View.OnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        item.tvSelect = View.OnClickListener {
            (activity as MyInformationActivity).switchFragment(AddBankCardFragment(),"AddBankCard")
        }
        adapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_bank_card_information_content.adapter = adapter
        mView.rv_bank_card_information_content.layoutManager = LinearLayoutManager(context)
    }
}