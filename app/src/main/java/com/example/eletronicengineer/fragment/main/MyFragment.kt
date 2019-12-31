package com.example.eletronicengineer.fragment.main

import android.content.ComponentName
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.*
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.db.My.UserSubitemEntity
import com.example.eletronicengineer.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_qrcode.view.*
import kotlinx.android.synthetic.main.dialog_recommended_application.view.*
import kotlinx.android.synthetic.main.my.view.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class MyFragment : Fragment() {
    lateinit var mView:View
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    lateinit var user:UserSubitemEntity
    private var headerImg = ""
    private var recommendNum= 0
    private var integral= "0"
    private var qrCodePath = ""
    var vipType = ""
    val vipLevels = arrayListOf("普通会员","精英会员","黄金会员")
    val glideImageLoader = GlideImageLoader()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.my, container, false)

        initOnClick()
        return mView
    }


    override fun onStart() {
        super.onStart()
        initFragment()
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        headerImg = pref.getString("headerImg","")
        glideImageLoader.displayImage(mView.iv_my_header,headerImg)
        recommendNum = pref.getInt("recommendNum",0)
        integral = pref.getString("integral","")
        vipType = pref.getString("vipType","")
        initData()
        val result = NetworkAdapter().getDataUser().subscribe( {
            user = it.message.user
            if(headerImg!=user.headerImg){
                headerImg = user.headerImg!!
                pref.edit().putString("headerImg",headerImg).apply()
                glideImageLoader.displayImage(mView.iv_my_header,headerImg)
            }
            integral = user.totalIntegral.toString()
            recommendNum = user.recommendResidueQuantity
            UnSerializeDataBase.userName = it.message.user.userName
            UnSerializeDataBase.userPhone = it.message.user.phone
            UnSerializeDataBase.userVipLevel = vipLevels.indexOf(it.message.vipLevel)
            if(it.message.vipLevel!=vipType){
                pref.edit().putString("vipType",this.vipType).apply()
            }
            vipType = it.message.vipLevel
            mView.tv_my_vip_level.text = vipType
            pref.edit().putInt("recommendNum",recommendNum).apply()
            pref.edit().putString("integral",integral).apply()
            Log.i("","$user.recommendResidueQuantity")
            initData()
        },{
            ToastHelper.mToast(mView.context,"获取个人信息异常")
            it.printStackTrace()
        })

//        initVipType(vipType)
//        NetworkAdapter().getDataUserOpenPower().subscribe( {
//            val openPowerEntity = it.message
//            val vipType = openPowerEntity.goodsPowerName
//            if(vipType!=this.vipType){
//                this.vipType = vipType
//                pref.edit().putString("vipType",this.vipType).apply()
//                initVipType(vipType)
//                }
//        },{
//            ToastHelper.mToast(mView.context,"获取会员等级信息异常")
//            it.printStackTrace()
//        })
    }
    fun initVipType(vipType :String){
        when(vipType){
            "elite_vip"->{
                this.vipType = "精英会员"
                UnSerializeDataBase.userVipLevel = 1
            }
            "gold_vip"->{
                this.vipType = "黄金会员"
                UnSerializeDataBase.userVipLevel = 2
            }
            "vip"->{
                this.vipType = "普通会员"
                UnSerializeDataBase.userVipLevel = 0
            }
            "tourist"->{
                this.vipType = "游客"
                UnSerializeDataBase.userVipLevel = -1
            }
        }
        mView.tv_my_vip_level.text = this.vipType
    }
    private fun initData() {
        if(mView.tv_integral.text.toString().substring(4)!=integral)
            mView.tv_integral.text = "当前积分:${integral}"
        if(mView.tv_recommend_num.text.toString().substring(5,mView.tv_recommend_num.text.length-2).toInt()!=recommendNum)
            mView.tv_recommend_num.text = "您已推荐 ${recommendNum} 人"
        if( mView.tv_username.text.toString()!=UnSerializeDataBase.userName)
            mView.tv_username.text = UnSerializeDataBase.userName
    }

    private fun initOnClick() {
//        mView.iv_my_header.setOnClickListener {
//            val intent = Intent(activity,ImageDisplayActivity::class.java)
//            intent.putExtra("imagePath",headerImg)
//            startActivity(intent)
//        }
        mView.view_my.setOnClickListener {
            val intent =Intent(activity,MyInformationActivity::class.java)
            startActivity(intent)
        }
//        mView.tv_my_vip.setOnClickListener{
//            val intent = Intent(activity,MyVipActivity::class.java)
//            startActivity(intent)
//        }
        mView.tv_qr_code.setOnClickListener {
            val intent =Intent(activity,MyQRCodeActivity::class.java)
            intent.putExtra("headerImg",headerImg)
            startActivity(intent)
        }
        mView.my_setting.setOnClickListener {
            val intent =Intent(activity,MySettingActivity::class.java)

            startActivity(intent)
        }
        mView.btn_my_vip_privileges.setOnClickListener {
//            val intent = Intent(activity,MyVipActivity::class.java)
//            startActivity(intent)
            val intent =Intent(activity,VipActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initFragment() {
//        getDataUser()
//        glideImageLoader.displayImage(mView.iv_my_bg,R.drawable.my_big_bg)
//        glideImageLoader.displayImage(mView.view_my,R.drawable.my_small_bg)

        mView.view_my_title_bg.viewTreeObserver.addOnGlobalLayoutListener {
            ImageLoadUtil.loadBackgound(mView.view_my_title_bg,mView.context,R.drawable.my_big_bg)
        }

//        mView.view_my.viewTreeObserver.addOnGlobalLayoutListener {
//            ImageLoadUtil.loadBackgound(mView.view_my,mView.context,R.drawable.my_small_bg)
//        }
        mMultiStyleItemList.clear()
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"我的发布",false))
        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener=View.OnClickListener {
            val intent = Intent(activity,MyReleaseActivity::class.java)
            startActivity(intent)
        }
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"分销中心",false))
        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener=View.OnClickListener {
            val intent = Intent(activity,RetailStoreActivity::class.java)
            intent.putExtra("headerImg",headerImg)
            startActivity(intent)
        }
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"我的报名",false))
        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener=View.OnClickListener {
            val intent = Intent(activity,MyRegistrationActivity::class.java)
            startActivity(intent)
        }
