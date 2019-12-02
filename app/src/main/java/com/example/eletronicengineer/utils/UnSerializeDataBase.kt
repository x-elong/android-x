package com.example.eletronicengineer.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.eletronicengineer.model.SelectFriendItem
import com.example.eletronicengineer.service.ReceiveService
import com.example.eletronicengineer.model.Constants
import retrofit2.http.FieldMap
import java.io.File

/**
 * @权限模块端口:8026
 * @供需模块端口:8012
 * @我的模块端口:8032
 * @学校服务器 10.1.5.141
 * @联通服务器 ycdlfw.com
 * @新服务器 111.230.150.200
 * @某人本地服务器 192.168.1.132
 */
object UnSerializeDataBase
{
  const val ip = "www.ycdlfw.com"
//  const val upmsBasePath = "http://192.168.1.149:8026/"
//  const val dmsBasePath = "http://${ip}/dms/"
//  const val mineBasePath = "http://192.168.1.149:8032/"
  const val upmsBasePath = "http://${ip}/upms/"
  const val dmsBasePath = "http://${ip}/dms/"
  const val mineBasePath = "http://${ip}/mine/"
  var userToken=""
  var cookie:String =""
  var userName:String=""
  var userPhone:String = ""
  var idCardName = ""
  var userVipLevel = 0
  var isLogined=false
  var vipOpenState = -1
  var isCertificate = false
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