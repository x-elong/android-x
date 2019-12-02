package com.example.eletronicengineer.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.ViewHolder.MovieViewHolder
import com.example.eletronicengineer.ViewHolder.PersonalIssueViewHolder
import com.example.eletronicengineer.activity.DemandDisplayActivity
import com.example.eletronicengineer.activity.SupplyDisplayActivity
import com.example.eletronicengineer.aninterface.Movie
import com.example.eletronicengineer.aninterface.PersonalIssue
import com.example.eletronicengineer.model.Constants


class ListAdapterForSupply(activity: Activity): RecyclerView.Adapter<PersonalIssueViewHolder>() {
    var mActivity: Activity
    private var mData: MutableList<PersonalIssue> = mutableListOf()

    init {
        mActivity = activity
    }

    override fun onCreateViewHolder(vg: ViewGroup, viewType: Int): PersonalIssueViewHolder {
        val inflater = LayoutInflater.from(vg.context)
        var view = PersonalIssueViewHolder(inflater, vg)
        view.itemView.setOnClickListener{

            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
            intent.putExtra("type", Constants.FragmentType.PERSONAL_TYPE.ordinal)
            intent.putExtra("id",view.mId)
            mActivity.startActivity(intent)

        }
        return view
    }

    override fun onBindViewHolder(holder: PersonalIssueViewHolder, position: Int) {
        val data: PersonalIssue = mData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = mData.size

    fun addData(data: PersonalIssue): Int {
        mData.add(data)
        notifyDataSetChanged()
        return mData.size
    }
    fun notifyData(){
        if (mData!=null)
        {
            mData.clear()
        }
    }
}