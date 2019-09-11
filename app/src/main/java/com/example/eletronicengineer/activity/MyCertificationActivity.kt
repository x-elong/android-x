package com.example.eletronicengineer.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.my.BusinessLicenseFragment
import com.example.eletronicengineer.fragment.my.MyCertificationFragment
import com.example.eletronicengineer.fragment.my.PersonalCertificationFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.BitmapMap
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.lcw.library.imagepicker.ImagePicker
import java.io.File
import java.io.FileOutputStream

class MyCertificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_certification)
        initData()
    }

    private fun initData() {
        addFragment(MyCertificationFragment())
    }

    fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_my_certification, fragment)
        transaction.commit()
    }

    fun switchFragment(fragment: Fragment,frameLayout:Int,tag:String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(frameLayout, fragment,tag)
        transaction.addToBackStack(null)
        transaction.commit()
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
                    val fragment=(this@MyCertificationActivity.supportFragmentManager.findFragmentByTag("Certification") as BusinessLicenseFragment)
                    fragment.refresh(file.path)
                }
                Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal-> {
                    val mImagePaths = data!!.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES)
                    val fragment=this@MyCertificationActivity.supportFragmentManager.findFragmentByTag("Certification")
                    if(fragment is BusinessLicenseFragment)
                        fragment.refresh(mImagePaths[0])
                    else if(fragment is PersonalCertificationFragment)
                        fragment.refresh(mImagePaths[0])
                }
            }
        }
    }
}
