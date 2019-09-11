package com.example.eletronicengineer.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ProjectDiskActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.fragment_project_disk.view.*
import kotlinx.android.synthetic.main.item_supply.view.*

class ProjectDiskFragment : Fragment() {
    lateinit var mActivity: ProjectDiskActivity
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = (activity as ProjectDiskActivity?)!!
    }
    var pageCount = 1
    var page = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_project_disk, container, false)
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context=view.context
        adapterGenerate.activity=activity as AppCompatActivity
        //val adapter = adapterGenerate.Professional_ProjectMore()
        val adapter = adapterGenerate.ProfessionalProjectDisk()
        view.rv_project_disk_content.adapter=adapter
        view.rv_project_disk_content.layoutManager=LinearLayoutManager(view.context)
        view.rv_project_disk_content.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                if(lastCompletelyVisibleItemPosition == layoutManager.itemCount-1 && page<=pageCount){
                    Log.i("page","$page")
                    Toast.makeText(view.context,"滑动到底了",Toast.LENGTH_SHORT).show()
                    val networkAdapter = NetworkAdapter(adapter.mData,context!!)
                    networkAdapter.getDataFromNetwork(123,page,"http://192.168.1.133:8014",adapter,this@ProjectDiskFragment)
                    page++
                }
            }
        })
        return view
    }
}