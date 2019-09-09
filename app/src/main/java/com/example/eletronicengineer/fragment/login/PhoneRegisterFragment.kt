package com.example.eletronicengineer.fragment.login

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
import com.kymjs.rxvolley.RxVolley
import com.kymjs.rxvolley.client.HttpCallback
import com.kymjs.rxvolley.http.VolleyError
import kotlinx.android.synthetic.main.fragment_register.view.*

class PhoneRegisterFragment: Fragment()  {
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
            var url = "http://v.juhe.cn/sms/send?mobile=${phoneNum}&tpl_id=181433&tpl_value=%23code%23%3D654654&key=eec6797008055e9ed67f3d34a3f073c1"
            //http://v.juhe.cn/sms/send?mobile=手机号码&tpl_id=短信模板ID&tpl_value=%23code%23%3D654654&key=
            if(!phoneNum.isBlank()){
                RxVolley.get(url, object: HttpCallback() {		//url为要请求的网址
                    //成功返回json数据--onSuccess为重写方法
                    override fun onSuccess(t:String ) {
                        Log.d("llhData",t)
                    }

                    override fun onFailure(error: VolleyError?) {
                        val toast = Toast.makeText(context,"请求失败！",Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER,0,0)
                        toast.show()
                    }
                })
            }else{
                val toast = Toast.makeText(this.activity,"手机号不能为空！",Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER,0,0)
                toast.show()
            }
        }
        v.tv_register_confirm.setOnClickListener {
            (activity as LoginActivity).switchFragment(SetPasswordFragment())
        }
        v.tv_register_login.setOnClickListener {
            (activity as LoginActivity).switchFragment(LoginFragment())
        }
        v.tv_register_forget_password.setOnClickListener {
            (activity as LoginActivity).switchFragment(ForgetPasswordFragment())
        }
        v.tv_register_problem.setOnClickListener {
            (activity as LoginActivity).switchFragment(ProblemFragment())
        }
    }
}