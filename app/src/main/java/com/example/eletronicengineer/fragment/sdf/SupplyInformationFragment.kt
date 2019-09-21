package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.utils.AdapterGenerate
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.demand.view.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

class SupplyInformationFragment : Fragment() {
  val selectOption1Items:MutableList<String> =ArrayList()
  val selectOption2Items:MutableList<MutableList<String>> =ArrayList()
  val selectOption3Items:MutableList<MutableList<MutableList<String>>> =ArrayList()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.demand,container,false)
    val Option1Items= listOf("普工","特种作业","专业操作","测量工","驾驶员","九大员","注册类","其他") as MutableList<String>
    val Option2Items= listOf(listOf("普工"), listOf("低压电工作业","高压电工作业","电力电缆作业","继电保护作业","电气试验作业","融化焊接与热切割作业","登高架设作业"),
      listOf("压接操作","机动绞磨操作","牵张设备操作","起重机械操作","钢筋工","混凝土工","木工","模板工","油漆工","砌筑工","通风工","打桩工","架子工"),
      listOf("测量工"), listOf("驾驶证A1","驾驶证A2","驾驶证A3","驾驶证B1","驾驶证B2","驾驶证C1","驾驶证C2","驾驶证C3","驾驶证D","驾驶证E"),
      listOf("施工员","安全员","质量员","材料员","资料员","预算员","标准员","机械员","劳务员"), listOf("造价工程师","一级建造师","安全工程师","电气工程师"),
      listOf("")) as MutableList<MutableList<String>>
    view.tv_demand_type_select.setOnClickListener {
      initDemandPerson(view,Option1Items,Option2Items)
    }
    initProjectSite(view)
    view.tv_demand_site_select.visibility=View.GONE
    view.tv_demand_site_select.setOnClickListener {
      initSite(view)
    }
    view.tv_demand_site_clear.setOnClickListener {
      view.tv_demand_site_select.text="选择地点"
      view.tv_demand_site_clear.visibility=View.GONE
    }
    view.tv_demand_type_clear.setOnClickListener {
      view.tv_demand_type_select.text="选择需求类别"
      view.tv_demand_type_clear.visibility=View.GONE
    }
    view.tv_demand_elect_standars_clear.setOnClickListener {
      view.tv_demand_elect_standard_select.text="选择电压等级"
      view.tv_demand_elect_standars_clear.visibility=View.GONE
    }
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
            view.rv_demand_content.adapter = adapter
            view.rv_demand_content.layoutManager = LinearLayoutManager(view.context)

            view.tv_demand_elect_standard_select.visibility=View.GONE
            view.tv_demand_site_select.visibility=View.GONE
            view.tv_demand_type_select.visibility=View.VISIBLE
            val Option1Items= listOf("普工","特种作业","专业操作","测量工","驾驶员","九大员","注册类","其他") as MutableList<String>
            val Option2Items= listOf(listOf("普工"), listOf("低压电工作业","高压电工作业","电力电缆作业","继电保护作业","电气试验作业","融化焊接与热切割作业","登高架设作业"),
              listOf("压接操作","机动绞磨操作","牵张设备操作","起重机械操作","钢筋工","混凝土工","木工","模板工","油漆工","砌筑工","通风工","打桩工","架子工"),
              listOf("测量工"), listOf("驾驶证A1","驾驶证A2","驾驶证A3","驾驶证B1","驾驶证B2","驾驶证C1","驾驶证C2","驾驶证C3","驾驶证D","驾驶证E"),
              listOf("施工员","安全员","质量员","材料员","资料员","预算员","标准员","机械员","劳务员"), listOf("造价工程师","一级建造师","安全工程师","电气工程师"),
              listOf("")) as MutableList<MutableList<String>>
            view.tv_demand_type_select.setOnClickListener {
              initDemandPerson(view,Option1Items,Option2Items)
            }
          }
          "团队服务"->{
            val adapterGenerate = AdapterGenerate()
            adapterGenerate.context = view.context
            adapterGenerate.activity = activity as AppCompatActivity
            val adapter = adapterGenerate.mainSupplyTeam()
            view.rv_demand_content.adapter = adapter
            view.rv_demand_content.layoutManager = LinearLayoutManager(view.context)

            view.tv_demand_elect_standard_select.visibility=View.VISIBLE
            view.tv_demand_type_select.visibility=View.VISIBLE
            view.tv_demand_site_select.visibility=View.VISIBLE
            val Option1Items= listOf("变电施工队","主网施工队","配网施工队","测量设计","马帮运输","桩基服务","非开挖顶管拉管作业","试验调试","跨越架","运行维护") as MutableList<String>
            val Option2Items= listOf("0.4KV","10KV","35KV","110KV","220KV","500KV")
            view.tv_demand_type_select.setOnClickListener {
              initDemandTeam(view,Option1Items)
            }
            view.tv_demand_elect_standard_select.setOnClickListener {
              initElectDialog(view, Option2Items as MutableList<String>)
            }
          }
          "租赁服务"->{
            val adapterGenerate = AdapterGenerate()
            adapterGenerate.context = view.context
            adapterGenerate.activity = activity as AppCompatActivity
            val adapter = adapterGenerate.mainSupplyLease()
            view.rv_demand_content.adapter = adapter
            view.rv_demand_content.layoutManager = LinearLayoutManager(view.context)

            view.tv_demand_elect_standard_select.visibility=View.GONE
            view.tv_demand_type_select.visibility=View.VISIBLE
            view.tv_demand_site_select.visibility=View.VISIBLE
            val Option1Items= listOf("车辆租赁","工器具租赁","机械租赁","设备租赁") as MutableList<String>
            val Option2Items= listOf(listOf("皮卡车","单排座工程车","双排座工程车","多排座工程车","载重自卸车","载重平板车","随吊车"),
              listOf(""), listOf(""), listOf("")) as MutableList<MutableList<String>>
            view.tv_demand_type_select.setOnClickListener {
              initDemandPerson(view,Option1Items, Option2Items)
            }
          }
          "三方服务"->{
            val adapterGenerate = AdapterGenerate()
            adapterGenerate.context = view.context
            adapterGenerate.activity = activity as AppCompatActivity
            val adapter = adapterGenerate.mainSupplyTripartite()
            view.rv_demand_content.adapter = adapter
            view.rv_demand_content.layoutManager = LinearLayoutManager(view.context)

            view.tv_demand_elect_standard_select.visibility=View.GONE
            view.tv_demand_type_select.visibility=View.VISIBLE
            view.tv_demand_site_select.visibility=View.VISIBLE
            val Option1Items= listOf("培训办证","财务记账","代办资格","标书服务","法律咨询","软件服务","资质合作","其他") as MutableList<String>
            view.tv_demand_type_select.setOnClickListener {
              initDemandTeam(view,Option1Items)
            }
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

  //初始化地点数据
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
  //地点选择弹窗
  fun initSite(view:View){
    var mHandler: Handler = Handler(Handler.Callback {
      when (it.what) {
        RecyclerviewAdapter.MESSAGE_SELECT_OK -> {
          val selectContent = it.data.getString("selectContent")
          view.tv_demand_site_select.text = selectContent
          view.tv_demand_site_clear.visibility = View.VISIBLE
          false
        }
        else -> {
          false
        }
      }
    })
    val selectDialog = CustomDialog(
      CustomDialog.Options.THREE_OPTIONS_SELECT_DIALOG,
      view.context,
      mHandler,
      selectOption1Items,
      selectOption2Items,
      selectOption3Items
    ).multiDialog
    selectDialog.show()
  }
  //个人劳务 租赁服务弹窗
  fun initDemandPerson(view:View,Option1Items: MutableList<String>,Option2Items: MutableList<MutableList<String>>){
    var mHandler: Handler = Handler(Handler.Callback {
      when(it.what)
      {
        RecyclerviewAdapter.MESSAGE_SELECT_OK ->{
          val selectContent=it.data.getString("selectContent")
          view.tv_demand_type_select.text=selectContent
          view.tv_demand_type_clear.visibility=View.VISIBLE
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
  //团队服务 三方服务弹窗
  fun initDemandTeam(view:View,Option1Items:MutableList<String>){
    var mHandler: Handler = Handler(Handler.Callback {
      when(it.what)
      {
        RecyclerviewAdapter.MESSAGE_SELECT_OK ->{
          val selectContent=it.data.getString("selectContent")
          view.tv_demand_type_select.text=selectContent
          view.tv_demand_type_clear.visibility=View.VISIBLE
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
  //电压等级弹窗
  fun initElectDialog(view:View,Option1Items:MutableList<String>){
    var mHandler: Handler = Handler(Handler.Callback {
      when(it.what)
      {
        RecyclerviewAdapter.MESSAGE_SELECT_OK ->{
          val selectContent=it.data.getString("selectContent")
          view.tv_demand_elect_standard_select.text=selectContent
          view.tv_demand_elect_standars_clear.visibility=View.VISIBLE
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
}