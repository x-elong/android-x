package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.utils.AdapterGenerate
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_inventory_item_more.view.*

class SupplyPublishInventoryItemMoreFragment:Fragment(){
        companion object{
            fun newInstance(args: Bundle,data:List<MultiStyleItem>): SupplyPublishInventoryItemMoreFragment {
                val inventoryItemMoreFragment = SupplyPublishInventoryItemMoreFragment()
                inventoryItemMoreFragment.arguments = args
                inventoryItemMoreFragment.mData = data
                return inventoryItemMoreFragment
            }
        }
        lateinit var mView: View
    lateinit var type: String
    lateinit var mData:List<MultiStyleItem>
    var adapter: RecyclerviewAdapter? = null
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            mView = inflater.inflate(R.layout.fragment_inventory_item_more,container,false)
            mView.tv_inventory_item_more_back.setOnClickListener {
                activity!!.supportFragmentManager.popBackStackImmediate()
            }
            type = arguments!!.getString("type")
            mView.tv_inventory_item_more_title.setText(type + "详情")
            mView.tv_select_ok.setOnClickListener {
                val networkAdapter= NetworkAdapter(adapter!!.mData,mView.context)
                if(networkAdapter.check()) {
                    val fragment =
                        activity!!.supportFragmentManager.findFragmentByTag("publishInventory") as SupplyPublishInventoryFragment
                    adapter!!.mData[0].selected = 1
                    fragment.update(adapter!!.mData)
                    activity!!.supportFragmentManager.popBackStackImmediate()
                }
            }
            if(adapter==null){
                val result = Observable.create<RecyclerviewAdapter> {
                    it.onNext(RecyclerviewAdapter(copyData(switchAdapter())))
                }   .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        adapter = it
                        mView.rv_inventory_item_more_content.adapter = adapter
                        mView.rv_inventory_item_more_content.layoutManager = LinearLayoutManager(context)
                    }
            }
            else{
                mView.rv_inventory_item_more_content.adapter = adapter
                mView.rv_inventory_item_more_content.layoutManager = LinearLayoutManager(context)
            }
            return mView
        }

    private fun switchAdapter():List<MultiStyleItem>{
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context= context!!
        val bundle = Bundle()
        val mData:List<MultiStyleItem>
        adapterGenerate.activity= activity as AppCompatActivity
        when(type){
            "成员清册发布"->
            {
                bundle.putString("type",type)
                mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
            }
            "车辆清册发布"->
            {
                bundle.putString("type",type)
                mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
            }
            "工器具清册发布"->
            {
                bundle.putString("type",type)
                mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
            }
            "租赁清册发布"->
            {
                bundle.putString("type",type)
                mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
            }
            else->{
                bundle.putString("type","成员清册发布")
                mData = adapterGenerate.SupplyPublishDetailList(bundle).mData
            }
        }
        return mData
    }
    fun copyData(dataList: List<MultiStyleItem>): List<MultiStyleItem> {
        val data = dataList
        data[0].id = mData[0].id
        for (j in mData) {
            val position = mData.indexOf(j)
            when (j.options) {
                MultiStyleItem.Options.SELECT_DIALOG,
                MultiStyleItem.Options.TWO_OPTIONS_SELECT_DIALOG,
                MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG -> {
                    data[position].selectContent = j.selectContent
                }
                MultiStyleItem.Options.SHIFT_INPUT->{
                    data[position].shiftInputPicture = j.shiftInputPicture
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
    fun refresh(imagePath:String,position:Int){
        adapter!!.mData[position].shiftInputPicture = imagePath
        adapter!!.notifyDataSetChanged()
    }
}