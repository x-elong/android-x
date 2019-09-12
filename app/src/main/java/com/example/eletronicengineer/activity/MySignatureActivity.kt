package com.example.eletronicengineer.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.eletronicengineer.R
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.GlideLoader
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lcw.library.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_my_signature.*
import kotlinx.android.synthetic.main.dialog_upload.view.*
import java.io.File
import java.io.FileOutputStream

class MySignatureActivity : AppCompatActivity() {

    val glideLoader = GlideLoader()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_signature)
        tv_my_signature_back.setOnClickListener {
            finish()
        }
        btn_signature.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_upload,null)
            dialogView.btn_photograph.setOnClickListener {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                // UnSerializeDataBase.imgList.add(BitmapMap("", mData[5].additionalKey))
               startActivityForResult(intent, Constants.RequestCode.REQUEST_PHOTOGRAPHY.ordinal)
                dialog.dismiss()
            }
            dialogView.btn_album.setOnClickListener {
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
                    .start(this, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
                dialog.dismiss()
            }
            dialogView.btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(dialogView)
            dialog.show()
        }
        btn_delete.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
                .setTitle("是否要删除当前签名?")
                .setNegativeButton("取消",object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0!!.dismiss()
                    }
                })
                .setPositiveButton("确定",object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        noSignature()
                        p0!!.dismiss()
                    }
                }).create()
            dialog.show()
        }
        btn_retransmission.setOnClickListener{
            val dialog = BottomSheetDialog(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_upload,null)
            dialogView.btn_photograph.setOnClickListener {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                // UnSerializeDataBase.imgList.add(BitmapMap("", mData[5].additionalKey))
                startActivityForResult(intent, Constants.RequestCode.REQUEST_PHOTOGRAPHY.ordinal)
                dialog.dismiss()
            }
            dialogView.btn_album.setOnClickListener {
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
                    .start(this, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
                dialog.dismiss()
            }
            dialogView.btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(dialogView)
            dialog.show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                Constants.RequestCode.REQUEST_PHOTOGRAPHY.ordinal->{
                    val bmp=data?.extras?.get("data") as Bitmap
                    val file= File(this.filesDir.absolutePath+"tmp.png")
                    val fos= FileOutputStream(file)
                    bmp.compress(Bitmap.CompressFormat.PNG,100,fos)
                    //val bmpToDrawable=BitmapDrawable.createFromPath(file.absolutePath)
                    glideLoader.loadImage(iv_signature,file.path)
                    haveSignature()
                }
                Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal-> {
                    val mImagePaths = data!!.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES)
                    glideLoader.loadImage(iv_signature,mImagePaths[0])
                    haveSignature()
                }
            }
        }
    }
    fun noSignature(){
        iv_signature.visibility = View.INVISIBLE
        tv_signature_hint.visibility = View.VISIBLE
        btn_signature.visibility = View.VISIBLE
        btn_delete.visibility = View.GONE
        btn_retransmission.visibility = View.GONE
    }
    fun haveSignature(){
        iv_signature.visibility = View.VISIBLE
        tv_signature_hint.visibility = View.GONE
        btn_signature.visibility = View.GONE
        btn_delete.visibility = View.VISIBLE
        btn_retransmission.visibility = View.VISIBLE
    }
}
