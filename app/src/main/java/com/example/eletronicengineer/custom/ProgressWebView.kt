package com.example.eletronicengineer.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.example.eletronicengineer.R

class ProgressWebView :WebView {
    lateinit var mProgressBar: ProgressBar
    constructor(context : Context, attrs : AttributeSet):super(context,attrs){
        mProgressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 8)
        mProgressBar.setLayoutParams(layoutParams)
        val drawable = ContextCompat.getDrawable(context,R.drawable.web_progress_bar_states)
        mProgressBar.setProgressDrawable(drawable)
        mProgressBar.setMax(100)
        mProgressBar.setLayoutParams(LayoutParams(LayoutParams.MATCH_PARENT,5,0,0))
        addView(mProgressBar)
        setWebChromeClient(WebChromeClient())
    }

    inner class WebChromeClient: android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {

            if(newProgress == 100)
                mProgressBar.visibility = View.GONE
            else {
                if(mProgressBar.visibility == View.GONE)
                    mProgressBar.visibility = View.VISIBLE
                mProgressBar.setProgress(newProgress)
            }
            super.onProgressChanged(view, newProgress)

        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        val lp = mProgressBar.getLayoutParams() as LayoutParams
        lp.x = l
        lp.y = t
        mProgressBar.setLayoutParams(lp)
        super.onScrollChanged(l, t, oldl, oldt)
    }
}