package com.example.eletronicengineer.fragment.sdf

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_image.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File

class ImageFragment:Fragment(){
    companion object{
        fun newInstance(args:Bundle): ImageFragment
        {
            val imageFragment= ImageFragment()
            imageFragment.arguments=args
            return imageFragment
        }
    }
    lateinit var mView:View
    lateinit var mAdapter:ImageAdapter
    lateinit var key:String
    private var mImagePaths: ArrayList<String> = ArrayList()
    var mImagesPath =""
    val iHandlerCallBack = object : IHandlerCallBack {
        override fun onFinish() {
        }

        override fun onCancel() {
        }

        override fun onError() {
        }

        override fun onStart() {
        }

        override fun onSuccess(photoList: MutableList<String>) {
//            val fragment=activity!!.supportFragmentManager.findFragmentByTag("Capture")!!
            NetworkAdapter(mView.context).upImage(photoList[0],this@ImageFragment)
//            photoAdapter.notifyDataSetChanged()
        }
    }
    val glideImageLoader = GlideImageLoader()
    val galleryConfig = GalleryConfig.Builder()
        .imageLoader(glideImageLoader)    // ImageLoader 加载框架（必填）
        .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
        .provider("com.example.eletronicengineer.fileProvider")   // provider (必填)
//        .pathList(mImagePaths)                         // 记录已选的图片
        .multiSelect(false, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
        .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
        .crop(true, 1F, 1F, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
        .isShowCamera(true)                     // 是否现实相机按钮  默认：false
        .filePath("/Gallery/Pictures")          // 图片存放路径
        .build()
    val lastImage = Image("",View.OnClickListener {
        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity)
    })
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        key = arguments!!.getString("key")
        val title = arguments!!.getString("title")
        mView=inflater.inflate(R.layout.fragment_image,container,false)
        if(activity is SupplyActivity || activity is DemandDisplayActivity || activity is MyReleaseActivity)
            mView.bt_image.visibility = View.GONE
        mView.tv_image_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
            if((activity is  SupplyActivity) || activity is DemandDisplayActivity || activity is MyReleaseActivity)
                Log.i("","")
            else
                UnSerializeDataBase.imgList.clear()
        }
        mView.bt_image.setOnClickListener {
            if(UnSerializeDataBase.imgList.size==0)
                Toast.makeText(context,"上传照片数量不能为空",Toast.LENGTH_SHORT).show()
            else{
                Log.i("imagePath",mImagesPath)
                //NetworkAdapter().updateData(arguments!!.getSerializable("serializable"),arguments!!.getString("baseUrl"),key,mImagesPath)
                NetworkAdapter(context!!).upImage(arguments!!.getSerializable("serializable"),arguments!!.getString("baseUrl"),key,this)
            }
        }
        mView.tv_image_title.setText(title)
        initAdapter()
        for (i in UnSerializeDataBase.imgList){
            if(i.key==key){
                mImagesPath=i.path
                val ImagesPath = i.path.split("|")
                this.mImagePaths = ArrayList(ImagesPath)
                Log.i("mImagePaths ",mImagePaths.toString())
                    refresh(ArrayList())
                break
            }
        }
        return mView
    }

    private fun initAdapter() {
        val imageList:MutableList<Image> = ArrayList()
        lastImage.isX = false
        imageList.add(lastImage)
        mAdapter = ImageAdapter(imageList)
        val recyclerView = mView.rv_image
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
    }
    fun refresh(mImagePaths:ArrayList<String>){
        var isExit = false
        var imagesPath = ""
        for (i in UnSerializeDataBase.imgList){
            if(i.key==key){
                val imagePaths = i.path.split("|").toMutableList()
                imagePaths.addAll(mImagePaths)
                this.mImagePaths=ArrayList(imagePaths)
                imagesPath=imagePaths.toString().replace(", ","|")
                imagesPath = imagesPath.substring(1,imagesPath.length-1)
                i.path = imagesPath
                isExit=true
                break
            }
        }
        if(!isExit){
            imagesPath=mImagePaths.toString().replace(", ","|")
            imagesPath = imagesPath.substring(1,imagesPath.length-1)
            this.mImagePaths = mImagePaths
            UnSerializeDataBase.imgList.add(BitmapMap(imagesPath,key))
        }
        val imageList:ArrayList<Image> = ArrayList()
        for (j in this.mImagePaths){
            imageList.add(Image(j,null))
            imageList[imageList.size-1].deleteListener = View.OnClickListener {
                val mImageList = mAdapter.mImageList.toMutableList()
                mImageList.removeAt(this.mImagePaths.indexOf(j))
                mAdapter.mImageList = mImageList
                mAdapter.notifyDataSetChanged()
                delect(j)
            }
        }
        imageList.add(lastImage)
        mAdapter = ImageAdapter(imageList)
        mView.rv_image.adapter = mAdapter
        Log.i("imagesPath " ,imagesPath)
        this.mImagesPath=imagesPath
    }
    fun delect(imagePath:String){
        for (i in UnSerializeDataBase.imgList){
            if(i.key==key){
                mImagePaths.remove(imagePath)
                var imagesPath = mImagePaths.toString().replace(", ","|")
                imagesPath = imagesPath.substring(1,imagesPath.length-1)
                i.path=imagesPath
                if(i.path=="")
                    UnSerializeDataBase.imgList.remove(i)
                break
            }
        }
    }
}