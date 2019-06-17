package com.example.eletronicengineer

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.bin.david.form.data.Column
import com.bin.david.form.data.table.TableData
import com.electric.engineering.model.MultiStyleItem
import com.electric.engineering.utils.ItemGenerate
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.model.User
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.downloadFile
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.FormBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var mData:List<MultiStyleItem>
    lateinit var multiButtonListeners:MutableList<View.OnClickListener>
    //List<MultiStyleItem> mData=new ArrayList<>()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PermissionHelper.getPermission(this@MainActivity,1)
        initData()
    }

    fun initData()
    {
        supportActionBar?.hide()
        multiButtonListeners=ArrayList()
        val adapterGenerate=AdapterGenerate()
        adapterGenerate.context=this@MainActivity
        adapterGenerate.activity=this@MainActivity

        val adapter=adapterGenerate.DemandGroupCrossingFrame()
        rv_main_content.adapter=adapter
        rv_main_content.layoutManager=LinearLayoutManager(this@MainActivity)
    }
    fun initTable()
    {
        val column1 = Column<String>("姓名", "name")
        val column2 = Column<String>("年龄", "age")
        val column3 = Column<String>("更新时间", "time")
        val column4 = Column<String>("头像", "portrait")
        //组合列
        val totalColumn1 = Column<Any>("组合列名", column1, column2,column3,column4)
        val userList:MutableList<User> =ArrayList(5)
        val user=User("张三","21","2016-7-5","xxx")
        userList.add(user)
        val tableData:TableData<User> = TableData("表格名",userList,column1,column2,column3,column4)
        st_main_table.tableData=tableData
        st_main_table.setZoom(true)
    }
    fun initRetrofit(targetPath:String,targetFileName:String,webFileName:String,baseUrl:String)
    {
        downloadFile(targetPath,targetFileName,webFileName,baseUrl)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK)
        {
            when(requestCode)
            {
                Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal->
                {
                    val uri=data!!.data
                    val path=getRealPathFromURI(uri!!)
                    Log.i("path",path)
                }
                Constants.RequestCode.REQUEST_PICK_FILE.ordinal->
                {
                    val uri=data!!.data
                    if (uri!!.toString().contains("content"))
                    {
                        val path=getRealPathFromURI(uri)
                        Log.i("path",path)
                    }
                    val file=File(uri.toString())
                    if (file.exists())
                    {
                        Log.i("file",file.name)
                    }
                    //val resultFile= File()
                }
            }
        }
    }
    fun getRealPathFromURI(contentUri: Uri):String?
    {
        var res:String?=null
        val projection:Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var cursor=contentResolver.query(contentUri,projection,null,null,null)
        try
        {
            if (cursor!=null)
            {
                val column=cursor.getColumnIndexOrThrow(projection[0])
                if(cursor.moveToFirst())
                {
                    res=cursor.getString(column)
                }
                cursor.close()
            }
            if (res==null)
            {
                cursor=contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,null,null,null)
                if (cursor!=null)
                {
                    val column=cursor.getColumnIndexOrThrow(projection[0])
                    if(cursor.moveToFirst())
                    {
                        res=cursor.getString(column)
                    }
                    cursor.close()
                }
            }
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }

        return res
    }

}
