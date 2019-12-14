package com.example.eletronicengineer.fragment.my

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.db.My.UserEntity
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.model.HttpResult
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.putSimpleMessage
import com.example.eletronicengineer.utils.uploadImage
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.lcw.library.imagepicker.ImagePicker
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_datepicker.view.*
import kotlinx.android.synthetic.main.dialog_edit.view.*
import kotlinx.android.synthetic.main.dialog_upload.view.*
import kotlinx.android.synthetic.main.fragment_my_information.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


class MyInformationFragment : Fragment() {
    val glideLoader = GlideLoader()
    var adapter:RecyclerviewAdapter?=null
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_my_information, container, false)
        mView.tv_my_information_back.setOnClickListener {
            activity!!.finish()
        }
        if(adapter==null)
            initFragment()
        else
        {
            (mView.rv_my_information_content.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            mView.rv_my_information_content.adapter = adapter
            mView.rv_my_information_content.layoutManager = LinearLayoutManager(context)
        }
        return mView
    }
    private fun initFragment() {
        val result = NetworkAdapter().getDataUser().subscribe {
            initDataUser(it)
        }

        (mView.rv_my_information_content.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
    }
    //将照片设置在对于的控件上
    fun refresh(mImagePath: String) {
        val loadingDialog = LoadingDialog(mView.context, "正在提交中...", R.mipmap.ic_dialog_loading)
        loadingDialog.show()
        val file = File(mImagePath)
        val imagePart = MultipartBody.Part.createFormData("file",file.name, RequestBody.create(MediaType.parse("image/*"),file))
        val result = uploadImage(imagePart).observeOn(AndroidSchedulers.mainThread()).subscribe({
            val json = JSONObject(it.string())
            Log.i("json",json.toString())
            if(json.getBoolean("success")){
                val imagePath = json.getString("httpUrl")
                val result = Observable.create<RequestBody>{
                    Log.i("it",it.toString())
                    val json = JSONObject().put("headerImg",imagePath)
                    val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
                    it.onNext(requestBody)
                }.subscribe {
                    val result = putSimpleMessage(it,UnSerializeDataBase.mineBasePath+Constants.HttpUrlPath.My.updateHeaderImg)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                            loadingDialog.dismiss()
                            val json = JSONObject(it.string())
                            Log.i("imagePath + code",imagePath+json.toString())
                            var result = ""
                            if(json.getInt("code")==200){
                                result = "更新成功"
                                adapter!!.mData[0].shiftInputPicture = imagePath
                                adapter!!.notifyItemChanged(0)
                            }else{
                                result = "更新失败"
                            }
                            ToastHelper.mToast(mView.context,result)
                        },{
                            loadingDialog.dismiss()
                            ToastHelper.mToast(mView.context,"提交头像异常")
                            it.printStackTrace()
                        })
            }
            }
            else{
                ToastHelper.mToast(mView.context,"上传失败")
                loadingDialog.dismiss()
            }
        },{
            loadingDialog.dismiss()
            ToastHelper.mToast(mView.context,"上传照片异常")
            it.printStackTrace()
        })
    }
    fun initDataUser(httpResult: HttpResult<UserEntity>){
                val user = httpResult.message.user
                val userJson = JSONObject(Gson().toJson(user))
                val mMultiStyleItemList: MutableList<MultiStyleItem> = ArrayList()
                var item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "头像", "","1")
                if(user.headerImg!=null)
                    item.shiftInputPicture = user.headerImg.toString()
                item.jumpListener = View.OnClickListener {
                    val dialog = BottomSheetDialog(context!!)
                    val dialogView = LayoutInflater.from(context!!).inflate(R.layout.dialog_upload, null)
                    dialogView.btn_photograph.setOnClickListener {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        // UnSerializeDataBase.imgList.add(BitmapMap("", mData[5].additionalKey))
                        activity!!.startActivityForResult(intent, Constants.RequestCode.REQUEST_PHOTOGRAPHY.ordinal)
                        dialog.dismiss()
                    }
                    dialogView.btn_album.setOnClickListener {
                        ImagePicker.getInstance()
                            .setTitle("图片")//设置标题
                            .showCamera(true)//设置是否显示拍照按钮
                            .showImage(true)//设置是否展示图片
                            .showVideo(true)//设置是否展示视频
                            .showVideo(true)//设置是否展示视频
                            .setSingleType(true)//设置图片视频不能同时选择
                            .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                            //.setImagePaths(mImagePaths)//保存上一次选择图片的状态，如果不需要可以忽略
                            .setImageLoader(glideLoader)//设置自定义图片加载器
                            .start(activity, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
                        dialog.dismiss()
                    }
                    dialogView.btn_cancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.setCanceledOnTouchOutside(false)
                    dialog.setContentView(dialogView)
                    dialog.show()
                }
                mMultiStyleItemList.add(item)
//                item = MultiStyleItem(MultiStyleItem.Options.HINT, "部门信息")
//                mMultiStyleItemList.add(item)
//                item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "公司", "湖南宇畅有限公司")
//                mMultiStyleItemList.add(item)
//                item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "部门", "董事长室")
//                mMultiStyleItemList.add(item)
//                item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "职务", "董事长")
//                mMultiStyleItemList.add(item)
                item = MultiStyleItem(MultiStyleItem.Options.HINT, "账号信息")
                mMultiStyleItemList.add(item)
                if(true){
                    val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"邮箱","未绑定")
                    var email =""
                    if(user.email!=null && user.email!=""){
                        email = user.email.toString()
                        item.shiftInputContent = email
                    }

                    item.jumpListener = View.OnClickListener {
                        val bundle = Bundle()
                        bundle.putString("email",email)
                        FragmentHelper.switchFragment(activity!!,BindEmailFragment.newInstance(bundle),R.id.frame_my_information,"bindEmail")
                    }
                    mMultiStyleItemList.add(item)
                }
                item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "手机", user.phone)
                item.jumpListener = View.OnClickListener {
                    val bundle = Bundle()
                    FragmentHelper.switchFragment(activity!!,BindPhoneFragment.newInstance(bundle),R.id.frame_my_information,"")
                }
                mMultiStyleItemList.add(item)
