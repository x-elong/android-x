package com.example.eletronicengineer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.AdapterGenerate
import kotlinx.android.synthetic.main.fragment_phone_list.view.*

class PhoneListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_phone_list, container, false)
        val adapterGenerate= AdapterGenerate()

        adapterGenerate.context=view.context
        adapterGenerate.activity=activity as AppCompatActivity
        val adapter = adapterGenerate.OfficeAddressBook()
        view.rv_phone_list.adapter=adapter
        view.rv_phone_list.layoutManager= LinearLayoutManager(view.context)
        return view
    }
}