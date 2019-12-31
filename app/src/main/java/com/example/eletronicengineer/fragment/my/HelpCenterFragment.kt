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
import com.example.eletronicengineer.utils.FragmentHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_help_center.view.*

class HelpCenterFragment :Fragment(){
    lateinit var mView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_help_center,container,false)
        Observable.create<RecyclerviewAdapter> {
            it.onNext(initFragment())
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
            .subscribe {
                mView.rv_help_center_content.adapter = it
                mView.rv_help_center_content.layoutManager = LinearLayoutManager(mView.context)
            }
        return mView
    }

    private fun initFragment():RecyclerviewAdapter {
        mView.tv_help_center_back.setOnClickListener {
            activity!!.finish()
        }
        val bundle = Bundle()
        val mMultiStyleItemList = ArrayList<MultiStyleItem>()
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"如何找到电企通",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,HelpCenterMoreFragment.newInstance(bundle),R.id.frame_help_center,"")
            }
            mMultiStyleItemList.add(item)
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"如何注册与登录",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,HelpCenterMoreFragment.newInstance(bundle),R.id.frame_help_center,"")
            }
            mMultiStyleItemList.add(item)
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"如何个人认证",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,HelpCenterMoreFragment.newInstance(bundle),R.id.frame_help_center,"")
            }
            mMultiStyleItemList.add(item)
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"如何公司认证",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,HelpCenterMoreFragment.newInstance(bundle),R.id.frame_help_center,"")
            }
            mMultiStyleItemList.add(item)
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"邮箱绑定",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,HelpCenterMoreFragment.newInstance(bundle),R.id.frame_help_center,"")
            }
            mMultiStyleItemList.add(item)
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"忘记密码",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,HelpCenterMoreFragment.newInstance(bundle),R.id.frame_help_center,"")
            }
            mMultiStyleItemList.add(item)
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"如何开通VIP会员",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,HelpCenterMoreFragment.newInstance(bundle),R.id.frame_help_center,"")
            }
            mMultiStyleItemList.add(item)
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"报名查看",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,HelpCenterMoreFragment.newInstance(bundle),R.id.frame_help_center,"")
            }
            mMultiStyleItemList.add(item)
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"付费帮助",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,HelpCenterMoreFragment.newInstance(bundle),R.id.frame_help_center,"")
            }
            mMultiStyleItemList.add(item)
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"电企通会员协议",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,HelpCenterMoreFragment.newInstance(bundle),R.id.frame_help_center,"")
            }
            mMultiStyleItemList.add(item)
        }
        return RecyclerviewAdapter(mMultiStyleItemList)
    }
}