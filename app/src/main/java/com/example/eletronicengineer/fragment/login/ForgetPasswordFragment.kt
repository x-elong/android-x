package com.example.eletronicengineer.fragment.login

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.LoginActivity
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.putSimpleMessage
import com.example.eletronicengineer.utils.sendMobile
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_forget_password.view.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ForgetPasswordFragment: Fragment()  {
    private var mDisposable: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_forget_password,container,false)
        initFragment(view)
        return view
    }

    private fun initFragment(v: View) {
        v.tv_forget_password_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        v.tv_forget_password_get_check.setOnClickListener {
            //获取手机号
            val phoneNum = v.et_forget_password_number.text.toString().trim()
            if (phoneNum.isBlank()) {
                val toast = Toast.makeText(context, "手机号不能为空！", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            } else if (phoneNum.length != 11) {
                val toast = Toast.makeText(context, "请输入正确的手机号码！", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            } else {
                if (v.tv_forget_password_get_check.text == "重新获取验证码" || v.tv_forget_password_get_check.text == "获取验证码") {
                    val result = sendMobile(phoneNum, UnSerializeDataBase.upmsBasePath).subscribeOn(
                        Schedulers.io()
                    ).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        {
                            if (it.desc == "验证码发送成功") {
                                ToastHelper.mToast(v.context, "验证码发送成功")
                            } else
                                ToastHelper.mToast(v.context, "验证码发送失败")
                        }, {
                            ToastHelper.mToast(v.context, "验证码发送异常")
                            it.printStackTrace()
                        })
                    // 倒计时 60s
                    mDisposable = Flowable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext({ aLong -> v.tv_forget_password_get_check.setText("倒计时 " + (59 - aLong!!).toString() + " 秒") })
                        .doOnComplete({ v.tv_forget_password_get_check.setText("重新获取验证码") })
                        .subscribe()
                }
            }
        }
        v.tv_forget_password_next.setOnClickListener {
            val phoneNum = v.et_forget_password_number.text.toString().trim()
            val registerPassword = v.et_forget_password.text.toString().trim()
            var result = ""
            if (phoneNum.isBlank()) {
                result = "手机号不能为空！"
            } else if (phoneNum.length != 11) {
                result = "请输入正确的手机号码！"
            } else if (registerPassword.isBlank()) {
                result = "验证码不能为空！"
            } else {
                val loadingDialog = LoadingDialog(v.context,"正在验证...")
                loadingDialog.show()
                val result = validFindCode(phoneNum, registerPassword)
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            loadingDialog.dismiss()
                            val json = JSONObject(it.string())
                            if (json.getInt("code") == 200) {
                                val bundle = Bundle()
                                bundle.putString("phoneNumber", phoneNum)
                                FragmentHelper.switchFragment(
                                    activity!!,
                                    ForgetPasswordThreeFragment.newInstance(bundle),
                                    R.id.frame_login,
                                    ""
                                )
                            }else{
                                ToastHelper.mToast(v.context,"验证码错误,请输入正确的验证码")
                            }
                        },
                        {
                            loadingDialog.dismiss()
                            ToastHelper.mToast(v.context,"验证异常")
                            it.printStackTrace()
                        }
                    )
            }
            if (result != "")
                ToastHelper.mToast(v.context, result)
        }
    }
}