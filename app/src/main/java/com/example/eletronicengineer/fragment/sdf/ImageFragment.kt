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
import com.example.eletronicengineer.adapter.ImageAdapter
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.aninterface.Image
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.startSendMessage
import com.example.eletronicengineer.utils.uploadImage
import com.lcw.library.imagepicker.ImagePicker
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
    val glideLoader = GlideLoader()
    lateinit var mView:View
    lateinit var mAdapter:ImageAdapter
    lateinit var key:String
    private var mImagePaths: ArrayList<String> = ArrayList()
    var mImagesPath =""
    val lastImage = Image("drawable/add.png",View.OnClickListener {
        ImagePicker.getInstance()
            .setTitle("图片")//设置标题
            .showCamera(true)//设置是否显示拍照按钮
            .showImage(true)//设置是否展示图片
            .showVideo(true)//设置是否展示视频
            .showVideo(true)//设置是否展示视频
            .setSingleType(true)//设置图片视频不能同时选择
            .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
            //.setImagePaths(mImagePaths)//保存上一次选择图片的状态，如果不需要可以忽略
            .setImageLoader(glideLoader)//设置自定义图片加载器
            .start(activity, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
    })
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        key = arguments!!.getString("key")
        val title = arguments!!.getString("title")
        mView=inflater.inflate(R.layout.fragment_image,container,false)
        mView.tv_image_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
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
        initAdapter(mView)
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

    private fun initAdapter(view: View) {
        var imageList:MutableList<Image> = ArrayList()
        imageList.add(lastImage)
        mAdapter = ImageAdapter(imageList)
        val recyclerView = view.rv_image
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
    }
    fun refresh(mImagePaths:ArrayList<String>){
        var isExit = false
        var imagesPath = ""

        for (i in UnSerializeDataBase.imgList){
            if(i.key==key){
                var imagePaths = i.path.split("|").toMutableList()
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
                break
            }
        }
    }
}