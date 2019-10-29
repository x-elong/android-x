package com.example.eletronicengineer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.creatOrder
import com.example.eletronicengineer.wxapi.WXPayEntryActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_vip.*
import org.json.JSONObject

class MyVipActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vip)
        btn_pay_test.setOnClickListener {
                val result = creatOrder("1").subscribeOn(Schedulers.io()).observeOn(
                    AndroidSchedulers.mainThread())
                    .subscribe({
                        val json = JSONObject(it.string())
                        if(json.getInt("code")==200){
                            val intent = Intent(this,WXPayEntryActivity::class.java)
                            intent.putExtra("json",json.getString("message"))
                            startActivity(intent)
                        }
                    },{
                        it.printStackTrace()
                    })
        }
    }
}
