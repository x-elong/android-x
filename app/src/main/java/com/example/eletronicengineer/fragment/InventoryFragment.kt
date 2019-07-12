package com.example.eletronicengineer.fragment

import android.content.Intent
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
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.fragemt_with_inventory.view.*

class InventoryFragment : Fragment() {
    companion object{
        fun newInstance(args:Bundle):InventoryFragment
        {
            val inventoryFragment=InventoryFragment()
            inventoryFragment.arguments=args
            return inventoryFragment
        }
    }

    lateinit var title:String
    var type:Int=-1
    val option1Items= listOf("查看","删除")
    val mHander= Handler(Handler.Callback {
        when(it.what){
            RecyclerviewAdapter.MESSAGE_SELECT_OK->{
                val selectContent=it.data.getString("selectContent")
                when(selectContent){
                    "查看"->{
                        val data= Bundle()
                        data.putInt("type", type)
                        data.putString("title",title)
                        val fragment=ShiftInputFragment.newInstance(data)
                        (activity as SupplyActivity).switchFragment(fragment,"ShiftInput")
                    }
                    "删除"->{

                    }
                }
                false
            }
            else->{
                false
            }
        }
    })
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragemt_with_inventory, container, false)
        type=arguments!!.getInt("type")
        title=arguments!!.getString("title")
        view.tv_title_title1.text=title+"清册"
        title=title.replace("现场","")
        initRecyclerviewAdapter(view)
        initOnClickListener(view)
        return view
    }
    fun initRecyclerviewAdapter(view: View){
        val mutable:MutableList<MultiStyleItem> = ArrayList()
        var mData:List<MultiStyleItem>
        when(title){
            "车辆"->{
                view.tv_tip1.text="类    别"
                view.tv_tip2.text="规格型号"
                for (i in 0 until 10) {
                    val multiSryleItem = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "第"+(i+1)+title, "kkk")
                    mutable.add(multiSryleItem)
                    mutable[i].jumpListener=View.OnClickListener {
                        val selectDialog= CustomDialog(CustomDialog.Options.SELECT_DIALOG,view.context,option1Items,mHander).dialog
                        selectDialog.show()
                    }
                }
            }
            else->{
                view.tv_tip1.text="工    种"
                view.tv_tip2.text="姓    名"
                for (i in 0 until 10) {
                    val multiSryleItem = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "第"+ (i+1) +title, "哈哈哈")
                    mutable.add(multiSryleItem)
                    mutable[i].jumpListener=View.OnClickListener {
                        val selectDialog= CustomDialog(CustomDialog.Options.SELECT_DIALOG,view.context,option1Items,mHander).dialog
                        selectDialog.show()
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
            val data= Bundle()
            data.putInt("type", type)
            data.putString("title",title)
            val fragment=ShiftInputFragment.newInstance(data)
            (activity as SupplyActivity).switchFragment(fragment,"ShiftInput")
        }
    }
}