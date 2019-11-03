package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.aninterface.CheckBoxStyle
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.deleteUrgentPeople
import com.example.eletronicengineer.utils.putSimpleMessage
import com.example.eletronicengineer.utils.startSendMessage
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_edit_emergency_contact.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class EditEmergencyContactFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):EditEmergencyContactFragment{
            val editEmergencyContactFragment = EditEmergencyContactFragment()
            editEmergencyContactFragment.arguments = args
            return editEmergencyContactFragment
        }
    }
    lateinit var json:JSONObject
    lateinit var mView: View
    var adapter = RecyclerviewAdapter(ArrayList())
    var style = -1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_edit_emergency_contact,container,false)
        style = arguments!!.getInt("style")
        if(style==0){
            mView.btn_delete_emergency.visibility=View.GONE
            mView.tv_edit_emergency_contact_title.setText("添加紧急联系人")
        }

        mView.tv_edit_emergency_contact_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.tv_edit_emergency_ok.setOnClickListener {
            val networkAdapter = NetworkAdapter(adapter.mData,context!!)
            if(networkAdapter.check()){
                if(style==1){
                    val jsonObject = json.put("urgentPeopleName",adapter.mData[0].inputSingleContent)
                        .put("relation",adapter.mData[1].inputSingleContent)
                        .put("phone",adapter.mData[2].inputSingleContent)
                        .put("urgentPeopleId",adapter.mData[3].inputSingleContent)
                        .put("address",adapter.mData[4].inputSingleContent)
                        .put("additonalExplain",adapter.mData[5].textAreaContent)
                    val result = NetworkAdapter().putSimpleMessage(jsonObject,UnSerializeDataBase.BasePath+Constants.HttpUrlPath.My.updateUrgentPeople)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                            if(JSONObject(it.string()).getInt("code")==200){
                                val toast = Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER,0,0)
                                toast.show()
                                mView.tv_edit_emergency_contact_back.callOnClick()
                            }else{
                                val toast = Toast.makeText(context,"修改失败",Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER,0,0)
                                toast.show()
                            }
                        },{
                            it.printStackTrace()
                        })
                }else{
                    val jsonObject = JSONObject().put("urgentPeopleName",adapter.mData[0].inputSingleContent)
                        .put("relation",adapter.mData[1].inputSingleContent)
                        .put("phone",adapter.mData[2].inputSingleContent)
                        .put("urgentPeopleId",adapter.mData[3].inputSingleContent)
                        .put("address",adapter.mData[4].inputSingleContent)
                        .put("additonalExplain",adapter.mData[5].textAreaContent)
                        val result = NetworkAdapter().startSendMessage(jsonObject,UnSerializeDataBase.BasePath+Constants.HttpUrlPath.My.insertUrgentPeople)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                                if(JSONObject(it.string()).getInt("code")==200){
                                    val toast = Toast.makeText(context,"添加成功",Toast.LENGTH_SHORT)
                                    toast.setGravity(Gravity.CENTER,0,0)
                                    toast.show()
                                    mView.tv_edit_emergency_contact_back.callOnClick()
                                }else{
                                    val toast = Toast.makeText(context,"添加失败",Toast.LENGTH_SHORT)
                                    toast.setGravity(Gravity.CENTER,0,0)
                                    toast.show()
                                }
                            },{
                                it.printStackTrace()
                            })
                    }
            }

        }
        mView.btn_delete_emergency.setOnClickListener {
            val result = deleteUrgentPeople(json.getString("id")).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe( {
                    if(JSONObject(it.string()).getInt("code")==200){
                        val toast = Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER,0,0)
                        toast.show()
                        mView.tv_edit_emergency_contact_back.callOnClick()
                    }else{
                        val toast = Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER,0,0)
                        toast.show()
                    }
                },{
                    it.printStackTrace()
                })
        }
        initFragment()
        return mView
    }
    private fun initFragment() {
        val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"姓名"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"关系"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"电话"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"身份证号码"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"地址"))
        mMultiStyleItemList.add(MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_TEXTAREA,"备注","",true))
        if(style==1){
            json = JSONObject(arguments!!.getString("urgentPeople"))
            mMultiStyleItemList[0].inputSingleContent = json.getString("urgentPeopleName")
            mMultiStyleItemList[1].inputSingleContent = json.getString("relation")
            mMultiStyleItemList[2].inputSingleContent = json.getString("phone")
            mMultiStyleItemList[3].inputSingleContent = json.getString("urgentPeopleId")
            mMultiStyleItemList[4].inputSingleContent = json.getString("address")
            if(!json.isNull("additonalExplain"))
                mMultiStyleItemList[5].textAreaContent = json.getString("additonalExplain")
        }
        adapter= RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_edit_emergency_contact_content.adapter = adapter
        mView.rv_edit_emergency_contact_content.layoutManager = LinearLayoutManager(context)
    }
}