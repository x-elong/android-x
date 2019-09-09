package com.example.eletronicengineer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.utils.ItemGenerate
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.fragment_application.view.*

/**
 * 项目名:    android
 * 包名:      com.example.eletronicengineer.fragment
 * 文件名:    MessageFragment
 * 创建者:    LLH
 * 创建时间:  2019/7/12 16:30
 * 描述:      TODO
 */
class ApplicationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_application, container, false)
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context=view.context
        adapterGenerate.activity=activity as AppCompatActivity
        val adapter=adapterGenerate.ProfessionalApplication()
        view.rv_application_content.adapter = adapter
        view.rv_application_content.layoutManager = LinearLayoutManager(view.context)
        return view
    }

}