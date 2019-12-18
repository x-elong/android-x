package com.example.eletronicengineer.fragment.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.LoginActivity
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.ToastHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_forget_password_three.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.regex.Pattern

class ForgetPasswordThreeFragment : Fragment()  {
    companion object{
        fun newInstance(args:Bundle):ForgetPasswordThreeFragment{
            val forgetPasswordThreeFragment = ForgetPasswordThreeFragment()
            forgetPasswordThreeFragment.arguments = args
            return forgetPasswordThreeFragment
        }
    }
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_forget_password_three,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_forget_password_three_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        val phoneNumber = arguments!!.getString("phoneNumber")
        mView.tv_forget_password_three_next.setOnClickListener {
            val password:String = mView.et_forget_password_three.text.toString().trim()
            val rePassword:String = mView.et_forget_password_three_again.text.toString().trim()
            var result = ""
            if(password.length < 6){
                result="${password},密码至少6位"
            }
            else if(!ispsd(password)){
                result="密码必须由字母+数字组成"
            }
            else if(password!=rePassword){
                result="两次输入的密码不相同"
            }else{
                val result = Observable.create<RequestBody> {
                    val json = JSONObject().put("username",phoneNumber).put("phoneNumber",phoneNumber)
                        .put("password",password).put("repassword",rePassword)
                        val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
                    it.onNext(requestBody)
                }.subscribe {
                    val result = startSendMessage(it,UnSerializeDataBase.upmsBasePath + Constants.HttpUrlPath.Login.findPassword)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                            val json = JSONObject(it.string())
                            Log.i("json",json.toString())
                            if(json.getInt("code")==200){
                                ToastHelper.mToast(mView.context,"重设密码成功")
                                activity!!.finish()
                                activity!!.startActivity(Intent(mView.context,LoginActivity::class.java))
                            }else
                                ToastHelper.mToast(mView.context,"重设密码失败")
                        },{
                            ToastHelper.mToast(mView.context,"重设密码异常")
                            it.printStackTrace()
                        })
                }
            }
            if(result!="")
                ToastHelper.mToast(mView.context,result)
        }


    }
    //字母加数字返回true
    fun ispsd(psd: String): Boolean {
        val p = Pattern.compile("^[A-Za-z].*[0-9]|[0-9].*[A-Za-z]")
        val m = p.matcher(psd)
        return m.matches()
    }
}