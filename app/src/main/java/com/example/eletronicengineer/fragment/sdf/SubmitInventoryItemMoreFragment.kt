package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import kotlinx.android.synthetic.main.fragment_inventory_item_more.view.*

class SubmitInventoryItemMoreFragment : Fragment(){
    companion object{
        fun newInstance(args: Bundle):SubmitInventoryItemMoreFragment{
            val inventoryItemMoreFragment = SubmitInventoryItemMoreFragment()
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
        var mData=arguments!!.getSerializable("inventoryItem") as List<MultiStyleItem>
       mView.tv_inventory_item_more_back.setOnClickListener {
          mData[0].selected =0//点击了返回按钮，*去掉
           val fragment =
               activity!!.supportFragmentManager.findFragmentByTag("inventory") as SubmitInventoryFragment
           fragment.update(adapter.mData)
           activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.tv_inventory_item_more_title.setText(arguments!!.getString("type")+"详情")
       mView.tv_select_ok.setOnClickListener {
           val networkAdapter= NetworkAdapter(mData,mView.context)
           if(networkAdapter.check()) {
               // Log.i("fragment is ok?",(activity.supportFragmentManager.findFragmentByTag("inventory") is InventoryFragment).toString())
               val fragment =
                   activity!!.supportFragmentManager.findFragmentByTag("inventory") as SubmitInventoryFragment
               activity!!.supportFragmentManager.popBackStackImmediate()
               mData[0].selected = 1
               fragment.update(adapter.mData)
           }
        }
        adapter = RecyclerviewAdapter(arguments!!.getSerializable("inventoryItem") as List<MultiStyleItem>)
        mView.rv_inventory_item_more_content.adapter = adapter
        mView.rv_inventory_item_more_content.layoutManager = LinearLayoutManager(context)
    }
}