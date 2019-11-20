package com.example.eletronicengineer.fragment.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.LoginActivity
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.putSimpleMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_forget_password.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class ForgetPasswordFragment: Fragment()  {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_forget_password,container,false)
        initFragment(view)
        return view
    }

    private fun initFragment(v: View) {
        v.tv_forget_password_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        v.tv_forget_password_next.setOnClickListener {
            val result = Observable.create<RequestBody> {
                //json.remove(key)
                //val imagePath = upImage(key)
                //var jsonObject= json.put(key,upImage(key))
                val jsonObject = JSONObject().put("mobileNumber", v.et_forget_password_number.text)
                    .put("code", v.et_forget_password.text)
                Log.i("json ,,", jsonObject.toString())
                val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                it.onNext(requestBody)
            }.subscribe {
                val result =
                    putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.upateDTO1).observeOn(
                        AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                            {
                                //                                                        Toast.makeText(context,it.string(),Toast.LENGTH_SHORT).show()
                                if (JSONObject(it.string()).getInt("code") == 200) {
                                    FragmentHelper.switchFragment(activity!!,ForgetPasswordThreeFragment(),R.id.frame_login,"")
                                }
                            },
                            {
                                it.printStackTrace()
                            }
                        )
            }

        }
    }
}