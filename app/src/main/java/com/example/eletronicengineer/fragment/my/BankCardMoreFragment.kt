package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.deleteBankCard
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_bank_card_more.view.*
import org.json.JSONObject

class BankCardMoreFragment :Fragment(){

    companion object{
        fun newInstance(args:Bundle):BankCardMoreFragment{
            val bankCardMoreFragment = BankCardMoreFragment()
            bankCardMoreFragment.arguments = args
            return bankCardMoreFragment
        }
    }
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_bank_card_more,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_bank_card_more_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        val bankCard = JSONObject(arguments!!.getString("bankCard"))
        mView.tv_bank_name.text = bankCard.getString("bankType")
        mView.tv_bank_number.text = bankCard.getString("bankCardNumber")
        mView.btn_delete_bank_card.setOnClickListener {
            val result = deleteBankCard(bankCard.getString("id")).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe( {
                    if(JSONObject(it.string()).getInt("code")==200){
                        val toast = Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER,0,0)
                        toast.show()
                        mView.tv_bank_card_more_back.callOnClick()
                    }else{
                        val toast = Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER,0,0)
                        toast.show()
                    }
                },{
                    it.printStackTrace()
                })
        }
    }
}