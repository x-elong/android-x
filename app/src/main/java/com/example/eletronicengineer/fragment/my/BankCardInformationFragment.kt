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
import com.example.eletronicengineer.utils.getUser
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_bank_card_information.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat

class BankCardInformationFragment :Fragment(){
    lateinit var mView: View
    lateinit var bankCards: JSONArray
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
        val result = getUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val data = adapter.mData.toMutableList()
                val bankCards = it.message.bankCards
                val jsonArray = JSONObject(Gson().toJson(it.message)).getJSONArray("bankCards")
                if(bankCards!=null)
                    for (j in bankCards){
                        val item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"银行卡",j.bankType,j.bankCardNumber)
                        item.jumpListener = View.OnClickListener {
                            val bundle = Bundle()
                            bundle.putString("bankCard",jsonArray.getJSONObject(bankCards.indexOf(j)).toString())
                            (activity as MyInformationActivity).switchFragment(BankCardMoreFragment.newInstance(bundle),"")
                        }
                        data.add(item)
                    }
                adapter.mData = data
                adapter.notifyDataSetChanged()
            },{
                it.printStackTrace()
            })
        adapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_bank_card_information_content.adapter = adapter
        mView.rv_bank_card_information_content.layoutManager = LinearLayoutManager(context)
    }
}