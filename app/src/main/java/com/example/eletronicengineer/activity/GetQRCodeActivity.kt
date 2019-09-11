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
import com.example.eletronicengineer.utils.GlideLoader
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.lcw.library.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_get_qr_code.*
import kotlinx.android.synthetic.main.activity_image_display.*
import kotlinx.android.synthetic.main.activity_photo_upload.view.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class GetQRCodeActivity : AppCompatActivity() {

    lateinit var key:String
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_get_qr_code)
            val intent = getIntent()
            val mImagePath=intent.getStringExtra("imagePath")
            tv_get_qr_code_back.setOnClickListener {
                finish()
            }
            GlideLoader().loadPreImage(tv_get_qr_code_content,mImagePath)
        }
}
