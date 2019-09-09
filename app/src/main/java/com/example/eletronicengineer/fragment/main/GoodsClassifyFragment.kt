package com.example.eletronicengineer.fragment.shopping

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.FunctionAdapter
import com.example.eletronicengineer.adapter.ItemMenuAdapter
import com.example.eletronicengineer.aninterface.Function
import com.example.eletronicengineer.aninterface.ItemMenu
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_goods_classify.view.*
import kotlinx.android.synthetic.main.item_goods_menu.view.*


class GoodsClassifyFragment: Fragment() {

    var mItemMenuList = ArrayList<ItemMenu>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_goods_classify,container,false)
        initFragment(view)
        return view
    }
    private fun initFragment(view: View) {
        view.tv_title_back.setOnClickListener {
            activity!!.finish()
        }
        val mFunctionAdapter = initFunctionAdapter()
        var functionAdapter = mFunctionAdapter[0]
        mItemMenuList.clear()
        mItemMenuList.add(ItemMenu("一次设备"))
        mItemMenuList.add(ItemMenu("二次设备"))
        mItemMenuList.add(ItemMenu("装备性材料"))
        mItemMenuList.add(ItemMenu("低压电器"))
        mItemMenuList.add(ItemMenu("基础"))
        mItemMenuList.add(ItemMenu("混凝土配合比材料"))
        mItemMenuList.add(ItemMenu("工器具"))
        mItemMenuList.add(ItemMenu("劳保用品"))
        mItemMenuList.add(ItemMenu("调试设备"))
        mItemMenuList.add(ItemMenu("车辆机械"))
        mItemMenuList.add(ItemMenu("其他材料"))
        mItemMenuList[0].checked=true
        for (j in 0 until mItemMenuList.size){
            mItemMenuList[j].viewOnClickListener = View.OnClickListener {
                mItemMenuList[j].checked = true
                for (i in 0 until mItemMenuList.size){
                    if(j!=i)
                        mItemMenuList[i].checked = false
                }
                view.recycler_right.adapter=mFunctionAdapter[j]
                view.recycler_left.adapter!!.notifyDataSetChanged()
                view.recycler_right.adapter!!.notifyDataSetChanged()
            }
        }
        var itemMenuAdapter = ItemMenuAdapter(mItemMenuList)
        view.recycler_left.adapter = itemMenuAdapter
        view.recycler_left.layoutManager= LinearLayoutManager(context)

        view.recycler_right.adapter = functionAdapter

        view.recycler_right.layoutManager = GridLayoutManager(context,3)

    }

    private fun initFunctionAdapter(): List<FunctionAdapter> {
        val mFunctionAdapter:MutableList<FunctionAdapter> = ArrayList()
        var functionList: MutableList<Function> = ArrayList()
        for (j in 0 until 11){
            mFunctionAdapter.add(FunctionAdapter(ArrayList()))
        }
        functionList = mFunctionAdapter[0].mFunctionList.toMutableList()
        var f = Function("交流变压器", R.drawable.banner,null)
        functionList.add(f)
        f = Function("调压器", R.drawable.banner,null)
        functionList.add(f)
        f = Function("开关柜(箱)", R.drawable.banner,null)
        functionList.add(f)
        f = Function("交流断路器", R.drawable.banner,null)
        functionList.add(f)
        f = Function("负荷开关", R.drawable.banner,null)
        functionList.add(f)
        f = Function("交流隔离开关", R.drawable.banner,null)
        functionList.add(f)
        f = Function("高压熔断器", R.drawable.banner,null)
        functionList.add(f)
        f = Function("避雷针", R.drawable.banner,null)
        functionList.add(f)
        f = Function("电力电容器", R.drawable.banner,null)
        functionList.add(f)
        mFunctionAdapter[0].mFunctionList = functionList

        functionList = mFunctionAdapter[1].mFunctionList.toMutableList()
        f = Function("低压屏（柜）箱", R.drawable.banner,null)
        functionList.add(f)
        f = Function("配电终端", R.drawable.banner,null)
        functionList.add(f)
        f = Function("集中器", R.drawable.banner,null)
        functionList.add(f)
        f = Function("高压试验仪器", R.drawable.banner,null)
        functionList.add(f)
        mFunctionAdapter[1].mFunctionList = functionList

        functionList = mFunctionAdapter[2].mFunctionList.toMutableList()
        f = Function("塔杆类", R.drawable.banner,null)
        functionList.add(f)
        f = Function("导、地线", R.drawable.banner,null)
        functionList.add(f)
        f = Function("电缆", R.drawable.banner,null)
        functionList.add(f)
        f = Function("电缆附件", R.drawable.banner,null)
        functionList.add(f)
        f = Function("光缆", R.drawable.banner,null)
        functionList.add(f)
        f = Function("绝缘子", R.drawable.banner,null)
        functionList.add(f)
        f = Function("金具", R.drawable.banner,null)
        functionList.add(f)
        f = Function("熔丝", R.drawable.banner,null)
        functionList.add(f)
        f = Function("铁附件", R.drawable.banner,null)
        functionList.add(f)
        f = Function("铁封", R.drawable.banner,null)
        functionList.add(f)
        f = Function("其他", R.drawable.banner,null)
        functionList.add(f)
        mFunctionAdapter[2].mFunctionList = functionList

        functionList = mFunctionAdapter[3].mFunctionList.toMutableList()
        f = Function("低压电流互感器", R.drawable.contract,null)
        functionList.add(f)
        f = Function("低压开关", R.drawable.contract,null)
        functionList.add(f)
        mFunctionAdapter[3].mFunctionList = functionList

        functionList = mFunctionAdapter[4].mFunctionList.toMutableList()
        f = Function("电缆直埋", R.drawable.contract,null)
        functionList.add(f)
        f = Function("电缆排管", R.drawable.contract,null)
        functionList.add(f)
        f = Function("电缆沟", R.drawable.contract,null)
        functionList.add(f)
        f = Function("电缆井", R.drawable.contract,null)
        functionList.add(f)
        f = Function("塔杆基础", R.drawable.contract,null)
        functionList.add(f)
        f = Function("设备基础", R.drawable.contract,null)
        functionList.add(f)
        f = Function("破除路面及恢复路面", R.drawable.contract,null)
        functionList.add(f)
        mFunctionAdapter[4].mFunctionList = functionList

        functionList = mFunctionAdapter[5].mFunctionList.toMutableList()
        f = Function("拆分材料", R.drawable.contract,null)
        functionList.add(f)
        f = Function("配合比材料", R.drawable.contract,null)
        functionList.add(f)
        f = Function("配合比2016", R.drawable.contract,null)
        functionList.add(f)
        mFunctionAdapter[5].mFunctionList = functionList

        functionList = mFunctionAdapter[6].mFunctionList.toMutableList()
        f = Function("安全工器具", R.drawable.contract,null)
        functionList.add(f)
        f = Function("施工工器具", R.drawable.contract,null)
        functionList.add(f)
        mFunctionAdapter[6].mFunctionList = functionList

        functionList = mFunctionAdapter[7].mFunctionList.toMutableList()
        f = Function("头部护具类", R.drawable.contract,null)
        functionList.add(f)
        f = Function("呼吸护具类", R.drawable.contract,null)
        functionList.add(f)
        f = Function("眼(面)护具类", R.drawable.contract,null)
        functionList.add(f)
        f = Function("防护眼类", R.drawable.contract,null)
        functionList.add(f)
        f = Function("防护鞋类", R.drawable.contract,null)
        functionList.add(f)
        f = Function("防坠落护具类", R.drawable.contract,null)
        functionList.add(f)
        mFunctionAdapter[7].mFunctionList = functionList

        functionList = mFunctionAdapter[8].mFunctionList.toMutableList()
        f = Function("一次调试设备", R.drawable.contract,null)
        functionList.add(f)
        f = Function("二次调试设备", R.drawable.contract,null)
        functionList.add(f)
        f = Function("计量设备", R.drawable.contract,null)
        functionList.add(f)
        mFunctionAdapter[8].mFunctionList = functionList

        functionList = mFunctionAdapter[9].mFunctionList.toMutableList()
        f = Function("车辆", R.drawable.contract,null)
        functionList.add(f)
        f = Function("机械", R.drawable.contract,null)
        functionList.add(f)
        mFunctionAdapter[9].mFunctionList = functionList

        functionList = mFunctionAdapter[10].mFunctionList.toMutableList()
        mFunctionAdapter[10].mFunctionList = functionList
        return mFunctionAdapter
    }
}