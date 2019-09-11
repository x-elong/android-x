package com.example.eletronicengineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.CheckBoxStyle
import com.example.eletronicengineer.aninterface.EngineeringAppliances
import com.example.eletronicengineer.aninterface.Function
import kotlinx.android.synthetic.main.function.view.*
import kotlinx.android.synthetic.main.item_chekbox_style.view.*
import kotlinx.android.synthetic.main.item_engineering_appliances.view.*
import kotlinx.android.synthetic.main.item_image_check.view.*


class EngineeringAppLiancesAdapter : RecyclerView.Adapter<EngineeringAppLiancesAdapter.ViewHolder> {

    var mEngineeringAppLiancesList:List<EngineeringAppliances>

    constructor(mEngineeringAppLiancesList:List<EngineeringAppliances>){
        this.mEngineeringAppLiancesList=mEngineeringAppLiancesList
    }
    inner class ViewHolder : RecyclerView.ViewHolder {
        var EngineeringAppLiancesName: TextView
        var EngineeringAppLiancesType: TextView
        var EngineeringAppLiancesUnit: TextView
        var EngineeringAppLiancesNum: TextView

        constructor(view:View):super (view){
            EngineeringAppLiancesName=view.engineering_appliances_name
            EngineeringAppLiancesType=view.engineering_appliances_type
            EngineeringAppLiancesUnit=view.engineering_appliances_unit
            EngineeringAppLiancesNum=view.engineering_appliances_number
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_engineering_appliances, viewGroup, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(@NonNull viewHolder: ViewHolder, i: Int) {
        val engineeringAppLiances = mEngineeringAppLiancesList[i]
        viewHolder.EngineeringAppLiancesName.text = engineeringAppLiances.engineeringName
        viewHolder.EngineeringAppLiancesType.text = engineeringAppLiances.engineeringType
        viewHolder.EngineeringAppLiancesUnit.text = engineeringAppLiances.engineeringUnit
        viewHolder.EngineeringAppLiancesNum.text = engineeringAppLiances.engineeringNum

    }

    override fun getItemCount(): Int {
        return mEngineeringAppLiancesList.size
    }

}