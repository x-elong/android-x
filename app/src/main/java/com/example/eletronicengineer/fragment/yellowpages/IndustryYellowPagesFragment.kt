package com.example.eletronicengineer.fragment.yellowpages

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.ListAdapterForDemand
import com.example.eletronicengineer.adapter.ListAdapterForSupply
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.aninterface.PersonalIssue
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.fragment.sdf.keyforsupply
import com.example.eletronicengineer.fragment.sdf.valueforsupply
import com.example.eletronicengineer.utils.*
import com.google.android.material.tabs.TabLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.demand.view.*
import kotlinx.android.synthetic.main.item_public_point_position_transport.view.*
import kotlinx.android.synthetic.main.yellowpages.*
import kotlinx.android.synthetic.main.yellowpages.view.*
import kotlinx.android.synthetic.main.yellowpages.view.tv_yellow_pages_type_select
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder


lateinit var key:MutableList<String>
lateinit var value:MutableList<String>
var d_temp:String=""
var yellowPagesName:String=""
var yellowPagesType:String=""
var yellowPagesGrade:Array<String> = arrayOf("","","")
var yellowPagesSite:String=""
//
var theflag=0

class IndustryYellowPagesFragment: Fragment(){
    val selectOption1Items:MutableList<String> =ArrayList()
    val selectOption2Items:MutableList<MutableList<String>> =ArrayList()
    val selectOption3Items:MutableList<MutableList<MutableList<String>>> =ArrayList()
    var mPersonalIssueAdapter: ListAdapterForSupply?=null
    val mCountPerPageForPerson = 30
    var mPageNumberForYellowPages = 1
    var mIsLastPageForYellowPages = false
    var mIsLoadingYellowPages = false
    lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.yellowpages, container, false)
        mView.yellow_pages_swipe_refresh.setColorSchemeColors(Color.rgb(47,223,189))
        //类别
        val Option1Items= listOf("安装类","勘察设计类","监理类","通讯类") as MutableList<String>
        mView.tv_yellow_pages_type_select.movementMethod=ScrollingMovementMethod.getInstance()
        mView.tv_yellow_pages_type_select.setOnClickListener{

            initYellowPagesType(mView,Option1Items)
            var w_temp=mView.tv_yellow_pages_type_select.text.toString()
            if(w_temp=="行业类别及名称"){mView.tv_yellow_pages_grade_select.visibility=View.GONE}
            else{
                mView.tv_yellow_pages_grade_select.visibility=View.VISIBLE
            }
            mView.tv_yellow_pages_grade_select.movementMethod=ScrollingMovementMethod.getInstance()
            mView.tv_yellow_pages_grade_select.setOnClickListener{
                var w_temp=mView.tv_yellow_pages_type_select.text.toString()
                d_temp=w_temp
            when(w_temp){
                "安装类"->{
                    val Option1Items= listOf("承装一级","承装二级","承装三级","承装四级","承装五级","") as MutableList<String>
                    val Option2Items= listOf(listOf("承修一级","承修二级","承修三级","承修四级","承修五级",""),
                        listOf("承修一级","承修二级","承修三级","承修四级","承修五级",""),
                        listOf("承修一级","承修二级","承修三级","承修四级","承修五级",""),
                        listOf("承修一级","承修二级","承修三级","承修四级","承修五级",""),
                        listOf("承修一级","承修二级","承修三级","承修四级","承修五级",""),
                        listOf("承修一级","承修二级","承修三级","承修四级","承修五级",""))as MutableList<MutableList<String>>
                    val Option3Items=listOf(listOf(listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级","")),
                        listOf(listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级","")),
                        listOf(listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级","")),
                        listOf(listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级","")),
                        listOf(listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级","")),
                        listOf(listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""),
                            listOf("承试一级","承试二级","承试三级","承试四级","承试五级",""))) as MutableList<MutableList<MutableList<String>>>
                    initYellowPagesGrade1(mView,Option1Items,Option2Items,Option3Items, d_temp)
                }
                "勘察设计类"->{
                    val Option1Items= listOf("工程设计电力行业送电工程专业甲级","工程设计电力行业送电工程专业乙级","工程设计电力行业送电工程专业丙级","") as MutableList<String>
                    val Option2Items= listOf(listOf("工程设计电力行业变电工程专业甲级","工程设计电力行业变电工程专业乙级","工程设计电力行业变电工程专业丙级",""),
                        listOf("工程设计电力行业变电工程专业甲级","工程设计电力行业变电工程专业乙级","工程设计电力行业变电工程专业丙级",""),
                        listOf("工程设计电力行业变电工程专业甲级","工程设计电力行业变电工程专业乙级","工程设计电力行业变电工程专业丙级",""),
                        listOf("工程设计电力行业变电工程专业甲级","工程设计电力行业变电工程专业乙级","工程设计电力行业变电工程专业丙级",""))as MutableList<MutableList<String>>
                    val Option3Items=listOf(listOf(listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级",""),
                            listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级",""),listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级",""),listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级","")),
                        listOf(listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级",""),
                            listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级",""),listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级",""),listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级","")),
                        listOf(listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级",""),
                            listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级",""),listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级",""),listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级","")),
                        listOf(listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级",""),
                            listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级",""),listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级",""),listOf("工程勘察工程测量专业甲级","工程勘察工程测量专业乙级","工程勘察工程测量专业丙级",""))) as MutableList<MutableList<MutableList<String>>>
                    initYellowPagesGrade1(mView,Option1Items,Option2Items,Option3Items,d_temp)
                }
                "监理类"->{
                    val Option1Items= listOf("工程监理电力工程专业甲级","工程监理电力工程专业乙级","工程监理电力工程专业丙级") as MutableList<String>
                    initYellowPagesGrade2(mView,Option1Items,d_temp)
                }
                "通讯类"->{
                    val Option1Items= listOf("电子与智能化工程专业承包一级","电子与智能化工程专业承包二级","电子与智能化工程专业承包三级",
                        "通信工程施工总承包一级","通信工程施工总承包二级","通信工程施工总承包三级") as MutableList<String>
                    initYellowPagesGrade2(mView,Option1Items,d_temp)
                }
            }
            }
        }
        //地点
        initProjectSite()
        mView.tv_yellow_pages_site_select.setOnClickListener {
            mView.tv_yellow_pages_site_select.movementMethod=ScrollingMovementMethod.getInstance()
            initSite(mView)
        }
        //名称
        mView.et_yellow_pages_name.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                initName(mView,s.toString())
            }
        })
        //去掉name
        mView.tv_yellow_pages_name_clear.setOnClickListener {
            mView.et_yellow_pages_name.setText("")
            mView.tv_yellow_pages_name_clear.visibility=View.GONE
            yellowPagesName=""
            key = arrayListOf("page","number","firstQualification","secondQualification","thirdQualification","companyType","companyAddress","companyName")
            value = arrayListOf(mPageNumberForYellowPages,mCountPerPageForPerson, yellowPagesGrade[0],yellowPagesGrade[1],yellowPagesGrade[2],
                yellowPagesType, yellowPagesSite, yellowPagesName) as MutableList<String>
            mPageNumberForYellowPages = 1
            mIsLastPageForYellowPages = false
            mPersonalIssueAdapter!!.notifyData()
            demandYellowPages(mView)
        }
        //去掉type
        mView.tv_yellow_pages_type_clear.setOnClickListener {
            mView.tv_yellow_pages_type_select.text="行业类别及名称"
            mView.tv_yellow_pages_type_clear.visibility=View.GONE
            mView.tv_yellow_pages_grade_select.visibility=View.GONE
            yellowPagesType=""
            yellowPagesGrade=arrayOf("","","")
            key = arrayListOf("page","number","firstQualification","secondQualification","thirdQualification","companyType","companyAddress","companyName")
            value = arrayListOf(mPageNumberForYellowPages,mCountPerPageForPerson, yellowPagesGrade[0],yellowPagesGrade[1],yellowPagesGrade[2],
                yellowPagesType, yellowPagesSite, yellowPagesName) as MutableList<String>
            mPageNumberForYellowPages = 1
            mIsLastPageForYellowPages = false
            mPersonalIssueAdapter!!.notifyData()
            demandYellowPages(mView)
        }
        //去掉地点
        mView.tv_yellow_pages_site_clear.setOnClickListener{
            mView.tv_yellow_pages_site_select.text="选择地点"
            mView.tv_yellow_pages_site_clear.visibility=View.GONE
            yellowPagesSite=""
            key = arrayListOf("page","number","firstQualification","secondQualification","thirdQualification","companyType","companyAddress","companyName")
            value = arrayListOf(mPageNumberForYellowPages,mCountPerPageForPerson, yellowPagesGrade[0],yellowPagesGrade[1],yellowPagesGrade[2],
                yellowPagesType, yellowPagesSite, yellowPagesName) as MutableList<String>
            mPageNumberForYellowPages = 1
            mIsLastPageForYellowPages = false
            mPersonalIssueAdapter!!.notifyData()
            demandYellowPages(mView)
        }
        //去掉grade
        mView.tv_yellow_pages_grade_clear.setOnClickListener{
            mView.tv_yellow_pages_grade_select.text="请选择类别与等级"
            mView.tv_yellow_pages_grade_clear.visibility=View.GONE
            yellowPagesGrade=arrayOf("","","")
            key = arrayListOf("page","number","firstQualification","secondQualification","thirdQualification","companyType","companyAddress","companyName")
            value = arrayListOf(mPageNumberForYellowPages,mCountPerPageForPerson, yellowPagesGrade[0],yellowPagesGrade[1],yellowPagesGrade[2],
                yellowPagesType, yellowPagesSite, yellowPagesName) as MutableList<String>
            mPageNumberForYellowPages = 1
            mIsLastPageForYellowPages = false
            mPersonalIssueAdapter!!.notifyData()
            demandYellowPages(mView)
        }
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPersonalIssueAdapter = ListAdapterForSupply(activity!!)
        key = arrayListOf("page","number","firstQualification","secondQualification","thirdQualification","companyType","companyAddress","companyName")
        value = arrayListOf(mPageNumberForYellowPages,mCountPerPageForPerson, yellowPagesGrade[0],yellowPagesGrade[1],yellowPagesGrade[2], yellowPagesType, yellowPagesSite, yellowPagesName) as MutableList<String>
        demandYellowPages(view)
    }
    //
    private fun demandYellowPages(view:View) {
        loadYellowPagesData()
        theflag=1
        view.tv_yellow_pages_content.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mPersonalIssueAdapter
        }
        view.tv_yellow_pages_content.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var m = view.tv_yellow_pages_content.layoutManager as LinearLayoutManager
                if (m.findLastVisibleItemPosition() == m.itemCount - 1) {
                    selectScroll(theflag)
                }
            }
        })
        view.yellow_pages_swipe_refresh.setOnRefreshListener(object :SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                mView.yellow_pages_swipe_refresh.isRefreshing=false
                mPageNumberForYellowPages=1
                mIsLastPageForYellowPages=false
                mPersonalIssueAdapter!!.notifyData()
                value = arrayListOf(mPageNumberForYellowPages,mCountPerPageForPerson, yellowPagesGrade[0],yellowPagesGrade[1],yellowPagesGrade[2],
                    yellowPagesType, yellowPagesSite, yellowPagesName) as MutableList<String>
                selectScroll(theflag)
            }
        })
    }



    @Synchronized fun setLoadingYellowPages() : Boolean {
        if (mIsLoadingYellowPages)
            return false
        mIsLoadingYellowPages = true
        return true
    }
    fun loadYellowPagesData() {
        if (mIsLastPageForYellowPages)
            return
        if (!setLoadingYellowPages())
            return

        _loadYellowPagesData()

    }
    fun _loadYellowPagesData() {
        val result= Observable.create<RequestBody> {
            //建立网络请求体 (类型，内容)
            val jsonObject = JSONObject()
            for (i in 0 until key.size) {
                jsonObject.put(key[i], value[i])
            }
            val requestBody= RequestBody.create(MediaType.parse("application/json"),jsonObject.toString())
            it.onNext(requestBody)
        }
            .subscribe {
                val loadingDialog = LoadingDialog(mView.context, "正在加载...", R.mipmap.ic_dialog_loading)
                loadingDialog.show()
                val result =
                    getYellowPages(it,UnSerializeDataBase.userToken,UnSerializeDataBase.dmsBasePath).
                        subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({
                            val code = it.code
                            loadingDialog.dismiss()
                           mIsLoadingYellowPages = false
                                if(code=="200"){
                                    val data = it.message.data
                                    for (j in data) {
                                        var temp1: String
                                        var temp2: String
                                        var temp3:String=""
                                        var temp4:String
                                        val Qualification:List<String> = listOf(j.firstQualification,j.secondQualification,j.thirdQualification)
                                        temp1 = if(j.companyType==null )
                                            " "
                                        else j.companyType
                                        temp2 = if(j.companyName == null)
                                            " "
                                        else j.companyName
                                        for(str in Qualification){
                                            if(str!=null&&str!=""){
                                                if(temp3!="")
                                                    temp3+="|"
                                                temp3+=str
                                            }
                                        }
                                        temp4 = if(j.id == null)
                                            " "
                                        else
                                            j.id
                                        mPersonalIssueAdapter?.addData(PersonalIssue(temp1,temp2, temp3,temp4,"行业黄页"))
                                    }
                                    if (data.size < mCountPerPageForPerson)
                                        mIsLastPageForYellowPages = true
                                    else
                                        mPageNumberForYellowPages ++
                                }else if(code=="400" && code=="没有该数据"){
                                    ToastHelper.mToast(context!!,"数据为空")
                                }else{
                                    ToastHelper.mToast(context!!,"加载失败")
                                }

                        }, {
                            loadingDialog.dismiss()
                            it.printStackTrace()
                        })
            }
    }


    //初始化地点数据
    fun initProjectSite(){
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
    //等级弹窗1
    fun initYellowPagesGrade1(view:View,Option1Items: MutableList<String>,Option2Items: MutableList<MutableList<String>>,Option3Items: MutableList<MutableList<MutableList<String>>>,d_temp:String){
        var mHandler:Handler= Handler(Handler.Callback {
            when(it.what)
            {
                RecyclerviewAdapter.MESSAGE_SELECT_OK ->{
                    mPageNumberForYellowPages = 1
                    val selectContent=it.data.getString("selectContent")
                    view.tv_yellow_pages_grade_select.text=selectContent
                    view.tv_yellow_pages_grade_clear.visibility=View.VISIBLE
                    val str1 = view.tv_yellow_pages_grade_select.text.toString().split(" ")
                    for (temp in 0 until  str1.size) {
                        yellowPagesGrade[temp]=str1[temp]
                    }
                    key = arrayListOf("page","number","firstQualification","secondQualification","thirdQualification","companyType","companyAddress","companyName")
                    value = arrayListOf(mPageNumberForYellowPages,mCountPerPageForPerson, yellowPagesGrade[0],yellowPagesGrade[1],yellowPagesGrade[2], yellowPagesType, yellowPagesSite, yellowPagesName) as MutableList<String>
                    mPageNumberForYellowPages = 1
                    mIsLastPageForYellowPages = false
                    mPersonalIssueAdapter!!.notifyData()
                        demandYellowPages(view)
                    false
                }
                else->
                {
                    false
                }
            }
        })
        val selectDialog= CustomDialog(CustomDialog.Options.THREE_OPTIONS_SELECT_DIALOG,view.context,mHandler,Option1Items,Option2Items,Option3Items).multiDialog
        selectDialog.show()
    }
    //等级弹窗2
    fun initYellowPagesGrade2(view:View,Option1Items: MutableList<String>,d_temp:String){
        var mHandler:Handler= Handler(Handler.Callback {
            when(it.what)
            {
                RecyclerviewAdapter.MESSAGE_SELECT_OK ->{
                    mPageNumberForYellowPages = 1
                    val selectContent=it.data.getString("selectContent")
                    view.tv_yellow_pages_grade_select.text=selectContent
                    view.tv_yellow_pages_grade_clear.visibility=View.VISIBLE
                    val str1 = view.tv_yellow_pages_grade_select.text.toString().split(" ")
                    for (temp in 0 until  str1.size) {
                        yellowPagesGrade[temp]=str1[temp]
                    }
                    key = arrayListOf("page","number","firstQualification","secondQualification","thirdQualification","companyType","companyAddress","companyName")
                    value = arrayListOf(mPageNumberForYellowPages,mCountPerPageForPerson, yellowPagesGrade[0],yellowPagesGrade[1],yellowPagesGrade[2], yellowPagesType, yellowPagesSite, yellowPagesName) as MutableList<String>
                    mPageNumberForYellowPages = 1
                    mIsLastPageForYellowPages = false
                    mPersonalIssueAdapter!!.notifyData()
                    demandYellowPages(view)
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
    //类别弹窗
    fun initYellowPagesType(view:View,Option1Items: MutableList<String>){
        var mHandler:Handler= Handler(Handler.Callback {
            when(it.what)
            {
                RecyclerviewAdapter.MESSAGE_SELECT_OK ->{
                    mPageNumberForYellowPages = 1
                    view.tv_yellow_pages_grade_select.visibility=View.VISIBLE
                    yellowPagesType=it.data.getString("selectContent")
                    view.tv_yellow_pages_type_select.text=yellowPagesType
                    view.tv_yellow_pages_type_clear.visibility=View.VISIBLE
                    key = arrayListOf("page","number","firstQualification","secondQualification","thirdQualification","companyType","companyAddress","companyName")
                    value = arrayListOf(mPageNumberForYellowPages,mCountPerPageForPerson, yellowPagesGrade[0],yellowPagesGrade[1],yellowPagesGrade[2], yellowPagesType, yellowPagesSite, yellowPagesName) as MutableList<String>
                    mPageNumberForYellowPages = 1
                    mIsLastPageForYellowPages = false
                    mPersonalIssueAdapter!!.notifyData()
                    demandYellowPages(view)
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
    //地点选择弹窗
    fun initSite(view:View) {
        var mHandler: Handler = Handler(Handler.Callback {
            when (it.what) {
                RecyclerviewAdapter.MESSAGE_SELECT_OK -> {
                    mPageNumberForYellowPages = 1
                    val selectContent = it.data.getString("selectContent")
                    view.tv_yellow_pages_site_select.text = selectContent
                    view.tv_yellow_pages_site_clear.visibility = View.VISIBLE
                    val str = view.tv_yellow_pages_site_select.text.toString().split(" ")
                    yellowPagesSite=""
                    for (temp in str) {
                        yellowPagesSite += temp
                        }
                    key = arrayListOf("page","number","firstQualification","secondQualification","thirdQualification","companyType","companyAddress","companyName")
                    value = arrayListOf(mPageNumberForYellowPages,mCountPerPageForPerson, yellowPagesGrade[0],yellowPagesGrade[1],yellowPagesGrade[2],
                        yellowPagesType, yellowPagesSite, yellowPagesName) as MutableList<String>
                    mPageNumberForYellowPages = 1
                    mIsLastPageForYellowPages = false
                    mPersonalIssueAdapter!!.notifyData()
                    demandYellowPages(view)
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
   // 公司名称
   fun initName(view:View,name:String){
       mPageNumberForYellowPages = 1
       mView.tv_yellow_pages_name_clear.visibility=View.VISIBLE
       yellowPagesName=name
       key = arrayListOf("page","number","firstQualification","secondQualification","thirdQualification","companyType","companyAddress","companyName")
       value = arrayListOf(mPageNumberForYellowPages,mCountPerPageForPerson, yellowPagesGrade[0],yellowPagesGrade[1],yellowPagesGrade[2],
           yellowPagesType, yellowPagesSite, yellowPagesName) as MutableList<String>
       mPageNumberForYellowPages = 1
       mIsLastPageForYellowPages = false
       mPersonalIssueAdapter!!.notifyData()
       demandYellowPages(view)
   }
    fun selectScroll(theflag:Int){
        when(theflag)
        {
            1->{
                value = arrayListOf(mPageNumberForYellowPages,mCountPerPageForPerson, yellowPagesGrade[0],yellowPagesGrade[1],yellowPagesGrade[2],
                    yellowPagesType, yellowPagesSite, yellowPagesName) as MutableList<String>
                loadYellowPagesData()
            }
            else->{
            }
        }
    }
}