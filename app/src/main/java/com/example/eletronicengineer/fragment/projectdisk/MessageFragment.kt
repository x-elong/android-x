package com.example.eletronicengineer.fragment.projectdisk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R

/**
 * 项目名:    android
 * 包名:      com.example.eletronicengineer.fragment
 * 文件名:    MessageFragment
 * 创建者:    LLH
 * 创建时间:  2019/7/12 16:30
 * 描述:      TODO
 */
class MessageFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_message, container, false)
        return view
    }
}