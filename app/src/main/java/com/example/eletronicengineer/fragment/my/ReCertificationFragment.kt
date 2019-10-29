package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyCertificationActivity
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.GlideLoader
import com.example.eletronicengineer.utils.ToastHelper
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_re_certification.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class ReCertificationFragment :Fragment(){
    companion object {
        fun newInstance(args:Bundle):ReCertificationFragment{
            val reCertificationFragment = ReCertificationFragment()
            reCertificationFragment.arguments = args
            return reCertificationFragment
        }
    }
    lateinit var mainType:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_re_certification,container,false)
        mainType = arguments!!.getString("mainType")
        view.tv_information_re_certification_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        view.tv_main_type.text = mainType
        if(mainType=="个人"){
            val bundle = Bundle()
            bundle.putString("mainType",mainType)
            (activity as MyCertificationActivity).addFragment(PersonalReCertificationFragment.newInstance(bundle),R.id.frame_re_certification_category,"Certification")
        }else{
            val bundle = Bundle()
            bundle.putString("mainType",mainType)
            (activity as MyCertificationActivity).addFragment(EnterpriseReCertificationFragment.newInstance(bundle),R.id.frame_re_certification_category,"Certification")
        }
        return view
    }
}