package com.example.eletronicengineer.fragment.yellowpages

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.SupplyActivity
import com.example.eletronicengineer.activity.YellowPagesActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.UnSerializeDataBase
import com.example.eletronicengineer.utils.startSendMessage
import com.example.eletronicengineer.utils.uploadImage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_supply_publish.*
import kotlinx.android.synthetic.main.activity_supply_publish.view.*
import kotlinx.android.synthetic.main.activity_supply_publish.view.submit
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray

import org.json.JSONObject
import java.io.File

class YellowPagesFragment:Fragment(){
    companion object{
        fun newInstance(args:Bundle): YellowPagesFragment
        {
            val fragment= YellowPagesFragment()
            fragment.arguments=args
            return fragment
        }
    }
    var mAdapter:RecyclerviewAdapter?=null
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView= inflater.inflate(R.layout.activity_supply_publish,container,false)
        initFragment(mView)
        return mView
    }
    fun initFragment(mView:View){
        val data=arguments
        val adapterGenerate=AdapterGenerate()
        adapterGenerate.context=mView.context
        adapterGenerate.activity=activity as YellowPagesActivity
        //加载选择的界面
        if(mAdapter==null){
            val result = Observable.create<RecyclerviewAdapter>{
                val adapter = switchAdapter(adapterGenerate,data!!.getInt("type"),arguments!!.getString("selectContent"))
                it.onNext(adapter)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mAdapter=it
                    mView.rv_supply_content.adapter=mAdapter
                    mView.rv_supply_content.layoutManager= LinearLayoutManager(context)
                }

        }
        else{
            mView.rv_supply_content.adapter=mAdapter
            mView.rv_supply_content.layoutManager= LinearLayoutManager(context)
        }
        //标题
        mView.tv_title_title1.setText("行业黄页")
        //返回
        mView.tv_title_back.setOnClickListener{
            UnSerializeDataBase.imgList.clear()
            activity!!.finish()
        }
        //发布
        mView.submit.setOnClickListener{

            val networkAdapter= NetworkAdapter(mAdapter!!.mData, submit.context)
            val provider=NetworkAdapter.Provider(mAdapter!!.mData,submit.context)
            if(networkAdapter.check()){
                val json=JSONObject()
                    provider.generateJsonRequestBody(json).subscribe {
                        val loadingDialog = LoadingDialog(mView.context, "正在请求...", R.mipmap.ic_dialog_loading)
                        loadingDialog.show()
                        val result = startSendMessage(it, UnSerializeDataBase.dmsBasePath+mAdapter!!.urlPath).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        loadingDialog.dismiss()
                                        val json = JSONObject(it.string())
                                        if (json.getInt("code") == 200) {
                                                Toast.makeText(context, "请求成功", Toast.LENGTH_SHORT).show()
                                                mView.tv_title_back.callOnClick()
                                        }else if(json.getInt("code") == 403){
                                            Toast.makeText(context, "${json.getString("desc")} 请升级为更高级会员", Toast.LENGTH_SHORT).show()
                                        } else if (json.getInt("code") == 400) {
                                            Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    {
                                        loadingDialog.dismiss()
                                        val toast = Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT)
                                        toast.setGravity(Gravity.CENTER, 0, 0)
                                        toast.show()
                                        it.printStackTrace()
                                    }
                                )
                }
            }
        }
    }
    fun switchAdapter(adapterGenerate: AdapterGenerate,Type: Int,selectContent:String):RecyclerviewAdapter
    {
        lateinit var adapter:RecyclerviewAdapter
        when(Type){
            Constants.FragmentType.INDUSTRYYELLOWPAGES_TYPE.ordinal->{
                adapter=adapterGenerate.yellowPagesThreePublish(selectContent)
                adapter.mData[0].singleDisplayRightContent = selectContent
                adapter.urlPath = Constants.HttpUrlPath.YellowPages.yellowPagesPublish
            }
        }
        return adapter
    }
}