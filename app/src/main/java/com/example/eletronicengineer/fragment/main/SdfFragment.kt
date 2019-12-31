package com.example.eletronicengineer.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MainActivity
import com.example.eletronicengineer.fragment.sdf.SdfInformationFragment
import com.example.eletronicengineer.utils.GlideImageLoader
import com.example.eletronicengineer.utils.ImageLoadUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.sdf.view.*

class SdrFragment : Fragment() {

    val glideImageLoader = GlideImageLoader()
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.sdf, container, false)
        (activity as MainActivity).switchFragment(SdfInformationFragment(),R.id.fragment_sdf)
        return mView
    }

    override fun onStart() {
        super.onStart()
//        glideImageLoader.displayImage(mView.view_advertisement_background as ImageView,R.drawable.advertisement_background1)
//        glideImageLoader.displayImage(mView.view_advertisement as ImageView,R.drawable.advertisement_text)
//        mView.view_advertisement_background.viewTreeObserver.addOnGlobalLayoutListener {
//            ImageLoadUtil.loadBackgound(mView.view_advertisement_background,mView.context,R.drawable.advertisement_background1)
//        }
//        mView.view_advertisement.viewTreeObserver.addOnGlobalLayoutListener {
//            ImageLoadUtil.loadBackgound(mView.view_advertisement,mView.context,R.drawable.advertisement_text)
//        }

    }
}