//                item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "微信", "换绑")
//                mMultiStyleItemList.add(item)
//                item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "QQ", "换绑")
//                mMultiStyleItemList.add(item)
//                item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "邮箱", "换绑")
//                mMultiStyleItemList.add(item)
                item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "密码", "* * * * * * * * *")
                item.jumpListener = View.OnClickListener {
                    FragmentHelper.switchFragment(activity!!,ChangePasswordFragment(),R.id.frame_my_information,"")
                }
                mMultiStyleItemList.add(item)

                item = MultiStyleItem(MultiStyleItem.Options.HINT, "个人信息")
                mMultiStyleItemList.add(item)
                item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "身份证姓名", "未认证")
                if(user.name!=null)
                    item.singleDisplayRightContent = user.name.toString()
                mMultiStyleItemList.add(item)
                item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "证件号码", "未认证")
                if(user.identifyCard!=null)
                    item.singleDisplayRightContent = user.identifyCard.toString()
                mMultiStyleItemList.add(item)
                item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "电企通帐号", user.userName)
                mMultiStyleItemList.add(item)

                if (true) {
                    val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "性别", "未设置")
                    if(user.workerSex==true){
                        item.shiftInputContent = "男"
                    }else if(user.workerSex==false){
                        item.shiftInputContent = "女"
                    }
                    mMultiStyleItemList.add(item)
                    item.jumpListener = View.OnClickListener {
                        val items = arrayListOf("女", "男")
                        var checkedItem = 0
                        if(item.shiftInputContent!="未设置")
                            items.indexOf(item.shiftInputContent)
                        val dialog = AlertDialog.Builder(context!!)
                            .setTitle("性别")
                            .setSingleChoiceItems(items.toTypedArray(), checkedItem, object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    checkedItem = p1
                                }
                            })
                            .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    val loadingDialog = LoadingDialog(mView.context, "正在提交中...", R.mipmap.ic_dialog_loading)
                                    loadingDialog.show()
                                    val result = Observable.create<RequestBody> {
                                        //json.remove(key)
                                        //val imagePath = upImage(key)
                                        //var jsonObject= json.put(key,upImage(key))
                                        val jsonObject = userJson.put("workerSex", checkedItem)
                                        Log.i("json ,,", jsonObject.toString())
                                        val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                                        it.onNext(requestBody)
                                    }.subscribe {
                                        val result =
                                            putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.upateDTO1).observeOn(AndroidSchedulers.mainThread())
                                                .subscribeOn(Schedulers.io())
                                                .subscribe(
                                                    {
                                                        loadingDialog.dismiss()
//                                                        Toast.makeText(context,it.string(),Toast.LENGTH_SHORT).show()
                                                        if (JSONObject(it.string()).getInt("code") == 200) {
                                                            adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent =
                                                                items[checkedItem]
                                                            adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                                                        }
                                                    },
                                                    {
                                                        loadingDialog.dismiss()
                                                        it.printStackTrace()
                                                    }
                                                )
                                    }
                                }
                            })
                            .create()
                        dialog.show()
                    }
                }

                if (true) {
                    val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "出生日期", "未设置")
                    if(user.birthday!=null){
                        item.shiftInputContent = SimpleDateFormat("yyyy-MM-dd").format(user.birthday)
                    }
                    mMultiStyleItemList.add(item)
                    item.jumpListener = View.OnClickListener {
                        val dialog = AlertDialog.Builder(context!!)
                        val mDialogView = layoutInflater.inflate(R.layout.dialog_datepicker, null)
                        if (item.shiftInputContent != "未设置") {
                            val s = item.shiftInputContent.split("-")
                            mDialogView.date_picker.init(s[0].toInt(), s[1].toInt() - 1, s[2].toInt(), null)
                        }
                        dialog.setTitle("出生日期")
                            .setView(mDialogView)
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                                val date = java.sql.Date.valueOf("${mDialogView.date_picker.year}-${mDialogView.date_picker.month + 1}-${mDialogView.date_picker.dayOfMonth}")
                                val result = Observable.create<RequestBody> {
                                    //json.remove(key)
                                    //val imagePath = upImage(key)
                                    //var jsonObject= json.put(key,upImage(key))
                                    val jsonObject = userJson.put("birthday", date)
                                    Log.i("json ,,", jsonObject.toString())
                                    val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                                    it.onNext(requestBody)
                                }.subscribe {
                                    val result =
                                        putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.upateDTO1).observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(
                                                {
                                                    //                                                        Toast.makeText(context,it.string(),Toast.LENGTH_SHORT).show()
                                                    if (JSONObject(it.string()).getInt("code") == 200) {
                                                        item.shiftInputContent = date.toString()
                                                        mView.rv_my_information_content.adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                                                    }
                                                },
                                                {
                                                    it.printStackTrace()
                                                }
                                            )
                                }
                            })
                            .create()
                            .show()
                    }
                }
                if (true) {
                    val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "血型", "未设置")
                    if(user.bloodType!=null){
                        when(user.bloodType!!.toInt()){
                            1->item.shiftInputContent = "A型"
                            2->item.shiftInputContent = "A型"
                            3->item.shiftInputContent = "AB型"
                            4->item.shiftInputContent = "O型"
                            else -> "其他"
                        }
                    }
                    mMultiStyleItemList.add(item)
                    item.jumpListener = View.OnClickListener {
                        val items = arrayListOf("A型", "B型", "AB型", "O型","其他")
                        var checkedItem = 0
                        if(item.shiftInputContent!="未设置")
                            items.indexOf(item.shiftInputContent)
                        val builder = AlertDialog.Builder(context!!)
                        builder.setTitle("血型")
                        builder.setSingleChoiceItems(items.toTypedArray(), checkedItem, object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                checkedItem = p1
                            }
                        })

                        builder.setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                            val result = Observable.create<RequestBody> {
                                //json.remove(key)
                                //val imagePath = upImage(key)
                                //var jsonObject= json.put(key,upImage(key))
                                val jsonObject = userJson.put("bloodType", checkedItem+1)
                                Log.i("json ,,", jsonObject.toString())
                                val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                                it.onNext(requestBody)
                            }.subscribe {
                                val result =
                                    putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.upateDTO1).observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(
                                            {
                                                //                                                        Toast.makeText(context,it.string(),Toast.LENGTH_SHORT).show()
                                                if (JSONObject(it.string()).getInt("code") == 200) {
                                                    adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent =
                                                        items[checkedItem]
                                                    adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                                                }
                                            },
                                            {
                                                it.printStackTrace()
                                            }
                                        )
                            }
                        })
                        val dialog = builder.create()
                        dialog.show()
                    }
                }
                if(true){
                    val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"民族","未设置")
                    if(user.nation!=null)
                        item.shiftInputContent = user.nation.toString()
                    mMultiStyleItemList.add(item)
                    item.jumpListener = View.OnClickListener {
                        val dialog = AlertDialog.Builder(context!!)
                        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit,null)
                        if(item.shiftInputContent!="未设置")
                            dialogView.et_dialog.setText(item.shiftInputContent)
                        dialogView.et_dialog.setSelection(dialogView.et_dialog.text.length)
                        dialog.setTitle("民族")
                            .setView(dialogView)
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定",DialogInterface.OnClickListener() { _, _ ->
                                val result = Observable.create<RequestBody> {
                                    //json.remove(key)
                                    //val imagePath = upImage(key)
                                    //var jsonObject= json.put(key,upImage(key))
                                    val jsonObject = userJson.put("nation", dialogView.et_dialog.text)
                                    Log.i("json ", jsonObject.toString())
                                    val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                                    it.onNext(requestBody)
                                }.subscribe {
                                    val result =
                                        putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.upateDTO1).observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(
                                                {
                                                    //                                                        Toast.makeText(context,it.string(),Toast.LENGTH_SHORT).show()
                                                    if (JSONObject(it.string()).getInt("code") == 200) {
                                                        adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = dialogView.et_dialog.text.toString()
                                                        adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                                                    }
                                                },
                                                {
                                                    it.printStackTrace()
                                                }
                                            )
                                }
                            })
                            .create()
                            .show()
                    }
                }
                item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "联系电话", user.phone)
                mMultiStyleItemList.add(item)
                if (true) {
                    val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "婚姻状况", "未设置")
                    if(user.marriageState==0){
                        item.shiftInputContent = "未婚"
                    }else if(user.marriageState==1){
                        item.shiftInputContent = "已婚"
                    }
                    item.jumpListener = View.OnClickListener {
                        val items = arrayListOf("未婚", "已婚")
                        var checkedItem = 0
                            if(item.shiftInputContent!="未设置")
                                items.indexOf(item.shiftInputContent)
                        val builder = AlertDialog.Builder(context!!)
                        builder.setTitle("婚姻状况")
                        builder.setSingleChoiceItems(items.toTypedArray(), checkedItem, object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                checkedItem = p1
                            }
                        })

                        builder.setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                            val result = Observable.create<RequestBody> {
                                //json.remove(key)
                                //val imagePath = upImage(key)
                                //var jsonObject= json.put(key,upImage(key))
                                val jsonObject = userJson.put("marriageState", checkedItem)
                                Log.i("json ,,", jsonObject.toString())
                                val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                                it.onNext(requestBody)
                            }.subscribe {
                                val result =
                                    putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.upateDTO1).observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(
                                            {
                                                //                                                        Toast.makeText(context,it.string(),Toast.LENGTH_SHORT).show()
                                                if (JSONObject(it.string()).getInt("code") == 200) {
                                                    adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent =
                                                        items[checkedItem]
                                                    adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                                                }
                                            },
                                            {
                                                it.printStackTrace()
                                            }
                                        )
                            }
                        })
                        val dialog = builder.create()
                        dialog.show()
                    }
                    mMultiStyleItemList.add(item)
                }

                if (true) {
                    val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "首次参加工作时间", "未设置")
                    if(user.firstWorkTime!=null)
                        item.shiftInputContent =  SimpleDateFormat("yyyy-MM-dd").format(user.firstWorkTime)
                    mMultiStyleItemList.add(item)
                    item.jumpListener = View.OnClickListener {
                        val dialog = AlertDialog.Builder(context!!)
                        val mDialogView = layoutInflater.inflate(R.layout.dialog_datepicker, null)
                        if (item.shiftInputContent != "未设置") {
                            val s = item.shiftInputContent.split("-")
                            mDialogView.date_picker.init(s[0].toInt(), s[1].toInt() - 1, s[2].toInt(), null)
                        }
                        dialog.setTitle("首次参加工作时间")
                            .setView(mDialogView)
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                                val date = java.sql.Date.valueOf("${mDialogView.date_picker.year}-${mDialogView.date_picker.month + 1}-${mDialogView.date_picker.dayOfMonth}")
                                val result = Observable.create<RequestBody> {
                                    //json.remove(key)
                                    //val imagePath = upImage(key)
                                    //var jsonObject= json.put(key,upImage(key))
                                    val jsonObject = userJson.put("firstWorkTime", date)
                                    Log.i("json ,,", jsonObject.toString())
                                    val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                                    it.onNext(requestBody)
                                }.subscribe {
                                    val result =
                                        putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.upateDTO1).observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(
                                                {
                                                    //                                                        Toast.makeText(context,it.string(),Toast.LENGTH_SHORT).show()
                                                    if (JSONObject(it.string()).getInt("code") == 200) {
                                                        item.shiftInputContent = date.toString()
                                                        mView.rv_my_information_content.adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                                                    }
                                                },
                                                {
                                                    it.printStackTrace()
                                                }
                                            )
                                }
                            })
                            .create()
                            .show()
                    }
                }
                if(true){
                    val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "户籍类型", "未设置")
                    if(user.censusType==false)
                        item.shiftInputContent =  "非农户口"
                    else if(user.censusType==true)
                        item.shiftInputContent =  "农业户口"
                    item.jumpListener = View.OnClickListener {
                        val items = arrayListOf("非农户口", "农业户口")
                        var checkedItem = 0
                        if(item.shiftInputContent!="未设置")
                            items.indexOf(item.shiftInputContent)
                        var isChecked = false
                        val builder = AlertDialog.Builder(context!!)
                        builder.setTitle("户籍类型")
                        builder.setSingleChoiceItems(items.toTypedArray(), checkedItem, object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                checkedItem = p1
                                isChecked = true
                            }
                        })

                        builder.setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                            if(items[checkedItem]=="其他"){
                                val dialog = AlertDialog.Builder(context!!)
                                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit,null)
                                if(item.shiftInputContent!="未设置")
                                    dialogView.et_dialog.setText(item.shiftInputContent)
                                dialogView.et_dialog.setSelection(dialogView.et_dialog.text.length)
                                dialog.setTitle("户籍类型")
                                    .setView(dialogView)
                                    .setNegativeButton("取消",null)
                                    .setPositiveButton("确定",DialogInterface.OnClickListener() { _, _ ->
                                        val result = Observable.create<RequestBody> {
                                            //json.remove(key)
                                            //val imagePath = upImage(key)
                                            //var jsonObject= json.put(key,upImage(key))
                                            val jsonObject = userJson.put("censusType", isChecked)
                                            Log.i("json ,,", jsonObject.toString())
                                            val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                                            it.onNext(requestBody)
                                        }.subscribe {
                                            val result =
                                                putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.upateDTO1).observeOn(AndroidSchedulers.mainThread())
                                                    .subscribeOn(Schedulers.io())
                                                    .subscribe(
                                                        {
                                                            //                                                        Toast.makeText(context,it.string(),Toast.LENGTH_SHORT).show()
                                                            if (JSONObject(it.string()).getInt("code") == 200) {
                                                                adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = dialogView.et_dialog.text.toString()
                                                                adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                                                            }
                                                        },
                                                        {
                                                            it.printStackTrace()
                                                        }
                                                    )
                                        }
                                    })
                                    .create()
                                    .show()
                            }
                            else{
                                adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = items[checkedItem]
                                adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                            }
                        })
                        val dialog = builder.create()
                        dialog.show()
                    }
                    mMultiStyleItemList.add(item)
                }

                if(true){
                    val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"家庭住址","未设置")
                    if(user.vipAddress!=null)
                        item.shiftInputContent = user.vipAddress.toString()
                    mMultiStyleItemList.add(item)
                    item.jumpListener = View.OnClickListener {
                        val dialog = AlertDialog.Builder(context!!)
                        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit,null)
                        if(item.shiftInputContent!="未设置")
                            dialogView.et_dialog.setText(item.shiftInputContent)
                        dialogView.et_dialog.setSelection(dialogView.et_dialog.text.length)
                        dialog.setTitle("家庭住址")
                            .setView(dialogView)
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定",DialogInterface.OnClickListener() { _, _ ->
                                val result = Observable.create<RequestBody> {
                                    //json.remove(key)
                                    //val imagePath = upImage(key)
                                    //var jsonObject= json.put(key,upImage(key))
                                    val jsonObject = userJson.put("vipAddress", dialogView.et_dialog.text)
                                    Log.i("json ,,", jsonObject.toString())
                                    val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                                    it.onNext(requestBody)
                                }.subscribe {
                                    val result =
                                        putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.upateDTO1).observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(
                                                {
                                                    //                                                        Toast.makeText(context,it.string(),Toast.LENGTH_SHORT).show()
                                                    if (JSONObject(it.string()).getInt("code") == 200) {
                                                        adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = dialogView.et_dialog.text.toString()
                                                        adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                                                    }
                                                },
                                                {
                                                    it.printStackTrace()
                                                }
                                            )
                                }
                            })
                            .create()
                            .show()
                    }
                }
                if(true){
                    val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"政治面貌","未设置")
                    if(user.politicsVisage!=null)
                        item.shiftInputContent = user.politicsVisage.toString()
                    mMultiStyleItemList.add(item)
                    item.jumpListener = View.OnClickListener {
                        val dialog = AlertDialog.Builder(context!!)
                        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit,null)
                        if(item.shiftInputContent!="未设置")
                            dialogView.et_dialog.setText(item.shiftInputContent)
                        dialogView.et_dialog.setSelection(dialogView.et_dialog.text.length)
                        dialog.setTitle("政治面貌")
                            .setView(dialogView)
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定",DialogInterface.OnClickListener() { _, _ ->
                                val result = Observable.create<RequestBody> {
                                    //json.remove(key)
                                    //val imagePath = upImage(key)
                                    //var jsonObject= json.put(key,upImage(key))
                                    val jsonObject = userJson.put("politicsVisage", dialogView.et_dialog.text)
                                    Log.i("json ,,", jsonObject.toString())
                                    val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                                    it.onNext(requestBody)
                                }.subscribe {
                                    val result =
                                        putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.upateDTO1).observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(
                                                {
                                                    //                                                        Toast.makeText(context,it.string(),Toast.LENGTH_SHORT).show()
                                                    if (JSONObject(it.string()).getInt("code") == 200) {
                                                        adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = dialogView.et_dialog.text.toString()
                                                        adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                                                    }
                                                },
                                                {
                                                    it.printStackTrace()
                                                }
                                            )
                                }
                            })
                            .create()
                            .show()
                    }
                }
                if(true){
                    val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"个人社保账号","未设置")
                    if(user.personSocialSecurityNumber!=null)
                        item.shiftInputContent = user.personSocialSecurityNumber.toString()
                    mMultiStyleItemList.add(item)
                    item.jumpListener = View.OnClickListener {
                        val dialog = AlertDialog.Builder(context!!)
                        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit,null)
                        if(item.shiftInputContent!="未设置")
                            dialogView.et_dialog.setText(item.shiftInputContent)
                        dialogView.et_dialog.setSelection(dialogView.et_dialog.text.length)
                        dialog.setTitle("个人社保账号")
                            .setView(dialogView)
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定",DialogInterface.OnClickListener() { _, _ ->
                                val result = Observable.create<RequestBody> {
                                    //json.remove(key)
                                    //val imagePath = upImage(key)
                                    //var jsonObject= json.put(key,upImage(key))
                                    val jsonObject = userJson.put("personSocialSecurityNumber", dialogView.et_dialog.text)
                                    Log.i("json ,,", jsonObject.toString())
                                    val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                                    it.onNext(requestBody)
                                }.subscribe {
                                    val result =
                                        putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.upateDTO1).observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(
                                                {
                                                    //                                                        Toast.makeText(context,it.string(),Toast.LENGTH_SHORT).show()
                                                    if (JSONObject(it.string()).getInt("code") == 200) {
                                                        adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = dialogView.et_dialog.text.toString()
                                                        adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                                                    }
                                                },
                                                {
                                                    it.printStackTrace()
                                                }
                                            )
                                }
                            })
                            .create()
                            .show()
                    }
                }
                if(true){
                    val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"个人公积金账号","未设置")
                    if(user.personAccumulationFundNumber!=null)
                        item.shiftInputContent = user.personAccumulationFundNumber.toString()
                    mMultiStyleItemList.add(item)
                    item.jumpListener = View.OnClickListener {
                        val dialog = AlertDialog.Builder(context!!)
                        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit,null)

                        if(item.shiftInputContent!="未设置")
                            dialogView.et_dialog.setText(item.shiftInputContent)
                        dialogView.et_dialog.setSelection(dialogView.et_dialog.text.length)
                        dialog.setTitle("个人公积金账号")
                            .setView(dialogView)
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定",DialogInterface.OnClickListener() { _, _ ->
                                val result = Observable.create<RequestBody> {
                                    //json.remove(key)
                                    //val imagePath = upImage(key)
                                    //var jsonObject= json.put(key,upImage(key))
                                    val jsonObject = userJson.put("personAccumulationFundNumber", dialogView.et_dialog.text)
                                    Log.i("json ,,", jsonObject.toString())
                                    val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                                    it.onNext(requestBody)
                                }.subscribe {
                                    val result =
                                        putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.upateDTO1).observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(
                                                {
                                                    //                                                        Toast.makeText(context,it.string(),Toast.LENGTH_SHORT).show()
                                                    if (JSONObject(it.string()).getInt("code") == 200) {
                                                        adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = dialogView.et_dialog.text.toString()
                                                        adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                                                    }
                                                },
                                                {
                                                    it.printStackTrace()
                                                }
                                            )
                                }
                            })
                            .create()
                            .show()
                    }
                }
                item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "学历信息", false)
                item.jumpListener = View.OnClickListener {
                    FragmentHelper.switchFragment(activity!!,EducationInformationFragment(),R.id.frame_my_information,"")
                }
                mMultiStyleItemList.add(item)

                item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "银行卡信息", false)
                item.jumpListener = View.OnClickListener {
                    FragmentHelper.switchFragment(activity!!,BankCardInformationFragment(),R.id.frame_my_information,"")
                }
                mMultiStyleItemList.add(item)

