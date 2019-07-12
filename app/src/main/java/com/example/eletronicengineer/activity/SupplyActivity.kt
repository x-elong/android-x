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
import com.example.eletronicengineer.fragment.SupplyFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.BitmapMap
import com.example.eletronicengineer.utils.PermissionHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
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
    fun initFragment(){
            initData()
            val data=Bundle()
            data.putInt("type",type)
            val fragment=SupplyFragment.newInstance(data)
            addFragment(fragment)
            initData()
    }
    private fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_supply,fragment,"1")
        transaction.commit()
    }
    fun switchFragment(fragment: Fragment,tag:String) {
        val transaction = supportFragmentManager.beginTransaction()
        //隐藏上个Fragment
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.frame_supply, fragment,tag)
        transaction.addToBackStack("1")
        transaction.commit()
    }
    private fun initData() {
        supportActionBar?.hide()
        multiButtonListeners=ArrayList()
        val intent = getIntent()
        type = intent.getIntExtra("type",0)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK)
        {
            when(requestCode)
            {
                Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal->
                {
                    val bmp=data?.extras?.get("data") as Bitmap
                    val file= File(this@SupplyActivity.filesDir.absolutePath+"tmp.png")
                    val fos= FileOutputStream(file)
                    bmp.compress(Bitmap.CompressFormat.PNG,100,fos)
                    //val bmpToDrawable=BitmapDrawable.createFromPath(file.absolutePath)
                    val fragment=this@SupplyActivity.supportFragmentManager.findFragmentByTag("Capture")
                    fragment!!.view!!.iv_uplaod_photo_content.setImageBitmap(bmp)
                    //UnSerializeDataBase.imgList.add(BitmapMap(file.path,""))
                }
            }
        }
    }

}
