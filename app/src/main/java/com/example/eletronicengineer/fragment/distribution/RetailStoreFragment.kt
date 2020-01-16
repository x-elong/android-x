package com.example.eletronicengineer.fragment.distribution

import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyQRCodeActivity
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.fragment.main.MyFragment
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.getOwnSpreadIntegral
import com.example.eletronicengineer.wxapi.WXShare
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_qrcode.view.*
import kotlinx.android.synthetic.main.dialog_recommended_application.view.*
import kotlinx.android.synthetic.main.fragment_retail_store.view.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class RetailStoreFragment:Fragment() {
    companion object{
//        const val SPREAD_DETAIL=1
//        const val RETURN_DETAIL=2
        const val SCORE_DETAIL=3
        const val LAST_VIP=4
        const val DETAILS=5
        fun newInstance(args: Bundle): RetailStoreFragment
        {
            val fragment= RetailStoreFragment()
            fragment.arguments=args
            return fragment
        }
    }
    lateinit var title:String
    lateinit var headerImg:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_retail_store, container, false)
//        title=arguments!!.getString("title")
//        view.tv_retail_title.text=title
        headerImg = arguments!!.getString("headerImg","")
        initFragment(view)
        return view
    }

    private fun initFragment(view: View) {
        view.tv_retail_back.setOnClickListener {
            activity!!.finish()
        }
        //头像
        GlideImageLoader().displayImage(view.iv_retail_header,headerImg)
        view.tv_retail_person_name.text = UnSerializeDataBase.userPhone
        val data = Bundle()
        //商城
        view.tv_total_score_switch.setOnClickListener {
            ToastHelper.mToast(view.context,"暂未开放")
        }
        //兑换
        view.tv_total_score_cash.setOnClickListener {
            ToastHelper.mToast(view.context,"暂未开放")
        }
        //二维码按钮监听
        view.tv_retail_two_dimensional_code_image.setOnClickListener{
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            var qrCodePath = pref.getString("qrCodePath","")
            val loadingDialog = LoadingDialog(view.context,"正在加载...")
            loadingDialog.show()
            getRemoteCode().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                loadingDialog.dismiss()
                val json = JSONObject(it.string())
                Log.i("json",json.toString())
                if(json.getInt("code")==200){
                    val qrCodeUrl = json.getString("message")
                    val dialog = AlertDialog.Builder(context!!).create()
                    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_qrcode,null)
                    GlideImageLoader().displayImage(dialogView.iv_qr_code,qrCodeUrl)
                    dialogView.btn_save_qr_code.visibility = View.VISIBLE
                    dialogView.btn_save_qr_code.setOnClickListener {
                        dialogView.cl.apply {
                            btn_save_qr_code.visibility = View.GONE
                            val image= Bitmap.createBitmap(measuredWidth,measuredHeight,
                                Bitmap.Config.ARGB_8888)
                            draw(Canvas(image))
                            //获取图片名
                            val name = qrCodeUrl.substring(qrCodeUrl.lastIndexOf("/")+1,qrCodeUrl.lastIndexOf("?"))
                            val filePath = "${UnSerializeDataBase.filePath}/image"
                            //缓存的二维码路径与数据库的不一样
                            if(qrCodePath!=name){
                                val file = File("${filePath}/${qrCodePath}")
                                if(file.isFile && file.exists())
                                    file.delete()
                                qrCodePath = name
                                pref.edit().putString("qrCodePath",qrCodePath).apply()
                            }
                            //判断文件夹是否存在 不存在就创建
                            val file = File(filePath)
                            file.mkdirs()
                            if(!file.exists()) {
                                file.mkdirs()
                            }
                            val path = "${filePath}/${name}"
//                            写入到文件夹下
                            val fos= FileOutputStream(path)
                            image.compress(Bitmap.CompressFormat.PNG,100,fos)
                            var result = "二维码保存失败"
                            Log.i("qrCode Url",path)
                            if(File(path).exists()){
                                MediaStore.Images.Media.insertImage(context.contentResolver,
                                    path, name, null)
                                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(path)))
                                result = "二维码保存成功"
                                dialog.dismiss()
                            }
                            ToastHelper.mToast(view.context,result)
                        }
                    }
                    dialog.setView(dialogView)
                    dialog.show()
                }else{
                    ToastHelper.mToast(view.context,"二维码服务器异常,加载失败.")
                }
            },{
                ToastHelper.mToast(view.context,"二维码服务器异常,加载失败.")
                loadingDialog.dismiss()
                it.printStackTrace()
            })
        }

        //推广积分
        val resultForSpreadScore = getOwnSpreadIntegral(UnSerializeDataBase.shoppingPath).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                if(it.message!= null)
                    view.tv_spread_score_num.text = it.message.toString()
                else
                    view.tv_return_score_num.text = "无"
            },{
                it.printStackTrace()
                ToastHelper.mToast(view.context,"服务器错误")
            })
        //收益积分
        val resultForReturnScore = getOwnReturnIntegral(UnSerializeDataBase.shoppingPath).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                if(it.message!= null)
                    view.tv_return_score_num.text = it.message.toString()
                else
                    view.tv_return_score_num.text = "无"
            },{
                ToastHelper.mToast(view.context,"服务器错误")
                it.printStackTrace()
            })

        //月推广积分
        val resultForMonthSpreadBenifit = getUserSpreadIncome("month", UnSerializeDataBase.shoppingPath).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                if(it.message!= null)
                {
                    view.tv_month_income_spread_num.text = it.message.toString()
//                    val listener1 = View.OnClickListener { //推广详情监听
//                        data.putInt("type",SPREAD_DETAIL)
//                        data.putString("title","推广详情")
//                        FragmentHelper.switchFragment(activity!!,IntegralFragment.newInstance(data),R.id.frame_retail_store,"")
//                    }
//                    view.tv_month_income_spread_score.setOnClickListener(listener1)
//                    view.tv_month_income_spread_num.setOnClickListener(listener1)
                }
                else{
                    view.tv_month_income_spread_num.text = "无"
                }
            },{
                ToastHelper.mToast(view.context,"服务器错误")
                it.printStackTrace()
            })
        //月返利积分
        val resultForMonthReturnBenifit = getUserReturnIncome("month", UnSerializeDataBase.shoppingPath).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                if(it.message!= null)
                {
                    view.tv_month_income_return_num.text = it.message.toString()
//                    val listener1 = View.OnClickListener { //返利积分监听
//                        data.putInt("type", RETURN_DETAIL)
//                        data.putString("title","返利详情")
//                        FragmentHelper.switchFragment(activity!!,IntegralFragment.newInstance(data),R.id.frame_retail_store,"")
//                    }
//                    view.tv_month_income_return_score.setOnClickListener(listener1)
//                    view.tv_month_income_return_num.setOnClickListener(listener1)
                }
                else{
                    view.tv_month_income_return_num.text = "无"
                }
            },{
                ToastHelper.mToast(view.context,"服务器错误")
                it.printStackTrace()
            })
        //日推广积分
        val resultForDaySpreadBenifit = getUserSpreadIncome("day", UnSerializeDataBase.shoppingPath).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                if(it.message!= null)
                {
                    view.tv_day_income_spread_num.text = it.message.toString()
//                    val listener1 = View.OnClickListener { //推广详情监听
//                        data.putInt("type",SPREAD_DETAIL)
//                        data.putString("title","推广详情")
//                        FragmentHelper.switchFragment(activity!!,IntegralFragment.newInstance(data),R.id.frame_retail_store,"")
//                    }
//                    view.tv_day_income_spread_score.setOnClickListener(listener1)
//                    view.tv_day_income_spread_num.setOnClickListener(listener1)
                }
                else{
                    view.tv_day_income_spread_num.text = "无"
                }
            },{
                ToastHelper.mToast(view.context,"服务器错误")
                it.printStackTrace()
            })
        //日返利积分
        val resultForDayReturnBenifit = getUserReturnIncome("day", UnSerializeDataBase.shoppingPath).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                if(it.message!= null)
                {
                    view.tv_day_income_return_num.text = it.message.toString()
//                    val listener1 = View.OnClickListener { //返利积分监听
//                        data.putInt("type", RETURN_DETAIL)
//                        data.putString("title","返利详情")
//                        FragmentHelper.switchFragment(activity!!,IntegralFragment.newInstance(data),R.id.frame_retail_store,"")
//                    }
//                    view.tv_day_income_return_score.setOnClickListener(listener1)
//                    view.tv_day_income_return_num.setOnClickListener(listener1)
                }
                else{
                    view.tv_day_income_return_num.text = "无"
                }
            },{
                ToastHelper.mToast(view.context,"服务器错误")
                it.printStackTrace()
            })
        //积分详情按钮监听
        view.tv_score_details_image.setOnClickListener {
              data.putInt("type",SCORE_DETAIL)
              data.putString("title","积分详情")
              FragmentHelper.switchFragment(activity!!,IntegralFragment.newInstance(data),R.id.frame_retail_store,"")
        }
        //下级会员按钮监听
        view.tv_sub_vip_image.setOnClickListener {
            data.putInt("type",LAST_VIP)
            data.putString("title","下级会员")
            FragmentHelper.switchFragment(activity!!,IntegralFragment.newInstance(data),R.id.frame_retail_store,"")
        }
        //推广详情按钮监听
        view.tv_promote_details_image.setOnClickListener {
            data.putInt("type",DETAILS)
            data.putString("title","详情")
            FragmentHelper.switchFragment(activity!!,IntegralFragment.newInstance(data),R.id.frame_retail_store,"")
        }
    }
}