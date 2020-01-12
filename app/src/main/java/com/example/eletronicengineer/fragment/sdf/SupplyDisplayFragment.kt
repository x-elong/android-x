package com.example.eletronicengineer.fragment.sdf

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
import com.example.eletronicengineer.activity.ImageDisplayActivity
import com.example.eletronicengineer.activity.SupplyDisplayActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.fragment.sdf.ProjectListFragment
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
                        if(data.salaryUnit=="面议"||data.workMoney=="-1.00"){
                            adapter.mData[6].singleDisplayRightContent= "面议"
                        }else {
                            adapter.mData[6].singleDisplayRightContent= "${data.workMoney} ${data.salaryUnit}"
                        }
                        adapter.mData[7].singleDisplayRightContent=if(data.contactPhone==null) {
                            " " } else{ data.contactPhone}
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
                                val bundle = Bundle()
                                bundle.putString("imagePath", data.certificatePath)
                                bundle.putString("title",adapter.mData[8].singleDisplayRightTitle)
                                FragmentHelper.switchFragment(activity!!,ImageDisplayFragment.newInstance(bundle),R.id.frame_display_supply,"")
//                                val intent = Intent(activity, ImageDisplayActivity::class.java)
//                                intent.putExtra("imagePath", data.certificatePath)
//                                startActivity(intent)
                            })
                        }
                        adapter.mData[9].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[10].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite}
                        adapter.mData[11].textAreaContent=if(data.remark==null) {
                            " " } else{ data.remark}
                        view.button_supply.setOnClickListener {
                            if(data.contactPhone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getPersonalIssuePhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                      val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
                        if(data.provideCrewLists==null)
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                            val listData = data.provideCrewLists
                                            mdata.putSerializable("listData7", listData as Serializable)
                                            mdata.putString("type","供应人员清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                                })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
//                        if(data.provideTransportMachines==null)
//                        {
//                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {
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
//                                    adapter.mData[3].buttonListener = listOf(View.OnClickListener {
//                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
//                                    })
//                                }
//                                else
//                                {
//                                    adapter.mData[3].buttonListener = listOf(View.OnClickListener {
//                                        val intent = Intent(activity, ImageDisplayActivity::class.java)
//                                        intent.putExtra("imagePath", i.carPhotoPath)
//                                        startActivity(intent)
//                                    })
//                                }
//                            }
//                        }
                        var str=""
                        for(i in data.voltages!!)
                        {
                            if(str!="")
                                str+="、"
                                str+="${i.voltageDegree}"
                        }
                        adapter.mData[3].singleDisplayRightContent=str
                        adapter.mData[4].singleDisplayRightContent=if(data.implementationRanges.name==null) {
                            " " } else{ data.implementationRanges.name }
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
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")

                                }
                            })
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.majorNetwork.validTime==null) {
                            " " } else{ data.majorNetwork.validTime}
                        adapter.mData[7].singleDisplayRightContent=if(data.majorNetwork.issuerBelongSite==null) {
                            " " } else{ data.majorNetwork.issuerBelongSite}
                        adapter.mData[8].singleDisplayRightContent=if(data.majorNetwork.issuerName==null) {
                            " " } else{ data.majorNetwork.issuerName}
                        adapter.mData[9].singleDisplayRightContent=if(data.majorNetwork.phone==null) {
                            " " } else{ data.majorNetwork.phone}
                        adapter.mData[10].textAreaContent=if(data.majorNetwork.remark==null) {
                            " " } else{ data.majorNetwork.remark}
                        view.button_supply.setOnClickListener {
                            if(data.majorNetwork.phone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getMajorNetworkPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
//                        adapter.mData[1].singleDisplayRightContent=" "
//                        when {
//                            data.distribuionNetwork.isCar=="true" -> adapter.mData[2].singleDisplayRightContent="提供"
//                            data.distribuionNetwork.isCar=="false" -> adapter.mData[2].singleDisplayRightContent="不提供"
//                            else -> { adapter.mData[2].singleDisplayRightContent=" "}
//                        }
//                        when {
//                            data.distribuionNetwork.isConstructionTool=="true" -> adapter.mData[3].singleDisplayRightContent="提供"
//                            data.distribuionNetwork.isConstructionTool=="false" -> adapter.mData[3].singleDisplayRightContent="不提供"
//                            else -> { adapter.mData[3].singleDisplayRightContent=" "}
//                        }
                        if(data.provideCrewLists==null)
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
//                        if(data.provideTransportMachines==null)
//                        {
//                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
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
//                                    adapter.mData[6].buttonListener = listOf(View.OnClickListener {
//                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
//                                    })
//                                }
//                                else
//                                {
//                                    adapter.mData[6].buttonListener = listOf(View.OnClickListener {
//                                        val intent = Intent(activity, ImageDisplayActivity::class.java)
//                                        intent.putExtra("imagePath", i.carPhotoPath)
//                                        startActivity(intent)
//                                    })
//                                }
//                            }
//                        }
                        var str=""
                        for(i in data.voltages!!)
                        {
                            if(str!="")
                                str+="、"
                            str+="${i.voltageDegree}"
                        }
                        adapter.mData[3].singleDisplayRightContent=str
                        adapter.mData[4].singleDisplayRightContent=if(data.implementationRanges.name==null) {
                            " " } else{ data.implementationRanges.name }
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
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.distribuionNetwork.validTime==null) {
                            " " } else{ data.distribuionNetwork.validTime}
                        adapter.mData[7].singleDisplayRightContent=if(data.distribuionNetwork.issuerBelongSite==null) {
                            " " } else{ data.distribuionNetwork.issuerBelongSite}
                        adapter.mData[8].singleDisplayRightContent=if(data.distribuionNetwork.issuerName==null) {
                            " " } else{ data.distribuionNetwork.issuerName}
                        adapter.mData[9].singleDisplayRightContent=if(data.distribuionNetwork.phone==null) {
                            " " } else{ data.distribuionNetwork.phone}
                        adapter.mData[10].textAreaContent=if(data.distribuionNetwork.remark==null) {
                            " " } else{ data.distribuionNetwork.remark}
                        view.button_supply.setOnClickListener {
                            if(data.distribuionNetwork.phone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getDistribuionNetworkPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
                        if(data.provideCrewLists==null)
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
//                        if(data.provideTransportMachines==null)
//                        {
//                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
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
//                                    adapter.mData[6].buttonListener = listOf(View.OnClickListener {
//                                        Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
//                                    })
//                                }
//                                else
//                                {
//                                    adapter.mData[6].buttonListener = listOf(View.OnClickListener {
//                                        val intent = Intent(activity, ImageDisplayActivity::class.java)
//                                        intent.putExtra("imagePath", i.carPhotoPath)
//                                        startActivity(intent)
//                                    })
//                                }
//                            }
//                        }
                        var str=""
                        for(i in data.voltages!!)
                        {
                            if(str!="")
                                str+="、"
                            str+="${i.voltageDegree}"
                        }
                        adapter.mData[3].singleDisplayRightContent=str
                        adapter.mData[4].singleDisplayRightContent=if(data.implementationRanges.name==null) {
                            " " } else{ data.implementationRanges.name }
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
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.powerTransformation.validTime==null) {
                            " " } else{ data.powerTransformation.validTime}
                        adapter.mData[7].singleDisplayRightContent=if(data.powerTransformation.issuerBelongSite==null) {
                            " " } else{ data.powerTransformation.issuerBelongSite}
                        adapter.mData[8].singleDisplayRightContent=if(data.powerTransformation.issuerName==null) {
                            " " } else{ data.powerTransformation.issuerName}
                        adapter.mData[9].singleDisplayRightContent=if(data.powerTransformation.phone==null) {
                            " " } else{ data.powerTransformation.phone}
                        adapter.mData[10].textAreaContent=if(data.powerTransformation.remark==null) {
                            " " } else{ data.powerTransformation.remark}
                        view.button_supply.setOnClickListener {
                            if(data.powerTransformation.phone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getPowerTransformationPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
//                        adapter.mData[1].singleDisplayRightContent=" "
//                        when {
//                            data.measureDesign.isCar=="true" -> adapter.mData[2].singleDisplayRightContent="提供"
//                            data.measureDesign.isCar=="false" -> adapter.mData[2].singleDisplayRightContent="不提供"
//                            else -> { adapter.mData[2].singleDisplayRightContent=" "}
//                        }
//                        when {
//                            data.measureDesign.isConstructionTool=="true" -> adapter.mData[3].singleDisplayRightContent="提供"
//                            data.measureDesign.isConstructionTool=="false" -> adapter.mData[3].singleDisplayRightContent="不提供"
//                            else -> { adapter.mData[3].singleDisplayRightContent=" "}
//                        }
                        if(data.provideCrewLists==null)
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }

                        var str=""
                        for(i in data.voltages!!)
                        {
                            if(str!="")
                                str+="、"
                            str+="${i.voltageDegree}"
                        }
                        adapter.mData[3].singleDisplayRightContent=str
                        adapter.mData[4].singleDisplayRightContent=if(data.implementationRanges.name==null) {
                            " " } else{ data.implementationRanges.name }
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
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
                        adapter.mData[6].singleDisplayRightContent=if(data.measureDesign.validTime==null) {
                            " " } else{ data.measureDesign.validTime}
                        adapter.mData[7].singleDisplayRightContent=if(data.measureDesign.issuerBelongSite==null) {
                            " " } else{ data.measureDesign.issuerBelongSite}
                        adapter.mData[8].singleDisplayRightContent=if(data.measureDesign.issuerName==null) {
                            " " } else{ data.measureDesign.issuerName}
                        adapter.mData[9].singleDisplayRightContent=if(data.measureDesign.phone==null) {
                            " " } else{ data.measureDesign.phone}
                        adapter.mData[10].textAreaContent=if(data.measureDesign.remark==null) {
                            " " } else{ data.measureDesign.remark}
                        view.button_supply.setOnClickListener {
                            if(data.measureDesign.phone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getMeasureDesignPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
                        if(data.provideCrewLists==null)
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
                        adapter.mData[2].singleDisplayRightContent=if(data.caravanTransport.horseNumber==null) {
                            " " } else{ data.caravanTransport.horseNumber}
                        adapter.mData[3].singleDisplayRightContent=if(data.caravanTransport.validTime==null) {
                            " " } else{ data.caravanTransport.validTime}
                        adapter.mData[4].singleDisplayRightContent=if(data.caravanTransport.issuerName==null) {
                            " " } else{ data.caravanTransport.issuerName}
                        adapter.mData[5].singleDisplayRightContent=if(data.caravanTransport.phone==null) {
                            " " } else{ data.caravanTransport.phone}
                        adapter.mData[6].singleDisplayRightContent=if(data.caravanTransport.issuerBelongSite==null) {
                            " " } else{ data.caravanTransport.issuerBelongSite}
                        adapter.mData[7].textAreaContent=if(data.caravanTransport.remark==null) {
                            " " } else{ data.caravanTransport.remark}
                        view.button_supply.setOnClickListener {
                            if(data.caravanTransport.phone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getCaravanTransportPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
            Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal->{//桩基服务
                adapter = adapterGenerate.supplyTeamDisplayPile()
                val result = getSupplyPileFoundation(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.pileFoundation.name==null) {
                            " " } else{ data.pileFoundation.name }
                        if(data.provideCrewLists==null)
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
//                        if(data.ProvideTransportMachines==null)
//                        {
//                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
//                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
//                            })
//                        }
//                        else
//                        {
//                            //显示图片
//                            for(i in data.ProvideTransportMachines!!)
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
//                                        val intent = Intent(activity, ImageDisplayActivity::class.java)
//                                        intent.putExtra("imagePath", i.carPhotoPath)
//                                        startActivity(intent)
//                                    })
//                                }
//                            }
//                        }
                        val implementationRanges = data.implementationRanges
                        if(implementationRanges!=null)
                            adapter.mData[3].singleDisplayRightContent=implementationRanges.name
                        adapter.mData[4].singleDisplayRightContent=if(data.pileFoundation.workDia==null) {
                            " " } else{ data.pileFoundation.workDia }
                        adapter.mData[5].singleDisplayRightContent=if(data.pileFoundation.location==null) {
                            " " } else{ data.pileFoundation.location }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putString("type","供应工器具清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
                        adapter.mData[7].singleDisplayRightContent=if(data.pileFoundation.validTime==null) {
                            " " } else{ data.pileFoundation.validTime}
                        adapter.mData[8].singleDisplayRightContent=if(data.pileFoundation.issuerName==null) {
                            " " } else{ data.pileFoundation.issuerName}
                        adapter.mData[9].singleDisplayRightContent=if(data.pileFoundation.phone==null) {
                            " " } else{ data.pileFoundation.phone}
                        adapter.mData[10].singleDisplayRightContent=if(data.pileFoundation.issuerBelongSite==null) {
                            " " } else{ data.pileFoundation.issuerBelongSite}
                        adapter.mData[11].textAreaContent=if(data.pileFoundation.remark==null) {
                            " " } else{ data.pileFoundation.remark}
                        view.button_supply.setOnClickListener {
                            if(data.pileFoundation.phone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getPileFoundationPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
            Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal->{//非开挖
                adapter = adapterGenerate.supplyTeamDisplayTrenchiless()
                val result = getSupplyUnexcavation(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name }
                        if(data.provideCrewLists==null)
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
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
//                                        val intent = Intent(activity, ImageDisplayActivity::class.java)
//                                        intent.putExtra("imagePath", i.carPhotoPath)
//                                        startActivity(intent)
//                                    })
//                                }
//                            }
//                        }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putString("type","供应工器具清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")

                                }
                            })
                        }
                        adapter.mData[4].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[5].singleDisplayRightContent=if(data.issuerName==null) {
                            " " } else{ data.issuerName}
                        adapter.mData[6].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[7].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite}
                        adapter.mData[8].textAreaContent=if(data.remark==null) {
                            " " } else{ data.remark}
                        view.button_supply.setOnClickListener {
                            if(data.phone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getUnexcavationPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
            Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal->{//试验调试
                adapter = adapterGenerate.supplyTeamDisplayTestAndDebugging()
                val result = getSupplyTest(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name }
                        if(data.provideCrewLists==null)
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //供应运输清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //供应运输清册查看
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
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
//                                        val intent = Intent(activity, ImageDisplayActivity::class.java)
//                                        intent.putExtra("imagePath", i.carPhotoPath)
//                                        startActivity(intent)
//                                    })
//                                }
//                            }
//                        }
                        var str=""
                        for(i in data.voltages!!)
                        {
                            if(str!="")
                                str+="、"
                            str+="${i.voltageDegree}"
                        }
                        adapter.mData[3].singleDisplayRightContent=str
                        adapter.mData[4].singleDisplayRightContent=if(data.testWorkTypes==null) {
                            " " } else{ data.testWorkTypes }
                        adapter.mData[5].singleDisplayRightContent=if(data.operateDegree==null) {
                            " " } else{  data.operateDegree }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putString("type","供应工器具清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
                        adapter.mData[7].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[8].singleDisplayRightContent=if(data.issuerName==null) {
                            " " } else{ data.issuerName}
                        adapter.mData[9].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[10].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite}
                        adapter.mData[11].textAreaContent=if(data.remark==null) {
                            " " } else{ data.remark}
                        view.button_supply.setOnClickListener {
                            if(data.phone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getTestTeamPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
            Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal->{//跨越架
                adapter = adapterGenerate.supplyTeamDisplayCrossFrame()
                val result = getSupplySpanWoodenSupprt(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name }
                        if(data.provideCrewLists==null)
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //车辆清册
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putString("type","供应运输清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
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
//                                        val intent = Intent(activity, ImageDisplayActivity::class.java)
//                                        intent.putExtra("imagePath", i.carPhotoPath)
//                                        startActivity(intent)
//                                    })
//                                }
//                            }
//                        }
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[3].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putString("type","供应工器具清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
                        adapter.mData[4].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[5].singleDisplayRightContent=if(data.issuerName==null) {
                            " " } else{ data.issuerName}
                        adapter.mData[6].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[7].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite}
                        adapter.mData[8].textAreaContent=if(data.remark==null) {
                            " " } else{ data.remark}
                        view.button_supply.setOnClickListener {
                            if(data.phone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getSpanWoodenSupprtPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
            Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal->{//运行维护
                adapter = adapterGenerate.supplyTeamDisplayOperationAndMaintenance()
                val result = getSupplyRunningMaintain(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                        var data=it.message
                        adapter.mData[0].singleDisplayRightContent=if(data.name==null) {
                            " " } else{ data.name }
                        if(data.provideCrewLists==null)
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener { //供应人员清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener {  //供应人员清册查看
                                if(data.provideCrewLists!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideCrewLists
                                    mdata.putSerializable("listData7", listData as Serializable)
                                    mdata.putString("type","供应人员清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }

                        if(data.provideTransportMachines==null)
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener { //车辆清册
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        else{
                            adapter.mData[2].buttonListener = listOf(View.OnClickListener {  //车辆清册
                                if(data.provideTransportMachines!!.isEmpty())
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.provideTransportMachines
                                    mdata.putSerializable("listData8", listData as Serializable)
                                    mdata.putInt("type",8)
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
//                        if(data.ProvideTransportMachines==null)
//                        {
//                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
//                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
//                            })
//                        }
//                        else
//                        {
//                            //显示图片
//                            for(i in data.ProvideTransportMachines!!)
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
//                                        val intent = Intent(activity, ImageDisplayActivity::class.java)
//                                        intent.putExtra("imagePath", i.carPhotoPath)
//                                        startActivity(intent)
//                                    })
//                                }
//                            }
//                        }
                        if(data.voltages==null){
                            adapter.mData[3].singleDisplayRightContent=""
                        }else{
                            var str=""
                            for(i in data.voltages!!)
                            {
                                if(str!="")
                                    str+="、"
                                str+="${i.voltageDegree}"
                            }
                            adapter.mData[3].singleDisplayRightContent=str
                        }
                        adapter.mData[4].singleDisplayRightContent=if(data.implementationRanges==null) {
                            " " } else{  data.implementationRanges }
                        adapter.mData[5].singleDisplayRightContent=if(data.workTerritory==null) {
                            " " } else{  data.workTerritory}
                        if(data.constructionToolLists==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                //供应工器具清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {  //供应工器具清册查看
                                if(data.constructionToolLists!!.isEmpty())  Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.constructionToolLists
                                    mdata.putSerializable("listData9", listData as Serializable)
                                    mdata.putString("type","供应工器具清册查看")
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
                        adapter.mData[7].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime}
                        adapter.mData[8].singleDisplayRightContent=if(data.issuerName==null) {
                            " " } else{ data.issuerName}
                        adapter.mData[9].singleDisplayRightContent=if(data.phone==null) {
                            " " } else{ data.phone}
                        adapter.mData[10].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite}
                        adapter.mData[11].textAreaContent=if(data.remark==null) {
                            " " } else{ data.remark}
                        view.button_supply.setOnClickListener {
                            if(data.phone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getRunningMaintainPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
                        if(data.salaryUnit=="面议"||data.money=="-1.00"){
                            adapter.mData[9].singleDisplayRightContent= "面议"
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
                            adapter.mData[14].jumpListener = View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            }
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[14].jumpListener = View.OnClickListener {
                                  val intent = Intent(activity, ImageDisplayActivity::class.java)
                                     intent.putExtra("imagePath", data.carTable.carPhotoPath)
                                    startActivity(intent)
                                  }
                        }
                        adapter.mData[15].singleDisplayRightContent=if(data.validTime==null) {
                            " " } else{ data.validTime }
                        adapter.mData[16].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite }
                        adapter.mData[17].textAreaContent=if(data.comment==null) {
                            " " } else{ data.comment }
                        view.button_supply.setOnClickListener {
                            if(data.contactPhone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getLeaseCarPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
                        if(data.companyCredential.businessLicensePath==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, ImageDisplayActivity::class.java)
                                intent.putExtra("imagePath", data.companyCredential.businessLicensePath)
                                startActivity(intent)
                            })
                        }
                            if(data.leaseList==null)
                            {
                                adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                    //工器具
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                })
                            }
                            else{
                                adapter.mData[7].buttonListener = listOf(View.OnClickListener {  //工器具出租清册
                                    if(data.leaseList!!.isEmpty())   Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                    else
                                    {
                                        val listData = data.leaseList
                                        mdata.putString("type","工器具租赁清册查看")
                                        mdata.putString("type","设备租赁清册查看")
                                        mdata.putString("type","机械租赁清册查看")
                                        mdata.putSerializable("listData10",listData as Serializable)
                                        FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                    }
                                })
                            }
                        if(data.leaseConstructionTool.leaseConTractPath==null)
                        {
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, ImageDisplayActivity::class.java)
                                intent.putExtra("imagePath", data.leaseConstructionTool.leaseConTractPath)
                                startActivity(intent)
                            })
                        }
                        if(data.leaseConstructionTool.isDistribution!=null){
                            when(data.leaseConstructionTool.isDistribution){
                                "true"->{adapter.mData[9].singleDisplayRightContent="配送"}
                                "false"->{adapter.mData[9].singleDisplayRightContent="不配送"}
                                else->{adapter.mData[9].singleDisplayRightContent=""}
                            }
                        }

                        adapter.mData[10].singleDisplayRightContent=if(data.leaseConstructionTool.conveyancePropertyInsurance==null) {
                            " " } else{ data.leaseConstructionTool.conveyancePropertyInsurance }
                        adapter.mData[11].singleDisplayRightContent=if(data.leaseConstructionTool.validTime==null) {
                            " " } else{ data.leaseConstructionTool.validTime }
                        adapter.mData[12].singleDisplayRightContent=if(data.leaseConstructionTool.contact==null) {
                            " " } else{ data.leaseConstructionTool.contact }
                        adapter.mData[13].singleDisplayRightContent=if(data.leaseConstructionTool.contactPhone==null) {
                            " " } else{ data.leaseConstructionTool.contactPhone }
                        adapter.mData[14].singleDisplayRightContent=if(data.leaseConstructionTool.issuerBelongSite==null) {
                            " " } else{ data.leaseConstructionTool.issuerBelongSite }
                        view.button_supply.setOnClickListener {
                            if(data.leaseConstructionTool.contactPhone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getLeaseConstructionToolPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
                        if(data.companyCredential.businessLicensePath==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, ImageDisplayActivity::class.java)
                                intent.putExtra("imagePath", data.companyCredential.businessLicensePath)
                                startActivity(intent)
                            })
                        }
                        if(data.leaseList==null)
                        {
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                //设备租赁清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {  //设备租赁清册查看
                                if(data.leaseList!!.isEmpty())   Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.leaseList
                                    mdata.putString("type","设备租赁清册查看")
                                    mdata.putSerializable("listData10",listData as Serializable)
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
                        if(data.leaseFacility.leaseConTractPath==null)
                        {
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, ImageDisplayActivity::class.java)
                                intent.putExtra("imagePath", data.leaseFacility.leaseConTractPath)
                                startActivity(intent)
                            })
                        }
                        if(data.leaseFacility.isDistribution!=null){
                            when(data.leaseFacility.isDistribution){
                                "true"->{adapter.mData[9].singleDisplayRightContent="配送"}
                                "false"->{adapter.mData[9].singleDisplayRightContent="不配送"}
                                else->{adapter.mData[9].singleDisplayRightContent=""}
                            }
                        }

                        adapter.mData[10].singleDisplayRightContent=if(data.leaseFacility.conveyancePropertyInsurance==null) {
                            " " } else{ data.leaseFacility.conveyancePropertyInsurance }
                        adapter.mData[11].singleDisplayRightContent=if(data.leaseFacility.validTime==null) {
                            " " } else{ data.leaseFacility.validTime }
                        adapter.mData[12].singleDisplayRightContent=if(data.leaseFacility.contact==null) {
                            " " } else{ data.leaseFacility.contact }
                        adapter.mData[13].singleDisplayRightContent=if(data.leaseFacility.contactPhone==null) {
                            " " } else{ data.leaseFacility.contactPhone }
                        adapter.mData[14].singleDisplayRightContent=if(data.leaseFacility.issuerBelongSite==null) {
                            " " } else{ data.leaseFacility.issuerBelongSite }
                        view.button_supply.setOnClickListener {
                            if(data.leaseFacility.contactPhone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getLeaseFacilityPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
                        if(data.companyCredential.businessLicensePath==null)
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, ImageDisplayActivity::class.java)
                                intent.putExtra("imagePath", data.companyCredential.businessLicensePath)
                                startActivity(intent)
                            })
                        }
                        if(data.leaseList==null)
                        {
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                //机械租赁清册查看
                                Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else{
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {  //机械租赁清册查看
                                if(data.leaseList!!.isEmpty())   Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                else
                                {
                                    val listData = data.leaseList
                                    mdata.putString("type","机械租赁清册查看")
                                    mdata.putSerializable("listData10",listData as Serializable)
                                    FragmentHelper.switchFragment(activity!!,ProjectListFragment.newInstance(mdata),R.id.frame_display_supply,"")
                                }
                            })
                        }
                        if(data.leaseMachinery.leaseConTractPath==null)
                        {
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[8].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, ImageDisplayActivity::class.java)
                                intent.putExtra("imagePath", data.leaseMachinery.leaseConTractPath)
                                startActivity(intent)
                            })
                        }
                        if(data.leaseMachinery.isDistribution!=null){
                            when(data.leaseMachinery.isDistribution){
                                "true"->{adapter.mData[9].singleDisplayRightContent="配送"}
                                "false"->{adapter.mData[9].singleDisplayRightContent="不配送"}
                                else->{adapter.mData[9].singleDisplayRightContent=""}
                            }
                        }

                        adapter.mData[10].singleDisplayRightContent=if(data.leaseMachinery.conveyancePropertyInsurance==null) {
                            " " } else{ data.leaseMachinery.conveyancePropertyInsurance }
                        adapter.mData[11].singleDisplayRightContent=if(data.leaseMachinery.validTime==null) {
                            " " } else{ data.leaseMachinery.validTime }
                        adapter.mData[12].singleDisplayRightContent=if(data.leaseMachinery.contact==null) {
                            " " } else{ data.leaseMachinery.contact }
                        adapter.mData[13].singleDisplayRightContent=if(data.leaseMachinery.contactPhone==null) {
                            " " } else{ data.leaseMachinery.contactPhone }
                        adapter.mData[14].singleDisplayRightContent=if(data.leaseMachinery.issuerBelongSite==null) {
                            " " } else{ data.leaseMachinery.issuerBelongSite }
                        view.button_supply.setOnClickListener {
                            if(data.leaseMachinery.contactPhone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getLeaseMachineryPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
//                        if(data.companyCredential.legalPersonIdCardPath==null)
//                        {
//                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
//                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
//                            })
//                        }
//                        else
//                        {
//                            //显示图片
//                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
//                                val intent = Intent(activity, ImageDisplayActivity::class.java)
//                                intent.putExtra("imagePath", data.companyCredential.legalPersonIdCardPath)
//                                startActivity(intent)
//                            })
//                        }
                        if(data.companyCredential.businessLicensePath==null||data.companyCredential.businessLicensePath=="")
                        {
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[6].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, ImageDisplayActivity::class.java)
                                intent.putExtra("imagePath", data.companyCredential.businessLicensePath)
                                startActivity(intent)
                            })
                        }
                        if(data.thirdServicesContractPath==null)
                        {
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(context,"无图片",Toast.LENGTH_SHORT).show()
                            })
                        }
                        else
                        {
                            //显示图片
                            adapter.mData[7].buttonListener = listOf(View.OnClickListener {
                                val intent = Intent(activity, ImageDisplayActivity::class.java)
                                intent.putExtra("imagePath", data.thirdServicesContractPath)
                                startActivity(intent)
                            })
                        }

                            adapter.mData[8].singleDisplayRightContent=if(data.validTime==null) {
                                " " } else{ data.validTime }
                        adapter.mData[9].singleDisplayRightContent=if(data.contact==null) {
                            " " } else{ data.contact }
                        adapter.mData[10].singleDisplayRightContent=if(data.contactPhone==null) {
                            " " } else{ data.contactPhone }
                           adapter.mData[11].singleDisplayRightContent=if(data.issuerBelongSite==null) {
                            " " } else{ data.issuerBelongSite }
                        adapter.mData[12].textAreaContent=if(data.businessScope==null) {
                            " " } else{ data.businessScope }
                        view.button_supply.setOnClickListener {
                            if(data.contactPhone!=null)
                            {
                                val loadingDialog = LoadingDialog(context!!, "请稍等...", R.mipmap.ic_dialog_loading)
                                loadingDialog.show()
                                val result = getThirdPhoneDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                        val  phone = it.message
                                        loadingDialog.dismiss()
                                        if(it.code=="200"&&it.desc=="OK"){
                                            val dialog = AlertDialog.Builder(this.context)
                                                .setTitle("对方联系电话:")
                                                .setMessage(phone)
                                                .setNegativeButton("联系对方") { dialog, which ->
                                                    val intent = Intent(Intent.ACTION_DIAL)
                                                    intent.setData(Uri.parse("tel:${phone}"))
                                                    startActivity(intent)
                                                }
                                                .setPositiveButton("取消")  { dialog, which ->
                                                    dialog.dismiss() }.create()
                                            dialog.show()
                                        }else{
                                            ToastHelper.mToast(context!!,"获取失败")
                                        }

                                    },{
                                        loadingDialog.dismiss()
                                        ToastHelper.mToast(context!!,"获取异常")
                                        it.printStackTrace()
                                    })
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
//            Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal->{//资质合作
//                adapter = adapterGenerate.supplyTeamDisplayDemandTripartiteCooperation()
//                val result = getSupplyThirdPartyDetail(id, UnSerializeDataBase.userToken, UnSerializeDataBase.dmsBasePath).subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread()).subscribe ({
//                            var data=it.message
//                            adapter.mData[0].singleDisplayRightContent=if(data.serveType==null) {
//                                " " } else{ data.serveType }
//                            adapter.mData[1].singleDisplayRightContent=if(data.cooperationObject==null) {
//                                " " } else{ data.cooperationObject }
//                            adapter.mData[2].singleDisplayRightContent=if(data.companyName==null) {
//                                " " } else{ data.companyName }
//                            adapter.mData[3].singleDisplayRightContent=if(data.companySite==null) {
//                                " " } else{ data.companySite }
//                            adapter.mData[4].singleDisplayRightContent=if(data.legalPersonName==null) {
//                                " " } else{ data.legalPersonName }
//                            adapter.mData[5].singleDisplayRightContent=if(data.cooperationAreas==null) {
//                                " " } else{ data.cooperationAreas }
//                            adapter.mData[6].singleDisplayRightContent=if(data.qualificationCondition==null) {
//                                " " } else{ data.qualificationCondition }
//                            adapter.mData[7].singleDisplayRightContent=if(data.issuerBelongSite==null) {
//                                " " } else{ data.issuerBelongSite }
//                            adapter.mData[9].singleDisplayRightContent=if(data.contact==null) {
//                                " " } else{ data.contact }
//                            adapter.mData[10].singleDisplayRightContent=if(data.contactPhone==null) {
//                                " " } else{ "联系对方时可见"}
//                            adapter.mData[11].submitListener = View.OnClickListener {
//                                if(data.contactPhone!=null)
//                                {
//                                    var dialog = AlertDialog.Builder(this.context)
//                                            .setTitle("对方联系电话:")
//                                            .setMessage(data.contactPhone)
//                                            .setNegativeButton("联系对方") { dialog, which ->
//                                                val intent = Intent(Intent.ACTION_DIAL)
//                                                intent.setData(Uri.parse("tel:${data.contactPhone}"))
//                                                startActivity(intent)
//                                            }
//                                            .setPositiveButton("确定") { dialog, which ->
//                                                dialog.dismiss() }.create()
//                                    dialog.show()
//                                }
//                                else{
//                                    Toast.makeText(context,"对方未留联系方式",Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                            view.rv_supply_display_content.adapter = adapter
//                            view.rv_supply_display_content.layoutManager = LinearLayoutManager(view.context)
//                        },{
//                            it.printStackTrace()
//                        })
//            }

        }

    }
}