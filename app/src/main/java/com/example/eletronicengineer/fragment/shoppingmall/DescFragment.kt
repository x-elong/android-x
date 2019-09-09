package com.example.eletronicengineer.fragment.shoppingmall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R

/**
 * 项目名:    android
 * 包名:      com.example.eletronicengineer.fragment.shoppingmall
 * 文件名:    DescFragment
 * 创建者:    LLH
 * 创建时间:  2019/8/17 10:55
 * 描述:      TODO
 */
class DescFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop_desc, container, false)
        return view
    }
}