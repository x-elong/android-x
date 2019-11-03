package com.example.eletronicengineer.fragment.my

import android.os.Bundle
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
import com.example.eletronicengineer.activity.MyReleaseActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.adapter.StoreTypeAdapter
import com.example.eletronicengineer.aninterface.StoresName
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.startSendMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_job_overview.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class JobOverviewFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):JobOverviewFragment{
            val jobOverviewFragment = JobOverviewFragment()
            jobOverviewFragment.arguments = args
            return jobOverviewFragment
        }
    }
    var baseUrl = "http://10.1.5.141:8012"
    lateinit var checkId:String
    var page = 1
    var pageCount = 1
    lateinit var mView: View
    lateinit var type:String
    var adapter = RecyclerviewAdapter(ArrayList())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_job_overview,container,false)
        checkId = arguments!!.getString("CheckId")
        type = arguments!!.getString("type")
        initFragment()
        initOnScrollListener()
        return mView
    }
    private fun initOnScrollListener() {
        mView.rv_registration_list_content.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                if(lastCompletelyVisibleItemPosition == layoutManager.itemCount-1 && page<=pageCount){
                    Log.i("page","$page")
                    Toast.makeText(mView.context,"滑动到底了", Toast.LENGTH_SHORT).show()
                    getData()
                }
            }
        })
    }
    private fun initFragment() {
        mView.tv_job_overview_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.rv_registration_list_content.adapter=adapter
        mView.rv_registration_list_content.layoutManager=LinearLayoutManager(context)
        getData()
    }

    private fun getData() {
        baseUrl+=when(type){
            "需求个人"->Constants.HttpUrlPath.My.getPersonRequirement
            "需求团队"->Constants.HttpUrlPath.My.getRequirementTeam
            "需求租赁"->Constants.HttpUrlPath.My.getLease
            else->Constants.HttpUrlPath.My.getRequirementThird
        }
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("checkId",checkId).put("page",page).put("pageSize",5)
                val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,baseUrl)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                    val jsonObject = JSONObject(it.string())
                    val code = jsonObject.getInt("code")
                    var result = ""
                    if(code==200){
                        result="当前数据获取成功"
                        page++
                        val json = jsonObject.getJSONObject("message")
                        pageCount = json.getInt("pageCount")
                        mView.tv_apply_number.text="(${json.getInt("sum")})"
                        val jsonArray = json.getJSONArray("data")
                        val size = adapter.mData.size
                        val data = adapter.mData.toMutableList()
                        for (j in 0 until jsonArray.length()){
                            val js = jsonArray.getJSONObject(j)
                            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,js.getString("name"),js.getString("phone"))
//                            val item = MultiStyleItem(MultiStyleItem.Options.STORE,"","张凌","44岁 | 湖南省 长沙市雨花区","普工 | 面议","0")
                            data.add(item)
                        }
                        adapter.mData = data
                        adapter.notifyItemRangeInserted(size,adapter.mData.size-size)
                    }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
                        result="当前数据为空"
                        pageCount = 0
                    }else if(code == 500){
                        result = jsonObject.getString("message")
                    }
                    Toast.makeText(context,result, Toast.LENGTH_SHORT).show()
                },{
                    it.printStackTrace()
                })
        }
    }
}