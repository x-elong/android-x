package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.market_tender.view.*

class MarketTenderFragment :Fragment(){
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view=inflater.inflate(R.layout.market_tender,container,false)
		initOnClick(view)
		return view
	}

	private fun initOnClick(view: View) {
		view.return_iv.setOnClickListener {
			activity!!.supportFragmentManager.popBackStackImmediate()
		}
	}
}