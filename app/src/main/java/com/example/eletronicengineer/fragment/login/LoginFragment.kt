package com.example.eletronicengineer.fragment.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MainActivity
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.sendLogin
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class LoginFragment: Fragment() {

    private var username = ""
    private var password = ""
    private lateinit var pref: SharedPreferences
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        mView = inflater.inflate(R.layout.fragment_login, container, false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        pref = PreferenceManager.getDefaultSharedPreferences(context)
        username = pref.getString("username","")
        password = pref.getString("password","")
        mView.et_login_name.setText(username)
        mView.et_login_password.setText(password)
        mView.tv_login_back.setOnClickListener {
            activity!!.finish()
        }
        mView.tv_login_confirm.setOnClickListener {
            username = mView.et_login_name.text.toString()
            password = mView.et_login_password.text.toString()
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
        mView.tv_login_register.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,PhoneRegisterFragment(),R.id.frame_login,"")
        }
        mView.tv_forget_password.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,ForgetPasswordFragment(),R.id.frame_login,"")
        }
        mView.tv_login_problem.setOnClickListener {
            FragmentHelper.switchFragment(activity!!,ProblemFragment(),R.id.frame_login,"")
        }
        mView.cb_pwd_display.setOnClickListener {
            Log.i("","${mView.cb_pwd_display.isChecked}")
            if(mView.cb_pwd_display.isChecked){
                mView.et_login_password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                mView.cb_pwd_display.background = mView.context.getDrawable(R.drawable.eys_open)
            }
            else{
                mView.et_login_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                mView.cb_pwd_display.background = mView.context.getDrawable(R.drawable.eys_close)
            }
            mView.et_login_password.setSelection(mView.et_login_password.length())
        }
//        if(username!="" && password!="")
//            v.tv_login_confirm.callOnClick()

    }
    fun sendLoginForHttp(key:ArrayList<String>,value:ArrayList<String>) {
        val loadingDialog = LoadingDialog(mView.context,"正在登录...")
        loadingDialog.show()
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
                val result = sendLogin(it,UnSerializeDataBase.upmsBasePath).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        loadingDialog.dismiss()
                        Log.i("111hy",it.code)
                        if(it.code=="200")
                        {
                            Log.i("cookie",UnSerializeDataBase.cookie)
                            UnSerializeDataBase.userToken = it.message.token
                            UnSerializeDataBase.userName = it.message.user.userName
                            UnSerializeDataBase.userPhone = it.message.user.phone
                            if(it.message.user.name!=null)
                                UnSerializeDataBase.idCardName = it.message.user.name!!
                            Log.i("token is ",it.message.token)
                            val editor = pref.edit()
                            editor.putString("username",username)
                            editor.putString("password",password)
                            editor.apply()
                            ToastHelper.mToast(mView.context,"登录成功")
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)

                        }else{
                            ToastHelper.mToast(mView.context,"登录失败, 请输入正确的用户名和密码")
                        }
                    },
                    {
                        loadingDialog.dismiss()
                        ToastHelper.mToast(mView.context,"登录异常")
                        it.printStackTrace()
                    }
                )
            }

    }
}