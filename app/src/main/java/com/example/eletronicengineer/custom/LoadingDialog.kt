package com.example.eletronicengineer.custom

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.dialog_loading.*

/**
 * Created by LT on 2018/5/12.
 */
class LoadingDialog : Dialog {

    lateinit var mMessage: String
    var mImageId: Int = 0
    var mCancelable = false
    lateinit var mRotateAnimation: RotateAnimation

    constructor(context: Context, message: String):super(context,R.style.LoadingDialog){
        mMessage = message
        mImageId = R.mipmap.ic_dialog_loading
    }
    constructor(context: Context, message: String, imageId:Int):super(context,R.style.LoadingDialog) {
        mMessage = message
        mImageId=imageId
    }

    constructor(context: Context,message: String,imageId: Int,themeResId:Int,cancelable:Boolean):super(context,themeResId){
        mMessage = message
        mImageId=imageId
        mCancelable=cancelable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        setContentView(R.layout.dialog_loading)
        // 设置窗口大小
        val windowManager = window!!.windowManager
        val screenWidth = windowManager.defaultDisplay.width
        val attributes = window!!.attributes
        attributes.alpha = 0.3f
        attributes.width = screenWidth / 3
        attributes.height = attributes.width
        window!!.attributes = attributes
        setCancelable(mCancelable)

        val tvLoading = tv_loading
        val ivLoading = iv_loading
        tvLoading.text = mMessage
        ivLoading.setImageResource(mImageId)
        ivLoading.measure(0, 0)
        mRotateAnimation = RotateAnimation(
            0f,
            360f,
            (ivLoading.measuredWidth / 2).toFloat(),
            (ivLoading.measuredHeight/ 2).toFloat()
        )
        mRotateAnimation.interpolator = LinearInterpolator()
        mRotateAnimation.duration = 1000
        mRotateAnimation.repeatCount = -1
        ivLoading.startAnimation(mRotateAnimation)
    }

    override fun dismiss() {
        mRotateAnimation.cancel()
        super.dismiss()
    }

    override fun onKeyDown(keyCode: Int, @NonNull event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 屏蔽返回键
            mCancelable
        } else super.onKeyDown(keyCode, event)
    }
}
