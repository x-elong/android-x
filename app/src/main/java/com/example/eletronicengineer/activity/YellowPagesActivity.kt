package com.example.eletronicengineer.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.fragment.sdf.ImageFragment
import com.example.eletronicengineer.fragment.sdf.SupplyFragment
import com.example.eletronicengineer.fragment.sdf.UpIdCardFragment
import com.example.eletronicengineer.fragment.sdf.UploadPhoneFragment
import com.example.eletronicengineer.fragment.yellowpages.YellowPagesFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.BitmapMap
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.PermissionHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.lcw.library.imagepicker.ImagePicker
import java.io.File
import java.io.FileOutputStream
import kotlinx.android.synthetic.main.activity_photo_upload.view.*
import kotlinx.android.synthetic.main.item_supply.*
import java.lang.Exception

class YellowPagesActivity : AppCompatActivity() {

	lateinit var mData:List<MultiStyleItem>
	var type:Int=-1
	lateinit var selectContent:String
	lateinit var multiButtonListeners:MutableList<View.OnClickListener>
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_yellow_pages)
		initFragment()

	}
	//接送传过来的界面类型并发送
	fun initFragment(){
		initData()
		//发送出加载的界面类型
		val data=Bundle()
		data.putInt("type",type)
		data.putString("selectContent",selectContent)
		FragmentHelper.addFragment(this,
			YellowPagesFragment.newInstance(data),R.id.frame_yellow_pages,"")
	}
	//获取加载的界面类型
	private fun initData() {
		supportActionBar?.hide()
		multiButtonListeners=ArrayList()
		val intent = getIntent()
		type = intent.getIntExtra("type",0)
		selectContent = intent.getStringExtra("selectContent")
	}

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            supportFragmentManager.popBackStackImmediate()
        }
        return true
    }*/


}
