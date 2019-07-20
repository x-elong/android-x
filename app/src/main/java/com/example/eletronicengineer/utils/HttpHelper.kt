package com.example.eletronicengineer.utils

import android.util.Log
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.model.HttpResult
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

interface HttpHelper
{
  @POST(Constants.HttpUrlPath.Requirement.excel)
  fun downloadExcel(@Path("fileName") filename:String):Call<ResponseBody>

  @POST(Constants.HttpUrlPath.Requirement.requirementLeaseCar)
  @FormUrlEncoded
  fun sendSimpleMessage(@FieldMap data:Map<String,String>):Observable<ResponseBody>

  @Multipart
  @POST
  fun SendMultiPartMessage(@PartMap data: Map<String,RequestBody>):Observable<HttpResult>
}
class ResponseInterceptor:Interceptor
{
  override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
    val response=chain.proceed(chain.request())
    val modified=response.newBuilder().addHeader("Content-Type","application/octet-stream;charset=utf-8").build()
    return modified
  }
}
internal fun startSendMessage(keys:Array<String>,values:Array<String>,baseUrl: String)
{
  val retrofit=Retrofit.Builder().baseUrl(baseUrl).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
  val httpHelper=retrofit.create(HttpHelper::class.java)
  val map=HashMap<String,String>(keys.size)
  for (j in 0 until keys.size)
  {
    map[keys[j]] = values[j]
  }
  val result=httpHelper.sendSimpleMessage(map).subscribe(
    {
      Log.i("responseBody",it.string())
    },
    {
      it.printStackTrace()
    }
  )
}
internal fun startSendMultiPartMessage(map:Map<String,RequestBody>,baseUrl: String)
{
  val retrofit=Retrofit.Builder().baseUrl(baseUrl).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
  val httpHelper=retrofit.create(HttpHelper::class.java)
  val result=httpHelper.SendMultiPartMessage(map).subscribe {
    Log.i("retrofitMsg","")
  }
}
internal fun startSendMessage(map:Map<String,String>,baseUrl: String)
{
  val retrofit=Retrofit.Builder().baseUrl(baseUrl).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
  val httpHelper=retrofit.create(HttpHelper::class.java)
  val result=httpHelper.sendSimpleMessage(map).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
    {
      Log.i("responseBody",it.string())
    },
    {
      it.printStackTrace()
    }
  )
}
internal fun downloadFile(targetPath:String,targetFileName:String,webFileName:String,baseUrl:String)
{
  val okhttp=OkHttpClient.Builder().addInterceptor(ResponseInterceptor()).build()
  val retrofit= Retrofit.Builder().baseUrl(baseUrl).client(okhttp).build()
  val file= File(targetPath,targetFileName)
  val httpHelper=retrofit.create(HttpHelper::class.java)
  httpHelper.downloadExcel(webFileName).enqueue(object:Callback<ResponseBody>{
    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
      t.printStackTrace()
    }

    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
      val inputStream=response.body()!!.byteStream()
      var buf=ByteArray(2048)
      var fos: FileOutputStream?=null
      try
      {
        val totalLength=response.body()!!.contentLength()
        var current=0
        var len:Int
        fos= FileOutputStream(file)
        while (inputStream.read(buf).let { len=it;len!=-1})
        {
          fos.write(buf,0,len)
          current+=len
        }
        fos.write(buf,0,len)
        Log.i("size",current.toString())
        fos.flush()
      }
      catch (ioExp: IOException)
      {
        ioExp.printStackTrace()
      }
      finally {
        inputStream.close()
        if (fos!=null)
          fos.close()
      }
    }
  })

}