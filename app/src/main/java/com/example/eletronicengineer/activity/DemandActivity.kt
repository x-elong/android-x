package com.example.eletronicengineer.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Adapter
import androidx.fragment.app.Fragment
import com.bin.david.form.data.Column
import com.bin.david.form.data.table.TableData
import com.codekidlabs.storagechooser.StorageChooser
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.fragment.sdf.*
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.model.User
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.downloadFile
import com.lcw.library.imagepicker.ImagePicker
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_demand.*
import kotlinx.android.synthetic.main.item_public_point_position1.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
class DemandActivity : AppCompatActivity() {

  var type:Int=0
  lateinit var selectContent2:String
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_demand_publish)
    initFragment()
  }
  private fun initFragment() {
    initData()
    val data = Bundle()
    data.putInt("type",type)
    data.putString("selectContent2",selectContent2)
    addFragment(DemandFragment.newInstance(data))
  }

  //获取加载的界面类型
  private fun initData() {
    val intent = getIntent()
    type = intent.getIntExtra("type",0)
    selectContent2 = intent.getStringExtra("selectContent2")
  }

  fun addFragment(fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.frame_demand_publish, fragment,"register")
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
          val fragment=this@DemandActivity.supportFragmentManager.findFragmentByTag("Capture")!!
          NetworkAdapter(this).upImage(mImagePaths[0],fragment)
        }
      }
    }
    else
    {
      if (UnSerializeDataBase.fileList.size!=0)
      {
        val fileMap= UnSerializeDataBase.fileList.get(UnSerializeDataBase.fileList.size-1)
        if (fileMap.path=="")
        {
          UnSerializeDataBase.fileList.removeAt(UnSerializeDataBase.fileList.size-1)
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
  }


}
