package com.example.eletronicengineer.fragment.projectdisk

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codekidlabs.storagechooser.StorageChooser
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.ImageCheckAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.aninterface.ImageCheck
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.FileMap
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.startSendMultiPartMessage
import com.example.eletronicengineer.utils.uploadFile
import com.lxj.xpopup.XPopup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_image_check.view.*
import kotlinx.android.synthetic.main.item_image_check.view.*
import kotlinx.android.synthetic.main.shift_dialog.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.net.URLEncoder

class ProjectImageCheckFragment : Fragment() {

    companion object{
        fun newInstance(args:Bundle): ProjectImageCheckFragment
        {
            val projectImageCheckFragment= ProjectImageCheckFragment ()
            projectImageCheckFragment.arguments=args
            return projectImageCheckFragment
        }
    }
    lateinit var title:String
    lateinit var type:String
    lateinit var imageCheckAdapter: ImageCheckAdapter
    var viewListener: View.OnClickListener? = null
    var imageCheckList: MutableList<ImageCheck> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_image_check, container, false)
        title=arguments!!.getString("title")
//        type = title.substring(0,2)
//        title = title.substring(3)
        view.tv_image_check_title.text=title
        initFragment(view)
        return view
    }

    private fun initFragment(v: View) {
        imageCheckList.clear()
        v.tv_image_check_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        v.tv_add_select.setOnClickListener {
            XPopup.Builder(context)
                .atView(it)
                .asAttachList(arrayOf("下载模板", "上传文件", "上传照片", "拍照"), null)
                { position, text ->
                    run {
                        if (text == "拍照") {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            activity!!.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
                        } else if (text == "上传照片") {
                            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            activity!!.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)

                        } else if (text == "下载模板") {
                            val chooser = StorageChooser.Builder().withActivity(activity).allowCustomPath(true)
                                .setType(StorageChooser.DIRECTORY_CHOOSER)
                                .withFragmentManager(activity!!.fragmentManager)
                                .withMemoryBar(true).build()
                            chooser.show()
                        } else if (text == "上传文件") {
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "*/*"
                            UnSerializeDataBase.fileList.add(FileMap("", "file"))
                            activity!!.startActivityForResult(intent, Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
                        }
                    }
                }.show()
        }
        for (j in 0 until 2){
            var imageCheck = ImageCheck(R.drawable.task_statistics, "财务统计标题"+j)
            imageCheckList.add(imageCheck)
        }
        imageCheckAdapter = ImageCheckAdapter(imageCheckList)
        val recyclerView = v.recycler_view_image_check
        val linearLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = imageCheckAdapter
    }
    fun uploadFile(filePath:String){
        val map=HashMap<String, RequestBody>()
        map["nodeSubitemId"]=RequestBody.create(MultipartBody.FORM,"1")
        var fileName = ""
        val result= Observable.create<HashMap<String,RequestBody>>{
            val key = "file"
            val file= File(filePath)
            fileName = file.name
            val builder= MultipartBody.Builder()
            if (file.name.contains("jpg")||file.name.contains("png"))
            {
                val requestBody=RequestBody.create(MediaType.parse("image/*"),file)
                builder.addFormDataPart(key,file.name,requestBody)
            }
            else
            {
                val requestBody=RequestBody.create(MediaType.parse("application/octet-stream;charset=utf-8"),file)
                if (file.exists())
                    Log.i("file","exist")
                if (file.canRead())
                    Log.i("file","can read")
                builder.addFormDataPart(key, URLEncoder.encode(file.name,"utf-8"),requestBody)
            }
            map[key]=builder.build()
            it.onNext(map)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                uploadFile(it,"http://192.168.1.133:8014"+Constants.HttpUrlPath.Professional.selfInspectionUploadFile).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe({
                        //Log.i("retrofitMsg", it.string())
                        val json = JSONObject(it.string()).getJSONObject("message")
                        val imageCheck = ImageCheck(R.drawable.photo,fileName)
                        imageCheck.id = json.getLong("id")
                        imageCheckList.add(imageCheck)
//                        if (UnSerializeDataBase.imgList[0].needDelete) {
//                            val file = File(UnSerializeDataBase.imgList[0].path)
//                            file.delete()
//                        }
                        UnSerializeDataBase.imgList.clear()
                        UnSerializeDataBase.fileList.clear()
                        imageCheckAdapter.mImageCheckList=imageCheckList
                        imageCheckAdapter.notifyItemInserted(imageCheckList.size)
                        Toast.makeText(context,"上传成功！！",Toast.LENGTH_SHORT).show()
                    },
                        {
                            it.printStackTrace()
                        })
            }
    }
}