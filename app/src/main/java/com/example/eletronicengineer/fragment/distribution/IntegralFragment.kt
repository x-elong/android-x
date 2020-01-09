package com.example.eletronicengineer.fragment.distribution

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.ExtensionAdapter
import com.example.eletronicengineer.adapter.IntegralAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.aninterface.ExtensionStyle
import com.example.eletronicengineer.aninterface.IntegralStyle
import com.example.eletronicengineer.custom.CustomDialog
//import com.example.eletronicengineer.fragment.distribution.RetailStoreFragment.Companion.DETAILS
//import com.example.eletronicengineer.fragment.distribution.RetailStoreFragment.Companion.LAST_VIP
//import com.example.eletronicengineer.fragment.distribution.RetailStoreFragment.Companion.SCORE_DETAIL
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.getOwnExtendUAndI
import com.example.eletronicengineer.utils.getOwnExtendUser
import com.example.eletronicengineer.utils.getOwnIntegralOfRebate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_image_display.*
import kotlinx.android.synthetic.main.fragment_integral.view.*
import kotlinx.android.synthetic.main.item_integral.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class IntegralFragment : Fragment(){
    companion object{
        fun newInstance(args: Bundle): IntegralFragment
        {
            val fragment= IntegralFragment()
            fragment.arguments=args
            return fragment
        }
    }
    lateinit var title:String
    var type:Int = 0
    var mIntegralList = ArrayList<IntegralStyle>()
    var mExtensionList = ArrayList<ExtensionStyle>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_integral,container, false)
        title=arguments!!.getString("title")
        view.tv_title_integral.text=title
        type=arguments!!.getInt("type")

        initFragment(view,type)
        return view
    }

    private fun initFragment(v: View,type : Int) {
        val calendar = Calendar.getInstance()
        val year=calendar.get(Calendar.YEAR)
        val month=calendar.get(Calendar.MONTH)
        val day=calendar.get(Calendar.DAY_OF_MONTH)

//        when (type) {
//            SCORE_DETAIL -> {//积分详情
//                v.tv_time_integral.visibility = View.VISIBLE
//                v.tv_calendar_integral.visibility=View.VISIBLE
//                v.tv_mark_integral.visibility=View.VISIBLE
//
//                v.tv_time_integral.text ="${year}年${month + 1}月"
//                val date= "${year}-${month+1}-${day}"
//                OwnIntegralOfRebate(date,UnSerializeDataBase.shoppingPath,v)
//
//                v.tv_calendar_integral.setOnClickListener {//弹出日期选择器，根据选择的日期重新查询
//                    val option1Items:MutableList<String> =ArrayList()
//                    val option2Items:MutableList<MutableList<String>> =ArrayList()
//                    for (j in 0 until 51)
//                    {
//                        option1Items.add((2000+j).toString()+"年")
//                    }
//                    for (j in 0 until 51)
//                    {
//                        option2Items.add(ArrayList())
//                        for (k in 1 until 13)
//                        {
//                            option2Items[j].add(k.toString()+"月")
//                        }
//                    }
//                    val handler = Handler(Handler.Callback {
//                        when (it.what) {
//                            RecyclerviewAdapter.MESSAGE_SELECT_OK -> {
//                                val type = it.data.getString("selectContent")
//                                v.tv_time_integral.text = type
//                                var tempYear =  type.substring(0,5)
//                                var tempMonth = if(type.length == 7)
//                                {
//                                    type.substring(5,6)
//                                }
//                                else{
//                                    type.substring(5,7)
//                                }
//                                var temp = "$tempYear-$tempMonth-01"
//
//                                OwnIntegralOfRebate(temp, UnSerializeDataBase.shoppingPath,v)
//
//                                false
//                            }
//                            else -> {
//                                false
//                            }
//                        }
//                    })
//                    val selectDialog =
//                        CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,v.context,handler,option1Items,option2Items).multiDialog
//                    selectDialog.show()
//                }
//            }
//            LAST_VIP -> {
//                v.tv_extended.visibility=View.VISIBLE
//                v.tv_rebate.visibility=View.VISIBLE
//
//                OwnExtendUser(1, UnSerializeDataBase.shoppingPath,v)
//
//                //点击一级会员
//                v.tv_extended.setOnClickListener {
//                    v.tv_rebate.setBackgroundColor(Color.parseColor("#e5e5e5"))
//                    v.tv_extended.setBackgroundColor(Color.parseColor("#ffffff"))
//                    OwnExtendUser(1, UnSerializeDataBase.shoppingPath,v)
//                }
//                //点击二级会员
//                v.tv_rebate.setOnClickListener {
//                    v.tv_rebate.setBackgroundColor(Color.parseColor("#ffffff"))
//                    v.tv_extended.setBackgroundColor(Color.parseColor("#e5e5e5"))
//                    OwnExtendUser(2, UnSerializeDataBase.shoppingPath,v)
//                }
//            }
//            DETAILS -> {
//                v.tv_extended.visibility=View.VISIBLE
//                v.tv_rebate.visibility=View.VISIBLE
//                v.tv_calendar_integral.visibility=View.VISIBLE
//                v.tv_time_integral.visibility = View.VISIBLE
//                v.tv_time_integral.text ="${year}年"
//
//                var vipLevel = 1
//                var mYear = year
//                OwnExtendUAndI(vipLevel,mYear,UnSerializeDataBase.shoppingPath,v)
//
//                v.tv_calendar_integral.setOnClickListener {
//                    var option1Items:MutableList<String> =ArrayList()
//                    for (j in 0 until 51)
//                    {
//                        option1Items.add((2019+j).toString()+"年")
//                    }
//                    val handler = Handler(Handler.Callback {
//                        when (it.what) {
//                            RecyclerviewAdapter.MESSAGE_SELECT_OK -> {
//                                var type = it.data.getString("selectContent")
//                                v.tv_time_integral.text = type
//                                var temp = type.substring(0,4)
//                                mYear = temp.toInt()
//                                OwnExtendUAndI(vipLevel,mYear,UnSerializeDataBase.shoppingPath,v)
//                                false
//                            }
//                            else -> {
//                                false
//                            }
//                        }
//                    })
//                    val selectDialog =
//                        CustomDialog(CustomDialog.Options.SELECT_DIALOG,v.context,option1Items,handler).dialog
//                    selectDialog.show()
//                }
//                //点击一级会员，将List进行clear()，重新加载初次传入的数据
//                v.tv_extended.setOnClickListener {
//                    v.tv_rebate.setBackgroundColor(Color.parseColor("#e5e5e5"))
//                    v.tv_extended.setBackgroundColor(Color.parseColor("#ffffff"))
//                    vipLevel=1
//                    OwnExtendUAndI(vipLevel,mYear, UnSerializeDataBase.shoppingPath,v)
//                }
//                //点击二级会员，将List进行clear()，重新加载别的数据
//                v.tv_rebate.setOnClickListener {
//                    v.tv_rebate.setBackgroundColor(Color.parseColor("#ffffff"))
//                    v.tv_extended.setBackgroundColor(Color.parseColor("#e5e5e5"))
//                    vipLevel=2
//                    OwnExtendUAndI(vipLevel,mYear, UnSerializeDataBase.shoppingPath,v)
//                }
//            }
//        }
        v.tv_title_back_integral.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
    }

    fun OwnIntegralOfRebate(date:String,baseUrl:String,view: View)
    {
        val resultHttp = getOwnIntegralOfRebate(date, baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                mIntegralList.clear()
                if(it.message!=null)
                {
                    val mdata = it.message
                    var eventName = "无"
                    var foundTime = "无"
                    var addIntegral = "无"
                    var tempSave=""
                    var tempSave1=""

                    view.tv_mark_integral.text = if(mdata.monthIntegral==null) { "数据异常" }
                                                else {    "+${mdata.monthIntegral}"   }
                    val temp = mdata.detailDTOList
                    if(temp.isEmpty())
                    {
                        Toast.makeText(view.context,"没有数据",Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        for(j in temp)
                        {
                            tempSave = when (j.userLevel) {
                                "0" -> "我的"
                                "1" -> "一级会员"
                                "2" -> "二级会员"
                                else -> "无"
                            }
                            tempSave1 = when (j.identification) {
                                "0" -> "推广积分"
                                "1" -> "返利积分"
                                "2" -> "其他"
                                else -> "无"
                            }
                            if(j.foundTime != null)
                                foundTime = SimpleDateFormat("yyyy-MM-dd").format(j.foundTime)
                            if(j.eventName != null)
                                eventName = j.eventName
                            if(j.addIntegral !=null)
                                addIntegral = j.addIntegral.toString()
                            val integralStyle = IntegralStyle("", eventName,
                                "时间:$foundTime  $tempSave1  $tempSave",
                                "+$addIntegral",
                                1)
                            mIntegralList.add(integralStyle)
                        }
                        view.recycler_integral.adapter = IntegralAdapter(mIntegralList)
                        view.recycler_integral.layoutManager = LinearLayoutManager(context)
                    }
                }
                else
                    Toast.makeText(view.context,"没有数据",Toast.LENGTH_SHORT).show()
            },{
                it.printStackTrace()
                Toast.makeText(view.context,"服务器异常",Toast.LENGTH_SHORT).show()
            })
    }
    fun OwnExtendUser(vipLevel:Int,baseUrl: String,view: View)
    {
        val resultHttp = getOwnExtendUser(vipLevel,baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                mIntegralList.clear()
                if(it.message != null)
                {
                    val temp=it.message
                    var foundTime = "(无)"
                    var headPortrait = "无"
                    var name = "无"
                    var phone = "(无)"
                    for(j in temp)
                    {
                        if(j.foundTime != null)
                            foundTime = SimpleDateFormat("yyyy年MM月dd日").format(j.foundTime)
                        if(j.headPortrait != null)
                            headPortrait = j.headPortrait
                        if(j.name != null)
                            name = j.name
                        if(j.phone != null)
                            phone = j.phone
                        val integralStyle = IntegralStyle(headPortrait, name,
                            "$phone  $foundTime",
                            "",
                            2)
                        mIntegralList.add(integralStyle)
                    }
                    view.recycler_integral.adapter = IntegralAdapter(mIntegralList)
                    view.recycler_integral.layoutManager = LinearLayoutManager(context)
                }
                else
                    Toast.makeText(view.context,"没有数据",Toast.LENGTH_SHORT).show()
            },{
                it.printStackTrace()
                Toast.makeText(view.context,"服务器异常",Toast.LENGTH_SHORT).show()
            })
    }
    fun OwnExtendUAndI(vipLevel: Int,year:Int,baseUrl: String,view: View)
    {
        val resultHttp = getOwnExtendUAndI(vipLevel,year,baseUrl).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                mExtensionList.clear()
                if(it.message !=null)
                {
//                    val tempName = listOf("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月")
//                    val tempMonth = listOf(listOf(it.message.januaryValue[0],it.message.januaryValue[1]),
//                        listOf(it.message.februaryValue[0],it.message.februaryValue[1]),
//                        listOf(it.message.marchValue[0],it.message.marchValue[1]),
//                        listOf(it.message.aprilValue[0],it.message.aprilValue[1]),
//                        listOf(it.message.mayValue[0],it.message.mayValue[1]),
//                        listOf(it.message.juneValue[0],it.message.juneValue[1]),
//                        listOf(it.message.julyValue[0],it.message.julyValue[1]),
//                        listOf(it.message.augustValue[0],it.message.augustValue[1]),
//                        listOf(it.message.septemberValue[0],it.message.septemberValue[1]),
//                        listOf(it.message.octoberValue[0],it.message.octoberValue[1]),
//                        listOf(it.message.novemberValue[0],it.message.novemberValue[1]),
//                        listOf(it.message.decemberValue[0],it.message.decemberValue[1])
//                    )
//                    for(j in 0 until tempName.size)
//                    {
//                        var extensionStyle = ExtensionStyle(tempName[j],"+${tempMonth[j][0]}","+${tempMonth[j][1]}")
//                        mExtensionList.add(extensionStyle)
//                    }
                    view.recycler_integral.adapter = ExtensionAdapter(mExtensionList)
                    view.recycler_integral.layoutManager = LinearLayoutManager(context)
                }
                else
                    Toast.makeText(view.context,"没有数据",Toast.LENGTH_SHORT).show()
            },{
                it.printStackTrace()
                Toast.makeText(view.context,"服务器异常",Toast.LENGTH_SHORT).show()
            })
    }

}