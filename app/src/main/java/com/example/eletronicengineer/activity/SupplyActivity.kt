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
import com.example.eletronicengineer.fragment.sdf.ImageFragment
import com.example.eletronicengineer.fragment.sdf.SupplyFragment
import com.example.eletronicengineer.fragment.sdf.UpIdCardFragment
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
import java.lang.Exception

class SupplyActivity : AppCompatActivity() {

	lateinit var mData:List<MultiStyleItem>
	var mFragmentView:View?=null
	var type:Int=-1
	lateinit var selectContent2:String
	lateinit var multiButtonListeners:MutableList<View.OnClickListener>
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_supply)
		initFragment()

	}
	//接送传过来的界面类型并发送
	fun initFragment(){
		initData()
		//发送出加载的界面类型
		val data=Bundle()
		data.putInt("type",type)
		data.putString("selectContent2",selectContent2)
		val fragment= SupplyFragment.newInstance(data)
		addFragment(fragment)
	}
	//在当前下增加碎片
	private fun addFragment(fragment: Fragment) {
		val transaction = supportFragmentManager.beginTransaction()
		transaction.add(R.id.frame_supply,fragment,"register")
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
	//获取加载的界面类型
	private fun initData() {
		supportActionBar?.hide()
		multiButtonListeners=ArrayList()
		val intent = getIntent()
		type = intent.getIntExtra("type",0)
		selectContent2 = intent.getStringExtra("selectContent2")
	}

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            supportFragmentManager.popBackStackImmediate()
        }
        return true
    }*/
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
					if(fragment is UploadPhoneFragment) {
						fragment.refresh(mImagePaths[0])
					}else if(fragment is UpIdCardFragment){
						fragment.refresh(mImagePaths)
					}else if(fragment is ImageFragment){
						fragment.refresh(mImagePaths)
					}
				}
				Constants.RequestCode.REQUEST_PICK_FILE.ordinal -> {
					val uri = data!!.data
					var path:String?=null
					if (uri!!.toString().contains("content")) {
						path = getRealPathFromURI(uri)
						Log.i("path", path)
						val fileMap=UnSerializeDataBase.fileList.get(UnSerializeDataBase.fileList.size-1)
						fileMap.path=path!!
						UnSerializeDataBase.fileList.set(UnSerializeDataBase.fileList.size-1,fileMap)
					}
					else
					{
						val file = File(uri.toString())
						if (file.exists())
						{
							Log.i("file", file.name)
						}
						val fileMap=UnSerializeDataBase.fileList.get(UnSerializeDataBase.fileList.size-1)
						fileMap.path=uri.toString()
						UnSerializeDataBase.fileList.set(UnSerializeDataBase.fileList.size-1,fileMap)
					}

					//val resultFile= File()
				}
			}
		}
		else
		{
			if (UnSerializeDataBase.fileList.size!=0)
			{
				val fileMap=UnSerializeDataBase.fileList.get(UnSerializeDataBase.fileList.size-1)
				if (fileMap.path=="")
				{
					UnSerializeDataBase.fileList.removeAt(UnSerializeDataBase.fileList.size-1)
				}
			}
			else if(UnSerializeDataBase.imgList.size!=0)
			{
				val imgMap=UnSerializeDataBase.imgList.get(UnSerializeDataBase.imgList.size-1)
				if (imgMap.path=="")
				{
					UnSerializeDataBase.imgList.removeAt(UnSerializeDataBase.imgList.size-1)
				}
			}

		}
	}
	fun getRealPathFromURI(contentUri: Uri): String? {
		var res: String? = null
		val projection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
		var cursor = contentResolver.query(contentUri, projection, null, null, null)
		try {
			if (cursor != null) {
				val column = cursor.getColumnIndexOrThrow(projection[0])
				if (cursor.moveToFirst()) {
					res = cursor.getString(column)
				}
				cursor.close()
			}
			if (res == null) {
				cursor =
					contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)
				if (cursor != null) {
					val column = cursor.getColumnIndexOrThrow(projection[0])
					if (cursor.moveToFirst()) {
						res = cursor.getString(column)
					}
					cursor.close()
				}
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}

		return res
	}
}
