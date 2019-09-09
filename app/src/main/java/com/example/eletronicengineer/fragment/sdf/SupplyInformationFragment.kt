package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.AdapterGenerate
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.demand.view.*

class SupplyInformationFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.demand,container,false)
    val adapterGenerate = AdapterGenerate()
    adapterGenerate.context = view.context
    adapterGenerate.activity = activity as AppCompatActivity
    val adapter = adapterGenerate.mainSupplyIndividual()
    view.tv_demand_content.adapter = adapter
    view.tv_demand_content.layoutManager = LinearLayoutManager(view.context)
    select(view)
    return view
  }
  fun select(view: View){
    val title = arrayListOf("个人劳务","团队服务","租赁服务","三方服务")
    for(i in 0 until title.size)
    {
      view.demand_direction.addTab(view.demand_direction.newTab().setText(title[i]))
    }
    view.demand_direction.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab) {
        //                添加选中Tab的逻辑
        when (tab.text) {
          "个人劳务"->{
            val adapterGenerate = AdapterGenerate()
            adapterGenerate.context = view.context
            adapterGenerate.activity = activity as AppCompatActivity
            val adapter = adapterGenerate.mainSupplyIndividual()
            view.tv_demand_content.adapter = adapter
            view.tv_demand_content.layoutManager = LinearLayoutManager(view.context)
          }
          "团队服务"->{
            val adapterGenerate = AdapterGenerate()
            adapterGenerate.context = view.context
            adapterGenerate.activity = activity as AppCompatActivity
            val adapter = adapterGenerate.mainSupplyTeam()
            view.tv_demand_content.adapter = adapter
            view.tv_demand_content.layoutManager = LinearLayoutManager(view.context)
          }
          "租赁服务"->{
            val adapterGenerate = AdapterGenerate()
            adapterGenerate.context = view.context
            adapterGenerate.activity = activity as AppCompatActivity
            val adapter = adapterGenerate.mainSupplyLease()
            view.tv_demand_content.adapter = adapter
            view.tv_demand_content.layoutManager = LinearLayoutManager(view.context)
          }
          "三方服务"->{
            val adapterGenerate = AdapterGenerate()
            adapterGenerate.context = view.context
            adapterGenerate.activity = activity as AppCompatActivity
            val adapter = adapterGenerate.mainSupplyTripartite()
            view.tv_demand_content.adapter = adapter
            view.tv_demand_content.layoutManager = LinearLayoutManager(view.context)
          }
        }
      }

      override fun onTabUnselected(tab: TabLayout.Tab) {
        //                添加未选中Tab的逻辑
      }
      override fun onTabReselected(tab: TabLayout.Tab) {
        //                再次选中tab的逻辑
      }
    })

  }
}