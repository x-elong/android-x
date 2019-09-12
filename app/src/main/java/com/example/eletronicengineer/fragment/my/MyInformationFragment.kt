package com.example.eletronicengineer.fragment.my

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyInformationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.GlideLoader
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lcw.library.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.dialog_upload.view.*
import kotlinx.android.synthetic.main.fragment_my_information.view.*
import java.util.*
import kotlin.collections.ArrayList


class MyInformationFragment : Fragment(){
    val glideLoader = GlideLoader()
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    var adapter = RecyclerviewAdapter(mMultiStyleItemList)
    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_my_information,container,false)
        initFragment()
        return mView
    }
    private fun initFragment() {
        mView.tv_my_information_back.setOnClickListener {
            activity!!.finish()
        }
        mMultiStyleItemList.clear()
        var item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"头像","","x")
        item.jumpListener = View.OnClickListener {
            val dialog = BottomSheetDialog(context!!)
            val dialogView = LayoutInflater.from(context!!).inflate(R.layout.dialog_upload,null)
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
        item = MultiStyleItem(MultiStyleItem.Options.HINT,"部门信息")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"公司","湖南宇畅有限公司")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"部门","董事长室")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"职务","董事长")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.HINT,"账号信息")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"手机","152****5626")
        item.jumpListener =View.OnClickListener {
            (activity as MyInformationActivity).switchFragment(BindPhoneFragment(),"")
        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"微信","换绑")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"QQ","换绑")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"邮箱","换绑")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"修改密码","去修改")
        item.jumpListener = View.OnClickListener {
            (activity as MyInformationActivity).switchFragment(ChangePasswordFragment(),"")
        }
        mMultiStyleItemList.add(item)

        item = MultiStyleItem(MultiStyleItem.Options.HINT,"个人信息")
        mMultiStyleItemList.add(item)

        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT,"电企通帐号","11111111")
        mMultiStyleItemList.add(item)

        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"性别",false)
            mMultiStyleItemList.add(item)
            item.jumpListener = View.OnClickListener {
                var checkedItem = 0
                val items = arrayListOf("男","女")
                val dialog = AlertDialog.Builder(context!!)
                    .setTitle("性别")
                    .setSingleChoiceItems(items.toTypedArray(),0,object :DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            checkedItem = p1
                        }
                    })
                    .setPositiveButton("确定",object :DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            adapter.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = items[checkedItem]
                            adapter.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                        }
                    })
                    .create()
                dialog.show()
            }
        }

        if (true) {
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "出生日期", false)
            mMultiStyleItemList.add(item)
            item.jumpListener = View.OnClickListener {
                val position = mMultiStyleItemList.indexOf(item)
                val cale1 = Calendar.getInstance()
                DatePickerDialog(
                    context,
                    android.R.style.Theme_Material_Dialog_Alert,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        Log.i("position", position.toString())
                        val result = "${year}-${month + 1}-${dayOfMonth}"
                        adapter.mData[position].shiftInputContent = result
                        adapter.notifyItemChanged(position)
                    },
                    cale1.get(Calendar.YEAR),
                    cale1.get(Calendar.MONTH),
                    cale1.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
        if(true){
            item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"血型",false)
            mMultiStyleItemList.add(item)
            item.jumpListener = View.OnClickListener {
                val items = arrayListOf("A型","B型","AB型","O型")
                var checkedItem = 0
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("血型")
            builder.setSingleChoiceItems(items.toTypedArray(),0,object :DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    checkedItem=p1
                }
            })

                builder.setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                    adapter.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent=items[checkedItem]
                    adapter.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                })
                val dialog = builder.create()
                dialog.show()
            }
        }
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"紧急联系人",false)
        item.jumpListener = View.OnClickListener {
//            activity as My
        }

        adapter = RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_my_information_content.adapter = adapter
        mView.rv_my_information_content.layoutManager= LinearLayoutManager(context)
    }
    fun refresh(mImagePath:String){
        adapter.mData[0].shiftInputPicture=mImagePath
        adapter.notifyItemChanged(0)
    }
}

