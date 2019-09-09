package com.example.eletronicengineer.fragment.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.LoginActivity
import com.example.eletronicengineer.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        initFragment(view)
        return view
    }

    private fun initFragment(v: View) {
        v.tv_login_back.setOnClickListener {
            activity!!.finish()
        }
        v.tv_login_confirm.setOnClickListener {
            var username:String = v.et_login_name.text.toString()
            var password:String = v.et_login_password.text.toString()
            if(!username.isBlank() && !password.isBlank()){
                startActivity(Intent(context, MainActivity::class.java))
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
}