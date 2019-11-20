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
import com.example.eletronicengineer.model.ApiConfig
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.getSupplyMajorNetWork
import com.example.eletronicengineer.utils.getSupplyPersonDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_demand_display.view.*
import java.io.Serializable

class SupplyDisplayFragment:Fragment() {
    companion object{
        fun newInstance(args: Bundle): SupplyDisplayFragment
        {
            val fragment= SupplyDisplayFragment()
            fragment.arguments=args
            return fragment
        }
    }
    var mdata=Bundle()
    var type:Int=0
    lateinit var id:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_demand_display, container, false)
        id=arguments!!.getString("id")
        type = arguments!!.getInt("type")
        initFragment(view)
        return view
    }

    private fun initFragment(view: View) {
        view.tv_display_demand_back.setOnClickListener {
            activity!!.finish()
        }
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = view.context
        adapterGenerate.activity = activity as AppCompatActivity
        lateinit var adapter: RecyclerviewAdapter
        when (type) {
            1 -> {
                adapter = adapterGenerate.supplyIndividualDisplay()
                val result = getSupplyPersonDetail(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.issuerWorkType==null) {
                            " " } else{ data.issuerWorkType }
                        adapter.mData[1].singleDisplayRightContent=if(data.issuerWorkerKind==null) {
                            " " } else{ data.issuerWorkerKind }
                        adapter.mData[2].singleDisplayRightContent=if(data.contact==null) {
                            " " } else{ data.contact}
                       if(data.sex==null) {
                           adapter.mData[3].singleDisplayRightContent=" " } else if(data.sex=="1"){  adapter.mData[3].singleDisplayRightContent="女"}
                        else if(data.sex=="0"){ adapter.mData[3].singleDisplayRightContent="男"}
                        adapter.mData[4].singleDisplayRightContent=if(data.age==null) {
                            " " } else{ data.age}
                        adapter.mData[5].singleDisplayRightContent=if(data.workExperience==null) {
                            " " } else{ data.workExperience}
                        adapter.mData[6].singleDisplayRightContent=if(data.workMoney==null||data.salaryUnit==null) {
                            " " } else{  "${data.workMoney} ${data.salaryUnit}"}
                        adapter.mData[7].singleDisplayRightContent=if(data.contactPhone==null) {
                            " " } else{ "联系对方时可见"}
                        if(data.certificatePath==null)
                        {
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                //显示图片
                                val intent = Intent(activity, GetQRCodeActivity::class.java)
                                intent.putExtra("imagePath", data.certificatePath)
                                startActivity(intent)
                            })
                        }
                        adapter.mData[9].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[10].singleDisplayRightContent=if(data.remark==null) {
                            " " } else{ data.remark}
                        adapter.mData[11].submitListener = View.OnClickListener {
                            if(data.contactPhone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话：")
                                    .setMessage(data.contactPhone)
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:${data.contactPhone}"))
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
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            2->{//主网
                adapter = adapterGenerate.supplyTeamDisplay()
                val result = getSupplyMajorNetWork(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.majorNetwork.name==null) {
                            " " } else{ data.majorNetwork.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //人员清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //人员清册
                                if(data.provideCrewLists!!.isEmpty())
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                            val listData = data.provideCrewLists
                                            mdata.putSerializable("listData7", listData as Serializable)
                                            mdata.putInt("type",7)
                                            (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                                })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //车辆清册
                                if(data.provideTransportMachines!!.isEmpty())
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putInt("type",8)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.provideTransportMachines==null)
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            for(i in data.provideTransportMachines!!)
                            {
                                if(i.carPhotoPath == null)
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                                        intent.putExtra("imagePath", i.carPhotoPath)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                        for(i in data.voltages!!)
                        {
                            var str=""
                            if(i.voltageDegree!=null)
                                str+="${i.voltageDegree} "
                            adapter.mData[5].singleDisplayRightContent=str
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.implementationRanges.name==null) {
                            " " } else{ data.implementationRanges.name }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                //机械清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {  //机械清册
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                        val listData = data.constructionToolLists
                                        mdata.putSerializable("listData9", listData as Serializable)
                                        mdata.putInt("type",9)
                                        (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[8].singleDisplayRightContent=if(data.majorNetwork.validTime==null) {
                            " " } else{ data.majorNetwork.validTime}
                        adapter.mData[9].submitListener = View.OnClickListener {
//                            if(data.contactPhone!=null)
//                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话：")
                                    .setMessage("10086")
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:10086"))
                                        startActivity(intent)
                                    }
                                    .setPositiveButton("确定") { dialog, which ->
                                        dialog.dismiss() }.create()
                                dialog.show()
//                            }
//                            else{
//Toast.makeText(context,"对方未留联系方式",Toast.LENGTH_SHORT).show()
//                            }
                        }
                        //Log.i("data.majorNetwork.name",data.majorNetwork.name)
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            3->{//配网
                adapter = adapterGenerate.supplyTeamDisplay()
                val result = getSupplyDistributionNetWork(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.majorNetwork.name==null) {
                            " " } else{ data.majorNetwork.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //人员清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //人员清册
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putInt("type",7)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //车辆清册
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putInt("type",8)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.provideTransportMachines==null)
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            for(i in data.provideTransportMachines!!)
                            {
                                if(i.carPhotoPath == null)
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                                        intent.putExtra("imagePath", i.carPhotoPath)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                        for(i in data.voltages!!)
                        {
                            var str=""
                            if(i.voltageDegree!=null)
                                str+="${i.voltageDegree} "
                            adapter.mData[5].singleDisplayRightContent=str
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.implementationRanges.name==null) {
                            " " } else{ data.implementationRanges.name }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                //机械清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {  //机械清册
                                if(data.constructionToolLists!!.isEmpty())
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                        val listData = data.constructionToolLists
                                        mdata.putSerializable("listData9", listData as Serializable)
                                        mdata.putInt("type",9)
                                        (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[8].singleDisplayRightContent=if(data.majorNetwork.validTime==null) {
                            " " } else{ data.majorNetwork.validTime}
                        adapter.mData[9].submitListener = View.OnClickListener {
                            //                            if(data.contactPhone!=null)
//                            {
                            var dialog = AlertDialog.Builder(this.context)
                                .setTitle("对方联系电话：")
                                .setMessage("10086")
                                .setNegativeButton("联系对方") { dialog, which ->
                                    val intent = Intent(Intent.ACTION_DIAL)
                                    intent.setData(Uri.parse("tel:10086"))
                                    startActivity(intent)
                                }
                                .setPositiveButton("确定") { dialog, which ->
                                    dialog.dismiss() }.create()
                            dialog.show()
//                            }
//                            else{
//Toast.makeText(context,"对方未留联系方式",Toast.LENGTH_SHORT).show()
//                            }
                        }
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            4->{//变电
                adapter = adapterGenerate.supplyTeamDisplay()
                val result = getSupplyPowerTransformation(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.majorNetwork.name==null) {
                            " " } else{ data.majorNetwork.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //人员清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //人员清册
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putInt("type",7)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //车辆清册
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putInt("type",8)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.provideTransportMachines==null)
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            for(i in data.provideTransportMachines!!)
                            {
                                if(i.carPhotoPath == null)
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                                        intent.putExtra("imagePath", i.carPhotoPath)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                        for(i in data.voltages!!)
                        {
                            var str=""
                            if(i.voltageDegree!=null)
                                str+="${i.voltageDegree} "
                            adapter.mData[5].singleDisplayRightContent=str
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.implementationRanges.name==null) {
                            " " } else{ data.implementationRanges.name }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                //机械清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {  //机械清册
                                if(data.constructionToolLists!!.isEmpty())
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                        val listData = data.constructionToolLists
                                        mdata.putSerializable("listData9", listData as Serializable)
                                        mdata.putInt("type",9)
                                        (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[8].singleDisplayRightContent=if(data.majorNetwork.validTime==null) {
                            " " } else{ data.majorNetwork.validTime}
                        adapter.mData[9].submitListener = View.OnClickListener {
                            //                            if(data.contactPhone!=null)
//                            {
                            var dialog = AlertDialog.Builder(this.context)
                                .setTitle("对方联系电话：")
                                .setMessage("10086")
                                .setNegativeButton("联系对方") { dialog, which ->
                                    val intent = Intent(Intent.ACTION_DIAL)
                                    intent.setData(Uri.parse("tel:10086"))
                                    startActivity(intent)
                                }
                                .setPositiveButton("确定") { dialog, which ->
                                    dialog.dismiss() }.create()
                            dialog.show()
//                            }
//                            else{
//Toast.makeText(context,"对方未留联系方式",Toast.LENGTH_SHORT).show()
//                            }
                        }
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            5->{//测量设计
                adapter = adapterGenerate.supplyTeamDisplay()
                val result = getSupplyMeasureDesign(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.majorNetwork.name==null) {
                            " " } else{ data.majorNetwork.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //人员清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //人员清册
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putInt("type",7)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //车辆清册
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putInt("type",8)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.provideTransportMachines==null)
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            for(i in data.provideTransportMachines!!)
                            {
                                if(i.carPhotoPath == null)
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                                        intent.putExtra("imagePath", i.carPhotoPath)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                        for(i in data.voltages!!)
                        {
                            var str=""
                            if(i.voltageDegree!=null)
                                str+="${i.voltageDegree} "
                            adapter.mData[5].singleDisplayRightContent=str
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.implementationRanges.name==null) {
                            " " } else{ data.implementationRanges.name }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                //机械清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                //机械清册
                                if (data.constructionToolLists!!.isEmpty())
                                    Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show()
                                else {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putInt("type", 9)
                                    (activity as SupplyDisplayActivity).switchFragment(
                                        ProjectListFragment.newInstance(mdata))
                                    }
                            })
                        }
                        adapter.mData[8].singleDisplayRightContent=if(data.majorNetwork.validTime==null) {
                            " " } else{ data.majorNetwork.validTime}
                        adapter.mData[9].submitListener = View.OnClickListener {
                            //                            if(data.contactPhone!=null)
//                            {
                            var dialog = AlertDialog.Builder(this.context)
                                .setTitle("对方联系电话：")
                                .setMessage("10086")
                                .setNegativeButton("联系对方") { dialog, which ->
                                    val intent = Intent(Intent.ACTION_DIAL)
                                    intent.setData(Uri.parse("tel:10086"))
                                    startActivity(intent)
                                }
                                .setPositiveButton("确定") { dialog, which ->
                                    dialog.dismiss() }.create()
                            dialog.show()
//                            }
//                            else{
//Toast.makeText(context,"对方未留联系方式",Toast.LENGTH_SHORT).show()
//                            }
                        }
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            6->{//马帮运输
                adapter = adapterGenerate.supplyTeamDisplayGongTrans()
                val result = getSupplyCaravanTransport(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.caravanTransport.name==null) {
                            " " } else{ data.caravanTransport.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //人员清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //人员清册
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putInt("type",7)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[3].singleDisplayRightContent=if(data.caravanTransport.horseNumber==null) {
                            " " } else{ data.caravanTransport.horseNumber}
                        adapter.mData[4].singleDisplayRightContent=if(data.caravanTransport.validTime==null) {
                            " " } else{ data.caravanTransport.validTime}
                        adapter.mData[5].submitListener = View.OnClickListener {
//                            if(data.contactPhone!=null)
//                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话：")
                                    .setMessage("10086")
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:10086"))
                                        startActivity(intent)
                                    }
                                    .setPositiveButton("确定") { dialog, which ->
                                        dialog.dismiss() }.create()
                                dialog.show()
//                            }
//                            else{
//Toast.makeText(context,"对方未留联系方式",Toast.LENGTH_SHORT).show()
//                            }
                        }
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            7->{//桩基服务
                adapter = adapterGenerate.supplyTeamDisplayPile()
                val result = getSupplyPileFoundation(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.pileFoundation.name==null) {
                            " " } else{ data.pileFoundation.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //人员清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //人员清册
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putInt("type",7)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //车辆清册
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putInt("type",8)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.provideTransportMachines==null)
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            for(i in data.provideTransportMachines!!)
                            {
                                if(i.carPhotoPath == null)
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                                        intent.putExtra("imagePath", i.carPhotoPath)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                        for(i in data.implementationRanges)
                        {
                            var str=""
                            if(i.name!=null)
                                str+="${i.name} "
                            adapter.mData[5].singleDisplayRightContent=str
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.pileFoundation.workDia==null) {
                            " " } else{ data.pileFoundation.workDia }
                        adapter.mData[7].singleDisplayRightContent=if(data.pileFoundation.location==null) {
                            " " } else{ data.pileFoundation.location }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                //机械清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {  //机械清册
                                if(data.constructionToolLists!!.isEmpty())
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                        val listData = data.constructionToolLists
                                        mdata.putSerializable("listData9", listData as Serializable)
                                        mdata.putInt("type",9)
                                        (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[9].singleDisplayRightContent=if(data.pileFoundation.validTime==null) {
                            " " } else{ data.pileFoundation.validTime}
                        adapter.mData[10].submitListener = View.OnClickListener {
//                            if(data.contactPhone!=null)
//                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话：")
                                    .setMessage("10086")
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:10086"))
                                        startActivity(intent)
                                    }
                                    .setPositiveButton("确定") { dialog, which ->
                                        dialog.dismiss() }.create()
                                dialog.show()
//                            }
//                            else{
//Toast.makeText(context,"对方未留联系方式",Toast.LENGTH_SHORT).show()
//                            }
                        }
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            8->{//非开挖
                adapter = adapterGenerate.supplyTeamDisplayTrenchiless()
                val result = getSupplyUnexcavation(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //人员清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //人员清册
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putInt("type",7)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //车辆清册
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putInt("type",8)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.provideTransportMachines==null)
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            for(i in data.provideTransportMachines!!)
                            {
                                if(i.carPhotoPath == null)
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                                        intent.putExtra("imagePath", i.carPhotoPath)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener {
                                //机械清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener {  //机械清册
                                if(data.constructionToolLists!!.isEmpty())
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                        val listData = data.constructionToolLists
                                        mdata.putSerializable("listData9", listData as Serializable)
                                        mdata.putInt("type",9)
                                        (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[7].submitListener = View.OnClickListener {
//                            if(data.contactPhone!=null)
//                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话：")
                                    .setMessage("10086")
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:10086"))
                                        startActivity(intent)
                                    }
                                    .setPositiveButton("确定") { dialog, which ->
                                        dialog.dismiss() }.create()
                                dialog.show()
//                            }
//                            else{
//Toast.makeText(context,"对方未留联系方式",Toast.LENGTH_SHORT).show()
//                            }
                        }
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            9->{//试验调试
                adapter = adapterGenerate.supplyTeamDisplayTestAndDebugging()
                val result = getSupplyTest(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //人员清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //人员清册
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putInt("type",7)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //车辆清册
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putInt("type",8)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.provideTransportMachines==null)
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            for(i in data.provideTransportMachines!!)
                            {
                                if(i.carPhotoPath == null)
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                                        intent.putExtra("imagePath", i.carPhotoPath)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                        for(i in data.voltages!!)
                        {
                            var str=""
                            if(i.voltageDegree!=null)
                                str+="${i.voltageDegree} "
                            adapter.mData[5].singleDisplayRightContent=str
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.testWorkTypes==null) {
                            " " } else{ data.testWorkTypes }
                        adapter.mData[7].singleDisplayRightContent=if(data.operateDegree==null) {
                            " " } else{  data.operateDegree }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                //机械清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {  //机械清册
                                if(data.constructionToolLists!!.isEmpty())
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                        val listData = data.constructionToolLists
                                        mdata.putSerializable("listData9", listData as Serializable)
                                        mdata.putInt("type",9)
                                        (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))

                                }
                            })
                        }
                        adapter.mData[9].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[10].submitListener = View.OnClickListener {
//                            if(data.contactPhone!=null)
//                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话：")
                                    .setMessage("10086")
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:10086"))
                                        startActivity(intent)
                                    }
                                    .setPositiveButton("确定") { dialog, which ->
                                        dialog.dismiss() }.create()
                                dialog.show()
//                            }
//                            else{
//Toast.makeText(context,"对方未留联系方式",Toast.LENGTH_SHORT).show()
//                            }
                        }
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            10->{//跨越架
                adapter = adapterGenerate.supplyTeamDisplayCrossFrame()
                val result = getSupplySpanWoodenSupprt(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //人员清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //人员清册
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putInt("type",7)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //车辆清册
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putInt("type",8)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.provideTransportMachines==null)
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            for(i in data.provideTransportMachines!!)
                            {
                                if(i.carPhotoPath == null)
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                                        intent.putExtra("imagePath", i.carPhotoPath)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener {
                                //机械清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener {  //机械清册
                                if(data.constructionToolLists!!.isEmpty())
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                        val listData = data.constructionToolLists
                                        mdata.putSerializable("listData9", listData as Serializable)
                                        mdata.putInt("type",9)
                                        (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[7].submitListener = View.OnClickListener {
//                            if(data.contactPhone!=null)
//                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话：")
                                    .setMessage("10086")
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:10086"))
                                        startActivity(intent)
                                    }
                                    .setPositiveButton("确定") { dialog, which ->
                                        dialog.dismiss() }.create()
                                dialog.show()
//                            }
//                            else{
//Toast.makeText(context,"对方未留联系方式",Toast.LENGTH_SHORT).show()
//                            }
                        }
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            11->{//运行维护
                adapter = adapterGenerate.supplyTeamDisplayOperationAndMaintenance()
                val result = getSupplyRunningMaintain(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //人员清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //人员清册
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putInt("type",7)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //车辆清册
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putInt("type",8)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.provideTransportMachines==null)
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            for(i in data.provideTransportMachines!!)
                            {
                                if(i.carPhotoPath == null)
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                                        intent.putExtra("imagePath", i.carPhotoPath)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                        for(i in data.voltages!!)
                        {
                            var str=""
                            if(i.voltageDegree!=null)
                                str+="${i.voltageDegree} "
                            adapter.mData[5].singleDisplayRightContent=str
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.implementationRanges==null) {
                            " " } else{  data.implementationRanges }
                        adapter.mData[7].singleDisplayRightContent=if(data.workTerritory==null) {
                            " " } else{  data.workTerritory}
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                //机械清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {  //机械清册
                                if(data.constructionToolLists!!.isEmpty())
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                        val listData = data.constructionToolLists
                                        mdata.putSerializable("listData9", listData as Serializable)
                                        mdata.putInt("type",9)
                                        (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[9].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[10].submitListener = View.OnClickListener {
                            //                            if(data.contactPhone!=null)
//                            {
                            var dialog = AlertDialog.Builder(this.context)
                                .setTitle("对方联系电话：")
                                .setMessage("10086")
                                .setNegativeButton("联系对方") { dialog, which ->
                                    val intent = Intent(Intent.ACTION_DIAL)
                                    intent.setData(Uri.parse("tel:10086"))
                                    startActivity(intent)
                                }
                                .setPositiveButton("确定") { dialog, which ->
                                    dialog.dismiss() }.create()
                            dialog.show()
//                            }
//                            else{
//Toast.makeText(context,"对方未留联系方式",Toast.LENGTH_SHORT).show()
//                            }
                        }
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            12->{//车辆租赁
                adapter = adapterGenerate.supplyTeamDisplayVehicleLeasing()
                val result = getSupplyLeaseCar(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.variety==null) {
                            " " } else{ data.variety }
                        adapter.mData[1].singleDisplayRightContent=if(data.carTable.id==null) {
                            " " } else{ data.carTable.id }
                        adapter.mData[2].singleDisplayRightContent=if(data.carTable.carType==null) {
                            " " } else{ data.carTable.carType }
                        adapter.mData[3].singleDisplayRightContent=if(data.carTable.maxPassengers==null) {
                            " " } else{ data.carTable.maxPassengers }
                        adapter.mData[4].singleDisplayRightContent=if(data.carTable.maxWeight==null) {
                            " " } else{ data.carTable.maxWeight }
                        adapter.mData[5].singleDisplayRightContent=if(data.carTable.construction==null) {
                            " " } else{ data.carTable.construction }
                        adapter.mData[6].singleDisplayRightContent=if(data.carTable.lenghtCar==null) {
                            " " } else{ data.carTable.lenghtCar }
                        adapter.mData[7].singleDisplayRightContent=if(data.carTable.isDriver==null) {
                            " " } else{ data.carTable.isDriver }
                        adapter.mData[8].singleDisplayRightContent=if(data.carTable.isInsurance==null) {
                            " " } else{ data.carTable.isInsurance }
                        adapter.mData[9].singleDisplayRightContent=if(data.money==null || data.salaryUnit==null) {
                            " " } else{ "${data.money} ${data.salaryUnit}"}
                        adapter.mData[11].singleDisplayRightContent=if(data.contact==null) {
                            " " } else{ data.contact }
                        adapter.mData[12].singleDisplayRightContent=if(data.contactPhone==null) {
                            " " } else{ data.contactPhone }
                        adapter.mData[13].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite }
                        if(data.carTable.carPhotoPath==null)
                        {
                            adapter.mData[14].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[14].buttonListener = listOf(View.OnClickListener {
                                  val intent = Intent(activity, GetQRCodeActivity::class.java)
                                     intent.putExtra("imagePath", data.carTable.carPhotoPath)
                                    startActivity(intent)
                                  })
                        }
                        adapter.mData[15].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime }
                        adapter.mData[16].submitListener = View.OnClickListener {
                            if(data.contactPhone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话：")
                                    .setMessage(data.contactPhone)
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:${data.contactPhone}"))
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
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            13->{//工器具租赁
                adapter = adapterGenerate.supplyTeamDisplayEquipmentLeasing()
                val result = getSupplyLeaseConstructionTool(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.leaseConstructionTool.variety==null) {
                            " " } else{ data.leaseConstructionTool.variety }
                        adapter.mData[1].singleDisplayRightContent=if(data.companyCredential.companyName==null) {
                            " " } else{ data.companyCredential.companyName }
                        adapter.mData[2].singleDisplayRightContent=if(data.companyCredential.companyAbbreviation==null) {
                            " " } else{ data.companyCredential.companyAbbreviation }
                        adapter.mData[3].singleDisplayRightContent=if(data.companyCredential.companyAddress==null) {
                            " " } else{ data.companyCredential.companyAddress }
                        adapter.mData[4].singleDisplayRightContent=if(data.companyCredential.legalPersonName==null) {
                            " " } else{ data.companyCredential.legalPersonName }
                        adapter.mData[5].singleDisplayRightContent=if(data.companyCredential.legalPersonPhone==null) {
                            " " } else{ data.companyCredential.legalPersonPhone }
                        if(data.companyCredential.legalPersonIdCardPath==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, GetQRCodeActivity::class.java)
                                intent.putExtra("imagePath", data.companyCredential.legalPersonIdCardPath)
                                startActivity(intent)
                            })
                        }
                        if(data.companyCredential.businessLicensePath==null)
                        {
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, GetQRCodeActivity::class.java)
                                intent.putExtra("imagePath", data.companyCredential.businessLicensePath)
                                startActivity(intent)
                            })
                        }
                            if(data.leaseList==null)
                            {
                                adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                    //工器具
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                })
                            }
                            else{
                                adapter.mData[8].buttonListener = listOf(View.OnClickListener {  //工器具出租清册
                                    if(data.leaseList!!.isEmpty())   Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                    else
                                    {
                                        val listData = data.leaseList
                                        mdata.putInt("type",10)
                                        mdata.putSerializable("listData10",listData as Serializable)
                                        (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                    }
                                })
                            }
                        if(data.leaseConstructionTool.leaseConTractPath==null)
                        {
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, GetQRCodeActivity::class.java)
                                intent.putExtra("imagePath", data.leaseConstructionTool.leaseConTractPath)
                                startActivity(intent)
                            })
                        }
                        adapter.mData[10].singleDisplayRightContent=if(data.leaseConstructionTool.isDistribution==null) {
                            " " } else{ data.leaseConstructionTool.isDistribution }
                        adapter.mData[11].singleDisplayRightContent=if(data.leaseConstructionTool.conveyancePropertyInsurance==null) {
                            " " } else{ data.leaseConstructionTool.conveyancePropertyInsurance }
                        adapter.mData[12].singleDisplayRightContent=if(data.leaseConstructionTool.validTime==null) {
                            " " } else{ data.leaseConstructionTool.validTime }
                        adapter.mData[13].submitListener = View.OnClickListener {
                            if(data.leaseConstructionTool.contactPhone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话：")
                                    .setMessage(data.leaseConstructionTool.contactPhone)
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:${data.leaseConstructionTool.contactPhone}"))
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
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            14->{//设备租赁
                adapter = adapterGenerate.supplyTeamDisplayEquipmentLeasing()
                val result = getSupplyLeaseFacility(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.leaseConstructionTool.variety==null) {
                            " " } else{ data.leaseConstructionTool.variety }
                        adapter.mData[1].singleDisplayRightContent=if(data.companyCredential.companyName==null) {
                            " " } else{ data.companyCredential.companyName }
                        adapter.mData[2].singleDisplayRightContent=if(data.companyCredential.companyAbbreviation==null) {
                            " " } else{ data.companyCredential.companyAbbreviation }
                        adapter.mData[3].singleDisplayRightContent=if(data.companyCredential.companyAddress==null) {
                            " " } else{ data.companyCredential.companyAddress }
                        adapter.mData[4].singleDisplayRightContent=if(data.companyCredential.legalPersonName==null) {
                            " " } else{ data.companyCredential.legalPersonName }
                        adapter.mData[5].singleDisplayRightContent=if(data.companyCredential.legalPersonPhone==null) {
                            " " } else{ data.companyCredential.legalPersonPhone }
                        if(data.companyCredential.legalPersonIdCardPath==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, GetQRCodeActivity::class.java)
                                intent.putExtra("imagePath", data.companyCredential.legalPersonIdCardPath)
                                startActivity(intent)
                            })
                        }
                        if(data.companyCredential.businessLicensePath==null)
                        {
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, GetQRCodeActivity::class.java)
                                intent.putExtra("imagePath", data.companyCredential.businessLicensePath)
                                startActivity(intent)
                            })
                        }
                        if(data.leaseList==null)
                        {
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                //工器具
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {  //工器具出租清册
                                if(data.leaseList!!.isEmpty())   Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.leaseList
                                    mdata.putInt("type",10)
                                    mdata.putSerializable("listData10",listData as Serializable)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.leaseConstructionTool.leaseConTractPath==null)
                        {
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, GetQRCodeActivity::class.java)
                                intent.putExtra("imagePath", data.leaseConstructionTool.leaseConTractPath)
                                startActivity(intent)
                            })
                        }
                        adapter.mData[10].singleDisplayRightContent=if(data.leaseConstructionTool.isDistribution==null) {
                            " " } else{ data.leaseConstructionTool.isDistribution }
                        adapter.mData[11].singleDisplayRightContent=if(data.leaseConstructionTool.conveyancePropertyInsurance==null) {
                            " " } else{ data.leaseConstructionTool.conveyancePropertyInsurance }
                        adapter.mData[12].singleDisplayRightContent=if(data.leaseConstructionTool.validTime==null) {
                            " " } else{ data.leaseConstructionTool.validTime }
                        adapter.mData[13].submitListener = View.OnClickListener {
                            if(data.leaseConstructionTool.contactPhone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话：")
                                    .setMessage(data.leaseConstructionTool.contactPhone)
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:${data.leaseConstructionTool.contactPhone}"))
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
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            15->{//机械租赁
                adapter = adapterGenerate.supplyTeamDisplayEquipmentLeasing()
                val result = getSupplyLeaseMachinery(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.leaseConstructionTool.variety==null) {
                            " " } else{ data.leaseConstructionTool.variety }
                        adapter.mData[1].singleDisplayRightContent=if(data.companyCredential.companyName==null) {
                            " " } else{ data.companyCredential.companyName }
                        adapter.mData[2].singleDisplayRightContent=if(data.companyCredential.companyAbbreviation==null) {
                            " " } else{ data.companyCredential.companyAbbreviation }
                        adapter.mData[3].singleDisplayRightContent=if(data.companyCredential.companyAddress==null) {
                            " " } else{ data.companyCredential.companyAddress }
                        adapter.mData[4].singleDisplayRightContent=if(data.companyCredential.legalPersonName==null) {
                            " " } else{ data.companyCredential.legalPersonName }
                        adapter.mData[5].singleDisplayRightContent=if(data.companyCredential.legalPersonPhone==null) {
                            " " } else{ data.companyCredential.legalPersonPhone }
                        if(data.companyCredential.legalPersonIdCardPath==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, GetQRCodeActivity::class.java)
                                intent.putExtra("imagePath", data.companyCredential.legalPersonIdCardPath)
                                startActivity(intent)
                            })
                        }
                        if(data.companyCredential.businessLicensePath==null)
                        {
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, GetQRCodeActivity::class.java)
                                intent.putExtra("imagePath", data.companyCredential.businessLicensePath)
                                startActivity(intent)
                            })
                        }
                        if(data.leaseList==null)
                        {
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                //工器具
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {  //工器具出租清册
                                if(data.leaseList!!.isEmpty())   Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.leaseList
                                    mdata.putInt("type",10)
                                    mdata.putSerializable("listData10",listData as Serializable)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.leaseConstructionTool.leaseConTractPath==null)
                        {
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, GetQRCodeActivity::class.java)
                                intent.putExtra("imagePath", data.leaseConstructionTool.leaseConTractPath)
                                startActivity(intent)
                            })
                        }
                        adapter.mData[10].singleDisplayRightContent=if(data.leaseConstructionTool.isDistribution==null) {
                            " " } else{ data.leaseConstructionTool.isDistribution }
                        adapter.mData[11].singleDisplayRightContent=if(data.leaseConstructionTool.conveyancePropertyInsurance==null) {
                            " " } else{ data.leaseConstructionTool.conveyancePropertyInsurance }
                        adapter.mData[12].singleDisplayRightContent=if(data.leaseConstructionTool.validTime==null) {
                            " " } else{ data.leaseConstructionTool.validTime }
                        adapter.mData[13].submitListener = View.OnClickListener {
                            if(data.leaseConstructionTool.contactPhone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话：")
                                    .setMessage(data.leaseConstructionTool.contactPhone)
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:${data.leaseConstructionTool.contactPhone}"))
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
                        view.tv_fragment_demand_display_content.adapter = adapter
                        view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            16->{//除资质合作
                adapter = adapterGenerate.supplyTeamDisplayDemandTripartite()
                val result = getSupplyThirdPartyDetail(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                            var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.serveType==null) {
                                " " } else{ data.serveType }
                            adapter.mData[1].singleDisplayRightContent=if(data.companyCredential.companyName==null) {
                                " " } else{ data.companyCredential.companyName }
                            adapter.mData[2].singleDisplayRightContent=if(data.companyCredential.companyAbbreviation==null) {
                                " " } else{ data.companyCredential.companyAbbreviation }
                            adapter.mData[3].singleDisplayRightContent=if(data.companyCredential.companyAddress==null) {
                                " " } else{ data.companyCredential.companyAddress }
                            adapter.mData[4].singleDisplayRightContent=if(data.companyCredential.legalPersonName==null) {
                                " " } else{ data.legalPersonName }
                            adapter.mData[5].singleDisplayRightContent=if(data.companyCredential.legalPersonPhone==null) {
                                " " } else{ data.companyCredential.legalPersonPhone }
                        if(data.companyCredential.legalPersonIdCardPath==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, GetQRCodeActivity::class.java)
                                intent.putExtra("imagePath", data.companyCredential.legalPersonIdCardPath)
                                startActivity(intent)
                            })
                        }
                        if(data.companyCredential.businessLicensePath==null)
                        {
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, GetQRCodeActivity::class.java)
                                intent.putExtra("imagePath", data.companyCredential.businessLicensePath)
                                startActivity(intent)
                            })
                        }
                        if(data.thirdServicesContractPath==null)
                        {
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, GetQRCodeActivity::class.java)
                                intent.putExtra("imagePath", data.thirdServicesContractPath)
                                startActivity(intent)
                            })
                        }
                            adapter.mData[9].singleDisplayRightContent=if(data.contact==null) {
                                " " } else{ data.contact }
                            adapter.mData[10].singleDisplayRightContent=if(data.contactPhone==null) {
                                " " } else{ "联系对方时可见"}
                            adapter.mData[11].singleDisplayRightContent=if(data.validTime==null) {
                                " " } else{ data.validTime }
                            adapter.mData[12].singleDisplayRightContent=if(data.businessScope==null) {
                                " " } else{ data.businessScope }
                            adapter.mData[13].submitListener = View.OnClickListener {
                                if(data.contactPhone!=null)
                                {
                                    var dialog = AlertDialog.Builder(this.context)
                                            .setTitle("对方联系电话：")
                                            .setMessage(data.contactPhone)
                                            .setNegativeButton("联系对方") { dialog, which ->
                                                val intent = Intent(Intent.ACTION_DIAL)
                                                intent.setData(Uri.parse("tel:${data.contactPhone}"))
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
                            view.tv_fragment_demand_display_content.adapter = adapter
                            view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                        },{
                            it.printStackTrace()
                        })
            }
            17->{//资质合作
                adapter = adapterGenerate.supplyTeamDisplayDemandTripartiteCooperation()
                val result = getSupplyThirdPartyDetail(id, ApiConfig.Token, ApiConfig.BasePath).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                            var data=it.message
                            adapter.mData[0].singleDisplayRightContent=if(data.serveType==null) {
                                " " } else{ data.serveType }
                            adapter.mData[1].singleDisplayRightContent=if(data.cooperationObject==null) {
                                " " } else{ data.cooperationObject }
                            adapter.mData[2].singleDisplayRightContent=if(data.companyName==null) {
                                " " } else{ data.companyName }
                            adapter.mData[3].singleDisplayRightContent=if(data.companySite==null) {
                                " " } else{ data.companySite }
                            adapter.mData[4].singleDisplayRightContent=if(data.legalPersonName==null) {
                                " " } else{ data.legalPersonName }
                            adapter.mData[5].singleDisplayRightContent=if(data.cooperationAreas==null) {
                                " " } else{ data.cooperationAreas }
                            adapter.mData[6].singleDisplayRightContent=if(data.qualificationCondition==null) {
                                " " } else{ data.qualificationCondition }
                            adapter.mData[7].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                                " " } else{ data.issuerBelongSite }
                            adapter.mData[9].singleDisplayRightContent=if(data.contact==null) {
                                " " } else{ data.contact }
                            adapter.mData[10].singleDisplayRightContent=if(data.contactPhone==null) {
                                " " } else{ "联系对方时可见"}
                            adapter.mData[11].submitListener = View.OnClickListener {
                                if(data.contactPhone!=null)
                                {
                                    var dialog = AlertDialog.Builder(this.context)
                                            .setTitle("对方联系电话：")
                                            .setMessage(data.contactPhone)
                                            .setNegativeButton("联系对方") { dialog, which ->
                                                val intent = Intent(Intent.ACTION_DIAL)
                                                intent.setData(Uri.parse("tel:${data.contactPhone}"))
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
                            view.tv_fragment_demand_display_content.adapter = adapter
                            view.tv_fragment_demand_display_content.layoutManager = LinearLayoutManager(view.context)
                        },{
                            it.printStackTrace()
                        })
            }

        }

    }
}