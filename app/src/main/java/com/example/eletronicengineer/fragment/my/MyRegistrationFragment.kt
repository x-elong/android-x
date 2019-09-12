package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyRegistrationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter.Companion.MESSAGE_SELECT_OK
import com.example.eletronicengineer.custom.CustomDialog
import kotlinx.android.synthetic.main.fragment_my_registration.view.*

class MyRegistrationFragment :Fragment(){

    val mHandler = Handler(Handler.Callback {
        when(it.what)
        {
            MESSAGE_SELECT_OK->
            {
                val selectContent=it.data.getString("selectContent")
                mView.tv_mode_content.text=selectContent
//                mView.sp_demand_moder.
                false
            }
            else->
            {
                false
            }
        }
    })
    lateinit var mView: View
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_my_registration,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_my_registration_back.setOnClickListener {
            activity!!.finish()
        }
        mView.view_sp.setOnClickListener{
            val Option1Items = listOf("需求个人","需求团队","需求租赁","需求三方")
            val Option2Items:List<List<String>> = listOf(listOf("普工","特种作业","专业操作","测量工","驾驶员","九大员","注册类","其他"), listOf("变电施工队","主网施工队","配网施工队","测量设计","马帮运输","桩基服务","非开挖顶拉管作业","试验调试","跨越架","运行维护"), listOf("车辆租赁","工器具租赁","机械租赁","设备租赁"), listOf("培训办证","财务记账","代办资格","标书服务","法律咨询","软件服务","其他"))
            val selectDialog= CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,context!!,mHandler,Option1Items,Option2Items).multiDialog
            selectDialog.show()
        }
        initData()
    }

    private fun initData() {
        mMultiStyleItemList.clear()
        var item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"需求个人","变电施工队","湖南神华永州电力新建工程")
        item.jumpListener=View.OnClickListener {
            val bundle = Bundle()
            (activity as MyRegistrationActivity).switchFragment(MyRegistrationMoreFragment.newInstance(bundle))
        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"需求个人","培训办证","")
        item.jumpListener=View.OnClickListener {
            val bundle = Bundle()
            (activity as MyRegistrationActivity).switchFragment(MyRegistrationMoreFragment.newInstance(bundle))
        }
        mMultiStyleItemList.add(item)
        mView.rv_my_registration.adapter=RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_my_registration.layoutManager=LinearLayoutManager(context)
    }
}