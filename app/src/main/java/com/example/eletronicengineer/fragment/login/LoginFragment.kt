package com.example.eletronicengineer.fragment.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.LoginActivity
import com.example.eletronicengineer.activity.MainActivity
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.sendLogin
import com.example.eletronicengineer.utils.sendRegister
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class LoginFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        initFragment(view)
        return view
    }

    private fun initFragment(v: View) {
        v.tv_login_back.setOnClickListener {
            activity!!.finish()
        }
        v.tv_login_confirm.setOnClickListener {
            val username:String = v.et_login_name.text.toString()
            val password:String = v.et_login_password.text.toString()
            if(!username.isBlank() && !password.isBlank()){
                val key= arrayListOf("username","password")
//                val value= arrayListOf("13575232531","123456")
                val value= arrayListOf(username,password)
                sendLoginForHttp(key,value)
//                val intent = Intent(context, MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
            }
            else {
                Toast.makeText(context,"请输入登陆账号及密码",Toast.LENGTH_SHORT).show()
            }

        }
        v.tv_login_register.setOnClickListener {
            (activity as LoginActivity).switchFragment(PhoneRegisterFragment())
        }
        v.tv_forget_password.setOnClickListener {
            (activity as LoginActivity).switchFragment(ForgetPasswordFragment())
        }
        v.tv_login_problem.setOnClickListener {
            (activity as LoginActivity).switchFragment(ProblemFragment())
        }
    }
    fun sendLoginForHttp(key:ArrayList<String>,value:ArrayList<String>) {
        val result= Observable.create<RequestBody> {
            //建立网络请求体 (类型，内容)
            var jsonObject = JSONObject()
            for (i in 0 until key.size) {
                jsonObject.put(key[i], value[i])
            }
            val requestBody= RequestBody.create(MediaType.parse("application/json"),jsonObject.toString())
            it.onNext(requestBody)
        }
            .subscribe {
                val result = sendLogin(it,"http://192.168.1.132:8026").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        Log.i("111hy",it.code)
                        if(it.code=="200")
                        {
                            Log.i("cookie",UnSerializeDataBase.cookie)
                            UnSerializeDataBase.userToken = it.message.token
                            UnSerializeDataBase.userName = it.message.user.userName
                            UnSerializeDataBase.userPhone = it.message.user.phone
                            Log.i("token is ",it.message.token)

                            Toast.makeText(context,"登录成功",Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                        }else{
                            Toast.makeText(context,"登录失败, 请输入正确的用户名和密码",Toast.LENGTH_SHORT).show()
                        }
                    },
                    {
                        val toast = Toast.makeText(context,"登录异常",Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER,0,0)
                        toast.show()
                        it.printStackTrace()
                    }
                )
            }

    }
}