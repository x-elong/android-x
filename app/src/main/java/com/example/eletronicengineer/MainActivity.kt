package com.example.eletronicengineer

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.widget.TextView
import android.widget.Toast
import com.bin.david.form.annotation.ColumnType
import com.bin.david.form.core.TableConfig
import com.bin.david.form.data.Column
import com.bin.david.form.data.table.TableData
import com.electric.engineering.model.MultiStyleItem
import com.electric.engineering.utils.ItemGenerate
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.User
import com.example.eletronicengineer.utils.DisplayHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_need.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var mData:List<MultiStyleItem>
    //List<MultiStyleItem> mData=new ArrayList<>()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
    }
    fun initData()
    {
        supportActionBar?.hide()
        val itemGenerate=ItemGenerate()
        itemGenerate.context=this@MainActivity
        //Demand/DemandGroup(Test debugging).json
        mData=itemGenerate.getJsonFromAsset("Provider/Test.json")
        val recyclerviewAdapter=RecyclerviewAdapter(mData)
        rv_main_content.adapter=recyclerviewAdapter
        rv_main_content.layoutManager=LinearLayoutManager(this@MainActivity)
    }
    fun initTable()
    {
        val column1 = Column<String>("姓名", "name")
        val column2 = Column<String>("年龄", "age")
        val column3 = Column<String>("更新时间", "time")
        val column4 = Column<String>("头像", "portrait")
        //组合列
        val totalColumn1 = Column<Any>("组合列名", column1, column2,column3,column4)
        val userList:MutableList<User> =ArrayList(5)
        val user=User("张三","21","2016-7-5","xxx")
        userList.add(user)
        val tableData:TableData<User> = TableData("表格名",userList,column1,column2,column3,column4)
        st_main_table.tableData=tableData
        st_main_table.setZoom(true)
    }
}
