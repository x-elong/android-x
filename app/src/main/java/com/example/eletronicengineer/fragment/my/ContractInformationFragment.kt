package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyInformationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import kotlinx.android.synthetic.main.fragment_contract_information.view.*

class ContractInformationFragment :Fragment(){
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_contract_information,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        val mMultiStyleItemList=ArrayList<MultiStyleItem>()
        var item = MultiStyleItem(MultiStyleItem.Options.TITLE,"合同信息","2")
        mMultiStyleItemList.add(item)
        item.backListener = View.OnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        item.tvSelect = View.OnClickListener {
            val bundle = Bundle()
            (activity as MyInformationActivity).switchFragment(AddContractInformationFragment.newInstance(bundle),"Education")
        }
        item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"合同期限","合同类型","合同公司")
        item.jumpListener = View.OnClickListener {
            val bundle = Bundle()
            (activity as MyInformationActivity).switchFragment(AddContractInformationFragment.newInstance(bundle),"AddEducation")
        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"50天","不要式子合同","湖南神华永州电力新建工程")
        item.jumpListener = View.OnClickListener {
            val bundle = Bundle()
            (activity as MyInformationActivity).switchFragment(AddContractInformationFragment.newInstance(bundle), "AddEducation")
        }
        mMultiStyleItemList.add(item)
//        (mView.rv_my_release_content.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        mView.rv_contract_information_content.adapter= RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_contract_information_content.layoutManager= LinearLayoutManager(context)
    }
}