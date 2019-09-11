package com.example.eletronicengineer.fragment.sdf

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.DemandActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.model.ApiConfig
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.getRequirementPerson
import kotlinx.android.synthetic.main.demand.view.*
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder


class DemandInformationFragment: Fragment(){
    val selectOption1Items:MutableList<String> =ArrayList()
    val selectOption2Items:MutableList<MutableList<String>> =ArrayList()
    val selectOption3Items:MutableList<MutableList<MutableList<String>>> =ArrayList()

    var Option1Items:MutableList<String> =ArrayList()
    var Option2Items: MutableList<MutableList<String>> =ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.demand, container, false)
//    val adapterGenerate = AdapterGenerate()
//    adapterGenerate.context = view.context
//    adapterGenerate.activity = activity as AppCompatActivity
//    val adapter = adapterGenerate.mainDemandIndividual()
//    view.tv_demand_content.adapter = adapter
//    view.tv_demand_content.layoutManager = LinearLayoutManager(view.context)
        initProjectSite(view)

        initProjectOneType(view)
        view.tv_demand_type_select.setOnClickListener {
            initProjectTwoType(view)
            Option1Items= listOf("普工","特种作业","专业操作","测量工","驾驶员","九大员","注册类","其他") as MutableList<String>
            Option2Items= listOf(listOf("普工"), listOf("低压电工作业","高压电工作业","电力电缆作业","继电保护作业","电气试验作业","融化焊接与热切割作业","登高架设作业")) as MutableList<MutableList<String>>
        }
    view.tv_demand_site_select.setOnClickListener {
        var mHandler:Handler= Handler(Handler.Callback {
            when(it.what)
            {
                RecyclerviewAdapter.MESSAGE_SELECT_OK ->{
                    val selectContent=it.data.getString("selectContent")
                    view.tv_demand_site_select.text=selectContent
                    false
                }
                else->
                {
                    false
                }
            }
        })
        val selectDialog= CustomDialog(CustomDialog.Options.THREE_OPTIONS_SELECT_DIALOG,view.context,mHandler,selectOption1Items,selectOption2Items,selectOption3Items).multiDialog
        selectDialog.show()
    }
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
             Option1Items= listOf("普工","特种作业","专业操作","测量工","驾驶员","九大员","注册类","其他") as MutableList<String>
             Option2Items= listOf(listOf("普工"), listOf("低压电工作业","高压电工作业","电力电缆作业","继电保护作业","电气试验作业","融化焊接与热切割作业","登高架设作业")) as MutableList<MutableList<String>>
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

    fun initProjectSite(view:View){
        val resultBuilder= StringBuilder()
        val bf= BufferedReader(InputStreamReader(context!!.assets.open("pca.json")))
        try {
            var line=bf.readLine()
            while (line!=null)
            {
                resultBuilder.append(line)
                line=bf.readLine()
            }
        }
        catch (io: IOException)
        {
            io.printStackTrace()
        }
        val json= JSONObject(resultBuilder.toString())
        val keys:Iterator<String> = json.keys()
        while (keys.hasNext()){
            val key = keys.next()
            selectOption1Items.add(key)
            val js = json.getJSONObject(key)
            val jskeys = js.keys()
            selectOption2Items.add(ArrayList())
            selectOption3Items.add(ArrayList())
            while (jskeys.hasNext()){
                val jskey = jskeys.next()
                selectOption2Items[selectOption2Items.size-1].add(jskey)
                selectOption3Items[selectOption3Items.size-1].add(ArrayList())
                var valueArray = js.getJSONArray(jskey)
                for (j in 0 until valueArray.length()){
                    selectOption3Items[selectOption3Items.size-1][selectOption3Items[selectOption3Items.size-1].size-1].add(valueArray[j].toString())
                }
            }
        }
    }
    fun initProjectOneType(view:View){
        var mHandler:Handler= Handler(Handler.Callback {
            when(it.what)
            {
                RecyclerviewAdapter.MESSAGE_SELECT_OK ->{
                    val selectContent=it.data.getString("selectContent")
                    view.tv_demand_type_select.text=selectContent
                    false
                }
                else->
                {
                    false
                }
            }
        })
        val selectDialog= CustomDialog(CustomDialog.Options.SELECT_DIALOG,view.context,Option1Items,mHandler).dialog
        selectDialog.show()
    }
    fun initProjectTwoType(view:View){
        var mHandler:Handler= Handler(Handler.Callback {
            when(it.what)
            {
                RecyclerviewAdapter.MESSAGE_SELECT_OK ->{
                    val selectContent=it.data.getString("selectContent")
                   view.tv_demand_type_select.text=selectContent
                    false
                }
                else->
                {
                    false
                }
            }
        })
        val selectDialog= CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,view.context,mHandler,Option1Items,Option2Items).multiDialog
        selectDialog.show()
    }
}