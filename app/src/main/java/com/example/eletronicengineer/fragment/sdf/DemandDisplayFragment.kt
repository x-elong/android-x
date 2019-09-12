package com.example.eletronicengineer.fragment.retailstore

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.GetQRCodeActivity
import com.example.eletronicengineer.activity.ImageDisplayActivity
import com.example.eletronicengineer.activity.MainActivity
import com.example.eletronicengineer.activity.RetailStoreActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.fragment.distribution.IntegralFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.getOwnIntegral
import com.example.eletronicengineer.utils.getQRCode
import com.example.eletronicengineer.utils.getUserIncome
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_demand_display.view.*
import kotlinx.android.synthetic.main.fragment_retail_store.view.*

class DemandDisplayFragment:Fragment() {
    companion object{
        fun newInstance(args: Bundle): DemandDisplayFragment
        {
            val fragment= DemandDisplayFragment()
            fragment.arguments=args
            return fragment
        }
    }
    lateinit var title:String
    var type:Int=1
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
                adapter = adapterGenerate.demandIndividualDisplay()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            2->{
                adapter = adapterGenerate.demandTeamDisplay()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            3->{
                adapter = adapterGenerate.demandTeamDisplayGongTrans()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            4->{
                adapter = adapterGenerate.demandTeamDisplayPile()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            5->{
                adapter = adapterGenerate.demandTeamDisplayTrenchiless()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            6->{
                adapter = adapterGenerate.demandTeamDisplayTestAndDebugging()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            7->{
                adapter = adapterGenerate.demandTeamDisplayCrossFrame()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            8->{
                adapter = adapterGenerate.demandTeamDisplayOperationAndMaintenance()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            9->{
                adapter = adapterGenerate.demandTeamDisplayVehicleLeasing()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            10->{
                adapter = adapterGenerate.demandTeamDisplayEquipmentLeasing()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }
            11->{
                adapter = adapterGenerate.demandTeamDisplayDemandTripartite()
                view.tv_fragment_demand_display_content.adapter = adapter
                view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
            }

        }

    }
}