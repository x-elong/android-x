package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.fragment_feedback.view.*

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
            if(mView.et_feedback_content.text.isEmpty()){
                Toast.makeText(context,"反馈内容不能为空",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,"反馈提交成功",Toast.LENGTH_SHORT).show()
                activity!!.supportFragmentManager.popBackStackImmediate()
            }

        }
    }
}