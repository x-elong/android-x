package com.example.eletronicengineer.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.projectdisk.ProjectImageCheckFragment
import com.example.eletronicengineer.fragment.projectdisk.ProjectMoreFragment
import com.example.eletronicengineer.fragment.sdf.ImageFragment
import com.example.eletronicengineer.fragment.sdf.UpIdCardFragment
import com.example.eletronicengineer.fragment.sdf.UploadPhoneFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.lcw.library.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_photo_upload.view.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class ProfessionalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professional)
        supportActionBar?.hide()
        initFragment()
    }

    private fun initFragment() {
        val intent = getIntent()
        val data = Bundle()
        val type= intent.getIntExtra("type",0)
        if(type==Constants.Subitem_TYPE.OVERHEAD_MORE.ordinal)
        data.putSerializable("MajorDistributionProjectEntity",intent.getSerializableExtra("MajorDistributionProjectEntity"))
        data.putInt("type", type)
        addFragment(ProjectMoreFragment.newInstance(data))
    }

    fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_professional, fragment)
        transaction.commit()
    }

    fun switchFragment(fragment: Fragment,tag:String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.frame_professional, fragment,tag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    //拍照回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK)
        {
            when(requestCode)
            {
                Constants.RequestCode.REQUEST_PHOTOGRAPHY.ordinal->{
                    val bmp=data?.extras?.get("data") as Bitmap
                    val file= File(this@ProfessionalActivity.filesDir.absolutePath+"tmp.png")
                    val fos= FileOutputStream(file)
                    bmp.compress(Bitmap.CompressFormat.PNG,100,fos)
                    //val bmpToDrawable=BitmapDrawable.createFromPath(file.absolutePath)
                    val fragment=this@ProfessionalActivity.supportFragmentManager.findFragmentByTag("Capture")
                    fragment!!.view!!.iv_uplaod_photo_content.setImageBitmap(bmp)
                    //UnSerializeDataBase.imgList.add(BitmapMap(file.path,""))
                }
                Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal->
                {
                    val mImagePaths = data!!.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES)
                    val fragment=this@ProfessionalActivity.supportFragmentManager.findFragmentByTag("Capture")
                    if(fragment is UploadPhoneFragment) {
                        fragment.refresh(mImagePaths[0])
                    }else if(fragment is UpIdCardFragment){
                        fragment.refresh(mImagePaths)
                    }else if(fragment is ImageFragment){
                        fragment.refresh(mImagePaths)
                    }
                }
                Constants.RequestCode.REQUEST_PICK_FILE.ordinal->{
                    val uri = data!!.data
                    var path:String?=null
                    if (uri!!.toString().contains("content")) {
                        path = getRealPathFromURI(uri)
                        Log.i("path", path)
                        val fileMap=UnSerializeDataBase.fileList.get(UnSerializeDataBase.fileList.size-1)
                        fileMap.path=path!!
                        UnSerializeDataBase.fileList.set(UnSerializeDataBase.fileList.size-1,fileMap)
                    }
                    else
                    {
                        val file = File(uri.toString())
                        if (file.exists())
                        {
                            Log.i("file", file.name)
                        }
                        val fileMap=UnSerializeDataBase.fileList.get(UnSerializeDataBase.fileList.size-1)
                        fileMap.path=uri.toString()
                        UnSerializeDataBase.fileList.set(UnSerializeDataBase.fileList.size-1,fileMap)
                    }
                    val fragment=this@ProfessionalActivity.supportFragmentManager.findFragmentByTag("fileManager") as ProjectImageCheckFragment
                    fragment.uploadFile(UnSerializeDataBase.fileList[0].path)
                    //val resultFile= File()
                }
            }
        }
        else if(UnSerializeDataBase.imgList.size!=0)
            {
                val imgMap= UnSerializeDataBase.imgList.get(UnSerializeDataBase.imgList.size-1)
                if (imgMap.path=="")
                {
                    UnSerializeDataBase.imgList.removeAt(UnSerializeDataBase.imgList.size-1)
                }
            }
    }
    fun getRealPathFromURI(contentUri: Uri): String? {
        var res: String? = null
        val projection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = contentResolver.query(contentUri, projection, null, null, null)
        try {
            if (cursor != null) {
                val column = cursor.getColumnIndexOrThrow(projection[0])
                if (cursor.moveToFirst()) {
                    res = cursor.getString(column)
                }
                cursor.close()
            }
            if (res == null) {
                cursor =
                    contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)
                if (cursor != null) {
                    val column = cursor.getColumnIndexOrThrow(projection[0])
                    if (cursor.moveToFirst()) {
                        res = cursor.getString(column)
                    }
                    cursor.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return res
    }
}
