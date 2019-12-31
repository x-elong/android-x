package com.example.eletronicengineer.fragment.projectdisk

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ProfessionalActivity
import com.example.eletronicengineer.activity.SearchActivity
import com.example.eletronicengineer.adapter.CheckBoxAdapter
import com.example.eletronicengineer.aninterface.CheckBoxStyle
import com.example.eletronicengineer.fragment.projectdisk.ProjectMoreFragment
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.model.Constants
import kotlinx.android.synthetic.main.checkbox.view.*
import kotlinx.android.synthetic.main.checkbox.view.*
import kotlinx.android.synthetic.main.fragment_project_more.view.*

class CheckBoxFragment : Fragment() {
    companion object{
        fun newInstance(args:Bundle): CheckBoxFragment
        {
            val fragment= CheckBoxFragment()
            fragment.arguments=args
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.checkbox, container, false)
        val adapterGenerate= AdapterGenerate()
        adapterGenerate.context=view.context
        adapterGenerate.activity=activity as AppCompatActivity
        // alterPosition time:2019/7/27
        // function:跳转到搜索界面
        view.btn_search.setOnClickListener {
            startActivity(Intent(activity,SearchActivity::class.java))
        }
        val adapter = switchAdapterCheck(adapterGenerate,arguments!!.getInt("type"),view)
        view.check_recycler.adapter = adapter
        view.check_recycler.layoutManager= LinearLayoutManager(context)
        return view
    }
    // alterPosition time:2019/7/20
    // function:
    fun switchAdapterCheck(adapterGenerate: AdapterGenerate, type: Int,view: View): CheckBoxAdapter {
        lateinit var adapter: CheckBoxAdapter
        when (type) {
//            //自查自检
//            Constants.Subitem_TYPE.SELF_EXAMINATION.ordinal->{
//                view.tv_title_title1_checkbox.text = "项目名称"
//                adapter = adapterGenerate.Professional_SelectProject()
////                view.tv_title_title1_checkbox.text = "车辆"
////                adapter = adapterGenerate.Professional_Vehicle()
////                view.tv_title_title1_checkbox.text = "标准"
////                adapter = adapterGenerate.Professional_Standard()
//            }
            //监理验收
            Constants.Subitem_TYPE.SUPERVISION_ACCEPTANCE.ordinal->{
//                view.tv_title_title1_checkbox.text = "班组人员"
//                adapter = adapterGenerate.Professional_GroupCrew()
//                view.tv_title_title1_checkbox.text = "安全措施"
//                adapter = adapterGenerate.Professional_SafetyMeasures()

//                view.tv_title_title1_checkbox.text = "技术措施"
//                adapter = adapterGenerate.Professional_TechnicalMeasures()
                view.tv_title_title1_checkbox.text = "作业指导书"
                adapter = adapterGenerate.ProfessionalWorkInstruction()

            }
            //业主验收
            Constants.Subitem_TYPE.OWNER_ACCEPTANCE.ordinal->{
//                view.tv_title_title1_checkbox.text = "机械"
//                adapter = adapterGenerate.Professional_Machine()
                view.tv_title_title1_checkbox.text = "施工方案"
                adapter = adapterGenerate.ProfessionalWorkingPlan()
            }
        }
        return adapter
    }
//
}