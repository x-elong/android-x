package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.CustomDialog
import kotlinx.android.synthetic.main.fragemt_with_inventory.view.*

class InventoryFragment : Fragment() {
    companion object{
        fun newInstance(args:Bundle): InventoryFragment
        {
            val inventoryFragment= InventoryFragment()
            inventoryFragment.arguments=args
            return inventoryFragment
        }
    }
    val mutable:MutableList<MultiStyleItem> = ArrayList()
    lateinit var title:String
    lateinit var key:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragemt_with_inventory, container, false)
        key=arguments!!.getString("key")
        title=arguments!!.getString("title")
        view.tv_title_title1.text=title
        initRecyclerviewAdapter(view)
        initOnClickListener(view)
        return view
    }
    fun initRecyclerviewAdapter(view: View){
        var mData:List<MultiStyleItem>
        mutable.clear()
        when(title){
            "车辆"->{
                for (i in 0 until 3) {
                    val multiSryleItem = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "第"+(i+1)+title, false)
                    mutable.add(multiSryleItem)
                    mutable[i].jumpListener=View.OnClickListener {
                        val data= Bundle()
                        data.putInt("position",i)
                        data.putString("key",key)
                        (activity as SupplyActivity).switchFragment(UploadPhoneFragment.newInstance(data),R.id.frame_supply,"Capture")
                    }
                }
            }
            else->{
                for (i in 0 until 3) {
                    val multiSryleItem = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "第"+ (i+1) +title, false)
                    mutable.add(multiSryleItem)
                    mutable[i].jumpListener=View.OnClickListener {
                        val data= Bundle()
                        data.putInt("position",i)
                        data.putString("key",key)
                        (activity as SupplyActivity).switchFragment(UploadPhoneFragment.newInstance(data),R.id.frame_supply,"Capture")
                    }
                }
            }
        }
        mData=mutable
        val adapter = RecyclerviewAdapter(mData)
        view.rv_inventory_fragment_content.adapter = adapter
        view.rv_inventory_fragment_content.layoutManager = LinearLayoutManager(view.context)

    }
    fun initOnClickListener(view:View){
        view.tv_title_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }
        view.button_add.setOnClickListener {

        }
    }
}