//                item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "合同信息", false)
//                item.jumpListener = View.OnClickListener {
//                    (activity as MyInformationActivity).switchFragment(ContractInformationFragment(), "")
//                }
//                mMultiStyleItemList.add(item)

                item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "家庭信息", false)
                item.jumpListener = View.OnClickListener {
                    FragmentHelper.switchFragment(activity!!,FamilyInformationFragment(),R.id.frame_my_information,"")
                }
                mMultiStyleItemList.add(item)
                item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "个人材料", false)
                item.jumpListener = View.OnClickListener {
                    FragmentHelper.switchFragment(activity!!,PersonalMaterialsFragment(),R.id.frame_my_information,"MyInformation")
                }
                mMultiStyleItemList.add(item)
                item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "紧急联系人", false)
                item.jumpListener = View.OnClickListener {
                    FragmentHelper.switchFragment(activity!!,EmergencyContactFragment(),R.id.frame_my_information,"EmergencyContact")
                }
                mMultiStyleItemList.add(item)
                adapter = RecyclerviewAdapter(mMultiStyleItemList)
                mView.rv_my_information_content.adapter = adapter
                mView.rv_my_information_content.layoutManager = LinearLayoutManager(context)
    }
    fun update(email:String){
        adapter!!.mData[2].shiftInputContent = email
        adapter!!.notifyItemRangeChanged(2,1)
    }
}

