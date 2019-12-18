package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.DemandActivity
import com.example.eletronicengineer.activity.MyReleaseActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.fragment_inventory_item_more.view.*
import kotlin.collections.ArrayList

class PublishInventoryItemMoreFragment:Fragment() {
    companion object {
        fun newInstance(args: Bundle): PublishInventoryItemMoreFragment {
            val inventoryItemMoreFragment = PublishInventoryItemMoreFragment()
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
        mView = inflater.inflate(R.layout.fragment_inventory_item_more, container, false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_inventory_item_more_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
            type = arguments!!.getString("type")
            mView.tv_inventory_item_more_title.setText(type + "详情")
            mView.tv_select_ok.setOnClickListener {
                val networkAdapter = NetworkAdapter(adapter.mData, mView.context)
                if (networkAdapter.check()) {
                    // Log.i("fragment is ok?",(activity.supportFragmentManager.findFragmentByTag("inventory") is InventoryFragment).toString())
                    val fragment =
                        activity!!.supportFragmentManager.findFragmentByTag("publishInventory") as PublishInventoryFragment
                    adapter.mData[0].selected = 1
                    fragment.update(adapter.mData)
                    activity!!.supportFragmentManager.popBackStackImmediate()
                }
            }
            adapter = RecyclerviewAdapter(copyData(switchAdapter()))
            mView.rv_inventory_item_more_content.adapter = adapter
            mView.rv_inventory_item_more_content.layoutManager = LinearLayoutManager(context)
    }
        fun switchAdapter():List<MultiStyleItem> {
            val adapterGenerate = AdapterGenerate()
            adapterGenerate.context = context!!
            adapterGenerate.activity =
                if (activity is DemandActivity) activity as DemandActivity else activity as MyReleaseActivity
            val bundle = Bundle()
            val mData: List<MultiStyleItem>
            when (type) {
                "成员清册发布" -> {
                    bundle.putString("type", type)
                    mData = adapterGenerate.PublishDetailList(bundle).mData
                }
                "车辆清册发布" -> {
                    bundle.putString("type", type)
                    mData = adapterGenerate.PublishDetailList(bundle).mData
                }
                "租赁清册发布" -> {
                    bundle.putString("type", type)
                    //bundle.putSerializable("listData4",arguments!!.getSerializable("listData4"))
                    mData = adapterGenerate.PublishDetailList(bundle).mData
                }
                "三方服务清册发布" -> {
                    bundle.putString("type", type)
                    // bundle.putSerializable("listData4",arguments!!.getSerializable("listData4"))
                    mData = adapterGenerate.PublishDetailList(bundle).mData
                }

                else -> {
                    bundle.putString("type", type)
                    mData = adapterGenerate.PublishDetailList(bundle).mData
                }
            }
            return mData
        }

        fun copyData(dataList: List<MultiStyleItem>): List<MultiStyleItem> {
            val data = dataList
            val mData = (arguments!!.getSerializable("inventoryItem") as List<MultiStyleItem>)
            data[0].id = mData[0].id
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
