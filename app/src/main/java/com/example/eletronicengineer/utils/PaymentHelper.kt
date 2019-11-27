package com.example.eletronicengineer.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import androidx.fragment.app.FragmentActivity
import com.alipay.sdk.app.PayResultActivity
import com.alipay.sdk.app.PayTask
import com.example.eletronicengineer.R
import com.example.eletronicengineer.model.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class PaymentHelper {
    companion object {
        lateinit var mActivity: FragmentActivity
        private val SDK_PAY_FLAG = 1
        private val SDK_AUTH_FLAG = 2
        val mHandler = Handler {
            when (it.what) {
                SDK_PAY_FLAG -> {
                    val result = PayResult(it.obj as Map<String, String>)
                    val resultInfo = result.result
                    val resultStatus = result.resultStatus
                    if (TextUtils.equals(resultStatus, "9000")){
                        checkResult(resultInfo)
                    }
                    else
                        showAlert(mActivity, "Payment failed:${resultInfo}")
                    false
                }
                else -> {
                    false
                }
            }
        }

        private fun checkResult(result: String) {
            val resultJson = JSONObject(result).getJSONObject("alipay_trade_app_pay_response")
            val result = Observable.create<RequestBody> {

                val json = JSONObject().put("outTradeNo", resultJson.getString("out_trade_no"))
                    .put("tradeNo", resultJson.getString("trade_no"))
                val requestBody =
                    RequestBody.create(MediaType.parse("application/json"), json.toString())
                it.onNext(requestBody)
            }.subscribe {
                val result = startSendMessage(
                    it,
                    "http://192.168.1.132:8032" + Constants.HttpUrlPath.My.checkAlipay
                )
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe({
                        val jsonObject = JSONObject(it.string())
                        val code = jsonObject.getInt("code")
                        var result = ""
                        showAlert(mActivity, "Payment success:${jsonObject.getString("message")}")
                        if (code == 200) {
                            mActivity.supportFragmentManager.popBackStackImmediate()
                        }
                    }, {
                        it.printStackTrace()
                    })
            }

        }
        fun startAlipay(activity: FragmentActivity, orderInfo: String) {
            this.mActivity = activity
             Thread(Runnable {
                val alipay =PayTask(mActivity)
                val result =alipay.payV2(orderInfo,true)
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