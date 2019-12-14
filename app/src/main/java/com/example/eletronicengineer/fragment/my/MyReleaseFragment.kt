package com.example.eletronicengineer.fragment.my

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.DemandDisplayActivity
import com.example.eletronicengineer.activity.MyReleaseActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter.Companion.MESSAGE_SELECT_OK
import com.example.eletronicengineer.custom.CustomDialog
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.fragment.retailstore.SupplyJobMoreFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_my_release.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class MyReleaseFragment :Fragment(){
    val mHandler = Handler(Handler.Callback {
        when(it.what)
        {
            MESSAGE_SELECT_OK->
            {
                page = 1
                pageCount = 1
                adapter = RecyclerviewAdapter(ArrayList())
                mView.rv_my_release_content.adapter=adapter
                mView.rv_my_release_content.layoutManager=LinearLayoutManager(context)
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
    lateinit var mView:View
    var page = 1
    var pageCount = 1
    lateinit var adapter:RecyclerviewAdapter
    var tvMode = "需求 需求个人"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_my_release,container,false)
        initFragment()
        initOnScrollListener()
        return mView
    }
    private fun initOnScrollListener() {
        mView.rv_my_release_content.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                if(lastCompletelyVisibleItemPosition == layoutManager.itemCount-1 && page<=pageCount){
                    Log.i("page","$page")
                    Toast.makeText(mView.context,"滑动到底了", Toast.LENGTH_SHORT).show()
                    initData()
                }
            }
        })
    }
    private fun initFragment() {
        adapter = RecyclerviewAdapter(ArrayList())
        if(tvMode!=""){
            mView.tv_mode_content.text = tvMode
            page = 1
            pageCount = 1
        }
        mView.tv_my_release_back.setOnClickListener {
            activity!!.finish()
        }
        mView.view_sp.setOnClickListener{
            val Option1Items = listOf("需求","供应")
            val Option2Items:List<List<String>> = listOf(listOf("需求个人","需求团队","需求租赁","需求三方"), listOf("个人劳务","团队服务","租赁服务","三方服务"))
            val selectDialog= CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,context!!,mHandler,Option1Items,Option2Items).multiDialog
            selectDialog.show()
        }
        //        (mView.rv_my_release_content.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        mView.rv_my_release_content.adapter=adapter
        mView.rv_my_release_content.layoutManager=LinearLayoutManager(context)
        initData()

    }
    fun initData(){
        when(tvMode){
            "需求 需求团队"->{
                getDataDemandGroup()
            }
            "需求 需求租赁"->{
                getDataDemandLease()
            }
            "需求 需求三方"->{
                getDataDemandTripartite()
            }
            "供应 个人劳务"->{
                getDataPersonalIssue()
            }
            "供应 团队服务"->{
                getDataTeamService()
            }
            "供应 租赁服务"->{
                getDataLeaseService()
            }
            "供应 三方服务"->{
                getDataThridService()
            }
            else->{
                getDataDemandIndividual()
            }
        }
    }
    private fun getDataDemandIndividual() {
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("page",page).put("number",4)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,UnSerializeDataBase.dmsBasePath+Constants.HttpUrlPath.My.getDemandIndividual)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        result="当前数据获取成功"
                        page++
                        val json = jsonObject.getJSONObject("message")
                        pageCount = json.getInt("pageCount")
                        val jsonArray = json.getJSONArray("data")
                        val size = adapter.mData.size
                        val data = adapter.mData.toMutableList()
                        for (j in 0 until jsonArray.length()){
                            val js = jsonArray.getJSONObject(j)
                            val item = MultiStyleItem(MultiStyleItem.Options.REGISTRATION_ITEM,js.getString("requirementType"),"需求专业:"+js.getString("requirementMajor"),"项目地点:"+js.getString("projectSite"))
                            item.necessary = true
                            val id = js.getString("id")
                            item.jumpListener = View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putInt("type",Constants.FragmentType.PERSONAL_TYPE.ordinal)
                                bundle.putString("id",id)
                                FragmentHelper.switchFragment(activity!!,JobMoreFragment.newInstance(bundle),R.id.frame_my_release,"")
                            }
                            item.deleteListener = View.OnClickListener {
                                val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                deleteRequirementPerson(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                    .subscribe({
                                        loadingDialog.dismiss()
                                        if(JSONObject(it.string()).getInt("code")==200){
                                            ToastHelper.mToast(mView.context,"删除成功")
                                            val mData = adapter.mData.toMutableList()
                                            mData.removeAt(mData.indexOf(item))
                                            adapter.mData=mData
                                            adapter.notifyDataSetChanged()
                                        }
                                        else
                                            ToastHelper.mToast(mView.context,"删除失败")
                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(mView.context,"删除信息异常")
                                        it.printStackTrace()
                                    })
                            }
                            item.registrationMoreListener = View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putString("CheckId",id)
                                bundle.putString("type","需求个人")
                                FragmentHelper.switchFragment(activity!!,JobOverviewFragment.newInstance(bundle),R.id.frame_my_release,"")
                            }
                            data.add(item)
                        }
                        adapter.mData = data
                        adapter.notifyItemRangeInserted(size,adapter.mData.size-size)
                    }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
                        result="当前数据为空"
                        pageCount = 0
                    }
                    ToastHelper.mToast(mView.context,result)
                },{
                    it.printStackTrace()
                })
        }
    }
    private fun getDataDemandGroup() {
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("page",page).put("number",4)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,UnSerializeDataBase.dmsBasePath+Constants.HttpUrlPath.My.getDemandGroup)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        result="当前数据获取成功"
                        page++
                        val json = jsonObject.getJSONObject("message")
                        pageCount = json.getInt("pageCount")
                        val jsonArray = json.getJSONArray("data")
                        val size = adapter.mData.size
                        val data = adapter.mData.toMutableList()
                        for (j in 0 until jsonArray.length()){
                            val js = jsonArray.getJSONObject(j)
                            val requirementVariety = js.getString("requirementVariety")

                            val id = js.getString("requirementTeamId")
                            val item = MultiStyleItem(MultiStyleItem.Options.REGISTRATION_ITEM,"需求团队","需求类别:"+requirementVariety,"项目地点:"+js.getString("projectSite"))
                            item.necessary = true
                            var type = 0
                            when(requirementVariety){
                                "变电施工队"-> {
                                    type = Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementPowerTransformation(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "主网施工队"->{
                                    type = Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementMajorNetwork(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "配网施工队"->{
                                    type = Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementDistribuionNetwork(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "测量设计"->{
                                    type = Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementMeasureDesign(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "马帮运输"->{
                                    type = Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementCaravanTransport(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "桩基服务"->{
                                    type = Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementPileFoundation(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "非开挖顶拉管作业"->{
                                    type = Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementUnexcavation(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "试验调试"->{
                                    type = Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementTest(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "跨越架"->{
                                    type = Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementSpanWoodenSupprt(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "运行维护"->{
                                    type = Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementRunningMaintain(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                            }
                            item.jumpListener = View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putString("id",id)
                                bundle.putInt("type",type)
                                FragmentHelper.switchFragment(activity!!,JobMoreFragment.newInstance(bundle),R.id.frame_my_release,"")
                            }
                            item.registrationMoreListener = View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putString("CheckId",id)
                                bundle.putString("type","需求团队")
                                FragmentHelper.switchFragment(activity!!,JobOverviewFragment.newInstance(bundle),R.id.frame_my_release,"")
                            }
                            data.add(item)
                        }
                        adapter.mData = data
                        adapter.notifyItemRangeInserted(size,adapter.mData.size-size)
                    }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
                        result="当前数据为空"
                        pageCount = 0
                    }
                    ToastHelper.mToast(mView.context,result)
                },{
                    it.printStackTrace()
                })
        }
    }
    private fun getDataDemandLease() {
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("page",page).put("number",4)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,UnSerializeDataBase.dmsBasePath+Constants.HttpUrlPath.My.getDemandLease)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        result="当前数据获取成功"
                        page++
                        val json = jsonObject.getJSONObject("message")
                        pageCount = json.getInt("pageCount")
                        val jsonArray = json.getJSONArray("data")
                        val size = adapter.mData.size
                        val data = adapter.mData.toMutableList()
                        for (j in 0 until jsonArray.length()){
                            val js = jsonArray.getJSONObject(j)
                            val requirementVariety = js.getString("requirementVariety")

                            val id = js.getString("requirementLeaseId")
                            var type = 0
                            val item = MultiStyleItem(MultiStyleItem.Options.REGISTRATION_ITEM,"需求租赁","租赁类型:"+requirementVariety,"项目地点:"+js.getString("projectSite"))
                            item.necessary = true
                            when(requirementVariety){
                                "车辆租赁"->{
                                    type = Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementLeaseCar(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "工器具租赁"->{
                                    type = Constants.FragmentType.TOOL_LEASING_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementLeaseConstructionTool(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "设备租赁"->{
                                    type = Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementLeaseFacility(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "机械租赁"->{
                                    type = Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                        loadingDialog.show()
                                        deleteRequirementLeaseMachinery(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                if(JSONObject(it.string()).getInt("code")==200){
                                                    ToastHelper.mToast(mView.context,"删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData=mData
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else
                                                    ToastHelper.mToast(mView.context,"删除失败")
                                            },{
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context,"删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                            }


                            item.jumpListener = View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putInt("type",type)
                                bundle.putString("id",id)
                                FragmentHelper.switchFragment(activity!!,JobMoreFragment.newInstance(bundle),R.id.frame_my_release,"")
                            }
                            item.registrationMoreListener = View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putString("CheckId",id)
                                bundle.putString("type","需求租赁")
                                FragmentHelper.switchFragment(activity!!,JobOverviewFragment.newInstance(bundle),R.id.frame_my_release,"")
                            }
                            data.add(item)
                        }
                        adapter.mData = data
                        adapter.notifyItemRangeInserted(size,adapter.mData.size-size)
                    }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
                        result="当前数据为空"
                        pageCount = 0
                    }
                    ToastHelper.mToast(mView.context,result)
                },{
                    it.printStackTrace()
                })
        }
    }
    private fun getDataDemandTripartite(){
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("page",page).put("number",4)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,UnSerializeDataBase.dmsBasePath+Constants.HttpUrlPath.My.getDemandTripartite)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        result="当前数据获取成功"
                        page++
                        val json = jsonObject.getJSONObject("message")
                        pageCount = json.getInt("pageCount")
                        val jsonArray = json.getJSONArray("data")
                        val size = adapter.mData.size
                        val data = adapter.mData.toMutableList()
                        for (j in 0 until jsonArray.length()){
                            val js = jsonArray.getJSONObject(j)
                            val type = "需求类别:"+js.getString("requirementVariety")
                            val partnerAttribute = arrayListOf("个人","单位")
                            val information = if(type=="资质合作") "合作地区:"+js.getString("cooperationRegion") else "合作方属性:"+partnerAttribute[js.getString("partnerAttribute").toInt()]
                            val item = MultiStyleItem(MultiStyleItem.Options.REGISTRATION_ITEM,"需求三方",type,information)
                            item.necessary = true
                            val id = js.getString("id")
                            item.jumpListener = View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putInt("type",Constants.FragmentType.TRIPARTITE_TYPE.ordinal)
                                bundle.putString("id",id)
                                FragmentHelper.switchFragment(activity!!,JobMoreFragment.newInstance(bundle),R.id.frame_my_release,"")
                            }
                            item.deleteListener = View.OnClickListener {
                                val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                deleteRequirementThirdParty(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                    .subscribe({
                                        loadingDialog.dismiss()
                                        if(JSONObject(it.string()).getInt("code")==200){
                                            ToastHelper.mToast(mView.context,"删除成功")
                                            val mData = adapter.mData.toMutableList()
                                            mData.removeAt(mData.indexOf(item))
                                            adapter.mData=mData
                                            adapter.notifyDataSetChanged()
                                        }
                                        else
                                            ToastHelper.mToast(mView.context,"删除失败")
                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(mView.context,"删除信息异常")
                                        it.printStackTrace()
                                    })
                            }
                            item.registrationMoreListener = View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putString("CheckId",id)
                                bundle.putString("type","需求三方")
                                FragmentHelper.switchFragment(activity!!,JobOverviewFragment.newInstance(bundle),R.id.frame_my_release,"")
                            }
                            data.add(item)
                        }
                        adapter.mData = data
                        adapter.notifyItemRangeInserted(size,adapter.mData.size-size)
                    }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
                        result="当前数据为空"
                        pageCount = 0
                    }
                    ToastHelper.mToast(mView.context,result)
                },{
                    it.printStackTrace()
                })
        }
    }

    private fun getDataPersonalIssue() {
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("page",page).put("pageSize",4)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it, UnSerializeDataBase.dmsBasePath+Constants.HttpUrlPath.My.getPersonalIssue)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if (code == 200) {
                        result = "当前数据获取成功"
                        page++
                        val json = jsonObject.getJSONObject("message")
                        pageCount = json.getInt("pageCount")
                        val jsonArray = json.getJSONArray("data")
                        if (jsonArray.length() == 0)
                            result = "当前数据为空"
                        val size = adapter.mData.size
                        val data = adapter.mData.toMutableList()
                        for (j in 0 until jsonArray.length()) {
                            val js = jsonArray.getJSONObject(j)
                            val sexs = arrayListOf("女", "男")
                            val item = MultiStyleItem(
                                MultiStyleItem.Options.REGISTRATION_ITEM,
                                "个人劳务",
                                "专业工种:" + js.getString("issuerWorkerKind"),
                                "性别要求:" + sexs[js.getInt("sex")]
                            )
                            val id = js.getString("id")
                            item.deleteListener = View.OnClickListener {
                                val loadingDialog = LoadingDialog(
                                    mView.context,
                                    "正在删除中...",
                                    R.mipmap.ic_dialog_loading
                                )
                                loadingDialog.show()
                                deletePersonalIssue(id).observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({
                                        loadingDialog.dismiss()
                                        val json = JSONObject(it.string())
                                        Log.i("json", json.toString())
                                        if (json.getString("desc") == "OK") {
                                            ToastHelper.mToast(mView.context, "删除成功")
                                            val mData = adapter.mData.toMutableList()
                                            mData.removeAt(mData.indexOf(item))
                                            adapter.mData = mData
                                            adapter.notifyDataSetChanged()
                                        } else
                                            ToastHelper.mToast(mView.context, "删除失败")
                                    }, {
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(mView.context, "删除信息异常")
                                        it.printStackTrace()
                                    })
                            }
                            item.jumpListener = View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putInt("type", Constants.FragmentType.PERSONAL_TYPE.ordinal)
                                bundle.putString("id", id)
                                FragmentHelper.switchFragment(
                                    activity!!,
                                    SupplyJobMoreFragment.newInstance(bundle),
                                    R.id.frame_my_release,
                                    ""
                                )
                            }
                            data.add(item)
                        }
                        adapter.mData = data
                        adapter.notifyItemRangeInserted(size, adapter.mData.size - size)
                    } else if (code == 400 && jsonObject.getString("message") == "没有该数据") {
                        result = "当前数据为空"
                        pageCount = 0
                    }
                    ToastHelper.mToast(mView.context, result)
                }, {
                    it.printStackTrace()
                })
        }
    }

    private fun getDataTeamService() {
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("page",page).put("pageSize",4)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,UnSerializeDataBase.dmsBasePath+Constants.HttpUrlPath.My.getTeamService)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if (code == 200) {
                        if (jsonObject.getString("desc") == "FAIL") {
                            result = "当前数据为空"
                            pageCount = 0
                        } else {
                            result = "当前数据获取成功"
                            page++
                            val json = jsonObject.getJSONObject("message")
                            pageCount = json.getInt("pageCount")
                            val jsonArray = json.getJSONArray("data")
                            val size = adapter.mData.size
                            val data = adapter.mData.toMutableList()
                            for (j in 0 until jsonArray.length()) {
                                val js = jsonArray.getJSONObject(j)
                                val name = js.getString("name")
                                var type = 0
                                val id = js.getString("id")
                                val information =
                                    if (js.isNull("implementationRange")) "" else js.getString("implementationRange")
                                val item =
                                    MultiStyleItem(
                                        MultiStyleItem.Options.REGISTRATION_ITEM,
                                        "团队服务",
                                        "供应类别:${name}",
                                        "可实施范围:${information}"
                                    )
                                when(name){
                                    "变电施工队"-> {
                                        type = Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal
                                        item.deleteListener = View.OnClickListener {
                                            val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                            loadingDialog.show()
                                            deletePowerTransformation(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                                .subscribe({
                                                    loadingDialog.dismiss()
                                                    if(JSONObject(it.string()).getInt("code")==200){
                                                        ToastHelper.mToast(mView.context,"删除成功")
                                                        val mData = adapter.mData.toMutableList()
                                                        mData.removeAt(mData.indexOf(item))
                                                        adapter.mData=mData
                                                        adapter.notifyDataSetChanged()
                                                    }
                                                    else
                                                        ToastHelper.mToast(mView.context,"删除失败")
                                                },{
                                                    loadingDialog.dismiss()
                                                    ToastHelper.mToast(mView.context,"删除信息异常")
                                                    it.printStackTrace()
                                                })
                                        }
                                    }
                                    "主网施工队"->{
                                        type = Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal
                                        item.deleteListener = View.OnClickListener {
                                            val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                            loadingDialog.show()
                                            deleteMajorNetwork(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                                .subscribe({
                                                    loadingDialog.dismiss()
                                                    if(JSONObject(it.string()).getInt("code")==200){
                                                        ToastHelper.mToast(mView.context,"删除成功")
                                                        val mData = adapter.mData.toMutableList()
                                                        mData.removeAt(mData.indexOf(item))
                                                        adapter.mData=mData
                                                        adapter.notifyDataSetChanged()
                                                    }
                                                    else
                                                        ToastHelper.mToast(mView.context,"删除失败")
                                                },{
                                                    loadingDialog.dismiss()
                                                    ToastHelper.mToast(mView.context,"删除信息异常")
                                                    it.printStackTrace()
                                                })
                                        }
                                    }
                                    "配网施工队"->{
                                        type = Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal
                                        item.deleteListener = View.OnClickListener {
                                            val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                            loadingDialog.show()
                                            deleteDistribuionNetwork(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                                .subscribe({
                                                    loadingDialog.dismiss()
                                                    if(JSONObject(it.string()).getInt("code")==200){
                                                        ToastHelper.mToast(mView.context,"删除成功")
                                                        val mData = adapter.mData.toMutableList()
                                                        mData.removeAt(mData.indexOf(item))
                                                        adapter.mData=mData
                                                        adapter.notifyDataSetChanged()
                                                    }
                                                    else
                                                        ToastHelper.mToast(mView.context,"删除失败")
                                                },{
                                                    loadingDialog.dismiss()
                                                    ToastHelper.mToast(mView.context,"删除信息异常")
                                                    it.printStackTrace()
                                                })
                                        }
                                    }
                                    "测量设计"->{
                                        type = Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal
                                        item.deleteListener = View.OnClickListener {
                                            val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                            loadingDialog.show()
                                            deleteMeasureDesign(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                                .subscribe({
                                                    loadingDialog.dismiss()
                                                    if(JSONObject(it.string()).getInt("code")==200){
                                                        ToastHelper.mToast(mView.context,"删除成功")
                                                        val mData = adapter.mData.toMutableList()
                                                        mData.removeAt(mData.indexOf(item))
                                                        adapter.mData=mData
                                                        adapter.notifyDataSetChanged()
                                                    }
                                                    else
                                                        ToastHelper.mToast(mView.context,"删除失败")
                                                },{
                                                    loadingDialog.dismiss()
                                                    ToastHelper.mToast(mView.context,"删除信息异常")
                                                    it.printStackTrace()
                                                })
                                        }
                                    }
                                    "马帮运输"->{
                                        type = Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal
                                        item.deleteListener = View.OnClickListener {
                                            val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                            loadingDialog.show()
                                            deleteCaravanTransport(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                                .subscribe({
                                                    loadingDialog.dismiss()
                                                    if(JSONObject(it.string()).getInt("code")==200){
                                                        ToastHelper.mToast(mView.context,"删除成功")
                                                        val mData = adapter.mData.toMutableList()
                                                        mData.removeAt(mData.indexOf(item))
                                                        adapter.mData=mData
                                                        adapter.notifyDataSetChanged()
                                                    }
                                                    else
                                                        ToastHelper.mToast(mView.context,"删除失败")
                                                },{
                                                    loadingDialog.dismiss()
                                                    ToastHelper.mToast(mView.context,"删除信息异常")
                                                    it.printStackTrace()
                                                })
                                        }
                                    }
                                    "桩基服务"->{
                                        type = Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal
                                        item.deleteListener = View.OnClickListener {
                                            val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                            loadingDialog.show()
                                            deletePileFoundation(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                                .subscribe({
                                                    loadingDialog.dismiss()
                                                    if(JSONObject(it.string()).getInt("code")==200){
                                                        ToastHelper.mToast(mView.context,"删除成功")
                                                        val mData = adapter.mData.toMutableList()
                                                        mData.removeAt(mData.indexOf(item))
                                                        adapter.mData=mData
                                                        adapter.notifyDataSetChanged()
                                                    }
                                                    else
                                                        ToastHelper.mToast(mView.context,"删除失败")
                                                },{
                                                    loadingDialog.dismiss()
                                                    ToastHelper.mToast(mView.context,"删除信息异常")
                                                    it.printStackTrace()
                                                })
                                        }
                                    }
                                    "非开挖顶拉管作业"->{
                                        type = Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal
                                        item.deleteListener = View.OnClickListener {
                                            val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                            loadingDialog.show()
                                            deleteUnexcavation(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                                .subscribe({
                                                    loadingDialog.dismiss()
                                                    if(JSONObject(it.string()).getInt("code")==200){
                                                        ToastHelper.mToast(mView.context,"删除成功")
                                                        val mData = adapter.mData.toMutableList()
                                                        mData.removeAt(mData.indexOf(item))
                                                        adapter.mData=mData
                                                        adapter.notifyDataSetChanged()
                                                    }
                                                    else
                                                        ToastHelper.mToast(mView.context,"删除失败")
                                                },{
                                                    loadingDialog.dismiss()
                                                    ToastHelper.mToast(mView.context,"删除信息异常")
                                                    it.printStackTrace()
                                                })
                                        }
                                    }
                                    "试验调试"->{
                                        type = Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal
                                        item.deleteListener = View.OnClickListener {
                                            val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                            loadingDialog.show()
                                            deleteTestTeam(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                                .subscribe({
                                                    loadingDialog.dismiss()
                                                    if(JSONObject(it.string()).getInt("code")==200){
                                                        ToastHelper.mToast(mView.context,"删除成功")
                                                        val mData = adapter.mData.toMutableList()
                                                        mData.removeAt(mData.indexOf(item))
                                                        adapter.mData=mData
                                                        adapter.notifyDataSetChanged()
                                                    }
                                                    else
                                                        ToastHelper.mToast(mView.context,"删除失败")
                                                },{
                                                    loadingDialog.dismiss()
                                                    ToastHelper.mToast(mView.context,"删除信息异常")
                                                    it.printStackTrace()
                                                })
                                        }
                                    }
                                    "跨越架"->{
                                        type = Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal
                                        item.deleteListener = View.OnClickListener {
                                            val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                            loadingDialog.show()
                                            deleteSpanWoodenSupprt(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                                .subscribe({
                                                    loadingDialog.dismiss()
                                                    if(JSONObject(it.string()).getInt("code")==200){
                                                        ToastHelper.mToast(mView.context,"删除成功")
                                                        val mData = adapter.mData.toMutableList()
                                                        mData.removeAt(mData.indexOf(item))
                                                        adapter.mData=mData
                                                        adapter.notifyDataSetChanged()
                                                    }
                                                    else
                                                        ToastHelper.mToast(mView.context,"删除失败")
                                                },{
                                                    loadingDialog.dismiss()
                                                    ToastHelper.mToast(mView.context,"删除信息异常")
                                                    it.printStackTrace()
                                                })
                                        }
                                    }
                                    "运行维护"->{
                                        type = Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal
                                        item.deleteListener = View.OnClickListener {
                                            val loadingDialog = LoadingDialog(mView.context, "正在删除中...", R.mipmap.ic_dialog_loading)
                                            loadingDialog.show()
                                            deleteRunningMaintain(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                                .subscribe({
                                                    loadingDialog.dismiss()
                                                    if(JSONObject(it.string()).getInt("code")==200){
                                                        ToastHelper.mToast(mView.context,"删除成功")
                                                        val mData = adapter.mData.toMutableList()
                                                        mData.removeAt(mData.indexOf(item))
                                                        adapter.mData=mData
                                                        adapter.notifyDataSetChanged()
                                                    }
                                                    else
                                                        ToastHelper.mToast(mView.context,"删除失败")
                                                },{
                                                    loadingDialog.dismiss()
                                                    ToastHelper.mToast(mView.context,"删除信息异常")
                                                    it.printStackTrace()
                                                })
                                        }
                                    }
                                }


                                item.jumpListener = View.OnClickListener {
                                    val bundle = Bundle()
                                    bundle.putString("id", id)
                                    bundle.putInt("type",type)
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        SupplyJobMoreFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                        ""
                                    )
                                }
                                data.add(item)
                            }
                            adapter.mData = data
                            adapter.notifyItemRangeInserted(size, adapter.mData.size - size)
                        }
                    }
                    ToastHelper.mToast(mView.context,result)
                }, {
                    it.printStackTrace()
                })
        }
    }

    private fun getDataLeaseService() {
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("page",page).put("number",4)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(
                it,
                UnSerializeDataBase.dmsBasePath + Constants.HttpUrlPath.My.getLeaseService
            )
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if (code == 200) {
                        result = "当前数据获取成功"
                        page++
                        val json = jsonObject.getJSONObject("message")
                        pageCount = json.getInt("pageCount")
                        val jsonArray = json.getJSONArray("data")
                        if (jsonArray.length() == 0)
                            result = "当前数据为空"
                        val size = adapter.mData.size
                        val data = adapter.mData.toMutableList()
                        for (j in 0 until jsonArray.length()) {
                            val js = jsonArray.getJSONObject(j)
                            val varietype = js.getString("type")
                            val information =
                                if (varietype == "车辆租赁") "可服务地域:" + js.getString("issuerBelongSite") else "有效期:${js.getString(
                                    "validTime"
                                )}天"
                            val item = MultiStyleItem(
                                MultiStyleItem.Options.REGISTRATION_ITEM,
                                "租赁服务",
                                "服务类型:${varietype}",
                                information
                            )
                            val id = js.getString("leaseId")
                            var type = 0
                            when (varietype) {
                                "车辆租赁" -> {
                                    type = Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(
                                            mView.context,
                                            "正在删除中...",
                                            R.mipmap.ic_dialog_loading
                                        )
                                        loadingDialog.show()
                                        deleteLeaseCar(id).observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                val json = JSONObject(it.string())
                                                Log.i("json", json.toString())
                                                if (json.getString("desc") == "OK") {
                                                    ToastHelper.mToast(mView.context, "删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData = mData
                                                    adapter.notifyDataSetChanged()
                                                } else
                                                    ToastHelper.mToast(mView.context, "删除失败")
                                            }, {
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context, "删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "设备租赁" -> {
                                    type = Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(
                                            mView.context,
                                            "正在删除中...",
                                            R.mipmap.ic_dialog_loading
                                        )
                                        loadingDialog.show()
                                        deleteLeaseFacility(id).observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                val json = JSONObject(it.string())
                                                Log.i("json", json.toString())
                                                if (json.getString("desc") == "OK") {
                                                    ToastHelper.mToast(mView.context, "删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData = mData
                                                    adapter.notifyDataSetChanged()
                                                } else
                                                    ToastHelper.mToast(mView.context, "删除失败")
                                            }, {
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context, "删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "机械租赁" -> {
                                    type = Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(
                                            mView.context,
                                            "正在删除中...",
                                            R.mipmap.ic_dialog_loading
                                        )
                                        loadingDialog.show()
                                        deleteLeaseMachinery(id).observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                val json = JSONObject(it.string())
                                                Log.i("json", json.toString())
                                                if (json.getString("desc") == "OK") {
                                                    ToastHelper.mToast(mView.context, "删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData = mData
                                                    adapter.notifyDataSetChanged()
                                                } else
                                                    ToastHelper.mToast(mView.context, "删除失败")
                                            }, {
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context, "删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                                "工器具租赁" -> {
                                    type = Constants.FragmentType.TOOL_LEASING_TYPE.ordinal
                                    item.deleteListener = View.OnClickListener {
                                        val loadingDialog = LoadingDialog(
                                            mView.context,
                                            "正在删除中...",
                                            R.mipmap.ic_dialog_loading
                                        )
                                        loadingDialog.show()
                                        deleteLeaseConstructionTool(id).observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe({
                                                loadingDialog.dismiss()
                                                val json = JSONObject(it.string())
                                                Log.i("json", json.toString())
                                                if (json.getString("desc") == "OK") {
                                                    ToastHelper.mToast(mView.context, "删除成功")
                                                    val mData = adapter.mData.toMutableList()
                                                    mData.removeAt(mData.indexOf(item))
                                                    adapter.mData = mData
                                                    adapter.notifyDataSetChanged()
                                                } else
                                                    ToastHelper.mToast(mView.context, "删除失败")
                                            }, {
                                                loadingDialog.dismiss()
                                                ToastHelper.mToast(mView.context, "删除信息异常")
                                                it.printStackTrace()
                                            })
                                    }
                                }
                            }

                            item.jumpListener = View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putString("id", id)
                                bundle.putInt("type", type)
                                FragmentHelper.switchFragment(
                                    activity!!,
                                    SupplyJobMoreFragment.newInstance(bundle),
                                    R.id.frame_my_release,
                                    ""
                                )
                            }
                            data.add(item)
                        }
                        adapter.mData = data
                        adapter.notifyItemRangeInserted(size, adapter.mData.size - size)
                    } else if (code == 400 && jsonObject.getString("message") == "没有该数据") {
                        result = "当前数据为空"
                        pageCount = 0
                    }
                    ToastHelper.mToast(mView.context, result)
                }, {
                    it.printStackTrace()
                })
        }
    }

    private fun getDataThridService() {
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("page",page).put("pageSize",4)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(
                it,
                UnSerializeDataBase.dmsBasePath + Constants.HttpUrlPath.My.getThridService
            )
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if (code == 200) {
                        result = "当前数据获取成功"
                        page++
                        val json = jsonObject.getJSONObject("message")
                        pageCount = json.getInt("pageCount")
                        val jsonArray = json.getJSONArray("data")
                        if (jsonArray.length() == 0)
                            result = "当前数据为空"
                        val size = adapter.mData.size
                        val data = adapter.mData.toMutableList()
                        for (j in 0 until jsonArray.length()) {
                            val js = jsonArray.getJSONObject(j)
                            val item = MultiStyleItem(
                                MultiStyleItem.Options.REGISTRATION_ITEM,
                                "三方服务",
                                "服务类型:" + js.getString("serveType"),
                                "有效期:${js.getString("validTime")}天"
                            )
                            val id = js.getString("id")
                            item.deleteListener = View.OnClickListener {
                                val loadingDialog =
                                    LoadingDialog(
                                        mView.context,
                                        "正在删除中...",
                                        R.mipmap.ic_dialog_loading
                                    )
                                loadingDialog.show()
                                deleteThirdServices(id).observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({
                                        loadingDialog.dismiss()
                                        val json = JSONObject(it.string())
                                        Log.i("json", json.toString())
                                        if (json.getString("desc") == "OK") {
                                            ToastHelper.mToast(mView.context, "删除成功")
                                            val mData = adapter.mData.toMutableList()
                                            mData.removeAt(mData.indexOf(item))
                                            adapter.mData = mData
                                            adapter.notifyDataSetChanged()
                                        } else
                                            ToastHelper.mToast(mView.context, "删除失败")
                                    }, {
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(mView.context, "删除信息异常")
                                        it.printStackTrace()
                                    })
                            }

                            item.jumpListener = View.OnClickListener {
                                val bundle = Bundle()
                                bundle.putInt(
                                    "type",
                                    Constants.FragmentType.TRIPARTITE_TYPE.ordinal
                                )
                                bundle.putString("id", id)
                                FragmentHelper.switchFragment(
                                    activity!!,
                                    SupplyJobMoreFragment.newInstance(bundle),
                                    R.id.frame_my_release,
                                    ""
                                )
                            }
                            data.add(item)
                        }
                        adapter.mData = data
                        adapter.notifyItemRangeInserted(size, adapter.mData.size - size)
                    } else if (code == 400 && jsonObject.getString("message") == "没有该数据") {
                        result = "当前数据为空"
                        pageCount = 0
                    }
                    ToastHelper.mToast(mView.context, result)
                }, {
                    it.printStackTrace()
                })
        }
    }
}