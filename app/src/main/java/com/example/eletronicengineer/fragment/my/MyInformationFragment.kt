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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyInformationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.GlideLoader
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lcw.library.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.dialog_datepicker.view.*
import kotlinx.android.synthetic.main.dialog_edit.view.*
import kotlinx.android.synthetic.main.dialog_recyclerview.view.*
import kotlinx.android.synthetic.main.dialog_upload.view.*
import kotlinx.android.synthetic.main.fragment_my_information.view.*
import java.util.*
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
        val mMultiStyleItemList: MutableList<MultiStyleItem> = ArrayList()
        var item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "头像", "", "x")
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
        item = MultiStyleItem(MultiStyleItem.Options.HINT, "部门信息")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "公司", "湖南宇畅有限公司")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "部门", "董事长室")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "职务", "董事长")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.HINT, "账号信息")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "手机", "152****5626")
        item.jumpListener = View.OnClickListener {
            (activity as MyInformationActivity).switchFragment(BindPhoneFragment(), "")
        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "微信", "换绑")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "QQ", "换绑")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "邮箱", "换绑")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "修改密码", "去修改")
        item.jumpListener = View.OnClickListener {
            (activity as MyInformationActivity).switchFragment(ChangePasswordFragment(), "")
        }
        mMultiStyleItemList.add(item)

        item = MultiStyleItem(MultiStyleItem.Options.HINT, "个人信息")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "身份证姓名", "未认证")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "证件号码", "未认证")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "电企通帐号", "11111111")
        mMultiStyleItemList.add(item)

        if (true) {
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "性别", "未设置")
            mMultiStyleItemList.add(item)
            item.jumpListener = View.OnClickListener {
                var checkedItem = 0
                val items = arrayListOf("男", "女")
                val dialog = AlertDialog.Builder(context!!)
                    .setTitle("性别")
                    .setSingleChoiceItems(items.toTypedArray(), 0, object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            checkedItem = p1
                        }
                    })
                    .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = items[checkedItem]
                            adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                        }
                    })
                    .create()
                dialog.show()
            }
        }

        if (true) {
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "出生日期", "未设置")
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
                        item.shiftInputContent =
                            "${mDialogView.date_picker.year}-${mDialogView.date_picker.month + 1}-${mDialogView.date_picker.dayOfMonth}"
                        mView.rv_my_information_content.adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                    })
                    .create()
                    .show()
            }
        }
        if (true) {
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "血型", "未设置")
            mMultiStyleItemList.add(item)
            item.jumpListener = View.OnClickListener {
                val items = arrayListOf("A型", "B型", "AB型", "O型")
                var checkedItem = 0
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("血型")
                builder.setSingleChoiceItems(items.toTypedArray(), 0, object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        checkedItem = p1
                    }
                })

                builder.setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                    adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = items[checkedItem]
                    adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                })
                val dialog = builder.create()
                dialog.show()
            }
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"民族","未设置")
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
                        adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = dialogView.et_dialog.text.toString()
                        adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                    })
                    .create()
                    .show()
            }
        }
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT, "联系电话", "152****5626")
        mMultiStyleItemList.add(item)
        if (true) {
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "婚姻状况", "未设置")
            item.jumpListener = View.OnClickListener {
                val items = arrayListOf("已婚", "未婚")
                var checkedItem = 0
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("婚姻状况")
                builder.setSingleChoiceItems(items.toTypedArray(), 0, object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        checkedItem = p1
                    }
                })

                builder.setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                    adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = items[checkedItem]
                    adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                })
                val dialog = builder.create()
                dialog.show()
            }
            mMultiStyleItemList.add(item)
        }

        if (true) {
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "首次参加工作时间", "未设置")
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
                        item.shiftInputContent =
                            "${mDialogView.date_picker.year}-${mDialogView.date_picker.month + 1}-${mDialogView.date_picker.dayOfMonth}"
                        mView.rv_my_information_content.adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                    })
                    .create()
                    .show()
            }
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "户籍类型", "未设置")
            item.jumpListener = View.OnClickListener {
                val items = arrayListOf("本地城镇", "本地农村","外地城镇(省内)", "外地农村(省内)","外地城镇(省外)", "外地农村(省外)","其他")
                var checkedItem = 0
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("户籍类型")
                builder.setSingleChoiceItems(items.toTypedArray(), 0, object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        checkedItem = p1
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
                                adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = dialogView.et_dialog.text.toString()
                                adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
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
                        adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = dialogView.et_dialog.text.toString()
                        adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                    })
                    .create()
                    .show()
            }
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"政治面貌","未设置")
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
                        adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = dialogView.et_dialog.text.toString()
                        adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                    })
                    .create()
                    .show()
            }
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"个人社保账号","未设置")
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
                        adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = dialogView.et_dialog.text.toString()
                        adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                    })
                    .create()
                    .show()
            }
        }
        if(true){
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"个人公积金账号","未设置")
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
                        adapter!!.mData[mMultiStyleItemList.indexOf(item)].shiftInputContent = dialogView.et_dialog.text.toString()
                        adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                    })
                    .create()
                    .show()
            }
        }
        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "学历信息", false)
        item.jumpListener = View.OnClickListener {
            (activity as MyInformationActivity).switchFragment(EducationInformationFragment(),"")
        }
        mMultiStyleItemList.add(item)

        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "银行卡信息", false)
        item.jumpListener = View.OnClickListener {
            (activity as MyInformationActivity).switchFragment(BankCardInformationFragment(), "")
        }
        mMultiStyleItemList.add(item)

        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "合同信息", false)
        item.jumpListener = View.OnClickListener {
            (activity as MyInformationActivity).switchFragment(ContractInformationFragment(), "")
        }
        mMultiStyleItemList.add(item)

        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "家庭信息", false)
        item.jumpListener = View.OnClickListener {
            (activity as MyInformationActivity).switchFragment(FamilyInformationFragment(), "")
        }
        mMultiStyleItemList.add(item)

        item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "紧急联系人", false)
        item.jumpListener = View.OnClickListener {
            (activity as MyInformationActivity).switchFragment(EmergencyContactFragment(),"EmergencyContact")
        }
        mMultiStyleItemList.add(item)

        adapter = RecyclerviewAdapter(mMultiStyleItemList)
        (mView.rv_my_information_content.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        mView.rv_my_information_content.adapter = adapter
        mView.rv_my_information_content.layoutManager = LinearLayoutManager(context)
    }

    fun refresh(mImagePath: String) {
        adapter!!.mData[0].shiftInputPicture = mImagePath
        adapter!!.notifyItemChanged(0)
    }
}

