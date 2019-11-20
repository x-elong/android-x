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
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.fragment.sdf.DemandDisplayFragment
import com.example.eletronicengineer.fragment.sdf.DemandFragment
import com.example.eletronicengineer.fragment.sdf.UploadPhoneFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.model.User
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.downloadFile
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



}
