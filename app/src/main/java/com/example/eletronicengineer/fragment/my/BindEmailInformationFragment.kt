package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.sendEmailCode
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_bind_email_information.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class BindEmailInformationFragment :Fragment(){
    lateinit var title:String
    private var mDisposable: Disposable? = null
    companion object{
        fun newInstance(args:Bundle):BindEmailInformationFragment{
            val bindEmailInformationFragment = BindEmailInformationFragment()
            bindEmailInformationFragment.arguments = args
            return bindEmailInformationFragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bind_email_information,container,false)
        title = arguments!!.getString("title")
        view.tv_bind_email_information_title.setText(title)
        view.tv_forget_password_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        view.tv_email_code_check.setOnClickListener {
            val receiver = view.et_bind_email_information_number.text.toString().trim()
            if (receiver.isBlank()) {
                ToastHelper.mToast(view.context,"邮箱不能为空!")
            }else {
                if (view.tv_email_code_check.text == "重新获取验证码" || view.tv_email_code_check.text == "获取验证码") {
                    val loadingDialog = LoadingDialog(view.context,"正在获取验证码...")
                    loadingDialog.show()
                        val result = sendEmailCode(
                            receiver
                        ).subscribeOn(
                            Schedulers.io()
                        ).observeOn(AndroidSchedulers.mainThread()).subscribe(
                            {
                                loadingDialog.dismiss()
                                val json = JSONObject(it.string())
                                val message = json.getString("message")
                                if(json.getInt("code")==200){
                                    // 倒计时 60s
                                    mDisposable = Flowable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnNext({ aLong -> view.tv_email_code_check.setText("倒计时 " + (59 - aLong!!).toString() + " 秒") })
                                        .doOnComplete({ view.tv_email_code_check.setText("重新获取验证码") })
                                        .subscribe()
                                }
                                ToastHelper.mToast(view.context, message)
                            }, {
                                loadingDialog.dismiss()
                                ToastHelper.mToast(view.context, "验证码发送异常")
                                it.printStackTrace()
                            })
                    }
            }
        }
        view.tv_bind_email_submit.setOnClickListener {
            val emailCode = view.et_email_code.text.toString().trim()
            val email = view.et_bind_email_information_number.text.toString().trim()
            if(email=="")
                ToastHelper.mToast(view.context, "邮箱不能为空!")
            else if(emailCode=="")
                ToastHelper.mToast(view.context, "验证码不能为空!")
            else{
                val loadingDialog = LoadingDialog(view.context,"正在提交...")
                loadingDialog.show()
                    val result = validBindEmail(
                        email,emailCode
                    ).subscribeOn(
                        Schedulers.io()
                    ).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        {
                            loadingDialog.dismiss()
                            val json = JSONObject(it.string())
                            val message = json.getString("message")
                            if(json.getInt("code")==200){
                                val fragment = activity!!.supportFragmentManager.findFragmentByTag("bindEmail") as BindEmailFragment
                                fragment.update(email)
                                activity!!.supportFragmentManager.popBackStackImmediate()
                            }
                            ToastHelper.mToast(view.context, message)
                        }, {
                            loadingDialog.dismiss()
                            ToastHelper.mToast(view.context, "${title}异常")
                            it.printStackTrace()
                        })
                }
        }
        return view
    }
}