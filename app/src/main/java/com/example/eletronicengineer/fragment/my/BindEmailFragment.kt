package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyInformationActivity
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import kotlinx.android.synthetic.main.fragment_bind_email.view.*
import kotlinx.android.synthetic.main.fragment_bind_phone.view.*
import kotlinx.android.synthetic.main.fragment_bind_phone.view.btn_modify_phone

class BindEmailFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):BindEmailFragment{
            val bindPhoneFragment = BindEmailFragment()
            bindPhoneFragment.arguments = args
            return bindPhoneFragment
        }
    }
    lateinit var email:String
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_bind_email,container,false)
        mView.tv_bind_email_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        email = arguments!!.getString("email")
        return mView
    }

    override fun onStart() {
        super.onStart()

        if(email==""){
            mView.tv_bind_email_number.text = "当前未绑定邮箱"
            mView.btn_modify_email.text = "绑定邮箱"

        }else{
            mView.tv_bind_email_number.text="当前绑定邮箱:"+email
        }
        mView.btn_modify_email.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("title",mView.btn_modify_email.text.toString())
            FragmentHelper.switchFragment(activity!!,BindEmailInformationFragment.newInstance(bundle),R.id.frame_my_information,"")
        }
    }
    fun update(email:String){
        this.email = email
        val fragment=(activity!!.supportFragmentManager.findFragmentByTag("MyInformation") as MyInformationFragment)
        fragment.update(email)
    }
}