package com.example.eletronicengineer.fragment.sdf

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.ListAdapterForDemand
import com.example.eletronicengineer.adapter.ListAdapterForSupply
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.aninterface.Movie
import com.example.eletronicengineer.aninterface.PersonalIssue
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.getSupplyPerson
import com.example.eletronicengineer.utils.getSupplyTeam
import com.google.android.material.tabs.TabLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.demand.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

lateinit var keyforsupply:MutableList<String>
lateinit var valueforsupply:MutableList<String>
class SupplyInformationFragment : Fragment() {

  val selectOption1Items:MutableList<String> =ArrayList()
  val selectOption2Items:MutableList<MutableList<String>> =ArrayList()
  val selectOption3Items:MutableList<MutableList<MutableList<String>>> =ArrayList()
  var mPersonAdapter: ListAdapterForDemand?=null
  var mPersonalIssueAdapter: ListAdapterForSupply?=null
    var checkedItems= arrayOf(false,false,false,false,false,false)
    var selectContentElect = arrayOf("", "", "", "", "", "")
   var mData =MultiStyleItem()

    val mCountPerPageForPerson = 30
    var mPageNumberForPerson = 1
    val mCountPerPageForTeam = 30
    var mPageNumberForTeam = 1
    val mCountPerPageForLease = 30
    var mPageNumberForLease = 1
    val mCountPerPageForThird = 30
    var mPageNumberForThird = 1
    var mIsLastPageForPerson = false
    var mIsLastPageForTeam = false
    var mIsLastPageForLease = false
    var mIsLastPageForThird = false
    var mIsLoadingPerson = false
    var mIsLoadingTeam = false
    var mIsLoadingLease = false
    var mIsLoadingThird = false

