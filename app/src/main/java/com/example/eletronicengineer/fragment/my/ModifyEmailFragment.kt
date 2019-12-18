package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.sendMobile
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_modify_email.view.*
import kotlinx.android.synthetic.main.fragment_modify_phone.view.*
import kotlinx.android.synthetic.main.fragment_modify_phone.view.tv_get_old_check
import java.util.concurrent.TimeUnit

class ModifyEmailFragment :Fragment(){
    companion object{
        fun newInstance(args: Bundle):ModifyEmailFragment{
            val modifyPhoneFragment = ModifyEmailFragment()
            modifyPhoneFragment.arguments = args
            return modifyPhoneFragment
        }
    }
    private var mDisposable: Disposable? = null
    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_modify_email,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_modify_email_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.tv_get_old_check.setOnClickListener {
                if(mView.tv_get_old_check.text=="重新获取验证码" || mView.tv_get_old_check.text=="获取验证码"){
                    val result= sendMobile(UnSerializeDataBase.userPhone,UnSerializeDataBase.upmsBasePath).subscribeOn(Schedulers.io()).observeOn(
                        AndroidSchedulers.mainThread()).subscribe(
                        {
                            if(it.desc=="验证码发送成功")
                            {
                                val toast = Toast.makeText(context,"验证码发送成功", Toast.LENGTH_SHORT)
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
                        .doOnNext({ aLong-> mView.tv_get_old_check.setText("倒计时 " + (59 - aLong!!).toString() + " 秒") })
                        .doOnComplete({ mView.tv_get_old_check.setText("重新获取验证码")   })
                        .subscribe()
                }
        }
    }
}