package com.example.eletronicengineer.fragment.shoppingmall

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.SearchActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.fragment.shopping.GoodsDetailsFragment
import com.library.OnChooseEditTextListener
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment(){

    companion object{
        fun newInstance(args:Bundle):SearchFragment{
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }
    lateinit var mView:View
    var chooseEditText=""
    var searchInputContent = ""
    var inputMultiSelectModel=""
    var itemList:MutableList<MultiStyleItem> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_search, container, false)
        searchInputContent = arguments!!.getString("GoodsName")
        initFragment()
        initAdapter()
        if(searchInputContent!="" && itemList.size==0){
            inputMultiSelectModel="商品"
            mView.btn_search.callOnClick()
        }
        return mView
    }

    private fun initAdapter() {
        if(inputMultiSelectModel=="商品"){
            mView.rv_search_content.adapter=RecyclerviewAdapter(itemList)
            mView.rv_search_content.layoutManager= GridLayoutManager(context,2)
        }
        else{
            mView.rv_search_content.adapter= RecyclerviewAdapter(itemList)
            mView.rv_search_content.layoutManager= LinearLayoutManager(context)
        }
    }

    private fun initFragment() {
        mView.tv_search_back.setOnClickListener {
            activity!!.finish()
        }
        val mAdapter = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, arrayListOf("商品","店铺"))
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mView.spinner_moder.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                inputMultiSelectModel=mAdapter.getItem(p2).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        if(inputMultiSelectModel!="")
            mView.spinner_moder.setSelection(arrayListOf("商品","店铺").indexOf(inputMultiSelectModel))
        mView.spinner_moder.adapter = mAdapter
        mView.spinner_moder.setSelection(0)
        mView.choose_edit_text.setOnChooseEditTextListener(object : OnChooseEditTextListener {
            override fun onTextChangeed(text: String) {
                searchInputContent = text
                Log.i("searchInputContent",searchInputContent)
            }
        })
        mView.btn_search.setOnClickListener{
            mView.choose_edit_text.addItem("")
//            Log.i("chooseEditText + searchInputContent","${chooseEditText} + ${searchInputContent}")
//            if(chooseEditText!=searchInputContent){
//                chooseEditText=searchInputContent
//                mView.choose_edit_text.addItem("")
//                mView.choose_edit_text.removeAllItem()
//                mView.choose_edit_text.addItem(chooseEditText)
//                Log.i("choose edit text",mView.choose_edit_text.text)
//                val goodsList:MutableList<MultiStyleItem> = ArrayList()
//                if(inputMultiSelectModel=="商品"){
//                    for (j in 0 until 10){
//                        val item = MultiStyleItem(MultiStyleItem.Options.GOODS,"1","1","1")
//                        item.itemListener = View.OnClickListener {
//                            (activity as SearchActivity).switchFragment(GoodsDetailsFragment(),"")
//                        }
//                        goodsList.add(item)
//                    }
//                    itemList = goodsList
//                }
//                else {
//                    for (j in 0 until 10){
//                        val item = MultiStyleItem(MultiStyleItem.Options.STORE,"1","1","1","1","0")
//                        item.itemListener = View.OnClickListener {
//                            (activity as SearchActivity).switchFragment(ShopFragment(),"")
//                        }
//                        goodsList.add(item)
//                    }
//                    itemList = goodsList
//                }
//                initAdapter()
//            }


//            val intent = Intent(this,ShopListActivity::class.java)
//            intent.putExtra("type",inputMultiSelectModel)
//            startActivity(intent)
        }
    }
}