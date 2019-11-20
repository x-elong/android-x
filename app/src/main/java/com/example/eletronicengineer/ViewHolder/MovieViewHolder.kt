package com.example.eletronicengineer.ViewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.Movie
import kotlinx.android.synthetic.main.activity_chat.view.*


class MovieViewHolder(inflater: LayoutInflater, vg: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_dispaly_demand_two, vg, false)) {

    //    var requirementTitle1:TextView?=null
//    var requirementTitle2:TextView?=null
    var reqirementMajor: TextView? = null
    var projectSite: TextView? = null
    var requirementMajor=""
    var requirementType=""
    var mId:String = ""

    init {
//        requirementTitle1 = itemView.findViewById(R.id.tv_display_demand_team_title1)
//        requirementTitle2 = itemView.findViewById(R.id.tv_display_demand_team_title2)
        reqirementMajor = itemView.findViewById(R.id.tv_display_demand_team_content1)
        projectSite = itemView.findViewById(R.id.tv_demand_demand_team_content2)
    }

    fun bind(movie: Movie) {
//        if(movie.reqiurementType=="需求个人")
//        {
//            requirementTitle1!!.text="需求专业"
//            requirementTitle2!!.text="项目地点"
//        }
//        else if(movie.reqiurementType=="需求团队")
//        {
//            requirementTitle1!!.text="需求类别"
//            requirementTitle2!!.text="项目地点"
//        }
//        else if(movie.reqiurementType=="需求租赁")
//        {
//            requirementTitle1!!.text="租赁类型"
//            requirementTitle2!!.text="项目地点"
//        }
//        else if(movie.reqiurementType=="需求三方")
//        {
//            if(movie.requirementMajor=="资质合作")
//            {
//                requirementTitle1!!.text="需求类别"
//                requirementTitle2!!.text="合作地区"
//            }
//            else{
//                requirementTitle1!!.text="需求类别"
//                requirementTitle2!!.text="合作方属性"
//            }
//        }
        reqirementMajor?.text = movie.requirementMajor
        projectSite?.text = movie.projectSite
        mId = movie.id
        requirementType=movie.reqiurementType
        requirementMajor=movie.requirementMajor
    }
}