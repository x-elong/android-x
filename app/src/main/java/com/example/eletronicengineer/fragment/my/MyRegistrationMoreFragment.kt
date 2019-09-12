package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyRegistrationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_my_registration.*
import kotlinx.android.synthetic.main.fragment_my_registration_more.view.*

class MyRegistrationMoreFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):MyRegistrationMoreFragment{
            val myRegistrationMoreFragment = MyRegistrationMoreFragment()
            myRegistrationMoreFragment.arguments = args
            return myRegistrationMoreFragment
        }
    }

    lateinit var mView: View
    val mMultiStyleItemList:MutableList<MultiStyleItem> =ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_my_registration_more,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_my_registration_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        initData()
    }

    private fun initData() {
        mMultiStyleItemList.clear()


        var item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,"项目名称：","湖南神华永州电力新建工程")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,"需求类型：","普工")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,"需求专业：","普工")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,"联系人名字：","王一")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_LEFT,"联系人电话：","18300000000")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"工程量清册"," 查看 ")
        item.jumpListener=View.OnClickListener {

        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"车辆清册"," 查看 ")
        item.jumpListener=View.OnClickListener {

        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"成员清册"," 查看 ")
        item.jumpListener=View.OnClickListener {

        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"工器具清册"," 查看 ")
        item.jumpListener=View.OnClickListener {

        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"机械清册"," 查看 ")
        item.jumpListener=View.OnClickListener {

        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"薪资清册"," 查看 ")
        item.jumpListener=View.OnClickListener {

        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_TEXTAREA,"备注信息：","     备注信息备注信息备注信息备注信息备注信息备注信息备注信息备注信息")
        mMultiStyleItemList.add(item)
        mView.rv_registration_more.adapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_registration_more.layoutManager = LinearLayoutManager(context)
    }
}