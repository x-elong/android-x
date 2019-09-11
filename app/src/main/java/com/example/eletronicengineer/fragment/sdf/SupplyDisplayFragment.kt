package com.example.eletronicengineer.fragment.retailstore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.fragment_demand_display.view.*

class SupplyDisplayFragment:Fragment() {
    companion object{
        fun newInstance(args: Bundle): SupplyDisplayFragment
        {
            val fragment= SupplyDisplayFragment()
            fragment.arguments=args
            return fragment
        }
    }
    lateinit var title:String
    var type:Int=0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_demand_display, container, false)
        title=arguments!!.getString("title")
        type = arguments!!.getInt("type")
        view.tv_display_demand_title.text=title
        initFragment(view)
        return view
    }

    private fun initFragment(view: View) {
        view.tv_display_demand_back.setOnClickListener {
            activity!!.finish()
        }
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = view.context
        adapterGenerate.activity = activity as AppCompatActivity
        lateinit var adapter: RecyclerviewAdapter
        when (type) {
            1 -> {
                adapter = adapterGenerate.supplyIndividualDisplay()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            2->{
                adapter = adapterGenerate.supplyTeamDisplay()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            3->{
                adapter = adapterGenerate.supplyTeamDisplayGongTrans()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            4->{
                adapter = adapterGenerate.supplyTeamDisplayPile()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            5->{
                adapter = adapterGenerate.supplyTeamDisplayTrenchiless()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            6->{
                adapter = adapterGenerate.supplyTeamDisplayTestAndDebugging()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            7->{
                adapter = adapterGenerate.supplyTeamDisplayCrossFrame()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            8->{
                adapter = adapterGenerate.supplyTeamDisplayOperationAndMaintenance()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            9->{
                adapter = adapterGenerate.supplyTeamDisplayVehicleLeasing()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            10->{
                adapter = adapterGenerate.supplyTeamDisplayEquipmentLeasing()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            11->{
                adapter = adapterGenerate.supplyTeamDisplayDemandTripartite()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }

        }

    }
}