package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.model.ApiConfig
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.getRequirementPerson
import kotlinx.android.synthetic.main.demand.view.*
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DemandInformationFragment: Fragment(){
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.demand, container, false)
//    val adapterGenerate = AdapterGenerate()
//    adapterGenerate.context = view.context
//    adapterGenerate.activity = activity as AppCompatActivity
//    val adapter = adapterGenerate.mainDemandIndividual()
//    view.tv_demand_content.adapter = adapter
//    view.tv_demand_content.layoutManager = LinearLayoutManager(view.context)
    select(view)
    return view
  }

  private fun fetchPersonalData(pageNumber : Int, view:View){
    val result=getRequirementPerson(pageNumber,ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread()).subscribe ({
        // todo
        for(i in it.data)
        {
          addIndividualItem(view, i.projectName)
        }
        if (pageNumber < it.pageCount.toInt()) {
          // go next page
          fetchPersonalData(pageNumber+1, view)
        }
      },{
        it.printStackTrace()
      })
  }

  private fun addIndividualItem(view:View, title:String)
  {
    val adapterGenerate = AdapterGenerate()
    adapterGenerate.context = view.context
    adapterGenerate.activity = activity as AppCompatActivity
    val adapter = adapterGenerate.mainDemandIndividual()
    adapter.mData[0].shiftInputTitle=title
    view.tv_demand_content.adapter = adapter
    view.tv_demand_content.layoutManager = LinearLayoutManager(view.context)
  }
  private fun select(view:View) {
    val title = arrayListOf("需求个人","需求团队","需求租赁","需求三方")
    for(i in 0 until title.size)
    {
      view.demand_direction.addTab(view.demand_direction.newTab().setText(title[i]))
    }
    view.demand_direction.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab) {
        //                添加选中Tab的逻辑
        when (tab.text) {
          "需求个人"->{
            fetchPersonalData(1, view)
          }
          "需求团队"->{
            val adapterGenerate = AdapterGenerate()
            adapterGenerate.context = view.context
            adapterGenerate.activity = activity as AppCompatActivity
            val adapter = adapterGenerate.mainDemandTeam()
            view.tv_demand_content.adapter = adapter
            view.tv_demand_content.layoutManager = LinearLayoutManager(view.context)
          }
          "需求租赁"->{
            val adapterGenerate = AdapterGenerate()
            adapterGenerate.context = view.context
            adapterGenerate.activity = activity as AppCompatActivity
            val adapter = adapterGenerate.mainDemandLease()
            view.tv_demand_content.adapter = adapter
            view.tv_demand_content.layoutManager = LinearLayoutManager(view.context)
          }
          "需求三方"->{
            val adapterGenerate = AdapterGenerate()
            adapterGenerate.context = view.context
            adapterGenerate.activity = activity as AppCompatActivity
            val adapter = adapterGenerate.mainDemandTripartite()
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