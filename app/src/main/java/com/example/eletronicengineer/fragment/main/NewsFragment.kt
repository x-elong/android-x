package com.example.eletronicengineer.fragment.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ProjectDiskActivity
import kotlinx.android.synthetic.main.news.view.*


class NewsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.news, container, false)
        initOnClick(view)
        return view
    }

    fun initOnClick(view: View) {
//        view.point_tv.setOnClickListener {
//            startActivity(Intent(activity,ProjectDiskActivity::class.java))
//        }
    }
}
