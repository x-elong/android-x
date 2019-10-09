package com.example.eletronicengineer.fragment.my

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.GlideLoader
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lcw.library.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.dialog_upload.view.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class EditProfileFragment :Fragment(){
    lateinit var mView: View
    val glideLoader = GlideLoader()
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    var adapter = RecyclerviewAdapter(mMultiStyleItemList)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_edit_profile,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mMultiStyleItemList.clear()
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.TITLE,"编辑个人资料","0"))
        mMultiStyleItemList[mMultiStyleItemList.lastIndex].backListener = View.OnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"头像","","x"))
        mMultiStyleItemList[mMultiStyleItemList.lastIndex].jumpListener = View.OnClickListener {
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
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SELECT_DIALOG,arrayListOf("男","女"),"性别"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"出生日期",false))
        mMultiStyleItemList[3].jumpListener = View.OnClickListener {
            var result = ""
            val cale1 = Calendar.getInstance()
            DatePickerDialog(
                context,
                DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    result += "${year}-${month + 1}-${dayOfMonth}"
                    adapter.mData[3].shiftInputContent = result
                    adapter.notifyItemChanged(3)
//                        adapter.onBindViewHolder(adapter.VHList.get(j), j)
                },
                cale1.get(Calendar.YEAR),
                cale1.get(Calendar.MONTH),
                cale1.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        val selectOption1Items:MutableList<String> =ArrayList()
        val selectOption2Items:MutableList<MutableList<String>> =ArrayList()
        val selectOption3Items:MutableList<MutableList<MutableList<String>>> =ArrayList()
        val resultBuilder= StringBuilder()
        val bf= BufferedReader(InputStreamReader(context!!.assets.open("pca.json")))
        try {
            var line=bf.readLine()
            while (line!=null)
            {
                resultBuilder.append(line)
                line=bf.readLine()
            }
        }
        catch (io: IOException)
        {
            io.printStackTrace()
        }
        val json= JSONObject(resultBuilder.toString())
        val keys:Iterator<String> = json.keys()
        while (keys.hasNext()){
            val key = keys.next()
            selectOption1Items.add(key)
            val js = json.getJSONObject(key)
            val jskeys = js.keys()
            selectOption2Items.add(ArrayList())
            selectOption3Items.add(ArrayList())
            while (jskeys.hasNext()){
                val jskey = jskeys.next()
                selectOption2Items[selectOption2Items.size-1].add(jskey)
                selectOption3Items[selectOption3Items.size-1].add(ArrayList())
                var valueArray = js.getJSONArray(jskey)
                for (j in 0 until valueArray.length()){
                    selectOption3Items[selectOption3Items.size-1][selectOption3Items[selectOption3Items.size-1].size-1].add(valueArray[j].toString())
                }
            }
        }
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG,selectOption1Items,selectOption2Items,selectOption3Items,"家庭住址"))
        adapter=RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_edit_profile.adapter = adapter
        mView.rv_edit_profile.layoutManager = LinearLayoutManager(context)
    }
    fun refresh(mImagePath:String){
        adapter.mData[1].shiftInputPicture=mImagePath
        adapter.notifyItemChanged(1)
    }
}