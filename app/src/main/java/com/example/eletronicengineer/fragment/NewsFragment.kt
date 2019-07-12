package com.example.eletronicengineer.fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.aninterface.Function
import java.util.ArrayList


class NewsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.news, container, false)
        return view
    }
}
