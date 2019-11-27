package com.example.eletronicengineer.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.eletronicengineer.model.SelectFriendItem
import com.example.eletronicengineer.service.ReceiveService
import com.example.eletronicengineer.model.Constants
import retrofit2.http.FieldMap
import java.io.File

/**
 * @权限模块:ycdlfw.com:8026
 * @供需模块:ycdlfw.com:8012
 * @我的模块:ycdlfw.com:8032
 */
object UnSerializeDataBase
{
  const val upmsBasePath = "http://192.168.1.132:8026"
  const val dmsBasePath = "http://192.168.1.132:8012"
  const val mineBasePath = "http://192.168.1.132:8032"
//  const val upmsBasePath = "http://10.1.5.141:8026"
//  const val dmsBasePath = "http://10.1.5.141:8012"
//  const val mineBasePath = "http://10.1.5.141:8032"
  var userToken=""
  var cookie:String =""
  var userName:String=""
  var userPhone:String = ""
  var idCardName = ""
  var userVipLevel = 0
  var isLogined=false
  var vipOpenState = -1
  //需求模块 由于是填完后一次性上传 故只保存图片和文件
  val fileList:MutableList<FileMap> =ArrayList()
  val imgList:MutableList<BitmapMap> =ArrayList()
  lateinit var receiveServiceBinder: ReceiveService.CustomBinder
  val friendRequestList:MutableList<SelectFriendItem> =ArrayList()
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
  val bitMap = BitmapFactory.decodeFile(path)
  var needDelete=false
}
class PageMap
{
  lateinit var type:Constants.PAGE_TYPE
  lateinit var dataMap:Map<String,String>
}