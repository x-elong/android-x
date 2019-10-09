package com.example.eletronicengineer.fragment.my

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.startSendMessage
import io.card.payment.CardIOActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_bank_card.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class AddBankCardFragment :Fragment(){
    lateinit var mView: View
    var editTextContent = ""
    var space = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_bank_card,container,false)
        initFragment()
        return mView

    }

    private fun initFragment() {
        mView.tv_add_bank_card_back.setOnClickListener{
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.btn_add_bank_card.setOnClickListener {
            val result = Observable.create<RequestBody>{
                val jsonObject = JSONObject().put("bankCardNumber",editTextContent.replace(" ",""))
                    .put("bankType",mView.sp_bank.selectedItem.toString())
                val requestBody = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString())
                it.onNext(requestBody)
            }.subscribe {
                val result = startSendMessage(it,
                    UnSerializeDataBase.BasePath+Constants.HttpUrlPath.My.insertBankCard).subscribeOn(
                    Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if(JSONObject(it.string()).getInt("code")==200){
                            val toast = Toast.makeText(context,"添加成功",Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.CENTER,0,0)
                            toast.show()
                            mView.tv_add_bank_card_back.callOnClick()
                        }else{
                            val toast = Toast.makeText(context,"添加失败",Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.CENTER,0,0)
                            toast.show()
                        }
                    },{
                        it.printStackTrace()
                    })
            }
        }
        mView.et_bank_card_account.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if((p0!!.length-space)%4==0){
                    editTextContent+=" "
                    mView.et_bank_card_account.setText(editTextContent)
                    mView.et_bank_card_account.setSelection(editTextContent.length)
                    space++
                }
                Log.i("size size","${editTextContent}:${editTextContent.length} ${p0}:${p0.toString().length}")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editTextContent+=(p0.toString().substring(editTextContent.length))
                Log.i("size size","${editTextContent}:${editTextContent.length} ${p0.toString()}:${p0.toString().length}")
            }

        })
//        mView.tv_add_bank_card_camera.setOnClickListener {
//            val intent = Intent(activity, CardIOActivity::class.java)
//            intent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true) // default: false
//            intent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false) // default: false
//            intent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false) // default: false
//            intent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, false)
//            intent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, true)
//            intent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true)
//            activity!!.startActivityForResult(intent, Constants.RequestCode.MY_SCAN_REQUEST_CODE.ordinal)
//        }
        val mAdapter = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, arrayListOf("中国银行","工商银行"))
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mView.sp_bank.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        mView.sp_bank.adapter = mAdapter
        mView.sp_bank.setSelection(0)
    }
    fun setBankCardAccount(Account:String){
        mView.et_bank_card_account.setText(Account)
    }
}