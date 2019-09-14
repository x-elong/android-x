package com.example.eletronicengineer.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.ViewHolder.MovieViewHolder
import com.example.eletronicengineer.activity.DemandDisplayActivity
import com.example.eletronicengineer.aninterface.Movie


class ListAdapterForDemandPerson(activity: Activity): RecyclerView.Adapter<MovieViewHolder>() {
    var mActivity: Activity
    private var mMovies: MutableList<Movie> = mutableListOf( )

    init {
        mActivity = activity
    }

    override fun onCreateViewHolder(vg: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(vg.context)
        var view = MovieViewHolder(inflater, vg)
        view.itemView.setOnClickListener{

            val intent = Intent(vg.context, DemandDisplayActivity::class.java)
            intent.putExtra("type",1)
            intent.putExtra("title",view.projectName)
            intent.putExtra("id",view.mId)
            mActivity.startActivity(intent)
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
}