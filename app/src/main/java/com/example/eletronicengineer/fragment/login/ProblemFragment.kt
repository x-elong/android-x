package com.example.eletronicengineer.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.fragment.my.HelpCenterMoreFragment
import com.example.eletronicengineer.utils.FragmentHelper
import kotlinx.android.synthetic.main.fragment_problem.view.*

class ProblemFragment : Fragment()  {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_problem,container,false)
        initFragment(view)
        return view
    }

    private fun initFragment(v: View) {
        val bundle = Bundle()
        v.tv_problem_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }

        val mMultiStyleItemList = ArrayList<MultiStyleItem>()
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"如何找到电企通",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,HelpCenterMoreFragment.newInstance(bundle),R.id.frame_login,"")
            }
            mMultiStyleItemList.add(item)
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"如何注册与登录",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,
                    HelpCenterMoreFragment.newInstance(bundle),R.id.frame_login,"")
            }
            mMultiStyleItemList.add(item)
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"邮箱绑定",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,HelpCenterMoreFragment.newInstance(bundle),R.id.frame_login,"")
            }
            mMultiStyleItemList.add(item)
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"忘记密码",false)
            item.jumpListener = View.OnClickListener {
                bundle.putString("title",item.shiftInputTitle)
                FragmentHelper.switchFragment(activity!!,HelpCenterMoreFragment.newInstance(bundle),R.id.frame_login,"")
            }
            mMultiStyleItemList.add(item)
        }
        v.rv_problem_help.adapter = RecyclerviewAdapter(mMultiStyleItemList)
        v.rv_problem_help.layoutManager = LinearLayoutManager(v.context)
    }

}