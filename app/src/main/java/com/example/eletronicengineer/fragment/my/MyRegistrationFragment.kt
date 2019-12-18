package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyRegistrationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter.Companion.MESSAGE_SELECT_OK
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_contract_information.view.*
import kotlinx.android.synthetic.main.fragment_my_registration.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class MyRegistrationFragment :Fragment(){

    val mHandler = Handler(Handler.Callback {
        when(it.what)
        {
            MESSAGE_SELECT_OK->
            {
                page = 1
                pageCount = 1
                adapter.mData = ArrayList()
                adapter.notifyDataSetChanged()
                val selectContent=it.data.getString("selectContent")
                tvMode = selectContent
                mView.tv_mode_content.text=selectContent
                initData()
//                mView.sp_demand_moder.
                false
            }
            else->
            {
                false
            }
        }
    })
    lateinit var mView: View
    val mMultiStyleItemList:MutableList<MultiStyleItem> = ArrayList()
    var page = 1
    var pageCount = 1
    var tvMode = "需求 需求个人"
    var adapter = RecyclerviewAdapter(ArrayList())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_my_registration,container,false)
        initFragment()
        initOnScrollListener()
        return mView
    }
    private fun initOnScrollListener() {
        mView.rv_my_registration_content.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                if(lastCompletelyVisibleItemPosition == layoutManager.itemCount-1 && page<=pageCount){
                    Log.i("page","$page")
                    page++
                    Toast.makeText(mView.context,"滑动到底了", Toast.LENGTH_SHORT).show()
                    initData()
                }
            }
        })
    }

    private fun initFragment() {
        if(tvMode!=""){
            mView.tv_mode_content.text = tvMode
            page = 1
            pageCount = 1
        }
        mMultiStyleItemList.clear()
        adapter.mData = mMultiStyleItemList
        mView.tv_my_registration_back.setOnClickListener {
            activity!!.finish()
        }
        mView.view_sp.setOnClickListener{
            val Option1Items = listOf("需求")
            val Option2Items:List<List<String>> = listOf(listOf("需求个人","需求团队","需求租赁","需求三方"))
            val selectDialog= CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,context!!,mHandler,Option1Items,Option2Items).multiDialog
            selectDialog.show()
        }
        mView.rv_my_registration_content.adapter=adapter
        mView.rv_my_registration_content.layoutManager=LinearLayoutManager(context)
        initData()
    }

    private fun initData() {
        when(mView.tv_mode_content.text){
            "需求 需求个人"->{
                getDataDemandIndividual()
            }
            "需求 需求团队"->{
                getDataDemandGroup()
            }
            "需求 需求租赁"->{
                getDataDemandLease()
            }
            "需求 需求三方"->{
                getDataDemandTripartite()
            }
            else->{
                getDataRegistration()
            }
        }
    }
    private fun getDataRegistration() {
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("page",page).put("pageSize",4)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it, UnSerializeDataBase.dmsBasePath+Constants.HttpUrlPath.My.getRegistration)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        result="当前数据获取成功"
                        val json = jsonObject.getJSONObject("message")
                        pageCount = json.getInt("pageCount")
                        val jsonArray = json.getJSONArray("data")
                        val size = adapter.mData.size
                        val data = adapter.mData.toMutableList()
                        for (j in 0 until jsonArray.length()){
                            val js = jsonArray.getJSONObject(j)
                            val item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"需求三方",js.getString("requirementVariety"),"")
                            item.jumpListener=View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putString("",js.toString())
                                bundle.putInt("type",Constants.FragmentType.DEMAND_TRIPARTITE_TYPE.ordinal)
                                FragmentHelper.switchFragment(activity!!,MyRegistrationMoreFragment.newInstance(bundle),R.id.frame_my_registration,"")
                            }
                            data.add(item)
                        }
                        adapter.mData = data
                        adapter.notifyItemRangeInserted(size,adapter.mData.size-size)
                    }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
                        result="当前数据为空"
                        pageCount = 0
                    }
                    Toast.makeText(context,result,Toast.LENGTH_SHORT).show()
                },{
                    it.printStackTrace()
                })
        }
    }
    private fun getDataDemandIndividual() {
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("page",page).put("pageSize",4)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,UnSerializeDataBase.dmsBasePath+Constants.HttpUrlPath.My.getPersonRequirement)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        result="当前数据获取成功"
                        val json = jsonObject.getJSONObject("message")
                        pageCount = json.getInt("pageCount")
                        val jsonArray = json.getJSONArray("data")
                        val size = adapter.mData.size
                        val data = adapter.mData.toMutableList()
                        for (j in 0 until jsonArray.length()){
                            val js = jsonArray.getJSONObject(j)
                            val item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"需求个人",js.getString("requirementVariety"),"")
                            item.jumpListener=View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putString("demandIndividual",js.toString())
                                bundle.putInt("type",Constants.FragmentType.DEMAND_INDIVIDUAL_TYPE.ordinal)
                                FragmentHelper.switchFragment(activity!!,MyRegistrationMoreFragment.newInstance(bundle),R.id.frame_my_registration,"")
                            }
                            data.add(item)
                        }
                        adapter.mData = data
                        adapter.notifyItemRangeInserted(size,adapter.mData.size-size)
                    }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
                        result="当前数据为空"
                        pageCount = 0
                    }
                    Toast.makeText(context,result,Toast.LENGTH_SHORT).show()
                },{
                    it.printStackTrace()
                })
        }
    }
    private fun getDataDemandGroup() {
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("page",page).put("pageSize",4)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,UnSerializeDataBase.dmsBasePath+Constants.HttpUrlPath.My.getRequirementTeam)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        result="当前数据获取成功"
                        val json = jsonObject.getJSONObject("message")
                        pageCount = json.getInt("pageCount")
                        val jsonArray = json.getJSONArray("data")
                        val size = adapter.mData.size
                        val data = adapter.mData.toMutableList()
                        for (j in 0 until jsonArray.length()){
                            val js = jsonArray.getJSONObject(j)
                            val item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"需求团队",js.getJSONObject("requirementTeamLoggingCheck").getString("type"),"")
                            item.jumpListener=View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putString("demandGroup",js.toString())
                                bundle.putInt("type",Constants.FragmentType.DEMAND_GROUP_TYPE.ordinal)
                                FragmentHelper.switchFragment(activity!!,MyRegistrationMoreFragment.newInstance(bundle),R.id.frame_my_registration,"")
                            }
                            data.add(item)
                        }
                        adapter.mData = data
                        adapter.notifyItemRangeInserted(size,adapter.mData.size-size)
                    }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
                        result="当前数据为空"
                        pageCount = 0
                    }
                    Toast.makeText(context,result,Toast.LENGTH_SHORT).show()
                },{
                    it.printStackTrace()
                })
        }
    }
    private fun getDataDemandLease() {
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("page",page).put("pageSize",4)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,UnSerializeDataBase.dmsBasePath+Constants.HttpUrlPath.My.getLease)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        result="当前数据获取成功"

                        val json = jsonObject.getJSONObject("message")
                        pageCount = json.getInt("pageCount")
                        val jsonArray = json.getJSONArray("data")
                        val size = adapter.mData.size
                        Log.i("size is :",size.toString())
                        val data = adapter.mData.toMutableList()
                        for (j in 0 until jsonArray.length()){
                            val js = jsonArray.getJSONObject(j)
                            val item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"需求租赁",js.getJSONObject("leaseLoggingCheck").getString("type"),"")
                            item.jumpListener=View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putString("demandLease",js.toString())
                                bundle.putInt("type",Constants.FragmentType.DEMAND_LEASE_TYPE.ordinal)
                                FragmentHelper.switchFragment(activity!!,MyRegistrationMoreFragment.newInstance(bundle),R.id.frame_my_registration,"")
                            }
                            data.add(item)
                        }
                        adapter.mData = data
                        adapter.notifyItemRangeInserted(size,adapter.mData.size-size)
                        Log.i("adapter.mData.size is :",adapter.mData.size.toString())
                    }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
                        result="当前数据为空"
                        pageCount = 0
                    }
                    Toast.makeText(context,result,Toast.LENGTH_SHORT).show()
                },{
                    it.printStackTrace()
                })
        }
    }
    private fun getDataDemandTripartite(){
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("page",page).put("pageSize",4)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,UnSerializeDataBase.dmsBasePath+Constants.HttpUrlPath.My.getRequirementThird)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        result="当前数据获取成功"

                        val json = jsonObject.getJSONObject("message")
                        pageCount = json.getInt("pageCount")
                        val jsonArray = json.getJSONArray("data")
                        val size = adapter.mData.size
                        val data = adapter.mData.toMutableList()
                        for (j in 0 until jsonArray.length()){
                            val js = jsonArray.getJSONObject(j)
                            val item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"需求三方",js.getString("requirementVariety"),"")
                            item.jumpListener=View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putString("demandTripartite",js.toString())
                                bundle.putInt("type",Constants.FragmentType.DEMAND_TRIPARTITE_TYPE.ordinal)
                                FragmentHelper.switchFragment(activity!!,MyRegistrationMoreFragment.newInstance(bundle),R.id.frame_my_registration,"")
                            }
                            data.add(item)
                        }
                        adapter.mData = data
                        adapter.notifyItemRangeInserted(size,adapter.mData.size-size)
                    }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
                        result="当前数据为空"
                        pageCount = 0
                    }
                    Toast.makeText(context,result,Toast.LENGTH_SHORT).show()
                },{
                    it.printStackTrace()
                })
        }
    }
}