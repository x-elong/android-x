package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.DemandDisplayActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.fragemt_inventory_item_more.view.*

class SubmitInventoryItemMoreFragment : Fragment(){
    companion object{
        fun newInstance(args: Bundle):SubmitInventoryItemMoreFragment{
            val inventoryItemMoreFragment = SubmitInventoryItemMoreFragment()
            inventoryItemMoreFragment.arguments = args
            return inventoryItemMoreFragment
        }
    }
    lateinit var mView: View
    lateinit var type: String
    var adapter: RecyclerviewAdapter = RecyclerviewAdapter(ArrayList())
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragemt_inventory_item_more,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
       mView.tv_inventory_item_more_back.setOnClickListener {
        }
        type = arguments!!.getString("type")
        mView.tv_inventory_item_more_title.setText(type + "详情")
       mView.tv_select_ok.setOnClickListener {
           val networkAdapter= NetworkAdapter(adapter.mData,mView.context)
           if(networkAdapter.check()) {
               val fragment =
                   activity!!.supportFragmentManager.findFragmentByTag("inventory") as SubmitInventoryFragment
               activity!!.supportFragmentManager.popBackStackImmediate()
               adapter.mData[0].selected = 1
               fragment.update(adapter.mData)
           }
        }
        adapter = RecyclerviewAdapter(copyData(switchAdapter()))
        mView.rv_inventory_item_more_content.adapter = adapter
        mView.rv_inventory_item_more_content.layoutManager = LinearLayoutManager(context)
    }
    private fun switchAdapter():List<MultiStyleItem>{
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = context!!
        val bundle = Bundle()
        adapterGenerate.activity=activity as DemandDisplayActivity
        lateinit var mData:List<MultiStyleItem>
        when(type){
            "成员清册" -> {
                bundle.putString("type", type)
                mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
            }
            "车辆租赁车辆清册" -> {
                bundle.putString("type", type)
                mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
            }
            "车辆清册" -> {
                bundle.putString("type", type)
                mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
            }
            "机械清册"->
            {
                bundle.putString("type",type)
                mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
            }
            "租赁清册"->
            {
                bundle.putString("type",type)
                mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
            }
            "三方清册"->
            {
                bundle.putString("type",type)
                mData = adapterGenerate.ApplicationSubmitDetailList(bundle).mData
            }

            else->{

            }
        }
        return mData
    }
    fun copyData(dataList: List<MultiStyleItem>): List<MultiStyleItem> {
        val data = dataList
        val mData = (arguments!!.getSerializable("inventoryItem") as List<MultiStyleItem>)
        for (j in mData) {
            val position = mData.indexOf(j)
            when (j.options) {
                MultiStyleItem.Options.SELECT_DIALOG,
                MultiStyleItem.Options.TWO_OPTIONS_SELECT_DIALOG,
                MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG -> {
                    data[position].selectContent = j.selectContent
                }
                MultiStyleItem.Options.INPUT_WITH_UNIT -> {
                    data[position].inputUnitContent = j.inputUnitContent
                }
                MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT -> {
                    data[position].inputMultiContent = j.inputMultiContent
                    data[position].inputMultiSelectUnit = j.inputMultiSelectUnit
                }
                MultiStyleItem.Options.SINGLE_INPUT -> {
                    data[position].inputSingleContent = j.inputSingleContent
                }
                MultiStyleItem.Options.HINT -> {
                    data[position].hintContent = j.hintContent
                }
                MultiStyleItem.Options.INPUT_WITH_TEXTAREA -> {
                    data[position].textAreaContent = j.textAreaContent
                }
                MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT -> {
                    data[position].singleDisplayRightContent = j.singleDisplayRightContent
                }
            }
        }
        return data
    }
}