package com.example.eletronicengineer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.Movie

class ListAdapter: RecyclerView.Adapter<MovieViewHolder>() {

    private var mMovies: MutableList<Movie> = mutableListOf( )

    override fun onCreateViewHolder(vg: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(vg.context)
        var view = MovieViewHolder(inflater, vg)
        view.itemView.setOnClickListener{
            Toast.makeText(vg.context, "clicked id=" + view.mId, Toast.LENGTH_LONG).show()
        }
        return view
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie = mMovies[position]
        holder.bind(movie)
        println("position: $position")
    }

    override fun getItemCount(): Int = mMovies.size

    companion object {
        private var inst = ListAdapter()
        fun instance(): ListAdapter = inst
    }

    fun addMovie(movie: Movie): Int {
        mMovies.add(movie)
        notifyDataSetChanged()
        return mMovies.size
    }
}


class MovieViewHolder(inflater: LayoutInflater, vg: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_dispaly_demand_two, vg, false)) {

    private var requirementVariety: TextView? = null
    private var projectSite: TextView? = null
    var mId:String = "?"

    init {
        requirementVariety = itemView.findViewById(R.id.tv_display_demand_team_content1)
        projectSite = itemView.findViewById(R.id.tv_demand_demand_team_content2)
    }

    fun bind(movie: Movie) {
        requirementVariety?.text = movie.requirementVariety
        projectSite?.text = movie.projectSite
        mId = movie.id
    }
}