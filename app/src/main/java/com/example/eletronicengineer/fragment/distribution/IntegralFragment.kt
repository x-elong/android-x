package com.example.eletronicengineer.fragment.distribution

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.GlideLoader
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
        when (type) {
            1 -> {
                val calendar = Calendar.getInstance()
                var year=calendar.get(Calendar.YEAR)
                var month=calendar.get(Calendar.MONTH)
                var day=calendar.get(Calendar.DAY_OF_MONTH)
                var result: String
                result = "${year}年${month + 1}月"
                v.tv_time_integral.text = result
                v.tv_time_integral.visibility = View.VISIBLE
                v.tv_calendar_integral.visibility=View.VISIBLE
                v.tv_mark_integral.visibility=View.VISIBLE

                var dateForHttp = "${year}-${month+1}-${day}"
                val resultForHttp = getOwnIntegralOfRebate(dateForHttp,"http://10.1.5.141:8022/").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(
                        {
                        mIntegralList.clear()
                        val mdata = it.message
                        if(mdata!=null)
                        {
                            v.tv_mark_integral.text = mdata.monthIntegral.toString()
                            var temp = mdata.detailDTOList
                            var temp1:String
                            var temp2:String
                            var tempSave=""
                            var tempSave1=""
                            for(j in temp)
                            {
                                temp1 = j.userLevel
                                if(temp1 == "0")
                                {
                                    tempSave = "我的"
                                }
                                else if(temp1=="1")
                                {
                                    tempSave = "一级会员"
                                }
                                else
                                {
                                    tempSave="二级会员"
                                }

                                temp2 = j.identification
                                if(temp2=="0")
                                {
                                    tempSave1="推广积分"
                                }
                                else if(temp2=="1")
                                {
                                    tempSave1="返利积分"
                                }
                                else if(temp2=="2")
                                {
                                    tempSave1="其他"
                                }
                                var foundTime = SimpleDateFormat("yyyy-MM-dd").format(j.foundTime)
                                var integralStyle =
                                    IntegralStyle("", j.eventName, "时间：${foundTime}       ${tempSave}       ${tempSave1}", "+${j.addIntegral}", 1)
                                mIntegralList.add(integralStyle)
                            }
                            v.recycler_integral.adapter = IntegralAdapter(mIntegralList)
                            v.recycler_integral.layoutManager = LinearLayoutManager(context)
                        }
                        else
                        {
                            Toast.makeText(context,"没有数据哦！",Toast.LENGTH_LONG).show()
                        }
                    },
                        {
                            it.printStackTrace()
                        }
                    )
                v.tv_calendar_integral.setOnClickListener {
                    var option1Items:MutableList<String> =ArrayList()
                    var option2Items:MutableList<MutableList<String>> =ArrayList()
                    for (j in 0 until 51)
                    {
                        option1Items.add((2010+j).toString()+"年")
                    }
                    for (j in 0 until 51)
                    {
                        option2Items.add(ArrayList())
                        for (k in 1 until 13)
                        {
                            option2Items[j].add(k.toString()+"月")
                        }
                    }
                    val handler = Handler(Handler.Callback {
                        when (it.what) {
                            RecyclerviewAdapter.MESSAGE_SELECT_OK -> {
                                var type = it.data.getString("selectContent")
                                v.tv_time_integral.text = type
                                var tempYear = ""
                                var tempMonth = ""
                                var temp = ""
                                tempYear = type.substring(0,5)
                                tempMonth = if(type.length == 7)
                                {
                                    type.substring(5,6)
                                }
                                else{
                                    type.substring(5,7)
                                }
                                temp = "$tempYear-$tempMonth-01"
                                Log.i("temp",temp)

                                val resultForHttp = getOwnIntegralOfRebate(temp,"http://10.1.5.141:8022/").subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        mIntegralList.clear()
                                        val mdata = it.message
                                        if(mdata!=null)
                                        {
                                            v.tv_mark_integral.text = mdata.monthIntegral.toString()
                                            var temp = mdata.detailDTOList
                                            var temp1:String
                                            var temp2:String
                                            var tempSave=""
                                            var tempSave1=""
                                            for(j in temp)
                                            {
                                                temp1 = j.userLevel
                                                if(temp1 == "0")
                                                {
                                                    tempSave = "我的"
                                                }
                                                else if(temp1=="1")
                                                {
                                                    tempSave = "一级会员"
                                                }
                                                else
                                                {
                                                    tempSave="二级会员"
                                                }

                                                temp2 = j.identification
                                                if(temp2=="0")
                                                {
                                                    tempSave1="推广积分"
                                                }
                                                else if(temp2=="1")
                                                {
                                                    tempSave1="返利积分"
                                                }
                                                else if(temp2=="2")
                                                {
                                                    tempSave1="其他"
                                                }
                                                var foundTime = SimpleDateFormat("yyyy-MM-dd").format(j.foundTime)
                                                var integralStyle =
                                                    IntegralStyle("", j.eventName, "时间：${foundTime}       ${tempSave}       ${tempSave1}", "+${j.addIntegral}", 1)
                                                mIntegralList.add(integralStyle)
                                            }
                                            v.recycler_integral.adapter = IntegralAdapter(mIntegralList)
                                            v.recycler_integral.layoutManager = LinearLayoutManager(context)
                                        }
                                        else
                                        {
                                            Toast.makeText(context,"没有数据哦！",Toast.LENGTH_LONG).show()
                                        }
                                    },
                                        {
                                            it.printStackTrace()
                                })
                                false
                            }
                            else -> {
                                false
                            }
                        }
                    })
                    val selectDialog =
                        CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,v.context,handler,option1Items,option2Items).multiDialog
                    selectDialog.show()
                }
            }
            2 -> {
                v.tv_extended.visibility=View.VISIBLE
                v.tv_rebate.visibility=View.VISIBLE
                var vipLevel = 1
                val resultForHttp = getOwnExtendUser(vipLevel,"http://10.1.5.141:8022/").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        mIntegralList.clear()
                        val temp=it.message
                        if(temp!=null)
                        {
                            for(j in temp)
                            {
                                //拿到图片路径，访问文件服务器，拿到图片，显示图片
                                var imgPath = j.headPortrait
                                if(imgPath!=null){
                                   // GlideLoader().loadPreImage(v.findViewById(R.id.tv_integral_image),imgPath)
                                    Glide.with(v).load(imgPath).override(50,50).fitCenter().into(v.findViewById(R.id.tv_integral_image))
                                }

                                var foundTime = SimpleDateFormat("yyyy-MM-dd").format(j.foundTime)
                                var integralStyle = IntegralStyle("", j.name, "${j.phone}        ${foundTime}", "", 2)
                                mIntegralList.add(integralStyle)
                                Log.i("data",mIntegralList.toString())
                            }
                            v.recycler_integral.adapter = IntegralAdapter(mIntegralList)
                            v.recycler_integral.layoutManager = LinearLayoutManager(context)
                        }
                        else
                        {
                            Toast.makeText(context,"没有数据哦！",Toast.LENGTH_LONG).show()
                        }
                    },{
                    it.printStackTrace()
                })

                //点击一级会员，将List进行clear()，重新加载初次传入的数据
                v.tv_extended.setOnClickListener {
                    v.tv_rebate.setBackgroundColor(Color.parseColor("#e5e5e5"))
                    v.tv_extended.setBackgroundColor(Color.parseColor("#ffffff"))

                    vipLevel = 1
                    val resultForHttp = getOwnExtendUser(vipLevel,"http://10.1.5.141:8022/").subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                            mIntegralList.clear()
                            val temp=it.message
                            if(temp!=null)
                            {
                                for(j in temp)
                                {
                                    //拿到图片路径，访问文件服务器，拿到图片，显示图片
                                    var imgPath = j.headPortrait
                                    if(imgPath!=null){
                                        GlideLoader().loadImage(v.findViewById(R.id.tv_integral_image),imgPath)
                                    }
                                    var foundTime = SimpleDateFormat("yyyy-MM-dd").format(j.foundTime)
                                    var integralStyle = IntegralStyle("", j.name, "${j.phone}        ${foundTime}", "", 2)
                                    mIntegralList.add(integralStyle)
                                    Log.i("data",mIntegralList.toString())
                                }
                                v.recycler_integral.adapter = IntegralAdapter(mIntegralList)
                                v.recycler_integral.layoutManager = LinearLayoutManager(context)
                            }
                            else
                            {
                                Toast.makeText(context,"没有数据哦！",Toast.LENGTH_LONG).show()
                            }
                        },{
                    it.printStackTrace()
                })
                }
                //点击二级会员，将List进行clear()，重新加载别的数据
                v.tv_rebate.setOnClickListener {
                    v.tv_rebate.setBackgroundColor(Color.parseColor("#ffffff"))
                    v.tv_extended.setBackgroundColor(Color.parseColor("#e5e5e5"))
                    vipLevel = 2
                    val resultForHttp = getOwnExtendUser(vipLevel,"http://10.1.5.141:8022/").subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                            mIntegralList.clear()
                            val temp=it.message
                            if(temp!=null)
                            {
                                for(j in temp)
                                {
                                    //拿到图片路径，访问文件服务器，拿到图片，显示图片
                                    var imgPath = j.headPortrait

                                    if(imgPath!=null){
                                        GlideLoader().loadPreImage(v.findViewById(R.id.tv_integral_image),imgPath)
                                    }

                                    var foundTime = SimpleDateFormat("yyyy-MM-dd").format(j.foundTime)
                                    var integralStyle = IntegralStyle("", j.name, "${j.phone}        ${foundTime}", "", 2)
                                    mIntegralList.add(integralStyle)
                                    Log.i("data",mIntegralList.toString())
                                }
                                v.recycler_integral.adapter = IntegralAdapter(mIntegralList)
                                v.recycler_integral.layoutManager = LinearLayoutManager(context)
                            }
                            else
                            {
                                Toast.makeText(context,"没有数据哦！",Toast.LENGTH_LONG).show()
                            }
                        },{
                            it.printStackTrace()
                        })
                }
            }
            3 -> {
                v.tv_extended.visibility=View.VISIBLE
                v.tv_rebate.visibility=View.VISIBLE

                val calendar = Calendar.getInstance()
                var year=calendar.get(Calendar.YEAR)
                v.tv_calendar_integral.visibility=View.VISIBLE
                var vipLevel:Int=1

                val resultforhttp = getOwnExtendUAndI(vipLevel,year,"http://10.1.5.141:8022/").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        mExtensionList.clear()
                        val tempName = listOf("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月")
                        val tempMonth = listOf(it.message.januaryValue,it.message.februaryValue,it.message.marchValue,it.message.aprilValue,it.message.mayValue,it.message.juneValue,it.message.julyValue,it.message.augustValue,it.message.septemberValue,it.message.octoberValue,it.message.novemberValue,it.message.decemberValue)
                       if(tempMonth!=null)
                       {
                           for(j in 0 until tempName.size)
                           {
                               var extensionStyle = ExtensionStyle(tempName[j],"+${tempMonth[j]}")
                               mExtensionList.add(extensionStyle)
                               Log.i("data",mExtensionList.toString())
                           }
                           v.recycler_integral.adapter = ExtensionAdapter(mExtensionList)
                           v.recycler_integral.layoutManager = LinearLayoutManager(context)
                       }
                        else{
                           Toast.makeText(context,"没有数据哦！",Toast.LENGTH_LONG).show()
                       }

                    },{
                        it.printStackTrace()
                    })

                v.tv_calendar_integral.setOnClickListener {
                    var option1Items:MutableList<String> =ArrayList()
                    for (j in 0 until 51)
                    {
                        option1Items.add((2010+j).toString()+"年")
                    }
                    val handler = Handler(Handler.Callback {
                        when (it.what) {
                            RecyclerviewAdapter.MESSAGE_SELECT_OK -> {
                                var type = it.data.getString("selectContent")
                                var temp = type.substring(0,4)
                                year = temp.toInt()

                                val resultforhttp = getOwnExtendUAndI(vipLevel,temp.toInt(),"http://10.1.5.141:8022/").subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        mExtensionList.clear()
                                        val tempName = listOf("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月")
                                        val tempMonth = listOf(it.message.januaryValue,it.message.februaryValue,it.message.marchValue,it.message.aprilValue,it.message.mayValue,it.message.juneValue,it.message.julyValue,it.message.augustValue,it.message.septemberValue,it.message.octoberValue,it.message.novemberValue,it.message.decemberValue)
                                        if(tempMonth!=null)
                                        {
                                            for(j in 0 until tempName.size)
                                            {
                                                var extensionStyle = ExtensionStyle(tempName[j],"+${tempMonth[j]}")
                                                mExtensionList.add(extensionStyle)
                                                Log.i("data",mExtensionList.toString())
                                            }
                                            v.recycler_integral.adapter = ExtensionAdapter(mExtensionList)
                                            v.recycler_integral.layoutManager = LinearLayoutManager(context)
                                        }
                                        else{
                                            Toast.makeText(context,"没有数据哦！",Toast.LENGTH_LONG).show()
                                        }
                                    },{
                                        it.printStackTrace()
                                    })
                                false
                            }
                            else -> {
                                false
                            }
                        }
                    })
                    val selectDialog =
                        CustomDialog(CustomDialog.Options.SELECT_DIALOG,v.context,option1Items,handler).dialog
                    selectDialog.show()
                }

                //点击一级会员，将List进行clear()，重新加载初次传入的数据
                v.tv_extended.setOnClickListener {
                    v.tv_rebate.setBackgroundColor(Color.parseColor("#e5e5e5"))
                    v.tv_extended.setBackgroundColor(Color.parseColor("#ffffff"))
                    vipLevel=1
                    val resultforhttp = getOwnExtendUAndI(vipLevel,year,"http://10.1.5.141:8022/").subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                            mExtensionList.clear()
                            val tempName = listOf("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月")
                            val tempMonth = listOf(it.message.januaryValue,it.message.februaryValue,it.message.marchValue,it.message.aprilValue,it.message.mayValue,it.message.juneValue,it.message.julyValue,it.message.augustValue,it.message.septemberValue,it.message.octoberValue,it.message.novemberValue,it.message.decemberValue)
                            if(tempMonth!=null)
                            {
                                for(j in 0 until tempName.size)
                                {
                                    var extensionStyle = ExtensionStyle(tempName[j],"+${tempMonth[j]}")
                                    mExtensionList.add(extensionStyle)
                                    Log.i("data",mExtensionList.toString())
                                }
                                v.recycler_integral.adapter = ExtensionAdapter(mExtensionList)
                                v.recycler_integral.layoutManager = LinearLayoutManager(context)
                            }
                            else{
                                Toast.makeText(context,"没有数据哦！",Toast.LENGTH_LONG).show()
                            }
                        },{
                    it.printStackTrace()
                })

                }

                //点击二级会员，将List进行clear()，重新加载别的数据
                v.tv_rebate.setOnClickListener {
                    v.tv_rebate.setBackgroundColor(Color.parseColor("#ffffff"))
                    v.tv_extended.setBackgroundColor(Color.parseColor("#e5e5e5"))
                    vipLevel=2
                    val resultforhttp = getOwnExtendUAndI(vipLevel,year,"http://10.1.5.141:8022/").subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                            mExtensionList.clear()
                            val tempName = listOf("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月")
                            val tempMonth = listOf(it.message.januaryValue,it.message.februaryValue,it.message.marchValue,it.message.aprilValue,it.message.mayValue,it.message.juneValue,it.message.julyValue,it.message.augustValue,it.message.septemberValue,it.message.octoberValue,it.message.novemberValue,it.message.decemberValue)
                            if(tempMonth!=null)
                            {
                                for(j in 0 until tempName.size)
                                {
                                    var extensionStyle = ExtensionStyle(tempName[j],"+${tempMonth[j]}")
                                    mExtensionList.add(extensionStyle)
                                    Log.i("data",mExtensionList.toString())
                                }
                                v.recycler_integral.adapter = ExtensionAdapter(mExtensionList)
                                v.recycler_integral.layoutManager = LinearLayoutManager(context)
                            }
                            else{
                                Toast.makeText(context,"没有数据哦！",Toast.LENGTH_LONG).show()
                            }
                        },{
                    it.printStackTrace()
                })
                }
            }
        }

        v.tv_title_back_integral.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
    }

}