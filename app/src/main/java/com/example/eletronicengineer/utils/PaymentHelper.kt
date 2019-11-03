package com.example.eletronicengineer.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import com.alipay.sdk.app.PayResultActivity
import com.alipay.sdk.app.PayTask
import com.example.eletronicengineer.R
import io.reactivex.Observable

class PaymentHelper {
    companion object {
        lateinit var mActivity: Activity
        private val SDK_PAY_FLAG = 1
        private val SDK_AUTH_FLAG = 2
        val mHandler = Handler {
            when (it.what) {
                SDK_PAY_FLAG -> {
                    val result = PayResult(it.obj as Map<String, String>)
                    val resultInfo = result.result
                    val resultStatus = result.resultStatus
                    if (TextUtils.equals(resultStatus, "9000"))
                        showAlert(mActivity, "Payment success:${result}")
                    else
                        showAlert(mActivity, "Payment failed:${result}")
                    false
                }
                else -> {
                    false
                }
            }
        }

        fun startAlipay(activity: Activity) {
            this.mActivity = activity
             Thread(Runnable {
                val alipay =PayTask(mActivity)
                val result =alipay.payV2("",true)
                val msg =Message()
                msg.what = SDK_PAY_FLAG
                msg.obj = result
                mHandler.sendMessage(msg)
            }).start()
        }

        private fun showAlert(ctx: Context, info: String) {
            showAlert(ctx, info, null)
        }

        private fun showAlert(
            ctx: Context,
            info: String,
            onDismiss: DialogInterface.OnDismissListener?
        ) {
            AlertDialog.Builder(ctx)
                .setMessage(info)
                .setPositiveButton(R.string.confirm, null)
                .setOnDismissListener(onDismiss)
                .show()
        }

    }
}