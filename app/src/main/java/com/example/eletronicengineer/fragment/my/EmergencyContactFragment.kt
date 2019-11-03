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
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.utils.FragmentHelper
import com.example.eletronicengineer.utils.getUser
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_emergency_contact.view.*
import org.json.JSONObject

class EmergencyContactFragment :Fragment(){
    lateinit var mView: View
    var adapter: RecyclerviewAdapter?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_emergency_contact,container,false)
        mView.tv_emergency_contact_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mView.btn_add_emergency.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("style",0)
            FragmentHelper.switchFragment(activity!!,EditEmergencyContactFragment.newInstance(bundle),R.id.frame_my_information,"")
        }
        initFragment()
        return mView
    }
    private fun initFragment() {
        val mMultiStyleItemList: MutableList<MultiStyleItem> = ArrayList()
//        val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,"张凌",false)
//        item.jumpListener = View.OnClickListener {
//            (activity as MyInformationActivity).switchFragment(EmergencyContactInformationFragment(),"")
//        }
//        mMultiStyleItemList.add(item)
        adapter = RecyclerviewAdapter(mMultiStyleItemList)
        val result = NetworkAdapter().getDataUser().subscribe({
            val urgentPeoples = it.message.urgentPeoples
            val jsonArray = JSONObject(Gson().toJson(it.message)).getJSONArray("urgentPeoples")
            if(urgentPeoples!=null)
                for (j in urgentPeoples){
                    val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT,j.urgentPeopleName,false)
                    item.jumpListener = View.OnClickListener {
                        val bundle = Bundle()
                        bundle.putString("urgentPeople",jsonArray.getJSONObject(urgentPeoples.indexOf(j)).toString())
                        FragmentHelper.switchFragment(activity!!,EmergencyContactInformationFragment.newInstance(bundle),R.id.frame_my_information,"")
                    }
                    mMultiStyleItemList.add(item)
                }
            adapter = RecyclerviewAdapter(mMultiStyleItemList)
            mView.rv_emergency_contact_content.adapter = adapter
            mView.rv_emergency_contact_content.layoutManager = LinearLayoutManager(context)
        },{

        })
//        mView.rv_emergency_contact_content.adapter = adapter
//        mView.rv_emergency_contact_content.layoutManager = LinearLayoutManager(context)
    }
}