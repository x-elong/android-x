package com.example.eletronicengineer.fragment.login

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.LoginActivity
import com.example.eletronicengineer.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_set_password.view.*
import java.util.regex.Pattern


class SetPasswordFragment : Fragment()  {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_set_password,container,false)
        initFragment(view)
        return view
    }

    private fun initFragment(v: View) {
        v.tv_set_password_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        v.tv_set_password_next.setOnClickListener {
            var password:String = v.et_set_password_number.text.toString()
            var password_check:String = v.et_set_password_again.text.toString()
            var result = ""
            if(password.length < 6){
                result="${password},密码至少6位"
            }
            else if(!ispsd(password)){
                result="密码必须由字母+数字组成"
            }
            else if(password!=password_check){
                result="两次输入的密码不相同"
            }
            else {
                result="注册成功"
                startActivity(Intent(context, MainActivity::class.java))
            }
            val toast = Toast.makeText(context,result,Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER,0,0)
            toast.show()
        }
    }
    fun ispsd(psd: String): Boolean {
        val p = Pattern.compile("[A-Za-z].*[0-9]|[0-9].*[A-Za-z]")
        val m = p.matcher(psd)
        return m.matches()
    }
}