package com.example.eletronicengineer.ViewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.PersonalIssue

class PersonalIssueViewHolder(inflater: LayoutInflater, vg: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_dispaly_demand_two, vg, false)) {

    var name:TextView?=null
    var sex:TextView?=null
    var major:TextView?= null
    var mId:String = ""
    var type=""

    init {
        name=itemView.findViewById(R.id.tv_display_demand_team_title1)
        sex=itemView.findViewById(R.id.tv_display_demand_team_content1)
        major=itemView.findViewById(R.id.tv_demand_demand_team_content2)
    }

    fun bind(data:PersonalIssue) {
        name?.visibility=View.VISIBLE
        name?.text = data.name
        sex?.text = data.sex
        major?.text = data.major
        mId = data.id
        type = data.Type
    }
}