package com.example.eletronicengineer.fragment.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ProjectDiskActivity
import com.example.eletronicengineer.adapter.FunctionAdapter
import com.example.eletronicengineer.aninterface.Function
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now
import interfaces.heweather.com.interfacesmodule.view.HeConfig
import interfaces.heweather.com.interfacesmodule.view.HeWeather
import kotlinx.android.synthetic.main.application.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class ApplicationFragment : Fragment() {

    var viewListener:View.OnClickListener?=null
    var functionList: MutableList<Function> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val startTime = System.currentTimeMillis()
        val view = inflater.inflate(R.layout.application, container, false)
        initData(view)
        initFunction(view)
        val endTime = System.currentTimeMillis()
        Log.i("application run time is:",(endTime-startTime).toString())
        return view
    }
    private fun initData(view: View) {
        val c = Calendar.getInstance()
        val month = (c.get(Calendar.MONTH)+1)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var way = c.get(Calendar.DAY_OF_WEEK).toString()
        when(way){
            "1"->way="天"
            "2"->way="一"
            "3"->way="二"
            "4"->way="三"
            "5"->way="四"
            "6"->way="五"
            "7"->way="六"
        }
        HeConfig.init("HE1907301002421036","20ee14925e644b8f9bf047b414c3e439")
        HeConfig.switchToFreeServerNode()
        HeWeather.getWeatherNow(context,"auto_ip",object :HeWeather.OnResultWeatherNowBeanListener{
            override fun onError(e: Throwable?) {
                Log.i("Weather Now","获取失败")
            }

            override fun onSuccess(dataObject: Now?) {
                Log.i("Success"," Weather Now onSuccess: " + Gson().toJson(dataObject))
                try {
                    val now = dataObject!!.now
                    view.i_tv.text="          星期${way}    ${month}月${day}日              ${now.cond_txt}      ${now.tmp}度     ${now.wind_dir}${now.wind_sc}级"

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        })
    }

    private fun initFunction(v: View) {
        functionList.clear()
        var f = Function("合 作 方", R.drawable.cooperation,null)
        functionList.add(f)
        f = Function("合 同", R.drawable.contract,null)
        functionList.add(f)
        f = Function("项 目 部", R.drawable.project_department,null)
        functionList.add(f)
        f = Function("招投供需", R.drawable.rsd,viewListener)
        functionList.add(f)
        f = Function("发 票", R.drawable.invoice,null)
        functionList.add(f)
        f = Function("记 账", R.drawable.bookkeeping,null)
        functionList.add(f)
        f = Function("任务统计", R.drawable.task_statistics,null)
        functionList.add(f)
        f = Function("财务统计", R.drawable.financial_statistics,null)
        functionList.add(f)
        f = Function("公 告", R.drawable.notice,null)
        functionList.add(f)
        f = Function("资 源 库", R.drawable.resource_library,null)
        functionList.add(f)
        f = Function("企业云盘", R.drawable.enterprise_cloud,null)
        functionList.add(f)
        f = Function("项目盘",R.drawable.enterprise_cloud,View.OnClickListener{
            startActivity(Intent(context,ProjectDiskActivity::class.java))
        })
        functionList.add(f)
        val recyclerView = v.recycler_view
        recyclerView.layoutManager = GridLayoutManager(context,4)
        val functionAdapter = FunctionAdapter(functionList)
        recyclerView.adapter = functionAdapter
    }
    fun setClickListener(listener: View.OnClickListener){
        viewListener=listener
    }
}