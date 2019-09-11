package com.example.eletronicengineer.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.fragment_problem.view.*

class ProblemFragment : Fragment()  {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_problem,container,false)
        initFragment(view)
        return view
    }

    private fun initFragment(v: View) {
        v.tv_problem_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
    }
}