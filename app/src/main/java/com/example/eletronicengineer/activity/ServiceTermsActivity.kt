package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.activity_service_terms.*

class ServiceTermsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_terms)
        tv_service_terms_back.setOnClickListener {
            finish()
        }
        wv_service_terms.setBackgroundColor(0)
        //允许执行JavaScript脚本
        wv_service_terms.settings.setJavaScriptEnabled(true)

        wv_service_terms.settings.setLoadWithOverviewMode(true)
        //跳转至另一网站时 仍在当前WebView中显示 而不是打开系统浏览器
        wv_service_terms.settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN)
//        wv_service_terms.settings.setSupportZoom(true)
        wv_service_terms.settings.setLoadWithOverviewMode(true)
        wv_service_terms.settings.setUseWideViewPort(true)
        wv_service_terms.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        })
        wv_service_terms.loadUrl("http://www.ycdlfw.com/userAgreement/")
    }
}
