package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import kotlinx.android.synthetic.main.fragment_inventory_item_more.view.*

class MyInventoryItemMoreFragment :Fragment(){
    var item:List<MultiStyleItem> = ArrayList()
    companion object{
        fun newInstance(args:Bundle):MyInventoryItemMoreFragment{
            val myInventoryItemMoreFragment = MyInventoryItemMoreFragment()
            myInventoryItemMoreFragment.arguments = args
            return myInventoryItemMoreFragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inventory_item_more,container,false)
        view.tv_select_ok.visibility = View.GONE
        view.tv_inventory_item_more_title.setText(arguments!!.getString("title")+"详情")
        view.rv_inventory_item_more_content.adapter = RecyclerviewAdapter(item)
        view.rv_inventory_item_more_content.layoutManager = LinearLayoutManager(view.context)
        view.tv_inventory_item_more_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        return view
    }
}