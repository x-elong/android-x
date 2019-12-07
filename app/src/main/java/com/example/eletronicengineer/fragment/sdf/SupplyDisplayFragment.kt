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
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.getSupplyMajorNetWork
import com.example.eletronicengineer.utils.getSupplyPersonDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_supply_display.view.*
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
        val view = inflater.inflate(R.layout.fragment_supply_display, container, false)
        id=arguments!!.getString("id")
        type = arguments!!.getInt("type")
        initFragment(view)
        return view
    }

    private fun initFragment(view: View) {
        view.tv_supply_display_back.setOnClickListener {
            activity!!.finish()
        }
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = view.context
        adapterGenerate.activity = activity as AppCompatActivity
        lateinit var adapter: RecyclerviewAdapter
        when (type) {
            Constants.FragmentType.PERSONAL_TYPE.ordinal -> {
                adapter = adapterGenerate.supplyIndividualDisplay()
                val result = getSupplyPersonDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.issuerWorkType==null) {
                            " " } else{ data.issuerWorkType }
                        adapter.mData[1].singleDisplayRightContent=if(data.issuerWorkerKind==null) {
                            " " } else{ data.issuerWorkerKind }
                        adapter.mData[2].singleDisplayRightContent=if(data.contact==null) {
                            " " } else{ data.contact}
                       if(data.sex==null) {
                           adapter.mData[3].singleDisplayRightContent=" " } else if(data.sex=="1"){  adapter.mData[3].singleDisplayRightContent="男"}
                        else if(data.sex=="0"){ adapter.mData[3].singleDisplayRightContent="女"}
                        if(data.age=="10"){
                            adapter.mData[4].singleDisplayRightContent=""
                        }else{ adapter.mData[4].singleDisplayRightContent=data.age }
                        adapter.mData[5].singleDisplayRightContent=if(data.workExperience==null) {
                            " " } else{ data.workExperience}
                        if(data.salaryUnit=="面议"){
                            adapter.mData[6].singleDisplayRightContent= data.salaryUnit
                        }else {
                            adapter.mData[6].singleDisplayRightContent= "${data.workMoney} ${data.salaryUnit}"
                        }
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
                        adapter.mData[10].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite}
                        adapter.mData[11].singleDisplayRightContent=if(data.remark==null) {
                            " " } else{ data.remark}
                        view.button_supply.setOnClickListener {
                            if(data.contactPhone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal->{//主网
                adapter = adapterGenerate.supplyTeamDisplay()
                val result = getSupplyMajorNetWork(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.majorNetwork.name==null) {
                            " " } else{ data.majorNetwork.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        when {
                            data.majorNetwork.isCar=="true" -> adapter.mData[2].singleDisplayRightContent="提供"
                            data.majorNetwork.isCar=="false" -> adapter.mData[2].singleDisplayRightContent="不提供"
                            else -> { adapter.mData[2].singleDisplayRightContent=" "}
                        }
                        when {
                            data.majorNetwork.isConstructionTool=="true" -> adapter.mData[3].singleDisplayRightContent="提供"
                            data.majorNetwork.isConstructionTool=="false" -> adapter.mData[3].singleDisplayRightContent="不提供"
                            else -> { adapter.mData[3].singleDisplayRightContent=" "}
                        }
                        if(data.provideCrewLists==null)
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                            val listData = data.provideCrewLists
                                            mdata.putSerializable("listData7", listData as Serializable)
                                            mdata.putString("type","供应人员清册查看")
                                            (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                                })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.provideTransportMachines==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
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
                                    adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                                        intent.putExtra("imagePath", i.carPhotoPath)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                        var str=""
                        for(i in data.voltages!!)
                        {
                            if(str!="")
                                str+="、"
                                str+="${i.voltageDegree}"
                        }
                        adapter.mData[7].singleDisplayRightContent=str
                        adapter.mData[8].singleDisplayRightContent=if(data.implementationRanges.name==null) {
                            " " } else{ data.implementationRanges.name }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                        val listData = data.constructionToolLists
                                        mdata.putSerializable("listData9", listData as Serializable)
                                         mdata.putString("type","供应工器具清册查看")
                                        (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[10].singleDisplayRightContent=if(data.majorNetwork.validTime==null) {
                            " " } else{ data.majorNetwork.validTime}
                        adapter.mData[11].singleDisplayRightContent=if(data.majorNetwork.issuerBelongSite==null) {
                            " " } else{ data.majorNetwork.issuerBelongSite}
                        adapter.mData[12].singleDisplayRightContent=if(data.majorNetwork.issuerName==null) {
                            " " } else{ data.majorNetwork.issuerName}
                        adapter.mData[13].singleDisplayRightContent=if(data.majorNetwork.phone==null) {
                            " " } else{ data.majorNetwork.phone}
                        view.button_supply.setOnClickListener {
                            if(data.majorNetwork.phone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
                                    .setMessage(data.majorNetwork.phone)
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:10086"))
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal->{//配网
                adapter = adapterGenerate.supplyTeamDisplay()
                val result = getSupplyDistributionNetWork(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.distribuionNetwork.name==null) {
                            " " } else{ data.distribuionNetwork.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        when {
                            data.distribuionNetwork.isCar=="true" -> adapter.mData[2].singleDisplayRightContent="提供"
                            data.distribuionNetwork.isCar=="false" -> adapter.mData[2].singleDisplayRightContent="不提供"
                            else -> { adapter.mData[2].singleDisplayRightContent=" "}
                        }
                        when {
                            data.distribuionNetwork.isConstructionTool=="true" -> adapter.mData[3].singleDisplayRightContent="提供"
                            data.distribuionNetwork.isConstructionTool=="false" -> adapter.mData[3].singleDisplayRightContent="不提供"
                            else -> { adapter.mData[3].singleDisplayRightContent=" "}
                        }
                        if(data.provideCrewLists==null)
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.provideTransportMachines==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
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
                                    adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                                        intent.putExtra("imagePath", i.carPhotoPath)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                        var str=""
                        for(i in data.voltages!!)
                        {
                            if(str!="")
                                str+="、"
                            str+="${i.voltageDegree}"
                        }
                        adapter.mData[7].singleDisplayRightContent=str
                        adapter.mData[8].singleDisplayRightContent=if(data.implementationRanges.name==null) {
                            " " } else{ data.implementationRanges.name }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putString("type","供应工器具清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[10].singleDisplayRightContent=if(data.distribuionNetwork.validTime==null) {
                            " " } else{ data.distribuionNetwork.validTime}
                        adapter.mData[11].singleDisplayRightContent=if(data.distribuionNetwork.issuerBelongSite==null) {
                            " " } else{ data.distribuionNetwork.issuerBelongSite}
                        adapter.mData[12].singleDisplayRightContent=if(data.distribuionNetwork.issuerName==null) {
                            " " } else{ data.distribuionNetwork.issuerName}
                        adapter.mData[13].singleDisplayRightContent=if(data.distribuionNetwork.phone==null) {
                            " " } else{ data.distribuionNetwork.phone}
                        view.button_supply.setOnClickListener {
                            if(data.distribuionNetwork.phone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
                                    .setMessage(data.distribuionNetwork.phone)
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:10086"))
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal->{//变电
                adapter = adapterGenerate.supplyTeamDisplay()
                val result = getSupplyPowerTransformation(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.powerTransformation.name==null) {
                            " " } else{ data.powerTransformation.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        when {
                            data.powerTransformation.isCar=="true" -> adapter.mData[2].singleDisplayRightContent="提供"
                            data.powerTransformation.isCar=="false" -> adapter.mData[2].singleDisplayRightContent="不提供"
                            else -> { adapter.mData[2].singleDisplayRightContent=" "}
                        }
                        when {
                            data.powerTransformation.isConstructionTool=="true" -> adapter.mData[3].singleDisplayRightContent="提供"
                            data.powerTransformation.isConstructionTool=="false" -> adapter.mData[3].singleDisplayRightContent="不提供"
                            else -> { adapter.mData[3].singleDisplayRightContent=" "}
                        }
                        if(data.provideCrewLists==null)
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.provideTransportMachines==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
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
                                    adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                                        intent.putExtra("imagePath", i.carPhotoPath)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                        var str=""
                        for(i in data.voltages!!)
                        {
                            if(str!="")
                                str+="、"
                            str+="${i.voltageDegree}"
                        }
                        adapter.mData[7].singleDisplayRightContent=str
                        adapter.mData[8].singleDisplayRightContent=if(data.implementationRanges.name==null) {
                            " " } else{ data.implementationRanges.name }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putString("type","供应工器具清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[10].singleDisplayRightContent=if(data.powerTransformation.validTime==null) {
                            " " } else{ data.powerTransformation.validTime}
                        adapter.mData[11].singleDisplayRightContent=if(data.powerTransformation.issuerBelongSite==null) {
                            " " } else{ data.powerTransformation.issuerBelongSite}
                        adapter.mData[12].singleDisplayRightContent=if(data.powerTransformation.issuerName==null) {
                            " " } else{ data.powerTransformation.issuerName}
                        adapter.mData[13].singleDisplayRightContent=if(data.powerTransformation.phone==null) {
                            " " } else{ data.powerTransformation.phone}
                        view.button_supply.setOnClickListener {
                            if(data.powerTransformation.phone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
                                    .setMessage(data.powerTransformation.phone)
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:10086"))
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal->{//测量设计
                adapter = adapterGenerate.supplyTeamDisplay()
                val result = getSupplyMeasureDesign(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.measureDesign.name==null) {
                            " " } else{ data.measureDesign.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        when {
                            data.measureDesign.isCar=="true" -> adapter.mData[2].singleDisplayRightContent="提供"
                            data.measureDesign.isCar=="false" -> adapter.mData[2].singleDisplayRightContent="不提供"
                            else -> { adapter.mData[2].singleDisplayRightContent=" "}
                        }
                        when {
                            data.measureDesign.isConstructionTool=="true" -> adapter.mData[3].singleDisplayRightContent="提供"
                            data.measureDesign.isConstructionTool=="false" -> adapter.mData[3].singleDisplayRightContent="不提供"
                            else -> { adapter.mData[3].singleDisplayRightContent=" "}
                        }
                        if(data.provideCrewLists==null)
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.provideTransportMachines==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
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
                                    adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                                    })
                                }
                                else
                                {
                                    adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
                                        intent.putExtra("imagePath", i.carPhotoPath)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                        var str=""
                        for(i in data.voltages!!)
                        {
                            if(str!="")
                                str+="、"
                            str+="${i.voltageDegree}"
                        }
                        adapter.mData[7].singleDisplayRightContent=str
                        adapter.mData[8].singleDisplayRightContent=if(data.implementationRanges.name==null) {
                            " " } else{ data.implementationRanges.name }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[9].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putString("type","供应工器具清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[10].singleDisplayRightContent=if(data.measureDesign.validTime==null) {
                            " " } else{ data.measureDesign.validTime}
                        adapter.mData[11].singleDisplayRightContent=if(data.measureDesign.issuerBelongSite==null) {
                            " " } else{ data.measureDesign.issuerBelongSite}
                        adapter.mData[12].singleDisplayRightContent=if(data.measureDesign.issuerName==null) {
                            " " } else{ data.measureDesign.issuerName}
                        adapter.mData[13].singleDisplayRightContent=if(data.measureDesign.phone==null) {
                            " " } else{ data.measureDesign.phone}
                        view.button_supply.setOnClickListener {
                            if(data.measureDesign.phone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
                                    .setMessage(data.measureDesign.phone)
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:10086"))
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal->{//马帮运输
                adapter = adapterGenerate.supplyTeamDisplayGongTrans()
                val result = getSupplyCaravanTransport(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.caravanTransport.name==null) {
                            " " } else{ data.caravanTransport.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[3].singleDisplayRightContent=if(data.caravanTransport.horseNumber==null) {
                            " " } else{ data.caravanTransport.horseNumber}
                        adapter.mData[4].singleDisplayRightContent=if(data.caravanTransport.validTime==null) {
                            " " } else{ data.caravanTransport.validTime}
                        adapter.mData[5].singleDisplayRightContent=if(data.caravanTransport.issuerBelongSite==null) {
                            " " } else{ data.caravanTransport.issuerBelongSite}
                        view.button_supply.setOnClickListener{
//                            if(data.contactPhone!=null)
//                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal->{//桩基服务
                adapter = adapterGenerate.supplyTeamDisplayPile()
                val result = getSupplyPileFoundation(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.pileFoundation.name==null) {
                            " " } else{ data.pileFoundation.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
//                        if(data.provideTransportMachines==null)
//                        {
//                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
//                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
//                            })
//                        }
//                        else
//                        {
//                            //显示图片
//                            for(i in data.provideTransportMachines!!)
//                            {
//                                if(i.carPhotoPath == null)
//                                {
//                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
//                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
//                                    })
//                                }
//                                else
//                                {
//                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
//                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
//                                        intent.putExtra("imagePath", i.carPhotoPath)
//                                        startActivity(intent)
//                                    })
//                                }
//                            }
//                        }
                        val implementationRanges = data.implementationRanges
                        if(implementationRanges!=null)
                            adapter.mData[4].singleDisplayRightContent=implementationRanges.name
                        adapter.mData[5].singleDisplayRightContent=if(data.pileFoundation.workDia==null) {
                            " " } else{ data.pileFoundation.workDia }
                        adapter.mData[6].singleDisplayRightContent=if(data.pileFoundation.location==null) {
                            " " } else{ data.pileFoundation.location }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putString("type","供应工器具清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[8].singleDisplayRightContent=if(data.pileFoundation.validTime==null) {
                            " " } else{ data.pileFoundation.validTime}
                        adapter.mData[9].singleDisplayRightContent=if(data.pileFoundation.issuerBelongSite==null) {
                            " " } else{ data.pileFoundation.issuerBelongSite}
                        view.button_supply.setOnClickListener{
//                            if(data.contactPhone!=null)
//                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal->{//非开挖
                adapter = adapterGenerate.supplyTeamDisplayTrenchiless()
                val result = getSupplyUnexcavation(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
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
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putString("type","供应工器具清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[7].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite}
                        view.button_supply.setOnClickListener {
//                            if(data.contactPhone!=null)
//                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal->{//试验调试
                adapter = adapterGenerate.supplyTeamDisplayTestAndDebugging()
                val result = getSupplyTest(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
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
                        var str=""
                        for(i in data.voltages!!)
                        {
                            if(str!="")
                                str+="、"
                            str+="${i.voltageDegree}"
                        }
                        adapter.mData[5].singleDisplayRightContent=str
                        adapter.mData[6].singleDisplayRightContent=if(data.testWorkTypes==null) {
                            " " } else{ data.testWorkTypes }
                        adapter.mData[7].singleDisplayRightContent=if(data.operateDegree==null) {
                            " " } else{  data.operateDegree }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putString("type","供应工器具清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[9].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[10].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite}
                        view.button_supply.setOnClickListener{
//                            if(data.contactPhone!=null)
//                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal->{//跨越架
                adapter = adapterGenerate.supplyTeamDisplayCrossFrame()
                val result = getSupplySpanWoodenSupprt(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
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
                                    mdata.putString("type","供应运输清册查看")
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
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[5].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putString("type","供应工器具清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[7].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite}
                        view.button_supply.setOnClickListener{
//                            if(data.contactPhone!=null)
//                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal->{//运行维护
                adapter = adapterGenerate.supplyTeamDisplayOperationAndMaintenance()
                val result = getSupplyRunningMaintain(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name }
                        adapter.mData[1].singleDisplayRightContent=" "
                        if(data.provideCrewLists==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
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
//                        if(data.provideTransportMachines==null)
//                        {
//                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
//                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
//                            })
//                        }
//                        else
//                        {
//                            //显示图片
//                            for(i in data.provideTransportMachines!!)
//                            {
//                                if(i.carPhotoPath == null)
//                                {
//                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
//                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
//                                    })
//                                }
//                                else
//                                {
//                                    adapter.mData[4].buttonListener = listOf(View.OnClickListener {
//                                        val intent = Intent(activity, GetQRCodeActivity::class.java)
//                                        intent.putExtra("imagePath", i.carPhotoPath)
//                                        startActivity(intent)
//                                    })
//                                }
//                            }
//                        }
                        if(data.voltages==null){
                            adapter.mData[4].singleDisplayRightContent=""
                        }else{
                            var str=""
                            for(i in data.voltages!!)
                            {
                                if(str!="")
                                    str+="、"
                                str+="${i.voltageDegree}"
                            }
                            adapter.mData[4].singleDisplayRightContent=str
                        }
                        adapter.mData[5].singleDisplayRightContent=if(data.implementationRanges==null) {
                            " " } else{  data.implementationRanges }
                        adapter.mData[6].singleDisplayRightContent=if(data.workTerritory==null) {
                            " " } else{  data.workTerritory}
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putString("type","供应工器具清册查看")
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        adapter.mData[8].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[9].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite}
                        view.button_supply.setOnClickListener{
                            //                            if(data.contactPhone!=null)
//                            {
                            var dialog = AlertDialog.Builder(this.context)
                                .setTitle("对方联系电话:")
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal->{//车辆租赁
                adapter = adapterGenerate.supplyTeamDisplayVehicleLeasing()
                val result = getSupplyLeaseCar(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.variety==null) {
                            " " } else{ data.variety }
                        adapter.mData[1].singleDisplayRightContent=if(data.carTable.carNumber==null) {
                            " " } else{ data.carTable.carNumber }
                        adapter.mData[2].singleDisplayRightContent=if(data.carTable.carType==null) {
                            " " } else{ data.carTable.carType }
                        adapter.mData[3].singleDisplayRightContent=if(data.carTable.maxPassengers==null) {
                            " " } else{ data.carTable.maxPassengers }
                        adapter.mData[4].singleDisplayRightContent=if(data.carTable.maxWeight==null) {
                            " " } else{ data.carTable.maxWeight }
                        when(data.carTable.construction){
                            "1"->{ adapter.mData[5].singleDisplayRightContent="箱式"}
                            "0"->{adapter.mData[5].singleDisplayRightContent="敞篷"}
                            else->{adapter.mData[5].singleDisplayRightContent=""}
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.carTable.lenghtCar==null) {
                            " " } else{ data.carTable.lenghtCar }
                        when(data.carTable.isDriver){
                            "true"->{ adapter.mData[7].singleDisplayRightContent="是"}
                            "false"->{adapter.mData[7].singleDisplayRightContent="否"}
                            else->{adapter.mData[7].singleDisplayRightContent=""}
                        }
                        when(data.carTable.isInsurance){
                            "true"->{ adapter.mData[8].singleDisplayRightContent="在保"}
                            "false"->{adapter.mData[8].singleDisplayRightContent="脱保"}
                            else->{adapter.mData[8].singleDisplayRightContent=""}
                        }
                        if(data.salaryUnit=="面议"){
                            adapter.mData[9].singleDisplayRightContent= data.salaryUnit
                        }else {
                            adapter.mData[9].singleDisplayRightContent= "${data.money} ${data.salaryUnit}"
                        }
                        adapter.mData[11].singleDisplayRightContent=if(data.contact==null) {
                            " " } else{ data.contact }
                        adapter.mData[12].singleDisplayRightContent=if(data.contactPhone==null) {
                            " " } else{ data.contactPhone }
                        adapter.mData[13].singleDisplayRightContent=if(data.site==null) {
                            " " } else{ data.site }
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
                        adapter.mData[16].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite }
                        adapter.mData[17].singleDisplayRightContent=if(data.comment==null) {
                            " " } else{ data.comment }
                        view.button_supply.setOnClickListener{
                            if(data.contactPhone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.TOOL_LEASING_TYPE.ordinal->{//工器具租赁
                        adapter = adapterGenerate.supplyTeamDisplayEquipmentLeasing("工器具租赁")
                val result = getSupplyLeaseConstructionTool(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
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
                                        mdata.putString("type","工器具租赁清册查看")
                                        mdata.putString("type","设备租赁清册查看")
                                        mdata.putString("type","机械租赁清册查看")
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
                        adapter.mData[13].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite }
                        view.button_supply.setOnClickListener{
                            if(data.leaseConstructionTool.contactPhone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal->{//设备租赁
                adapter = adapterGenerate.supplyTeamDisplayEquipmentLeasing("设备租赁")
                val result = getSupplyLeaseFacility(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.leaseFacility.variety==null) {
                            " " } else{ data.leaseFacility.variety }
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
                                //设备租赁清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {  //设备租赁清册查看
                                if(data.leaseList!!.isEmpty())   Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.leaseList
                                    mdata.putString("type","设备租赁清册查看")
                                    mdata.putSerializable("listData10",listData as Serializable)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.leaseFacility.leaseConTractPath==null)
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
                                intent.putExtra("imagePath", data.leaseFacility.leaseConTractPath)
                                startActivity(intent)
                            })
                        }
                        adapter.mData[10].singleDisplayRightContent=if(data.leaseFacility.isDistribution==null) {
                            " " } else{ data.leaseFacility.isDistribution }
                        adapter.mData[11].singleDisplayRightContent=if(data.leaseFacility.conveyancePropertyInsurance==null) {
                            " " } else{ data.leaseFacility.conveyancePropertyInsurance }
                        adapter.mData[12].singleDisplayRightContent=if(data.leaseFacility.validTime==null) {
                            " " } else{ data.leaseFacility.validTime }
                        adapter.mData[13].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite }
                        view.button_supply.setOnClickListener{
                            if(data.leaseFacility.contactPhone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
                                    .setMessage(data.leaseFacility.contactPhone)
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:${data.leaseFacility.contactPhone}"))
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal->{//机械租赁
                adapter = adapterGenerate.supplyTeamDisplayEquipmentLeasing("机械租赁")
                val result = getSupplyLeaseMachinery(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.leaseMachinery.variety==null) {
                            " " } else{ data.leaseMachinery.variety }
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
                                //机械租赁清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {  //机械租赁清册查看
                                if(data.leaseList!!.isEmpty())   Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.leaseList
                                    mdata.putString("type","机械租赁清册查看")
                                    mdata.putSerializable("listData10",listData as Serializable)
                                    (activity as SupplyDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                        }
                        if(data.leaseMachinery.leaseConTractPath==null)
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
                                intent.putExtra("imagePath", data.leaseMachinery.leaseConTractPath)
                                startActivity(intent)
                            })
                        }
                        adapter.mData[10].singleDisplayRightContent=if(data.leaseMachinery.isDistribution==null) {
                            " " } else{ data.leaseMachinery.isDistribution }
                        adapter.mData[11].singleDisplayRightContent=if(data.leaseMachinery.conveyancePropertyInsurance==null) {
                            " " } else{ data.leaseMachinery.conveyancePropertyInsurance }
                        adapter.mData[12].singleDisplayRightContent=if(data.leaseMachinery.validTime==null) {
                            " " } else{ data.leaseMachinery.validTime }
                        adapter.mData[13].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite }
                        view.button_supply.setOnClickListener{
                            if(data.leaseMachinery.contactPhone!=null)
                            {
                                var dialog = AlertDialog.Builder(this.context)
                                    .setTitle("对方联系电话:")
                                    .setMessage(data.leaseMachinery.contactPhone)
                                    .setNegativeButton("联系对方") { dialog, which ->
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:${data.leaseMachinery.contactPhone}"))
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
                        view.rv_supply_display_content.adapter = adapter
                        view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                    },{
                        it.printStackTrace()
                    })
            }
            Constants.FragmentType.TRIPARTITE_TYPE.ordinal->{//除资质合作
                adapter = adapterGenerate.supplyTeamDisplayDemandTripartite()
                val result = getSupplyThirdPartyDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
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
                           adapter.mData[13].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite }
                           view.button_supply.setOnClickListener{
                                if(data.contactPhone!=null)
                                {
                                    var dialog = AlertDialog.Builder(this.context)
                                            .setTitle("对方联系电话:")
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
                            view.rv_supply_display_content.adapter = adapter
                            view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                        },{
                            it.printStackTrace()
                        })
            }
            Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal->{//资质合作
                adapter = adapterGenerate.supplyTeamDisplayDemandTripartiteCooperation()
                val result = getSupplyThirdPartyDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
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
                                            .setTitle("对方联系电话:")
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
                            view.rv_supply_display_content.adapter = adapter
                            view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
                        },{
                            it.printStackTrace()
                        })
            }

        }

    }
}