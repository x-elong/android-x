package com.example.eletronicengineer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.electric.engineering.utils.ItemGenerate
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_need.*

class MainActivity : AppCompatActivity() {
    lateinit var mData:List<MultiStyleItem>
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
        mData=itemGenerate.getJsonFromAsset("Demand/DemandGroup(Test debugging).json")
        val recyclerviewAdapter=RecyclerviewAdapter(mData)
        rv_main_content.adapter=recyclerviewAdapter
        rv_main_content.layoutManager=LinearLayoutManager(this@MainActivity)
    }
}
