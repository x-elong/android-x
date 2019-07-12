package com.example.eletronicengineer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.Function
import kotlinx.android.synthetic.main.application.view.*
import java.util.ArrayList

class ApplicationFragment : Fragment() {

    var functionList: MutableList<Function> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.application, container, false)
        initFunction(view)
        return view
    }
    private fun initFunction(v: View) {
        functionList.clear()
        var f = Function("合 作 方", R.drawable.cooperation)
        functionList.add(f)
        f = Function("合 同", R.drawable.contract)
        functionList.add(f)
        f = Function("项 目 部", R.drawable.project_department)
        functionList.add(f)
        f = Function("招投供需", R.drawable.rsd)
        functionList.add(f)
        f = Function("发 票", R.drawable.invoice)
        functionList.add(f)
        f = Function("记 账", R.drawable.bookkeeping)
        functionList.add(f)
        f = Function("任务统计", R.drawable.task_statistics)
        functionList.add(f)
        f = Function("财务统计", R.drawable.financial_statistics)
        functionList.add(f)
        f = Function("公 告", R.drawable.notice)
        functionList.add(f)
        f = Function("资 源 库", R.drawable.resource_library)
        functionList.add(f)
        f = Function("企业云盘", R.drawable.enterprise_cloud)
        functionList.add(f)
        val recyclerView = v.recycler_view
        val linearLayoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = linearLayoutManager
        val functionAdapter = FunctionAdapter(functionList)
        recyclerView.adapter = functionAdapter
    }
}