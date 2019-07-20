package com.example.eletronicengineer.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.sdf.SupplyFragment
import com.example.eletronicengineer.fragment.sdf.UploadPhoneFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.BitmapMap
import com.example.eletronicengineer.utils.PermissionHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.lcw.library.imagepicker.ImagePicker
import java.io.File
import java.io.FileOutputStream
import kotlinx.android.synthetic.main.activity_photo_upload.view.*
import kotlinx.android.synthetic.main.item_supply.*

class SupplyActivity : AppCompatActivity() {

	lateinit var mData:List<MultiStyleItem>
	var mFragmentView:View?=null
	var type:Int=-1
	lateinit var multiButtonListeners:MutableList<View.OnClickListener>
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_supply)
		initFragment()
		PermissionHelper.getPermission(this@SupplyActivity,1)
	}
	//接送传过来的界面类型并发送
	fun initFragment(){
		initData()
		//发送出加载的界面类型
		val data=Bundle()
		data.putInt("type",type)
		val fragment= SupplyFragment.newInstance(data)
		addFragment(fragment)
//            initData()
	}
	//在当前下增加碎片
	private fun addFragment(fragment: Fragment) {
		val transaction = supportFragmentManager.beginTransaction()
		transaction.add(R.id.frame_supply,fragment,"1")
		transaction.commit()
	}
	//Fragment切换
	fun switchFragment(fragment: Fragment,frameLayout:Int,tag:String) {
		val transaction = supportFragmentManager.beginTransaction()
		//隐藏上个Fragment
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		transaction.replace(frameLayout, fragment,tag)
		transaction.addToBackStack("1")
		transaction.commit()
	}
	//获取加载的界面类型
	private fun initData() {
		supportActionBar?.hide()
		multiButtonListeners=ArrayList()
		val intent = getIntent()
		type = intent.getIntExtra("type",0)
	}
	//拍照回调
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode== Activity.RESULT_OK)
		{
			when(requestCode)
			{
				Constants.RequestCode.REQUEST_PHOTOGRAPHY.ordinal->{
					val bmp=data?.extras?.get("data") as Bitmap
					val file= File(this@SupplyActivity.filesDir.absolutePath+"tmp.png")
					val fos= FileOutputStream(file)
					bmp.compress(Bitmap.CompressFormat.PNG,100,fos)
					//val bmpToDrawable=BitmapDrawable.createFromPath(file.absolutePath)
					val fragment=this@SupplyActivity.supportFragmentManager.findFragmentByTag("Capture")
					fragment!!.view!!.iv_uplaod_photo_content.setImageBitmap(bmp)
					//UnSerializeDataBase.imgList.add(BitmapMap(file.path,""))
				}
				Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal->
				{
					val mImagePaths = data!!.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES)
					val fragment=this@SupplyActivity.supportFragmentManager.findFragmentByTag("Capture")
					(fragment as UploadPhoneFragment).refresh(mImagePaths)
				}
			}
		}
	}

}
