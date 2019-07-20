package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eletronicengineer.R
import com.example.eletronicengineer.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_image_display.*

class ImageDisplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display)
        val intent = getIntent()
        val mImagePaths=intent.getStringArrayListExtra("mImagePaths")
        GlideLoader().loadPreImage(iv_preImage,mImagePaths.get(0))
    }
}
