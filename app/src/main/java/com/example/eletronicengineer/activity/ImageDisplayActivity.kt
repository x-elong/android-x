package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.sdf.ImageFragment
import com.example.eletronicengineer.utils.GlideImageLoader
import com.example.eletronicengineer.utils.GlideLoader
import com.example.eletronicengineer.utils.UnSerializeDataBase
import kotlinx.android.synthetic.main.activity_image_display.*

class  ImageDisplayActivity : AppCompatActivity() {

    lateinit var key:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display)
        val intent = getIntent()
        val mImagePath=intent.getStringExtra("imagePath")
        view_image_display.setOnClickListener {
            finish()
        }
        GlideImageLoader().displayImage(iv_preImage,mImagePath)
    }
}
