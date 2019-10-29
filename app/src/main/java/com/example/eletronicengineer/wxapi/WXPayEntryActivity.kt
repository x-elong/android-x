package com.example.eletronicengineer.wxapi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eletronicengineer.R
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.eletronicengineer.utils.ToastHelper
import com.example.eletronicengineer.utils.payNotify
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelpay.PayReq
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.pay_result.*
import org.json.JSONObject

class WXPayEntryActivity : AppCompatActivity(),IWXAPIEventHandler{
    val TAG = "MicroMsg.SDKSample.WXPayEntryActivity"
    lateinit var api: IWXAPI
    private val APP_ID = "wx69d595e7793f3fb9"
    var orderNumber = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pay_result)
        val intent = getIntent()
        val json = JSONObject(intent.getStringExtra("json"))
        orderNumber = json.getString("orderNumber")
        api = WXAPIFactory.createWXAPI(this,null)
        api.registerApp(APP_ID)
        api.handleIntent(getIntent(),this)

        val req = PayReq()
        req.appId = json.getString("appid")
        req.partnerId = json.getString("partnerid")
        req.prepayId = json.getString("prepayid")
        req.packageValue = json.getString("package")
        req.nonceStr = json.getString("noncestr")
        req.timeStamp = json.getString("timestamp")
        req.sign = json.getString("sign")
        api.sendReq(req)
        Log.i("checkArgs=" , req.checkArgs().toString())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        api.handleIntent(intent,this)
    }
    override fun onResp(resp: BaseResp) {
        var payResult = "支付失败"
        if(resp.type== ConstantsAPI.COMMAND_PAY_BY_WX){
            if(resp.errCode==0){
                val result = payNotify(orderNumber).subscribeOn(Schedulers.io()).observeOn(
                    AndroidSchedulers.mainThread())
                    .subscribe({
                        val json = JSONObject(it.string())
                        var payResult = ""
                        if(json.getInt("code")==200){
                            payResult="支付成功"
                        }
                        dialogResult(payResult)
                    },{
                        it.printStackTrace()
                    })
            }else{
                dialogResult(payResult)
            }
        }

    }

    override fun onReq(req: BaseReq) {
    }
    fun dialogResult(payResult:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.app_tip)
            .setMessage(payResult)
            .setPositiveButton("OK",object :DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    finish()
                }
            })
            .setCancelable(false)
        builder.show()
    }
}