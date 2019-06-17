package com.example.eletronicengineer.utils

import com.example.eletronicengineer.model.HttpResult
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import rx.Observable
import rx.Observer
import rx.Scheduler
import rx.internal.schedulers.SchedulerWhen
import rx.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

interface HttpHelper
{
    @POST("/excel/downloadexcel/{filename}")
    fun downloadExcel(@Path("filename") filename:String):Call<ResponseBody>
    fun sendMessage(keys:Array<String>,values:Array<String>):Observable<ResponseBody>
}
internal fun startSendMessage(keys:Array<String>,values:Array<String>)
{
    val retrofit=Retrofit.Builder().baseUrl("").addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build()
    val httpHelper=retrofit.create(HttpHelper::class.java)
}
internal fun downloadFile(targetPath:String,targetFileName:String,webFileName:String,baseUrl:String)
{
    val retrofit= Retrofit.Builder().baseUrl(baseUrl).build()
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
            try {
                val totalLength=response.body()!!.contentLength()
                var current=0
                var len:Int
                fos= FileOutputStream(file)
                while (inputStream.read(buf).let { len=it;len!=-1})
                {
                    len=inputStream.read(buf)
                    current+=len
                    fos.write(buf,0,len)
                }
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