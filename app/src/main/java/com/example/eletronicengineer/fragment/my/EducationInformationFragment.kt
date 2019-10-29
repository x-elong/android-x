package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyInformationActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.utils.getUser
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_education_information.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat

class EducationInformationFragment :Fragment(){

    lateinit var mView: View
    var adapter = RecyclerviewAdapter(ArrayList())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_education_information,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        val mMultiStyleItemList=ArrayList<MultiStyleItem>()
        var item = MultiStyleItem(MultiStyleItem.Options.TITLE,"学历信息","2")
        mMultiStyleItemList.add(item)
        item.backListener = View.OnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        item.tvSelect = View.OnClickListener {
            val bundle = Bundle()
            bundle.putInt("type",0)
            (activity as MyInformationActivity).switchFragment(AddEducationInformationFragment.newInstance(bundle),"AddEducation")
        }
//        item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"学历","毕业时间","毕业院校+专业")
//        item.jumpListener = View.OnClickListener {
//            val bundle = Bundle()
//            (activity as MyInformationActivity).switchFragment(AddEducationInformationFragment.newInstance(bundle),"AddEducation")
//        }
//        mMultiStyleItemList.add(item)
//        item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,"本科","2019-10-01","湖南人文科技学院物联网工程")
//        item.jumpListener = View.OnClickListener {
//            val bundle = Bundle()
//            (activity as MyInformationActivity).switchFragment(AddEducationInformationFragment.newInstance(bundle), "AddEducation")
//        }
//        mMultiStyleItemList.add(item)
        adapter = RecyclerviewAdapter(mMultiStyleItemList)
        val result = getUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val educationBackgrounds= it.message.educationBackgrounds
                val jsonArray = JSONObject(Gson().toJson(it.message)).getJSONArray("educationBackgrounds")
                if(educationBackgrounds!=null)
                    for (j in educationBackgrounds){
                        val item = MultiStyleItem(MultiStyleItem.Options.DEMAND_ITEM,j.educationBackground,
                            SimpleDateFormat("yyyy-MM-dd").format(j.graduationTime),j.graduationAcademy+j.major)
                        item.jumpListener = View.OnClickListener {
                            val bundle = Bundle()
                            bundle.putInt("type",1)
                            bundle.putString("EducationBackground",jsonArray.getJSONObject(educationBackgrounds.indexOf(j)).toString())
                            (activity as MyInformationActivity).switchFragment(AddEducationInformationFragment.newInstance(bundle), "ModifyEducation")
                        }
                        mMultiStyleItemList.add(item)
                    }
                adapter.mData = mMultiStyleItemList
                adapter.notifyDataSetChanged()
            },{
                it.printStackTrace()
            })
//        (mView.rv_my_release_content.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        mView.rv_education_information_content.adapter  = adapter
        mView.rv_education_information_content.layoutManager= LinearLayoutManager(context)
    }
}