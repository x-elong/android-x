package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_change_password.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class ChangePasswordFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_change_password,container,false)
        view.tv_change_password_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        view.btn_change_password.setOnClickListener {
            val result = Observable.create<RequestBody> {
                val json = JSONObject().put("oldPassword",view.et_old_password.text)
                    .put("newPassword",view.et_new_password.text)
                    .put("renewPassword",view.et_confirm_new_password.text)
                val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
                it.onNext(requestBody)
            }.subscribe {
                val result = startSendMessage(it,"http://10.1.5.141:8026"+ Constants.HttpUrlPath.My.updatePassword)
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                        val jsonObject = JSONObject(it.string())
                        val code = jsonObject.getInt("code")
                        var result = ""
                        if(code==200){
                            result = "修改成功"
                            activity!!.supportFragmentManager.popBackStackImmediate()
                        }
                        else
                            result = "修改失败"
                        val toast = Toast.makeText(context,result, Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER,0,0)
                        toast.show()
                    },{
                        it.printStackTrace()
                    })
            }
        }
        return view
    }
}