//        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"签名设置",false))
//        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener=View.OnClickListener {
//            val intent = Intent(activity,MySignatureActivity::class.java)
//            startActivity(intent)
//        }
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"认证管理",false))
        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener=View.OnClickListener {
            val intent = Intent(activity,MyCertificationActivity::class.java)
            startActivity(intent)
        }
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"帮助中心",false))
        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener=View.OnClickListener{
            activity!!.startActivity(Intent(mView.context,HelpCenterActivity::class.java))
        }
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"推荐电企通",false))
        mMultiStyleItemList[mMultiStyleItemList.size-1].jumpListener=View.OnClickListener {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            qrCodePath = pref.getString("qrCodePath","")
            val loadingDialog = LoadingDialog(mView.context,"正在加载...")
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
                    dialogView.btn_save_qr_code.setOnClickListener {
                        dialogView.iv_qr_code.apply {
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
                                //通知相册更新
                                val intent = Intent()
                                intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                                intent.setData(Uri.fromFile(File(path)))
                                mView.context.sendBroadcast(intent)
                                result = "二维码保存成功"
                                dialog.dismiss()
                                val dialog = BottomSheetDialog(context!!)
                                val dialogView = LayoutInflater.from(context!!).inflate(R.layout.dialog_recommended_application,null)
                                dialogView.view_sms.setOnClickListener {
                                    dialog.dismiss()
                                }
                                dialogView.view_we_chat.setOnClickListener {
                                    val wechatIntent = Intent(Intent.ACTION_SEND)
//                                    val cop = ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareImgUI")
//                                    wechatIntent.setComponent(cop)
                                    wechatIntent.setPackage("com.tencent.mm")
                                    wechatIntent.setType("image/*")
                                    wechatIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
                                    wechatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(Intent.createChooser(wechatIntent, "推荐电企通"))
                                    dialog.dismiss()
                                }
                                dialogView.view_qq.setOnClickListener {
                                    Log.i("headerImg",headerImg)
                                    val qqIntent = Intent(Intent.ACTION_SEND)
                                    qqIntent.setPackage("com.tencent.mobileqq")
                                    qqIntent.setType("image/*")
                                    qqIntent.putExtra(Intent.EXTRA_STREAM, path)
                                    startActivity(Intent.createChooser(qqIntent, "推荐电企通"))
                                    dialog.dismiss()
                                }
                                dialogView.tv_cancel.setOnClickListener {
                                    dialog.dismiss()
                                }

                                dialog.setCanceledOnTouchOutside(false)
                                dialog.setContentView(dialogView)
                                dialog.show()
                            }

                            ToastHelper.mToast(context,result)
                        }
                    }
                    dialog.setView(dialogView)
                    dialog.show()
                }else{
                    ToastHelper.mToast(mView.context,"获取二维码失败,加载失败.")
                }
            },{
                loadingDialog.dismiss()
                ToastHelper.mToast(mView.context,"服务器异常,加载失败.")
                it.printStackTrace()
            })
        }
        mView.rv_my_other_function_content.adapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_my_other_function_content.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("onDestroy onDestroy","")
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onStop() {
        super.onStop()
        Log.i("stop stop stop","")
    }
}