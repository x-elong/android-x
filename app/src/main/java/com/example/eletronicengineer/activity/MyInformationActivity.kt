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
import com.example.eletronicengineer.fragment.my.EditProfileFragment
import com.example.eletronicengineer.fragment.my.MyInformationFragment
import com.example.eletronicengineer.fragment.my.PersonalCertificationFragment
import com.example.eletronicengineer.model.Constants
import com.lcw.library.imagepicker.ImagePicker
import java.io.File
import java.io.FileOutputStream

class MyInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_information)
        initData()
    }

    private fun initData() {
        addFragment(MyInformationFragment())
    }

    fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_my_information, fragment,"MyInformation")
        transaction.commit()
    }

    fun switchFragment(fragment: Fragment, tag:String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.frame_my_information, fragment,tag)
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
                    val fragment=(this.supportFragmentManager.findFragmentByTag("MyInformation") as MyInformationFragment)
                    fragment.refresh(file.path)
                }
                Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal-> {
                    val mImagePaths = data!!.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES)
                    val fragment=this.supportFragmentManager.findFragmentByTag("MyInformation")
                    if(fragment is MyInformationFragment)
                        fragment.refresh(mImagePaths[0])
                }
            }
        }
    }
}
