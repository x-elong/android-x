package com.example.eletronicengineer.fragment.sdf

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

class InventoryItemMoreFragment : Fragment(){
    companion object{
        fun newInstance(args:Bundle):InventoryItemMoreFragment{
            val inventoryItemMoreFragment = InventoryItemMoreFragment()
            inventoryItemMoreFragment.arguments = args
            return inventoryItemMoreFragment
        }
    }
    lateinit var mView: View
    var adapter: RecyclerviewAdapter = RecyclerviewAdapter(ArrayList())
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_inventory_item_more,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        adapter = RecyclerviewAdapter(arguments!!.getSerializable("inventoryItem") as List<MultiStyleItem>)
        mView.rv_inventory_item_more_content.adapter = adapter
        mView.rv_inventory_item_more_content.layoutManager = LinearLayoutManager(context)
    }
}