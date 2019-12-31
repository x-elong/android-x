package com.example.eletronicengineer.fragment.my

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ServiceTermsActivity
import kotlinx.android.synthetic.main.fragment_related.view.*

class RelatedFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_related,container,false)
        view.tv_related_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        view.view_terms_of_service.setOnClickListener {
            activity!!.startActivity(Intent(activity!!,ServiceTermsActivity::class.java))
        }
        return view
    }
}