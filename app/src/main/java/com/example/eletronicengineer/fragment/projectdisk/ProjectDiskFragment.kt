package com.example.eletronicengineer.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ProjectDiskActivity
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.fragment_project_disk.view.*
import kotlinx.android.synthetic.main.item_supply.view.*

/**
 * 项目名:    android
 * 包名:      com.example.eletronicengineer.fragment
 * 文件名:    MessageFragment
 * 创建者:    LLH
 * 创建时间:  2019/7/12 16:30
 * 描述:      TODO
 */
class ProjectDiskFragment : Fragment() {
    lateinit var mActivity: ProjectDiskActivity
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = (activity as ProjectDiskActivity?)!!
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_project_disk, container, false)

        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context=view.context
        adapterGenerate.activity=activity as AppCompatActivity
        //val adapter = adapterGenerate.Professional_ProjectMore()
        val adapter = adapterGenerate.ProfessionalProjectDisk()
        view.rv_project_disk_content.adapter=adapter
        view.rv_project_disk_content.layoutManager=LinearLayoutManager(view.context)
        return view
    }
}