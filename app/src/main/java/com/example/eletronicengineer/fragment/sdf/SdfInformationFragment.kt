package com.example.eletronicengineer.fragment.sdf

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.DemandActivity
import com.example.eletronicengineer.activity.MyCertificationActivity
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.sdf.*
import kotlinx.android.synthetic.main.sdf_information.*
import kotlinx.android.synthetic.main.sdf_information.view.*
import kotlinx.android.synthetic.main.sdf_information.view.demand_release_tv

class SdfInformationFragment:Fragment(){
    lateinit var titleReturn:View
    lateinit var titleContent:TextView
    lateinit var tv_add_information:TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sdf_information,container,false)
        titleReturn=activity!!.return_iv
        titleContent=activity!!.tv_title
        tv_add_information = activity!!.tv_add_information
        initonClick(view)
        return view
    }
    fun initonClick(view: View){
        view.external_bidding_tv.setOnClickListener{
            activity!!.supportFragmentManager.popBackStackImmediate()
            tv_add_information.visibility = View.VISIBLE
            tv_add_information.setOnClickListener {
            }
            titleReturn.visibility=View.VISIBLE
            titleReturn.setOnClickListener {
                activity!!.supportFragmentManager.popBackStackImmediate()
                tv_add_information.visibility = View.GONE
                titleReturn.visibility=View.INVISIBLE
                titleContent.text="招投供需"
            }
        }
        view.market_tender_tv.setOnClickListener {
            switchFragment(MarketTenderFragment(),R.id.frame_main)
            tv_add_information.visibility = View.VISIBLE
            tv_add_information.setOnClickListener {

            }
            titleReturn.visibility=View.VISIBLE
            titleReturn.setOnClickListener {
                activity!!.supportFragmentManager.popBackStackImmediate()
                tv_add_information.visibility = View.GONE
                titleReturn.visibility=View.INVISIBLE
                titleContent.text="招投供需"
            }
        }
        view.demand_release_tv.setOnClickListener {
            switchFragment(DemandInformationFragment(),R.id.fragment_sdf)
            titleContent.text="需求发布"
            tv_add_information.visibility = View.VISIBLE
            tv_add_information.setOnClickListener {
                val Option1Items = listOf("需求个人","需求团队","需求租赁","需求三方")
                val Option2Items:List<List<String>> = listOf(listOf("普工","特种作业","专业操作","测量工","驾驶员","九大员","注册类","其他"), listOf("变电施工队","主网施工队","配网施工队","测量设计","马帮运输","桩基服务","非开挖顶拉管作业","试验调试","跨越架","运行维护"), listOf("车辆租赁","工器具租赁","机械租赁","设备租赁"), listOf("培训办证","财务记账","代办资格","标书服务","法律咨询","软件服务","其他"))
                var mHandler:Handler= Handler(Handler.Callback {
                    when(it.what)
                    {
                        RecyclerviewAdapter.MESSAGE_SELECT_OK ->{
                            val selectContent=it.data.getString("selectContent")
                            val selectContent2=it.data.getString("selectContent2")
                            val adapterGenerate = AdapterGenerate()
                            val type = adapterGenerate.getType(selectContent)
                            val intent = Intent(context, DemandActivity::class.java)
                            intent.putExtra("type",type)
                            intent.putExtra("selectContent2",selectContent2)
                            startActivity(intent)
                            false
                        }
                        else->
                        {
                            false
                        }
                    }
                })
                val selectDialog= CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,view.context,mHandler,Option1Items,Option2Items).multiDialog
                selectDialog.show()
            }
            titleReturn.visibility=View.VISIBLE
            titleReturn.setOnClickListener {
                activity!!.supportFragmentManager.popBackStackImmediate()
                tv_add_information.visibility = View.GONE
                titleReturn.visibility=View.INVISIBLE
                titleContent.text="招投供需"
            }
        }
        view.supply_announcement_tv.setOnClickListener {
            switchFragment(SupplyInformationFragment(),R.id.fragment_sdf)
            titleContent.text="供应发布"
            tv_add_information.visibility = View.VISIBLE
            tv_add_information.setOnClickListener {
                val option1Items= listOf("个人劳务","团队服务","租赁服务","三方服务")
                val option2Items= listOf(listOf("普工","特种作业","专业操作","测量工","驾驶员","九大员","注册类","其他"), listOf("变电施工队","主网施工队","配网施工队","测量设计","马帮运输","桩基服务","非开挖顶拉管作业","试验调试","跨越架","运行维护"), listOf("车辆租赁","工器具租赁","机械租赁","设备租赁"), listOf("培训办证","财务记账","代办资格","标书服务","法律咨询","软件服务","其他"))
                val mHander=Handler(Handler.Callback {
                    when(it.what){
                        RecyclerviewAdapter.MESSAGE_SELECT_OK->{
                            val selectContent=it.data.getString("selectContent")
                            val selectContent2=it.data.getString("selectContent2")
                            val adapterGenerate = AdapterGenerate()
                            val type = adapterGenerate.getType(selectContent)
                            val intent = Intent(context, SupplyActivity::class.java)
                            intent.putExtra("type",type)
                            intent.putExtra("selectContent2",selectContent2)
                            startActivity(intent)
                            false
                        }
                        else->{
                            false
                        }
                    }
                })
                val selectDialog = CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,view.context,mHander,option1Items,option2Items).multiDialog
                selectDialog.show()
            }
            titleReturn.visibility=View.VISIBLE
            titleReturn.setOnClickListener {
                activity!!.supportFragmentManager.popBackStackImmediate()
                tv_add_information.visibility = View.GONE
                titleReturn.visibility=View.INVISIBLE
                titleContent.text="招投供需"
            }
        }
    }
    fun switchFragment(fragment: Fragment,frameLayout:Int) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        //隐藏上个Fragment
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}