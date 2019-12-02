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
import com.example.eletronicengineer.fragment.sdf.DemandDisplayFragment
import com.example.eletronicengineer.fragment.sdf.ImageFragment
import com.example.eletronicengineer.fragment.sdf.UpIdCardFragment
import com.example.eletronicengineer.fragment.sdf.UploadPhoneFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.lcw.library.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_photo_upload.view.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class DemandDisplayActivity : AppCompatActivity() {
  var type:Int=0
  lateinit var id:String
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_demand_display)
    initFragment()
  }

  private fun initFragment() {
    initData()
    val data = Bundle()
    data.putInt("type",type)
    data.putString("id",id)
    addFragment(DemandDisplayFragment.newInstance(data))
  }

  //获取加载的界面类型
  private fun initData() {
    val intent = getIntent()
    type = intent.getIntExtra("type",0)
    id = intent.getStringExtra("id")
  }

  fun addFragment(fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.frame_display_demand, fragment)
    transaction.commit()
  }
  fun switchFragment(fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    transaction.replace(R.id.frame_display_demand, fragment)
    transaction.addToBackStack(null)
    transaction.commit()
  }
  //Fragment切换
  fun switchFragment(fragment: Fragment,frameLayout:Int,tag:String) {
    val transaction = supportFragmentManager.beginTransaction()
    //隐藏上个Fragment
    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    transaction.replace(frameLayout, fragment,tag)
    transaction.addToBackStack(tag)
    transaction.commit()
  }
  //拍照回调
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode== Activity.RESULT_OK)
    {
      when(requestCode)
      {
        Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal->
        {
          val mImagePaths = data!!.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES)
          val fragment=this@DemandDisplayActivity.supportFragmentManager.findFragmentByTag("Capture")
          if(fragment is UploadPhoneFragment) {
            fragment.refresh(mImagePaths[0])
          }else if(fragment is UpIdCardFragment){
            fragment.refresh(mImagePaths)
          }else if(fragment is ImageFragment){
            fragment.refresh(mImagePaths)
          }
        }
      }
    }
  }

}
