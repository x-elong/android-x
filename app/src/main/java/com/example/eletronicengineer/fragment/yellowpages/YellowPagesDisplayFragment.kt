package com.example.eletronicengineer.fragment.retailstore

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.GetQRCodeActivity
import com.example.eletronicengineer.activity.SupplyDisplayActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.fragment.sdf.ProjectListFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.getSupplyMajorNetWork
import com.example.eletronicengineer.utils.getSupplyPersonDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_yellow_pages_display.view.*
import java.io.Serializable


class YellowPagesDisplayFragment:Fragment() {
    companion object{
        fun newInstance(args: Bundle): YellowPagesDisplayFragment
        {
            val fragment= YellowPagesDisplayFragment()
            fragment.arguments=args
            return fragment
        }
    }
    var mdata=Bundle()
    var type:Int=0
    lateinit var id:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_yellow_pages_display, container, false)
        id=arguments!!.getString("id")
        type = arguments!!.getInt("type")
        initFragment(view)
        return view
    }

    private fun initFragment(view: View) {
        view.tv_yellow_pages_display_back.setOnClickListener {
            activity!!.finish()
        }
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = view.context
        adapterGenerate.activity = activity as AppCompatActivity
        lateinit var adapter: RecyclerviewAdapter
        when (type) {
            Constants.FragmentType.INDUSTRYYELLOWPAGES_TYPE.ordinal -> {
                adapter = adapterGenerate.yellowPagesDisplay()
                val result = getYellowPagesDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        val data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.companyType==null) {
                            " " } else{ data.companyType }
                        val Qualification:List<String> = listOf(data.firstQualification,data.secondQualification,data.thirdQualification)
                        var temp=""
                        for(str in Qualification){
                            if(str!=null&&str!=""){
                                if(temp!="")
                                    temp+="|"
                                temp+=str
                            }
                        }
                        adapter.mData[1].singleDisplayRightContent= temp
                        adapter.mData[2].singleDisplayRightContent=if(data.companyName==null) {
                            " " } else{ data.companyName}
                        adapter.mData[3].singleDisplayRightContent=if(data.companyAddress==null) {
                            " " } else{ data.companyAddress}
                        adapter.mData[4].singleDisplayRightContent=if(data.businessScope==null) {
                            " " } else{ data.businessScope}
                        adapter.mData[5].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[6].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name}
                        view.button_yellow_pages.setOnClickListener {
                            if(data.phone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
                                    .setMessage(data.phone)
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:${data.phone}"))
                                        startActivity(intent)
                                    }
                                    .setPositiveButton("确定") { dialog, which ->
                                        dialog.dismiss() }.create()
                                dialog.show()
                            }
                        else{
                                Toast.makeText(context,"对方未留联系方式",Toast.LENGTH_SHORT).show()
                            }
                        }
                        view.rv_yellow_pages_display_content.adapter = adapter
                        view.rv_yellow_pages_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }


        }

    }
}