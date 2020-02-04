package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_feedback.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class FeedbackFragment :Fragment(){
    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_feedback,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_feedback_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.tv_feedback_ok.setOnClickListener {
            val str = mView.et_feedback_content.text.toString()
            if(str.isEmpty()){
                Toast.makeText(context,"反馈内容不能为空",Toast.LENGTH_SHORT).show()
            }
            else{
                val loadingDialog = LoadingDialog(mView.context,"正在提交...")
                loadingDialog.show()
                val result = Observable.create<RequestBody>{
                    val requestBody = RequestBody.create(MediaType.parse("text/x-markdown"),str)
                    it.onNext(requestBody)
                }.subscribe {
                    val result = startSendMessage(it,
                        UnSerializeDataBase.mineBasePath+ Constants.HttpUrlPath.My.insertFeedback).subscribeOn(
                        Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            loadingDialog.dismiss()
                            if(JSONObject(it.string()).getInt("code")==200){
                                Toast.makeText(context,"反馈提交成功",Toast.LENGTH_SHORT).show()
                                activity!!.supportFragmentManager.popBackStackImmediate()
                            }else{
                                Toast.makeText(context,"反馈提交失败",Toast.LENGTH_SHORT).show()
                            }
                        },{
                            loadingDialog.dismiss()
                            Toast.makeText(context,"服务器异常",Toast.LENGTH_SHORT).show()
                            it.printStackTrace()
                        })
                }
            }

        }
    }
}