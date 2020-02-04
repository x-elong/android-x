package com.example.eletronicengineer.fragment.my

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyInformationActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.deleteEducationBackground
import com.example.eletronicengineer.utils.putSimpleMessage
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_datepicker.view.*
import kotlinx.android.synthetic.main.fragment_education_information.view.*
import kotlinx.android.synthetic.main.fragment_my_information.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat

class AddEducationInformationFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):AddEducationInformationFragment{
            val addEducationInformationFragment = AddEducationInformationFragment()
            addEducationInformationFragment.arguments = args
            return addEducationInformationFragment
        }
    }
    lateinit var mView: View
    var type = 0
    lateinit var json:JSONObject
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_education_information,container,false)
        type = arguments!!.getInt("type")
        initFragment()
        return mView

    }

    private fun initFragment() {
        val mMultiStyleItemList = ArrayList<MultiStyleItem>()
        var item = MultiStyleItem(MultiStyleItem.Options.TITLE,"学历信息","3")

        item.backListener = View.OnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        item.tvSelectListener = View.OnClickListener {
            val networkAdapter = NetworkAdapter(mMultiStyleItemList,context!!)
            if(networkAdapter.check()){
                if(type==0){
                    val result = Observable.create<RequestBody>{
                        val date = java.sql.Date.valueOf(mMultiStyleItemList[3].shiftInputContent)
                        val jsonObject = JSONObject().put("educationBackground",mMultiStyleItemList[1].inputSingleContent)
                            .put("graduationAcademy",mMultiStyleItemList[2].inputSingleContent)
                            .put("graduationTime",date)
                            .put("major",mMultiStyleItemList[4].inputSingleContent)
                        val requestBody =RequestBody.create(MediaType.parse("application/json"),jsonObject.toString())
                        it.onNext(requestBody)
                    }.subscribe {
                        val result = startSendMessage(it,UnSerializeDataBase.mineBasePath+Constants.HttpUrlPath.My.insertEducationBackground)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                val json = JSONObject(it.string())
                                if(json.getInt("code") == 200){
                                    ToastHelper.mToast(mView.context,"添加成功")
                                    activity!!.supportFragmentManager.popBackStackImmediate()
                                }
                            },{
                                it.printStackTrace()
                            })
                    }
                }else{
                    val date = java.sql.Date.valueOf(mMultiStyleItemList[3].shiftInputContent)
                    val result = Observable.create<RequestBody> {

                        val jsonObject = JSONObject().put("educationBackground",mMultiStyleItemList[1].inputSingleContent)
                            .put("graduationAcademy",mMultiStyleItemList[2].inputSingleContent)
                            .put("graduationTime",date)
                            .put("major",mMultiStyleItemList[4].inputSingleContent)
                            .put("id",json.getString("id"))
                        Log.i("json ,,", jsonObject.toString())
                        val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                        it.onNext(requestBody)
                    }.subscribe {
                        val result =
                            putSimpleMessage(it, UnSerializeDataBase.mineBasePath + Constants.HttpUrlPath.My.updateEducationBackground).observeOn(
                                AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(
                                    {
                                        //                                                        Toast.makeText(context,it.string(),Toast.LENGTH_SHORT).show()
                                        if (JSONObject(it.string()).getInt("code") == 200) {
                                            Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show()
                                            activity!!.supportFragmentManager.popBackStackImmediate()
                                        }
                                    },
                                    {
                                        it.printStackTrace()
                                    }
                                )
                    }
                }
            }

        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"学历","")
        item.necessary = true
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"毕业院校","")
        item.necessary = true
        mMultiStyleItemList.add(item)
        if(true) {
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "毕业时间", "")
            item.necessary = true
            mMultiStyleItemList.add(item)
            item.jumpListener = View.OnClickListener {
                val dialog = AlertDialog.Builder(context!!)
                val mDialogView = layoutInflater.inflate(R.layout.dialog_datepicker, null)
                if (item.shiftInputContent != "") {
                    val s = item.shiftInputContent.split("-")
                    mDialogView.date_picker.init(s[0].toInt(), s[1].toInt() - 1, s[2].toInt(), null)
                }
                dialog.setTitle("毕业时间")
                    .setView(mDialogView)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                        item.shiftInputContent =
                            "${mDialogView.date_picker.year}-${mDialogView.date_picker.month + 1}-${mDialogView.date_picker.dayOfMonth}"
                        mView.rv_education_information_content.adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                    })
                    .create()
                    .show()
            }
        }
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"所学专业","")
        item.necessary = true
        mMultiStyleItemList.add(item)
        if(type==1){
            mView.btn_delete_education.visibility = View.VISIBLE
            mView.btn_delete_education.setOnClickListener {
                val result = deleteEducationBackground(
                    json.getString("id")
                ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe( {
                        if (JSONObject(it.string()).getInt("code") == 200){
                            val toast = Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.CENTER,0,0)
                            toast.show()
                            activity!!.supportFragmentManager.popBackStackImmediate()
                        }
                    },{
                        it.printStackTrace()
                    })
            }
            json = JSONObject(arguments!!.getString("EducationBackground"))
            mMultiStyleItemList[1].inputSingleContent = json.getString("educationBackground")
            mMultiStyleItemList[2].inputSingleContent = json.getString("graduationAcademy")
            mMultiStyleItemList[3].shiftInputContent = SimpleDateFormat("yyyy-MM-dd").format(json.getLong("graduationTime"))
            mMultiStyleItemList[4].inputSingleContent = json.getString("major")
        }
        (mView.rv_education_information_content.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        mView.rv_education_information_content.adapter= RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_education_information_content.layoutManager= LinearLayoutManager(context)

    }
}