    //搜索栏三个变量
    var supplySite:String=""
    var supplyVariety:String=""
    var supplyElect:String=""
    //车辆租赁特殊变量
    var supplyVehicle:String=""
    //
    var theflag=0
    lateinit var mView: View

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    retainInstance = true
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      mView = inflater.inflate(R.layout.demand, container, false)
      mView.demand_swipe_refresh.setColorSchemeColors(Color.rgb(47,223,189))
      mView.tv_demand_type_select.setText("选择供应类别")
      val Option1Items= listOf("普工","负责人","工程师","设计员","特种作业","专业操作","勘测员","驾驶员","九大员","注册类","其他") as MutableList<String>
      val Option2Items= listOf(listOf("普工"),
          listOf("配网项目负责人", "配网班组负责人", "主网项目负责人", "主网基础负责人", "主网组塔负责人","主网架线负责人","变电项目负责人","变电土建负责人","变电一次负责人","变电二次负责人","变电调试负责人"),
          listOf("助理工程师", "中级工程师", "高级工程师"),
          listOf("主网", "配网", "变电"),
          listOf("低压电工作业","高压电工作业","电力电缆作业","继电保护作业","电气试验作业","融化焊接与热切割作业","登高架设作业"),
          listOf("压接操作","机动绞磨操作","牵张设备操作","起重机械操作","钢筋工","混凝土工","木工","模板工","油漆工","砌筑工","通风工","打桩工","架子工"),
          listOf("测量工"), listOf("驾驶证A1","驾驶证A2","驾驶证A3","驾驶证B1","驾驶证B2","驾驶证C1","驾驶证C2","驾驶证C3","驾驶证D","驾驶证E"),
          listOf("施工员","安全员","质量员","材料员","资料员","预算员","标准员","机械员","劳务员"), listOf("造价工程师","一级建造师","二级建造师","安全工程师","电气工程师"),
          listOf("其他")) as MutableList<MutableList<String>>
      mView.tv_demand_type_select.movementMethod= ScrollingMovementMethod.getInstance()
      mView.tv_demand_site_select.movementMethod= ScrollingMovementMethod.getInstance()
      mView.tv_demand_elect_standard_select.movementMethod= ScrollingMovementMethod.getInstance()
      mView.tv_demand_type_select.setOnClickListener {
          initDemandPerson(mView,Option1Items,Option2Items,1)
      }
      mView.tv_demand_site_select.visibility=View.VISIBLE
      mView.tv_demand_site_select.setOnClickListener {
          initSite(mView,4)
      }
      mView.tv_demand_site_clear.setOnClickListener {
          mView.tv_demand_site_select.text="选择地点"
          mView.tv_demand_site_clear.visibility=View.GONE
          supplySite=""
          for(i in 0 until mData.placeCheckBoolenArray.size){
              mData.placeCheckBoolenArray[i]=false
          }
          keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","kind")
          valueforsupply= arrayListOf(mPageNumberForPerson,mCountPerPageForPerson,
              supplySite,supplyVariety) as MutableList<String>
          mPageNumberForPerson = 1
          mIsLastPageForPerson = false
          mIsLoadingPerson=false
          mPersonalIssueAdapter!!.notifyData()
          supplyPerson(mView)
      }
      mView.tv_demand_type_clear.setOnClickListener {
          mView.tv_demand_type_select.text="选择供应类别"
          mView.tv_demand_type_clear.visibility=View.GONE
          supplyVariety=""
          keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","kind")
          valueforsupply= arrayListOf(mPageNumberForPerson,mCountPerPageForPerson,
              supplySite,supplyVariety) as MutableList<String>
          mPageNumberForPerson = 1
          mIsLastPageForPerson = false
          mIsLoadingPerson=false
          mPersonalIssueAdapter!!.notifyData()
          supplyPerson(mView)
      }
      return mView
  }
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
      mPersonAdapter = ListAdapterForDemand(activity!!)
      mPersonalIssueAdapter = ListAdapterForSupply(activity!!)
      keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","kind")
      valueforsupply= arrayListOf(mPageNumberForPerson,mCountPerPageForPerson,
          supplySite,supplyVariety) as MutableList<String>
      supplyPerson(view)
      select(view)
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
               supplySite=""
               supplyVariety=""
               supplyElect=""
               selectContentElect = arrayOf("", "", "", "", "", "")
               supplyVehicle=""
              mPageNumberForPerson = 1
              mIsLastPageForPerson = false
              mIsLoadingPerson=false
              mPersonalIssueAdapter!!.notifyData()
              keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","kind")
              valueforsupply= arrayListOf(mPageNumberForPerson,mCountPerPageForPerson,
                  supplySite,supplyVariety) as MutableList<String>
              supplyPerson(view)
              view.tv_demand_site_select.text="选择地点"
              view.tv_demand_type_select.text="选择供应类别"
              view.tv_demand_site_clear.visibility=View.GONE
              view.tv_demand_type_clear.visibility=View.GONE
              view.tv_demand_elect_standars_clear.visibility=View.GONE
              view.tv_demand_elect_standard_select.visibility=View.GONE
              view.tv_demand_site_select.visibility=View.VISIBLE
              view.tv_demand_type_select.visibility=View.VISIBLE
              val Option1Items= listOf("普工","负责人","工程师","设计员","特种作业","专业操作","勘测员","驾驶员","九大员","注册类","其他") as MutableList<String>
              val Option2Items= listOf(listOf("普工"),
                  listOf("配网项目负责人", "配网班组负责人", "主网项目负责人", "主网基础负责人", "主网组塔负责人","主网架线负责人","变电项目负责人","变电土建负责人","变电一次负责人","变电二次负责人","变电调试负责人"),
                  listOf("助理工程师", "中级工程师", "高级工程师"),
                  listOf("主网", "配网", "变电"),
                  listOf("低压电工作业","高压电工作业","电力电缆作业","继电保护作业","电气试验作业","融化焊接与热切割作业","登高架设作业"),
                  listOf("压接操作","机动绞磨操作","牵张设备操作","起重机械操作","钢筋工","混凝土工","木工","模板工","油漆工","砌筑工","通风工","打桩工","架子工"),
                  listOf("测量工"), listOf("驾驶证A1","驾驶证A2","驾驶证A3","驾驶证B1","驾驶证B2","驾驶证C1","驾驶证C2","驾驶证C3","驾驶证D","驾驶证E"),
                  listOf("施工员","安全员","质量员","材料员","资料员","预算员","标准员","机械员","劳务员"), listOf("造价工程师","一级建造师","二级建造师","安全工程师","电气工程师"),
                  listOf("其他")) as MutableList<MutableList<String>>
              view.tv_demand_type_select.setOnClickListener {
                  initDemandPerson(view,Option1Items,Option2Items,1)
              }
              view.tv_demand_site_select.setOnClickListener {
                  initSite(view,4)
              }
              view.tv_demand_site_clear.setOnClickListener {
                  view.tv_demand_site_select.text="选择地点"
                  view.tv_demand_site_clear.visibility=View.GONE
                  supplySite=""
                  for(i in 0 until mData.placeCheckBoolenArray.size){
                      mData.placeCheckBoolenArray[i]=false
                  }
                  keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","kind")
                  valueforsupply= arrayListOf(mPageNumberForPerson,mCountPerPageForPerson,
                      supplySite,supplyVariety) as MutableList<String>
                  mPageNumberForPerson = 1
                  mIsLastPageForPerson = false
                  mIsLoadingPerson=false
                  mPersonalIssueAdapter!!.notifyData()
                  supplyPerson(view)
              }

              view.tv_demand_type_clear.setOnClickListener {
                  view.tv_demand_type_select.text="选择供应类别"
                  view.tv_demand_type_clear.visibility=View.GONE
                  supplyVariety=""
                  keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","kind")
                  valueforsupply= arrayListOf(mPageNumberForPerson,mCountPerPageForPerson,
                      supplySite,supplyVariety) as MutableList<String>
                  mPageNumberForPerson = 1
                  mIsLastPageForPerson = false
                  mIsLoadingPerson=false
                  mPersonalIssueAdapter!!.notifyData()
                  supplyPerson(view)
              }
          }
          "团队服务"->{
              supplySite=""
              supplyVariety=""
              supplyElect=""
              selectContentElect = arrayOf("", "", "", "", "", "")
              supplyVehicle=""
              mPageNumberForTeam = 1
              mIsLastPageForTeam = false
              mIsLoadingTeam=false
              mPersonAdapter!!.notifyMovie()
              view.tv_demand_site_select.setOnClickListener {
                  initSite(view, 2)
              }
              keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","name","v1","v2","v3","v4","v5","v6")
              valueforsupply= arrayListOf(mPageNumberForTeam,mCountPerPageForTeam,supplySite,supplyVariety,
                  selectContentElect[0],selectContentElect[1],selectContentElect[2],selectContentElect[3],
                  selectContentElect[4],selectContentElect[5]) as MutableList<String>
              supplyTeam(view)
              view.tv_demand_site_select.text="选择地点"
              view.tv_demand_type_select.text="选择供应类别"
              view.tv_demand_elect_standard_select.text="选择电压等级"
              view.tv_demand_site_clear.visibility=View.GONE
              view.tv_demand_type_clear.visibility=View.GONE
              view.tv_demand_elect_standars_clear.visibility=View.GONE
              view.tv_demand_elect_standard_select.visibility=View.VISIBLE
              view.tv_demand_type_select.visibility=View.VISIBLE
              view.tv_demand_site_select.visibility=View.VISIBLE
              val Option1Items= listOf("变电施工队","主网施工队","配网施工队","测量设计","马帮运输","桩基服务","非开挖顶拉管作业","试验调试","跨越架","运行维护") as MutableList<String>
              val Option2Items= listOf("35KV","110KV","220KV","500KV")
              view.tv_demand_type_select.setOnClickListener {
                  initDemandTeam(view,Option1Items,1)
              }
              view.tv_demand_elect_standard_select.setOnClickListener {
                  initElectDialog(view, Option2Items as MutableList<String>)
              }
              //清空电压等级筛选
              view.tv_demand_elect_standars_clear.setOnClickListener {
                  view.tv_demand_elect_standard_select.text="选择电压等级"
                  view.tv_demand_elect_standars_clear.visibility=View.GONE
                  checkedItems= arrayOf(false,false,false,false,false,false)
                  selectContentElect = arrayOf("", "", "", "", "", "")
                  supplyElect=""
                  keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","name","v1","v2","v3","v4","v5","v6")
                  valueforsupply= arrayListOf(mPageNumberForTeam,mCountPerPageForTeam,supplySite,supplyVariety,
                      selectContentElect[0],selectContentElect[1],selectContentElect[2],selectContentElect[3],
                      selectContentElect[4],selectContentElect[5]) as MutableList<String>
                  mPageNumberForTeam = 1
                  mIsLastPageForTeam = false
                  mIsLoadingTeam=false
                  mPersonAdapter!!.notifyMovie()
                  supplyTeam(view)
              }
              view.tv_demand_site_clear.setOnClickListener {
                  view.tv_demand_site_select.text="选择地点"
                  view.tv_demand_site_clear.visibility=View.GONE
                  supplySite=""
                  for(i in 0 until mData.placeCheckBoolenArray.size){
                      mData.placeCheckBoolenArray[i]=false
                  }
                  keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","name","v1","v2","v3","v4","v5","v6")
                  valueforsupply= arrayListOf(mPageNumberForTeam,mCountPerPageForTeam,supplySite,supplyVariety,
                      selectContentElect[0],selectContentElect[1],selectContentElect[2],selectContentElect[3],
                      selectContentElect[4],selectContentElect[5]) as MutableList<String>
                  mPageNumberForTeam = 1
                  mIsLastPageForTeam = false
                  mIsLoadingTeam=false
                  mPersonAdapter!!.notifyMovie()
                  supplyTeam(view)
              }
              view.tv_demand_type_clear.setOnClickListener {
                  view.tv_demand_type_select.text="选择供应类别"
                  view.tv_demand_type_clear.visibility=View.GONE
                  supplyVariety=""
                  keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","name","v1","v2","v3","v4","v5","v6")
                  valueforsupply= arrayListOf(mPageNumberForTeam,mCountPerPageForTeam,supplySite,supplyVariety,
                      selectContentElect[0],selectContentElect[1],selectContentElect[2],selectContentElect[3],
                      selectContentElect[4],selectContentElect[5]) as MutableList<String>
                  mPageNumberForTeam = 1
                  mIsLastPageForTeam = false
                  mIsLoadingTeam=false
                  mPersonAdapter!!.notifyMovie()
                  supplyTeam(view)
              }
          }
          "租赁服务"->{
              supplySite=""
              supplyVariety=""
              supplyElect=""
              selectContentElect = arrayOf("", "", "", "", "", "")
              supplyVehicle=""
              mPageNumberForLease = 1
              mIsLastPageForLease = false
              mIsLoadingLease=false
              mPersonAdapter!!.notifyMovie()
              view.tv_demand_site_select.setOnClickListener {
                  initSite(view, 3)
              }
              keyforsupply= arrayListOf("page","number","issuerBelongSite","type","carType")
              valueforsupply= arrayListOf(mPageNumberForLease,mCountPerPageForLease,supplySite,supplyVariety,
                  supplyVehicle) as MutableList<String>
              supplyLease(view)
              view.tv_demand_site_select.text="选择地点"
              view.tv_demand_type_select.text="选择供应类别"
              view.tv_demand_site_clear.visibility=View.GONE
              view.tv_demand_type_clear.visibility=View.GONE
              view.tv_demand_elect_standars_clear.visibility=View.GONE
              view.tv_demand_elect_standard_select.visibility=View.GONE
              view.tv_demand_type_select.visibility=View.VISIBLE
              view.tv_demand_site_select.visibility=View.VISIBLE
              val Option1Items= listOf("车辆租赁","工器具租赁","机械租赁","设备租赁") as MutableList<String>
              val Option2Items= listOf(listOf("皮卡车","单排座工程车","双排座工程车","多排座工程车","载重自卸车","载重平板车","随吊车"),
                  listOf(""), listOf(""), listOf("")) as MutableList<MutableList<String>>
              view.tv_demand_type_select.setOnClickListener {
                  initDemandPerson(view,Option1Items, Option2Items,2)
              }
              view.tv_demand_site_clear.setOnClickListener {
                  view.tv_demand_site_select.text="选择地点"
                  view.tv_demand_site_clear.visibility=View.GONE
                  supplySite=""
                  for(i in 0 until mData.placeCheckBoolenArray.size){
                      mData.placeCheckBoolenArray[i]=false
                  }
                  keyforsupply= arrayListOf("page","number","issuerBelongSite","type","carType")
                  valueforsupply= arrayListOf(mPageNumberForLease,mCountPerPageForLease,supplySite,supplyVariety,
                      supplyVehicle) as MutableList<String>
                  mPageNumberForLease = 1
                  mIsLastPageForLease = false
                  mIsLoadingLease=false
                  mPersonAdapter!!.notifyMovie()
                  supplyLease(view)
              }
              view.tv_demand_type_clear.setOnClickListener {
                  view.tv_demand_type_select.text="选择供应类别"
                  view.tv_demand_type_clear.visibility=View.GONE
                  supplyVariety=""
                  supplyVehicle=""
                  keyforsupply= arrayListOf("page","number","issuerBelongSite","type","carType")
                  valueforsupply= arrayListOf(mPageNumberForLease,mCountPerPageForLease,supplySite,supplyVariety,
                      supplyVehicle) as MutableList<String>
                  mPageNumberForLease = 1
                  mIsLastPageForLease = false
                  mIsLoadingLease=false
                  mPersonAdapter!!.notifyMovie()
                  supplyLease(view)
              }
          }
          "三方服务"->{
              supplySite=""
              supplyVariety=""
              supplyElect=""
              selectContentElect = arrayOf("", "", "", "", "", "")
              supplyVehicle=""
              mPageNumberForThird = 1
              mIsLastPageForThird = false
              mIsLoadingThird=false
              mPersonAdapter!!.notifyMovie()
              view.tv_demand_site_select.setOnClickListener {
                  initSite(view, 1)
              }
              keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","serveType")
              valueforsupply= arrayListOf(mPageNumberForThird,mCountPerPageForThird,supplySite,
                  supplyVariety) as MutableList<String>
              supplyThird(view)
              view.tv_demand_type_select.text="选择供应类别"
              view.tv_demand_site_clear.visibility=View.GONE
              view.tv_demand_type_clear.visibility=View.GONE
              view.tv_demand_elect_standars_clear.visibility=View.GONE
              view.tv_demand_elect_standard_select.visibility=View.GONE
              view.tv_demand_site_select.visibility=View.VISIBLE
              view.tv_demand_type_select.visibility=View.VISIBLE
              val Option1Items= listOf("培训办证","财务记账","代办资格","标书服务","法律咨询","软件服务","资质合作","其他") as MutableList<String>
              view.tv_demand_type_select.setOnClickListener {
                  initDemandTeam(view,Option1Items,2)
              }
              view.tv_demand_type_clear.setOnClickListener {
                  view.tv_demand_type_select.text="选择供应类别"
                  view.tv_demand_type_clear.visibility=View.GONE
                  supplyVariety=""
                  keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","serveType")
                  valueforsupply= arrayListOf(mPageNumberForThird,mCountPerPageForThird,supplySite,
                      supplyVariety) as MutableList<String>
                  mPageNumberForThird = 1
                  mIsLastPageForThird = false
                  mIsLoadingThird=false
                  mPersonAdapter!!.notifyMovie()
                  supplyThird(view)
              }
              view.tv_demand_site_clear.setOnClickListener {
                  view.tv_demand_site_select.text="选择地点"
                  view.tv_demand_site_clear.visibility=View.GONE
                  supplySite=""
                  for(i in 0 until mData.placeCheckBoolenArray.size){
                      mData.placeCheckBoolenArray[i]=false
                  }
                  keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","serveType")
                  valueforsupply= arrayListOf(mPageNumberForThird,mCountPerPageForThird,supplySite,
                      supplyVariety) as MutableList<String>
                  mPageNumberForThird = 1
                  mIsLastPageForThird = false
                  mIsLoadingThird=false
                  mPersonAdapter!!.notifyMovie()
                  supplyThird(view)
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

  //个人劳务
  private fun supplyPerson(view:View) {
      loadSupplyPersonData()
      theflag=1
      view.tv_demand_content.apply {
          layoutManager = LinearLayoutManager(activity)
          adapter = mPersonalIssueAdapter
      }
      view.tv_demand_content.addOnScrollListener(object : RecyclerView.OnScrollListener() {
          override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
              super.onScrolled(recyclerView, dx, dy)

              var m = view.tv_demand_content.layoutManager as LinearLayoutManager
              if (m.findLastVisibleItemPosition() == m.itemCount - 1) {
                  selectScroll(theflag,view)
              }
          }
      })
      view.demand_swipe_refresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
          override fun onRefresh() {
              mView.demand_swipe_refresh.isRefreshing=false
              mPageNumberForPerson=1
              mIsLastPageForPerson=false
              mIsLoadingPerson=false
              mPersonalIssueAdapter!!.notifyData()
              valueforsupply= arrayListOf(mPageNumberForPerson,mCountPerPageForPerson,supplySite,
                  supplyVariety) as MutableList<String>
              selectScroll(theflag,view)
          }
      })
  }
    //团队服务
    private fun supplyTeam(view:View) {
        loadSupplyTeamData()
        theflag=2
        view.tv_demand_content.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mPersonAdapter
        }
        view.tv_demand_content.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var m = view.tv_demand_content.layoutManager as LinearLayoutManager
                if (m.findLastVisibleItemPosition() == m.itemCount - 1) {
                    selectScroll(theflag,view)
                }
            }
        })
        view.demand_swipe_refresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                mView.demand_swipe_refresh.isRefreshing=false
                mPageNumberForTeam=1
                mIsLastPageForTeam=false
                mIsLoadingTeam=false
                mPersonAdapter!!.notifyMovie()
                valueforsupply= arrayListOf(mPageNumberForTeam,mCountPerPageForTeam,supplySite,supplyVariety,
                    selectContentElect[0],selectContentElect[1],selectContentElect[2],selectContentElect[3],
                    selectContentElect[4],selectContentElect[5]) as MutableList<String>
                selectScroll(theflag,view)
            }
        })
    }
    //租赁服务
    private fun supplyLease(view:View) {
        loadSupplyLeaseData()
        theflag=3
        view.tv_demand_content.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mPersonAdapter
        }
        view.tv_demand_content.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var m = view.tv_demand_content.layoutManager as LinearLayoutManager
                if (m.findLastVisibleItemPosition() == m.itemCount - 1) {
                    selectScroll(theflag,view)
                }
            }
        })
        view.demand_swipe_refresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                mView.demand_swipe_refresh.isRefreshing=false
                mPageNumberForLease=1
                mIsLastPageForLease=false
                mIsLoadingLease=false
                mPersonAdapter!!.notifyMovie()
                valueforsupply= arrayListOf(mPageNumberForLease,mCountPerPageForLease,supplySite,supplyVariety,
                    supplyVehicle) as MutableList<String>
                selectScroll(theflag,view)
            }
        })
    }
    //三方服务
    private fun supplyThird(view:View) {
        loadSupplyThirdData()
        theflag=4
        view.tv_demand_content.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mPersonAdapter
        }
        view.tv_demand_content.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var m = view.tv_demand_content.layoutManager as LinearLayoutManager
                if (m.findLastVisibleItemPosition() == m.itemCount - 1) {
                    selectScroll(theflag,view)
                }
            }
        })
        view.demand_swipe_refresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                mView.demand_swipe_refresh.isRefreshing=false
                mPageNumberForThird=1
                mIsLastPageForThird=false
                mIsLoadingThird=false
                mPersonAdapter!!.notifyMovie()
                valueforsupply= arrayListOf(mPageNumberForThird,mCountPerPageForThird,supplySite,
                    supplyVariety) as MutableList<String>
                selectScroll(theflag,view)
            }
        })
    }
    @Synchronized fun setLoadingPerson() : Boolean {
        if (mIsLoadingPerson)
            return false
        mIsLoadingPerson = true
        return true
    }

    @Synchronized fun setLoadingTeam() : Boolean {
        if (mIsLoadingTeam)
            return false
        mIsLoadingTeam = true
        return true
    }
    @Synchronized fun setLoadingLease() : Boolean {
        if (mIsLoadingLease)
            return false
        mIsLoadingLease = true
        return true
    }
    @Synchronized fun setLoadingThird() : Boolean {
        if (mIsLoadingThird)
            return false
        mIsLoadingThird = true
        return true
    }

    fun loadSupplyPersonData() {
        if (mIsLastPageForPerson)
            return
        if (!setLoadingPerson())
            return

        _loadSupplyPersonData()
    }
    fun loadSupplyTeamData() {
        if (mIsLastPageForTeam)
            return
        if (!setLoadingTeam())
            return

        _loadSupplyTeamData()
    }
    fun loadSupplyLeaseData() {
        if (mIsLastPageForLease)
            return
        if (!setLoadingLease())
            return

        _loadSupplyLeaseData()
    }
    fun loadSupplyThirdData() {
        if (mIsLastPageForThird)
            return
        if (!setLoadingThird())
            return

        _loadSupplyThirdData()
    }

  fun _loadSupplyPersonData() {
    val result= Observable.create<RequestBody> {
      //建立网络请求体 (类型，内容)
      val jsonObject = JSONObject()
      for (i in 0 until keyforsupply.size) {
        jsonObject.put(keyforsupply[i], valueforsupply[i])
      }
      val requestBody= RequestBody.create(MediaType.parse("application/json"),jsonObject.toString())
      it.onNext(requestBody)
    }
      .subscribe {
          val loadingDialog = LoadingDialog(mView.context, "正在加载...", R.mipmap.ic_dialog_loading)
          loadingDialog.show()
        val result =
          getSupplyPerson(it, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                  val code = it.code
                  val desc=it.desc
                  loadingDialog.dismiss()
                  mIsLoadingPerson = false
                  if(code=="200"&&desc=="OK"){
                      val data = it.message.data
                      for (j in data) {
                          var temp1=""
                          var temp2=""
                          var temp3=""
                          var temp4=""
                          temp1 = if(j.contact==null) {
                              " "
                          } else{ j.contact }
                          when {
                              j.sex=="1" -> temp2 ="男"
                              j.sex=="0" -> temp2="女"
                              j.sex==null -> temp2=" "
                          }
                          temp3 = if(j.id== null) { " " }
                          else { j.id }
                          temp4 =if(j.issuerWorkerKind == null) { " " }
                          else{ j.issuerWorkerKind }
                          mPersonalIssueAdapter?.addData(PersonalIssue(temp1,temp2,temp4,temp3,"个人劳务"))
                      }
                      if (data.size < mCountPerPageForPerson)
                          mIsLastPageForPerson = true
                      else
                          mPageNumberForPerson ++
                  }else{
                      ToastHelper.mToast(context!!,"加载失败")
                  }

                  }, {
                   loadingDialog.dismiss()
                  if(mIsLoadingPerson){
                      ToastHelper.mToast(mView.context,"无数据")
                  }
                    it.printStackTrace()
                   })
      }
  }

  fun _loadSupplyTeamData() {

    val result= Observable.create<RequestBody> {
      //建立网络请求体 (类型，内容)
      val jsonObject = JSONObject()
      for (i in 0 until keyforsupply.size) {
        jsonObject.put(keyforsupply[i], valueforsupply[i])
      }
      val requestBody= RequestBody.create(MediaType.parse("application/json"),jsonObject.toString())
      it.onNext(requestBody)
    }
      .subscribe {
          val loadingDialog = LoadingDialog(mView.context, "正在加载...", R.mipmap.ic_dialog_loading)
          loadingDialog.show()
        val result =
          getSupplyTeam(it, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                  val code = it.code
                  loadingDialog.dismiss()
                  mIsLoadingTeam = false
                  if(code=="200"){
                      val data = it.message.data
                      for (j in data) {
                          var temp1=""
                          var temp2=""
                          var temp3=""
                          temp1 = if(j.name==null) { " " } else{ j.name }
                          temp2 = if(j.issuerBelongSite== null) { " " } else { j.issuerBelongSite }
                          temp3 =if(j.id == null) { " " } else{ j.id }
                          mPersonAdapter?.addMovie(Movie(temp1,temp2,temp3,"供应团队服务"))
                      }
                      if (data.size < mCountPerPageForTeam)
                          mIsLastPageForTeam = true
                      else
                          mPageNumberForTeam ++
                  }else if(code=="400" && code=="没有该数据"){
                      ToastHelper.mToast(context!!,"数据为空")
                  }else{
                      ToastHelper.mToast(context!!,"加载失败")
                  }

                    }, {
                         loadingDialog.dismiss()
                  if(mIsLoadingTeam){
                      ToastHelper.mToast(mView.context,"无数据")
                  }
                        it.printStackTrace()
                    })
      }
  }
  fun _loadSupplyLeaseData() {
    val result= Observable.create<RequestBody> {
      //建立网络请求体 (类型，内容)
      val jsonObject = JSONObject()
      for (i in 0 until keyforsupply.size) {
        jsonObject.put(keyforsupply[i],valueforsupply[i])
      }
      val requestBody= RequestBody.create(MediaType.parse("application/json"),jsonObject.toString())
      it.onNext(requestBody)
    }
      .subscribe {
          val loadingDialog = LoadingDialog(mView.context, "正在加载...", R.mipmap.ic_dialog_loading)
          loadingDialog.show()
        val result =
          getSupplyLease(it, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(
            Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                  val code = it.code
                  loadingDialog.dismiss()
                  mIsLoadingLease = false
                  if(code=="200"){
                      val data = it.message.data
                      for (j in data) {
                          if(j.type=="车辆租赁")
                          {
                              var temp1=""
                              var temp2=""
                              var temp3=""
                              temp1 = if(j.carType==null) { " " } else{ j.carType }
                              temp2 = if(j.issuerBelongSite== null) { " " } else { j.issuerBelongSite }
                              temp3 =if(j.leaseId == null) { " " } else{ j.leaseId }
                              mPersonAdapter?.addMovie(Movie(temp1,temp2,temp3,"供应租赁车辆"))
                          }
                          else
                          {
                              var temp1=""
                              var temp2=""
                              var temp3=""
                              temp1 = if(j.type==null) { " " } else{ j.type }
                              temp2 = if(j.validTime== null) { " " } else { j.validTime }
                              temp3 =if(j.leaseId == null) { " " } else{ j.leaseId }
                              mPersonAdapter?.addMovie(Movie(temp1,temp2,temp3,"供应租赁服务"))
                          }
                      }
                      if (data.size < mCountPerPageForLease)
                          mIsLastPageForLease = true
                      else
                          mPageNumberForLease ++
                  }else if(code=="400" && code=="没有该数据"){
                      ToastHelper.mToast(context!!,"数据为空")
                  }else{
                      ToastHelper.mToast(context!!,"加载失败")
                  }
            }, {
                  loadingDialog.dismiss()
                  if(mIsLoadingLease){
                      ToastHelper.mToast(mView.context,"无数据")
                  }
                it.printStackTrace()
            })
      }
  }
  fun _loadSupplyThirdData() {
    val result= Observable.create<RequestBody> {
      //建立网络请求体 (类型，内容)
      val jsonObject = JSONObject()
      for (i in 0 until keyforsupply.size) {
        jsonObject.put(keyforsupply[i], valueforsupply[i])
      }
      val requestBody= RequestBody.create(MediaType.parse("application/json"),jsonObject.toString())
      it.onNext(requestBody)
    }
      .subscribe {
          val loadingDialog = LoadingDialog(mView.context, "正在加载...", R.mipmap.ic_dialog_loading)
          loadingDialog.show()
        val result =
          getSupplyThirdParty(it, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(
            Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                  val code = it.code
                  loadingDialog.dismiss()
                  mIsLoadingThird = false
                  if(code=="200"){
                      val data = it.message.data
                      for (j in data) {
                          if(j.serveType=="资质合作")
                          {
                              var temp1=""
                              var temp2=""
                              var temp3=""
                              temp1 = if(j.serveType==null) { " " } else{ j.serveType }
                              temp2 = if(j.companyName== null) { " " } else { j.companyName }
                              temp3 =if(j.id == null) { " " } else{ j.id }
                              mPersonAdapter?.addMovie(Movie(temp1,temp2,temp3,"供应三方服务"))
                          }
                          else{
                              var temp1=""
                              var temp2=""
                              var temp3=""
                              temp1 = if(j.serveType==null) { " " } else{ j.serveType }
                              temp2 = if(j.validTime== null) { " " } else { j.validTime }
                              temp3 =if(j.id == null) { " " } else{ j.id }
                              mPersonAdapter?.addMovie(Movie(temp1,temp2,temp3,"供应三方服务"))
                          }
                      }
                      if(data.size==0)
                      {
                          ToastHelper.mToast(mView.context,"无数据")
                      }
                      if (data.size < mCountPerPageForThird)
                          mIsLastPageForThird = true
                      else
                          mPageNumberForThird ++
                  }else if(code=="400" && code=="没有该数据"){
                      ToastHelper.mToast(context!!,"数据为空")
                  }else{
                      ToastHelper.mToast(context!!,"加载失败")
                  }

            }, {
                  loadingDialog.dismiss()
                  if(mIsLoadingThird){
                      ToastHelper.mToast(mView.context,"无数据")
                  }
              it.printStackTrace()
            })
      }
  }

    //地点选择弹窗
    fun initSite(view:View,flag:Int) {
        var mHandler: Handler = Handler(Handler.Callback {
            when (it.what) {
                RecyclerviewAdapter.MESSAGE_SELECT_CHECK_OK -> {
                    mPageNumberForPerson=1
                    mPageNumberForTeam=1
                    mPageNumberForLease=1
                    mPageNumberForThird=1
                    supplySite=it.data.getString("selectContent")
                    view.tv_demand_site_select.text = supplySite
                    view.tv_demand_site_clear.visibility = View.VISIBLE
                    when (flag) {
                        1 -> {//三方服务
                            keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","serveType")
                            valueforsupply= arrayListOf(mPageNumberForThird,mCountPerPageForThird,
                                supplySite,supplyVariety) as MutableList<String>
                            mPageNumberForThird = 1
                            mIsLastPageForThird = false
                            mPersonAdapter!!.notifyMovie()
                            supplyThird(view)
                        }
                        2 -> {//团队服务
                            keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","name","v1","v2","v3","v4","v5","v6")
                            valueforsupply= arrayListOf(mPageNumberForTeam,mCountPerPageForTeam,supplySite,supplyVariety,
                                selectContentElect[0],selectContentElect[1],selectContentElect[2],selectContentElect[3],
                                selectContentElect[4],selectContentElect[5]) as MutableList<String>
                            mPageNumberForTeam = 1
                            mIsLastPageForTeam = false
                            mPersonAdapter!!.notifyMovie()
                            supplyTeam(view)
                        }
                        3 -> {//租赁服务
                            keyforsupply= arrayListOf("page","number","issuerBelongSite","type","carType")
                            valueforsupply= arrayListOf(mPageNumberForLease,mCountPerPageForLease,supplySite,supplyVariety,
                                supplyVehicle) as MutableList<String>
                            mPageNumberForLease = 1
                            mIsLastPageForLease = false
                            mPersonAdapter!!.notifyMovie()
                            supplyLease(view)
                        }
                        4->{//个人劳务
                            keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","kind")
                            valueforsupply= arrayListOf(mPageNumberForPerson,mCountPerPageForPerson,
                                supplySite,supplyVariety) as MutableList<String>
                            mPageNumberForPerson = 1
                            mIsLastPageForPerson = false
                            mPersonalIssueAdapter!!.notifyData()
                            supplyPerson(view)
                        }
                    }
                    false
                }
                else -> {
                    false
                }
            }
        })
        val selectDialog = CustomDialog(
            CustomDialog.Options.SELECT_CHECK_DIALOG,
            view.context,
            mData.placeCheckArray,
            supplySite,
            mHandler
        ).dialog
        selectDialog.show()
    }
    //个人劳务 租赁弹窗
    fun initDemandPerson(view:View,Option1Items: MutableList<String>,Option2Items: MutableList<MutableList<String>>,flag: Int){
        var mHandler:Handler= Handler(Handler.Callback {
            when(it.what)
            {
                RecyclerviewAdapter.MESSAGE_SELECT_OK ->{
                    mPageNumberForPerson=1
                    mPageNumberForLease=1
                    val selectContent=it.data.getString("selectContent")
                    view.tv_demand_type_select.text=selectContent
                    view.tv_demand_type_clear.visibility=View.VISIBLE
                    if(flag==1){//个人劳务
                        val  str=selectContent.split(" ")
                        var flagfortype=0
                        for(temp in str) {
                            flagfortype += 1
                            if (flagfortype==1) {
                                supplyVariety=""
                            } else if(flagfortype==2) {
                                supplyVariety=temp
                            }
                        }
                        keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","kind")
                        valueforsupply= arrayListOf(mPageNumberForPerson,mCountPerPageForPerson,supplySite,supplyVariety) as MutableList<String>
                        mPageNumberForPerson = 1
                        mIsLastPageForPerson = false
                        mPersonalIssueAdapter!!.notifyData()
                        supplyPerson(view)
                    }
                    else if(flag==2)//供应租赁
                    {
                            val  strfortype=selectContent.split(" ")
                            var flagfortype=0
                            for(temp in strfortype) {
                                flagfortype += 1
                                if(flagfortype==1)
                                    supplyVariety=temp
                                else if(flagfortype==2) {
                                    supplyVehicle = temp
                                }
                            }
                        keyforsupply= arrayListOf("page","number","issuerBelongSite","type","carType")
                        valueforsupply= arrayListOf(mPageNumberForLease,mCountPerPageForLease,supplySite,supplyVariety,
                            supplyVehicle) as MutableList<String>
                        mPageNumberForLease = 1
                        mIsLastPageForLease = false
                        mPersonAdapter!!.notifyMovie()
                        supplyLease(view)
                    }
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
    //供应团队 三方服务弹窗 （需求类别）
    fun initDemandTeam(view:View,Option1Items:MutableList<String>,flag: Int){
        var mHandler:Handler= Handler(Handler.Callback {
            when(it.what)
            {
                RecyclerviewAdapter.MESSAGE_SELECT_OK ->{
                    mPageNumberForTeam=1
                    mPageNumberForThird=1
                    supplyVariety=it.data.getString("selectContent")
                    view.tv_demand_type_select.text=supplyVariety
                    view.tv_demand_type_clear.visibility=View.VISIBLE
                    if(flag==1){//供应团队
                        keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","name","v1","v2","v3","v4","v5","v6")
                        valueforsupply= arrayListOf(mPageNumberForTeam,mCountPerPageForTeam,supplySite,supplyVariety,
                            selectContentElect[0],selectContentElect[1],selectContentElect[2],selectContentElect[3],
                            selectContentElect[4],selectContentElect[5]) as MutableList<String>
                        mPageNumberForTeam = 1
                        mIsLastPageForTeam = false
                        mPersonAdapter!!.notifyMovie()
                        supplyTeam(view)
                    }
                    else if(flag==2)//供应三方
                    {
                        keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","serveType")
                        valueforsupply= arrayListOf(mPageNumberForThird,mCountPerPageForThird,supplySite,supplyVariety) as MutableList<String>
                        mPageNumberForThird = 1
                        mIsLastPageForThird = false
                        mPersonAdapter!!.notifyMovie()
                        supplyThird(view)
                    }
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
        mPageNumberForTeam=1
        var dialog = AlertDialog.Builder(this.context)
            .setMultiChoiceItems(Option1Items.toTypedArray() as Array<out CharSequence>,checkedItems.toBooleanArray()) { dialog: DialogInterface?, which: Int, isChecked: Boolean ->
                checkedItems[which]=isChecked
            }
            .setPositiveButton("确定") { dialog, which ->
                supplyElect=""
                for (i in checkedItems.indices) {
                    if (checkedItems[i]) {
                        selectContentElect[i] = Option1Items[i]
                        if(supplyElect!="")
                            supplyElect+=" "
                        supplyElect+=Option1Items[i]
                    }
                }
                view.tv_demand_elect_standard_select.text=supplyElect
                view.tv_demand_elect_standars_clear.visibility=View.VISIBLE
                keyforsupply= arrayListOf("page","pageSize","issuerBelongSite","name","v1","v2","v3","v4","v5","v6")
                valueforsupply= arrayListOf(mPageNumberForTeam,mCountPerPageForTeam,supplySite,supplyVariety,
                    selectContentElect[0],selectContentElect[1],selectContentElect[2],selectContentElect[3],
                    selectContentElect[4],selectContentElect[5]) as MutableList<String>
                mPageNumberForTeam = 1
                mIsLastPageForTeam = false
                mPersonAdapter!!.notifyMovie()
                supplyTeam(view)
                dialog.dismiss()
            }.create()
        dialog.show()
    }
    fun selectScroll(theflag:Int,view:View){
        when(theflag)//number
        {
            1->{
                valueforsupply= arrayListOf(mPageNumberForPerson,mCountPerPageForPerson,supplySite,supplyVariety) as MutableList<String>
                loadSupplyPersonData()
            }
            2->{
                valueforsupply= arrayListOf(mPageNumberForTeam,mCountPerPageForTeam,supplySite,supplyVariety,
                    selectContentElect[0],selectContentElect[1],selectContentElect[2],selectContentElect[3],
                    selectContentElect[4],selectContentElect[5]) as MutableList<String>
                loadSupplyTeamData()
            }
            3->{
                valueforsupply= arrayListOf(mPageNumberForLease,mCountPerPageForLease,supplySite,supplyVariety,
                    supplyVehicle) as MutableList<String>
                loadSupplyLeaseData()
            }
            4->{
                valueforsupply= arrayListOf(mPageNumberForThird,mCountPerPageForThird,supplySite,supplyVariety) as MutableList<String>
                loadSupplyThirdData()
            }
            else->{
            }
        }
    }
}