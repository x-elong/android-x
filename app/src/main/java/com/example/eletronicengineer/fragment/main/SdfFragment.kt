package com.example.eletronicengineer.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MainActivity
import com.example.eletronicengineer.fragment.sdf.SdfInformationFragment

class SdrFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sdf, container, false)
        (activity as MainActivity).switchFragment(SdfInformationFragment(),R.id.fragment_sdf)
        return view
    }
}