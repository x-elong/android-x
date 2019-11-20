package com.example.eletronicengineer.fragment.login

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import com.amap.api.col.sl2.it
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.LoginActivity
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.sendMobile
import com.example.eletronicengineer.utils.sendRegister
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlinx.android.synthetic.main.fragment_register.view.et_set_password_again
import kotlinx.android.synthetic.main.fragment_register.view.et_set_password_number
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class   PhoneRegisterFragment: Fragment()  {

    private var mDisposable: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register,container,false)
        initFragment(view)
        return view
    }

    private fun initFragment(v: View) {
        v.tv_register_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        v.tv_get_check.setOnClickListener {
            //获取手机号
            var phoneNum = v.et_register_number.text.toString().trim()
            if(phoneNum.isBlank()){
                val toast = Toast.makeText(context,"手机号不能为空！",Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER,0,0)
                toast.show()
            }else if(phoneNum.length!=11){
                val toast = Toast.makeText(context,"请输入正确的手机号码！",Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER,0,0)
                toast.show()
            }
            else{
                if(v.tv_get_check.text=="重新获取验证码" || v.tv_get_check.text=="获取验证码"){
                    val result= sendMobile(phoneNum, UnSerializeDataBase.upmsBasePath).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        {
                            if(it.desc=="验证码发送成功")
                            {
                                val toast = Toast.makeText(context,"验证码发送成功",Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER,0,0)
                                toast.show()
                            }
                        },{
                            it.printStackTrace()
                        })
                    // 倒计时 60s
                    mDisposable = Flowable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext({ aLong-> v.tv_get_check.setText("倒计时 " + (59 - aLong!!).toString() + " 秒") })
                        .doOnComplete({ v.tv_get_check.setText("重新获取验证码")   })
                        .subscribe()
                }
            }
        }

        v.tv_register_confirm.setOnClickListener {
            val phoneNum = v.et_register_number.text.toString().trim()
            val registerPassword = v.et_register_password.text.toString().trim()
            val password:String = v.et_set_password_number.text.toString().trim()
            val rePassword:String = v.et_set_password_again.text.toString().trim()
            var result = ""
            if(phoneNum.isBlank()){
                result="手机号不能为空！"
            }else if(phoneNum.length!=11){
                result="请输入正确的手机号码！"
            }else if(registerPassword.isBlank()){
                result="验证码不能为空！"
            }
            else if(password.length < 6){
                result="${password},密码至少6位"
            }
            else if(!ispsd(password)){
                result="密码必须由字母+数字组成"
            }
            else if(password!=rePassword){
                result="两次输入的密码不相同"
            }
            else{
                JMessageClient.register(phoneNum,password,object:BasicCallback(){
                    override fun gotResult(p0: Int, p1: String?) {
                        Log.i("registerMsg",p1)
                    }
                })
                val key= arrayListOf("username","code","password","repassword")
                val value= arrayListOf(phoneNum,registerPassword,password,rePassword)
                sendRegisterForHttp(key,value,UnSerializeDataBase.upmsBasePath)
            }
            if(result!=""){
                val toast = Toast.makeText(context,result,Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER,0,0)
                toast.show()
            }
        }

        v.tv_register_login.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,LoginFragment(),R.id.frame_login,"")
        }
        v.tv_register_forget_password.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,ForgetPasswordFragment(),R.id.frame_login,"")
        }
        v.tv_register_problem.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,ProblemFragment(),R.id.frame_login,"")
        }
    }

    //字母加数字返回true
    fun ispsd(psd: String): Boolean {
        val p = Pattern.compile("^[A-Za-z].*[0-9]|[0-9].*[A-Za-z]")
        val m = p.matcher(psd)

        return m.matches()
    }

    fun sendRegisterForHttp(key:ArrayList<String>,value:ArrayList<String>,baseUrl:String) {
        val result= Observable.create<RequestBody> {
            //建立网络请求体 (类型，内容)
            val jsonObject = JSONObject()
            for (i in 0 until key.size) {
                jsonObject.put(key[i], value[i])
            }
            val requestBody= RequestBody.create(MediaType.parse("application/json"),jsonObject.toString())
            it.onNext(requestBody)
        }
            .subscribe {
                val result = sendRegister(it,baseUrl).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        if(it.desc=="注册成功")
                        {
                            Log.d("111hy","注册成功！")
                            val toast = Toast.makeText(context,"注册成功",Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.CENTER,0,0)
                            toast.show()
                            FragmentHelper.switchFragment(activity!!,LoginFragment(),R.id.frame_login,"")
                        }
                        else if(it.desc=="用户名称已存在"){
                            val toast = Toast.makeText(context,"用户名称已存在,请重新输入手机号！",Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.CENTER,0,0)
                            toast.show()
                        }else{
                            val toast = Toast.makeText(context,"验证失败, 请输入正确的手机号并重新获取验证码",Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.CENTER,0,0)
                            toast.show()
                        }
                    },
                    {
                        val toast = Toast.makeText(context,"连接超时",Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER,0,0)
                        toast.show()
                        toast.setGravity(Gravity.CENTER,0,0)
                        toast.show()
                        it.printStackTrace()
                    }
                )
            }

    }
}