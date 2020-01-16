package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.DemandDisplayActivity
import com.example.eletronicengineer.activity.MyReleaseActivity
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.adapter.ImageAdapter
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.aninterface.Image
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.startSendMessage
import com.example.eletronicengineer.utils.uploadImage
import com.lcw.library.imagepicker.ImagePicker
import com.yancy.gallerypick.config.GalleryConfig
import com.yancy.gallerypick.config.GalleryPick
import com.yancy.gallerypick.inter.IHandlerCallBack
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_display_image.*
import kotlinx.android.synthetic.main.fragment_display_image.view.*
import kotlinx.android.synthetic.main.fragment_image.view.*
import kotlinx.android.synthetic.main.fragment_image.view.rv_image
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File

class ImageDisplayFragment:Fragment(){
    companion object{
        fun newInstance(args:Bundle): ImageDisplayFragment
        {
            val imageFragment= ImageDisplayFragment()
            imageFragment.arguments=args
            return imageFragment
        }
    }
    lateinit var mView:View
    lateinit var mAdapter:ImageAdapter
    lateinit var imagePath:String
    lateinit var title:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        imagePath = arguments!!.getString("imagePath")
        title = arguments!!.getString("title")
        mView=inflater.inflate(R.layout.fragment_display_image,container,false)
        mView.tv_display_image_title.text=title
        initAdapter()
        return mView
    }

    private fun initAdapter() {
        val imageList:MutableList<Image> = ArrayList()
        for(i in imagePath.split("|")){
            val lastImage=Image(i,null)
            lastImage.isX = false
            imageList.add(lastImage)
        }
        mAdapter = ImageAdapter(imageList)
        val recyclerView = mView.rv_image
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
    }
}