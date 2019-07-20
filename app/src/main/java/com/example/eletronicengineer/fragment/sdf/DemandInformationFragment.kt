package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.eletronicengineer.R
import android.content.Intent
import com.example.eletronicengineer.activity.DemandActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.demand.view.*


class DemandInformationFragment: Fragment(){
  lateinit var demandAdd:Button
  val Option1Items = listOf<String>("需求个人","需求团队","需求租赁","需求三方")
  val Option2Items:List<List<String>> = listOf(listOf("普工","特种作业","专业操作","测量工","驾驶员","九大员","注册类","其他"), listOf("变电施工队","主网施工队","配网施工队","测量设计","马帮运输","桩基服务","非开挖顶拉管作业","试验调试","跨越架","运行维护"), listOf("车辆租赁","工器具租赁","机械租赁","设备租赁"), listOf("培训办证","财务记账","代办资格","标书服务","法律咨询","软件服务","其他"))
  var mHandler:Handler= Handler(Handler.Callback {
    when(it.what)
    {
      RecyclerviewAdapter.MESSAGE_SELECT_OK ->{
        val selectContent=it.data.getString("selectContent")
        val adapterGenerate = AdapterGenerate()
        val type = adapterGenerate.getType(selectContent)
        val intent = Intent(context, DemandActivity::class.java)
        intent.putExtra("type",type)
        startActivity(intent)
        false
      }
      else->
      {
        false
      }
    }
  })
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.demand, container, false)
    select(view)
    return view
  }
  private fun select(view:View) {
    demandAdd=view.demand_add
    demandAdd.setOnClickListener {
      //Toast.makeText(view.context,"这是一个选择框",Toast.LENGTH_LONG).show()
      val selectDialog= CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,view.context,mHandler,Option1Items,Option2Items).multiDialog
      selectDialog.show()
    }
  }
}