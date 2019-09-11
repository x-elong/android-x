package com.example.eletronicengineer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.activity_shop_list.*
import kotlinx.android.synthetic.main.mall.view.*

class ShopListActivity : AppCompatActivity() {
    var inputMultiSelectModel = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)
        tv_shop_list_back.setOnClickListener {
            finish()
        }
        inputMultiSelectModel = getIntent().getStringExtra("type")
        val mAdapter = ArrayAdapter(this,R.layout.item_dropdown, arrayListOf("商品","店铺"))
        mAdapter.setDropDownViewResource(R.layout.item_dropdown)
        spinner_moder.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                inputMultiSelectModel=mAdapter.getItem(p2).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        spinner_moder.adapter = mAdapter
        spinner_moder.setSelection(0)
        if(inputMultiSelectModel!="")
            spinner_moder.setSelection(arrayListOf("商品","店铺").indexOf(inputMultiSelectModel))
        tv_shop_list_search_bg.setOnClickListener {
            val intent = Intent(this,SearchActivity::class.java)
            intent.putExtra("type",inputMultiSelectModel)
            startActivity(intent)
        }
    }
}
