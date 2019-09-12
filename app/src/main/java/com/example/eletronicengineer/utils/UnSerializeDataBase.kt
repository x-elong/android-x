package com.example.eletronicengineer.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.eletronicengineer.model.SelectFriendItem
import com.example.eletronicengineer.service.ReceiveService
import com.example.eletronicengineer.model.Constants
import retrofit2.http.FieldMap
import java.io.File

object UnSerializeDataBase
{
  var userToken=""
  var isLogined=false
  //需求模块 由于是填完后一次性上传 故只保存图片和文件
  val fileList:MutableList<FileMap> =ArrayList()
  val imgList:MutableList<BitmapMap> =ArrayList()
  lateinit var receiveServiceBinder: ReceiveService.CustomBinder
  val friendRequestList:MutableList<SelectFriendItem> =ArrayList()
  var username:String?=null
  //供应模块
  var pageMapList:MutableList<PageMap> =ArrayList()
}
class FileMap constructor(path:String,key:String)
{
  val file=File(path)
  val key:String=key
  var path:String=path
}
class BitmapMap constructor(var path:String,val key:String)
{
  val bitmap = BitmapFactory.decodeFile(path)
  var needDelete=false
}
class PageMap
{
  lateinit var type:Constants.PAGE_TYPE
  lateinit var dataMap:Map<String,String>
}