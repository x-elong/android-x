package com.example.eletronicengineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.ProjectList
import kotlinx.android.synthetic.main.item_shift_input.view.*

class ProjectListAdapter  : RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    var mProjectList:MutableList<ProjectList> = mutableListOf()
    lateinit var mView:View
    constructor(mProjectList:MutableList<ProjectList>){
        this.mProjectList = mProjectList

    }
    constructor(){

    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        var listname:TextView
        var listListener:TextView
       lateinit var mdata:List<MultiStyleItem>
        constructor(view: View):super (view){
            listname = view.tv_shift_input_title
            listListener = view.tv_shift_input_show
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, viewType: Int): ViewHolder {
        mView= LayoutInflater.from(viewGroup.context).inflate(R.layout.item_shift_input, viewGroup, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(@NonNull viewHolder: ViewHolder, i: Int) {
        val projectList = mProjectList[i]
        viewHolder.listname.text = projectList.listname
        viewHolder.listListener.setOnClickListener(projectList.listListener)
       // viewHolder.mdata=projectList.mdata
    }

    override fun getItemCount(): Int {
        return mProjectList.size
    }
    fun addList(tmp:ProjectList,manager: LinearLayoutManager):Int{
        mProjectList.add(tmp)

        manager.scrollToPosition(mProjectList.size+1)
        notifyDataSetChanged()
        return mProjectList.size
    }
}