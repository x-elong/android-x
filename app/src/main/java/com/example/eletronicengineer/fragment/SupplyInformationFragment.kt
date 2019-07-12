package com.example.eletronicengineer.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.model.Conversion
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.application.*
import kotlinx.android.synthetic.main.supply.view.*

class SupplyInformationFragment : Fragment() {

    lateinit var supply_add:Button
    val option1Items= listOf("个人劳务","团队服务","租赁服务","三方服务")
    val option2Items= listOf(listOf("普工","特种作业","专业操作","测量工","驾驶员","九大员","注册类","其他"), listOf("变电施工队","主网施工队","配网施工队","测量设计","马帮运输","桩基服务","非开挖顶拉管作业","试验调试","跨越架","运行维护"), listOf("车辆租赁","工器具租赁","机械租赁","设备租赁"), listOf("培训办证","财务记账","代办资格","标书服务","法律咨询","软件服务","其他"))
    val mHander=Handler(Handler.Callback {
        when(it.what){
            RecyclerviewAdapter.MESSAGE_SELECT_OK->{
                val selectContent=it.data.getString("selectContent")
                val adapterGenerate = AdapterGenerate()
                val type = adapterGenerate.getType(selectContent)
                val intent = Intent(context,SupplyActivity::class.java)
                intent.putExtra("type",type)
                startActivity(intent)
                false
            }
            else->{
                false
            }
        }
    })
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.supply,container,false)
        select(view)
        return view
    }
    fun select(view: View){
        supply_add=view.supply_add
        supply_add.setOnClickListener {
            val selectDialog = CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,view.context,mHander,option1Items,option2Items).multiDialog
            selectDialog.show()
        }
    }
}