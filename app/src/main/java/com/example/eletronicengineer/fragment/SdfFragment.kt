package com.example.eletronicengineer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MainActivity
import kotlinx.android.synthetic.main.sdf.view.*

class sdrFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sdf, container, false)
        (activity as MainActivity).switchFragment(SdfInformationFragment(),R.id.fragment_sdf)
        return view
    }
}