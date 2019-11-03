package com.example.eletronicengineer.fragment.retailstore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.GetQRCodeActivity
import com.example.eletronicengineer.activity.ImageDisplayActivity
import com.example.eletronicengineer.activity.MainActivity
import com.example.eletronicengineer.activity.RetailStoreActivity
import com.example.eletronicengineer.fragment.distribution.IntegralFragment
import com.example.eletronicengineer.utils.getOwnIntegral
import com.example.eletronicengineer.utils.getQRCode
import com.example.eletronicengineer.utils.getUserIncome
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_retail_store.view.*

class RetailStoreFragment:Fragment() {
    companion object{
        fun newInstance(args: Bundle): RetailStoreFragment
        {
            val fragment= RetailStoreFragment()
            fragment.arguments=args
            return fragment
        }
    }
    lateinit var title:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_retail_store, container, false)
//        title=arguments!!.getString("title")
//        view.tv_retail_title.text=title
        initFragment(view)
        return view
    }

    private fun initFragment(view: View) {
        view.tv_retail_back.setOnClickListener {
            activity!!.finish()
        }
        val data = Bundle()

        //二维码按钮监听
        view.tv_retail_two_dimensional_code_image.setOnClickListener{
            val result = getQRCode("http://10.1.5.141:8022/").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    val path = it.message
                    //将拿到的路径访问文件服务器，拿到照片，然后进入碎片显示
                    Toast.makeText(context,path,Toast.LENGTH_LONG).show()

                    if(path!=null)
                    {
                        //进入二维码活动显示二维码
                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                        intent.putExtra("mImagePaths", path)
                        activity!!.startActivity(intent)
                    }
                },{
                    it.printStackTrace()
                })
            }

        //总积分
        val resultForTotalScore = getOwnIntegral("http://10.1.5.141:8022/").subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                if(it.message!= null)
                {
                    view.tv_total_score_num.text = it.message.toString()
                    Log.i("totalNum",it.message.toString())
                }
                else{
                    val temp = 0
                    view.tv_total_score_num.text = temp.toString()
                }
            },{
                it.printStackTrace()
            })

        //月收益
        val resultForMonthBenifit = getUserIncome("month","http://10.1.5.141:8022/").subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                if(it.message!= null)
                {
                view.tv_month_income_num.text = it.message
                Log.i("num", it.message)
                }
                else{
                    val temp = 0
                    view.tv_month_income_num.text = temp.toString()
                }
            },{
                it.printStackTrace()
            })

        //日收益
        val resultForDayBenifit = getUserIncome("day","http://10.1.5.141:8022/").subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                if(it.message!= null)
                {
                    view.tv_day_income_num.text = it.message
                    Log.i("num", it.message)
                }
                else{
                    val temp = 0
                    view.tv_day_income_num.text = temp.toString()
                }
            },{
                it.printStackTrace()
            })
        //积分详情按钮监听
        view.tv_score_details_image.setOnClickListener {
              data.putInt("type",1)
              data.putString("title","积分详情")
            (activity as RetailStoreActivity).switchFragment(IntegralFragment.newInstance(data))
        }
        //下级会员按钮监听
        view.tv_sub_vip_image.setOnClickListener {
            data.putInt("type",2)
            data.putString("title","下级会员")
            (activity as RetailStoreActivity).switchFragment(IntegralFragment.newInstance(data))
        }
        //推广详情按钮监听
        view.tv_promote_details_image.setOnClickListener {
            data.putInt("type",3)
            data.putString("title","推广详情")
            (activity as RetailStoreActivity).switchFragment(IntegralFragment.newInstance(data))
        }
    }
}