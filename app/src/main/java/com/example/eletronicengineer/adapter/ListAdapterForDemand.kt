package com.example.eletronicengineer.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.ViewHolder.MovieViewHolder
import com.example.eletronicengineer.activity.DemandDisplayActivity
import com.example.eletronicengineer.activity.SupplyDisplayActivity
import com.example.eletronicengineer.aninterface.Movie


class ListAdapterForDemand(activity: Activity): RecyclerView.Adapter<MovieViewHolder>() {
    var mActivity: Activity
    private var mMovies: MutableList<Movie> = mutableListOf( )

    init {
        mActivity = activity
    }

    override fun onCreateViewHolder(vg: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(vg.context)
        var view = MovieViewHolder(inflater, vg)
        view.itemView.setOnClickListener{
//            Toast.makeText(vg.context,view.mId,Toast.LENGTH_LONG).show()
            when {
                view.requirementType == "需求个人" -> {
                    val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                    intent.putExtra("type",1)
                    intent.putExtra("id",view.mId)
                    mActivity.startActivity(intent)
                }
                view.requirementType.equals("需求团队")-> {
                    when {
                        view.requirementMajor == ("主网施工队") -> {
                            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                            intent.putExtra("type", 2)
                            intent.putExtra("id", view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("配网施工队") -> {
                            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                            intent.putExtra("type", 3)
                            intent.putExtra("id", view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("变电施工队") -> {
                            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                            intent.putExtra("type", 4)
                            intent.putExtra("id", view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("测量设计") -> {
                            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                            intent.putExtra("type", 5)
                            intent.putExtra("id", view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("马帮运输") -> {
                            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                            intent.putExtra("type", 6)
                            intent.putExtra("id", view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("桩基服务") -> {
                            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                            intent.putExtra("type", 7)
                            intent.putExtra("id", view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("非开挖顶拉管作业") -> {
                            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                            intent.putExtra("type", 8)
                            intent.putExtra("id", view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("试验调试") -> {
                            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                            intent.putExtra("type", 9)
                            intent.putExtra("id", view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("跨越架") -> {
                            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                            intent.putExtra("type", 10)
                            intent.putExtra("id", view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == "运行维护" -> {
                            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                            intent.putExtra("type", 11)
                            intent.putExtra("id", view.mId)
                            mActivity.startActivity(intent)
                        }
                    }
                }
                view.requirementType == ("需求车辆租赁") -> {
                    val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                    intent.putExtra("type",12)
                    intent.putExtra("id",view.mId)
                    mActivity.startActivity(intent)
                }
                view.requirementType.equals("需求租赁")-> {
                    when {
                        view.requirementMajor == ("工器具租赁") -> {
                            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                            intent.putExtra("type", 13)
                            intent.putExtra("id", view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("设备租赁") -> {
                            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                            intent.putExtra("type", 14)
                            intent.putExtra("id", view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("机械租赁") -> {
                            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                            intent.putExtra("type", 15)
                            intent.putExtra("id", view.mId)
                            mActivity.startActivity(intent)
                        }
                    }
                }
                view.requirementType.equals("需求三方") -> {
                    if(view.requirementMajor=="资质合作"){
                        val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                        intent.putExtra("type",17)
                        intent.putExtra("id",view.mId)
                        mActivity.startActivity(intent)
                    }
                    else{
                        val intent = Intent(vg.context, DemandDisplayActivity::class.java)
                        intent.putExtra("type",16)
                        intent.putExtra("id",view.mId)
                        mActivity.startActivity(intent)
                    }
                }
                view.requirementType.equals("供应团队服务")->{
                    when
                    {
                        view.requirementMajor == "主网施工队"-> {
                            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                            intent.putExtra("type",2)
                            intent.putExtra("id",view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor =="配网施工队" -> {

                            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                            intent.putExtra("type",3)
                            intent.putExtra("id",view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("变电施工队") -> {
                            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                            intent.putExtra("type",4)
                            intent.putExtra("id",view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("测量设计") -> {
                            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                            intent.putExtra("type",5)
                            intent.putExtra("id",view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("马帮运输") -> {
                            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                            intent.putExtra("type",6)
                            intent.putExtra("id",view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("桩基服务") -> {
                            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                            intent.putExtra("type",7)
                            intent.putExtra("id",view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("非开挖顶拉管作业") -> {
                            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                            intent.putExtra("type",8)
                            intent.putExtra("id",view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("试验调试") -> {
                            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                            intent.putExtra("type",9)
                            intent.putExtra("id",view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("跨越架") -> {
                            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                            intent.putExtra("type",10)
                            intent.putExtra("id",view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == "运行维护" -> {
                            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                            intent.putExtra("type",11)
                            intent.putExtra("id",view.mId)
                            mActivity.startActivity(intent)
                        }
                    }
                    }
                view.requirementType == ("供应租赁车辆") -> {
                    val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                    intent.putExtra("type",12)
                    intent.putExtra("id",view.mId)
                    mActivity.startActivity(intent)
                }
                view.requirementType.equals("供应租赁服务")-> {
                    when {
                        view.requirementMajor == ("工器具租赁") -> {
                            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                            intent.putExtra("type",13)
                            intent.putExtra("id",view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("设备租赁") -> {
                            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                            intent.putExtra("type",14)
                            intent.putExtra("id",view.mId)
                            mActivity.startActivity(intent)
                        }
                        view.requirementMajor == ("机械租赁") -> {
                            val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                            intent.putExtra("type",15)
                            intent.putExtra("id",view.mId)
                            mActivity.startActivity(intent)
                        }
                    }
                }
                view.requirementType.equals("供应三方服务") -> {
                    if(view.requirementMajor=="资质合作"){
                        val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                        intent.putExtra("type",17)
                        intent.putExtra("id",view.mId)
                        mActivity.startActivity(intent)
                    }
                    else{
                        val intent = Intent(vg.context, SupplyDisplayActivity::class.java)
                        intent.putExtra("type",16)
                        intent.putExtra("id",view.mId)
                        mActivity.startActivity(intent)
                    }
                }

            }
        }
        return view
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie = mMovies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = mMovies.size

    fun addMovie(movie: Movie): Int {
        mMovies.add(movie)
        notifyDataSetChanged()
        return mMovies.size
    }
    fun notifyMovie(){
        if (mMovies!=null)
        {
            mMovies.clear()
        }
    }
}